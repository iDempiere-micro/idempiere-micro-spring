package software.hsharp.business.services

import software.hsharp.business.models.IBusinessPartner
import software.hsharp.core.models.IResult

// TODO: remove and replace with GraphQL
@Deprecated("Replace with GraphQL")
interface IBusinessPartnerResult : IResult {
    val businessPartner: IBusinessPartner?
}