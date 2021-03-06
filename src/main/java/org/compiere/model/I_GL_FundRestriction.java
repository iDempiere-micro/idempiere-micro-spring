package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.idempiere.common.util.KeyNamePair;

/**
 * Generated Interface for GL_FundRestriction
 *
 * @author iDempiere (generated)
 * @version Release 5.1
 */
public interface I_GL_FundRestriction {

  /** TableName=GL_FundRestriction */
  public static final String Table_Name = "GL_FundRestriction";

  /** AD_Table_ID=824 */
  public static final int Table_ID = 824;

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

  /** Column name C_ElementValue_ID */
  public static final String COLUMNNAME_C_ElementValue_ID = "C_ElementValue_ID";

  /** Set Account Element. Account Element */
  public void setC_ElementValue_ID(int C_ElementValue_ID);

  /** Get Account Element. Account Element */
  public int getC_ElementValue_ID();

  public I_C_ElementValue getC_ElementValue() throws RuntimeException;

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

  /** Column name GL_Fund_ID */
  public static final String COLUMNNAME_GL_Fund_ID = "GL_Fund_ID";

  /** Set GL Fund. General Ledger Funds Control */
  public void setGL_Fund_ID(int GL_Fund_ID);

  /** Get GL Fund. General Ledger Funds Control */
  public int getGL_Fund_ID();

  public I_GL_Fund getGL_Fund() throws RuntimeException;

  /** Column name GL_FundRestriction_ID */
  public static final String COLUMNNAME_GL_FundRestriction_ID = "GL_FundRestriction_ID";

  /** Set Fund Restriction. Restriction of Funds */
  public void setGL_FundRestriction_ID(int GL_FundRestriction_ID);

  /** Get Fund Restriction. Restriction of Funds */
  public int getGL_FundRestriction_ID();

  /** Column name GL_FundRestriction_UU */
  public static final String COLUMNNAME_GL_FundRestriction_UU = "GL_FundRestriction_UU";

  /** Set GL_FundRestriction_UU */
  public void setGL_FundRestriction_UU(String GL_FundRestriction_UU);

  /** Get GL_FundRestriction_UU */
  public String getGL_FundRestriction_UU();

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
