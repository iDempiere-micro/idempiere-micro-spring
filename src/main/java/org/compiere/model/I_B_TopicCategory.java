package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.idempiere.common.util.KeyNamePair;

/**
 * Generated Interface for B_TopicCategory
 *
 * @author iDempiere (generated)
 * @version Release 5.1
 */
public interface I_B_TopicCategory {

  /** TableName=B_TopicCategory */
  public static final String Table_Name = "B_TopicCategory";

  /** AD_Table_ID=691 */
  public static final int Table_ID = 691;

  KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

  /** AccessLevel = 2 - Client */
  BigDecimal accessLevel = BigDecimal.valueOf(2);

  /** Load Meta Data */

  /** Get Client. Client/Tenant for this installation. */
  public int getADClientID();

  /** Column name AD_Org_ID */
  public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

  /** Set Organization. Organizational entity within client */
  public void setAD_Org_ID(int AD_Org_ID);

  /** Get Organization. Organizational entity within client */
  public int getAD_Org_ID();

  /** Column name B_TopicCategory_ID */
  public static final String COLUMNNAME_B_TopicCategory_ID = "B_TopicCategory_ID";

  /** Set Topic Category. Auction Topic Category */
  public void setB_TopicCategory_ID(int B_TopicCategory_ID);

  /** Get Topic Category. Auction Topic Category */
  public int getB_TopicCategory_ID();

  /** Column name B_TopicCategory_UU */
  public static final String COLUMNNAME_B_TopicCategory_UU = "B_TopicCategory_UU";

  /** Set B_TopicCategory_UU */
  public void setB_TopicCategory_UU(String B_TopicCategory_UU);

  /** Get B_TopicCategory_UU */
  public String getB_TopicCategory_UU();

  /** Column name B_TopicType_ID */
  public static final String COLUMNNAME_B_TopicType_ID = "B_TopicType_ID";

  /** Set Topic Type. Auction Topic Type */
  public void setB_TopicType_ID(int B_TopicType_ID);

  /** Get Topic Type. Auction Topic Type */
  public int getB_TopicType_ID();

  public I_B_TopicType getB_TopicType() throws RuntimeException;

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
