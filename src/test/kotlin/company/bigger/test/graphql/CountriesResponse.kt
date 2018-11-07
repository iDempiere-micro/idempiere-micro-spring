package company.bigger.test.graphql

import io.aexp.nodes.graphql.annotations.GraphQLProperty

class CountriesResponse(@GraphQLProperty(name = "countries") var countries: Array<Country>? = null)