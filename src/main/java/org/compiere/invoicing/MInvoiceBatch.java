package org.compiere.invoicing;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import org.idempiere.common.util.DB;
import org.idempiere.common.util.Env;
import org.idempiere.orm.PO;

/**
 * Invoice Batch Header Model
 *
 * @author Jorg Janke
 * @version $Id: MInvoiceBatch.java,v 1.2 2006/07/30 00:51:03 jjanke Exp $
 */
public class MInvoiceBatch extends X_C_InvoiceBatch {

  /** */
  private static final long serialVersionUID = 3449653049236263604L;

  /**
   * Standard Constructor
   *
   * @param ctx context
   * @param C_InvoiceBatch_ID id
   * @param trxName trx
   */
  public MInvoiceBatch(Properties ctx, int C_InvoiceBatch_ID, String trxName) {
    super(ctx, C_InvoiceBatch_ID, trxName);
    if (C_InvoiceBatch_ID == 0) {
      //	setDocumentNo (null);
      //	setC_Currency_ID (0);	// @$C_Currency_ID@
      setControlAmt(Env.ZERO); // 0
      setDateDoc(new Timestamp(System.currentTimeMillis())); // @#Date@
      setDocumentAmt(Env.ZERO);
      setIsSOTrx(false); // N
      setProcessed(false);
      //	setSalesRep_ID (0);
    }
  } //	MInvoiceBatch

  /**
   * Load Constructor
   *
   * @param ctx context
   * @param rs result set
   * @param trxName trx
   */
  public MInvoiceBatch(Properties ctx, ResultSet rs, String trxName) {
    super(ctx, rs, trxName);
  } //	MInvoiceBatch

  /** The Lines */
  private MInvoiceBatchLine[] m_lines = null;

  /**
   * Get Lines
   *
   * @param reload reload data
   * @return array of lines
   */
  public MInvoiceBatchLine[] getLines(boolean reload) {
    if (m_lines != null && !reload) {
      PO.set_TrxName(m_lines, get_TrxName());
      return m_lines;
    }
    String sql = "SELECT * FROM C_InvoiceBatchLine WHERE C_InvoiceBatch_ID=? ORDER BY Line";
    ArrayList<MInvoiceBatchLine> list = new ArrayList<MInvoiceBatchLine>();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      pstmt = DB.prepareStatement(sql, get_TrxName());
      pstmt.setInt(1, getC_InvoiceBatch_ID());
      rs = pstmt.executeQuery();
      while (rs.next()) {
        list.add(new MInvoiceBatchLine(getCtx(), rs, get_TrxName()));
      }
    } catch (Exception e) {
      log.log(Level.SEVERE, sql, e);
    } finally {
      DB.close(rs, pstmt);
      rs = null;
      pstmt = null;
    }
    //
    m_lines = new MInvoiceBatchLine[list.size()];
    list.toArray(m_lines);
    return m_lines;
  } //	getLines

  /**
   * Set Processed
   *
   * @param processed processed
   */
  public void setProcessed(boolean processed) {
    super.setProcessed(processed);
    if (getId() == 0) return;
    StringBuilder set =
        new StringBuilder("SET Processed='")
            .append((processed ? "Y" : "N"))
            .append("' WHERE C_InvoiceBatch_ID=")
            .append(getC_InvoiceBatch_ID());
    int noLine = DB.executeUpdate("UPDATE C_InvoiceBatchLine " + set, get_TrxName());
    m_lines = null;
    if (log.isLoggable(Level.FINE)) log.fine(processed + " - Lines=" + noLine);
  } //	setProcessed
} //	MInvoiceBatch
