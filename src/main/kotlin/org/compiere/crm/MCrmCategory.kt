package org.compiere.crm

import org.compiere.orm.BasePONameValue
import org.idempiere.orm.I_Persistent
import org.idempiere.orm.POInfo
import java.util.Properties

class MCrmCategory(ctx: Properties, crm_category_ID: Int, trxName: String?) : BasePONameValue(ctx, crm_category_ID, trxName), I_Persistent {
    companion object {
        const val Table_ID = 200227
        const val Table_Name = "Crm_Category"
    }

    override fun initPO(ctx: Properties?): POInfo {
        return POInfo.getPOInfo(ctx, Table_ID, _TrxName)
    }

    override fun get_AccessLevel(): Int {
        return 3 // AccessLevel = 3 - Client - Org
    }
}
