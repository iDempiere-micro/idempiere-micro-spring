package company.bigger.web.controller

import company.bigger.Micro

open class BaseController {
    protected val userService get() = Micro.instance.userService!!
}