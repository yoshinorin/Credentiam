package app.services.ldap

import scala.collection.JavaConverters._
import scala.collection.mutable

import com.typesafe.config.ConfigFactory
import com.unboundid.ldap.sdk._

import app.models.{ LDAPAttribute, OrganizationUnit, ActiveDirectoryUser }
import utils.ClassUtil

object LDAPServiceProvider {

  val configuration = ConfigFactory.load
  val isActiveDirectory = configuration.getBoolean("ldap.isActiveDirectory")

  val server: LDAPServiceProvider = {
    if (isActiveDirectory) {
      new ActiveDirectoryService()
    } else {
      //TODO: OpenLDAP support
      new ActiveDirectoryService
    }
  }

}

trait LDAPServiceProvider extends LDAPConnectionProvider {

  /**
   * User bind with LDAP server.
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
    val searchResult = {
      defaultConnection.search(new SearchRequest(
        baseDN, SearchScope.SUB,
        Filter.createEqualityFilter(uidAttributeName, uid)
      )
      ).getSearchEntries
    }
    searchResult.isEmpty match {
      case false => Some(searchResult.get(0).getDN)
      case true => None
    }
  }

  /**
   * Mapping com.unboundid.ldap.sdk.SearchResultEntry to OrganizationUnit
   *
   * @param List[com.unboundid.ldap.sdk.SearchResultEntry]
   * @return List[OrganizationUnit]
   * TODO: More Abstractly
   */
  def mapOrganizationUnit(srEntry: List[com.unboundid.ldap.sdk.SearchResultEntry]): List[OrganizationUnit] = {
    var ou = mutable.ListBuffer.empty[OrganizationUnit]
    srEntry.foreach(v =>
      ou += OrganizationUnit(
        LDAPAttribute("ldap.attribute.distinguishedName", v.getAttributeValue("distinguishedName")),
        LDAPAttribute("ldap.attribute.name", v.getAttributeValue("name")),
        LDAPAttribute("ldap.attribute.ou", v.getAttributeValue("ou"))
      )
    )
    ou.toList
  }

  /**
   * Get Organizations
   */
  def getOrganizations(connectionUser: String): Option[List[app.models.OrganizationUnit]] = {
    getConnectionByUser(connectionUser) match {
      case Some(uc) => {
        val searchResult = {
          uc.connection.search(new SearchRequest(
            baseDN,
            SearchScope.SUB,
            "(ou=*)",
            ClassUtil.getFields[OrganizationUnit]: _*
          )
          ).getSearchEntries
        }
        searchResult.isEmpty match {
          case false => {
            Some(mapOrganizationUnit(searchResult.asScala.toList))
          }
          case true => None
        }
      }
      case None => None
    }
  }

  /**
   * Get user information by uid.
   * @param Uid for connection user.
   * @param Serach user's uid
   * @return ActiveDirectoryUser
   */
  def getUser(connectionUser: String, targetUid: String): Option[ActiveDirectoryUser]

}