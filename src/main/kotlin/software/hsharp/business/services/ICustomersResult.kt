package software.hsharp.business.services

import software.hsharp.business.models.ICustomer
import software.hsharp.core.models.IResult

// TODO: remove and replace with GraphQL
@Deprecated("Replace with GraphQL")
interface ICustomersResult : IResult {
    val customers: Array<ICustomer>
}