package org.compiere.crm

import java.sql.PreparedStatement

abstract class SimpleBPartnerSearch : BaseBPartnerSearch() {
    override fun setStatementParams(statement: PreparedStatement) {
        statement.setInt(1, AD_CLIENT_ID)
        statement.setInt(2, AD_ORG_ID)
        statement.setInt(3, AD_ORG_ID)
    }
}