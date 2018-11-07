package company.bigger.web.resolver

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import company.bigger.Micro

/**
 * Helper class to allow to write QueryResolvers. Uses the [Micro.instance] singleton to get to the services.
 * Note we cannot use Spring autowiring here as the class is enhanced by the GraphQL library.
 * Also note the class is accessed on startup before all the wiring is done that is why we support asking with question mark (?)
 * to allow null values.
 */
abstract class BaseResolver : GraphQLQueryResolver {
    protected val userService get() = Micro.instance?.userService
    protected val categoryService get() = Micro.instance?.categoryService
    protected val countryService get() = Micro.instance?.countryService
}