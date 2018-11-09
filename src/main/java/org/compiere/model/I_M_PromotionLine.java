package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.idempiere.common.util.KeyNamePair;

/** Generated Interface for M_PromotionLine
 *  @author iDempiere (generated) 
 *  @version Release 5.1
 */
public interface I_M_PromotionLine 
{

    /** TableName=M_PromotionLine */
    public static final String Table_Name = "M_PromotionLine";

    /** AD_Table_ID=53179 */
    public static final int Table_ID = 53179;

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    
    

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getADClientID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name IsMandatoryPL */
    public static final String COLUMNNAME_IsMandatoryPL = "IsMandatoryPL";

	/** Set Mandatory Promotion Line.
	  * Order must have this promotion line
	  */
	public void setIsMandatoryPL (boolean IsMandatoryPL);

	/** Get Mandatory Promotion Line.
	  * Order must have this promotion line
	  */
	public boolean isMandatoryPL();

    /** Column name MinimumAmt */
    public static final String COLUMNNAME_MinimumAmt = "MinimumAmt";

	/** Set Minimum Amt.
	  * Minimum Amount in Document Currency
	  */
	public void setMinimumAmt (BigDecimal MinimumAmt);

	/** Get Minimum Amt.
	  * Minimum Amount in Document Currency
	  */
	public BigDecimal getMinimumAmt();

    /** Column name M_PromotionGroup_ID */
    public static final String COLUMNNAME_M_PromotionGroup_ID = "M_PromotionGroup_ID";

	/** Set Promotion Group	  */
	public void setM_PromotionGroup_ID (int M_PromotionGroup_ID);

	/** Get Promotion Group	  */
	public int getM_PromotionGroup_ID();

	public I_M_PromotionGroup getM_PromotionGroup() throws RuntimeException;

    /** Column name M_Promotion_ID */
    public static final String COLUMNNAME_M_Promotion_ID = "M_Promotion_ID";

	/** Set Promotion	  */
	public void setM_Promotion_ID (int M_Promotion_ID);

	/** Get Promotion	  */
	public int getM_Promotion_ID();

	public I_M_Promotion getM_Promotion() throws RuntimeException;

    /** Column name M_PromotionLine_ID */
    public static final String COLUMNNAME_M_PromotionLine_ID = "M_PromotionLine_ID";

	/** Set Promotion Line	  */
	public void setM_PromotionLine_ID (int M_PromotionLine_ID);

	/** Get Promotion Line	  */
	public int getM_PromotionLine_ID();

    /** Column name M_PromotionLine_UU */
    public static final String COLUMNNAME_M_PromotionLine_UU = "M_PromotionLine_UU";

	/** Set M_PromotionLine_UU	  */
	public void setM_PromotionLine_UU (String M_PromotionLine_UU);

	/** Get M_PromotionLine_UU	  */
	public String getM_PromotionLine_UU();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();
}