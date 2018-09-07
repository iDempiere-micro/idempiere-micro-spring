package company.bigger.web.controller

import company.bigger.Micro
import company.bigger.dto.UserLoginModel
import company.bigger.dto.UserLoginModelResponse
import company.bigger.service.LoginService
import company.bigger.service.UserService
import company.bigger.web.jwt.ApiKeySecured
import org.compiere.model.I_AD_User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Configurable
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
open class UserController : BaseController() {
    @GetMapping()
    @RequestMapping(value = ["/user/{username}/login/{password}"])
    fun login(@PathVariable username: String, @PathVariable password: String): UserLoginModelResponse? {
        return userService.login(UserLoginModel(username, password))
    }

    @GetMapping()
    @ApiKeySecured
    @RequestMapping(value = ["/user/me"])
    fun me(): I_AD_User? {
        return userService.currentUser()
    }
}