package org.compiere.accounting;

import org.compiere.model.I_C_AcctSchema;
import org.compiere.orm.BasePOName;
import org.compiere.orm.MTable;
import org.idempiere.orm.I_Persistent;
import org.idempiere.orm.POInfo;


import java.sql.ResultSet;
import java.util.Properties;

/** Generated Model for C_AcctSchema
 *  @author iDempiere (generated) 
 *  @version Release 5.1 - $Id$ */
public class X_C_AcctSchema extends BasePOName implements I_C_AcctSchema, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20171031L;

    /** Standard Constructor */
    public X_C_AcctSchema (Properties ctx, int C_AcctSchema_ID, String trxName)
    {
      super (ctx, C_AcctSchema_ID, trxName);
    }

    /** Load Constructor */
    public X_C_AcctSchema (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 2 - Client 
      */
    protected int getAccessLevel()
    {
      return I_C_AcctSchema.accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
		return POInfo.getPOInfo (ctx, I_C_AcctSchema.Table_ID, get_TrxName());
    }

    public String toString()
    {
		return "X_C_AcctSchema[" +
			get_ID() + "]";
    }

	/** Set Only Organization.
		@param AD_OrgOnly_ID 
		Create posting entries only for this organization
	  */
	public void setAD_OrgOnly_ID (int AD_OrgOnly_ID)
	{
		if (AD_OrgOnly_ID < 1) 
			set_Value (I_C_AcctSchema.COLUMNNAME_AD_OrgOnly_ID, null);
		else 
			set_Value (I_C_AcctSchema.COLUMNNAME_AD_OrgOnly_ID, AD_OrgOnly_ID);
	}

	/** Get Only Organization.
		@return Create posting entries only for this organization
	  */
	public int getAD_OrgOnly_ID () 
	{
		Integer ii = (Integer)get_Value(I_C_AcctSchema.COLUMNNAME_AD_OrgOnly_ID);
		if (ii == null)
			 return 0;
		return ii;
	}

	/** Set Automatic Period Control.
		@param AutoPeriodControl 
		If selected, the periods are automatically opened and closed
	  */
	public void setAutoPeriodControl (boolean AutoPeriodControl)
	{
		set_Value (I_C_AcctSchema.COLUMNNAME_AutoPeriodControl, AutoPeriodControl);
	}

	/** Get Automatic Period Control.
		@return If selected, the periods are automatically opened and closed
	  */
	public boolean isAutoPeriodControl () 
	{
		Object oo = get_Value(I_C_AcctSchema.COLUMNNAME_AutoPeriodControl);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return (Boolean) oo;
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Accounting Schema.
		@param C_AcctSchema_ID 
		Rules for accounting
	  */
	public void setC_AcctSchema_ID (int C_AcctSchema_ID)
	{
		if (C_AcctSchema_ID < 1) 
			set_ValueNoCheck (I_C_AcctSchema.COLUMNNAME_C_AcctSchema_ID, null);
		else 
			set_ValueNoCheck (I_C_AcctSchema.COLUMNNAME_C_AcctSchema_ID, C_AcctSchema_ID);
	}

	/** Get Accounting Schema.
		@return Rules for accounting
	  */
	public int getC_AcctSchema_ID () 
	{
		Integer ii = (Integer)get_Value(I_C_AcctSchema.COLUMNNAME_C_AcctSchema_ID);
		if (ii == null)
			 return 0;
		return ii;
	}

	/** Set C_AcctSchema_UU.
		@param C_AcctSchema_UU C_AcctSchema_UU	  */
	public void setC_AcctSchema_UU (String C_AcctSchema_UU)
	{
		set_Value (I_C_AcctSchema.COLUMNNAME_C_AcctSchema_UU, C_AcctSchema_UU);
	}

	/** Get C_AcctSchema_UU.
		@return C_AcctSchema_UU	  */
	public String getC_AcctSchema_UU () 
	{
		return (String)get_Value(I_C_AcctSchema.COLUMNNAME_C_AcctSchema_UU);
	}

	public org.compiere.model.I_C_Currency getC_Currency() throws RuntimeException
    {
		return (org.compiere.model.I_C_Currency)MTable.get(getCtx(), org.compiere.model.I_C_Currency.Table_Name)
			.getPO(getC_Currency_ID(), get_TrxName());	}

	/** Set Currency.
		@param C_Currency_ID 
		The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID)
	{
		if (C_Currency_ID < 1) 
			set_Value (I_C_AcctSchema.COLUMNNAME_C_Currency_ID, null);
		else 
			set_Value (I_C_AcctSchema.COLUMNNAME_C_Currency_ID, C_Currency_ID);
	}

	/** Get Currency.
		@return The Currency for this record
	  */
	public int getC_Currency_ID () 
	{
		Integer ii = (Integer)get_Value(I_C_AcctSchema.COLUMNNAME_C_Currency_ID);
		if (ii == null)
			 return 0;
		return ii;
	}

	/** CommitmentType AD_Reference_ID=359 */
	public static final int COMMITMENTTYPE_AD_Reference_ID=359;
	/** PO Commitment only = C */
	public static final String COMMITMENTTYPE_POCommitmentOnly = "C";
	/** PO Commitment & Reservation = B */
	public static final String COMMITMENTTYPE_POCommitmentReservation = "B";
	/** None = N */
	public static final String COMMITMENTTYPE_None = "N";
	/** PO/SO Commitment & Reservation = A */
	public static final String COMMITMENTTYPE_POSOCommitmentReservation = "A";
	/** SO Commitment only = S */
	public static final String COMMITMENTTYPE_SOCommitmentOnly = "S";
	/** PO/SO Commitment = O */
	public static final String COMMITMENTTYPE_POSOCommitment = "O";
	/** Set Commitment Type.
		@param CommitmentType 
		Create Commitment and/or Reservations for Budget Control
	  */
	public void setCommitmentType (String CommitmentType)
	{

		set_Value (I_C_AcctSchema.COLUMNNAME_CommitmentType, CommitmentType);
	}

	/** Get Commitment Type.
		@return Create Commitment and/or Reservations for Budget Control
	  */
	public String getCommitmentType () 
	{
		return (String)get_Value(I_C_AcctSchema.COLUMNNAME_CommitmentType);
	}

	/** CostingLevel AD_Reference_ID=355 */
	public static final int COSTINGLEVEL_AD_Reference_ID=355;
	/** Client = C */
	public static final String COSTINGLEVEL_Client = "C";
	/** Organization = O */
	public static final String COSTINGLEVEL_Organization = "O";
	/** Batch/Lot = B */
	public static final String COSTINGLEVEL_BatchLot = "B";
	/** Set Costing Level.
		@param CostingLevel 
		The lowest level to accumulate Costing Information
	  */
	public void setCostingLevel (String CostingLevel)
	{

		set_Value (I_C_AcctSchema.COLUMNNAME_CostingLevel, CostingLevel);
	}

	/** Get Costing Level.
		@return The lowest level to accumulate Costing Information
	  */
	public String getCostingLevel () 
	{
		return (String)get_Value(I_C_AcctSchema.COLUMNNAME_CostingLevel);
	}

	/** CostingMethod AD_Reference_ID=122 */
	public static final int COSTINGMETHOD_AD_Reference_ID=122;
	/** Standard Costing = S */
	public static final String COSTINGMETHOD_StandardCosting = "S";
	/** Average PO = A */
	public static final String COSTINGMETHOD_AveragePO = "A";
	/** Lifo = L */
	public static final String COSTINGMETHOD_Lifo = "L";
	/** Fifo = F */
	public static final String COSTINGMETHOD_Fifo = "F";
	/** Last PO Price = p */
	public static final String COSTINGMETHOD_LastPOPrice = "p";
	/** Average Invoice = I */
	public static final String COSTINGMETHOD_AverageInvoice = "I";
	/** Last Invoice = i */
	public static final String COSTINGMETHOD_LastInvoice = "i";
	/** User Defined = U */
	public static final String COSTINGMETHOD_UserDefined = "U";
	/** _ = x */
	public static final String COSTINGMETHOD__ = "x";
	/** Set Costing Method.
		@param CostingMethod 
		Indicates how Costs will be calculated
	  */
	public void setCostingMethod (String CostingMethod)
	{

		set_Value (I_C_AcctSchema.COLUMNNAME_CostingMethod, CostingMethod);
	}

	/** Get Costing Method.
		@return Indicates how Costs will be calculated
	  */
	public String getCostingMethod () 
	{
		return (String)get_Value(I_C_AcctSchema.COLUMNNAME_CostingMethod);
	}

	public org.compiere.model.I_C_Period getC_Period() throws RuntimeException
    {
		return (org.compiere.model.I_C_Period)MTable.get(getCtx(), org.compiere.model.I_C_Period.Table_Name)
			.getPO(getC_Period_ID(), get_TrxName());	}

	/** Set Period.
		@param C_Period_ID 
		Period of the Calendar
	  */
	public void setC_Period_ID (int C_Period_ID)
	{
		if (C_Period_ID < 1) 
			set_ValueNoCheck (I_C_AcctSchema.COLUMNNAME_C_Period_ID, null);
		else 
			set_ValueNoCheck (I_C_AcctSchema.COLUMNNAME_C_Period_ID, C_Period_ID);
	}

	/** Get Period.
		@return Period of the Calendar
	  */
	public int getC_Period_ID () 
	{
		Integer ii = (Integer)get_Value(I_C_AcctSchema.COLUMNNAME_C_Period_ID);
		if (ii == null)
			 return 0;
		return ii;
	}

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (I_C_AcctSchema.COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(I_C_AcctSchema.COLUMNNAME_Description);
	}

	/** GAAP AD_Reference_ID=123 */
	public static final int GAAP_AD_Reference_ID=123;
	/** International GAAP = UN */
	public static final String GAAP_InternationalGAAP = "UN";
	/** US GAAP = US */
	public static final String GAAP_USGAAP = "US";
	/** German HGB = DE */
	public static final String GAAP_GermanHGB = "DE";
	/** French Accounting Standard = FR */
	public static final String GAAP_FrenchAccountingStandard = "FR";
	/** Custom Accounting Rules = XX */
	public static final String GAAP_CustomAccountingRules = "XX";
	/** Set GAAP.
		@param GAAP 
		Generally Accepted Accounting Principles
	  */
	public void setGAAP (String GAAP)
	{

		set_Value (I_C_AcctSchema.COLUMNNAME_GAAP, GAAP);
	}

	/** Get GAAP.
		@return Generally Accepted Accounting Principles
	  */
	public String getGAAP () 
	{
		return (String)get_Value(I_C_AcctSchema.COLUMNNAME_GAAP);
	}

	/** Set Use Account Alias.
		@param HasAlias 
		Ability to select (partial) account combinations by an Alias
	  */
	public void setHasAlias (boolean HasAlias)
	{
		set_Value (I_C_AcctSchema.COLUMNNAME_HasAlias, HasAlias);
	}

	/** Get Use Account Alias.
		@return Ability to select (partial) account combinations by an Alias
	  */
	public boolean isHasAlias () 
	{
		Object oo = get_Value(I_C_AcctSchema.COLUMNNAME_HasAlias);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return (Boolean) oo;
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Use Account Combination Control.
		@param HasCombination 
		Combination of account elements are checked
	  */
	public void setHasCombination (boolean HasCombination)
	{
		set_Value (I_C_AcctSchema.COLUMNNAME_HasCombination, HasCombination);
	}

	/** Get Use Account Combination Control.
		@return Combination of account elements are checked
	  */
	public boolean isHasCombination () 
	{
		Object oo = get_Value(I_C_AcctSchema.COLUMNNAME_HasCombination);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return (Boolean) oo;
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Accrual.
		@param IsAccrual 
		Indicates if Accrual or Cash Based accounting will be used
	  */
	public void setIsAccrual (boolean IsAccrual)
	{
		set_Value (I_C_AcctSchema.COLUMNNAME_IsAccrual, IsAccrual);
	}

	/** Get Accrual.
		@return Indicates if Accrual or Cash Based accounting will be used
	  */
	public boolean isAccrual () 
	{
		Object oo = get_Value(I_C_AcctSchema.COLUMNNAME_IsAccrual);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return (Boolean) oo;
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Adjust COGS.
		@param IsAdjustCOGS 
		Adjust Cost of Good Sold
	  */
	public void setIsAdjustCOGS (boolean IsAdjustCOGS)
	{
		set_Value (I_C_AcctSchema.COLUMNNAME_IsAdjustCOGS, IsAdjustCOGS);
	}

	/** Get Adjust COGS.
		@return Adjust Cost of Good Sold
	  */
	public boolean isAdjustCOGS () 
	{
		Object oo = get_Value(I_C_AcctSchema.COLUMNNAME_IsAdjustCOGS);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return (Boolean) oo;
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Allow Negative Posting.
		@param IsAllowNegativePosting 
		Allow to post negative accounting values
	  */
	public void setIsAllowNegativePosting (boolean IsAllowNegativePosting)
	{
		set_Value (I_C_AcctSchema.COLUMNNAME_IsAllowNegativePosting, IsAllowNegativePosting);
	}

	/** Get Allow Negative Posting.
		@return Allow to post negative accounting values
	  */
	public boolean isAllowNegativePosting () 
	{
		Object oo = get_Value(I_C_AcctSchema.COLUMNNAME_IsAllowNegativePosting);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return (Boolean) oo;
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Correct tax for Discounts/Charges.
		@param IsDiscountCorrectsTax 
		Correct the tax for payment discount and charges
	  */
	public void setIsDiscountCorrectsTax (boolean IsDiscountCorrectsTax)
	{
		set_Value (I_C_AcctSchema.COLUMNNAME_IsDiscountCorrectsTax, IsDiscountCorrectsTax);
	}

	/** Get Correct tax for Discounts/Charges.
		@return Correct the tax for payment discount and charges
	  */
	public boolean isDiscountCorrectsTax () 
	{
		Object oo = get_Value(I_C_AcctSchema.COLUMNNAME_IsDiscountCorrectsTax);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return (Boolean) oo;
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Explicit Cost Adjustment.
		@param IsExplicitCostAdjustment 
		Post the cost adjustment explicitly
	  */
	public void setIsExplicitCostAdjustment (boolean IsExplicitCostAdjustment)
	{
		set_Value (I_C_AcctSchema.COLUMNNAME_IsExplicitCostAdjustment, IsExplicitCostAdjustment);
	}

	/** Get Explicit Cost Adjustment.
		@return Post the cost adjustment explicitly
	  */
	public boolean isExplicitCostAdjustment () 
	{
		Object oo = get_Value(I_C_AcctSchema.COLUMNNAME_IsExplicitCostAdjustment);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return (Boolean) oo;
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Post if Clearing Equal.
		@param IsPostIfClearingEqual 
		This flag controls if Adempiere must post when clearing (transit) and final accounts are the same
	  */
	public void setIsPostIfClearingEqual (boolean IsPostIfClearingEqual)
	{
		set_Value (I_C_AcctSchema.COLUMNNAME_IsPostIfClearingEqual, IsPostIfClearingEqual);
	}

	/** Get Post if Clearing Equal.
		@return This flag controls if Adempiere must post when clearing (transit) and final accounts are the same
	  */
	public boolean isPostIfClearingEqual () 
	{
		Object oo = get_Value(I_C_AcctSchema.COLUMNNAME_IsPostIfClearingEqual);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return (Boolean) oo;
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Post Services Separately.
		@param IsPostServices 
		Differentiate between Services and Product Receivable/Payables
	  */
	public void setIsPostServices (boolean IsPostServices)
	{
		set_Value (I_C_AcctSchema.COLUMNNAME_IsPostServices, IsPostServices);
	}

	/** Get Post Services Separately.
		@return Differentiate between Services and Product Receivable/Payables
	  */
	public boolean isPostServices () 
	{
		Object oo = get_Value(I_C_AcctSchema.COLUMNNAME_IsPostServices);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return (Boolean) oo;
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Post Trade Discount.
		@param IsTradeDiscountPosted 
		Generate postings for trade discounts
	  */
	public void setIsTradeDiscountPosted (boolean IsTradeDiscountPosted)
	{
		set_Value (I_C_AcctSchema.COLUMNNAME_IsTradeDiscountPosted, IsTradeDiscountPosted);
	}

	/** Get Post Trade Discount.
		@return Generate postings for trade discounts
	  */
	public boolean isTradeDiscountPosted () 
	{
		Object oo = get_Value(I_C_AcctSchema.COLUMNNAME_IsTradeDiscountPosted);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return (Boolean) oo;
			return "Y".equals(oo);
		}
		return false;
	}

	public org.compiere.model.I_M_CostType getM_CostType() throws RuntimeException
    {
		return (org.compiere.model.I_M_CostType)MTable.get(getCtx(), org.compiere.model.I_M_CostType.Table_Name)
			.getPO(getM_CostType_ID(), get_TrxName());	}

	/** Set Cost Type.
		@param M_CostType_ID 
		Type of Cost (e.g. Current, Plan, Future)
	  */
	public void setM_CostType_ID (int M_CostType_ID)
	{
		if (M_CostType_ID < 1) 
			set_Value (I_C_AcctSchema.COLUMNNAME_M_CostType_ID, null);
		else 
			set_Value (I_C_AcctSchema.COLUMNNAME_M_CostType_ID, M_CostType_ID);
	}

	/** Get Cost Type.
		@return Type of Cost (e.g. Current, Plan, Future)
	  */
	public int getM_CostType_ID () 
	{
		Integer ii = (Integer)get_Value(I_C_AcctSchema.COLUMNNAME_M_CostType_ID);
		if (ii == null)
			 return 0;
		return ii;
	}

	/** Set Future Days.
		@param Period_OpenFuture 
		Number of days to be able to post to a future date (based on system date)
	  */
	public void setPeriod_OpenFuture (int Period_OpenFuture)
	{
		set_Value (I_C_AcctSchema.COLUMNNAME_Period_OpenFuture, Period_OpenFuture);
	}

	/** Get Future Days.
		@return Number of days to be able to post to a future date (based on system date)
	  */
	public int getPeriod_OpenFuture () 
	{
		Integer ii = (Integer)get_Value(I_C_AcctSchema.COLUMNNAME_Period_OpenFuture);
		if (ii == null)
			 return 0;
		return ii;
	}

	/** Set History Days.
		@param Period_OpenHistory 
		Number of days to be able to post in the past (based on system date)
	  */
	public void setPeriod_OpenHistory (int Period_OpenHistory)
	{
		set_Value (I_C_AcctSchema.COLUMNNAME_Period_OpenHistory, Period_OpenHistory);
	}

	/** Get History Days.
		@return Number of days to be able to post in the past (based on system date)
	  */
	public int getPeriod_OpenHistory () 
	{
		Integer ii = (Integer)get_Value(I_C_AcctSchema.COLUMNNAME_Period_OpenHistory);
		if (ii == null)
			 return 0;
		return ii;
	}

	/** Set Process Now.
		@param Processing Process Now	  */
	public void setProcessing (boolean Processing)
	{
		set_Value (I_C_AcctSchema.COLUMNNAME_Processing, Processing);
	}

	/** Get Process Now.
		@return Process Now	  */
	public boolean isProcessing () 
	{
		Object oo = get_Value(I_C_AcctSchema.COLUMNNAME_Processing);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return (Boolean) oo;
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Element Separator.
		@param Separator 
		Element Separator
	  */
	public void setSeparator (String Separator)
	{
		set_Value (I_C_AcctSchema.COLUMNNAME_Separator, Separator);
	}

	/** Get Element Separator.
		@return Element Separator
	  */
	public String getSeparator () 
	{
		return (String)get_Value(I_C_AcctSchema.COLUMNNAME_Separator);
	}

	/** TaxCorrectionType AD_Reference_ID=392 */
	public static final int TAXCORRECTIONTYPE_AD_Reference_ID=392;
	/** None = N */
	public static final String TAXCORRECTIONTYPE_None = "N";
	/** Write-off only = W */
	public static final String TAXCORRECTIONTYPE_Write_OffOnly = "W";
	/** Discount only = D */
	public static final String TAXCORRECTIONTYPE_DiscountOnly = "D";
	/** Write-off and Discount = B */
	public static final String TAXCORRECTIONTYPE_Write_OffAndDiscount = "B";
	/** Set Tax Correction.
		@param TaxCorrectionType 
		Type of Tax Correction
	  */
	public void setTaxCorrectionType (String TaxCorrectionType)
	{

		set_Value (I_C_AcctSchema.COLUMNNAME_TaxCorrectionType, TaxCorrectionType);
	}

	/** Get Tax Correction.
		@return Type of Tax Correction
	  */
	public String getTaxCorrectionType () 
	{
		return (String)get_Value(I_C_AcctSchema.COLUMNNAME_TaxCorrectionType);
	}
}