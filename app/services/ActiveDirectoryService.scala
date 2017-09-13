package app.services

import com.unboundid.ldap.sdk._

object ActiveDirectoryService extends LDAPService {

  /**
   * User bind with ActiveDirectory
   */
  def bind(uid: String, password: String): Int = {
    getDN(uid) match {
      case Some(dn) => {
        createConnectionByUser(uid: String, dn: String, password: String)
        0
      }
      case None => 1
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

}