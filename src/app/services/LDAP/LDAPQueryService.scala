package app.services.ldap

import app.utils.config.LDAPSearchableAttributes
import app.utils.types.SearchRelations

object LDAPQueryService {

  def relationMatcha(relation: String): SearchRelations = {
    relation.toUpperCase match {
      case "ANY" => SearchRelations.ANY
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
