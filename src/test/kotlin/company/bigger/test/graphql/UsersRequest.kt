package company.bigger.test.graphql

import io.aexp.nodes.graphql.annotations.GraphQLProperty

@GraphQLProperty(name = "users")
class UsersRequest(@GraphQLProperty(name = "id") var id: Int? = null, @GraphQLProperty(name = "description") var description: String? = null)