package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.idempiere.common.util.KeyNamePair;

/**
 * Generated Interface for CM_ChatUpdate
 *
 * @author iDempiere (generated)
 * @version Release 5.1
 */
public interface I_CM_ChatUpdate {

  /** TableName=CM_ChatUpdate */
  public static final String Table_Name = "CM_ChatUpdate";

  /** AD_Table_ID=878 */
  public static final int Table_ID = 878;

  KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

  /** AccessLevel = 7 - System - Client - Org */
  BigDecimal accessLevel = BigDecimal.valueOf(7);

  /** Load Meta Data */

  /** Get Client. Client/Tenant for this installation. */
  public int getADClientID();

  /** Column name AD_Org_ID */
  public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

  /** Set Organization. Organizational entity within client */
  public void setAD_Org_ID(int AD_Org_ID);

  /** Get Organization. Organizational entity within client */
  public int getAD_Org_ID();

  /** Column name AD_User_ID */
  public static final String COLUMNNAME_AD_User_ID = "AD_User_ID";

  /** Set User/Contact. User within the system - Internal or Business Partner Contact */
  public void setAD_User_ID(int AD_User_ID);

  /** Get User/Contact. User within the system - Internal or Business Partner Contact */
  public int getAD_User_ID();

  public I_AD_User getAD_User() throws RuntimeException;

  /** Column name CM_Chat_ID */
  public static final String COLUMNNAME_CM_Chat_ID = "CM_Chat_ID";

  /** Set Chat. Chat or discussion thread */
  public void setCM_Chat_ID(int CM_Chat_ID);

  /** Get Chat. Chat or discussion thread */
  public int getCM_Chat_ID();

  public I_CM_Chat getCM_Chat() throws RuntimeException;

  /** Column name CM_ChatUpdate_UU */
  public static final String COLUMNNAME_CM_ChatUpdate_UU = "CM_ChatUpdate_UU";

  /** Set CM_ChatUpdate_UU */
  public void setCM_ChatUpdate_UU(String CM_ChatUpdate_UU);

  /** Get CM_ChatUpdate_UU */
  public String getCM_ChatUpdate_UU();

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

  /** Column name IsSelfService */
  public static final String COLUMNNAME_IsSelfService = "IsSelfService";

  /**
   * Set Self-Service. This is a Self-Service entry or this entry can be changed via Self-Service
   */
  public void setIsSelfService(boolean IsSelfService);

  /**
   * Get Self-Service. This is a Self-Service entry or this entry can be changed via Self-Service
   */
  public boolean isSelfService();

  /** Column name Updated */
  public static final String COLUMNNAME_Updated = "Updated";

  /** Get Updated. Date this record was updated */
  public Timestamp getUpdated();

  /** Column name UpdatedBy */
  public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

  /** Get Updated By. User who updated this records */
  public int getUpdatedBy();
}
