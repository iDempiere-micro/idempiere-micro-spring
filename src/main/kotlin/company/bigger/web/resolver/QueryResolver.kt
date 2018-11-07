package company.bigger.web.resolver

import company.bigger.util.Version
import org.springframework.stereotype.Component

/**
 * GraphQL Query. Put all your queries here. Use the services from [BaseResolver]
 */
@Component
class QueryResolver : BaseResolver() {
    companion object {
        const val VER = "1.0.0"
    }

    val version = Version(VER)
    val me get() = userService?.currentUser()
    val users get() = userService?.getUsers()
    val categories get() = categoryService?.getAllCategories()
    val countries get() = countryService?.getAllCountries()
}
