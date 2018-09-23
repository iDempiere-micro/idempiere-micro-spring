package org.compiere.crm

class AbandonedBPartners : SimpleBPartnerSearch() {
    override fun getSql(): String {
        val columns =
            if (full) { "*, C_ContactActivity_ID as activity_C_ContactActivity_ID" } else { "c_bpartner_id,name,taxid" }

        return """select $columns from adempiere.bpartner_v
        where salesrep_id is null
        and ad_client_id IN (0, ?) and ( ad_org_id IN (0,?) or ? = 0) and isactive = 'Y' -- params 1..3
        order by name asc
        """
    }
}