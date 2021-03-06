package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.idempiere.common.util.KeyNamePair;

/**
 * Generated Interface for AD_UserPreference
 *
 * @author iDempiere (generated)
 * @version Release 5.1
 */
public interface I_AD_UserPreference {

  /** TableName=AD_UserPreference */
  public static final String Table_Name = "AD_UserPreference";

  /** AD_Table_ID=200174 */
  public static final int Table_ID = 200174;

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

  /** Column name AD_UserPreference_ID */
  public static final String COLUMNNAME_AD_UserPreference_ID = "AD_UserPreference_ID";

  /** Set AD_UserPreference_ID */
  public void setAD_UserPreference_ID(int AD_UserPreference_ID);

  /** Get AD_UserPreference_ID */
  public int getAD_UserPreference_ID();

  /** Column name AD_UserPreference_UU */
  public static final String COLUMNNAME_AD_UserPreference_UU = "AD_UserPreference_UU";

  /** Set AD_UserPreference_UU */
  public void setAD_UserPreference_UU(String AD_UserPreference_UU);

  /** Get AD_UserPreference_UU */
  public String getAD_UserPreference_UU();

  /** Column name AutoCommit */
  public static final String COLUMNNAME_AutoCommit = "AutoCommit";

  /** Set Automatic Commit */
  public void setAutoCommit(boolean AutoCommit);

  /** Get Automatic Commit */
  public boolean isAutoCommit();

  /** Column name AutomaticDecimalPlacesForAmoun */
  public static final String COLUMNNAME_AutomaticDecimalPlacesForAmoun =
      "AutomaticDecimalPlacesForAmoun";

  /** Set Automatic Decimal Places For Amounts. Automatically insert a decimal point */
  public void setAutomaticDecimalPlacesForAmoun(int AutomaticDecimalPlacesForAmoun);

  /** Get Automatic Decimal Places For Amounts. Automatically insert a decimal point */
  public int getAutomaticDecimalPlacesForAmoun();

  /** Column name AutoNew */
  public static final String COLUMNNAME_AutoNew = "AutoNew";

  /** Set Automatic New Record */
  public void setAutoNew(boolean AutoNew);

  /** Get Automatic New Record */
  public boolean isAutoNew();

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

  /** Column name IsDetailedZoomAcross */
  public static final String COLUMNNAME_IsDetailedZoomAcross = "IsDetailedZoomAcross";

  /** Set Detailed Zoom Across */
  public void setIsDetailedZoomAcross(boolean IsDetailedZoomAcross);

  /** Get Detailed Zoom Across */
  public boolean isDetailedZoomAcross();

  /** Column name ToggleOnDoubleClick */
  public static final String COLUMNNAME_ToggleOnDoubleClick = "ToggleOnDoubleClick";

  /**
   * Set Toggle on Double Click. Defines if double click in a field on grid mode switch to form view
   */
  public void setToggleOnDoubleClick(boolean ToggleOnDoubleClick);

  /**
   * Get Toggle on Double Click. Defines if double click in a field on grid mode switch to form view
   */
  public boolean isToggleOnDoubleClick();

  /** Column name Updated */
  public static final String COLUMNNAME_Updated = "Updated";

  /** Get Updated. Date this record was updated */
  public Timestamp getUpdated();

  /** Column name UpdatedBy */
  public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

  /** Get Updated By. User who updated this records */
  public int getUpdatedBy();
}
