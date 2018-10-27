package company.bigger.test.clients

import company.bigger.test.clients.response.CreateCustomerModelResponse
import feign.Headers
import feign.Param
import feign.RequestLine

@Headers("Content-Type: application/json",
        "Authorization: Token {token}")
interface CategoryClient {
    @RequestLine("GET /categories")
    fun create(@Param("token") token: String): CreateCustomerModelResponse
}