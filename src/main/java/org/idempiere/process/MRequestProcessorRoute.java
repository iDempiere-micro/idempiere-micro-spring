package org.idempiere.process;

import java.sql.ResultSet;
import java.util.Properties;

public class MRequestProcessorRoute extends X_R_RequestProcessor_Route {
  /** */
  private static final long serialVersionUID = 8739358607059413339L;

  /**
   * Standard Constructor
   *
   * @param ctx context
   * @param R_RequestProcessor_Route_ID id
   */
  public MRequestProcessorRoute(Properties ctx, int R_RequestProcessor_Route_ID, String trxName) {
    super(ctx, R_RequestProcessor_Route_ID, trxName);
  } //	MRequestProcessorRoute

  /**
   * Load Constructor
   *
   * @param ctx context
   * @param rs result set
   */
  public MRequestProcessorRoute(Properties ctx, ResultSet rs, String trxName) {
    super(ctx, rs, trxName);
  } //	MRequestProcessorRoute
} //	MRequestProcessorRoute
