package app.services

import com.unboundid.ldap.sdk._

object ActiveDirectoryService extends LDAPService {

  def bind(uid: String, password: String): Int = {
    getDN(uid) match {
      case Some(dn) => connectionPool.bindAndRevertAuthentication(new SimpleBindRequest(dn, password)).getResultCode.intValue
      case None => 1
    }
  }

  def getDN(uid: String): Option[String] = {
    val searchResult = connection.search(new SearchRequest(baseDN, SearchScope.SUB, Filter.createEqualityFilter(uidAttributeName, uid))).getSearchEntries

    searchResult.size match {
      case _ => Some(searchResult.get(0).getDN)
      case 0 => None
    }
  }

}