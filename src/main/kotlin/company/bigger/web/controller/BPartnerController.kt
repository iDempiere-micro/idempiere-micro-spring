package company.bigger.web.controller

import company.bigger.dto.CreateCustomerModel
import company.bigger.service.BPartnerService
import company.bigger.web.jwt.SecuredApi
import org.compiere.bo.CustomerProcessBaseResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class BPartnerController {
    @Autowired
    private lateinit var securedApi: SecuredApi

    @Autowired
    private lateinit var bpartnerService: BPartnerService

    @PutMapping()
    @RequestMapping(value = ["/bpartners"])
    fun all(
        @RequestBody createCustomerModel: CreateCustomerModel
    ): CustomerProcessBaseResult? {
        return bpartnerService.createBPartner(createCustomerModel)
    }
}