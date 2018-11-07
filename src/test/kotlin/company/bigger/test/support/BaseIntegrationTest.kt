package company.bigger.test.support

import company.bigger.Micro
import company.bigger.test.clients.BPartnerClient
import company.bigger.test.clients.LoginClient
import company.bigger.test.clients.UserClient
import feign.Feign
import feign.gson.GsonDecoder
import feign.gson.GsonEncoder
import io.aexp.nodes.graphql.GraphQLRequestEntity
import io.aexp.nodes.graphql.GraphQLResponseEntity
import io.aexp.nodes.graphql.GraphQLTemplate
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.core.env.Environment
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletPathProvider

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @AutoConfigureMockMvc
open class BaseIntegrationTest : BaseTest() {
    companion object {
        const val USER = "GardenUser"
    }

    @Autowired
    protected lateinit var environment: Environment

    @LocalServerPort
    var randomServerPort: Int = 0

    val serverUrl get() = "http://localhost:$randomServerPort"

    private fun <T> buildClient(t: Class<T>): T {
        environment.let {
            return Feign.builder()
                    .encoder(GsonEncoder()).decoder(GsonDecoder())
                    .target(t, serverUrl)
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
        val gardenUserLogin = loginClient?.login(USER, USER)
        gardenUserLogin!!
        val token = gardenUserLogin.token
        token!!
        return token
    }

    class DummyDispatcherServletPathProvider : DispatcherServletPathProvider {
        override fun getServletPath(): String {
            return ""
        }
    }

    fun <T> getGraphQL(t: Class<T>): GraphQLResponseEntity<T> {
        val token = getGardenUserToken()
        val graphQLTemplate = GraphQLTemplate()
        val requestEntity = GraphQLRequestEntity.Builder()
                .url("$serverUrl/graphql")
                .headers(mapOf("Authorization" to "Bearer $token"))
                .request(t)
                .build()
        return graphQLTemplate.query(requestEntity, t)
    }

    fun <T, R> getGraphQL(t: Class<T>, r: Class<R>): GraphQLResponseEntity<R> {
        val token = getGardenUserToken()
        val graphQLTemplate = GraphQLTemplate()
        val requestEntity = GraphQLRequestEntity.Builder()
                .url("$serverUrl/graphql")
                .headers(mapOf("Authorization" to "Bearer $token"))
                .request(t)
                .build()
        return graphQLTemplate.query(requestEntity, r)
    }
}