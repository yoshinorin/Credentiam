package app.services.ldap

import app.utils.config.LDAPSearchableAttributes
import app.utils.types.{ LDAPObjectType, SearchRelations }

object LDAPQueryService {


  /**
   * Create equality filter of [[com.unboundid.ldap.sdk.Filter]]
   *
   * @param objectType [[LDAPObjectType]]
   * @return [[com.unboundid.ldap.sdk.Filter]] equality filter of each LDAP object types.
   */
  private def createFilterByLDAPObjectType(objectType: LDAPObjectType): com.unboundid.ldap.sdk.Filter = {
    objectType match {
      case LDAPObjectType.ORGANIZATION => Filter.createEqualityFilter("objectClass", "organizationalUnit")
      case LDAPObjectType.USER => Filter.createEqualityFilter("objectClass", "user")
      //TODO: objectCategory attributes is only for ActiveDirectory
      case LDAPObjectType.COMPUTER => Filter.createEqualityFilter("objectCategory", "computer")
    }
  }

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
