package org.adempiere.exceptions;

import org.idempiere.common.exceptions.AdempiereException;

/**
 * Throwed when there is no Vendor Info for given Product.
 * @author Teo Sarca, www.arhipac.ro
 * 			<li>FR [ 2457781 ] Introduce NoVendorForProductException
 */
public class NoVendorForProductException extends
		AdempiereException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3412903630540562323L;

	/**
	 * @param productName M_Product Name
	 */
	public NoVendorForProductException(String productName)
	{
		super("@NoVendorForProduct@ "+productName);
	}
}