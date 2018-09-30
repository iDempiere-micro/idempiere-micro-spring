package company.bigger.test

import company.bigger.dto.UserLoginModel
import company.bigger.test.support.BaseTest
import company.bigger.web.controller.UserController
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertNotNull

class UserControllerTests : BaseTest() {
    @Test
    fun `GardenUser can login (service)`() {
        val result = userService.login(UserLoginModel("GardenUser", "GardenUser"))
        assertNotNull(result?.token)
    }

    @Autowired
    private lateinit var userController: UserController

    @Test
    fun `GardenUser can login (controller)`() {
        val result = userController.login("GardenUser", "GardenUser")
        assertNotNull(result?.token)
    }
}