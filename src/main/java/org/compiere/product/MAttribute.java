package org.compiere.product;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import org.compiere.model.I_M_Attribute;
import org.compiere.model.I_M_AttributeInstance;
import org.compiere.model.I_M_AttributeValue;
import org.compiere.orm.Query;
import org.idempiere.common.util.CLogger;
import org.idempiere.common.util.DB;
import org.idempiere.common.util.Env;

/**
 * Product Attribute
 *
 * @author Jorg Janke
 * @version $Id: MAttribute.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MAttribute extends X_M_Attribute {
  /** */
  private static final long serialVersionUID = 7869800574413317999L;

  /**
   * Get Attributes Of Client
   *
   * @param ctx Properties
   * @param onlyProductAttributes only Product Attributes
   * @param onlyListAttributes only List Attributes
   * @return array of attributes
   */
  public static MAttribute[] getOfClient(
      Properties ctx, boolean onlyProductAttributes, boolean onlyListAttributes) {
    int AD_Client_ID = Env.getADClientID(ctx);
    String sql = "";
    ArrayList<Object> params = new ArrayList<Object>();
    params.add(AD_Client_ID);
    if (onlyProductAttributes) {
      sql += " AND IsInstanceAttribute=?";
      params.add(false);
    }
    if (onlyListAttributes) {
      sql += " AND AttributeValueType=?";
      params.add(MAttribute.ATTRIBUTEVALUETYPE_List);
    }
    StringBuilder whereClause = new StringBuilder("AD_Client_ID=?").append(sql);

    List<MAttribute> list =
        new Query(ctx, I_M_Attribute.Table_Name, whereClause.toString(), null)
            .setParameters(params)
            .setOnlyActiveRecords(true)
            .setOrderBy("Name")
            .list();

    MAttribute[] retValue = new MAttribute[list.size()];
    list.toArray(retValue);
    if (s_log.isLoggable(Level.FINE))
      s_log.fine("AD_Client_ID=" + AD_Client_ID + " - #" + list.size());
    return retValue;
  } //	getOfClient

  /** Logger */
  private static CLogger s_log = CLogger.getCLogger(MAttribute.class);

  /**
   * Standard Constructor
   *
   * @param ctx context
   * @param M_Attribute_ID id
   * @param trxName transaction
   */
  public MAttribute(Properties ctx, int M_Attribute_ID, String trxName) {
    super(ctx, M_Attribute_ID, trxName);
    if (M_Attribute_ID == 0) {
      setAttributeValueType(X_M_Attribute.ATTRIBUTEVALUETYPE_StringMax40);
      setIsInstanceAttribute(false);
      setIsMandatory(false);
    }
  } //	MAttribute

  /**
   * Load Constructor
   *
   * @param ctx context
   * @param rs result set
   * @param trxName transaction
   */
  public MAttribute(Properties ctx, ResultSet rs, String trxName) {
    super(ctx, rs, trxName);
  } //	MAttribute

  /** Values */
  private MAttributeValue[] m_values = null;

  /**
   * Get Values if List
   *
   * @return Values or null if not list
   */
  public MAttributeValue[] getMAttributeValues() {
    if (m_values == null && X_M_Attribute.ATTRIBUTEVALUETYPE_List.equals(getAttributeValueType())) {
      final String whereClause = I_M_AttributeValue.COLUMNNAME_M_Attribute_ID + "=?";
      List<MAttributeValue> list = new ArrayList<MAttributeValue>();
      if (!isMandatory()) list.add(null);
      list =
          new Query(getCtx(), I_M_AttributeValue.Table_Name, whereClause, null)
              .setParameters(getMAttribute_ID())
              .setOrderBy("Value")
              .list();
      m_values = new MAttributeValue[list.size()];
      list.toArray(m_values);
    }
    return m_values;
  } //	getValues

  /**
   * ************************************************************************ Get Attribute Instance
   *
   * @param M_AttributeSetInstance_ID attribute set instance
   * @return Attribute Instance or null
   */
  public MAttributeInstance getMAttributeInstance(int M_AttributeSetInstance_ID) {
    final String whereClause =
        I_M_AttributeInstance.COLUMNNAME_M_Attribute_ID
            + "=? AND "
            + I_M_AttributeInstance.COLUMNNAME_M_AttributeSetInstance_ID
            + "=?";
    MAttributeInstance retValue =
        new Query(getCtx(), I_M_AttributeInstance.Table_Name, whereClause, get_TrxName())
            .setParameters(getMAttribute_ID(), M_AttributeSetInstance_ID)
            .first();

    return retValue;
  } //	getAttributeInstance

  /**
   * Set Attribute Instance
   *
   * @param value value
   * @param M_AttributeSetInstance_ID id
   */
  public void setMAttributeInstance(int M_AttributeSetInstance_ID, MAttributeValue value) {
    MAttributeInstance instance = getMAttributeInstance(M_AttributeSetInstance_ID);
    if (instance == null) {
      if (value != null)
        instance =
            new MAttributeInstance(
                getCtx(),
                getMAttribute_ID(),
                M_AttributeSetInstance_ID,
                value.getMAttributeValue_ID(),
                value.getName(),
                get_TrxName()); //	Cached !!
      else
        instance =
            new MAttributeInstance(
                getCtx(), getMAttribute_ID(), M_AttributeSetInstance_ID, 0, null, get_TrxName());
    } else {
      if (value != null) {
        instance.setM_AttributeValue_ID(value.getMAttributeValue_ID());
        instance.setValue(value.getName()); // 	Cached !!
      } else {
        instance.setM_AttributeValue_ID(0);
        instance.setValue(null);
      }
    }
    instance.saveEx();
  } //	setAttributeInstance

  /**
   * Set Attribute Instance
   *
   * @param value string value
   * @param M_AttributeSetInstance_ID id
   */
  public void setMAttributeInstance(int M_AttributeSetInstance_ID, String value) {
    MAttributeInstance instance = getMAttributeInstance(M_AttributeSetInstance_ID);
    if (instance == null)
      instance =
          new MAttributeInstance(
              getCtx(), getMAttribute_ID(), M_AttributeSetInstance_ID, value, get_TrxName());
    else instance.setValue(value);
    instance.saveEx();
  } //	setAttributeInstance

  /**
   * Set Attribute Instance
   *
   * @param value number value
   * @param M_AttributeSetInstance_ID id
   */
  public void setMAttributeInstance(int M_AttributeSetInstance_ID, BigDecimal value) {
    MAttributeInstance instance = getMAttributeInstance(M_AttributeSetInstance_ID);
    if (instance == null)
      instance =
          new MAttributeInstance(
              getCtx(), getMAttribute_ID(), M_AttributeSetInstance_ID, value, get_TrxName());
    else instance.setValueNumber(value);
    instance.saveEx();
  } //	setAttributeInstance

  /**
   * String Representation
   *
   * @return info
   */
  public String toString() {
    StringBuilder sb = new StringBuilder("MAttribute[");
    sb.append(getId())
        .append("-")
        .append(getName())
        .append(",Type=")
        .append(getAttributeValueType())
        .append(",Instance=")
        .append(isInstanceAttribute())
        .append("]");
    return sb.toString();
  } //	toString

  /**
   * AfterSave
   *
   * @param newRecord new
   * @param success success
   * @return success
   */
  protected boolean afterSave(boolean newRecord, boolean success) {
    if (!success) return success;
    //	Changed to Instance Attribute
    if (!newRecord && is_ValueChanged("IsInstanceAttribute") && isInstanceAttribute()) {
      StringBuilder sql =
          new StringBuilder("UPDATE M_AttributeSet mas ")
              .append("SET IsInstanceAttribute='Y' ")
              .append("WHERE IsInstanceAttribute='N'")
              .append(" AND EXISTS (SELECT * FROM M_AttributeUse mau ")
              .append("WHERE mas.M_AttributeSet_ID=mau.M_AttributeSet_ID")
              .append(" AND mau.M_Attribute_ID=")
              .append(getMAttribute_ID())
              .append(")");
      int no = DB.executeUpdate(sql.toString(), get_TrxName());
      if (log.isLoggable(Level.FINE)) log.fine("AttributeSet Instance set #" + no);
    }
    return success;
  } //	afterSave
} //	MAttribute
