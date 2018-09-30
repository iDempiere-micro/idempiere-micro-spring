package company.bigger.test.support

import company.bigger.Micro
import company.bigger.test.clients.BPartnerClient
import company.bigger.test.clients.LoginClient
import company.bigger.test.clients.UserClient
import feign.Feign
import feign.gson.GsonDecoder
import feign.gson.GsonEncoder
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.core.env.Environment
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
open class BaseIntegrationTest : BaseTest() {
    @Autowired
    protected lateinit var environment: Environment

    @LocalServerPort
    var randomServerPort: Int = 0

    private fun <T> buildClient(t: Class<T>): T {
        environment.let {
            return Feign.builder()
                    .encoder(GsonEncoder()).decoder(GsonDecoder())
                    .target(t, "http://localhost:$randomServerPort")
        }
    }

    @Autowired
    private lateinit var micro: Micro

    protected var loginClient: LoginClient? = null
    protected var userClient: UserClient? = null
    protected var bpartnerClient: BPartnerClient? = null

    @Before
    override fun prepare() {
        super.prepare()
        micro.startup()
        loginClient = buildClient(LoginClient::class.java)
        userClient = buildClient(UserClient::class.java)
        bpartnerClient = buildClient(BPartnerClient::class.java)
    }

    @Test
    fun contextLoadsBase() {
    }

    protected fun getGardenUserToken(): String {
        val gardenUserLogin = loginClient?.login("GardenUser", "GardenUser")
        gardenUserLogin!!
        val token = gardenUserLogin.token
        token!!
        return token
    }
}