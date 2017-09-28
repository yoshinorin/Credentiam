package app.services.ldap

import scala.collection.JavaConverters._
import scala.collection.mutable

import com.typesafe.config.ConfigFactory
import com.unboundid.ldap.sdk._

import app.models.{ LDAPAttribute, OrganizationUnit, ActiveDirectoryUser }
import utils.ClassUtil
import utils.types.UserId

object LDAPService {

  val configuration = ConfigFactory.load
  val isActiveDirectory = configuration.getBoolean("ldap.isActiveDirectory")

  val server: LDAPService = {
    if (isActiveDirectory) {
      new ActiveDirectoryService()
    } else {
      //TODO: OpenLDAP support
      new ActiveDirectoryService
    }
  }

}

trait LDAPService extends LDAPConnectionProvider {

  /**
   * User bind with LDAP server.
   */
  def bind(uid: UserId, password: String): ResultCode = {
    getDN(uid) match {
      case Some(dn) => {
        createConnectionByUser(uid: UserId, dn: String, password: String)
        ResultCode.SUCCESS
      }
      case None => ResultCode.OPERATIONS_ERROR
    }
  }

  /**
   * Search LDAP Object using by current user's connection & filter condition.
   *
   * @param connectionUser The current user id.
   * @param filter Filter condition.
   * @param attributes Get attributes.
   * @return Option[Seq[com.unboundid.ldap.sdk.SearchResultEntry]]
   */
  def search(connectionUser: UserId, filter: String, attributes: Array[String]): Option[Seq[com.unboundid.ldap.sdk.SearchResultEntry]] = {
    getConnectionByUser(connectionUser) match {
      case Some(uc) => {
        val searchResult = {
          uc.connection.search(new SearchRequest(
            baseDN,
            SearchScope.SUB,
            filter,
            attributes: _*
          )
          ).getSearchEntries
        }
        searchResult.isEmpty match {
          case false => {
            Some(searchResult.asScala.toSeq)
          }
          case true => None
        }
      }
      case None => None
    }
  }

  /**
   * Get DN by uid.
   */
  def getDN(uid: UserId): Option[String] = {
    val searchResult = {
      defaultConnection.search(new SearchRequest(
        baseDN, SearchScope.SUB,
        Filter.createEqualityFilter(uidAttributeName, uid.value.toString)
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
   * @param Seq[com.unboundid.ldap.sdk.SearchResultEntry]
   * @return Seq[OrganizationUnit]
   * TODO: More Abstractly
   */
  def mapOrganizationUnit(srEntry: Seq[com.unboundid.ldap.sdk.SearchResultEntry]): Seq[OrganizationUnit] = {
    var ou = mutable.ListBuffer.empty[OrganizationUnit]
    srEntry.foreach(v =>
      ou += OrganizationUnit(
        LDAPAttribute("ldap.attribute.distinguishedName", v.getAttributeValue("distinguishedName")),
        LDAPAttribute("ldap.attribute.name", v.getAttributeValue("name")),
        LDAPAttribute("ldap.attribute.ou", v.getAttributeValue("ou"))
      )
    )
    ou.toSeq
  }

  /**
   * Get Organizations
   *
   * @param connectionUser The current user id.
   * @return Option[Seq[app.models.OrganizationUnit]]
   */
  def getOrganizations(connectionUser: UserId): Option[Seq[app.models.OrganizationUnit]] = {
    search(connectionUser, "(ou=*)", ClassUtil.getFields[OrganizationUnit]) match {
      case Some(sr) => Some(mapOrganizationUnit(sr))
      case None => None
    }
  }

  /**
   * Get user information by uid.
   * @param Uid for connection user.
   * @param Serach user's uid
   * @return ActiveDirectoryUser
   */
  def getUser(connectionUser: UserId, targetUid: String): Option[ActiveDirectoryUser]

}