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

  object CONTAINS extends SearchRelations("contains")

  object EXCLUDES extends SearchRelations("excludes")

  object STARTSWITH extends SearchRelations("startswith")

  object ENDSWITH extends SearchRelations("endswith")

  object EQUAL extends SearchRelations("equal")

  object NOTEQUAL extends SearchRelations("notequal")

}

/**
 * LDAP object types abstract class.
 */
sealed abstract class LDAPObjectType(val name: String)

/**
 * LDAP object types abstract class.
 */
object LDAPObjectType {

  object ORGANIZATION extends LDAPObjectType("Organization")

  object USER extends LDAPObjectType("User")

  object COMPUTER extends LDAPObjectType("Computer")

}
