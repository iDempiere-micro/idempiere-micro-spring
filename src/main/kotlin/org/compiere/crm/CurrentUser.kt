package org.compiere.crm

import java.io.Serializable

data class CurrentUserResult(val AD_USER_ID: Int) : Serializable

class CurrentUser : SvrProcessBase() {
    override fun getResult(): Serializable {
        return CurrentUserResult(AD_USER_ID)
    }
}