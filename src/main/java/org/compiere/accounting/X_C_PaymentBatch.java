
/** Generated Model - DO NOT CHANGE */
package org.compiere.accounting;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.model.I_C_PaymentBatch;
import org.compiere.orm.MTable;
import org.compiere.orm.PO;
import org.idempiere.orm.I_Persistent;
import org.idempiere.common.util.KeyNamePair;
import org.idempiere.orm.POInfo;


/** Generated Model for C_PaymentBatch
 *  @author iDempiere (generated) 
 *  @version Release 5.1 - $Id$ */
public class X_C_PaymentBatch extends PO implements I_C_PaymentBatch, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20171031L;

    /** Standard Constructor */
    public X_C_PaymentBatch (Properties ctx, int C_PaymentBatch_ID, String trxName)
    {
      super (ctx, C_PaymentBatch_ID, trxName);
      /** if (C_PaymentBatch_ID == 0)
        {
			setC_PaymentBatch_ID (0);
			setC_PaymentProcessor_ID (0);
			setName (null);
			setProcessed (false);
			setProcessing (false);
        } */
    }

    /** Load Constructor */
    public X_C_PaymentBatch (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 1 - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_C_PaymentBatch[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Payment Batch.
		@param C_PaymentBatch_ID 
		Payment batch for EFT
	  */
	public void setC_PaymentBatch_ID (int C_PaymentBatch_ID)
	{
		if (C_PaymentBatch_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_PaymentBatch_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_PaymentBatch_ID, Integer.valueOf(C_PaymentBatch_ID));
	}

	/** Get Payment Batch.
		@return Payment batch for EFT
	  */
	public int getC_PaymentBatch_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_PaymentBatch_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set C_PaymentBatch_UU.
		@param C_PaymentBatch_UU C_PaymentBatch_UU	  */
	public void setC_PaymentBatch_UU (String C_PaymentBatch_UU)
	{
		set_Value (COLUMNNAME_C_PaymentBatch_UU, C_PaymentBatch_UU);
	}

	/** Get C_PaymentBatch_UU.
		@return C_PaymentBatch_UU	  */
	public String getC_PaymentBatch_UU () 
	{
		return (String)get_Value(COLUMNNAME_C_PaymentBatch_UU);
	}

	public org.compiere.model.I_C_PaymentProcessor getC_PaymentProcessor() throws RuntimeException
    {
		return (org.compiere.model.I_C_PaymentProcessor)MTable.get(getCtx(), org.compiere.model.I_C_PaymentProcessor.Table_Name)
			.getPO(getC_PaymentProcessor_ID(), get_TrxName());	}

	/** Set Payment Processor.
		@param C_PaymentProcessor_ID 
		Payment processor for electronic payments
	  */
	public void setC_PaymentProcessor_ID (int C_PaymentProcessor_ID)
	{
		if (C_PaymentProcessor_ID < 1) 
			set_Value (COLUMNNAME_C_PaymentProcessor_ID, null);
		else 
			set_Value (COLUMNNAME_C_PaymentProcessor_ID, Integer.valueOf(C_PaymentProcessor_ID));
	}

	/** Get Payment Processor.
		@return Payment processor for electronic payments
	  */
	public int getC_PaymentProcessor_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_PaymentProcessor_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Document No.
		@param DocumentNo 
		Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo)
	{
		set_Value (COLUMNNAME_DocumentNo, DocumentNo);
	}

	/** Get Document No.
		@return Document sequence number of the document
	  */
	public String getDocumentNo () 
	{
		return (String)get_Value(COLUMNNAME_DocumentNo);
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getDocumentNo());
    }

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

	/** Set Processed.
		@param Processed 
		The document has been processed
	  */
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed () 
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Process Now.
		@param Processing Process Now	  */
	public void setProcessing (boolean Processing)
	{
		set_Value (COLUMNNAME_Processing, Boolean.valueOf(Processing));
	}

	/** Get Process Now.
		@return Process Now	  */
	public boolean isProcessing () 
	{
		Object oo = get_Value(COLUMNNAME_Processing);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Processing date.
		@param ProcessingDate Processing date	  */
	public void setProcessingDate (Timestamp ProcessingDate)
	{
		set_Value (COLUMNNAME_ProcessingDate, ProcessingDate);
	}

	/** Get Processing date.
		@return Processing date	  */
	public Timestamp getProcessingDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_ProcessingDate);
	}
}