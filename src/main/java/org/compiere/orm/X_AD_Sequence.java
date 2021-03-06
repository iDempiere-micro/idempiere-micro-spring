package org.compiere.orm;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.I_AD_Sequence;
import org.idempiere.orm.I_Persistent;
import org.idempiere.orm.POInfo;

/**
 * Generated Model for AD_Sequence
 *
 * @author iDempiere (generated)
 * @version Release 5.1 - $Id$
 */
public class X_AD_Sequence extends BasePOName implements I_AD_Sequence, I_Persistent {

  /** */
  private static final long serialVersionUID = 20171031L;

  /** Standard Constructor */
  public X_AD_Sequence(Properties ctx, int AD_Sequence_ID, String trxName) {
    super(ctx, AD_Sequence_ID, trxName);
  }

  /** Load Constructor */
  public X_AD_Sequence(Properties ctx, ResultSet rs, String trxName) {
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
    StringBuffer sb = new StringBuffer("X_AD_Sequence[").append(getId()).append("]");
    return sb.toString();
  }

  /**
   * Set Sequence.
   *
   * @param AD_Sequence_ID Document Sequence
   */
  public void setAD_Sequence_ID(int AD_Sequence_ID) {
    if (AD_Sequence_ID < 1) set_ValueNoCheck(COLUMNNAME_AD_Sequence_ID, null);
    else set_ValueNoCheck(COLUMNNAME_AD_Sequence_ID, Integer.valueOf(AD_Sequence_ID));
  }

  /**
   * Get Sequence.
   *
   * @return Document Sequence
   */
  public int getAD_Sequence_ID() {
    Integer ii = (Integer) get_Value(COLUMNNAME_AD_Sequence_ID);
    if (ii == null) return 0;
    return ii;
  }

  /**
   * Set AD_Sequence_UU.
   *
   * @param AD_Sequence_UU AD_Sequence_UU
   */
  public void setAD_Sequence_UU(String AD_Sequence_UU) {
    set_Value(COLUMNNAME_AD_Sequence_UU, AD_Sequence_UU);
  }

  /**
   * Get AD_Sequence_UU.
   *
   * @return AD_Sequence_UU
   */
  public String getAD_Sequence_UU() {
    return (String) get_Value(COLUMNNAME_AD_Sequence_UU);
  }

  /**
   * Set Current Next.
   *
   * @param CurrentNext The next number to be used
   */
  public void setCurrentNext(int CurrentNext) {
    set_Value(COLUMNNAME_CurrentNext, Integer.valueOf(CurrentNext));
  }

  /**
   * Get Current Next.
   *
   * @return The next number to be used
   */
  public int getCurrentNext() {
    Integer ii = (Integer) get_Value(COLUMNNAME_CurrentNext);
    if (ii == null) return 0;
    return ii;
  }

  /**
   * Set Current Next (System).
   *
   * @param CurrentNextSys Next sequence for system use
   */
  public void setCurrentNextSys(int CurrentNextSys) {
    set_Value(COLUMNNAME_CurrentNextSys, Integer.valueOf(CurrentNextSys));
  }

  /**
   * Get Current Next (System).
   *
   * @return Next sequence for system use
   */
  public int getCurrentNextSys() {
    Integer ii = (Integer) get_Value(COLUMNNAME_CurrentNextSys);
    if (ii == null) return 0;
    return ii;
  }

  /**
   * Set Date Column.
   *
   * @param DateColumn Fully qualified date column
   */
  public void setDateColumn(String DateColumn) {
    set_Value(COLUMNNAME_DateColumn, DateColumn);
  }

  /**
   * Get Date Column.
   *
   * @return Fully qualified date column
   */
  public String getDateColumn() {
    return (String) get_Value(COLUMNNAME_DateColumn);
  }

  /**
   * Set Decimal Pattern.
   *
   * @param DecimalPattern Java Decimal Pattern
   */
  public void setDecimalPattern(String DecimalPattern) {
    set_Value(COLUMNNAME_DecimalPattern, DecimalPattern);
  }

  /**
   * Get Decimal Pattern.
   *
   * @return Java Decimal Pattern
   */
  public String getDecimalPattern() {
    return (String) get_Value(COLUMNNAME_DecimalPattern);
  }

  /**
   * Set Description.
   *
   * @param Description Optional short description of the record
   */
  public void setDescription(String Description) {
    set_Value(COLUMNNAME_Description, Description);
  }

  /**
   * Get Description.
   *
   * @return Optional short description of the record
   */
  public String getDescription() {
    return (String) get_Value(COLUMNNAME_Description);
  }

  /**
   * Set Increment.
   *
   * @param IncrementNo The number to increment the last document number by
   */
  public void setIncrementNo(int IncrementNo) {
    set_Value(COLUMNNAME_IncrementNo, Integer.valueOf(IncrementNo));
  }

  /**
   * Get Increment.
   *
   * @return The number to increment the last document number by
   */
  public int getIncrementNo() {
    Integer ii = (Integer) get_Value(COLUMNNAME_IncrementNo);
    if (ii == null) return 0;
    return ii;
  }

  /**
   * Set Activate Audit.
   *
   * @param IsAudited Activate Audit Trail of what numbers are generated
   */
  public void setIsAudited(boolean IsAudited) {
    set_Value(COLUMNNAME_IsAudited, Boolean.valueOf(IsAudited));
  }

