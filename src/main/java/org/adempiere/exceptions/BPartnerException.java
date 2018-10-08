package org.adempiere.exceptions;

import org.compiere.model.I_C_BPartner;
import org.idempiere.common.exceptions.AdempiereException;

/**
 * Thrown when an exception related to a BPartner happened.
 * @author Teo Sarca, www.arhipac.ro
 */
public abstract class BPartnerException extends AdempiereException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4311798678799373821L;
	private final int C_BPartner_ID;
	
	BPartnerException(String ad_message, I_C_BPartner bp)
	{
		super("@"+ad_message+"@ - "+(bp == null ? "?" : bp.getValue()+"_"+bp.getName()));
		if (bp != null)
		{
			this.C_BPartner_ID = bp.getC_BPartner_ID();
		}
		else
		{
			this.C_BPartner_ID = -1;
		}
	}
	
	/**
	 * @return the c_BPartner_ID
	 */
	public int getC_BPartner_ID()
	{
		return C_BPartner_ID;
	}
}