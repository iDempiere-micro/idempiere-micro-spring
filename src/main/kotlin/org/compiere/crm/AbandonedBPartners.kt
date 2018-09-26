package org.compiere.crm

class AbandonedBPartners : SimpleBPartnerSearch() {
    override fun whereCondition(): String {
        return "salesrep_id is null"
    }
}