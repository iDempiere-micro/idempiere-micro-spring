package company.bigger.test

import company.bigger.web.controller.UserController
import company.bigger.test.support.BaseTest
import company.bigger.Micro
import company.bigger.test.clients.LoginClient
import feign.Feign
import feign.gson.GsonDecoder
import feign.gson.GsonEncoder
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertNotNull
import org.springframework.boot.web.server.LocalServerPort

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class IdempiereMicroSpringApplicationTests : BaseTest() {

    @Test
    fun contextLoads() {
    }

    @Autowired
    private lateinit var userController: UserController

    @Autowired
    private lateinit var micro: Micro

    @Before
    override fun prepare() {
        super.prepare()
        micro.startup()
        loginClient = buildClient(LoginClient::class.java)
    }

    @Test
    fun `GardenUser can login (service)`() {
        val result = userController.login("GardenUser", "GardenUser")
        assertNotNull(result.token)
    }

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

    protected var loginClient: LoginClient? = null

    @Test
    fun `GardenUser can login (controller)`() {
        val gardenUserLogin = loginClient?.login("GardenUser", "GardenUser")
        println("$gardenUserLogin")
    }
}
