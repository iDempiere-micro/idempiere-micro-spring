package company.bigger.web.resolver

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import company.bigger.Micro

abstract class BaseResolver : GraphQLQueryResolver {
    protected val userService get() = Micro.instance?.userService
    protected val categoryService get() = Micro.instance?.categoryService
    protected val countryService get() = Micro.instance?.countryService
}