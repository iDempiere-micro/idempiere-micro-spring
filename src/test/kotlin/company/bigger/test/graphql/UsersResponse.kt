package company.bigger.test.graphql

import io.aexp.nodes.graphql.annotations.GraphQLProperty

class UsersResponse(@GraphQLProperty(name = "users") var users: Array<MeResponse?>? = null)