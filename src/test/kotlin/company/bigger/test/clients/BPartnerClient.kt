package company.bigger.test.clients

import company.bigger.dto.CreateCustomerModel
import company.bigger.test.clients.response.CreateCustomerModelResponse
import feign.Headers
import feign.Param
import feign.RequestLine

@Headers("Content-Type: application/json",
        "Authorization: Token {token}")
interface BPartnerClient {
    @RequestLine("PUT /bpartners")
    fun create(@Param("token") token: String, createCustomerModel: CreateCustomerModel): CreateCustomerModelResponse
}