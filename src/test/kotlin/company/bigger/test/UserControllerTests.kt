package company.bigger.test

import company.bigger.dto.UserLoginModel
import company.bigger.test.support.BaseTest
import company.bigger.test.support.randomString
import company.bigger.web.controller.SessionController
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class UserControllerTests : BaseTest() {
    @Test
    fun `GardenUser can login (service)`() {
        val result = userService.login(UserLoginModel("GardenUser", "GardenUser"))
        assertNotNull(result?.token)
    }
    @Test
    fun `GardenUser can not login with wrong password (service)`() {
        val result = userService.login(UserLoginModel("GardenUser", randomString(20)))
        assertNull(result?.token)
    }

    @Autowired
    private lateinit var sessionController: SessionController

    @Test
    fun `GardenUser can login (controller)`() {
        val result = sessionController.login("GardenUser", "GardenUser")
        assertNotNull(result?.token)
    }
}