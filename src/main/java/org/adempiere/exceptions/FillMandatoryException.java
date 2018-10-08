package org.adempiere.exceptions;

import org.idempiere.common.exceptions.AdempiereException;

/**
 * Throwed when there are some fields that are mandatory but unfilled.
 * @author Teo Sarca, SC ARHIPAC SERVICE SRL
 */
public class FillMandatoryException extends AdempiereException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9074980284529933724L;

	public FillMandatoryException(String...fields)
	{
		super("@FillMandatory@ "+buildMessage(fields));
	}
	
	private static final String buildMessage(String...fields)
	{
		StringBuilder sb = new StringBuilder();
		for (String f : fields) {
			if (sb.length() > 0)
				sb.append(", ");
			sb.append("@").append(f).append("@");
		}
		return sb.toString();
	}
}