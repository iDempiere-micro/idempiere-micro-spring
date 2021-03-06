package org.idempiere.common.util;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Savepoint;

public class SavepointVO implements Savepoint, Serializable {

  /** */
  private static final long serialVersionUID = -3108395063700087876L;

  int savePointId = -1;
  String savePointName = null;

  public SavepointVO(Savepoint sp) {
    try {
      savePointId = sp.getSavepointId();
    } catch (SQLException e) {
    }

    try {
      savePointName = sp.getSavepointName();
    } catch (SQLException e) {
    }
  }

  public int getSavepointId() throws SQLException {
    return savePointId;
  }

  public String getSavepointName() throws SQLException {
    return savePointName;
  }
}
