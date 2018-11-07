package company.bigger.test.graphql

import io.aexp.nodes.graphql.annotations.GraphQLProperty

@GraphQLProperty(name = "me")
class MeRequest(@GraphQLProperty(name = "id") var id: Int? = null, @GraphQLProperty(name = "description") var description: String? = null)