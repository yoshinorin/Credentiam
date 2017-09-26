package app.services.ldap

import scala.collection.mutable
import scala.collection.JavaConverters._

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

trait LDAPServiceProvider {

  val configuration = ConfigFactory.load
  val host = configuration.getString("ldap.host")
  val port = configuration.getInt("ldap.port")
  val ldaps = configuration.getBoolean("ldap.ldaps")
  val baseDN = configuration.getString("ldap.baseDN")
  val bindDN = configuration.getString("ldap.bindDN")
  val password = configuration.getString("ldap.password")
  val uidAttributeName = configuration.getString("ldap.uidAttributeName")
  val initialConnextions = configuration.getInt("ldap.initialConnextions")
  val maxConnections = configuration.getInt("ldap.maxConnections")

  val connectionOptions = new LDAPConnectionOptions
  connectionOptions.setConnectTimeoutMillis(configuration.getInt("ldap.connectTimeout"))
  connectionOptions.setResponseTimeoutMillis(configuration.getInt("ldap.responseTimeout"))
  connectionOptions.setAbandonOnTimeout(configuration.getBoolean("ldap.abandonOnTimeOut"))

  case class UserConnection(
    dn: String,
    connection: LDAPConnection
  //TODO: Add user's role.
  )

  /**
   * Create connection using by config user.
   * TODO: Support LDAPS
   */
  protected val defaultConnection = new LDAPConnection(connectionOptions, host, port, bindDN, password)

  /**
   * The connections store by user.
   */
  protected val connections: mutable.HashMap[String, UserConnection] = mutable.HashMap()

  /**
   * Create connection by users and store it.
   * TODO: Support LDAPS
   */
  def createConnectionByUser(uid: String, dn: String, password: String): Unit = {
    val connection = UserConnection(dn, new LDAPConnection(connectionOptions, host, port, dn, password))
    connections += (uid -> connection)
  }

  /**
   * Remove & close connection from connections store by User.
   */
  def removeConnectionByUser(uid: String): Unit = {
    //TODO: Release connection certainly.
    connections.get(uid) match {
      case Some(uc) => {
        uc.connection.close
        connections -= uid
      }
      case None => None
    }
  }

  /**
   * Get Connection by uid.
   */
  def getConnectionByUser(uid: String): Option[UserConnection] = {
    connections.get(uid)
  }

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