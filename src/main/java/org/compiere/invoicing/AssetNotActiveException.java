/**
 * 
 */
package org.compiere.invoicing;

import org.compiere.invoicing.AssetException;

/**
 * @author Teo Sarca, www.arhipac.ro
 *
 */
public class AssetNotActiveException extends AssetException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -958395266586669844L;

	public AssetNotActiveException(int A_Asset_ID)
	{
		super("@AssetNotActive@ (ID="+A_Asset_ID+")");
	}
}