  /**
   * Get Activate Audit.
   *
   * @return Activate Audit Trail of what numbers are generated
   */
  public boolean isAudited() {
    Object oo = get_Value(COLUMNNAME_IsAudited);
    if (oo != null) {
      if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
      return "Y".equals(oo);
    }
    return false;
  }

  /**
   * Set Auto numbering.
   *
   * @param IsAutoSequence Automatically assign the next number
   */
  public void setIsAutoSequence(boolean IsAutoSequence) {
    set_Value(COLUMNNAME_IsAutoSequence, Boolean.valueOf(IsAutoSequence));
  }

  /**
   * Get Auto numbering.
   *
   * @return Automatically assign the next number
   */
  public boolean isAutoSequence() {
    Object oo = get_Value(COLUMNNAME_IsAutoSequence);
    if (oo != null) {
      if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
      return "Y".equals(oo);
    }
    return false;
  }

  /**
   * Set Organization level.
   *
   * @param IsOrgLevelSequence This sequence can be defined for each organization
   */
  public void setIsOrgLevelSequence(boolean IsOrgLevelSequence) {
    set_Value(COLUMNNAME_IsOrgLevelSequence, Boolean.valueOf(IsOrgLevelSequence));
  }

  /**
   * Get Organization level.
   *
   * @return This sequence can be defined for each organization
   */
  public boolean isOrgLevelSequence() {
    Object oo = get_Value(COLUMNNAME_IsOrgLevelSequence);
    if (oo != null) {
      if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
      return "Y".equals(oo);
    }
    return false;
  }

  /**
   * Set Used for Record ID.
   *
   * @param IsTableID The document number will be used as the record key
   */
  public void setIsTableID(boolean IsTableID) {
    set_Value(COLUMNNAME_IsTableID, Boolean.valueOf(IsTableID));
  }

  /**
   * Get Used for Record ID.
   *
   * @return The document number will be used as the record key
   */
  public boolean isTableID() {
    Object oo = get_Value(COLUMNNAME_IsTableID);
    if (oo != null) {
      if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
      return "Y".equals(oo);
    }
    return false;
  }

  /**
   * Set Org Column.
   *
   * @param OrgColumn Fully qualified Organization column (AD_Org_ID)
   */
  public void setOrgColumn(String OrgColumn) {
    set_Value(COLUMNNAME_OrgColumn, OrgColumn);
  }

  /**
   * Get Org Column.
   *
   * @return Fully qualified Organization column (AD_Org_ID)
   */
  public String getOrgColumn() {
    return (String) get_Value(COLUMNNAME_OrgColumn);
  }

  /**
   * Set Prefix.
   *
   * @param Prefix Prefix before the sequence number
   */
  public void setPrefix(String Prefix) {
    set_Value(COLUMNNAME_Prefix, Prefix);
  }

  /**
   * Get Prefix.
   *
   * @return Prefix before the sequence number
   */
  public String getPrefix() {
    return (String) get_Value(COLUMNNAME_Prefix);
  }

  /**
   * Set Restart sequence every month.
   *
   * @param StartNewMonth Restart sequence every month
   */
  public void setStartNewMonth(boolean StartNewMonth) {
    set_Value(COLUMNNAME_StartNewMonth, Boolean.valueOf(StartNewMonth));
  }

  /**
   * Get Restart sequence every month.
   *
   * @return Restart sequence every month
   */
  public boolean isStartNewMonth() {
    Object oo = get_Value(COLUMNNAME_StartNewMonth);
    if (oo != null) {
      if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
      return "Y".equals(oo);
    }
    return false;
  }

  /**
   * Set Restart sequence every Year.
   *
   * @param StartNewYear Restart the sequence with Start on every 1/1
   */
  public void setStartNewYear(boolean StartNewYear) {
    set_Value(COLUMNNAME_StartNewYear, Boolean.valueOf(StartNewYear));
  }

  /**
   * Get Restart sequence every Year.
   *
   * @return Restart the sequence with Start on every 1/1
   */
  public boolean isStartNewYear() {
    Object oo = get_Value(COLUMNNAME_StartNewYear);
    if (oo != null) {
      if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
      return "Y".equals(oo);
    }
    return false;
  }

  /**
   * Set Start No.
   *
   * @param StartNo Starting number/position
   */
  public void setStartNo(int StartNo) {
    set_Value(COLUMNNAME_StartNo, Integer.valueOf(StartNo));
  }

  /**
   * Get Start No.
   *
   * @return Starting number/position
   */
  public int getStartNo() {
    Integer ii = (Integer) get_Value(COLUMNNAME_StartNo);
    if (ii == null) return 0;
    return ii;
  }

  /**
   * Set Suffix.
   *
   * @param Suffix Suffix after the number
   */
  public void setSuffix(String Suffix) {
    set_Value(COLUMNNAME_Suffix, Suffix);
  }

  /**
   * Get Suffix.
   *
   * @return Suffix after the number
   */
  public String getSuffix() {
    return (String) get_Value(COLUMNNAME_Suffix);
  }

  /**
   * Set Value Format.
   *
   * @param VFormat Format of the value; Can contain fixed format elements, Variables:
   *     "_lLoOaAcCa09"
   */
  public void setVFormat(String VFormat) {
    set_Value(COLUMNNAME_VFormat, VFormat);
  }

  /**
   * Get Value Format.
   *
   * @return Format of the value; Can contain fixed format elements, Variables: "_lLoOaAcCa09"
   */
  public String getVFormat() {
    return (String) get_Value(COLUMNNAME_VFormat);
  }
}
