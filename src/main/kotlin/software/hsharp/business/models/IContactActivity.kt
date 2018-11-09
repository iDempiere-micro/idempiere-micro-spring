package software.hsharp.business.models

// TODO: remove and replace with GraphQL
@Deprecated("Replace with GraphQL")
interface IContactActivity : IDatabaseEntity {
    val start: Long // represent the value of java.sql.Timestamp.time
    val bpartnerName: String
    val completed: Boolean
    val activityOwnerName: String
}