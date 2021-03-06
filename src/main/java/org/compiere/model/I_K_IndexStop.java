package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.idempiere.common.util.KeyNamePair;

/**
 * Generated Interface for K_IndexStop
 *
 * @author iDempiere (generated)
 * @version Release 5.1
 */
public interface I_K_IndexStop {

  /** TableName=K_IndexStop */
  public static final String Table_Name = "K_IndexStop";

  /** AD_Table_ID=901 */
  public static final int Table_ID = 901;

  KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

  /** AccessLevel = 6 - System - Client */
  BigDecimal accessLevel = BigDecimal.valueOf(6);

  /** Load Meta Data */

  /** Get Client. Client/Tenant for this installation. */
  public int getADClientID();

  /** Column name AD_Org_ID */
  public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

  /** Set Organization. Organizational entity within client */
  public void setAD_Org_ID(int AD_Org_ID);

  /** Get Organization. Organizational entity within client */
  public int getAD_Org_ID();

  /** Column name C_DocType_ID */
  public static final String COLUMNNAME_C_DocType_ID = "C_DocType_ID";

  /** Set Document Type. Document type or rules */
  public void setC_DocType_ID(int C_DocType_ID);

  /** Get Document Type. Document type or rules */
  public int getC_DocType_ID();

  public I_C_DocType getC_DocType() throws RuntimeException;

  /** Column name CM_WebProject_ID */
  public static final String COLUMNNAME_CM_WebProject_ID = "CM_WebProject_ID";

  /**
   * Set Web Project. A web project is the main data container for Containers, URLs, Ads, Media etc.
   */
  public void setCM_WebProject_ID(int CM_WebProject_ID);

  /**
   * Get Web Project. A web project is the main data container for Containers, URLs, Ads, Media etc.
   */
  public int getCM_WebProject_ID();

  public I_CM_WebProject getCM_WebProject() throws RuntimeException;

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

  /** Column name IsManual */
  public static final String COLUMNNAME_IsManual = "IsManual";

  /** Set Manual. This is a manual process */
  public void setIsManual(boolean IsManual);

  /** Get Manual. This is a manual process */
  public boolean isManual();

  /** Column name Keyword */
  public static final String COLUMNNAME_Keyword = "Keyword";

  /** Set Keyword. Case insensitive keyword */
  public void setKeyword(String Keyword);

  /** Get Keyword. Case insensitive keyword */
  public String getKeyword();

  /** Column name K_IndexStop_ID */
  public static final String COLUMNNAME_K_IndexStop_ID = "K_IndexStop_ID";

  /** Set Index Stop. Keyword not to be indexed */
  public void setK_IndexStop_ID(int K_IndexStop_ID);

  /** Get Index Stop. Keyword not to be indexed */
  public int getK_IndexStop_ID();

  /** Column name K_IndexStop_UU */
  public static final String COLUMNNAME_K_IndexStop_UU = "K_IndexStop_UU";

  /** Set K_IndexStop_UU */
  public void setK_IndexStop_UU(String K_IndexStop_UU);

  /** Get K_IndexStop_UU */
  public String getK_IndexStop_UU();

  /** Column name R_RequestType_ID */
  public static final String COLUMNNAME_R_RequestType_ID = "R_RequestType_ID";

  /** Set Request Type. Type of request (e.g. Inquiry, Complaint, ..) */
  public void setR_RequestType_ID(int R_RequestType_ID);

  /** Get Request Type. Type of request (e.g. Inquiry, Complaint, ..) */
  public int getR_RequestType_ID();

  public I_R_RequestType getR_RequestType() throws RuntimeException;

  /** Column name Updated */
  public static final String COLUMNNAME_Updated = "Updated";

  /** Get Updated. Date this record was updated */
  public Timestamp getUpdated();

  /** Column name UpdatedBy */
  public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

  /** Get Updated By. User who updated this records */
  public int getUpdatedBy();
}
