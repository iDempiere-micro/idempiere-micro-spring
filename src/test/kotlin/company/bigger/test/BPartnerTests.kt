package company.bigger.test

import company.bigger.dto.CreateCustomerModel
import company.bigger.test.support.BaseIntegrationTest
import company.bigger.test.support.randomString
import org.junit.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class BPartnerTests : BaseIntegrationTest() {

    @Test
    fun contextLoads() {
    }

    @Test
    fun `create business partner`() {
        val name = randomString(10)
        val result = bpartnerClient?.create(getGardenUserToken(), CreateCustomerModel(bpName = name, value = name))
        assertNotNull(result)
        result!!
        assertTrue(result.c_BPartner_Id > 0)
    }
}