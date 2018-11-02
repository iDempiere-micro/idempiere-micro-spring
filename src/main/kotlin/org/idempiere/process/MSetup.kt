package org.idempiere.process

import java.io.File
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.util.Properties
import java.util.UUID
import java.util.logging.Level
import org.compiere.accounting.MAccount
import org.compiere.accounting.MAcctSchema
import org.compiere.accounting.MCalendar
import org.compiere.accounting.MCashBook
import org.compiere.accounting.MClient
import org.compiere.accounting.MElement
import org.compiere.accounting.MElementValue
import org.compiere.accounting.MProduct
import org.compiere.accounting.MWarehouse
import org.compiere.accounting.X_C_AcctSchema_Default
import org.compiere.accounting.X_C_AcctSchema_GL
import org.compiere.crm.MBPGroup
import org.compiere.crm.MBPartner
import org.compiere.crm.MBPartnerLocation
import org.compiere.crm.MLocation
import org.compiere.crm.MUser
import org.compiere.model.I_C_AcctSchema
import org.compiere.orm.MDocType
import org.compiere.orm.MOrg
import org.compiere.orm.MOrgInfo
import org.compiere.orm.MRole
import org.compiere.orm.MRoleOrgAccess
import org.compiere.orm.MSequence
import org.compiere.orm.MTable
import org.compiere.orm.PO
import org.compiere.process.ProcessInfo
import org.compiere.process.ProcessInfoParameter
import org.compiere.process.ProcessUtil
import org.compiere.product.MDiscountSchema
import org.compiere.product.MPriceList
import org.compiere.product.MPriceListVersion
import org.compiere.product.MProductCategory
import org.compiere.product.MProductPrice
import org.compiere.production.MLocator
import org.compiere.tax.MTax
import org.compiere.util.DisplayType
import org.compiere.util.Msg
import org.compiere.util.SystemIDs
import org.idempiere.common.exceptions.AdempiereException
import org.idempiere.common.util.AdempiereUserError
import org.idempiere.common.util.CLogger
import org.idempiere.common.util.DB
import org.idempiere.common.util.Env
import org.idempiere.common.util.KeyNamePair
import org.idempiere.common.util.Trx

class MSetup
/**
 * Constructor
 * @param ctx context
 * @param WindowNo window
 */
