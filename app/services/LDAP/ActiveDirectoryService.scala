package app.services.ldap

import javax.inject._
import scala.collection.JavaConverters._

import com.unboundid.ldap.sdk._

import app.models.{ LDAPAttribute, ActiveDirectoryUser }
import utils.ClassUtil

@Singleton
class ActiveDirectoryService extends LDAPServiceProvider {

  /**
   * Mapping com.unboundid.ldap.sdk.SearchResultEntry to ActiveDirectoryUser
   *
   * @param com.unboundid.ldap.sdk.SearchResultEntry
   * @return ActiveDirectoryUser
   * TODO: More Abstractly
   */
  def mapActiveDirectoryUser(srEntry: com.unboundid.ldap.sdk.SearchResultEntry): ActiveDirectoryUser = {
    ActiveDirectoryUser(
      LDAPAttribute("ldap.attribute.cn", srEntry.getAttributeValue("cn")),
      LDAPAttribute("ldap.attribute.displayName", srEntry.getAttributeValue("displayName")),
      LDAPAttribute("ldap.attribute.distinguishedName", srEntry.getAttributeValue("distinguishedName")),
      LDAPAttribute("ldap.attribute.name", srEntry.getAttributeValue("name")),
      LDAPAttribute("ldap.attribute.sAMAccountName", srEntry.getAttributeValue("sAMAccountName")),
      LDAPAttribute("ldap.attribute.sn", srEntry.getAttributeValue("sn")),
      LDAPAttribute("ldap.attribute.userPrincipalNames", srEntry.getAttributeValue("userPrincipalNames"))
    )
  }

  /**
   * Get user information by uid.
   * @param Uid for connection user.
   * @param Serach user's uid
   * @return ActiveDirectoryUser
   */
  override def getUser(connectionUser: String, targetUid: String): Option[ActiveDirectoryUser] = {

    getConnectionByUser(connectionUser) match {
      case Some(uc) => {
        val searchResult = {
          uc.connection.search(new SearchRequest(
            baseDN,
            SearchScope.SUB,
            Filter.createEqualityFilter(uidAttributeName, targetUid),
            ClassUtil.getFields[ActiveDirectoryUser]: _*
          )
          ).getSearchEntries
        }
        searchResult.isEmpty match {
          case false => {
            Some(mapActiveDirectoryUser(searchResult.get(0)))
          }
          case true => None
        }
      }
      case None => None
    }
  }

}