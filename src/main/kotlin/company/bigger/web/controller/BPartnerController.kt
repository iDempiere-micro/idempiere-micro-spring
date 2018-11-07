package company.bigger.web.controller

import company.bigger.dto.CreateCustomerModel
import company.bigger.service.BPartnerService
import company.bigger.web.jwt.SecuredApi
import org.compiere.bo.CustomerProcessBaseResult
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class BPartnerController(
    private val securedApi: SecuredApi,
    private val bpartnerService: BPartnerService
) {
    @PutMapping()
    @RequestMapping(value = ["/bpartners"])
    fun all(
        @RequestBody createCustomerModel: CreateCustomerModel
    ): CustomerProcessBaseResult? {
        return bpartnerService.createBPartner(createCustomerModel)
    }
}