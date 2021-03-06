package org.compiere.product;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;
import org.compiere.model.I_M_PriceList;
import org.compiere.model.I_M_ProductPrice;
import org.compiere.orm.Query;
import org.compiere.orm.TimeUtil;
import org.compiere.util.DisplayType;

/**
 * Price List Version Model
 *
 * @author Jorg Janke
 * @version $Id: MPriceListVersion.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MPriceListVersion extends X_M_PriceList_Version {
  /** */
  private static final long serialVersionUID = -3607494586575155059L;

  /**
   * Standard Constructor
   *
   * @param ctx context
   * @param M_PriceList_Version_ID id
   * @param trxName transaction
   */
  public MPriceListVersion(Properties ctx, int M_PriceList_Version_ID, String trxName) {
    super(ctx, M_PriceList_Version_ID, trxName);
    if (M_PriceList_Version_ID == 0) {
      //	setName (null);	// @#Date@
      //	setM_PriceList_ID (0);
      //	setValidFrom (TimeUtil.getDay(null));	// @#Date@
      //	setM_DiscountSchema_ID (0);
    }
  } //	MPriceListVersion

  /**
   * Load Constructor
   *
   * @param ctx context
   * @param rs result set
   * @param trxName transaction
   */
  public MPriceListVersion(Properties ctx, ResultSet rs, String trxName) {
    super(ctx, rs, trxName);
  } //	MPriceListVersion

  /**
   * Parent Constructor
   *
   * @param pl parent
   */
  public MPriceListVersion(I_M_PriceList pl) {
    this(pl.getCtx(), 0, pl.get_TrxName());
    setClientOrg(pl);
    setM_PriceList_ID(pl.getM_PriceList_ID());
  } //	MPriceListVersion

  /** Product Prices */
  private MProductPrice[] m_pp = null;
  /** Price List */
  private MPriceList m_pl = null;

  /**
   * Get Parent PriceList
   *
   * @return price List
   */
  public MPriceList getPriceList() {
    if (m_pl == null && getM_PriceList_ID() != 0)
      m_pl = MPriceList.get(getCtx(), getM_PriceList_ID(), null);
    return m_pl;
  } //	PriceList

  /**
   * Get Product Price
   *
   * @param refresh true if refresh
   * @return product price
   */
  public MProductPrice[] getProductPrice(boolean refresh) {
    if (m_pp != null && !refresh) return m_pp;
    m_pp = getProductPrice(null);
    return m_pp;
  } //	getProductPrice

  /**
   * Get Product Price
   *
   * @param whereClause optional where clause
   * @return product price
   */
  public MProductPrice[] getProductPrice(String whereClause) {
    String localWhereClause = I_M_ProductPrice.COLUMNNAME_M_PriceList_Version_ID + "=?";
    if (whereClause != null) localWhereClause += " " + whereClause;
    List<MProductPrice> list =
        new Query(getCtx(), I_M_ProductPrice.Table_Name, localWhereClause, get_TrxName())
            .setParameters(getM_PriceList_Version_ID())
            .list();
    MProductPrice[] pp = new MProductPrice[list.size()];
    list.toArray(pp);
    return pp;
  } //	getProductPrice

  /** Set Name to Valid From Date. If valid from not set, use today */
  public void setName() {
    if (getValidFrom() == null) setValidFrom(TimeUtil.getDay(null));
    if (getName() == null) {
      String name = DisplayType.getDateFormat(DisplayType.Date).format(getValidFrom());
      setName(name);
    }
  } //	setName

  /**
   * Before Save
   *
   * @param newRecord new
   * @return true
   */
  protected boolean beforeSave(boolean newRecord) {
    setName();

    return true;
  } //	beforeSave
} //	MPriceListVersion
