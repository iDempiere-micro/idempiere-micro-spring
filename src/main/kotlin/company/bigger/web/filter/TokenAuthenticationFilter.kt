package company.bigger.web.filter

import company.bigger.dto.UserLoginModel
import company.bigger.service.UserService
import company.bigger.web.jwt.AnonAuthentication
import company.bigger.web.jwt.TokenBasedAuthentication
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.IOException

class TokenAuthenticationFilter(
    internal val userService: UserService
) : OncePerRequestFilter() {

    @Value("\${jwt.header:Authorization}")
    private val AUTH_HEADER: String? = null

    @Value("\${jwt.autologin.name:}")
    private val autoLoginName: String? = null

    @Value("\${jwt.autologin.password:}")
    private val autoLoginPassword: String? = null

    private fun getToken(request: HttpServletRequest): String? {

        val authHeader = request.getHeader(AUTH_HEADER)
        return if (authHeader != null && (authHeader.startsWith("Bearer ") || authHeader.startsWith("Token "))) {
            authHeader.replace("Bearer ", "").replace("Token ", "")
        } else request.parameterMap["access_token"]?.firstOrNull()
    }

    @Throws(IOException::class, ServletException::class)
    public override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        var error = ""
        val authToken = getToken(request)
        val autoLoginName = autoLoginName ?: ""
        val autoLoginPassword = autoLoginPassword ?: ""

        val user =
                if (!autoLoginName.isEmpty() && !autoLoginPassword.isEmpty()) {
                    userService.login(UserLoginModel(loginName = autoLoginName, password = autoLoginPassword))
                } else {
                    // Get username from token
                    userService.findByToken(authToken)
                }

        if (user != null) {
            // Get user
            val userDetails = userService.loadUserByUsername(user.loginName)

            // Create authentication
            val authentication = TokenBasedAuthentication(userDetails)
            authentication.token = authToken
            SecurityContextHolder.getContext().authentication = authentication
            userService.setCurrentUser(user)
        } else {
            error = "Username from token '$authToken' can't be found in DB."
        }
        if (error != "") {
            println(error)
            SecurityContextHolder.getContext().authentication = AnonAuthentication()
        }
        chain.doFilter(request, response)
    }
}