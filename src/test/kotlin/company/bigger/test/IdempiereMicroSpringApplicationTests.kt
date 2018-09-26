package company.bigger.test

import company.bigger.web.controller.UserController
import company.bigger.test.support.BaseTest
import company.bigger.Micro
import company.bigger.dto.UserLoginModel
import company.bigger.service.UserService
import company.bigger.test.clients.LoginClient
import company.bigger.test.clients.UserClient
import company.bigger.test.support.randomString
import feign.Feign
import feign.FeignException
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
import org.springframework.boot.web.server.LocalServerPort
import kotlin.test.*

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class IdempiereMicroSpringApplicationTests : BaseTest() {

    @Test
    fun contextLoads() {
    }

    @Autowired
    private lateinit var userService: UserService
    @Test
    fun `GardenUser can login (service)`() {
        val result = userService.login(UserLoginModel("GardenUser", "GardenUser"))
        assertNotNull(result?.token)
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
        userClient = buildClient(UserClient::class.java)
    }

    @Test
    fun `GardenUser can login (controller)`() {
        val result = userController.login("GardenUser", "GardenUser")
        assertNotNull(result?.token)
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
    protected var userClient: UserClient? = null

    @Test
    fun `GardenUser can login (REST)`() {
        val gardenUserLogin = loginClient?.login("GardenUser", "GardenUser")
        println("$gardenUserLogin")
        assertNotNull(gardenUserLogin)
        gardenUserLogin!!
        assertTrue { gardenUserLogin.logged }
    }
    @Test
    fun `GardenUser can login and token works`() {
        val gardenUserLogin = loginClient?.login("GardenUser", "GardenUser")
        println("$gardenUserLogin")
        assertNotNull(gardenUserLogin)
        gardenUserLogin!!
        assertTrue { gardenUserLogin.logged }
        val token = gardenUserLogin.token
        assertNotNull(token)
        token!!
        val profile = userClient?.profile(token)
        assertEquals("GardenUser", profile?.firstName)
    }

    @Test
    fun `GardenUser can login and see other users`() {
        val gardenUserLogin = loginClient?.login("GardenUser", "GardenUser")
        println("$gardenUserLogin")
        assertNotNull(gardenUserLogin)
        gardenUserLogin!!
        assertTrue { gardenUserLogin.logged }
        val token = gardenUserLogin.token
        assertNotNull(token)
        token!!
        val profiles = userClient?.all(token)
        val found = profiles?.firstOrNull { it.firstName == "GardenUser" }
        assertNotNull(found)
    }

    @Test
    fun `Random user can not login (REST)`() {
        val gardenUserLogin = loginClient?.login(randomString(10), randomString(10))
        println("$gardenUserLogin")
        assertNotNull(gardenUserLogin)
        gardenUserLogin!!
        assertFalse { gardenUserLogin.logged }
    }

    @Test
    fun `Random token does not work`() {
        val token = randomString(50)
        try {
            userClient?.profile(token)

        } catch (ex: FeignException) {
            if (ex.status()!=401) fail("Should HTTP 401 fail, not $ex")
        }
    }
}
