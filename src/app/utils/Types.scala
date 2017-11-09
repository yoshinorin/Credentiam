package app.utils.types

/**
 * Type for user's id.
 * @param userId
 */
case class UserId(val value: String) extends AnyVal

/**
 * Search relations abstract class.
 */
sealed abstract class SearchRelations(val name: String)

/**
 * Search relations for search option.
 */
object SearchRelations {

  object ANY extends SearchRelations("any")

  object CONTAINS extends SearchRelations("contains")

  object EXCLUDES extends SearchRelations("excludes")

  object STARTSWITH extends SearchRelations("startswith")

  object ENDSWITH extends SearchRelations("endswith")

  object EQUAL extends SearchRelations("equal")

  object NOTEQUAL extends SearchRelations("notequal")

}
