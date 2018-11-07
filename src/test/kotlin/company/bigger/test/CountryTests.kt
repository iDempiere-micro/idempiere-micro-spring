package company.bigger.test

import company.bigger.test.graphql.CountriesRequest
import company.bigger.test.graphql.CountriesResponse
import company.bigger.test.support.BaseIntegrationTest
import org.junit.Test
import kotlin.test.assertNotNull

class CountryTests : BaseIntegrationTest() {
    companion object {
        const val GER = "Germany - Deutschland"
    }

    @Test
    fun `can get countries`() {
        val responseEntity = getGraphQL(CountriesRequest::class.java, CountriesResponse::class.java)
        val result = responseEntity.response
        assertNotNull(result.countries)
        val countries = result.countries!!
        assert(countries.count() > 0)
        countries.first { it.name == GER }
    }
}