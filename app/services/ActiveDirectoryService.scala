package app.services

import scala.collection.JavaConverters._
import com.unboundid.ldap.sdk._
import app.models.ActiveDirectoryUser

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
   * Get DN by uid.
   */
  def getDN(uid: String): Option[String] = {
    val searchResult = defaultConnection.search(new SearchRequest(baseDN, SearchScope.SUB, Filter.createEqualityFilter(uidAttributeName, uid))).getSearchEntries

    searchResult.size match {
      case _ => Some(searchResult.get(0).getDN)
      case 0 => None
    }
  }

  /**
   * Get user information by uid.
   */
  def getUser(uid: String): Option[List[com.unboundid.ldap.sdk.Attribute]] = {
    getConnectionByUser(uid) match {
      case Some(uc) => {
        val searchResult = {
          uc.connection.search(
            baseDN,
            SearchScope.SUB,
            Filter.createEqualityFilter(uidAttributeName, uid)
          ).getSearchEntries
        }
        searchResult.size match {
          case _ => {
            Some(searchResult.get(0).getAttributes.asScala.toList)
          }
          case 0 => None
        }
      }
      case None => None
    }
  }

}