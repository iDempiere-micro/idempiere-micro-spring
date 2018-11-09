package software.hsharp.business.models

/**
 * This interface indicates that the object is ready to be JSON-serialized.
 * It means mainly that all the properties are not lazy-bound.
 * We were running into a lot of leaked transactions when trying to serialize directly the ORM objects.
 */
// TODO: remove and replace with GraphQL
@Deprecated("Replace with GraphQL")
interface IDTOReady : java.io.Serializable