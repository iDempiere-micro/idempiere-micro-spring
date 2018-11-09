package company.bigger.web.jwt

import org.springframework.security.authentication.AbstractAuthenticationToken

class AnonAuthentication : AbstractAuthenticationToken(null) {

    override fun getCredentials(): Any? {
        return null
    }

    override fun getPrincipal(): Any? {
        return null
    }

    override fun isAuthenticated(): Boolean {
        return true
    }

    override fun hashCode(): Int {
        return 7
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null) {
            return false
        }
        return javaClass == other.javaClass
    }
}