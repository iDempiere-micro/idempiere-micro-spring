package company.bigger.service

import company.bigger.dto.UserLoginModel
import company.bigger.dto.UserLoginModelResponse
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.compiere.crm.MUser
import org.compiere.model.I_AD_User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.validation.Errors
import java.util.*

@Service
class UserService(
        val loginService: LoginService,
        @Value("\${jwt.secret}") val jwtSecret: String,
        @Value("\${jwt.issuer}")val jwtIssuer: String
) {
    companion object {
        // user login result by UserName
        private val users = mutableMapOf<String, UserLoginModelResponse>()
    }

    fun findByToken(token: String) = users.values.firstOrNull { it.token == token }
    fun clearCurrentUser() {
        loginService.clearCurrentUser()
    }

    fun setCurrentUser(user: UserLoginModelResponse?): UserLoginModelResponse? {
        loginService.setCurrentUser(user)
        return user
    }

    private fun newToken(user: UserLoginModelResponse): String {
        return Jwts.builder()
                .setIssuedAt(Date())
                .setSubject(user.loginName)
                .setIssuer(jwtIssuer)
                .setExpiration(Date(System.currentTimeMillis() + 10 * 24 * 60 * 60 * 1000)) // 10 days
                .signWith(SignatureAlgorithm.HS256, jwtSecret).compact()
    }

    fun validToken(token: String, user: UserLoginModelResponse?): Boolean {
        val claims = Jwts.parser().setSigningKey(jwtSecret)
                .parseClaimsJws(token).body
        return claims.subject == user?.loginName && claims.issuer == jwtIssuer
                && Date().before(claims.expiration)
    }

    //@CachePut(cacheNames=arrayOf("usersByToken"), key="#user.token")
    fun updateToken(user: UserLoginModelResponse): UserLoginModelResponse{
        val result =user.copy(token=newToken(user))
        users[result.loginName] = result
        return result
    }

    fun login(login: UserLoginModel): UserLoginModelResponse? {
        val user = loginService.login(login)
        if (user.logged) return updateToken(user)
        return user
    }

    fun currentUser(): I_AD_User {
        return loginService.currentUser()
    }

}