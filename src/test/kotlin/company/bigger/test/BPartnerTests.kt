package company.bigger.test

import company.bigger.dto.CreateCustomerModel
import company.bigger.test.support.BaseIntegrationTest
import company.bigger.test.support.randomString
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
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