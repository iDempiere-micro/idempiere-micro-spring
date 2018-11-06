package org.compiere.invoicing;

import org.compiere.accounting.MWarehouse;
import org.compiere.docengine.DocumentEngine;
import org.compiere.model.IDoc;
import org.compiere.model.IPODoc;
import org.compiere.model.I_M_InOutLineConfirm;
import org.compiere.order.X_M_InOutConfirm;
import org.compiere.orm.MDocType;
import org.compiere.orm.Query;
import org.compiere.orm.X_C_DocType;
import org.compiere.process.CompleteActionResult;
import org.compiere.process.DocAction;
import org.compiere.util.Msg;
import org.compiere.validation.ModelValidationEngine;
import org.compiere.validation.ModelValidator;
import org.idempiere.common.exceptions.AdempiereException;
import org.idempiere.common.util.Env;
import org.idempiere.orm.PO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

public class MInOutConfirm extends org.compiere.order.MInOutConfirm implements DocAction, IPODoc {
    /**************************************************************************
     * 	Standard Constructor
     *	@param ctx context
     *	@param M_InOutConfirm_ID id
     *	@param trxName transaction
     */
    public MInOutConfirm (Properties ctx, int M_InOutConfirm_ID, String trxName) {
        super(ctx, M_InOutConfirm_ID, trxName);
    }

    /**
     * 	Parent Constructor
     *	@param ship shipment
     *	@param confirmType confirmation type
     */
    public MInOutConfirm (MInOut ship, String confirmType) {
        super(ship, confirmType);
    }



    /**	Confirm Lines					*/
    private MInOutLineConfirm[]	m_lines = null;
    /** Credit Memo to create			*/
    private MInvoice			m_creditMemo = null;
    /**	Physical Inventory to create	*/
    private MInventory			m_inventory = null;

    /**
     * 	Get Lines
     *	@param requery requery
     *	@return array of lines
     */
    public MInOutLineConfirm[] getLines (boolean requery)
    {
        if (m_lines != null && !requery) {
            PO.set_TrxName(m_lines, get_TrxName());
            return m_lines;
        }
        final String whereClause = I_M_InOutLineConfirm.COLUMNNAME_M_InOutConfirm_ID+"=?";
        List<MInOutLineConfirm> list = new Query(getCtx(), I_M_InOutLineConfirm.Table_Name, whereClause, get_TrxName())
            .setParameters(getM_InOutConfirm_ID())
            .list();
        m_lines = new MInOutLineConfirm[list.size ()];
        list.toArray (m_lines);
        return m_lines;
    }	//	getLines

    /**************************************************************************
     * 	Process document
     *	@param processAction document action
     *	@return true if performed
     */
    public boolean processIt (String processAction)
    {
        m_processMsg = null;
        DocumentEngine engine = new DocumentEngine (this, getDocStatus());
        return engine.processIt (processAction, getDocAction());
    }	//	processIt

    /**	Process Message 			*/
    private String		m_processMsg = null;

