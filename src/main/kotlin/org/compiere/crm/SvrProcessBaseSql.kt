package org.compiere.crm

import org.idempiere.common.util.DB
import software.hsharp.business.models.IDTOReady
import java.sql.Connection

abstract class SvrProcessBaseSql(
    _AD_CLIENT_ID: Int = 0,
    _AD_ORG_ID: Int = 0,
    _AD_USER_ID: Int = 0
) : SvrProcessBase(_AD_CLIENT_ID, _AD_ORG_ID, _AD_USER_ID) {
    protected abstract fun getSqlResult(cnn: Connection): IDTOReady
    protected abstract val isRO: Boolean

    override fun getResult(): IDTOReady {
        val cnn = if (isRO) {
            DB.getConnectionRO() } else { DB.getConnectionRW() }
        try {
            val result = getSqlResult(cnn)
            return result
        } finally {
            cnn.close()
        }
    }
}