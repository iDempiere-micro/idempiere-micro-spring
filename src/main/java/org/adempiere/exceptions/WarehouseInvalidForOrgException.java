package org.adempiere.exceptions;


import org.idempiere.common.exceptions.AdempiereException;

/**
 * Throw when there is no Warehouse for Organization.
 * @author victor.perez@e-evolution.com, www.e-evolution.com
 * 			<li>FR [ 2457781 ] Introduce WarehouseInvalidForOrgException
 * 			<li>https://sourceforge.net/tracker/index.php?func=detail&aid=2877521&group_id=176962&atid=879335
 */
public class WarehouseInvalidForOrgException extends AdempiereException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8637554073291880392L;

	/**
	 * @param productName M_Product Name
	 */
	public WarehouseInvalidForOrgException(String wname ,String oname)
	{
		super(wname+ " @M_Warehouse_ID@  @NotValid@  @AD_Org_ID@  "+ oname);
	}
}