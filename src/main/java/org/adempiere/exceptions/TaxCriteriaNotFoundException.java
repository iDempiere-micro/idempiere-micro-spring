package org.adempiere.exceptions;


import org.idempiere.common.exceptions.AdempiereException;

/**
 * Throw when a tax criteria was not found
 * @author Teo Sarca, www.arhipac.ro
 * 			<li>FR [ 2758097 ] Implement TaxNotFoundException
 */
public class TaxCriteriaNotFoundException extends AdempiereException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8192276006656371964L;
	
	private static final String AD_Message = "TaxCriteriaNotFound";
	
	public TaxCriteriaNotFoundException(String criteriaName, int criteria_ID)
	{
		super(buildMessage(criteriaName, criteria_ID));
	}
	
	private static final String buildMessage (String criteriaName, int criteria_ID)
	{
		StringBuffer msg = new StringBuffer("@").append(AD_Message).append("@");
		msg.append(" @").append(criteriaName).append("@");
		msg.append(" (ID ").append(criteria_ID).append(")");
		return msg.toString();
	}
	

}