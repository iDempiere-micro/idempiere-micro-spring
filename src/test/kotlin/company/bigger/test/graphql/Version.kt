package company.bigger.test.graphql

import io.aexp.nodes.graphql.annotations.GraphQLProperty

@GraphQLProperty(name = "version")
class Version(@GraphQLProperty(name = "v") var v: String? = null)