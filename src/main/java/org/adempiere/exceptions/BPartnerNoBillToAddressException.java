package org.adempiere.exceptions;

import org.compiere.model.I_C_BPartner;

/**
 * Thrown when Bill To Address is required for a BPartner but not found.
 * @author Teo Sarca, www.arhipac.ro
 */
public class BPartnerNoBillToAddressException extends BPartnerException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8423260338845096466L;
	public static final String AD_Message = "BPartnerNoBillToAddress";
	
	public BPartnerNoBillToAddressException(I_C_BPartner bp)
	{
		super(AD_Message, bp);
	}
}