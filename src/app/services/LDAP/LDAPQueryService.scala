package app.services.ldap

import app.utils.config.LDAPSearchableAttributes
import app.utils.types.{ LDAPObjectType, SearchRelations }

object LDAPQueryService {


  /**
   * Get searchable ldap attributes by [[LDAPObjectType]].
   *
   * @param objectType [[LDAPObjectType]]
   * @return Attributes name.
   */
  private def getSearchableAttributes(objectType: LDAPObjectType): List[String] = {
    objectType match {
      case LDAPObjectType.ORGANIZATION => LDAPSearchableAttributes.organization
      case LDAPObjectType.USER => LDAPSearchableAttributes.user
      case LDAPObjectType.COMPUTER => LDAPSearchableAttributes.computer
    }
  }

}
