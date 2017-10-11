package app.services.ldap

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.reflect._
import scala.reflect.runtime.universe._

import com.typesafe.config.ConfigFactory
import com.unboundid.ldap.sdk._
import app.models.{ ActiveDirectoryUser, Computer, LDAPAttribute, OrganizationUnit }
import utils.ClassUtil
import utils.types.UserId

object LDAPService {

  val configuration = ConfigFactory.load
  val isActiveDirectory = configuration.getBoolean("ldap.isActiveDirectory")

  val server = {
    if (isActiveDirectory) {
      new ActiveDirectoryService
    } else {
      //TODO: OpenLDAP support
      new ActiveDirectoryService
    }
  }

}

trait LDAPService[T] extends LDAPConnectionProvider {

  val configuration = ConfigFactory.load
  val administratorDN = configuration.getString("ldap.administratorDN")

  /**
   * Check the user is ldap server's administrator or not.
   *
   * @param uid
   * @return Boolean
   */
  def isAdmin(uid: UserId): Boolean = {
    getDN(uid) match {
      case Some(dn) => if (dn == administratorDN) true else false
      case None => false
    }
  }

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
  protected def search(connectionUser: UserId, filter: com.unboundid.ldap.sdk.Filter, attributes: Seq[String]): Option[Seq[com.unboundid.ldap.sdk.SearchResultEntry]] = {
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
   * Map SearchResultEntries to specify LDAP classes.
   *
   * @param SearchResultEntries
   * @return Specify LDAP classes.
   */
  protected def mapSearchResultEntryToLdapClass[T](sr: Seq[com.unboundid.ldap.sdk.SearchResultEntry])(implicit cTag: ClassTag[T]): Seq[T] = {
    var t = mutable.ListBuffer.empty[T]
    sr.foreach(v =>
      t += cTag.runtimeClass.getConstructor(classOf[com.unboundid.ldap.sdk.SearchResultEntry]).newInstance(v).asInstanceOf[T]
    )
    t.toSeq
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
   * Get specific mapped LDAP object class.
   *
   * @param connectionUser The current user id.
   * @return Specify LDAP object class or none.
   */
  def getByDN[T](connectionUser: UserId, dn: String)(implicit tTag: TypeTag[T], cTag: ClassTag[T]): Option[T] = {
    search(connectionUser, Filter.createEqualityFilter("distinguishedName", dn), ClassUtil.getLDAPAttributeFields[T]) match {
      case Some(sr) => Some(mapSearchResultEntryToLdapClass[T](sr).head)
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
    search(connectionUser, Filter.create("(ou=*)"), ClassUtil.getLDAPAttributeFields[OrganizationUnit]) match {
      case Some(sr) => Some(mapSearchResultEntryToLdapClass[OrganizationUnit](sr))
      case None => None
    }
  }

  /**
   * Get Computer.
   *
   * @param connectionUser The current user id.
   * @param dn The current user id.
   * @return Computer.
   */
  def getComputer(connectionUser: UserId, dn: String): Option[app.models.Computer] = {
    search(connectionUser, Filter.createEqualityFilter("distinguishedName", dn), ClassUtil.getLDAPAttributeFields[Computer]) match {
      case Some(sr) => Some(mapSearchResultEntryToLdapClass[Computer](sr).head)
      case None => None
    }
  }

  /**
   * Get Computers.
   *
   * @param connectionUser The current user id.
   * @return Computers.
   */
  def getComputers(connectionUser: UserId): Option[Seq[app.models.Computer]] = {
    search(connectionUser, Filter.create("objectCategory=computer"), ClassUtil.getLDAPAttributeFields[Computer]) match {
      case Some(sr) => Some(mapSearchResultEntryToLdapClass[Computer](sr))
      case None => None
    }
  }

  /**
   * Get user information by uid.
   *
   * @param connectionUser The current user id.
   * @param targetUid The target user's uid.
   * @return T
   */
  def getUser(connectionUser: UserId, targetUid: String): Option[T]

}