(ctx: Properties, private val m_WindowNo: Int) {

    /**	Logger			 */
    protected var log = CLogger.getCLogger(javaClass)

    private val m_trx = Trx.get(Trx.createTrxName("Setup"), true)
    private val m_ctx: Properties
    private val m_lang: String
    private var m_info: StringBuffer? = null
    //
    private var m_clientName: String? = null
    //	private String          m_orgName;
    //
    private val m_stdColumns = "AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy"
    private var m_stdValues: String? = null
    private var m_stdValuesOrg: String? = null
    //
    private var m_nap: NaturalAccountMap<String, MElementValue>? = null
    //
    private var m_client: MClient? = null
    private var m_org: MOrg? = null
    private var m_as: MAcctSchema? = null
    //
    /**
     * Get AD_User_ID
     * @return AD_User_ID
     */
    var aD_User_ID: Int = 0
        private set
    private var AD_User_Name: String? = null
    private var AD_User_U_ID: Int = 0
    private var AD_User_U_Name: String? = null
    private var m_calendar: MCalendar? = null
    private var m_AD_Tree_Account_ID: Int = 0
    private var C_Cycle_ID: Int = 0
    //
    private var m_hasProject = false
    private var m_hasMCampaign = false
    private var m_hasSRegion = false
    private var m_hasActivity = false

    /**
     * Get Client
     * @return AD_Client_ID
     */
    val aD_Client_ID: Int
        get() = m_client!!.aD_Client_ID
    /**
     * Get AD_Org_ID
     * @return AD_Org_ID
     */
    val aD_Org_ID: Int
        get() = m_org!!.aD_Org_ID
    /**
     * Get Info
     * @return Info
     */
    val info: String
        get() = m_info!!.toString()

    init {
        m_ctx = Properties(ctx)    //	copy
        m_lang = Env.getAD_Language(m_ctx)
    }   //  MSetup

    /**
     * Create Client Info.
     * - Client, Trees, Org, Role, User, User_Role
     * @param clientName client name
     * @param orgName org name
     * @param userClient user id client
     * @param userOrg user id org
     * @param isSetInitialPassword
     * @return true if created
     */
    fun createClient(clientName: String, orgValue: String?, orgName: String,
                     userClient: String, userOrg: String, phone: String, phone2: String, fax: String, eMail: String, taxID: String?,
                     adminEmail: String, userEmail: String, isSetInitialPassword: Boolean): Boolean {
        var orgValue1 = orgValue
        log.info(clientName)
        m_trx!!.displayName = javaClass.name + "_createClient"
        m_trx.start()

        //  info header
        m_info = StringBuffer()
        //  Standard columns
        var name: String?
        var sql: String?
        var no: Int

        /**
         * Create Client
         */
        name = clientName
        if (name.isEmpty())
            name = "newClient"
        m_clientName = name
        m_client = MClient(m_ctx, 0, true, m_trx.trxName)
        m_client!!.value = m_clientName!!
        m_client!!.name = m_clientName!!
        if (!m_client!!.save()) {
            val err = "Client NOT created"
            log.log(Level.SEVERE, err)
            m_info!!.append(err)
            m_trx.rollback()
            m_trx.close()
            return false
        }
        val AD_Client_ID = m_client!!.aD_Client_ID
        Env.setContext(m_ctx, m_WindowNo, "AD_Client_ID", AD_Client_ID)
        Env.setContext(m_ctx, "#AD_Client_ID", AD_Client_ID)

        //	Standard Values
        m_stdValues = AD_Client_ID.toString() + ",0,'Y',SysDate,0,SysDate,0"
        //  Info - Client
        m_info!!.append(Msg.translate(m_lang, "AD_Client_ID")).append("=").append(name).append("\n")

        //	Setup Sequences
        if (!MSequence.checkClientSequences(m_ctx, AD_Client_ID, m_trx.trxName)) {
            val err = "Sequences NOT created"
            log.log(Level.SEVERE, err)
            m_info!!.append(err)
            m_trx.rollback()
            m_trx.close()
            return false
        }

        //  Trees and Client Info
        if (!m_client!!.setupClientInfo(m_lang)) {
            val err = "Client Info NOT created"
            log.log(Level.SEVERE, err)
            m_info!!.append(err)
            m_trx.rollback()
            m_trx.close()
            return false
        }
        m_AD_Tree_Account_ID = m_client!!.setup_AD_Tree_Account_ID

        /**
         * Create Org
         */
        name = orgName
        if (name.isEmpty())
            name = "newOrg"
        if (orgValue1 == null || orgValue1.length == 0)
            orgValue1 = name
        m_org = MOrg(m_client!!, orgValue1, name)
        if (!m_org!!.save()) {
            val err = "Organization NOT created"
            log.log(Level.SEVERE, err)
            m_info!!.append(err)
            m_trx.rollback()
            m_trx.close()
            return false
        }
        Env.setContext(m_ctx, m_WindowNo, "AD_Org_ID", aD_Org_ID)
        Env.setContext(m_ctx, "#AD_Org_ID", aD_Org_ID)
        m_stdValuesOrg = AD_Client_ID.toString() + "," + aD_Org_ID + ",'Y',SysDate,0,SysDate,0"
        //  Info
        m_info!!.append(Msg.translate(m_lang, "AD_Org_ID")).append("=").append(name).append("\n")

        // Set Organization Phone, Phone2, Fax, EMail
        val orgInfo = MOrgInfo.get(m_ctx, aD_Org_ID, m_trx.trxName)
        orgInfo!!.phone = phone
        orgInfo.phone2 = phone2
        orgInfo.fax = fax
        orgInfo.eMail = eMail
        if (taxID != null && taxID.length > 0) {
            orgInfo.taxID = taxID
        }
        if (!orgInfo.save()) {
            val err = "Organization Info NOT Updated"
            log.log(Level.SEVERE, err)
            m_info!!.append(err)
            m_trx.rollback()
            m_trx.close()
            return false
        }

        /**
         * Create Roles
         * - Admin
         * - User
         */
        name = m_clientName!! + " Admin"
        val admin = MRole(m_ctx, 0, m_trx.trxName)
        admin.setClientOrg(m_client)
        admin.name = name
        admin.userLevel = MRole.USERLEVEL_ClientPlusOrganization
        admin.preferenceType = MRole.PREFERENCETYPE_Client
        admin.setIsShowAcct(true)
        admin.setIsAccessAdvanced(true)
        if (!admin.save()) {
            val err = "Admin Role A NOT inserted"
            log.log(Level.SEVERE, err)
            m_info!!.append(err)
            m_trx.rollback()
            m_trx.close()
            return false
        }
        //	OrgAccess x, 0
        val adminClientAccess = MRoleOrgAccess(admin, 0)
        if (!adminClientAccess.save())
            log.log(Level.SEVERE, "Admin Role_OrgAccess 0 NOT created")
        //  OrgAccess x,y
        val adminOrgAccess = MRoleOrgAccess(admin, m_org!!.aD_Org_ID)
        if (!adminOrgAccess.save())
            log.log(Level.SEVERE, "Admin Role_OrgAccess NOT created")

        //  Info - Admin Role
        m_info!!.append(Msg.translate(m_lang, "AD_Role_ID")).append("=").append(name).append("\n")

        //
        name = m_clientName!! + " User"
        val user = MRole(m_ctx, 0, m_trx.trxName)
        user.setClientOrg(m_client)
        user.name = name
        user.setIsAccessAdvanced(false)
        if (!user.save()) {
            val err = "User Role A NOT inserted"
            log.log(Level.SEVERE, err)
            m_info!!.append(err)
            m_trx.rollback()
            m_trx.close()
            return false
        }
        //  OrgAccess x,y
        val userOrgAccess = MRoleOrgAccess(user, m_org!!.aD_Org_ID)
        if (!userOrgAccess.save())
            log.log(Level.SEVERE, "User Role_OrgAccess NOT created")

        //  Info - Client Role
        m_info!!.append(Msg.translate(m_lang, "AD_Role_ID")).append("=").append(name).append("\n")

        /**
         * Create Users
         * - Client
         * - Org
         */
        val clientAdminUser = MUser(m_ctx, 0, m_trx.trxName)

        name = userClient
        if (name.isEmpty())
            name = m_clientName!! + "Client"

        if (isSetInitialPassword)
            clientAdminUser.password = name
        clientAdminUser.description = name
        clientAdminUser.setName(name)
        clientAdminUser.aD_Client_ID = AD_Client_ID
        clientAdminUser.aD_Org_ID = 0
        clientAdminUser.eMail = adminEmail

        try {
            clientAdminUser.saveEx()
        } catch (ex: AdempiereException) {
            val err = "Admin User NOT inserted - " + AD_User_Name!!
            log.log(Level.SEVERE, err)
            m_info!!.append(err)
            m_trx.rollback()
            m_trx.close()
            return false
        }

        aD_User_ID = clientAdminUser.aD_User_ID
        AD_User_Name = name

        //  Info
        m_info!!.append(Msg.translate(m_lang, "AD_User_ID")).append("=").append(AD_User_Name).append("/").append(AD_User_Name).append("\n")

        val clientUser = MUser(m_ctx, 0, m_trx.trxName)

        name = userOrg
        if (name.isEmpty())
            name = m_clientName!! + "Org"

        if (isSetInitialPassword)
            clientUser.password = name
        clientUser.description = name
        clientUser.setName(name)
        clientUser.aD_Client_ID = AD_Client_ID
        clientUser.aD_Org_ID = 0
        clientUser.eMail = userEmail

        try {
            clientUser.saveEx()
        } catch (ex: AdempiereException) {
            val err = "Org User NOT inserted - " + AD_User_U_Name!!
            log.log(Level.SEVERE, err)
            m_info!!.append(err)
            m_trx.rollback()
            m_trx.close()
            return false
        }

        AD_User_U_ID = clientUser.aD_User_ID
        AD_User_U_Name = name
        //  Info
        m_info!!.append(Msg.translate(m_lang, "AD_User_ID")).append("=").append(AD_User_U_Name).append("/").append(AD_User_U_Name).append("\n")

        /**
         * Create User-Role
         */
        //  ClientUser          - Admin & User
        sql = ("INSERT INTO AD_User_Roles(" + m_stdColumns + ",AD_User_ID,AD_Role_ID,AD_User_Roles_UU)"
                + " VALUES (" + m_stdValues + "," + aD_User_ID + "," + admin.aD_Role_ID + "," + DB.TO_STRING(UUID.randomUUID().toString()) + ")")
        no = DB.executeUpdateEx(sql, m_trx.trxName)
        if (no != 1)
            log.log(Level.SEVERE, "UserRole ClientUser+Admin NOT inserted")
        sql = ("INSERT INTO AD_User_Roles(" + m_stdColumns + ",AD_User_ID,AD_Role_ID,AD_User_Roles_UU)"
                + " VALUES (" + m_stdValues + "," + aD_User_ID + "," + user.aD_Role_ID + "," + DB.TO_STRING(UUID.randomUUID().toString()) + ")")
        no = DB.executeUpdateEx(sql, m_trx.trxName)
        if (no != 1)
            log.log(Level.SEVERE, "UserRole ClientUser+User NOT inserted")
        //  OrgUser             - User
        sql = ("INSERT INTO AD_User_Roles(" + m_stdColumns + ",AD_User_ID,AD_Role_ID,AD_User_Roles_UU)"
                + " VALUES (" + m_stdValues + "," + AD_User_U_ID + "," + user.aD_Role_ID + "," + DB.TO_STRING(UUID.randomUUID().toString()) + ")")
        no = DB.executeUpdateEx(sql, m_trx.trxName)
        if (no != 1)
            log.log(Level.SEVERE, "UserRole OrgUser+Org NOT inserted")

        //	Processors
        val ap = MAcctProcessor(m_client!!, aD_User_ID)
        ap.aD_Schedule_ID = SystemIDs.SCHEDULE_10_MINUTES
        ap.saveEx()

        val rp = MRequestProcessor(m_client!!, aD_User_ID)
        rp.aD_Schedule_ID = SystemIDs.SCHEDULE_15_MINUTES
        rp.saveEx()

        log.info("fini")
        return true
    }   //  createClient

    /**************************************************************************
     * Create Accounting elements.
     * - Calendar
     * - Account Trees
     * - Account Values
     * - Accounting Schema
     * - Default Accounts
     *
     * @param currency currency
     * @param hasProduct has product segment
     * @param hasBPartner has bp segment
     * @param hasProject has project segment
     * @param hasMCampaign has campaign segment
     * @param hasSRegion has sales region segment
     * @param hasActivity has activity segment
     * @param AccountingFile file name of accounting file
     * @param inactivateDefaults inactivate the default accounts after created
     * @param useDefaultCoA use the Default CoA (load and group summary account)
     * @return true if created
     */
    fun createAccounting(currency: KeyNamePair,
                         hasProduct: Boolean, hasBPartner: Boolean, hasProject: Boolean,
                         hasMCampaign: Boolean, hasSRegion: Boolean,
                         hasActivity: Boolean, AccountingFile: File, inactivateDefaults: Boolean): Boolean {
        if (log.isLoggable(Level.INFO)) log.info(m_client!!.toString())
        //
        m_hasProject = hasProject
        m_hasMCampaign = hasMCampaign
        m_hasSRegion = hasSRegion
        m_hasActivity = hasActivity

        //  Standard variables
        m_info = StringBuffer()
        var name: String?
        var sqlCmd: StringBuffer?
        var no: Int

        /**
         * Create Calendar
         */
        m_calendar = MCalendar(m_client!!)
        if (!m_calendar!!.save()) {
            val err = "Calendar NOT inserted"
            log.log(Level.SEVERE, err)
            m_info!!.append(err)
            m_trx!!.rollback()
            m_trx.close()
            return false
        }
        //  Info
        m_info!!.append(Msg.translate(m_lang, "C_Calendar_ID")).append("=").append(m_calendar!!.name).append("\n")

        if (m_calendar!!.createYear(m_client!!.locale) == null)
            log.log(Level.SEVERE, "Year NOT inserted")

        //	Create Account Elements
        name = m_clientName + " " + Msg.translate(m_lang, "Account_ID")
        val element = MElement(m_client!!, name,
                MElement.ELEMENTTYPE_Account, m_AD_Tree_Account_ID)
        if (!element.save()) {
            val err = "Acct Element NOT inserted"
            log.log(Level.SEVERE, err)
            m_info!!.append(err)
            m_trx!!.rollback()
            m_trx.close()
            return false
        }
        val C_Element_ID = element.c_Element_ID
        m_info!!.append(Msg.translate(m_lang, "C_Element_ID")).append("=").append(name).append("\n")

        //	Create Account Values
        m_nap = NaturalAccountMap(m_ctx, m_trx!!.trxName)
        val errMsg = m_nap!!.parseFile(AccountingFile)
        if (errMsg.length != 0) {
            log.log(Level.SEVERE, errMsg)
            m_info!!.append(errMsg)
            m_trx.rollback()
            m_trx.close()
            return false
        }
        if (m_nap!!.saveAccounts(aD_Client_ID, aD_Org_ID, C_Element_ID, !inactivateDefaults))
            m_info!!.append(Msg.translate(m_lang, "C_ElementValue_ID")).append(" # ").append(m_nap!!.size).append("\n")
        else {
            val err = "Acct Element Values NOT inserted"
            log.log(Level.SEVERE, err)
            m_info!!.append(err)
            m_trx.rollback()
            m_trx.close()
            return false
        }

        val summary_ID = m_nap!!.getC_ElementValue_ID("SUMMARY")
        if (log.isLoggable(Level.FINE)) log.fine("summary_ID=$summary_ID")
        if (summary_ID > 0) {
            DB.executeUpdateEx("UPDATE AD_TreeNode SET Parent_ID=? WHERE AD_Tree_ID=? AND Node_ID!=?",
                    arrayOf<Any>(summary_ID, m_AD_Tree_Account_ID, summary_ID),
                    m_trx.trxName)
        }

        val C_ElementValue_ID = m_nap!!.getC_ElementValue_ID("DEFAULT_ACCT")
        if (log.isLoggable(Level.FINE)) log.fine("C_ElementValue_ID=$C_ElementValue_ID")

        /**
         * Create AccountingSchema
         */
        m_as = MAcctSchema(m_client!!, currency)
        if (!m_as!!.save()) {
            val err = "AcctSchema NOT inserted"
            log.log(Level.SEVERE, err)
            m_info!!.append(err)
            m_trx.rollback()
            m_trx.close()
            return false
        }
        //  Info
        m_info!!.append(Msg.translate(m_lang, "C_AcctSchema_ID")).append("=").append(m_as!!.name).append("\n")

        /**
         * Create AccountingSchema Elements (Structure)
         */
        var sql2: String?
        if (Env.isBaseLanguage(m_lang, "AD_Reference"))
        //	Get ElementTypes & Name
            sql2 = "SELECT Value, Name FROM AD_Ref_List WHERE AD_Reference_ID=181"
        else
            sql2 = ("SELECT l.Value, t.Name FROM AD_Ref_List l, AD_Ref_List_Trl t "
                    + "WHERE l.AD_Reference_ID=181 AND l.AD_Ref_List_ID=t.AD_Ref_List_ID"
                    + " AND t.AD_Language=" + DB.TO_STRING(m_lang)) //bug [ 1638421 ]
        var stmt: PreparedStatement? = null
        var rs: ResultSet? = null
        try {
            val AD_Client_ID = m_client!!.aD_Client_ID
            stmt = DB.prepareStatement(sql2, m_trx.trxName)
            rs = stmt!!.executeQuery()
            while (rs!!.next()) {
                val ElementType = rs.getString(1)
                name = rs.getString(2)
                //
                var IsMandatory: String? = null
                var IsBalanced = "N"
                var SeqNo = 0
                var C_AcctSchema_Element_ID = 0

                if (ElementType == "OO") {
                    C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element")
                    IsMandatory = "Y"
                    IsBalanced = "Y"
                    SeqNo = 10
                } else if (ElementType == "AC") {
                    C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element")
                    IsMandatory = "Y"
                    SeqNo = 20
                } else if (ElementType == "PR" && hasProduct) {
                    C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element")
                    IsMandatory = "N"
                    SeqNo = 30
                } else if (ElementType == "BP" && hasBPartner) {
                    C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element")
                    IsMandatory = "N"
                    SeqNo = 40
                } else if (ElementType == "PJ" && hasProject) {
                    C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element")
                    IsMandatory = "N"
                    SeqNo = 50
                } else if (ElementType == "MC" && hasMCampaign) {
                    C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element")
                    IsMandatory = "N"
                    SeqNo = 60
                } else if (ElementType == "SR" && hasSRegion) {
                    C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element")
                    IsMandatory = "N"
                    SeqNo = 70
                } else if (ElementType == "AY" && hasActivity) {
                    C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element")
                    IsMandatory = "N"
                    SeqNo = 80
                }
                //	Not OT, LF, LT, U1, U2

                if (IsMandatory != null) {
                    sqlCmd = StringBuffer("INSERT INTO C_AcctSchema_Element(")
                    sqlCmd.append(m_stdColumns).append(",C_AcctSchema_Element_ID,C_AcctSchema_ID,")
                            .append("ElementType,Name,SeqNo,IsMandatory,IsBalanced,C_AcctSchema_Element_UU) VALUES (")
                    sqlCmd.append(m_stdValues).append(",").append(C_AcctSchema_Element_ID).append(",").append(m_as!!.c_AcctSchema_ID).append(",")
                            .append("'").append(ElementType).append("','").append(name).append("',").append(SeqNo).append(",'")
                            .append(IsMandatory).append("','").append(IsBalanced).append("',").append(DB.TO_STRING(UUID.randomUUID().toString())).append(")")
                    no = DB.executeUpdateEx(sqlCmd.toString(), m_trx.trxName)
                    if (no == 1)
                        m_info!!.append(Msg.translate(m_lang, "C_AcctSchema_Element_ID")).append("=").append(name).append("\n")

                    /** Default value for mandatory elements: OO and AC  */
                    if (ElementType == "OO") {
                        sqlCmd = StringBuffer("UPDATE C_AcctSchema_Element SET Org_ID=")
                        sqlCmd.append(aD_Org_ID).append(" WHERE C_AcctSchema_Element_ID=").append(C_AcctSchema_Element_ID)
                        no = DB.executeUpdateEx(sqlCmd.toString(), m_trx.trxName)
                        if (no != 1)
                            log.log(Level.SEVERE, "Default Org in AcctSchemaElement NOT updated")
                    }
                    if (ElementType == "AC") {
                        sqlCmd = StringBuffer("UPDATE C_AcctSchema_Element SET C_ElementValue_ID=")
                        sqlCmd.append(C_ElementValue_ID).append(", C_Element_ID=").append(C_Element_ID)
                        sqlCmd.append(" WHERE C_AcctSchema_Element_ID=").append(C_AcctSchema_Element_ID)
                        no = DB.executeUpdateEx(sqlCmd.toString(), m_trx.trxName)
                        if (no != 1)
                            log.log(Level.SEVERE, "Default Account in AcctSchemaElement NOT updated")
                    }
                }
            }
        } catch (e1: SQLException) {
            log.log(Level.SEVERE, "Elements", e1)
            m_info!!.append(e1.message)
            m_trx.rollback()
            m_trx.close()
            return false
        } finally {
            DB.close(rs, stmt)
        }
        //  Create AcctSchema


        //  Create Defaults Accounts
        try {
            createAccountingRecord(X_C_AcctSchema_GL.Table_Name)
            createAccountingRecord(X_C_AcctSchema_Default.Table_Name)
        } catch (e: Exception) {
            val err = e.localizedMessage
            log.log(Level.SEVERE, err)
            e.printStackTrace()
            m_info!!.append(err)
            m_trx.rollback()
            m_trx.close()
            return false
        }

        //  GL Categories
        createGLCategory("Standard", MGLCategory.CATEGORYTYPE_Manual, true)
        val GL_None = createGLCategory("None", MGLCategory.CATEGORYTYPE_Document, false)
        val GL_GL = createGLCategory("Manual", MGLCategory.CATEGORYTYPE_Manual, false)
        val GL_ARI = createGLCategory("AR Invoice", MGLCategory.CATEGORYTYPE_Document, false)
        val GL_ARR = createGLCategory("AR Receipt", MGLCategory.CATEGORYTYPE_Document, false)
        val GL_MM = createGLCategory("Material Management", MGLCategory.CATEGORYTYPE_Document, false)
        val GL_API = createGLCategory("AP Invoice", MGLCategory.CATEGORYTYPE_Document, false)
        val GL_APP = createGLCategory("AP Payment", MGLCategory.CATEGORYTYPE_Document, false)
        val GL_CASH = createGLCategory("Cash/Payments", MGLCategory.CATEGORYTYPE_Document, false)
        val GL_Manufacturing = createGLCategory("Manufacturing", MGLCategory.CATEGORYTYPE_Document, false)
        val GL_Distribution = createGLCategory("Distribution", MGLCategory.CATEGORYTYPE_Document, false)
        val GL_Payroll = createGLCategory("Payroll", MGLCategory.CATEGORYTYPE_Document, false)

        //	Base DocumentTypes
        val ii = createDocType("GL Journal", Msg.getElement(m_ctx, "GL_Journal_ID"),
                MDocType.DOCBASETYPE_GLJournal, null, 0, 0, 1000, GL_GL, false)
        if (ii == 0) {
            val err = "Document Type not created"
            m_info!!.append(err)
            m_trx.rollback()
            m_trx.close()
            return false
        }
        createDocType("GL Journal Batch", Msg.getElement(m_ctx, "GL_JournalBatch_ID"),
                MDocType.DOCBASETYPE_GLJournal, null, 0, 0, 100, GL_GL, false)
        //	MDocType.DOCBASETYPE_GLDocument
        //
        val DT_I = createDocType("AR Invoice", Msg.getElement(m_ctx, "C_Invoice_ID", true),
                MDocType.DOCBASETYPE_ARInvoice, null, 0, 0, 100000, GL_ARI, false)
        val DT_II = createDocType("AR Invoice Indirect", Msg.getElement(m_ctx, "C_Invoice_ID", true),
                MDocType.DOCBASETYPE_ARInvoice, null, 0, 0, 150000, GL_ARI, false)
        val DT_IC = createDocType("AR Credit Memo", Msg.getMsg(m_ctx, "CreditMemo"),
                MDocType.DOCBASETYPE_ARCreditMemo, null, 0, 0, 170000, GL_ARI, false)
        //	MDocType.DOCBASETYPE_ARProFormaInvoice

        createDocType("AP Invoice", Msg.getElement(m_ctx, "C_Invoice_ID", false),
                MDocType.DOCBASETYPE_APInvoice, null, 0, 0, 0, GL_API, false)
        val DT_IPC = createDocType("AP CreditMemo", Msg.getMsg(m_ctx, "CreditMemo"),
                MDocType.DOCBASETYPE_APCreditMemo, null, 0, 0, 0, GL_API, false)
        createDocType("Match Invoice", Msg.getElement(m_ctx, "M_MatchInv_ID", false),
                MDocType.DOCBASETYPE_MatchInvoice, null, 0, 0, 390000, GL_API, false)

        createDocType("AR Receipt", Msg.getElement(m_ctx, "C_Payment_ID", true),
                MDocType.DOCBASETYPE_ARReceipt, null, 0, 0, 0, GL_ARR, false)
        createDocType("AP Payment", Msg.getElement(m_ctx, "C_Payment_ID", false),
                MDocType.DOCBASETYPE_APPayment, null, 0, 0, 0, GL_APP, false)
        createDocType("Allocation", "Allocation",
                MDocType.DOCBASETYPE_PaymentAllocation, null, 0, 0, 490000, GL_CASH, false)

        val DT_S = createDocType("MM Shipment", "Delivery Note",
                MDocType.DOCBASETYPE_MaterialDelivery, null, 0, 0, 500000, GL_MM, false)
        val DT_SI = createDocType("MM Shipment Indirect", "Delivery Note",
                MDocType.DOCBASETYPE_MaterialDelivery, null, 0, 0, 550000, GL_MM, false)
        val DT_VRM = createDocType("MM Vendor Return", "Vendor Return",
                MDocType.DOCBASETYPE_MaterialDelivery, null, 0, 0, 590000, GL_MM, true)

        createDocType("MM Receipt", "Vendor Delivery",
                MDocType.DOCBASETYPE_MaterialReceipt, null, 0, 0, 0, GL_MM, false)
        val DT_RM = createDocType("MM Customer Return", "Customer Return",
                MDocType.DOCBASETYPE_MaterialReceipt, null, 0, 0, 570000, GL_MM, true)

        createDocType("Purchase Order", Msg.getElement(m_ctx, "C_Order_ID", false),
                MDocType.DOCBASETYPE_PurchaseOrder, null, 0, 0, 800000, GL_None, false)
        createDocType("Match PO", Msg.getElement(m_ctx, "M_MatchPO_ID", false),
                MDocType.DOCBASETYPE_MatchPO, null, 0, 0, 890000, GL_None, false)
        createDocType("Purchase Requisition", Msg.getElement(m_ctx, "M_Requisition_ID", false),
                MDocType.DOCBASETYPE_PurchaseRequisition, null, 0, 0, 900000, GL_None, false)
        createDocType("Vendor Return Material", "Vendor Return Material Authorization",
                MDocType.DOCBASETYPE_PurchaseOrder, MDocType.DOCSUBTYPESO_ReturnMaterial, DT_VRM,
                DT_IPC, 990000, GL_MM, false)

        createDocType("Bank Statement", Msg.getElement(m_ctx, "C_BankStatemet_ID", true),
                MDocType.DOCBASETYPE_BankStatement, null, 0, 0, 700000, GL_CASH, false)
        createDocType("Cash Journal", Msg.getElement(m_ctx, "C_Cash_ID", true),
                MDocType.DOCBASETYPE_CashJournal, null, 0, 0, 750000, GL_CASH, false)

        createDocType("Material Movement", Msg.getElement(m_ctx, "M_Movement_ID", false),
                MDocType.DOCBASETYPE_MaterialMovement, null, 0, 0, 610000, GL_MM, false)
        createDocType("Physical Inventory", Msg.getElement(m_ctx, "M_Inventory_ID", false),
                MDocType.DOCBASETYPE_MaterialPhysicalInventory, MDocType.DOCSUBTYPEINV_PhysicalInventory, 0, 0, 620000, GL_MM, false)
        createDocType("Material Production", Msg.getElement(m_ctx, "M_Production_ID", false),
                MDocType.DOCBASETYPE_MaterialProduction, null, 0, 0, 630000, GL_MM, false)
        createDocType("Project Issue", Msg.getElement(m_ctx, "C_ProjectIssue_ID", false),
                MDocType.DOCBASETYPE_ProjectIssue, null, 0, 0, 640000, GL_MM, false)
        createDocType("Internal Use Inventory", "Internal Use Inventory",
                MDocType.DOCBASETYPE_MaterialPhysicalInventory, MDocType.DOCSUBTYPEINV_InternalUseInventory, 0, 0, 650000, GL_MM, false)
        createDocType("Cost Adjustment", "Cost Adjustment",
                MDocType.DOCBASETYPE_MaterialPhysicalInventory, MDocType.DOCSUBTYPEINV_CostAdjustment, 0, 0, 660000, GL_MM, false)

        //  Order Entry
        createDocType("Binding offer", "Quotation",
                MDocType.DOCBASETYPE_SalesOrder, MDocType.DOCSUBTYPESO_Quotation,
                0, 0, 10000, GL_None, false)
        createDocType("Non binding offer", "Proposal",
                MDocType.DOCBASETYPE_SalesOrder, MDocType.DOCSUBTYPESO_Proposal,
                0, 0, 20000, GL_None, false)
        createDocType("Prepay Order", "Prepay Order",
                MDocType.DOCBASETYPE_SalesOrder, MDocType.DOCSUBTYPESO_PrepayOrder,
                DT_S, DT_I, 30000, GL_None, false)
        createDocType("Customer Return Material", "Customer Return Material Authorization",
                MDocType.DOCBASETYPE_SalesOrder, MDocType.DOCSUBTYPESO_ReturnMaterial,
                DT_RM, DT_IC, 30000, GL_None, false)
        createDocType("Standard Order", "Order Confirmation",
                MDocType.DOCBASETYPE_SalesOrder, MDocType.DOCSUBTYPESO_StandardOrder,
                DT_S, DT_I, 50000, GL_None, false)
        createDocType("Credit Order", "Order Confirmation",
                MDocType.DOCBASETYPE_SalesOrder, MDocType.DOCSUBTYPESO_OnCreditOrder,
                DT_SI, DT_I, 60000, GL_None, false)   //  RE
        createDocType("Warehouse Order", "Order Confirmation",
                MDocType.DOCBASETYPE_SalesOrder, MDocType.DOCSUBTYPESO_WarehouseOrder,
                DT_S, DT_I, 70000, GL_None, false)    //  LS

        //Manufacturing Document
        createDocType("Manufacturing Order", "Manufacturing Order",
                MDocType.DOCBASETYPE_ManufacturingOrder, null,
                0, 0, 80000, GL_Manufacturing, false)
        createDocType("Manufacturing Cost Collector", "Cost Collector",
                MDocType.DOCBASETYPE_ManufacturingCostCollector, null,
                0, 0, 81000, GL_Manufacturing, false)
        createDocType("Maintenance Order", "Maintenance Order",
                MDocType.DOCBASETYPE_MaintenanceOrder, null,
                0, 0, 86000, GL_Manufacturing, false)
        createDocType("Quality Order", "Quality Order",
                MDocType.DOCBASETYPE_QualityOrder, null,
                0, 0, 87000, GL_Manufacturing, false)
        createDocType("Distribution Order", "Distribution Order",
                MDocType.DOCBASETYPE_DistributionOrder, null,
                0, 0, 88000, GL_Distribution, false)
        //Payroll
        createDocType("Payroll", "Payroll",
                MDocType.DOCBASETYPE_Payroll, null,
                0, 0, 90000, GL_Payroll, false)

        val DT = createDocType("POS Order", "Order Confirmation",
                MDocType.DOCBASETYPE_SalesOrder, MDocType.DOCSUBTYPESO_POSOrder,
                DT_SI, DT_II, 80000, GL_None, false)    // Bar
        //	POS As Default for window SO
        createPreference("C_DocTypeTarget_ID", DT.toString(), 143)

        //  Update ClientInfo
        sqlCmd = StringBuffer("UPDATE AD_ClientInfo SET ")
        sqlCmd.append("C_AcctSchema1_ID=").append(m_as!!.c_AcctSchema_ID)
                .append(", C_Calendar_ID=").append(m_calendar!!.c_Calendar_ID)
                .append(" WHERE AD_Client_ID=").append(m_client!!.aD_Client_ID)
        no = DB.executeUpdateEx(sqlCmd.toString(), m_trx.trxName)
        if (no != 1) {
            val err = "ClientInfo not updated"
            log.log(Level.SEVERE, err)
            m_info!!.append(err)
            m_trx.rollback()
            m_trx.close()
            return false
        }

        //	Validate Completeness
        val processInfo = ProcessInfo("Document Type Verify", 0)
        processInfo.aD_Client_ID = aD_Client_ID
        processInfo.aD_User_ID = aD_User_ID
        processInfo.parameter = arrayOfNulls<ProcessInfoParameter>(0)
        if (!ProcessUtil.startJavaProcess(m_ctx, processInfo, m_trx, false, null, DocumentTypeVerify())) {
            val err = "Document type verification failed. Message=" + processInfo.summary!!
            log.log(Level.SEVERE, err)
            m_info!!.append(err)
            m_trx.rollback()
            m_trx.close()
            return false
        }
        //
        log.info("fini")
        return true
    }   //  createAccounting

    @Throws(Exception::class)
    private fun createAccountingRecord(tableName: String) {
        val table = MTable.get(m_ctx, tableName)
        val acct = table.getPO(-1, m_trx!!.trxName) as PO // Note this should create a new Acct; ugly hack, because we return null for 0

        val cols = table.getColumns(false)
        for (c in cols) {
            if (!c.isActive)
                continue
            val columnName = c.columnName
            if (c.isStandardColumn) {
            } else if (DisplayType.Account == c.aD_Reference_ID) {
                acct.set_Value(columnName, getAcct(columnName))
                if (log.isLoggable(Level.INFO)) log.info("Account: $columnName")
            } else if (DisplayType.YesNo == c.aD_Reference_ID) {
                acct.set_Value(columnName, java.lang.Boolean.TRUE)
                if (log.isLoggable(Level.INFO)) log.info("YesNo: " + c.columnName)
            }
        }
        acct.aD_Client_ID = m_client!!.aD_Client_ID
        acct.set_Value(I_C_AcctSchema.COLUMNNAME_C_AcctSchema_ID, m_as!!.c_AcctSchema_ID)
        //
        if (!acct.save()) {
            throw AdempiereUserError(CLogger.retrieveErrorString(table.name + " not created"))
        }
    }


    /**
     * Get Account ID for key
     * @param key key
     * @return C_ValidCombination_ID
     * @throws AdempiereUserError
     */
    @Throws(AdempiereUserError::class)
    private fun getAcct(key: String): Int? {
        log.fine(key)
        //  Element
        val C_ElementValue_ID = m_nap!!.getC_ElementValue_ID(key.toUpperCase())
        if (C_ElementValue_ID == 0) {
            throw AdempiereUserError("Account not defined: $key")
        }

        val vc = MAccount.getDefault(m_as, true)    //	optional null
        vc.aD_Org_ID = 0        //	will be overwritten
        vc.account_ID = C_ElementValue_ID
        if (!vc.save()) {
            throw AdempiereUserError("Not Saved - Key=$key, C_ElementValue_ID=$C_ElementValue_ID")
        }
        val C_ValidCombination_ID = vc.c_ValidCombination_ID
        if (C_ValidCombination_ID == 0) {
            throw AdempiereUserError("No account - Key=$key, C_ElementValue_ID=$C_ElementValue_ID")
        }
        return C_ValidCombination_ID
    }   //  getAcct

    /**
     * Create GL Category
     * @param Name name
     * @param CategoryType category type MGLCategory.CATEGORYTYPE_*
     * @param isDefault is default value
     * @return GL_Category_ID
     */
    private fun createGLCategory(Name: String, CategoryType: String, isDefault: Boolean): Int {
        val cat = MGLCategory(m_ctx, 0, m_trx!!.trxName)
        cat.name = Name
        cat.categoryType = CategoryType
        cat.setIsDefault(isDefault)
        if (!cat.save()) {
            log.log(Level.SEVERE, "GL Category NOT created - $Name")
            return 0
        }
        //
        return cat.gL_Category_ID
    }   //  createGLCategory

    /**
     * Create Document Types with Sequence
     * @param Name name
     * @param PrintName print name
     * @param DocBaseType document base type
     * @param DocSubTypeSO sales order sub type
     * @param C_DocTypeShipment_ID shipment doc
     * @param C_DocTypeInvoice_ID invoice doc
     * @param StartNo start doc no
     * @param GL_Category_ID gl category
     * @param isReturnTrx is return trx
     * @return C_DocType_ID doc type or 0 for error
     */
    private fun createDocType(Name: String, PrintName: String?,
                              DocBaseType: String, DocSubTypeSO: String?,
                              C_DocTypeShipment_ID: Int, C_DocTypeInvoice_ID: Int,
                              StartNo: Int, GL_Category_ID: Int, isReturnTrx: Boolean): Int {
        var sequence: MSequence? = null
        if (StartNo != 0) {
            sequence = MSequence(m_ctx, aD_Client_ID, Name, StartNo, m_trx!!.trxName)
            if (!sequence.save()) {
                log.log(Level.SEVERE, "Sequence NOT created - $Name")
                return 0
            }
        }

        val dt = MDocType(m_ctx, DocBaseType, Name, m_trx!!.trxName)
        if (PrintName != null && PrintName.length > 0)
            dt.printName = PrintName    //	Defaults to Name
        if (DocSubTypeSO != null) {
            if (MDocType.DOCBASETYPE_MaterialPhysicalInventory == DocBaseType) {
                dt.docSubTypeInv = DocSubTypeSO
            } else {
                dt.docSubTypeSO = DocSubTypeSO
            }
        }
        if (C_DocTypeShipment_ID != 0)
            dt.c_DocTypeShipment_ID = C_DocTypeShipment_ID
        if (C_DocTypeInvoice_ID != 0)
            dt.c_DocTypeInvoice_ID = C_DocTypeInvoice_ID
        if (GL_Category_ID != 0)
            dt.gL_Category_ID = GL_Category_ID
        if (sequence == null)
            dt.setIsDocNoControlled(false)
        else {
            dt.setIsDocNoControlled(true)
            dt.docNoSequence_ID = sequence.aD_Sequence_ID
        }
        dt.setIsSOTrx()
        if (isReturnTrx)
            dt.setIsSOTrx(!dt.isSOTrx)
        if (!dt.save()) {
            log.log(Level.SEVERE, "DocType NOT created - $Name")
            return 0
        }
        //
        return dt.c_DocType_ID
    }   //  createDocType


    /**************************************************************************
     * Create Default main entities.
     * - Dimensions & BPGroup, Prod Category)
     * - Location, Locator, Warehouse
     * - PriceList
     * - Cashbook, PaymentTerm
     * @param C_Country_ID country
     * @param City city
     * @param C_Region_ID region
     * @param C_Currency_ID currency
     * @return true if created
     */
    fun createEntities(C_Country_ID: Int, City: String, C_Region_ID: Int, C_Currency_ID: Int, postal: String, address1: String): Boolean {
        if (m_as == null) {
            log.severe("No AcctountingSChema")
            m_trx!!.rollback()
            m_trx.close()
            return false
        }
        if (log.isLoggable(Level.INFO))
            log.info("C_Country_ID=" + C_Country_ID
                    + ", City=" + City + ", C_Region_ID=" + C_Region_ID)
        m_info!!.append("\n----\n")
        //
        val defaultName = Msg.translate(m_lang, "Standard")
        val defaultEntry = "'$defaultName',"
        var sqlCmd: StringBuffer?
        var no: Int

        //	Create Marketing Channel/Campaign
        val C_Channel_ID = getNextID(aD_Client_ID, "C_Channel")
        sqlCmd = StringBuffer("INSERT INTO C_Channel ")
        sqlCmd.append("(C_Channel_ID,Name,")
        sqlCmd.append(m_stdColumns).append(",C_Channel_UU) VALUES (")
        sqlCmd.append(C_Channel_ID).append(",").append(defaultEntry)
        sqlCmd.append(m_stdValues).append(",").append(DB.TO_STRING(UUID.randomUUID().toString())).append(")")
        no = DB.executeUpdateEx(sqlCmd.toString(), m_trx!!.trxName)
        if (no != 1)
            log.log(Level.SEVERE, "Channel NOT inserted")

        val C_Campaign_ID = getNextID(aD_Client_ID, "C_Campaign")
        sqlCmd = StringBuffer("INSERT INTO C_Campaign ")
        sqlCmd.append("(C_Campaign_ID,C_Channel_ID,").append(m_stdColumns).append(",")
        sqlCmd.append(" Value,Name,Costs,C_Campaign_UU) VALUES (")
        sqlCmd.append(C_Campaign_ID).append(",").append(C_Channel_ID).append(",").append(m_stdValues).append(",")
        sqlCmd.append(defaultEntry).append(defaultEntry).append("0").append(",").append(DB.TO_STRING(UUID.randomUUID().toString())).append(")")
        no = DB.executeUpdateEx(sqlCmd.toString(), m_trx.trxName)
        if (no == 1)
            m_info!!.append(Msg.translate(m_lang, "C_Campaign_ID")).append("=").append(defaultName).append("\n")
        else
            log.log(Level.SEVERE, "Campaign NOT inserted")
        if (m_hasMCampaign) {
            //  Default
            sqlCmd = StringBuffer("UPDATE C_AcctSchema_Element SET ")
            sqlCmd.append("C_Campaign_ID=").append(C_Campaign_ID)
            sqlCmd.append(" WHERE C_AcctSchema_ID=").append(m_as!!.c_AcctSchema_ID)
            sqlCmd.append(" AND ElementType='MC'")
            no = DB.executeUpdateEx(sqlCmd.toString(), m_trx.trxName)
            if (no != 1)
                log.log(Level.SEVERE, "AcctSchema Element Campaign NOT updated")
        }
        // Campaign Translation
        sqlCmd = StringBuffer("INSERT INTO C_Campaign_Trl (AD_Language,C_Campaign_ID, Description,Name, IsTranslated,AD_Client_ID,AD_Org_ID,Created,Createdby,Updated,UpdatedBy,C_Campaign_Trl_UU)")
        sqlCmd.append(" SELECT l.AD_Language,t.C_Campaign_ID, t.Description,t.Name, 'N',t.AD_Client_ID,t.AD_Org_ID,t.Created,t.Createdby,t.Updated,t.UpdatedBy, generate_uuid() FROM AD_Language l, C_Campaign t")
        sqlCmd.append(" WHERE l.IsActive='Y' AND l.IsSystemLanguage='Y' AND l.IsBaseLanguage='N' AND t.C_Campaign_ID=").append(C_Campaign_ID)
        sqlCmd.append(" AND NOT EXISTS (SELECT * FROM C_Campaign_Trl tt WHERE tt.AD_Language=l.AD_Language AND tt.C_Campaign_ID=t.C_Campaign_ID)")
        no = DB.executeUpdateEx(sqlCmd.toString(), m_trx.trxName)
        if (no < 0)
            log.log(Level.SEVERE, "Campaign Translation NOT inserted")

        //	Create Sales Region
        val C_SalesRegion_ID = getNextID(aD_Client_ID, "C_SalesRegion")
        sqlCmd = StringBuffer("INSERT INTO C_SalesRegion ")
        sqlCmd.append("(C_SalesRegion_ID,").append(m_stdColumns).append(",")
        sqlCmd.append(" Value,Name,IsSummary,C_SalesRegion_UU) VALUES (")
        sqlCmd.append(C_SalesRegion_ID).append(",").append(m_stdValues).append(", ")
        sqlCmd.append(defaultEntry).append(defaultEntry).append("'N'").append(",").append(DB.TO_STRING(UUID.randomUUID().toString())).append(")")
        no = DB.executeUpdateEx(sqlCmd.toString(), m_trx.trxName)
        if (no == 1)
            m_info!!.append(Msg.translate(m_lang, "C_SalesRegion_ID")).append("=").append(defaultName).append("\n")
        else
            log.log(Level.SEVERE, "SalesRegion NOT inserted")
        if (m_hasSRegion) {
            //  Default
            sqlCmd = StringBuffer("UPDATE C_AcctSchema_Element SET ")
            sqlCmd.append("C_SalesRegion_ID=").append(C_SalesRegion_ID)
            sqlCmd.append(" WHERE C_AcctSchema_ID=").append(m_as!!.c_AcctSchema_ID)
            sqlCmd.append(" AND ElementType='SR'")
            no = DB.executeUpdateEx(sqlCmd.toString(), m_trx.trxName)
            if (no != 1)
                log.log(Level.SEVERE, "AcctSchema Element SalesRegion NOT updated")
        }
        // Sales Region Translation
        sqlCmd = StringBuffer("INSERT INTO C_SalesRegion_Trl (AD_Language,C_SalesRegion_ID, Description,Name, IsTranslated,AD_Client_ID,AD_Org_ID,Created,Createdby,Updated,UpdatedBy,C_SalesRegion_Trl_UU)")
        sqlCmd.append(" SELECT l.AD_Language,t.C_SalesRegion_ID, t.Description,t.Name, 'N',t.AD_Client_ID,t.AD_Org_ID,t.Created,t.Createdby,t.Updated,t.UpdatedBy, generate_uuid() FROM AD_Language l, C_SalesRegion t")
        sqlCmd.append(" WHERE l.IsActive='Y' AND l.IsSystemLanguage='Y' AND l.IsBaseLanguage='N' AND t.C_SalesRegion_ID=").append(C_SalesRegion_ID)
        sqlCmd.append(" AND NOT EXISTS (SELECT * FROM C_SalesRegion_Trl tt WHERE tt.AD_Language=l.AD_Language AND tt.C_SalesRegion_ID=t.C_SalesRegion_ID)")
        no = DB.executeUpdateEx(sqlCmd.toString(), m_trx.trxName)
        if (no < 0)
            log.log(Level.SEVERE, "Sales Region Translation NOT inserted")

        //	Create Activity
        val C_Activity_ID = getNextID(aD_Client_ID, "C_Activity")
        sqlCmd = StringBuffer("INSERT INTO C_Activity ")
        sqlCmd.append("(C_Activity_ID,").append(m_stdColumns).append(",")
        sqlCmd.append(" Value,Name,IsSummary,C_Activity_UU) VALUES (")
        sqlCmd.append(C_Activity_ID).append(",").append(m_stdValues).append(", ")
        sqlCmd.append(defaultEntry).append(defaultEntry).append("'N'").append(",").append(DB.TO_STRING(UUID.randomUUID().toString())).append(")")
        no = DB.executeUpdateEx(sqlCmd.toString(), m_trx.trxName)
        if (no == 1)
            m_info!!.append(Msg.translate(m_lang, "C_Activity_ID")).append("=").append(defaultName).append("\n")
        else
            log.log(Level.SEVERE, "Activity NOT inserted")
        if (m_hasActivity) {
            //  Default
            sqlCmd = StringBuffer("UPDATE C_AcctSchema_Element SET ")
            sqlCmd.append("C_Activity_ID=").append(C_Activity_ID)
            sqlCmd.append(" WHERE C_AcctSchema_ID=").append(m_as!!.c_AcctSchema_ID)
            sqlCmd.append(" AND ElementType='AY'")
            no = DB.executeUpdateEx(sqlCmd.toString(), m_trx.trxName)
            if (no != 1)
                log.log(Level.SEVERE, "AcctSchema Element Activity NOT updated")
        }
        // Activity Translation
        sqlCmd = StringBuffer("INSERT INTO C_Activity_Trl (AD_Language,C_Activity_ID, Description,Name, IsTranslated,AD_Client_ID,AD_Org_ID,Created,Createdby,Updated,UpdatedBy,C_Activity_Trl_UU)")
        sqlCmd.append(" SELECT l.AD_Language,t.C_Activity_ID, t.Description,t.Name, 'N',t.AD_Client_ID,t.AD_Org_ID,t.Created,t.Createdby,t.Updated,t.UpdatedBy, generate_uuid() FROM AD_Language l, C_Activity t")
        sqlCmd.append(" WHERE l.IsActive='Y' AND l.IsSystemLanguage='Y' AND l.IsBaseLanguage='N' AND t.C_Activity_ID=").append(C_Activity_ID)
        sqlCmd.append(" AND NOT EXISTS (SELECT * FROM C_Activity_Trl tt WHERE tt.AD_Language=l.AD_Language AND tt.C_Activity_ID=t.C_Activity_ID)")
        no = DB.executeUpdateEx(sqlCmd.toString(), m_trx.trxName)
        if (no < 0)
            log.log(Level.SEVERE, "Activity Translation NOT inserted")

        /**
         * Business Partner
         */
        //  Create BP Group
        val bpg = MBPGroup(m_ctx, 0, m_trx.trxName)
        bpg.value = defaultName
        bpg.name = defaultName
        bpg.setIsDefault(true)
        if (bpg.save())
            m_info!!.append(Msg.translate(m_lang, "C_BP_Group_ID")).append("=").append(defaultName).append("\n")
        else
            log.log(Level.SEVERE, "BP Group NOT inserted")

        //	Create BPartner
        val bp = MBPartner(m_ctx, 0, m_trx.trxName)
        bp.setValue(defaultName)
        bp.setName(defaultName)
        bp.bpGroup = bpg
        if (bp.save())
            m_info!!.append(Msg.translate(m_lang, "C_BPartner_ID")).append("=").append(defaultName).append("\n")
        else
            log.log(Level.SEVERE, "BPartner NOT inserted")
        //  Location for Standard BP
        val bpLoc = MLocation(m_ctx, C_Country_ID, C_Region_ID, City, m_trx.trxName)
        bpLoc.saveEx()
        val bpl = MBPartnerLocation(bp)
        bpl.c_Location_ID = bpLoc.c_Location_ID
        if (!bpl.save())
            log.log(Level.SEVERE, "BP_Location (Standard) NOT inserted")
        //  Default
        sqlCmd = StringBuffer("UPDATE C_AcctSchema_Element SET ")
        sqlCmd.append("C_BPartner_ID=").append(bp.c_BPartner_ID)
        sqlCmd.append(" WHERE C_AcctSchema_ID=").append(m_as!!.c_AcctSchema_ID)
        sqlCmd.append(" AND ElementType='BP'")
        no = DB.executeUpdateEx(sqlCmd.toString(), m_trx.trxName)
        if (no != 1)
            log.log(Level.SEVERE, "AcctSchema Element BPartner NOT updated")
        createPreference("C_BPartner_ID", bp.c_BPartner_ID.toString(), 143)

        /**
         * Product
         */
        //  Create Product Category
        val pc = MProductCategory(m_ctx, 0, m_trx.trxName)
        pc.value = defaultName
        pc.name = defaultName
        pc.setIsDefault(true)
        if (pc.save())
            m_info!!.append(Msg.translate(m_lang, "M_Product_Category_ID")).append("=").append(defaultName).append("\n")
        else
            log.log(Level.SEVERE, "Product Category NOT inserted")

        //  UOM (EA)
        val C_UOM_ID = 100

        //  TaxCategory
        val C_TaxCategory_ID = getNextID(aD_Client_ID, "C_TaxCategory")
        sqlCmd = StringBuffer("INSERT INTO C_TaxCategory ")
        sqlCmd.append("(C_TaxCategory_ID,").append(m_stdColumns).append(",")
        sqlCmd.append(" Name,IsDefault,C_TaxCategory_UU) VALUES (")
        sqlCmd.append(C_TaxCategory_ID).append(",").append(m_stdValues).append(", ")
        if (C_Country_ID == SystemIDs.COUNTRY_US)
        // US
            sqlCmd.append("'Sales Tax','Y',")
        else
            sqlCmd.append(defaultEntry).append("'Y',")
        sqlCmd.append(DB.TO_STRING(UUID.randomUUID().toString())).append(")")
        no = DB.executeUpdateEx(sqlCmd.toString(), m_trx.trxName)
        if (no != 1)
            log.log(Level.SEVERE, "TaxCategory NOT inserted")

        //  TaxCategory translation
        sqlCmd = StringBuffer("INSERT INTO C_TaxCategory_Trl (AD_Language,C_TaxCategory_ID, Description,Name, IsTranslated,AD_Client_ID,AD_Org_ID,Created,Createdby,Updated,UpdatedBy,C_TaxCategory_Trl_UU)")
        sqlCmd.append(" SELECT l.AD_Language,t.C_TaxCategory_ID, t.Description,t.Name, 'N',t.AD_Client_ID,t.AD_Org_ID,t.Created,t.Createdby,t.Updated,t.UpdatedBy, generate_uuid() FROM AD_Language l, C_TaxCategory t")
        sqlCmd.append(" WHERE l.IsActive='Y' AND l.IsSystemLanguage='Y' AND l.IsBaseLanguage='N' AND t.C_TaxCategory_ID=").append(C_TaxCategory_ID)
        sqlCmd.append(" AND NOT EXISTS (SELECT * FROM C_TaxCategory_Trl tt WHERE tt.AD_Language=l.AD_Language AND tt.C_TaxCategory_ID=t.C_TaxCategory_ID)")
        no = DB.executeUpdateEx(sqlCmd.toString(), m_trx.trxName)
        if (no < 0)
            log.log(Level.SEVERE, "TaxCategory Translation NOT inserted")

        //  Tax - Zero Rate
        val tax = MTax(m_ctx, "Standard", Env.ZERO, C_TaxCategory_ID, m_trx.trxName)
        tax.setIsDefault(true)
        if (tax.save())
            m_info!!.append(Msg.translate(m_lang, "C_Tax_ID"))
                    .append("=").append(tax.name).append("\n")
        else
            log.log(Level.SEVERE, "Tax NOT inserted")

        //	Create Product
        val product = MProduct(m_ctx, 0, m_trx.trxName)
        product.value = defaultName
        product.name = defaultName
        product.c_UOM_ID = C_UOM_ID
        product.m_Product_Category_ID = pc.m_Product_Category_ID
        product.c_TaxCategory_ID = C_TaxCategory_ID
        if (product.save())
            m_info!!.append(Msg.translate(m_lang, "M_Product_ID")).append("=").append(defaultName).append("\n")
        else
            log.log(Level.SEVERE, "Product NOT inserted")
        //  Default
        sqlCmd = StringBuffer("UPDATE C_AcctSchema_Element SET ")
        sqlCmd.append("M_Product_ID=").append(product.m_Product_ID)
        sqlCmd.append(" WHERE C_AcctSchema_ID=").append(m_as!!.c_AcctSchema_ID)
        sqlCmd.append(" AND ElementType='PR'")
        no = DB.executeUpdateEx(sqlCmd.toString(), m_trx.trxName)
        if (no != 1)
            log.log(Level.SEVERE, "AcctSchema Element Product NOT updated")

        /**
         * Location, Warehouse, Locator
         */
        //  Location (Company)
        val loc = MLocation(m_ctx, C_Country_ID, C_Region_ID, City, m_trx.trxName)
        loc.address1 = address1
        loc.postal = postal
        loc.saveEx()
        sqlCmd = StringBuffer("UPDATE AD_OrgInfo SET C_Location_ID=")
        sqlCmd.append(loc.c_Location_ID).append(" WHERE AD_Org_ID=").append(aD_Org_ID)
        no = DB.executeUpdateEx(sqlCmd.toString(), m_trx.trxName)
        if (no != 1)
            log.log(Level.SEVERE, "Location NOT inserted")
        createPreference("C_Country_ID", C_Country_ID.toString(), 0)

        //  Default Warehouse
        val locwh = MLocation(m_ctx, C_Country_ID, C_Region_ID, City, m_trx.trxName)
        locwh.address1 = address1
        locwh.postal = postal
        locwh.saveEx()
        val wh = MWarehouse(m_ctx, 0, m_trx.trxName)
        wh.value = defaultName
        wh.name = defaultName
        wh.c_Location_ID = locwh.c_Location_ID
        if (!wh.save())
            log.log(Level.SEVERE, "Warehouse NOT inserted")

        //   Locator
        val locator = MLocator(wh, defaultName)
        locator.setIsDefault(true)
        if (!locator.save())
            log.log(Level.SEVERE, "Locator NOT inserted")

        //  Update ClientInfo
        sqlCmd = StringBuffer("UPDATE AD_ClientInfo SET ")
        sqlCmd.append("C_BPartnerCashTrx_ID=").append(bp.c_BPartner_ID)
        sqlCmd.append(",M_ProductFreight_ID=").append(product.m_Product_ID)
        //		sqlCmd.append("C_UOM_Volume_ID=");
        //		sqlCmd.append(",C_UOM_Weight_ID=");
        //		sqlCmd.append(",C_UOM_Length_ID=");
        //		sqlCmd.append(",C_UOM_Time_ID=");
        sqlCmd.append(" WHERE AD_Client_ID=").append(aD_Client_ID)
        no = DB.executeUpdateEx(sqlCmd.toString(), m_trx.trxName)
        if (no != 1) {
            val err = "ClientInfo not updated"
            log.log(Level.SEVERE, err)
            m_info!!.append(err)
            return false
        }

        /**
         * Other
         */
        //  PriceList
        val pl = MPriceList(m_ctx, 0, m_trx.trxName)
        pl.name = defaultName
        pl.c_Currency_ID = C_Currency_ID
        pl.setIsDefault(true)
        if (!pl.save())
            log.log(Level.SEVERE, "PriceList NOT inserted")
        //  Price List
        val ds = MDiscountSchema(m_ctx, 0, m_trx.trxName)
        ds.name = defaultName
        ds.discountType = MDiscountSchema.DISCOUNTTYPE_Pricelist
        if (!ds.save())
            log.log(Level.SEVERE, "DiscountSchema NOT inserted")
        //  PriceList Version
        val plv = MPriceListVersion(pl)
        plv.setName()
        plv.m_DiscountSchema_ID = ds.m_DiscountSchema_ID
        if (!plv.save())
            log.log(Level.SEVERE, "PriceList_Version NOT inserted")
        //  ProductPrice
        val pp = MProductPrice(plv, product.m_Product_ID,
                Env.ONE, Env.ONE, Env.ONE)
        if (!pp.save())
            log.log(Level.SEVERE, "ProductPrice NOT inserted")


        //	Create Sales Rep for Client-User
        val bpCU = MBPartner(m_ctx, 0, m_trx.trxName)
        bpCU.setValue(AD_User_U_Name!!)
        bpCU.setName(AD_User_U_Name!!)
        bpCU.bpGroup = bpg
        bpCU.setIsEmployee(true)
        bpCU.setIsSalesRep(true)
        if (bpCU.save())
            m_info!!.append(Msg.translate(m_lang, "SalesRep_ID")).append("=").append(AD_User_U_Name).append("\n")
        else
            log.log(Level.SEVERE, "SalesRep (User) NOT inserted")
        //  Location for Client-User
        val bpLocCU = MLocation(m_ctx, C_Country_ID, C_Region_ID, City, m_trx.trxName)
        bpLocCU.saveEx()
        val bplCU = MBPartnerLocation(bpCU)
        bplCU.c_Location_ID = bpLocCU.c_Location_ID
        if (!bplCU.save())
            log.log(Level.SEVERE, "BP_Location (User) NOT inserted")
        //  Update User
        sqlCmd = StringBuffer("UPDATE AD_User SET C_BPartner_ID=")
        sqlCmd.append(bpCU.c_BPartner_ID).append(" WHERE AD_User_ID=").append(AD_User_U_ID)
        no = DB.executeUpdateEx(sqlCmd.toString(), m_trx.trxName)
        if (no != 1)
            log.log(Level.SEVERE, "User of SalesRep (User) NOT updated")


        //	Create Sales Rep for Client-Admin
        val bpCA = MBPartner(m_ctx, 0, m_trx.trxName)
        bpCA.setValue(AD_User_Name!!)
        bpCA.setName(AD_User_Name!!)
        bpCA.bpGroup = bpg
        bpCA.setIsEmployee(true)
        bpCA.setIsSalesRep(true)
        if (bpCA.save())
            m_info!!.append(Msg.translate(m_lang, "SalesRep_ID")).append("=").append(AD_User_Name).append("\n")
        else
            log.log(Level.SEVERE, "SalesRep (Admin) NOT inserted")
        //  Location for Client-Admin
        val bpLocCA = MLocation(m_ctx, C_Country_ID, C_Region_ID, City, m_trx.trxName)
        bpLocCA.saveEx()
        val bplCA = MBPartnerLocation(bpCA)
        bplCA.c_Location_ID = bpLocCA.c_Location_ID
        if (!bplCA.save())
            log.log(Level.SEVERE, "BP_Location (Admin) NOT inserted")
        //  Update User
        sqlCmd = StringBuffer("UPDATE AD_User SET C_BPartner_ID=")
        sqlCmd.append(bpCA.c_BPartner_ID).append(" WHERE AD_User_ID=").append(aD_User_ID)
        no = DB.executeUpdateEx(sqlCmd.toString(), m_trx.trxName)
        if (no != 1)
            log.log(Level.SEVERE, "User of SalesRep (Admin) NOT updated")


        //  Payment Term
        val C_PaymentTerm_ID = getNextID(aD_Client_ID, "C_PaymentTerm")
        sqlCmd = StringBuffer("INSERT INTO C_PaymentTerm ")
        sqlCmd.append("(C_PaymentTerm_ID,").append(m_stdColumns).append(",")
        sqlCmd.append("Value,Name,NetDays,GraceDays,DiscountDays,Discount,DiscountDays2,Discount2,IsDefault,C_PaymentTerm_UU) VALUES (")
        sqlCmd.append(C_PaymentTerm_ID).append(",").append(m_stdValues).append(",")
        sqlCmd.append("'Immediate','Immediate',0,0,0,0,0,0,'Y'").append(",").append(DB.TO_STRING(UUID.randomUUID().toString())).append(")")
        no = DB.executeUpdateEx(sqlCmd.toString(), m_trx.trxName)
        if (no != 1)
            log.log(Level.SEVERE, "PaymentTerm NOT inserted")
        // Payment Term Translation
        sqlCmd = StringBuffer("INSERT INTO C_PaymentTerm_Trl (AD_Language,C_PaymentTerm_ID, Description,Name, IsTranslated,AD_Client_ID,AD_Org_ID,Created,Createdby,Updated,UpdatedBy,C_PaymentTerm_Trl_UU)")
        sqlCmd.append(" SELECT l.AD_Language,t.C_PaymentTerm_ID, t.Description,t.Name, 'N',t.AD_Client_ID,t.AD_Org_ID,t.Created,t.Createdby,t.Updated,t.UpdatedBy, generate_uuid() FROM AD_Language l, C_PaymentTerm t")
        sqlCmd.append(" WHERE l.IsActive='Y' AND l.IsSystemLanguage='Y' AND l.IsBaseLanguage='N' AND t.C_PaymentTerm_ID=").append(C_PaymentTerm_ID)
        sqlCmd.append(" AND NOT EXISTS (SELECT * FROM C_PaymentTerm_Trl tt WHERE tt.AD_Language=l.AD_Language AND tt.C_PaymentTerm_ID=t.C_PaymentTerm_ID)")
        no = DB.executeUpdateEx(sqlCmd.toString(), m_trx.trxName)
        if (no < 0)
            log.log(Level.SEVERE, "Payment Term Translation NOT inserted")

        //  Project Cycle
        C_Cycle_ID = getNextID(aD_Client_ID, "C_Cycle")
        sqlCmd = StringBuffer("INSERT INTO C_Cycle ")
        sqlCmd.append("(C_Cycle_ID,").append(m_stdColumns).append(",")
        sqlCmd.append(" Name,C_Currency_ID,C_Cycle_UU) VALUES (")
        sqlCmd.append(C_Cycle_ID).append(",").append(m_stdValues).append(", ")
        sqlCmd.append(defaultEntry).append(C_Currency_ID).append(",").append(DB.TO_STRING(UUID.randomUUID().toString())).append(")")
        no = DB.executeUpdateEx(sqlCmd.toString(), m_trx.trxName)
        if (no != 1)
            log.log(Level.SEVERE, "Cycle NOT inserted")

        /**
         * Organization level data	===========================================
         */

        //	Create Default Project
        val C_Project_ID = getNextID(aD_Client_ID, "C_Project")
        sqlCmd = StringBuffer("INSERT INTO C_Project ")
        sqlCmd.append("(C_Project_ID,").append(m_stdColumns).append(",")
        sqlCmd.append(" Value,Name,C_Currency_ID,IsSummary,C_Project_UU) VALUES (")
        sqlCmd.append(C_Project_ID).append(",").append(m_stdValuesOrg).append(", ")
        sqlCmd.append(defaultEntry).append(defaultEntry).append(C_Currency_ID).append(",'N'").append(",").append(DB.TO_STRING(UUID.randomUUID().toString())).append(")")
        no = DB.executeUpdateEx(sqlCmd.toString(), m_trx.trxName)
        if (no == 1)
            m_info!!.append(Msg.translate(m_lang, "C_Project_ID")).append("=").append(defaultName).append("\n")
        else
            log.log(Level.SEVERE, "Project NOT inserted")
        //  Default Project
        if (m_hasProject) {
            sqlCmd = StringBuffer("UPDATE C_AcctSchema_Element SET ")
            sqlCmd.append("C_Project_ID=").append(C_Project_ID)
            sqlCmd.append(" WHERE C_AcctSchema_ID=").append(m_as!!.c_AcctSchema_ID)
            sqlCmd.append(" AND ElementType='PJ'")
            no = DB.executeUpdateEx(sqlCmd.toString(), m_trx.trxName)
            if (no != 1)
                log.log(Level.SEVERE, "AcctSchema Element Project NOT updated")
        }

        //  CashBook
        val cb = MCashBook(m_ctx, 0, m_trx.trxName)
        cb.name = defaultName
        cb.c_Currency_ID = C_Currency_ID
        if (cb.save())
            m_info!!.append(Msg.translate(m_lang, "C_CashBook_ID")).append("=").append(defaultName).append("\n")
        else
            log.log(Level.SEVERE, "CashBook NOT inserted")
        //
        val success = m_trx.commit()
        m_trx.close()
        log.info("finish")
        return success
    }   //  createEntities

    /**
     * Create Preference
     * @param Attribute attribute
     * @param Value value
     * @param AD_Window_ID window
     */
    private fun createPreference(Attribute: String, Value: String, AD_Window_ID: Int) {
        val AD_Preference_ID = getNextID(aD_Client_ID, "AD_Preference")
        val sqlCmd = StringBuilder("INSERT INTO AD_Preference ")
        sqlCmd.append("(AD_Preference_ID,").append("AD_Preference_UU,").append(m_stdColumns).append(",")
        sqlCmd.append("Attribute,Value,AD_Window_ID) VALUES (")
        sqlCmd.append(AD_Preference_ID).append(",").append(DB.TO_STRING(UUID.randomUUID().toString())).append(",").append(m_stdValues).append(",")
        sqlCmd.append("'").append(Attribute).append("','").append(Value).append("',")
        if (AD_Window_ID == 0)
            sqlCmd.append("NULL)")
        else
            sqlCmd.append(AD_Window_ID).append(")")
        val no = DB.executeUpdateEx(sqlCmd.toString(), m_trx!!.trxName)
        if (no != 1)
            log.log(Level.SEVERE, "Preference NOT inserted - $Attribute")
    }   //  createPreference


    /**************************************************************************
     * Get Next ID
     * @param AD_Client_ID client
     * @param TableName table name
     * @return id
     */
    private fun getNextID(AD_Client_ID: Int, TableName: String): Int {
        //	TODO: Exception
        return MSequence.getNextID(AD_Client_ID, TableName, m_trx!!.trxName)
    }    //	getNextID

    /**
     * Rollback Internal Transaction
     */
    fun rollback() {
        try {
            m_trx!!.rollback()
            m_trx.close()
        } catch (e: Exception) {
        }

    }

}   //  MSetup
