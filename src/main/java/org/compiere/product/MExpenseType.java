package org.compiere.product;

 

import java.sql.ResultSet;
import java.util.Properties;


/**
 *	Expense Type Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MExpenseType.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class MExpenseType extends X_S_ExpenseType
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5721855125106737886L;

	/**
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param S_ExpenseType_ID id
	 *	@param trxName transaction
	 */
	public MExpenseType (Properties ctx, int S_ExpenseType_ID, String trxName)
	{
		super (ctx, S_ExpenseType_ID, trxName);
	}	//	MExpenseType

	/**
	 * 	MExpenseType
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MExpenseType (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MExpenseType
	
	/** Cached Product			*/
	private MProduct	m_product = null;
	
	/**
	 * 	Get Product
	 *	@return product
	 */
	public MProduct getProduct()
	{
		if (m_product == null)
		{
			StringBuilder msgmp = new StringBuilder("S_ExpenseType_ID=").append(getS_ExpenseType_ID());
			MProduct[] products = MProduct.get(getCtx(), msgmp.toString(), get_TrxName());
			if (products.length > 0)
				m_product = products[0];
		}
		return m_product;
	}	//	getProduct
	
	
	/**
	 * 	beforeSave
	 *	@see PO#beforeSave(boolean)
	 *	@param newRecord
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (newRecord)
		{
			if (getValue() == null || getValue().length() == 0)
				setValue(getName());
			m_product = new MProduct(this);
			return m_product.save(get_TrxName());
		}
		return true;
	}	//	beforeSave
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
				
		MProduct prod = getProduct();
		if (prod.setExpenseType(this))
			prod.saveEx(get_TrxName());
		
		return success;
	}	//	afterSave
	
	
}	//	MExpenseType