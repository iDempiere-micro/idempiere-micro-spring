package company.bigger.test.clients

import feign.Headers
import feign.Param
import feign.RequestLine
import org.compiere.model.I_AD_User

@Headers("Content-Type: application/json",
        "Authorization: Token {token}")
interface UserClient {
    @RequestLine("GET /user/me")
    fun profile(@Param("token") token: String): I_AD_User?
}