    /**
     *	Prepare Document
     * 	@return new status (In Progress or Invalid)
     */
    public String prepareIt()
    {
        if (log.isLoggable(Level.INFO)) log.info(toString());
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
        if (m_processMsg != null)
            return DocAction.Companion.getSTATUS_Invalid();

        /**
         MDocType dt = MDocType.get(getCtx(), getC_DocTypeTarget_ID());

         //	Std Period open?
         if (!MPeriod.isOpen(getCtx(), getDateAcct(), dt.getDocBaseType()))
         {
         m_processMsg = "@PeriodClosed@";
         return DocAction.STATUS_Invalid;
         }
         **/

        MInOutLineConfirm[] lines = getLines(true);
        if (lines.length == 0)
        {
            m_processMsg = "@NoLines@";
            return DocAction.Companion.getSTATUS_Invalid();
        }
        //	Set dispute if not fully confirmed
        boolean difference = false;
        for (int i = 0; i < lines.length; i++)
        {
            if (!lines[i].isFullyConfirmed())
            {
                difference = true;
                break;
            }
        }
        setIsInDispute(difference);

        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_PREPARE);
        if (m_processMsg != null)
            return DocAction.Companion.getSTATUS_Invalid();
        //
        m_justPrepared = true;
        if (!X_M_InOutConfirm.DOCACTION_Complete.equals(getDocAction()))
            setDocAction(X_M_InOutConfirm.DOCACTION_Complete);
        return DocAction.Companion.getSTATUS_InProgress();
    }	//	prepareIt


    /**
     * 	Complete Document
     * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
     */
    public CompleteActionResult completeIt()
    {
        //	Re-Check
        if (!m_justPrepared)
        {
            String status = prepareIt();
            m_justPrepared = false;
            if (!DocAction.Companion.getSTATUS_InProgress().equals(status))
                return new CompleteActionResult(status);
        }

        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
        if (m_processMsg != null)
            return new CompleteActionResult(DocAction.Companion.getSTATUS_Invalid());

        //	Implicit Approval
        if (!isApproved())
            approveIt();
        if (log.isLoggable(Level.INFO)) log.info(toString());
        //
        MInOut inout = new MInOut(getCtx(), getM_InOut_ID(), get_TrxName());
        MInOutLineConfirm[] lines = getLines(false);

        //	Check if we need to split Shipment
        if (isInDispute())
        {
            MDocType dt = MDocType.get(getCtx(), inout.getC_DocType_ID());
            if (dt.isSplitWhenDifference())
            {
                if (dt.getC_DocTypeDifference_ID() == 0)
                {
                    m_processMsg = "No Split Document Type defined for: " + dt.getName();
                    return new CompleteActionResult(DocAction.Companion.getSTATUS_Invalid());
                }
                splitInOut (inout, dt.getC_DocTypeDifference_ID(), lines);
                m_lines = null;
            }
        }

        //	All lines
        for (int i = 0; i < lines.length; i++)
        {
            MInOutLineConfirm confirmLine = lines[i];
            confirmLine.set_TrxName(get_TrxName());
            if (!confirmLine.processLine (inout.isSOTrx(), getConfirmType()))
            {
                m_processMsg = "ShipLine not saved - " + confirmLine;
                return new CompleteActionResult(DocAction.Companion.getSTATUS_Invalid());
            }
            if (confirmLine.isFullyConfirmed())
            {
                confirmLine.setProcessed(true);
                confirmLine.saveEx();
            }
            else
            {
                if (createDifferenceDoc (inout, confirmLine))
                {
                    confirmLine.setProcessed(true);
                    confirmLine.saveEx();
                }
                else
                {
                    log.log(Level.SEVERE, "Scrapped=" + confirmLine.getScrappedQty()
                        + " - Difference=" + confirmLine.getDifferenceQty());
                    return new CompleteActionResult(DocAction.Companion.getSTATUS_Invalid());
                }
            }
        }	//	for all lines

        if (m_creditMemo != null)
            m_processMsg += " @C_Invoice_ID@=" + m_creditMemo.getDocumentNo();
        if (m_inventory != null)
            m_processMsg += " @M_Inventory_ID@=" + m_inventory.getDocumentNo();


        //	Try to complete Shipment
        //	if (inout.processIt(DocAction.ACTION_Complete))
        //		m_processMsg = "@M_InOut_ID@ " + inout.getDocumentNo() + ": @Completed@";

        //	User Validation
        String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
        if (valid != null)
        {
            m_processMsg = valid;
            return new CompleteActionResult(DocAction.Companion.getSTATUS_Invalid());
        }

        setProcessed(true);
        setDocAction(X_M_InOutConfirm.DOCACTION_Close);
        return new CompleteActionResult(DocAction.Companion.getSTATUS_Completed());
    }	//	completeIt

    /**
     * 	Split Shipment into confirmed and dispute
     *	@param original original shipment
     *	@param C_DocType_ID target DocType
     *	@param confirmLines confirm lines
     */
    private void splitInOut (MInOut original, int C_DocType_ID, MInOutLineConfirm[] confirmLines)
    {
        MInOut split = null;
        //	Go through confirmations
        for (int i = 0; i < confirmLines.length; i++)
        {
            MInOutLineConfirm confirmLine = confirmLines[i];
            BigDecimal differenceQty = confirmLine.getDifferenceQty();
            if (differenceQty.compareTo(Env.ZERO) == 0)
                continue;
            //
            org.compiere.order.MInOutLine oldLine = confirmLine.getLine();
            if (log.isLoggable(Level.FINE)) log.fine("Qty=" + differenceQty + ", Old=" + oldLine);
            //
            // Create Header
            if (split == null)
            {
                split = new MInOut(original, C_DocType_ID, original.getMovementDate());
                StringBuilder msgd = new StringBuilder("Splitted from ").append(original.getDocumentNo());
                split.addDescription(msgd.toString());
                split.setIsInDispute(true);
                split.saveEx();
                msgd = new StringBuilder("Split: ").append(split.getDocumentNo());
                original.addDescription(msgd.toString());
                original.saveEx();
            }
            //
            org.compiere.order.MInOutLine splitLine = new org.compiere.order.MInOutLine(split);
            splitLine.setC_OrderLine_ID(oldLine.getC_OrderLine_ID());
            splitLine.setC_UOM_ID(oldLine.getC_UOM_ID());
            splitLine.setDescription(oldLine.getDescription());
            splitLine.setIsDescription(oldLine.isDescription());
            splitLine.setLine(oldLine.getLine());
            splitLine.setM_AttributeSetInstance_ID(oldLine.getMAttributeSetInstance_ID());
            splitLine.setM_Locator_ID(oldLine.getM_Locator_ID());
            splitLine.setM_Product_ID(oldLine.getM_Product_ID());
            splitLine.setM_Warehouse_ID(oldLine.getM_Warehouse_ID());
            splitLine.setRef_InOutLine_ID(oldLine.getRef_InOutLine_ID());
            StringBuilder msgd = new StringBuilder("Split: from ").append(oldLine.getMovementQty());
            splitLine.addDescription(msgd.toString());
            //	Qtys
            splitLine.setQty(differenceQty);		//	Entered/Movement
            splitLine.saveEx();
            //	Old
            msgd = new StringBuilder("Splitted: from ").append(oldLine.getMovementQty());
            oldLine.addDescription(msgd.toString());
            oldLine.setQty(oldLine.getMovementQty().subtract(differenceQty));
            oldLine.saveEx();
            //	Update Confirmation Line
            confirmLine.setTargetQty(confirmLine.getTargetQty().subtract(differenceQty));
            confirmLine.setDifferenceQty(Env.ZERO);
            confirmLine.saveEx();
        }	//	for all confirmations

        // Nothing to split
        if (split == null)
        {
            return ;
        }

        m_processMsg = "Split @M_InOut_ID@=" + split.getDocumentNo()
            + " - @M_InOutConfirm_ID@=";

        MDocType dt = MDocType.get(getCtx(), original.getC_DocType_ID());
        if (!dt.isPrepareSplitDocument())
        {
            return ;
        }

        //	Create Dispute Confirmation
        if (!split.processIt(DocAction.Companion.getACTION_Prepare()))
            throw new AdempiereException(split.getProcessMsg());
        //	split.createConfirmation();
        split.saveEx();
        MInOutConfirm[] splitConfirms = split.getConfirmations(true);
        if (splitConfirms.length > 0)
        {
            int index = 0;
            if (splitConfirms[index].isProcessed())
            {
                if (splitConfirms.length > 1)
                    index++;	//	try just next
                if (splitConfirms[index].isProcessed())
                {
                    m_processMsg += splitConfirms[index].getDocumentNo() + " processed??";
                    return;
                }
            }
            splitConfirms[index].setIsInDispute(true);
            splitConfirms[index].saveEx();
            m_processMsg += splitConfirms[index].getDocumentNo();
            //	Set Lines to unconfirmed
            MInOutLineConfirm[] splitConfirmLines = splitConfirms[index].getLines(false);
            for (int i = 0; i < splitConfirmLines.length; i++)
            {
                MInOutLineConfirm splitConfirmLine = splitConfirmLines[i];
                splitConfirmLine.setScrappedQty(Env.ZERO);
                splitConfirmLine.setConfirmedQty(Env.ZERO);
                splitConfirmLine.saveEx();
            }
        }
        else
            m_processMsg += "??";

    }	//	splitInOut


    /**
     * 	Create Difference Document
     * 	@param inout shipment/receipt
     *	@param confirm confirm line
     *	@return true if created
     */
    private boolean createDifferenceDoc (org.compiere.order.MInOut inout, MInOutLineConfirm confirm)
    {
        if (m_processMsg == null)
            m_processMsg = "";
        else if (m_processMsg.length() > 0)
            m_processMsg += "; ";
        //	Credit Memo if linked Document
        if (confirm.getDifferenceQty().signum() != 0
            && !inout.isSOTrx() && inout.getRef_InOut_ID() != 0)
        {
            if (log.isLoggable(Level.INFO)) log.info("Difference=" + confirm.getDifferenceQty());
            if (m_creditMemo == null)
            {
                m_creditMemo = new MInvoice (inout, null);
                StringBuilder msgd = new StringBuilder().append(Msg.translate(getCtx(), "M_InOutConfirm_ID")).append(" ").append(getDocumentNo());
                m_creditMemo.setDescription(msgd.toString());
                m_creditMemo.setC_DocTypeTarget_ID(MDocType.DOCBASETYPE_APCreditMemo);
                m_creditMemo.saveEx();
                setC_Invoice_ID(m_creditMemo.getC_Invoice_ID());
            }
            MInvoiceLine line = new MInvoiceLine (m_creditMemo);
            line.setShipLine(confirm.getLine());
            if (confirm.getLine().getProduct() != null) {
                // use product UOM in case the shipment hasn't the same uom than the order
                line.setC_UOM_ID(confirm.getLine().getProduct().getC_UOM_ID());
            }
            // Note: confirmation is always in the qty according to the product UOM
            line.setQty(confirm.getDifferenceQty());	//	Entered/Invoiced
            line.saveEx();
            confirm.setC_InvoiceLine_ID(line.getC_InvoiceLine_ID());
        }

        //	Create Inventory Difference
        if (confirm.getScrappedQty().signum() != 0)
        {
            if (log.isLoggable(Level.INFO)) log.info("Scrapped=" + confirm.getScrappedQty());
            if (m_inventory == null)
            {
                MWarehouse wh = MWarehouse.get(getCtx(), inout.getM_Warehouse_ID());
                m_inventory = new MInventory (wh, get_TrxName());
                StringBuilder msgd = new StringBuilder().append(Msg.translate(getCtx(), "M_InOutConfirm_ID")).append(" ").append(getDocumentNo());
                m_inventory.setDescription(msgd.toString());
                setInventoryDocType(m_inventory);
                m_inventory.saveEx();
                setM_Inventory_ID(m_inventory.getM_Inventory_ID());
            }
            MInOutLine ioLine = confirm.getLine();
            MInventoryLine line = new MInventoryLine (m_inventory,
                ioLine.getM_Locator_ID(), ioLine.getM_Product_ID(), ioLine.getMAttributeSetInstance_ID(),
                confirm.getScrappedQty(), Env.ZERO);
            if (!line.save(get_TrxName()))
            {
                m_processMsg += "Inventory Line not created";
                return false;
            }
            confirm.setM_InventoryLine_ID(line.getM_InventoryLine_ID());
        }

        //
        if (!confirm.save(get_TrxName()))
        {
            m_processMsg += "Confirmation Line not saved";
            return false;
        }
        return true;
    }	//	createDifferenceDoc


    /**
     * @param inventory
     */
    private void setInventoryDocType(MInventory inventory) {
        MDocType[] doctypes = MDocType.getOfDocBaseType(Env.getCtx(), X_C_DocType.DOCBASETYPE_MaterialPhysicalInventory);
        for(MDocType doctype : doctypes)
        {
            if (X_C_DocType.DOCSUBTYPEINV_PhysicalInventory.equals(doctype.getDocSubTypeInv()))
            {
                inventory.setC_DocType_ID(doctype.getC_DocType_ID());
                break;
            }
        }
    }


    /**
     * 	Void Document.
     * 	@return false
     */
    public boolean voidIt()
    {
        if (log.isLoggable(Level.INFO)) log.info(toString());
        // Before Void
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_VOID);
        if (m_processMsg != null)
            return false;

        if (X_M_InOutConfirm.DOCSTATUS_Closed.equals(getDocStatus())
            || X_M_InOutConfirm.DOCSTATUS_Reversed.equals(getDocStatus())
            || X_M_InOutConfirm.DOCSTATUS_Voided.equals(getDocStatus()))
        {
            m_processMsg = "Document Closed: " + getDocStatus();
            return false;
        }

        //	Not Processed
        if (X_M_InOutConfirm.DOCSTATUS_Drafted.equals(getDocStatus())
            || X_M_InOutConfirm.DOCSTATUS_Invalid.equals(getDocStatus())
            || X_M_InOutConfirm.DOCSTATUS_InProgress.equals(getDocStatus())
            || X_M_InOutConfirm.DOCSTATUS_Approved.equals(getDocStatus())
            || X_M_InOutConfirm.DOCSTATUS_NotApproved.equals(getDocStatus()) )
        {
            org.compiere.order.MInOut inout = (org.compiere.order.MInOut)getM_InOut();
            if (!org.compiere.order.MInOut.DOCSTATUS_Voided.equals(inout.getDocStatus())
                && !MInOut.DOCSTATUS_Reversed.equals(inout.getDocStatus()) )
            {
                throw new AdempiereException("@M_InOut_ID@ @DocStatus@<>VO");
            }
            for (MInOutLineConfirm confirmLine : getLines(true))
            {
                confirmLine.setTargetQty(Env.ZERO);
                confirmLine.setConfirmedQty(Env.ZERO);
                confirmLine.setScrappedQty(Env.ZERO);
                confirmLine.setDifferenceQty(Env.ZERO);
                confirmLine.setProcessed(true);
                confirmLine.saveEx();
            }
            setIsCancelled(true);
        }
        else
        {
            return reverseCorrectIt();
        }

        // After Void
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_VOID);
        if (m_processMsg != null)
            return false;

        setProcessed(true);
        setDocAction(X_M_InOutConfirm.DOCACTION_None);

        return true;
    }	//	voidIt

    /**
     * 	Close Document.
     * 	@return true if success
     */
    public boolean closeIt()
    {
        if (log.isLoggable(Level.INFO)) log.info(toString());
        // Before Close
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_CLOSE);
        if (m_processMsg != null)
            return false;

        setDocAction(X_M_InOutConfirm.DOCACTION_None);

        // After Close
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_CLOSE);
        if (m_processMsg != null)
            return false;

        return true;
    }	//	closeIt

    /**
     * 	Reverse Correction
     * 	@return false
     */
    public boolean reverseCorrectIt()
    {
        if (log.isLoggable(Level.INFO)) log.info(toString());
        // Before reverseCorrect
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REVERSECORRECT);
        if (m_processMsg != null)
            return false;

        // After reverseCorrect
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REVERSECORRECT);
        if (m_processMsg != null)
            return false;

        return false;
    }	//	reverseCorrectionIt


    /**
     * 	Reverse Accrual - none
     * 	@return false
     */
    public boolean reverseAccrualIt()
    {
        if (log.isLoggable(Level.INFO)) log.info(toString());
        // Before reverseAccrual
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REVERSEACCRUAL);
        if (m_processMsg != null)
            return false;

        // After reverseAccrual
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REVERSEACCRUAL);
        if (m_processMsg != null)
            return false;

        return false;
    }	//	reverseAccrualIt

    /**
     * 	Re-activate
     * 	@return false
     */
    public boolean reActivateIt()
    {
        if (log.isLoggable(Level.INFO)) log.info(toString());
        // Before reActivate
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REACTIVATE);
        if (m_processMsg != null)
            return false;

        // After reActivate
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REACTIVATE);
        if (m_processMsg != null)
            return false;

        return false;
    }	//	reActivateIt


    /*************************************************************************
     * 	Get Summary
     *	@return Summary of Document
     */
    public String getSummary()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getDocumentNo());
        //	: Total Lines = 123.00 (#1)
        sb.append(": ")
            .append(Msg.translate(getCtx(),"ApprovalAmt")).append("=").append(getApprovalAmt())
            .append(" (#").append(getLines(false).length).append(")");
        //	 - Description
        if (getDescription() != null && getDescription().length() > 0)
            sb.append(" - ").append(getDescription());
        return sb.toString();
    }	//	getSummary

    /**
     * 	Get Process Message
     *	@return clear text error message
     */
    public String getProcessMsg()
    {
        return m_processMsg;
    }	//	getProcessMsg

    /**
     * 	Create Confirmation or return existing one
     *	@param ship shipment
     *	@param confirmType confirmation type
     *	@param checkExisting if false, new confirmation is created
     *	@return Confirmation
     */
    public static MInOutConfirm create (MInOut ship, String confirmType, boolean checkExisting)
    {
        if (checkExisting)
        {
            MInOutConfirm[] confirmations = ship.getConfirmations(false);
            for (int i = 0; i < confirmations.length; i++)
            {
                MInOutConfirm confirm = confirmations[i];
                if (confirm.getConfirmType().equals(confirmType))
                {
                    if (s_log.isLoggable(Level.INFO)) s_log.info("create - existing: " + confirm);
                    return confirm;
                }
            }
        }

        MInOutConfirm confirm = new MInOutConfirm(ship, confirmType);
        confirm.saveEx();
        MInOutLine[] shipLines = ship.getLines(false);
        for (int i = 0; i < shipLines.length; i++)
        {
            MInOutLine sLine = shipLines[i];
            MInOutLineConfirm cLine = new MInOutLineConfirm (confirm);
            cLine.setInOutLine(sLine);
            cLine.saveEx();
        }
        if (s_log.isLoggable(Level.INFO)) s_log.info("New: " + confirm);
        return confirm;
    }	//	MInOutConfirm


    /**
     * 	String Representation
     *	@return info
     */
    public String toString ()
    {
        StringBuilder sb = new StringBuilder ("MInOutConfirm[");
        sb.append(get_ID()).append("-").append(getSummary())
            .append ("]");
        return sb.toString ();
    }	//	toString

    /* Doc - To be used on ModelValidator to get the corresponding Doc from the PO */
    private IDoc m_doc;

    @Override
    public void setDoc(IDoc doc) {
        m_doc = doc;
    }

    @Override
    public void setProcessedOn(String processed, boolean b, boolean b1) {

    }
}