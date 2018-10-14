package org.compiere.crm

import org.compiere.orm.BasePO
import org.idempiere.orm.I_Persistent
import org.idempiere.orm.POInfo
import java.util.*

class MCrmCategory(ctx: Properties, crm_category_ID: Int, trxName: String?): BasePO(ctx, crm_category_ID, trxName), I_Persistent {
    companion object {
        const val Table_ID = 200227
    }

    override fun initPO(ctx: Properties?): POInfo {
        return POInfo.getPOInfo(ctx, Table_ID, _TrxName)
    }

    override fun get_AccessLevel(): Int {
        return 3 // AccessLevel = 3 - Client - Org
    }

}
