package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.idempiere.common.util.KeyNamePair;

/**
 * Generated Interface for AD_Sequence_No
 *
 * @author iDempiere (generated)
 * @version Release 5.1
 */
public interface I_AD_Sequence_No {

  /** TableName=AD_Sequence_No */
  public static final String Table_Name = "AD_Sequence_No";

  /** AD_Table_ID=122 */
  public static final int Table_ID = 122;

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

  /** Column name AD_Sequence_ID */
  public static final String COLUMNNAME_AD_Sequence_ID = "AD_Sequence_ID";

  /** Set Sequence. Document Sequence */
  public void setAD_Sequence_ID(int AD_Sequence_ID);

  /** Get Sequence. Document Sequence */
  public int getAD_Sequence_ID();

  public I_AD_Sequence getAD_Sequence() throws RuntimeException;

  /** Column name AD_Sequence_No_UU */
  public static final String COLUMNNAME_AD_Sequence_No_UU = "AD_Sequence_No_UU";

  /** Set AD_Sequence_No_UU */
  public void setAD_Sequence_No_UU(String AD_Sequence_No_UU);

  /** Get AD_Sequence_No_UU */
  public String getAD_Sequence_No_UU();

  /** Column name CalendarYearMonth */
  public static final String COLUMNNAME_CalendarYearMonth = "CalendarYearMonth";

  /** Set YearMonth. YYYYMM */
  public void setCalendarYearMonth(String CalendarYearMonth);

  /** Get YearMonth. YYYYMM */
  public String getCalendarYearMonth();

  /** Column name Created */
  public static final String COLUMNNAME_Created = "Created";

  /** Get Created. Date this record was created */
  public Timestamp getCreated();

  /** Column name CreatedBy */
  public static final String COLUMNNAME_CreatedBy = "CreatedBy";

  /** Get Created By. User who created this records */
  public int getCreatedBy();

  /** Column name CurrentNext */
  public static final String COLUMNNAME_CurrentNext = "CurrentNext";

  /** Set Current Next. The next number to be used */
  public void setCurrentNext(int CurrentNext);

  /** Get Current Next. The next number to be used */
  public int getCurrentNext();

  /** Column name IsActive */
  public static final String COLUMNNAME_IsActive = "IsActive";

  /** Set Active. The record is active in the system */
  public void setIsActive(boolean IsActive);

  /** Get Active. The record is active in the system */
  public boolean isActive();

  /** Column name Updated */
  public static final String COLUMNNAME_Updated = "Updated";

  /** Get Updated. Date this record was updated */
  public Timestamp getUpdated();

  /** Column name UpdatedBy */
  public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

  /** Get Updated By. User who updated this records */
  public int getUpdatedBy();
}
