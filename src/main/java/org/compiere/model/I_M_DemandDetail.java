package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.idempiere.common.util.KeyNamePair;

/**
 * Generated Interface for M_DemandDetail
 *
 * @author iDempiere (generated)
 * @version Release 5.1
 */
public interface I_M_DemandDetail {

  /** TableName=M_DemandDetail */
  public static final String Table_Name = "M_DemandDetail";

  /** AD_Table_ID=721 */
  public static final int Table_ID = 721;

  KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

  /** AccessLevel = 2 - Client */
  BigDecimal accessLevel = BigDecimal.valueOf(2);

  /** Load Meta Data */

  /** Get Client. Client/Tenant for this installation. */
  public int getADClientID();

  /** Column name AD_Org_ID */
  public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

  /** Set Organization. Organizational entity within client */
  public void setAD_Org_ID(int AD_Org_ID);

  /** Get Organization. Organizational entity within client */
  public int getAD_Org_ID();

  /** Column name C_OrderLine_ID */
  public static final String COLUMNNAME_C_OrderLine_ID = "C_OrderLine_ID";

  /** Set Sales Order Line. Sales Order Line */
  public void setC_OrderLine_ID(int C_OrderLine_ID);

  /** Get Sales Order Line. Sales Order Line */
  public int getC_OrderLine_ID();

  public I_C_OrderLine getC_OrderLine() throws RuntimeException;

  /** Column name Created */
  public static final String COLUMNNAME_Created = "Created";

  /** Get Created. Date this record was created */
  public Timestamp getCreated();

  /** Column name CreatedBy */
  public static final String COLUMNNAME_CreatedBy = "CreatedBy";

  /** Get Created By. User who created this records */
  public int getCreatedBy();

  /** Column name IsActive */
  public static final String COLUMNNAME_IsActive = "IsActive";

  /** Set Active. The record is active in the system */
  public void setIsActive(boolean IsActive);

  /** Get Active. The record is active in the system */
  public boolean isActive();

  /** Column name M_DemandDetail_ID */
  public static final String COLUMNNAME_M_DemandDetail_ID = "M_DemandDetail_ID";

  /** Set Demand Detail. Material Demand Line Source Detail */
  public void setM_DemandDetail_ID(int M_DemandDetail_ID);

  /** Get Demand Detail. Material Demand Line Source Detail */
  public int getM_DemandDetail_ID();

  /** Column name M_DemandDetail_UU */
  public static final String COLUMNNAME_M_DemandDetail_UU = "M_DemandDetail_UU";

  /** Set M_DemandDetail_UU */
  public void setM_DemandDetail_UU(String M_DemandDetail_UU);

  /** Get M_DemandDetail_UU */
  public String getM_DemandDetail_UU();

  /** Column name M_DemandLine_ID */
  public static final String COLUMNNAME_M_DemandLine_ID = "M_DemandLine_ID";

  /** Set Demand Line. Material Demand Line */
  public void setM_DemandLine_ID(int M_DemandLine_ID);

  /** Get Demand Line. Material Demand Line */
  public int getM_DemandLine_ID();

  public I_M_DemandLine getM_DemandLine() throws RuntimeException;

  /** Column name M_ForecastLine_ID */
  public static final String COLUMNNAME_M_ForecastLine_ID = "M_ForecastLine_ID";

  /** Set Forecast Line. Forecast Line */
  public void setM_ForecastLine_ID(int M_ForecastLine_ID);

  /** Get Forecast Line. Forecast Line */
  public int getM_ForecastLine_ID();

  public I_M_ForecastLine getM_ForecastLine() throws RuntimeException;

  /** Column name M_RequisitionLine_ID */
  public static final String COLUMNNAME_M_RequisitionLine_ID = "M_RequisitionLine_ID";

  /** Set Requisition Line. Material Requisition Line */
  public void setM_RequisitionLine_ID(int M_RequisitionLine_ID);

  /** Get Requisition Line. Material Requisition Line */
  public int getM_RequisitionLine_ID();

  public I_M_RequisitionLine getM_RequisitionLine() throws RuntimeException;

  /** Column name Updated */
  public static final String COLUMNNAME_Updated = "Updated";

  /** Get Updated. Date this record was updated */
  public Timestamp getUpdated();

  /** Column name UpdatedBy */
  public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

  /** Get Updated By. User who updated this records */
  public int getUpdatedBy();
}
