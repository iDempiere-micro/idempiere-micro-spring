/**
 * **************************************************************************** Product: Compiere
 * ERP & CRM Smart Business Solution * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved. *
 * This program is free software; you can redistribute it and/or modify it * under the terms version
 * 2 of the GNU General Public License as published * by the Free Software Foundation. This program
 * is distributed in the hope * that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. * See the GNU General
 * Public License for more details. * You should have received a copy of the GNU General Public
 * License along * with this program; if not, write to the Free Software Foundation, Inc., * 59
 * Temple Place, Suite 330, Boston, MA 02111-1307 USA. * For the text or an alternative of this
 * public license, you may reach us * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA
 * 95054, USA * or via info@compiere.org or http://www.compiere.org/license.html *
 * ***************************************************************************
 */
package org.idempiere.process;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.accounting.MProduct;
import org.compiere.model.IProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.product.MProductBOM;
import org.idempiere.common.util.DB;
import org.idempiere.common.util.Env;

/**
 * Validate BOM
 *
 * @author Jorg Janke
 * @version $Id: BOMVerify.java,v 1.1 2007/07/23 05:34:35 mfuggle Exp $
 */
public class BOMVerify extends SvrProcess {
  /** The Product */
  private int p_M_Product_ID = 0;
  /** Product Category */
  private int p_M_Product_Category_ID = 0;
  /** Re-Validate */
  private boolean p_IsReValidate = false;

  private boolean p_fromButton = false;

  /** List of Products */
  private ArrayList<MProduct> foundproducts = new ArrayList<MProduct>();

  private ArrayList<MProduct> validproducts = new ArrayList<MProduct>();
  private ArrayList<MProduct> invalidproducts = new ArrayList<MProduct>();
  private ArrayList<MProduct> containinvalidproducts = new ArrayList<MProduct>();
  private ArrayList<MProduct> checkedproducts = new ArrayList<MProduct>();

  /** Prepare */
  protected void prepare() {
    IProcessInfoParameter[] para = getParameter();
    for (int i = 0; i < para.length; i++) {
      String name = para[i].getParameterName();
      if (para[i].getParameter() == null) ;
      else if (name.equals("M_Product_ID")) p_M_Product_ID = para[i].getParameterAsInt();
      else if (name.equals("M_Product_Category_ID"))
        p_M_Product_Category_ID = para[i].getParameterAsInt();
      else if (name.equals("IsReValidate")) p_IsReValidate = "Y".equals(para[i].getParameter());
      else log.log(Level.SEVERE, "Unknown Parameter: " + name);
    }
    if (p_M_Product_ID == 0) p_M_Product_ID = getRecord_ID();
    p_fromButton = (getRecord_ID() > 0);
  } //	prepare

  /**
   * Process
   *
   * @return Info
   * @throws Exception
   */
  protected String doIt() throws Exception {
    if (p_M_Product_ID != 0) {
      if (log.isLoggable(Level.INFO)) log.info("M_Product_ID=" + p_M_Product_ID);
      checkProduct(new MProduct(getCtx(), p_M_Product_ID, get_TrxName()));
      return "Product Checked";
    }
    if (log.isLoggable(Level.INFO))
      log.info(
          "M_Product_Category_ID=" + p_M_Product_Category_ID + ", IsReValidate=" + p_IsReValidate);
    //
    int counter = 0;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String sql = "SELECT M_Product_ID FROM M_Product " + "WHERE IsBOM='Y' AND ";
    if (p_M_Product_Category_ID == 0) sql += "AD_Client_ID=? ";
    else sql += "M_Product_Category_ID=? ";
    if (!p_IsReValidate) sql += "AND IsVerified<>'Y' ";
    sql += "ORDER BY Name";
    int AD_Client_ID = Env.getADClientID(getCtx());
    try {
      pstmt = DB.prepareStatement(sql, get_TrxName());
      if (p_M_Product_Category_ID == 0) pstmt.setInt(1, AD_Client_ID);
      else pstmt.setInt(1, p_M_Product_Category_ID);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        p_M_Product_ID = rs.getInt(1); // ADAXA - validate the product retrieved from database
        checkProduct(new MProduct(getCtx(), p_M_Product_ID, get_TrxName()));

        counter++;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      DB.close(rs, pstmt);
      rs = null;
      pstmt = null;
    }
    return "#" + counter;
  } //	doIt

  private void checkProduct(MProduct product) {
    if (product.isBOM() && !checkedproducts.contains(product)) {
      validateProduct(product);
    }
  }

  /**
   * Validate Product
   *
   * @param product product
   * @return Info
   */
  private boolean validateProduct(MProduct product) {
    if (!product.isBOM()) return false;

    //	Check Old Product BOM Structure
    if (log.isLoggable(Level.CONFIG)) log.config(product.getName());

    foundproducts.add(product);
    MProductBOM[] productsBOMs = MProductBOM.getBOMLines(product);
    boolean containsinvalid = false;
    boolean invalid = false;
    int lines = 0;
    for (MProductBOM productsBOM : productsBOMs) {
      if (!productsBOM.isActive()) continue;
      lines++;
      MProduct pp = new MProduct(getCtx(), productsBOM.getM_ProductBOM_ID(), get_TrxName());
      if (!pp.isBOM()) {
        if (log.isLoggable(Level.FINER)) log.finer(pp.getName());
      } else {
        if (validproducts.contains(pp)) {
          // Do nothing, no need to recheck
        }
        if (invalidproducts.contains(pp)) {
          containsinvalid = true;
        } else if (foundproducts.contains(pp)) {
          invalid = true;
          if (p_fromButton)
            addLog(0, null, null, product.getValue() + " recursively contains " + pp.getValue());
          else
            addBufferLog(
                0,
                null,
                null,
                product.getValue() + " recursively contains " + pp.getValue(),
                MProduct.Table_ID,
                product.getM_Product_ID());
        } else {
          if (!validateProduct(pp)) {
            containsinvalid = true;
          }
        }
      }
    }

    if (lines == 0) {
      invalid = true;
      if (p_fromButton) addLog(0, null, null, product.getValue() + " does not have lines");
      else
        addBufferLog(
            0,
            null,
            null,
            product.getValue() + " does not have lines",
            MProduct.Table_ID,
            product.getM_Product_ID());
    }

    checkedproducts.add(product);
    foundproducts.remove(product);
    if (invalid) {
      invalidproducts.add(product);
      product.setIsVerified(false);
      product.saveEx();
      return false;
    } else if (containsinvalid) {
      containinvalidproducts.add(product);
      product.setIsVerified(false);
      product.saveEx();
      return false;
    } else {
      validproducts.add(product);
      product.setIsVerified(true);
      product.saveEx();
      return true;
    }
  } //	validateProduct
} //	BOMValidate
