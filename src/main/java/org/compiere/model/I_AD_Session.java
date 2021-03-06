package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.idempiere.common.util.KeyNamePair;

/**
 * Generated Interface for AD_Session
 *
 * @author iDempiere (generated)
 * @version Release 5.1
 */
public interface I_AD_Session {

  /** TableName=AD_Session */
  public static final String Table_Name = "AD_Session";

  /** AD_Table_ID=566 */
  public static final int Table_ID = 566;

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

  /** Column name AD_Role_ID */
  public static final String COLUMNNAME_AD_Role_ID = "AD_Role_ID";

  /** Set Role. Responsibility Role */
  public void setAD_Role_ID(int AD_Role_ID);

  /** Get Role. Responsibility Role */
  public int getAD_Role_ID();

  public I_AD_Role getAD_Role() throws RuntimeException;

  /** Column name AD_Session_ID */
  public static final String COLUMNNAME_AD_Session_ID = "AD_Session_ID";

  /** Set Session. User Session Online or Web */
  public void setAD_Session_ID(int AD_Session_ID);

  /** Get Session. User Session Online or Web */
  public int getAD_Session_ID();

  /** Column name AD_Session_UU */
  public static final String COLUMNNAME_AD_Session_UU = "AD_Session_UU";

  /** Set AD_Session_UU */
  public void setAD_Session_UU(String AD_Session_UU);

  /** Get AD_Session_UU */
  public String getAD_Session_UU();

  /** Column name Created */
  public static final String COLUMNNAME_Created = "Created";

  /** Get Created. Date this record was created */
  public Timestamp getCreated();

  /** Column name CreatedBy */
  public static final String COLUMNNAME_CreatedBy = "CreatedBy";

  /** Get Created By. User who created this records */
  public int getCreatedBy();

  /** Column name Description */
  public static final String COLUMNNAME_Description = "Description";

  /** Set Description. Optional short description of the record */
  public void setDescription(String Description);

  /** Get Description. Optional short description of the record */
  public String getDescription();

  /** Column name IsActive */
  public static final String COLUMNNAME_IsActive = "IsActive";

  /** Set Active. The record is active in the system */
  public void setIsActive(boolean IsActive);

  /** Get Active. The record is active in the system */
  public boolean isActive();

  /** Column name LoginDate */
  public static final String COLUMNNAME_LoginDate = "LoginDate";

  /** Set Login date */
  public void setLoginDate(Timestamp LoginDate);

  /** Get Login date */
  public Timestamp getLoginDate();

  /** Column name Processed */
  public static final String COLUMNNAME_Processed = "Processed";

  /** Set Processed. The document has been processed */
  public void setProcessed(boolean Processed);

  /** Get Processed. The document has been processed */
  public boolean isProcessed();

  /** Column name Remote_Addr */
  public static final String COLUMNNAME_Remote_Addr = "Remote_Addr";

  /** Set Remote Addr. Remote Address */
  public void setRemote_Addr(String Remote_Addr);

  /** Get Remote Addr. Remote Address */
  public String getRemote_Addr();

  /** Column name Remote_Host */
  public static final String COLUMNNAME_Remote_Host = "Remote_Host";

  /** Set Remote Host. Remote host Info */
  public void setRemote_Host(String Remote_Host);

  /** Get Remote Host. Remote host Info */
  public String getRemote_Host();

  /** Column name ServerName */
  public static final String COLUMNNAME_ServerName = "ServerName";

  /** Set Server Name */
  public void setServerName(String ServerName);

  /** Get Server Name */
  public String getServerName();

  /** Column name Updated */
  public static final String COLUMNNAME_Updated = "Updated";

  /** Get Updated. Date this record was updated */
  public Timestamp getUpdated();

  /** Column name UpdatedBy */
  public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

  /** Get Updated By. User who updated this records */
  public int getUpdatedBy();

  /** Column name WebSession */
  public static final String COLUMNNAME_WebSession = "WebSession";

  /** Set Web Session. Web Session ID */
  public void setWebSession(String WebSession);

  /** Get Web Session. Web Session ID */
  public String getWebSession();
}
