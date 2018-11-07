package company.bigger.test

import company.bigger.test.graphql.MeRequest
import company.bigger.test.graphql.MeResponse
import company.bigger.test.graphql.UsersRequest
import company.bigger.test.graphql.UsersResponse
import company.bigger.test.support.BaseIntegrationTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class UserIntegrationTests : BaseIntegrationTest() {
    @Test
    fun `can get me as user`() {
        val responseEntity = getGraphQL(MeRequest::class.java, MeResponse::class.java)
        val result = responseEntity.response
        assertNotNull(result.description)
        val description = result.description!!
        assertEquals(USER, description)
    }

    @Test
    fun `can get users`() {
        val responseEntity = getGraphQL(UsersRequest::class.java, UsersResponse::class.java)
        val result = responseEntity.response
        assertNotNull(result.users)
        val users = result.users!!
        assert(users.count() > 0)
        users.first { it?.description == USER }
    }
}