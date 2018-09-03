package company.bigger.test.clients.response

data class UserLoginModelResponse(
    val logged: Boolean,
    val clients: List<Client>?,
    val roles: List<Role>?,
    val orgs: List<Org>?,
    val warehouses: List<Any>?,
    val token: String?
)