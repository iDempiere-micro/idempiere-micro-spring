package company.bigger.web.jwt

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.authentication.AbstractAuthenticationToken

class TokenBasedAuthentication(private val principle: UserDetails) : AbstractAuthenticationToken(principle.authorities) {

    var token: String? = null

    override fun isAuthenticated(): Boolean {
        return true
    }

    override fun getCredentials(): Any? {
        return token
    }

    override fun getPrincipal(): UserDetails {
        return principle
    }
}