package app.services.ldap

import scala.collection.mutable
import scala.collection.JavaConverters._

import com.typesafe.config.ConfigFactory
import com.unboundid.ldap.sdk._

import app.models.{ LDAPAttribute, ActiveDirectoryUser }

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

  //TODO: Create connection options

  case class UserConnection(
    dn: String,
    connection: LDAPConnection
  //TODO: Add user's role.
  )

  /**
   * Create connection using by config user.
   * TODO: Support LDAPS
   */
  protected val defaultConnection = new LDAPConnection(host, port, bindDN, password)

  /**
   * The connections store by user.
   */
  protected val connections: mutable.HashMap[String, UserConnection] = mutable.HashMap()

  /**
   * Create connection by users and store it.
   * TODO: Support LDAPS
   */
  def createConnectionByUser(uid: String, dn: String, password: String): Unit = {
    val connection = UserConnection(dn, new LDAPConnection(host, port, dn, password))
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
   * Get Organizations
   */
  def getOrganizations(connectionUser: String): Option[List[com.unboundid.ldap.sdk.SearchResultEntry]] = {
    getConnectionByUser(connectionUser) match {
      case Some(uc) => {
        val searchResult = {
          uc.connection.search(new SearchRequest(
            baseDN,
            SearchScope.SUB,
            "(ou=*)",
            "name"
          )
          ).getSearchEntries
        }
        searchResult.isEmpty match {
          case false => {
            Some(searchResult.asScala.toList)
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