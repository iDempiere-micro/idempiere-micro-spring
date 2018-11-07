package org.compiere.production;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.model.I_R_Category;
import org.idempiere.common.util.CCache;

/**
 * 	Request Category Model
 *  @author Jorg Janke
 *  @version $Id: MRequestCategory.java,v 1.2 2006/07/30 00:51:05 jjanke Exp $
 */
public class MRequestCategory extends X_R_Category
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9174605980194362716L;


	/**
	 * 	Get MCategory from Cache
	 *	@param ctx context
	 *	@param R_Category_ID id
	 *	@return MCategory
	 */
	public static MRequestCategory get (Properties ctx, int R_Category_ID)
	{
		Integer key = new Integer (R_Category_ID);
		MRequestCategory retValue = (MRequestCategory) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MRequestCategory (ctx, R_Category_ID, null);
		if (retValue.getId() != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static CCache<Integer,MRequestCategory>	s_cache	
		= new CCache<Integer,MRequestCategory>(I_R_Category.Table_Name, 20);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param R_Category_ID id
	 *	@param trxName trx
	 */
	public MRequestCategory (Properties ctx, int R_Category_ID, String trxName)
	{
		super (ctx, R_Category_ID, trxName);
	}	//	MCategory

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MRequestCategory (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MCategory
	
}	//	MCategory