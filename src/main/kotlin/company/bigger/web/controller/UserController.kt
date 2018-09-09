package company.bigger.web.controller

import company.bigger.dto.UserLoginModel
import company.bigger.dto.UserLoginModelResponse
import company.bigger.web.jwt.ApiKeySecured
import company.bigger.web.jwt.ApiKeySecuredAspect
import org.compiere.model.I_AD_User
import org.springframework.web.bind.annotation.*

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
    fun me(@RequestHeader(value = "Authorization") authorization: String): I_AD_User? {
        return ApiKeySecuredAspect.processAuthorization(authorization, userService, {}, { userService.currentUser() }) as I_AD_User?
    }
}