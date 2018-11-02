package org.compiere.crm

import org.compiere.model.I_C_BPartner
import org.compiere.orm.BasePOName
import org.idempiere.orm.I_Persistent
import org.idempiere.orm.POInfo
import java.sql.ResultSet
import java.util.Properties

class MCrmCustomerCategory : BasePOName, I_Persistent {
    constructor(ctx: Properties, ID: Int, trxName: String?) : super(ctx, ID, trxName)
    constructor (ctx: Properties, rs: ResultSet, trxName: String?) : super(ctx, rs, trxName)
    constructor (ctx: Properties, rs: ResultSet, trxName: String?, a: String?) : super(ctx, rs, trxName, a)

    companion object {
        const val Table_ID = 1000001
        const val Table_Name = "Crm_Customer_Category"
    }

    override fun initPO(ctx: Properties?): POInfo {
        return POInfo.getPOInfo(ctx, Table_ID, _TrxName)
    }

    override fun get_AccessLevel(): Int {
        return 3 // AccessLevel = 3 - Client - Org
    }

    var bPartner: I_C_BPartner
        get() {
            return MBPartner(ctx, get_Value(I_C_BPartner.COLUMNNAME_C_BPartner_ID) as Int, null)
        }
        set(bp) {
            set_ValueNoCheck(I_C_BPartner.COLUMNNAME_C_BPartner_ID, bp.c_BPartner_ID)
        }

    var category: MCrmCategory
        get() {
            return MCrmCategory(ctx, get_Value("Crm_Category_ID") as Int, null)
        }
        set(cat) {
            set_ValueNoCheck("Crm_Category_ID", cat._ID)
        }
}
