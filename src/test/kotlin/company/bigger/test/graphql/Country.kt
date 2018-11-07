package company.bigger.test.graphql

import io.aexp.nodes.graphql.annotations.GraphQLProperty

class Country(@GraphQLProperty(name = "id") var id: Int? = null, @GraphQLProperty(name = "name") var name: String? = null)