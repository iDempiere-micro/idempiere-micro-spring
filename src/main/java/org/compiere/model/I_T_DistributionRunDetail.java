package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.idempiere.common.util.KeyNamePair;

/**
 * Generated Interface for T_DistributionRunDetail
 *
 * @author iDempiere (generated)
 * @version Release 5.1
 */
public interface I_T_DistributionRunDetail {

  /** TableName=T_DistributionRunDetail */
  public static final String Table_Name = "T_DistributionRunDetail";

  /** AD_Table_ID=714 */
  public static final int Table_ID = 714;

  KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

  /** AccessLevel = 3 - Client - Org */
  BigDecimal accessLevel = BigDecimal.valueOf(3);

  /** Load Meta Data */

  /** Get Client. Client/Tenant for this installation. */
  public int getADClientID();

  /** Column name AD_Org_ID */
  public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

  /** Set Organization. Organizational entity within client */
  public void setAD_Org_ID(int AD_Org_ID);

  /** Get Organization. Organizational entity within client */
  public int getAD_Org_ID();

  /** Column name C_BPartner_ID */
  public static final String COLUMNNAME_C_BPartner_ID = "C_BPartner_ID";

  /** Set Business Partner . Identifies a Business Partner */
  public void setC_BPartner_ID(int C_BPartner_ID);

  /** Get Business Partner . Identifies a Business Partner */
  public int getC_BPartner_ID();

  public I_C_BPartner getC_BPartner() throws RuntimeException;

  /** Column name C_BPartner_Location_ID */
  public static final String COLUMNNAME_C_BPartner_Location_ID = "C_BPartner_Location_ID";

  /** Set Partner Location. Identifies the (ship to) address for this Business Partner */
  public void setC_BPartner_Location_ID(int C_BPartner_Location_ID);

  /** Get Partner Location. Identifies the (ship to) address for this Business Partner */
  public int getC_BPartner_Location_ID();

  public I_C_BPartner_Location getC_BPartner_Location() throws RuntimeException;

  /** Column name Created */
  public static final String COLUMNNAME_Created = "Created";

  /** Get Created. Date this record was created */
  public Timestamp getCreated();

  /** Column name CreatedBy */
  public static final String COLUMNNAME_CreatedBy = "CreatedBy";

  /** Get Created By. User who created this records */
  public int getCreatedBy();

  /** Column name IsActive */
  public static final String COLUMNNAME_IsActive = "IsActive";

  /** Set Active. The record is active in the system */
  public void setIsActive(boolean IsActive);

  /** Get Active. The record is active in the system */
  public boolean isActive();

  /** Column name M_DistributionList_ID */
  public static final String COLUMNNAME_M_DistributionList_ID = "M_DistributionList_ID";

  /**
   * Set Distribution List. Distribution Lists allow to distribute products to a selected list of
   * partners
   */
  public void setM_DistributionList_ID(int M_DistributionList_ID);

  /**
   * Get Distribution List. Distribution Lists allow to distribute products to a selected list of
   * partners
   */
  public int getM_DistributionList_ID();

  public I_M_DistributionList getM_DistributionList() throws RuntimeException;

  /** Column name M_DistributionListLine_ID */
  public static final String COLUMNNAME_M_DistributionListLine_ID = "M_DistributionListLine_ID";

  /**
   * Set Distribution List Line. Distribution List Line with Business Partner and
   * Quantity/Percentage
   */
  public void setM_DistributionListLine_ID(int M_DistributionListLine_ID);

  /**
   * Get Distribution List Line. Distribution List Line with Business Partner and
   * Quantity/Percentage
   */
  public int getM_DistributionListLine_ID();

  public I_M_DistributionListLine getM_DistributionListLine() throws RuntimeException;

  /** Column name M_DistributionRun_ID */
  public static final String COLUMNNAME_M_DistributionRun_ID = "M_DistributionRun_ID";

  /**
   * Set Distribution Run. Distribution Run create Orders to distribute products to a selected list
   * of partners
   */
  public void setM_DistributionRun_ID(int M_DistributionRun_ID);

  /**
   * Get Distribution Run. Distribution Run create Orders to distribute products to a selected list
   * of partners
   */
  public int getM_DistributionRun_ID();

  public I_M_DistributionRun getM_DistributionRun() throws RuntimeException;

  /** Column name M_DistributionRunLine_ID */
  public static final String COLUMNNAME_M_DistributionRunLine_ID = "M_DistributionRunLine_ID";

  /**
   * Set Distribution Run Line. Distribution Run Lines define Distribution List, the Product and
   * Quantities
   */
  public void setM_DistributionRunLine_ID(int M_DistributionRunLine_ID);

  /**
   * Get Distribution Run Line. Distribution Run Lines define Distribution List, the Product and
   * Quantities
   */
  public int getM_DistributionRunLine_ID();

  public I_M_DistributionRunLine getM_DistributionRunLine() throws RuntimeException;

  /** Column name MinQty */
  public static final String COLUMNNAME_MinQty = "MinQty";

  /** Set Minimum Quantity. Minimum quantity for the business partner */
  public void setMinQty(BigDecimal MinQty);

  /** Get Minimum Quantity. Minimum quantity for the business partner */
  public BigDecimal getMinQty();

  /** Column name M_Product_ID */
  public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";

  /** Set Product. Product, Service, Item */
  public void setM_Product_ID(int M_Product_ID);

  /** Get Product. Product, Service, Item */
  public int getM_Product_ID();

  public I_M_Product getM_Product() throws RuntimeException;

  /** Column name Qty */
  public static final String COLUMNNAME_Qty = "Qty";

  /** Set Quantity. Quantity */
  public void setQty(BigDecimal Qty);

  /** Get Quantity. Quantity */
  public BigDecimal getQty();

  /** Column name Ratio */
  public static final String COLUMNNAME_Ratio = "Ratio";

  /** Set Ratio. Relative Ratio for Distributions */
  public void setRatio(BigDecimal Ratio);

  /** Get Ratio. Relative Ratio for Distributions */
  public BigDecimal getRatio();

  /** Column name T_DistributionRunDetail_UU */
  public static final String COLUMNNAME_T_DistributionRunDetail_UU = "T_DistributionRunDetail_UU";

  /** Set T_DistributionRunDetail_UU */
  public void setT_DistributionRunDetail_UU(String T_DistributionRunDetail_UU);

  /** Get T_DistributionRunDetail_UU */
  public String getT_DistributionRunDetail_UU();

  /** Column name Updated */
  public static final String COLUMNNAME_Updated = "Updated";

  /** Get Updated. Date this record was updated */
  public Timestamp getUpdated();

  /** Column name UpdatedBy */
  public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

  /** Get Updated By. User who updated this records */
  public int getUpdatedBy();
}
