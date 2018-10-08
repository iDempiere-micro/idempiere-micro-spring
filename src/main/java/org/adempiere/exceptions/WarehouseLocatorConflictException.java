package org.adempiere.exceptions;

import org.compiere.model.I_M_Locator;
import org.compiere.model.I_M_Warehouse;
import org.idempiere.common.exceptions.AdempiereException;

/**
 * Throwed when a document warehouse does not match with document or document line locator.
 * @author Teo Sarca, www.arhipac.ro
 */
public class WarehouseLocatorConflictException extends AdempiereException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4812283712626432829L;

	/**
	 * @param wh warehouse
	 * @param locator locator 
	 * @param lineNo Document Line#
	 */
	public WarehouseLocatorConflictException(I_M_Warehouse wh, I_M_Locator locator, int lineNo)
	{
		super ("@WarehouseLocatorConflict@"
				+" @M_Warehouse_ID@: "+(wh != null ? wh.getName() : "?")
				+" @M_Locator_ID@: "+(locator != null ? locator.getValue() : "?")
				+" @Line@: "+lineNo
		);
	}
}