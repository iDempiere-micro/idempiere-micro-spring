package company.bigger.test.graphql

import io.aexp.nodes.graphql.annotations.GraphQLProperty

@GraphQLProperty(name = "me")
class MeResponse(@GraphQLProperty(name = "description") var description: String? = null, @GraphQLProperty(name = "id") var id: String? = null)