/**********************************************************************
* This file is part of iDempiere ERP Open Source                      *
* http://www.idempiere.org                                            *
*                                                                     *
* Copyright (C) Contributors                                          *
*                                                                     *
* This program is free software; you can redistribute it and/or       *
* modify it under the terms of the GNU General Public License         *
* as published by the Free Software Foundation; either version 2      *
* of the License, or (at your option) any later version.              *
*                                                                     *
* This program is distributed in the hope that it will be useful,     *
* but WITHOUT ANY WARRANTY; without even the implied warranty of      *
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the        *
* GNU General Public License for more details.                        *
*                                                                     *
* You should have received a copy of the GNU General Public License   *
* along with this program; if not, write to the Free Software         *
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,          *
* MA 02110-1301, USA.                                                 *
*                                                                     *
* Contributors:                                                       *
* - Trek Global                                                       *
* - Carlos Ruiz                                                       *
**********************************************************************/
package org.idempiere.process;

import java.util.logging.Level;

import org.compiere.crm.MLanguage;
import org.compiere.model.IProcessInfoParameter;
import org.compiere.model.I_AD_Language;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.idempiere.common.util.AdempiereUserError;
import org.idempiere.common.util.Language;
import org.idempiere.common.util.Util;

/**
 *	Verify Language Configuration
 *
 * 	@author 	Carlos Ruiz - globalqss
 */
public class ChangeBaseLanguage extends SvrProcess
{
	/* The new language */
	private String p_Language;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		for (IProcessInfoParameter para : getParameter())
		{
			String name = para.getParameterName();
			if (name.equals("AD_Language")) {
				p_Language = (String) para.getParameter();
			} else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message
	 *  @throws Exception
	 */
	protected String doIt() throws Exception
	{
		if (log.isLoggable(Level.INFO)) log.info("AD_Language=" + p_Language);

		if (Util.isEmpty(p_Language))
			throw new AdempiereUserError("Language required");

		I_AD_Language lang = MLanguage.get(getCtx(), p_Language);
		if (lang.isBaseLanguage())
			throw new AdempiereUserError("Same base language");
		if (lang.isSystemLanguage())
			throw new AdempiereUserError("Base language cannot be a system language");

		if (Language.getBaseAD_Language().equals(p_Language))
			throw new AdempiereUserError("Same base language");

		// Disable the base flag on the actual
		I_AD_Language baselang = MLanguage.get(getCtx(), Language.getBaseAD_Language());
		baselang.setIsBaseLanguage(false);
		baselang.saveEx(get_TrxName());

		// Enable base flag on new language
		lang.setIsBaseLanguage(true);
		lang.saveEx(get_TrxName());
		
		Language.setBaseLanguage(p_Language);

		return "@OK@";
	}	//	doIt

}	//	ChangeBaseLanguage
