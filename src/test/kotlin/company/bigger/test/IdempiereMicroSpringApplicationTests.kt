package company.bigger.test

import company.bigger.test.support.BaseIntegrationTest
import company.bigger.test.support.randomString
import feign.FeignException
import org.junit.Test
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertEquals
import kotlin.test.fail

class IdempiereMicroSpringApplicationTests : BaseIntegrationTest() {

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
        assertEquals("GardenUser", profile?.firstName)
    }

    @Test
    fun `GardenUser can login and see other users`() {
        val profiles = userClient?.all(getGardenUserToken())
        val found = profiles?.firstOrNull { it.firstName == "GardenUser" }
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
            if (ex.status() != 401) fail("Should HTTP 401 fail, not $ex")
        }
    }
}
