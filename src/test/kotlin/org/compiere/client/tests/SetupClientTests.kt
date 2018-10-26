package org.compiere.client.tests

import company.bigger.test.support.BaseComponentTest
import company.bigger.test.support.randomString
import org.compiere.crm.MCountry
import org.compiere.process.ProcessInfo
import org.idempiere.process.InitialClientSetup
import org.junit.Test
import kotlin.test.assertTrue

class SetupClientTests : BaseComponentTest() {
    @Test
    fun `creating a new client should work`() {
        val clientName = randomString(20)
        val orgValue= randomString(20)
        val orgName= randomString(20)
        val userClient= randomString(20)
        val eMail= randomString(5) + "@test.com"
        val adminEmail= randomString(5) + "@test.com"
        val countryId = 101
        // TODO: fix this bug - the list of the countries needs to be prepared before calling the InitialClientSetup
        val country = MCountry.get(ctx, countryId)

        val clientSetup = InitialClientSetup(
            p_ClientName = clientName, p_OrgValue = orgValue, p_OrgName = orgName, p_AdminUserName = adminEmail,
            p_NormalUserName = userClient, p_EMail = eMail, p_UseDefaultCoA = false, p_C_Currency_ID = 102, // EUR
            p_C_Country_ID = country._ID, // Germany - Deutschland
            p_CoAFile = "src/test/resources/coa/AccountingCZ.csv"
        )
        val pi = ProcessInfo("", 0)
        assertTrue(clientSetup.startProcess(ctx, pi, null))
    }
}