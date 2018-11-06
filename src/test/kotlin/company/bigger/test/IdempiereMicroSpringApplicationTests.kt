package company.bigger.test

import company.bigger.test.support.BaseIntegrationTest
import company.bigger.test.support.randomString
import company.bigger.web.resolver.QueryResolver
import feign.FeignException
import io.aexp.nodes.graphql.GraphQLRequestEntity
import org.junit.Test
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertEquals
import kotlin.test.fail
import io.aexp.nodes.graphql.GraphQLTemplate
import io.aexp.nodes.graphql.annotations.GraphQLProperty

class IdempiereMicroSpringApplicationTests : BaseIntegrationTest() {

    @GraphQLProperty(name="version")
    class Version(@GraphQLProperty(name="v") var v: String? = null)

    @Test
    fun contextLoads() {
    }

    @Test
    fun `GardenUser can login`() {
        val gardenUserLogin = loginClient?.login("GardenUser", "GardenUser")
        println("$gardenUserLogin")
        assertNotNull(gardenUserLogin)
        gardenUserLogin!!
        assertTrue { gardenUserLogin.logged }
    }
    @Test
    fun `GardenUser can login and token works`() {
        val profile = userClient?.profile(getGardenUserToken())
        assertEquals("GardenUser", profile?.name)
    }

    @Test
    fun `GardenUser can login and see other users`() {
        val profiles = userClient?.all(getGardenUserToken())
        val found = profiles?.firstOrNull { it.name == "GardenUser" }
        assertNotNull(found)
    }

    @Test
    fun `Random user can not login`() {
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
            if (ex.status() != 403) fail("Should HTTP 403 fail, not $ex")
        }
    }

    @Test
    fun `Can ask the GraphQL for version`() {
        val token = getGardenUserToken()
        val graphQLTemplate = GraphQLTemplate()
        println("$serverUrl/graphql")
        val requestEntity = GraphQLRequestEntity.Builder()
            .url("$serverUrl/graphql")
            .headers(mapOf("Authorization" to "Bearer $token"))
            .request(Version::class.java)
            .build()
        val responseEntity = graphQLTemplate.query(requestEntity, Version::class.java)
        println(responseEntity)
        assertEquals(QueryResolver.VER, responseEntity.response.v)
    }
}
