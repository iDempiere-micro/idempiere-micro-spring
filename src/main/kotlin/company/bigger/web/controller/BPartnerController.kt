package company.bigger.web.controller

import company.bigger.dto.CreateCustomerModel
import company.bigger.service.BPartnerService
import company.bigger.web.jwt.SecuredApi
import org.compiere.crm.DetailResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class BPartnerController {
    @Autowired
    private lateinit var securedApi: SecuredApi

    @Autowired
    private lateinit var bpartnerService: BPartnerService

    @PutMapping()
    @RequestMapping(value = ["/bpartners"])
    fun all(
            @RequestHeader(value = "Authorization") authorization: String,
            @RequestBody createCustomerModel: CreateCustomerModel): ResponseEntity<DetailResult?> {
        return securedApi.processAuthorization(authorization) { bpartnerService.createBPartner(createCustomerModel) }
    }
}