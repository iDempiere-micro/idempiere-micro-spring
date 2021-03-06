package org.compiere.orm;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.I_AD_Record_Access;
import org.idempiere.common.util.KeyNamePair;
import org.idempiere.orm.I_Persistent;
import org.idempiere.orm.POInfo;

/**
 * Generated Model for AD_Record_Access
 *
 * @author iDempiere (generated)
 * @version Release 5.1 - $Id$
 */
public class X_AD_Record_Access extends PO implements I_AD_Record_Access, I_Persistent {

  /** */
  private static final long serialVersionUID = 20171031L;

  /** Standard Constructor */
  public X_AD_Record_Access(Properties ctx, int AD_Record_Access_ID, String trxName) {
    super(ctx, AD_Record_Access_ID, trxName);
    /**
     * if (AD_Record_Access_ID == 0) { setAD_Role_ID (0); setAD_Table_ID (0); setIsDependentEntities
     * (false); // N setIsExclude (true); // Y setIsReadOnly (false); setRecord_ID (0); }
     */
  }

  /** Load Constructor */
  public X_AD_Record_Access(Properties ctx, ResultSet rs, String trxName) {
    super(ctx, rs, trxName);
  }

  /**
   * AccessLevel
   *
   * @return 6 - System - Client
   */
  protected int getAccessLevel() {
    return accessLevel.intValue();
  }

  /** Load Meta Data */
  protected POInfo initPO(Properties ctx) {
    POInfo poi = POInfo.getPOInfo(ctx, Table_ID, get_TrxName());
    return poi;
  }

  public String toString() {
    StringBuffer sb = new StringBuffer("X_AD_Record_Access[").append(getId()).append("]");
    return sb.toString();
  }

  /**
   * Set AD_Record_Access_UU.
   *
   * @param AD_Record_Access_UU AD_Record_Access_UU
   */
  public void setAD_Record_Access_UU(String AD_Record_Access_UU) {
    set_Value(COLUMNNAME_AD_Record_Access_UU, AD_Record_Access_UU);
  }

  /**
   * Get AD_Record_Access_UU.
   *
   * @return AD_Record_Access_UU
   */
  public String getAD_Record_Access_UU() {
    return (String) get_Value(COLUMNNAME_AD_Record_Access_UU);
  }

  public org.compiere.model.I_AD_Role getAD_Role() throws RuntimeException {
    return (org.compiere.model.I_AD_Role)
        MTable.get(getCtx(), org.compiere.model.I_AD_Role.Table_Name)
            .getPO(getAD_Role_ID(), get_TrxName());
  }

  /**
   * Set Role.
   *
   * @param AD_Role_ID Responsibility Role
   */
  public void setAD_Role_ID(int AD_Role_ID) {
    if (AD_Role_ID < 0) set_ValueNoCheck(COLUMNNAME_AD_Role_ID, null);
    else set_ValueNoCheck(COLUMNNAME_AD_Role_ID, Integer.valueOf(AD_Role_ID));
  }

  /**
   * Get Role.
   *
   * @return Responsibility Role
   */
  public int getAD_Role_ID() {
    Integer ii = (Integer) get_Value(COLUMNNAME_AD_Role_ID);
    if (ii == null) return 0;
    return ii;
  }

  public org.compiere.model.I_AD_Table getAD_Table() throws RuntimeException {
    return (org.compiere.model.I_AD_Table)
        MTable.get(getCtx(), org.compiere.model.I_AD_Table.Table_Name)
            .getPO(getAD_Table_ID(), get_TrxName());
  }

  /**
   * Set Table.
   *
   * @param AD_Table_ID Database Table information
   */
  public void setAD_Table_ID(int AD_Table_ID) {
    if (AD_Table_ID < 1) set_ValueNoCheck(COLUMNNAME_AD_Table_ID, null);
    else set_ValueNoCheck(COLUMNNAME_AD_Table_ID, Integer.valueOf(AD_Table_ID));
  }

  /**
   * Get Table.
   *
   * @return Database Table information
   */
  public int getAD_Table_ID() {
    Integer ii = (Integer) get_Value(COLUMNNAME_AD_Table_ID);
    if (ii == null) return 0;
    return ii;
  }

  /**
   * Get Record ID/ColumnName
   *
   * @return ID/ColumnName pair
   */
  public KeyNamePair getKeyNamePair() {
    return new KeyNamePair(getId(), String.valueOf(getAD_Table_ID()));
  }

  /**
   * Set Dependent Entities.
   *
   * @param IsDependentEntities Also check access in dependent entities
   */
  public void setIsDependentEntities(boolean IsDependentEntities) {
    set_Value(COLUMNNAME_IsDependentEntities, Boolean.valueOf(IsDependentEntities));
  }

  /**
   * Get Dependent Entities.
   *
   * @return Also check access in dependent entities
   */
  public boolean isDependentEntities() {
    Object oo = get_Value(COLUMNNAME_IsDependentEntities);
    if (oo != null) {
      if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
      return "Y".equals(oo);
    }
    return false;
  }

  /**
   * Set Exclude.
   *
   * @param IsExclude Exclude access to the data - if not selected Include access to the data
   */
  public void setIsExclude(boolean IsExclude) {
    set_Value(COLUMNNAME_IsExclude, Boolean.valueOf(IsExclude));
  }

  /**
   * Get Exclude.
   *
   * @return Exclude access to the data - if not selected Include access to the data
   */
  public boolean isExclude() {
    Object oo = get_Value(COLUMNNAME_IsExclude);
    if (oo != null) {
      if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
      return "Y".equals(oo);
    }
    return false;
  }

  /**
   * Set Read Only.
   *
   * @param IsReadOnly Field is read only
   */
  public void setIsReadOnly(boolean IsReadOnly) {
    set_Value(COLUMNNAME_IsReadOnly, Boolean.valueOf(IsReadOnly));
  }

  /**
   * Get Read Only.
   *
   * @return Field is read only
   */
  public boolean isReadOnly() {
    Object oo = get_Value(COLUMNNAME_IsReadOnly);
    if (oo != null) {
      if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
      return "Y".equals(oo);
    }
    return false;
  }

  /**
   * Set Record ID.
   *
   * @param Record_ID Direct internal record ID
   */
  public void setRecord_ID(int Record_ID) {
    if (Record_ID < 0) set_ValueNoCheck(COLUMNNAME_Record_ID, null);
    else set_ValueNoCheck(COLUMNNAME_Record_ID, Integer.valueOf(Record_ID));
  }

  /**
   * Get Record ID.
   *
   * @return Direct internal record ID
   */
  public int getRecord_ID() {
    Integer ii = (Integer) get_Value(COLUMNNAME_Record_ID);
    if (ii == null) return 0;
    return ii;
  }
}
