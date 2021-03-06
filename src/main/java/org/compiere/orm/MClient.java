package org.compiere.orm;

import java.io.File;
import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;
import org.compiere.model.I_AD_Client;
import org.compiere.model.I_AD_User;
import org.idempiere.common.util.CCache;
import org.idempiere.common.util.Env;
import org.idempiere.common.util.Language;

public class MClient extends X_AD_Client {
  /** Cache */
  protected static CCache<Integer, MClient> s_cache =
      new CCache<Integer, MClient>(Table_Name, 3, 120, true);

  /**
   * ************************************************************************ Standard Constructor
   *
   * @param ctx context
   * @param AD_Client_ID id
   * @param createNew create new
   * @param trxName transaction
   */
  public MClient(Properties ctx, int AD_Client_ID, boolean createNew, String trxName) {
    super(ctx, AD_Client_ID, trxName);
    m_createNew = createNew;
    if (AD_Client_ID == 0) {
      if (m_createNew) {
        //	setValue (null);
        //	setName (null);
        setAD_Org_ID(0);
        setIsMultiLingualDocument(false);
        setIsSmtpAuthorization(false);
        setIsUseBetaFunctions(true);
        setIsServerEMail(false);
        setADLanguage(Language.getBaseAD_Language());
        setAutoArchive(AUTOARCHIVE_None);
        setMMPolicy(MMPOLICY_FiFo); // F
        setIsPostImmediate(false);
      } else load(get_TrxName());
    }
  } //	MClient

  /**
   * Standard Constructor
   *
   * @param ctx context
   * @param AD_Client_ID id
   * @param trxName transaction
   */
  public MClient(Properties ctx, int AD_Client_ID, String trxName) {
    this(ctx, AD_Client_ID, false, trxName);
  } //	MClient

  /**
   * Load Constructor
   *
   * @param ctx context
   * @param rs result set
   * @param trxName transaction
   */
  public MClient(Properties ctx, ResultSet rs, String trxName) {
    super(ctx, rs, trxName);
  } //	MClient

  /**
   * Simplified Constructor
   *
   * @param ctx context
   * @param trxName transaction
   */
  public MClient(Properties ctx, String trxName) {
    this(ctx, Env.getADClientID(ctx), trxName);
  } //	MClient

  /**
   * Get optionally cached client
   *
   * @param ctx context
   * @return client
   */
  public static MClient get(Properties ctx) {
    return get(ctx, Env.getADClientID(ctx));
  } //	get

  /**
   * Get client
   *
   * @param ctx context
   * @param AD_Client_ID id
   * @return client
   */
  public static MClient get(Properties ctx, int AD_Client_ID) {
    Integer key = new Integer(AD_Client_ID);
    MClient client = (MClient) s_cache.get(key);
    if (client != null) return client;
    client = new MClient(ctx, AD_Client_ID, null);
    s_cache.put(key, client);
    return client;
  } //	get

  /**
   * Get all clients
   *
   * @param ctx context
   * @return clients
   */
  public static MClient[] getAll(Properties ctx) {
    return getAll(ctx, "");
  } //	getAll

  /**
   * Get all clients
   *
   * @param ctx context
   * @param order by clause
   * @return clients
   */
  public static MClient[] getAll(Properties ctx, String orderBy) {
    List<MClient> list =
        new Query(ctx, I_AD_Client.Table_Name, null, null).setOrderBy(orderBy).list();
    for (MClient client : list) {
      s_cache.put(new Integer(client.getADClientID()), client);
    }
    MClient[] retValue = new MClient[list.size()];
    list.toArray(retValue);
    return retValue;
  } //	getAll

  /**
   * Get Client Info
   *
   * @return Client Info
   */
  public MClientInfo getInfo() {
    if (m_info == null) m_info = MClientInfo.get(getCtx(), getADClientID(), get_TrxName());
    return m_info;
  } //	getMClientInfo

  /** Client Info */
  protected MClientInfo m_info = null;

  public boolean sendEMail(String to, String subject, String message, File attachment) {
    return true;
  }

  public boolean sendEMail(int AD_User_ID, String subject, String message, File attachment) {
    return true;
  }

  public boolean sendEMail(
      String to, String subject, String message, File attachment, boolean html) {
    return true;
  }

  public boolean sendEMail(
      I_AD_User from,
      I_AD_User to,
      String subject,
      String message,
      File attachment,
      boolean isHtml) {
    return true;
  }

  public boolean sendEMail(
      I_AD_User from, I_AD_User to, String subject, String message, File attachment) {
    return true;
  }

  public void sendEMailAttachments(
      I_AD_User from,
      I_AD_User user,
      String schedulerName,
      String mailContent,
      List<File> fileList) {}

  /*
   * Is Client Accounting enabled?
   * CLIENT_ACCOUNTING parameter allow the next values
   *   D - Disabled (default)
   *   Q - Queue (enabled to post by hand - queue documents for posterior processing)
   *   I - Immediate (immediate post - allow complete on errors)
   *
   *	@return boolean representing if client accounting is enabled and it's on a client
   */
  // private static final String CLIENT_ACCOUNTING_DISABLED = "D";
  protected static final String CLIENT_ACCOUNTING_QUEUE = "Q";
  protected static final String CLIENT_ACCOUNTING_IMMEDIATE = "I";

  public boolean isClientAccountingImmediate() {
    String ca =
        MSysConfig.getValue(
            MSysConfig.CLIENT_ACCOUNTING,
            CLIENT_ACCOUNTING_QUEUE, // default
            Env.getADClientID(Env.getCtx()));
    return ca.equalsIgnoreCase(CLIENT_ACCOUNTING_IMMEDIATE);
  }
}
