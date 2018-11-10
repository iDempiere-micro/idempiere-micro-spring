package company.bigger.web.resolver

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import org.compiere.crm.MCrmCategory
import org.idempiere.common.util.Env
import org.springframework.stereotype.Component

/**
 * GraphQL Mutation. Put all your mutations here. Use the services from [BaseResolver]
 */
@Component
class MutationResolver : BaseResolver(), GraphQLMutationResolver {
    fun createCategory(name: String, value: String): MCrmCategory {
        val result = MCrmCategory(Env.getCtx(), 0, null)
        result.setName(name)
        result.setValue(value)
        result.save()
        return result
    }
}