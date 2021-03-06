package org.compiere.order;

import java.util.List;
import java.util.logging.Level;
import org.idempiere.common.base.Service;
import org.idempiere.common.util.CLogger;
import org.idempiere.common.util.DB;

/**
 * Facade for MShipper, providing accessor method for custom field
 *
 * @author Low Heng Sin
 */
public class MShipperFacade {
  private MShipper m_shipper;
  private MShippingProcessor m_processor;
  private static final CLogger s_log = CLogger.getCLogger(MShipperFacade.class);

  public MShipperFacade(MShipper shipper) {
    m_shipper = shipper;
    m_processor = getShippingProcessor();
  }

  public MShipper getMShipper() {
    return m_shipper;
  }

  private MShippingProcessor getShippingProcessor() {
    if (m_shipper.getM_ShippingProcessor_ID() > 0)
      return new MShippingProcessor(
          m_shipper.getCtx(), m_shipper.getM_ShippingProcessor_ID(), m_shipper.get_TrxName());
    return null;
  }

  public String getShippingProcessorClass() {
    return m_processor.getShippingProcessorClass();
  }

  public String getConnectionKey() {
    return m_processor.getConnectionKey();
  }

  public String getConnectionPassword() {
    return m_processor.getConnectionPassword();
  }

  public String getUserID() {
    return m_processor.getUserID();
  }

  public String getHostAddress() {
    return m_processor.getHostAddress();
  }

  public String getProxyAddress() {
    return m_processor.getProxyAddress();
  }

  public int getHostPort() {
    return m_processor.getHostPort();
  }

  public String getProxyLogon() {
    return m_processor.getProxyLogon();
  }

  public String getProxyPassword() {
    return m_processor.getProxyPassword();
  }

  public int getProxyPort() {
    return m_processor.getProxyPort();
  }

  public String getServicePath() {
    return m_processor.getServicePath();
  }

  public String getShippingServiceCode() {
    return m_shipper.getShippingServiceCode();
  }

  public String getShipperAccount(int AD_Org_ID) {
    StringBuilder sql = new StringBuilder();
    sql.append("Select ShipperAccount From C_BP_ShippingAcct ")
        .append("Where C_BPartner_ID = ? ")
        .append(" AND AD_Org_ID In (0, ")
        .append(AD_Org_ID)
        .append(") ")
        .append(" Order By AD_Org_ID Desc ");
    String ac = DB.getSQLValueString(null, sql.toString(), m_shipper.getC_BPartner_ID());
    if (ac != null) {
      ac = ac.replaceAll("[-]", "");
      ac = ac.replaceAll(" ", "");
    }
    return ac;
  }

  public String getDutiesShipperAccount(int AD_Org_ID) {
    StringBuilder sql = new StringBuilder();
    sql.append("Select DutiesShipperAccount From C_BP_ShippingAcct ")
        .append("Where C_BPartner_ID = ? ")
        .append(" AND AD_Org_ID In (0, ")
        .append(AD_Org_ID)
        .append(") ")
        .append(" Order By AD_Org_ID Desc ");
    String ac = DB.getSQLValueString(null, sql.toString(), m_shipper.getC_BPartner_ID());
    if (ac != null) {
      ac = ac.replaceAll("[-]", "");
      ac = ac.replaceAll(" ", "");
    }
    return ac;
  }

  /**
   * get Meter Number associated with Account Number, use by the Fedex interface
   *
   * @param shipment
   * @return Shipper Meter Number
   */
  public String getShipperMeter(int AD_Org_ID) {
    StringBuilder sql = new StringBuilder();
    sql.append("Select ShipperMeter From C_BP_ShippingAcct ")
        .append("Where C_BPartner_ID = ? ")
        .append(" AND AD_Org_ID In (0, ")
        .append(AD_Org_ID)
        .append(") ")
        .append(" Order By AD_Org_ID Desc ");
    return DB.getSQLValueString(null, sql.toString(), m_shipper.getC_BPartner_ID());
  }

  public boolean isResidential() {
    return m_shipper.isResidential();
  }

  public boolean isSaturdayDelivery() {
    return m_shipper.isSaturdayDelivery();
  }

  public boolean isInternational() {
    return m_shipper.isInternational();
  }

  /**
   * @param sf
   * @return shipment process instance or null if not found
   */
  public static IShipmentProcessor getShipmentProcessor(MShipperFacade sf) {
    if (s_log.isLoggable(Level.FINE)) s_log.fine("create for " + sf);

    String className = sf.getShippingProcessorClass();
    if (className == null || className.length() == 0) {
      s_log.log(Level.SEVERE, "Shipment processor class not define for shipper " + sf);
      return null;
    }

    List<IShipmentProcessorFactory> factoryList =
        Service.Companion.locator().list(IShipmentProcessorFactory.class).getServices();
    if (factoryList == null) return null;
    for (IShipmentProcessorFactory factory : factoryList) {
      IShipmentProcessor processor = factory.newShipmentProcessorInstance(className);
      if (processor != null) return processor;
    }

    return null;
  }
}
