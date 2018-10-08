package org.adempiere.exceptions;

import org.idempiere.common.exceptions.AdempiereException;

/**
 * Throwed when an invoice is fully matched so no more receipts can be generated.
 * @author Teo Sarca, www.arhipac.ro
 */
public class InvoiceFullyMatchedException extends AdempiereException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7474922528576404203L;
	
	public static final String AD_Message = "InvoiceFullyMatched";
	
	public InvoiceFullyMatchedException()
	{
		super("@"+AD_Message+"@");
	}
}