package org.adempiere.exceptions;

import org.compiere.model.I_C_BPartner;

/**
 * Thrown when Ship To Address is required for a BPartner but not found.
 * @author Teo Sarca, www.arhipac.ro
 */
public class BPartnerNoShipToAddressException extends BPartnerException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4388496060894704270L;
	public static final String AD_Message = "BPartnerNoShipToAddress";
	
	public BPartnerNoShipToAddressException(I_C_BPartner bp)
	{
		super(AD_Message, bp);
	}
}