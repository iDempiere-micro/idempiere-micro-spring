package org.compiere.accounting;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

import org.compiere.model.I_M_Product_PO;
import org.compiere.orm.Query;
import org.idempiere.common.exceptions.DBException;
import org.idempiere.common.util.DB;
import org.compiere.util.Msg;

/**
 *	Product PO Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MProductPO.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MProductPO extends X_M_Product_PO
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1883198806060209516L;

	/**
	 * 	Get current PO of Product
	 * 	@param ctx context
	 *	@param M_Product_ID product
	 *	@param trxName transaction
	 *	@return PO - current vendor first
	 */
	public static MProductPO[] getOfProduct (Properties ctx, int M_Product_ID, String trxName)
	{
		final String whereClause = "M_Product_ID=?";
		List<MProductPO> list = new Query(ctx, I_M_Product_PO.Table_Name, whereClause, trxName)
									.setParameters(M_Product_ID)
									.setOnlyActiveRecords(true)
									.setOrderBy("IsCurrentVendor DESC")
									.list();
		return list.toArray(new MProductPO[list.size()]);
	}	//	getOfProduct

	/**
	 * 	Persistency Constructor
	 *	@param ctx context
	 *	@param ignored ignored
	 *	@param trxName transaction
	 */
	public MProductPO (Properties ctx, int ignored, String trxName)
	{
		super(ctx, 0, trxName);
		if (ignored != 0)
			throw new IllegalArgumentException("Multi-Key");
		else
		{
		//	setM_Product_ID (0);	// @M_Product_ID@
		//	setC_BPartner_ID (0);	// 0
		//	setVendorProductNo (null);	// @Value@
			setIsCurrentVendor (true);	// Y
		}
	}	//	MProduct_PO
	
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MProductPO(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MProductPO

	/**
	 * Before Save
	 * @param newRecord new
	 * @return true
	 */
	@Override
	protected boolean beforeSave(boolean newRecord) 
	{
		if ((newRecord && isActive() && isCurrentVendor()) || 
				(!newRecord &&
						(
								(is_ValueChanged("IsActive") && isActive()) // now active
								|| (is_ValueChanged("IsCurrentVendor") && isCurrentVendor()) // now current vendor
								|| is_ValueChanged("C_BPartner_ID")
								|| is_ValueChanged("M_Product_ID") 
						)
				)
			) 
		{
			int cnt = DB.getSQLValue(get_TrxName(),
							"SELECT COUNT(*) FROM M_Product_PO WHERE IsActive='Y' AND IsCurrentVendor='Y' AND C_BPartner_ID!=? AND M_Product_ID=?",
							getC_BPartner_ID(), getM_Product_ID());
			if (cnt > 0) {
				log.saveError("SaveError", Msg.getMsg(getCtx(), DBException.SAVE_ERROR_NOT_UNIQUE_MSG, true) + Msg.getElement(getCtx(), I_M_Product_PO.COLUMNNAME_IsCurrentVendor));
				return false;
			}
		}
		
		return true;
	}

}	//	MProductPO