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
   *
   * @param uid The user id for bind LDAP server.
   * @param uid The user password.
   * @return LDAP result code.
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
   * @param attributes Attributes to be acquired.
   * @return SearchResultEntries
   */
  def search(connectionUser: UserId, filter: com.unboundid.ldap.sdk.Filter, attributes: Array[String]): Option[Seq[com.unboundid.ldap.sdk.SearchResultEntry]] = {
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
   *
   * @param connectionUser The current user id.
   */
  def getDN(uid: UserId): Option[String] = {
    val searchResult = {
      defaultConnection.search(new SearchRequest(
        baseDN,
        SearchScope.SUB,
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
   * Mapping SearchResultEntries to OrganizationUnits
   *
   * @param SearchResultEntries
   * @return OrganizationUnits.
   * TODO: More Abstractly
   */
  def mapOrganizationUnit(sr: Seq[com.unboundid.ldap.sdk.SearchResultEntry]): Seq[OrganizationUnit] = {
    var ous = mutable.ListBuffer.empty[OrganizationUnit]
    sr.foreach(v =>
      ous += OrganizationUnit(
        LDAPAttribute.store("ldap.attribute.description", v.getAttributeValue("description")),
        LDAPAttribute.store("ldap.attribute.distinguishedName", v.getAttributeValue("distinguishedName")),
        LDAPAttribute.store("ldap.attribute.l", v.getAttributeValue("l")),
        LDAPAttribute.store("ldap.attribute.name", v.getAttributeValue("name")),
        LDAPAttribute.store("ldap.attribute.ou", v.getAttributeValue("ou")),
        LDAPAttribute.store("ldap.attribute.postalCode", v.getAttributeValue("postalCode")),
        LDAPAttribute.store("ldap.attribute.st", v.getAttributeValue("st")),
        LDAPAttribute.store("ldap.attribute.street", v.getAttributeValue("street")),
        LDAPAttribute.store("ldap.attribute.whenChanged", v.getAttributeValue("whenChanged")),
        LDAPAttribute.store("ldap.attribute.whenCreated", v.getAttributeValue("whenCreated"))
      )
    )
    ous.toSeq
  }

  /**
   * Get Organization
   *
   * @param connectionUser The current user id.
   * @param dn The current user id.
   * @return OrganizationUnits.
   */
  def getOrganization(connectionUser: UserId, dn: String): Option[app.models.OrganizationUnit] = {
    search(connectionUser, Filter.createEqualityFilter("distinguishedName", dn), ClassUtil.getFields[OrganizationUnit]) match {
      case Some(sr) => Some(mapOrganizationUnit(sr).head)
      case None => None
    }
  }

  /**
   * Get Organizations
   *
   * @param connectionUser The current user id.
   * @return OrganizationUnits.
   */
  def getOrganizations(connectionUser: UserId): Option[Seq[app.models.OrganizationUnit]] = {
    search(connectionUser, Filter.create("(ou=*)"), ClassUtil.getFields[OrganizationUnit]) match {
      case Some(sr) => Some(mapOrganizationUnit(sr))
      case None => None
    }
  }

  /**
   * Get user information by uid.
   *
   * @param connectionUser The current user id.
   * @param targetUid The target user's uid.
   * @return ActiveDirectoryUser //TODO: Change abstractly.
   */
  def getUser(connectionUser: UserId, targetUid: String): Option[ActiveDirectoryUser]

}