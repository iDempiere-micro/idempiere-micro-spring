package company.bigger.web.resolver

import org.springframework.stereotype.Component

@Component
class QueryResolver : BaseResolver() {
    val version = "1.0.0"
    val me get() = userService?.currentUser()
}
