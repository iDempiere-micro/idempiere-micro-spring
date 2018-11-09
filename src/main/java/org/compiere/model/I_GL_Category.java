package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.idempiere.common.util.KeyNamePair;

/**
 * Generated Interface for GL_Category
 *
 * @author iDempiere (generated)
 * @version Release 5.1
 */
public interface I_GL_Category {

  /** TableName=GL_Category */
  public static final String Table_Name = "GL_Category";

  /** AD_Table_ID=218 */
  public static final int Table_ID = 218;

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

  /** Column name CategoryType */
  public static final String COLUMNNAME_CategoryType = "CategoryType";

  /** Set Category Type. Source of the Journal with this category */
  public void setCategoryType(String CategoryType);

  /** Get Category Type. Source of the Journal with this category */
  public String getCategoryType();

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

  /** Column name GL_Category_ID */
  public static final String COLUMNNAME_GL_Category_ID = "GL_Category_ID";

  /** Set GL Category. General Ledger Category */
  public void setGL_Category_ID(int GL_Category_ID);

  /** Get GL Category. General Ledger Category */
  public int getGL_Category_ID();

  /** Column name GL_Category_UU */
  public static final String COLUMNNAME_GL_Category_UU = "GL_Category_UU";

  /** Set GL_Category_UU */
  public void setGL_Category_UU(String GL_Category_UU);

  /** Get GL_Category_UU */
  public String getGL_Category_UU();

  /** Column name IsActive */
  public static final String COLUMNNAME_IsActive = "IsActive";

  /** Set Active. The record is active in the system */
  public void setIsActive(boolean IsActive);

  /** Get Active. The record is active in the system */
  public boolean isActive();

  /** Column name IsDefault */
  public static final String COLUMNNAME_IsDefault = "IsDefault";

  /** Set Default. Default value */
  public void setIsDefault(boolean IsDefault);

  /** Get Default. Default value */
  public boolean isDefault();

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
