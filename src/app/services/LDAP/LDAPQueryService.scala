package app.services.ldap

import app.utils.config.LDAPSearchableAttributes
import app.utils.types.SearchRelations

object LDAPQueryService {

  /**
   * Get [[SearchRelations]] type from string.
   *
   * @param relation Name of relation.
   * @return [[SearchRelations]]
   */
  def getRelationTypeFromString(relation: String): SearchRelations = {
    relation.toUpperCase match {
      case "CONTAINS" => SearchRelations.CONTAINS
      case "EXCLUDES" => SearchRelations.EXCLUDES
      case "STARTSWITH" => SearchRelations.STARTSWITH
      case "ENDSWITH" => SearchRelations.ENDSWITH
      case "EQUAL" => SearchRelations.EQUAL
      case "NOTEQUAL" => SearchRelations.NOTEQUAL
      case _ => SearchRelations.ANY
    }
  }

}
