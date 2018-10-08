package org.adempiere.exceptions;

import org.compiere.model.I_C_BPartner;

/**
 * Thrown when an location/address is required for a BPartner but not found.
 * @author Teo Sarca, www.arhipac.ro
 */
public class BPartnerNoAddressException extends BPartnerException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1892858395845764918L;
	public static final String AD_Message = "BPartnerNoAddress";

	/**
	 * @param message
	 * @param bp
	 */
	public BPartnerNoAddressException(I_C_BPartner bp)
	{
		super(AD_Message, bp);
	}

}