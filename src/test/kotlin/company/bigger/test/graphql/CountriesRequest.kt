package company.bigger.test.graphql

import io.aexp.nodes.graphql.annotations.GraphQLProperty

@GraphQLProperty(name = "countries")
class CountriesRequest(@GraphQLProperty(name = "id") var id: Int? = null, @GraphQLProperty(name = "name") var name: String? = null)