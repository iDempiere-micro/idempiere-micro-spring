package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.idempiere.common.util.KeyNamePair;

/**
 * Generated Interface for CM_Template_Ad_Cat
 *
 * @author iDempiere (generated)
 * @version Release 5.1
 */
public interface I_CM_Template_Ad_Cat {

  /** TableName=CM_Template_Ad_Cat */
  public static final String Table_Name = "CM_Template_Ad_Cat";

  /** AD_Table_ID=872 */
  public static final int Table_ID = 872;

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

  /** Column name CM_Ad_Cat_ID */
  public static final String COLUMNNAME_CM_Ad_Cat_ID = "CM_Ad_Cat_ID";

  /** Set Advertisement Category. Advertisement Category like Banner Homepage */
  public void setCM_Ad_Cat_ID(int CM_Ad_Cat_ID);

  /** Get Advertisement Category. Advertisement Category like Banner Homepage */
  public int getCM_Ad_Cat_ID();

  public I_CM_Ad_Cat getCM_Ad_Cat() throws RuntimeException;

  /** Column name CM_Template_Ad_Cat_UU */
  public static final String COLUMNNAME_CM_Template_Ad_Cat_UU = "CM_Template_Ad_Cat_UU";

  /** Set CM_Template_Ad_Cat_UU */
  public void setCM_Template_Ad_Cat_UU(String CM_Template_Ad_Cat_UU);

  /** Get CM_Template_Ad_Cat_UU */
  public String getCM_Template_Ad_Cat_UU();

  /** Column name CM_Template_ID */
  public static final String COLUMNNAME_CM_Template_ID = "CM_Template_ID";

  /** Set Template. Template defines how content is displayed */
  public void setCM_Template_ID(int CM_Template_ID);

  /** Get Template. Template defines how content is displayed */
  public int getCM_Template_ID();

  public I_CM_Template getCM_Template() throws RuntimeException;

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

  /** Set Name. Alphanumeric identifier of the entity */
  public void setName(String Name);

  /** Get Name. Alphanumeric identifier of the entity */
  public String getName();

  /** Column name Updated */
  public static final String COLUMNNAME_Updated = "Updated";

  /** Get Updated. Date this record was updated */
  public Timestamp getUpdated();

  /** Column name UpdatedBy */
  public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

  /** Get Updated By. User who updated this records */
  public int getUpdatedBy();
}
