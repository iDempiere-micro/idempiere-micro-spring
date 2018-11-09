package company.bigger.web.controller

import org.compiere.model.I_AD_User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import company.bigger.service.UserService

// TODO: remove and replace with GraphQL
@Deprecated("Replace with GraphQL")
data class User(
    val id: Int,
    val name: String,
    val description: String?
) {
    constructor(user: I_AD_User): this(user.id, user.name, user.description)
}

// TODO: remove and replace with GraphQL
@Deprecated("Replace with GraphQL")
@RestController
open class UserController(
    private val userService: UserService
) {
    @GetMapping()
    @RequestMapping(value = ["/user/me"])
    fun me(): User? {
        return userService.currentUser()?.let { User(it) }
    }

    @GetMapping()
    @RequestMapping(value = ["/users"])
    fun all(): List<User> {
        return userService.getUsers().map { User(it) }
    }
}