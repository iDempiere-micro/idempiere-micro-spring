package org.compiere.accounting;

import java.sql.ResultSet;
import java.util.Iterator;
import java.util.Properties;

import org.compiere.model.I_C_CashBook;
import org.compiere.orm.Query;
import org.idempiere.common.util.CCache;
import org.idempiere.common.util.CLogger;

/**
 *	Cash Book Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MCashBook.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 *  @author red1 - FR: [ 2214883 ] Remove SQL code and Replace for Query
 */
public class MCashBook extends X_C_CashBook
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3991585668643587699L;

	/**
	 * 	Get MCashBook from Cache
	 *	@param ctx context
	 *	@param C_CashBook_ID id
	 *	@return MCashBook
	 */
	public static MCashBook get (Properties ctx, int C_CashBook_ID)
	{
		return get(ctx, C_CashBook_ID, null);
	}	//	get
	
	/**
	 * Gets MCashBook from Cache under transaction scope
	 * @param ctx 				context	
	 * @param C_CashBook_ID		id of cashbook to load
	 * @param trxName			transaction name
	 * @return   Cashbook
	 */
	public static MCashBook get(Properties ctx, int C_CashBook_ID, String trxName)
	{
		Integer key = new Integer (C_CashBook_ID);
		MCashBook retValue = (MCashBook) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MCashBook (ctx, C_CashBook_ID, trxName);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**
	 * 	Get CashBook for Org and Currency
	 *	@param ctx context
	 *	@param AD_Org_ID org
	 *	@param C_Currency_ID currency
	 *	@return cash book or null
	 */
	public static MCashBook get (Properties ctx, int AD_Org_ID, int C_Currency_ID)
	{
		//	Try from cache
		Iterator<MCashBook> it = s_cache.values().iterator();
		while (it.hasNext())
		{
			MCashBook cb = (MCashBook)it.next();
			if (cb.getAD_Org_ID() == AD_Org_ID && cb.getC_Currency_ID() == C_Currency_ID)
				return cb;
		}
		
		//	Get from DB
		final String whereClause = I_C_CashBook.COLUMNNAME_AD_Org_ID+"=? AND "+I_C_CashBook.COLUMNNAME_C_Currency_ID+"=?";
		MCashBook retValue = new Query(ctx,I_C_CashBook.Table_Name,whereClause,null)
		.setParameters(AD_Org_ID,C_Currency_ID)
		.setOrderBy("IsDefault DESC")
		.first();
		if (retValue!=null)
			{
				Integer key = new Integer (retValue.getC_CashBook_ID());
				s_cache.put (key, retValue);
			}
		return retValue;
	}	//	get
	

	/**	Cache						*/
	private static CCache<Integer,MCashBook> s_cache
		= new CCache<Integer,MCashBook>(I_C_CashBook.Table_Name, 20);
	/**	Static Logger	*/
	@SuppressWarnings("unused")
	private static CLogger	s_log	= CLogger.getCLogger (MCashBook.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_CashBook_ID id
	 *	@param trxName transaction
	 */
	public MCashBook (Properties ctx, int C_CashBook_ID, String trxName)
	{
		super (ctx, C_CashBook_ID, trxName);
	}	//	MCashBook

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MCashBook (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MCashBook
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (newRecord && success)
			insert_Accounting("C_CashBook_Acct", "C_AcctSchema_Default", null);

		return success;
	}	//	afterSave

}	//	MCashBook