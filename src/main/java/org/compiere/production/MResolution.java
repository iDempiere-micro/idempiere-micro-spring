package org.compiere.production;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.model.I_R_Resolution;
import org.idempiere.common.util.CCache;

/**
 * 	Request Resolution Model
 *  @author Jorg Janke
 *  @version $Id: MResolution.java,v 1.2 2006/07/30 00:51:03 jjanke Exp $
 */
public class MResolution extends X_R_Resolution
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9163046533055602877L;



	/**
	 * 	Get MResolution from Cache
	 *	@param ctx context
	 *	@param R_Resolution_ID id
	 *	@return MResolution
	 */
	public static MResolution get (Properties ctx, int R_Resolution_ID)
	{
		Integer key = new Integer (R_Resolution_ID);
		MResolution retValue = (MResolution) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MResolution (ctx, R_Resolution_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,MResolution>	s_cache	= new CCache<Integer,MResolution>(I_R_Resolution.Table_Name, 10);
	
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param R_Resolution_ID id
	 *	@param trxName
	 */
	public MResolution (Properties ctx, int R_Resolution_ID, String trxName)
	{
		super (ctx, R_Resolution_ID, trxName);
	}	//	MResolution

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MResolution (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MResolution
	
}	//	MResolution