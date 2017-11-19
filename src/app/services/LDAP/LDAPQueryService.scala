package app.services.ldap

import scala.collection.JavaConverters._
import com.unboundid.ldap.sdk.Filter
import app.utils.config.LDAPSearchableAttributes
import app.utils.types.{ LDAPObjectType, SearchRelations }

object LDAPQueryService {

  /**
   * Build [[com.unboundid.ldap.sdk.Filter]] using by arguments.
   *
   * @param objectType [[LDAPObjectType]]
   * @param relation [[SearchRelations]]
   * @param word Search word.
   * @return Built filter.
   */
  def filterBuilder(objectType: LDAPObjectType, relation: SearchRelations, word: String): com.unboundid.ldap.sdk.Filter = {
    val filters = for (attr <- getSearchableAttributes(objectType)) yield {
      createFilter(attr, relation, word)
    }

    Filter.createANDFilter(createFilterByLDAPObjectType(objectType), Filter.createORFilter(filters.asJava))
  }

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
   * Create [[com.unboundid.ldap.sdk.Filter]] by LDAP attribute.
   *
   * @param attributeName LDAP attribute name.
   * @param relation [[SearchRelations]]
   * @param word Search word.
   * @return Query filter.
   */
  private def createFilter(attributeName: String, relation: SearchRelations, word: String): com.unboundid.ldap.sdk.Filter = {
    relation match {
      case SearchRelations.CONTAINS => Filter.create(s"(${attributeName}=*${word}*)")
      case SearchRelations.EXCLUDES => Filter.create(s"(!(${attributeName}=*${word}*))")
      case SearchRelations.STARTSWITH => Filter.create(s"(${attributeName}=${word}*)")
      case SearchRelations.ENDSWITH => Filter.create(s"(${attributeName}=*${word})")
      case SearchRelations.EQUAL => Filter.createEqualityFilter(attributeName, word)
      case SearchRelations.NOTEQUAL => Filter.create(s"(!(${attributeName}=${word}))")
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
