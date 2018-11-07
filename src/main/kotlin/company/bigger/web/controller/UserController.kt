package company.bigger.web.controller

import company.bigger.web.jwt.SecuredApi
import org.compiere.model.I_AD_User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import company.bigger.service.UserService

data class User(
    val id: Int,
    val name: String,
    val description: String?
) {
    constructor(user: I_AD_User): this(user.id, user.name, user.description)
}

@RestController
open class UserController {
    @Autowired
    private lateinit var userService: UserService
    @Autowired
    private lateinit var securedApi: SecuredApi

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