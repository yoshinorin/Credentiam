package app.services

import scala.collection.JavaConverters._
import com.unboundid.ldap.sdk._
import app.models.ActiveDirectoryUser
import utils.ClassUtil

object ActiveDirectoryService extends LDAPService {

  /**
   * User bind with ActiveDirectory
   */
  def bind(uid: String, password: String): ResultCode = {
    getDN(uid) match {
      case Some(dn) => {
        createConnectionByUser(uid: String, dn: String, password: String)
        ResultCode.SUCCESS
      }
      case None => ResultCode.OPERATIONS_ERROR
    }
  }

  /**
   * Mapping com.unboundid.ldap.sdk.SearchResultEntry to ActiveDirectoryUser
   *
   * @param com.unboundid.ldap.sdk.SearchResultEntry
   * @return ActiveDirectoryUser
   * TODO: More Abstractly
   */
  def mapActiveDirectoryUser(srEntry: com.unboundid.ldap.sdk.SearchResultEntry): ActiveDirectoryUser = {
    ActiveDirectoryUser(
      srEntry.getAttributeValue("dn"),
      srEntry.getAttributeValue("cn"),
      srEntry.getAttributeValue("displayName"),
      srEntry.getAttributeValue("distinguishedName"),
      srEntry.getAttributeValue("name"),
      srEntry.getAttributeValue("sAMAccountName"),
      srEntry.getAttributeValue("sn"),
      srEntry.getAttributeValue("userPrincipalNames")
    )
  }

  /**
   * Get DN by uid.
   */
  def getDN(uid: String): Option[String] = {
    val searchResult = defaultConnection.search(new SearchRequest(baseDN, SearchScope.SUB, Filter.createEqualityFilter(uidAttributeName, uid))).getSearchEntries

    searchResult.isEmpty match {
      case false => Some(searchResult.get(0).getDN)
      case true => None
    }
  }

  /**
   * Get user information by uid.
   */
  def getUser(uid: String): Option[ActiveDirectoryUser] = {

    getConnectionByUser(uid) match {
      case Some(uc) => {
        val searchResult = {
          uc.connection.search(new SearchRequest(
            baseDN,
            SearchScope.SUB,
            Filter.createEqualityFilter(uidAttributeName, uid),
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