package org.compiere.order;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;
import org.compiere.model.I_C_InvoiceLine;
import org.compiere.model.I_C_Order;
import org.compiere.model.I_M_RMA;
import org.compiere.model.I_M_RMALine;
import org.compiere.orm.Query;
import org.compiere.product.MProduct;
import org.compiere.tax.ITaxProvider;
import org.compiere.tax.MTax;
import org.compiere.tax.MTaxCategory;
import org.compiere.tax.MTaxProvider;
import org.compiere.util.Msg;
import org.idempiere.common.exceptions.AdempiereException;
import org.idempiere.common.util.DB;
import org.idempiere.common.util.Env;
import org.idempiere.icommon.model.IPO;

/**
 * RMA Line Model
 *
 * @author Jorg Janke
 * @version $Id: MRMALine.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MRMALine extends X_M_RMALine implements I_M_RMALine {
  /** */
  private static final long serialVersionUID = -3459158383642518763L;

  /**
   * Standard Constructor
   *
   * @param ctx context
   * @param M_RMALine_ID id
   * @param trxName transaction
   */
  public MRMALine(Properties ctx, int M_RMALine_ID, String trxName) {
    super(ctx, M_RMALine_ID, trxName);
    if (M_RMALine_ID == 0) {
      setQty(Env.ONE);
      this.setQtyDelivered(Env.ZERO);
    }

    init();
  } //	MRMALine

  /**
   * Load Constructor
   *
   * @param ctx context
   * @param rs result set
   * @param trxName transaction
   */
  public MRMALine(Properties ctx, ResultSet rs, String trxName) {
    super(ctx, rs, trxName);
    init();
  } //	MRMALine

  /** Shipment Line */
  protected MInOutLine m_ioLine = null;
  /** Product */
  private MProduct m_product = null;
  /** Charge */
  private MCharge m_charge = null;
  /** Tax */
  private MTax m_tax = null;
  /** Parent */
  private MRMA m_parent = null;

  protected int precision = 0;
  protected BigDecimal unitAmount = Env.ZERO;
  protected BigDecimal originalQty = Env.ZERO;
  protected int taxId = 0;

  /** Initialise parameters that are required */
  protected void init() {
    getShipLine();

    if (m_ioLine != null) {
      // Get pricing details (Based on invoice if found, on order otherwise)
      //   --> m_ioLine.isInvoiced just work for sales orders - so it doesn't work for purchases
      if (m_ioLine.getC_OrderLine_ID() != 0) {
        MOrderLine orderLine =
            new MOrderLine(getCtx(), m_ioLine.getC_OrderLine_ID(), get_TrxName());
        precision = orderLine.getPrecision();
        unitAmount = orderLine.getPriceEntered();
        originalQty = orderLine.getQtyDelivered();
        taxId = orderLine.getC_Tax_ID();
      } else {
        throw new IllegalStateException(
            "No Invoice/Order line found the Shipment/Receipt line associated");
      }
    } else if (getC_Charge_ID() != 0) {
      MCharge charge = MCharge.get(this.getCtx(), getC_Charge_ID());
      unitAmount = charge.getChargeAmt();

      {
        I_C_Order order = getParent().getOriginalOrder();
        if (order != null) precision = order.getPrecision();
        else
          throw new IllegalStateException("No Invoice/Order found the Shipment/Receipt associated");
      }

      // Retrieve tax Exempt
      String sql =
          "SELECT C_Tax_ID FROM C_Tax WHERE AD_Client_ID=? AND IsActive='Y' "
              + "AND IsTaxExempt='Y' AND ValidFrom < SYSDATE ORDER BY IsDefault DESC";

      // Set tax for charge as exempt
      taxId = DB.getSQLValueEx(null, sql, Env.getADClientID(getCtx()));
      m_ioLine = null;
    }
  }

  /**
   * Get Parent
   *
   * @return parent
   */
  public I_M_RMA getParent() {
    if (m_parent == null) m_parent = new MRMA(getCtx(), getM_RMA_ID(), get_TrxName());
    return m_parent;
  } //  getParent

  /**
   * Set M_InOutLine_ID
   *
   * @param M_InOutLine_ID
   */
  public void setM_InOutLine_ID(int M_InOutLine_ID) {
    super.setM_InOutLine_ID(M_InOutLine_ID);
    m_ioLine = null;
  } //	setM_InOutLine_ID

  /**
   * Get Ship Line
   *
   * @return ship line
   */
  public MInOutLine getShipLine() {
    if ((m_ioLine == null || is_ValueChanged(I_M_RMALine.COLUMNNAME_M_InOutLine_ID))
        && getM_InOutLine_ID() != 0)
      m_ioLine = new MInOutLine(getCtx(), getM_InOutLine_ID(), get_TrxName());
    return m_ioLine;
  } //	getShipLine

  /**
   * Retrieves the invoiceLine Id associated with the Shipment/Receipt Line
   *
   * @return Invoice Line ID
   */
  protected int getInvoiceLineId() {
    int invoiceLine_ID =
        new Query(getCtx(), I_C_InvoiceLine.Table_Name, "M_InOutLine_ID=?", get_TrxName())
            .setParameters(getM_InOutLine_ID())
            .firstId();
    return invoiceLine_ID <= 0 ? 0 : invoiceLine_ID;
  }

  /**
   * Calculates the unit amount for the product/charge
   *
   * @return Unit Amount
   */
  public BigDecimal getUnitAmt() {
    return unitAmount;
  }

  /**
   * Get Total Amt for the line including tax
   *
   * @return amt
   */
  public BigDecimal getTotalAmt() {
    BigDecimal bd = getAmt().multiply(getQty());

    boolean documentLevel = getTax().isDocumentLevel();

    //	juddm: Tax Exempt & Tax Included in Price List & not Document Level - Adjust Line Amount
    //  http://sourceforge.net/tracker/index.php?func=detail&aid=1733602&group_id=176962&atid=879332
    if (getParent().isTaxIncluded() && !documentLevel) {
      BigDecimal taxStdAmt = Env.ZERO, taxThisAmt = Env.ZERO;

      MTax orderTax = getTax();
      MTax stdTax = null;

      //	get the standard tax
      if (getProduct() == null) {
        if (getCharge() != null) // Charge
        {
          stdTax =
              new MTax(
                  getCtx(),
                  ((MTaxCategory) getCharge().getC_TaxCategory()).getDefaultTax().getC_Tax_ID(),
                  get_TrxName());
        }

      } else // Product
      stdTax =
            new MTax(
                getCtx(),
                ((MTaxCategory) getProduct().getC_TaxCategory()).getDefaultTax().getC_Tax_ID(),
                get_TrxName());

      if (stdTax != null) {
        if (log.isLoggable(Level.FINE)) {
          log.fine("stdTax rate is " + stdTax.getRate());
          log.fine("orderTax rate is " + orderTax.getRate());
        }

        taxThisAmt =
            taxThisAmt.add(orderTax.calculateTax(bd, getParent().isTaxIncluded(), getPrecision()));
        taxStdAmt =
            taxStdAmt.add(stdTax.calculateTax(bd, getParent().isTaxIncluded(), getPrecision()));

        bd = bd.subtract(taxStdAmt).add(taxThisAmt);

        if (log.isLoggable(Level.FINE))
          log.fine(
              "Price List includes Tax and Tax Changed on Order Line: New Tax Amt: "
                  + taxThisAmt
                  + " Standard Tax Amt: "
                  + taxStdAmt
                  + " Line Net Amt: "
                  + bd);
      }
    }
    int precision = getPrecision();
    if (bd.scale() > precision) bd = bd.setScale(precision, BigDecimal.ROUND_HALF_UP);

    return bd;
  } //  getAmt

  /**
   * Get whether the Ship line has been invoiced
   *
   * @return true if invoiced
   */
  public boolean isShipLineInvoiced() {
    return (getInvoiceLineId() != 0);
  }

  @Override
  protected boolean beforeSave(boolean newRecord) {
    if (newRecord && getParent().isComplete()) {
      log.saveError("ParentComplete", Msg.translate(getCtx(), "M_RMA"));
      return false;
    }
    if (getM_InOutLine_ID() == 0 && getC_Charge_ID() == 0 && getM_Product_ID() == 0) {
      log.saveError("FillShipLineOrProductOrCharge", "");
      return false;
    }

    if (getM_Product_ID() != 0 && getC_Charge_ID() != 0) {
      log.saveError("JustProductOrCharge", "");
      return false;
    }

    init();
    if (m_ioLine != null) {
      if (!checkQty()) {
        log.saveError("AmtReturned>Shipped", "");
        return false;
      }

      if (newRecord || is_ValueChanged(I_M_RMALine.COLUMNNAME_M_InOutLine_ID)) {
        String whereClause =
            "M_RMA_ID="
                + getM_RMA_ID()
                + " AND M_InOutLine_ID="
                + getM_InOutLine_ID()
                + " AND M_RMALine_ID!="
                + getM_RMALine_ID();

        int lineIds[] = MRMALine.getAllIDs(MRMALine.Table_Name, whereClause, this.get_TrxName());

        if (lineIds.length > 0) {
          log.saveError("InOutLineAlreadyEntered", "");
          return false;
        }
      }
    }

    // Set default amount and qty for product
    if (this.getM_Product_ID() != 0
        && this.getQty().doubleValue() <= 0
        && !MRMA.DOCACTION_Void.equals(getParent().getDocAction())) {
      if (getQty().signum() == 0) this.setQty(Env.ONE);
      if (getAmt().signum() == 0) this.setAmt(getUnitAmt());
    }

    // Set default amount and qty for charge
    if (this.getC_Charge_ID() != 0
        && this.getQty().doubleValue() <= 0
        && !MRMA.DOCACTION_Void.equals(getParent().getDocAction())) {
      if (getQty().signum() == 0) this.setQty(Env.ONE);
      if (getAmt().signum() == 0) this.setAmt(getUnitAmt());
    }

    // Set amount for products
    if (this.getM_InOutLine_ID() != 0 && !MRMA.DOCACTION_Void.equals(getParent().getDocAction())) {
      this.setM_Product_ID(m_ioLine.getM_Product_ID());
      this.setC_Charge_ID(m_ioLine.getC_Charge_ID());
      this.setAmt(getUnitAmt());

      if (newRecord && getQty().signum() == 0) this.setQty(originalQty);
    }

    // Set tax
    if (this.getC_Tax_ID() == 0) this.setC_Tax_ID(taxId);

    // Get Line No
    if (getLine() == 0) {
      String sql = "SELECT COALESCE(MAX(Line),0)+10 FROM M_RMALine WHERE M_RMA_ID=?";
      int ii = DB.getSQLValue(get_TrxName(), sql, getM_RMA_ID());
      setLine(ii);
    }

    this.setLineNetAmt(getTotalAmt());

    return true;
  }

  public boolean checkQty() {
    if (m_ioLine.getMovementQty().compareTo(getQty()) < 0) return false;

    BigDecimal totalQty =
        DB.getSQLValueBD(
            get_TrxName(),
            "SELECT SUM(Qty) FROM M_RMALine rl JOIN M_RMA r ON (r.M_RMA_ID = rl.M_RMA_ID) WHERE M_InOutLine_ID = ? AND M_RMALine_ID != ? AND r.Processed = 'Y' AND r.DocStatus IN ('CO','CL')",
            getM_InOutLine_ID(),
            getM_RMALine_ID());
    if (totalQty == null) totalQty = Env.ZERO;
    totalQty = totalQty.add(getQty());
    if (m_ioLine.getMovementQty().compareTo(totalQty) < 0) return false;

    return true;
  }

  public boolean updateOrderTax(boolean oldTax) {
    MRMATax tax = MRMATax.get(this, getPrecision(), oldTax, get_TrxName());
    if (tax != null) {
      if (!tax.calculateTaxFromLines()) return false;
      if (tax.getTaxAmt().signum() != 0) {
        if (!tax.save(get_TrxName())) return false;
      } else {
        if (!tax.is_new() && !tax.delete(false, get_TrxName())) return false;
      }
    }
    return true;
  }

  @Override
  protected boolean afterSave(boolean newRecord, boolean success) {
    if (!success) return success;
    MTax tax = new MTax(getCtx(), getC_Tax_ID(), get_TrxName());
    MTaxProvider provider =
        new MTaxProvider(tax.getCtx(), tax.getC_TaxProvider_ID(), tax.get_TrxName());
    ITaxProvider calculator = MTaxProvider.getTaxProvider(provider, new StandardTaxProvider());
    if (calculator == null) throw new AdempiereException(Msg.getMsg(getCtx(), "TaxNoProvider"));
    return calculator.recalculateTax(provider, this, newRecord);
  }

  @Override
  protected boolean afterDelete(boolean success) {
    if (!success) return success;
    return updateHeaderAmt();
  }

  /**
   * Update Amount on Header
   *
   * @return true if header updated
   */
  public boolean updateHeaderAmt() {
    // Update header only if the document is not processed
    if (isProcessed() && !is_ValueChanged(I_M_RMALine.COLUMNNAME_Processed)) return true;

    MTax tax = new MTax(getCtx(), getC_Tax_ID(), get_TrxName());
    MTaxProvider provider =
        new MTaxProvider(tax.getCtx(), tax.getC_TaxProvider_ID(), tax.get_TrxName());
    ITaxProvider calculator = MTaxProvider.getTaxProvider(provider, new StandardTaxProvider());
    if (calculator == null) throw new AdempiereException(Msg.getMsg(getCtx(), "TaxNoProvider"));
    if (!calculator.updateRMATax(provider, this)) return false;

    return calculator.updateHeaderTax(provider, this);
  } //	updateHeaderTax

  /**
   * Add to Description
   *
   * @param description text
   */
  public void addDescription(String description) {
    String desc = getDescription();
    if (desc == null) setDescription(description);
    else setDescription(desc + " | " + description);
  } //  addDescription

  /** Get precision Based on Invoice if the shipment was invoiced, on Order otherwise */
  public int getPrecision() {
    return precision;
  }

  /**
   * Get UOM Based on Shipment line if present Default to Each (100) for charge
   *
   * @return UOM if based on shipment line and 100 for charge based
   */
  public int getC_UOM_ID() {
    if (m_ioLine == null && getC_Charge_ID() != 0) // Charge
    return 100; // Each
    else if (m_ioLine == null && getM_Product_ID() != 0) {
      MProduct product = getProduct();
      return product.getC_UOM_ID();
    }
    return m_ioLine.getC_UOM_ID();
  }

  /**
   * Get Product
   *
   * @return product or null
   */
  public MProduct getProduct() {
    if (m_product == null && getM_Product_ID() != 0)
      m_product = MProduct.get(getCtx(), getM_Product_ID());
    return m_product;
  }

  /**
   * Get Charge
   *
   * @return product or null
   */
  public MCharge getCharge() {
    if (m_charge == null && getC_Charge_ID() != 0)
      m_charge = MCharge.get(getCtx(), getC_Charge_ID());
    return m_charge;
  }

  /**
   * Get Tax
   *
   * @return tax
   */
  protected MTax getTax() {
    if (m_tax == null) m_tax = MTax.get(getCtx(), getC_Tax_ID());
    return m_tax;
  }

  /**
   * Get Project
   *
   * @return project if based on shipment line and 0 for charge based
   */
  public int getC_Project_ID() {
    if (m_ioLine == null) return 0;
    return m_ioLine.getC_Project_ID();
  }

  /**
   * Get Project Phase
   *
   * @return project phase if based on shipment line and 0 for charge based
   */
  public int getC_ProjectPhase_ID() {
    if (m_ioLine == null) return 0;
    return m_ioLine.getC_ProjectPhase_ID();
  }

  /**
   * Get Project Task
   *
   * @return project task if based on shipment line and 0 for charge based
   */
  public int getC_ProjectTask_ID() {
    if (m_ioLine == null) return 0;
    return m_ioLine.getC_ProjectTask_ID();
  }

  /**
   * Get Activity
   *
   * @return project phase if based on shipment line and 0 for charge based
   */
  public int getC_Activity_ID() {
    if (m_ioLine == null) return 0;
    return m_ioLine.getC_Activity_ID();
  }

  /**
   * Get Campaign
   *
   * @return campaign if based on shipment line and 0 for charge based
   */
  public int getC_Campaign_ID() {
    if (m_ioLine == null) return 0;
    return m_ioLine.getC_Campaign_ID();
  }

  /**
   * Get Org Trx
   *
   * @return Org Trx if based on shipment line and 0 for charge based
   */
  public int getAD_OrgTrx_ID() {
    if (m_ioLine == null) return 0;
    return m_ioLine.getAD_OrgTrx_ID();
  }

  /**
   * Get User1
   *
   * @return user1 if based on shipment line and 0 for charge based
   */
  public int getUser1_ID() {
    if (m_ioLine == null) return 0;
    return m_ioLine.getUser1_ID();
  }

  /**
   * Get User2
   *
   * @return user2 if based on shipment line and 0 for charge based
   */
  public int getUser2_ID() {
    if (m_ioLine == null) return 0;
    return m_ioLine.getUser2_ID();
  }

  /**
   * Get Attribute Set Instance
   *
   * @return ASI if based on shipment line and 0 for charge based
   */
  public int getMAttributeSetInstance_ID() {
    if (m_ioLine == null) return 0;
    return m_ioLine.getMAttributeSetInstance_ID();
  }

  /**
   * Get Locator
   *
   * @return locator if based on shipment line and 0 for charge based
   */
  public int getM_Locator_ID() {
    if (m_ioLine == null && getC_Charge_ID() != 0) return 0;
    return m_ioLine.getM_Locator_ID();
  }

  public void clearParent() {
    this.m_parent = null;
  }

  public void setClientOrg(IPO po) {
    super.setClientOrg(po);
  }
} //	MRMALine
