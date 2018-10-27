package company.bigger.web.controller

import company.bigger.service.CategoryService
import company.bigger.web.jwt.SecuredApi
import org.compiere.crm.MCrmCategory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CategoryController {
    @Autowired
    private lateinit var securedApi: SecuredApi

    @Autowired
    private lateinit var categoryService: CategoryService

    @GetMapping()
    @RequestMapping(value = ["/categories"])
    fun all(
        @RequestHeader(value = "Authorization") authorization: String
    ): ResponseEntity<List<MCrmCategory>?> {
        return securedApi.processAuthorization(authorization) { categoryService.getAllCategories() }
    }
}