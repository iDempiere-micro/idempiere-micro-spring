package company.bigger.test.clients

import company.bigger.test.clients.response.User
import feign.Headers
import feign.Param
import feign.RequestLine

@Headers("Content-Type: application/json",
        "Authorization: Token {token}")
interface UserClient {
    @RequestLine("GET /user/me")
    fun profile(@Param("token") token: String): User?
}
