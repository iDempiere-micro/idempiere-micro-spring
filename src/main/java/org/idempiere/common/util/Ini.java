/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@idempiere.org or http://www.idempiere.org/license.html           *
 *****************************************************************************/
package org.idempiere.common.util;

import kotlin.Unit;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import org.springframework.beans.factory.annotation.Value;

//import org.idempiere.model.ModelValidationEngine; DAP TODO

/**
 *	Load & Save INI Settings from property file
 *	Initiated in Adempiere.startup
 *	Settings activated in ALogin.getIni
 *
 *  @author     Jorg Janke
 *  @version    $Id$
 *
 * @author Teo Sarca, www.arhipac.ro
 * 			<li>FR [ 1658127 ] Select charset encoding on import
 * 			<li>FR [ 2406123 ] Ini.getIni().saveProperties- fails if target directory does not exist
 */

public class Ini extends software.hsharp.core.util.Ini
{
}	//	Ini
