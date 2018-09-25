package org.compiere.crm

class ForgottenBPartners : SimpleBPartnerSearch() {
    override fun whereCondition(): String {
        return "activity_startdate < current_date or c_contactactivity_id is null"
    }
}