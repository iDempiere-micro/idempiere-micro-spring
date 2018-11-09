package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.idempiere.common.util.KeyNamePair;

/**
 * Generated Interface for C_ChargeType_DocType
 *
 * @author iDempiere (generated)
 * @version Release 5.1
 */
public interface I_C_ChargeType_DocType {

  /** TableName=C_ChargeType_DocType */
  public static final String Table_Name = "C_ChargeType_DocType";

  /** AD_Table_ID=53146 */
  public static final int Table_ID = 53146;

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

  /** Column name C_ChargeType_DocType_UU */
  public static final String COLUMNNAME_C_ChargeType_DocType_UU = "C_ChargeType_DocType_UU";

  /** Set C_ChargeType_DocType_UU */
  public void setC_ChargeType_DocType_UU(String C_ChargeType_DocType_UU);

  /** Get C_ChargeType_DocType_UU */
  public String getC_ChargeType_DocType_UU();

  /** Column name C_ChargeType_ID */
  public static final String COLUMNNAME_C_ChargeType_ID = "C_ChargeType_ID";

  /** Set Charge Type */
  public void setC_ChargeType_ID(int C_ChargeType_ID);

  /** Get Charge Type */
  public int getC_ChargeType_ID();

  public I_C_ChargeType getC_ChargeType() throws RuntimeException;

  /** Column name C_DocType_ID */
  public static final String COLUMNNAME_C_DocType_ID = "C_DocType_ID";

  /** Set Document Type. Document type or rules */
  public void setC_DocType_ID(int C_DocType_ID);

  /** Get Document Type. Document type or rules */
  public int getC_DocType_ID();

  public I_C_DocType getC_DocType() throws RuntimeException;

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

  /** Column name IsAllowNegative */
  public static final String COLUMNNAME_IsAllowNegative = "IsAllowNegative";

  /** Set Allow Negative */
  public void setIsAllowNegative(boolean IsAllowNegative);

  /** Get Allow Negative */
  public boolean isAllowNegative();

  /** Column name IsAllowPositive */
  public static final String COLUMNNAME_IsAllowPositive = "IsAllowPositive";

  /** Set Allow Positive */
  public void setIsAllowPositive(boolean IsAllowPositive);

  /** Get Allow Positive */
  public boolean isAllowPositive();

  /** Column name Updated */
  public static final String COLUMNNAME_Updated = "Updated";

  /** Get Updated. Date this record was updated */
  public Timestamp getUpdated();

  /** Column name UpdatedBy */
  public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

  /** Get Updated By. User who updated this records */
  public int getUpdatedBy();
}
