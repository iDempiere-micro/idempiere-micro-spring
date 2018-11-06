package org.compiere.conversionrate;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.model.I_C_ConversionType;
import org.idempiere.common.util.CCache;
import org.idempiere.common.util.DB;

/**
 *	Currency Conversion Type Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MConversionType.java,v 1.3 2006/07/30 00:58:04 jjanke Exp $
 */
public class MConversionType extends X_C_ConversionType
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7198388106444590667L;

	/**
	 * These static variables won't have to change unless we change the AD.
	 * I've added them here for convenience. Daniel Tamm [usrdno] 090528
	 */
	public static final int TYPE_SPOT = 114;
	public static final int TYPE_PERIOD_END = 115;
	public static final int TYPE_AVERAGE = 200;
	public static final int TYPE_COMPANY = 201;
	
	/**
	 * 	Get Default Conversion Rate for Client/Org
	 *	@param AD_Client_ID client
	 *	@return C_ConversionType_ID or 0 if not found
	 */
	public static int getDefault (int AD_Client_ID)
	{
		//	Try Cache
		Integer key = new Integer (AD_Client_ID);
		Integer ii = (Integer)s_cache.get(key);
		if (ii != null)
			return ii;
			
		//	Get from DB
		int C_ConversionType_ID = 0;
		String sql = "SELECT C_ConversionType_ID "
			+ "FROM C_ConversionType "
			+ "WHERE IsActive='Y'"
			+ " AND AD_Client_ID IN (0,?) "		//	#1
			+ "ORDER BY IsDefault DESC, AD_Client_ID DESC";
		C_ConversionType_ID = DB.getSQLValue(null, sql, AD_Client_ID);
		//	Return
		s_cache.put(key, new Integer(C_ConversionType_ID));
		return C_ConversionType_ID;
	}	//	getDefault
	
	/**	Cache Client-ID					*/
	private static CCache<Integer,Integer> s_cache = new CCache<Integer,Integer>(I_C_ConversionType.Table_Name, 4);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_ConversionType_ID id
	 *	@param trxName transaction
	 */
	public MConversionType(Properties ctx, int C_ConversionType_ID, String trxName)
	{
		super(ctx, C_ConversionType_ID, trxName);
	}	//	MConversionType

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MConversionType(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MConversionType

}	//	MConversionType