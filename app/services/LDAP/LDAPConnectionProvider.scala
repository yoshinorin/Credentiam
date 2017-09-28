package app.services.ldap

import scala.collection.mutable

import com.typesafe.config.ConfigFactory
import com.unboundid.ldap.sdk._

import utils.types.UserId

trait LDAPConnectionProvider {

  private val configuration = ConfigFactory.load
  private val host = configuration.getString("ldap.host")
  private val port = configuration.getInt("ldap.port")
  private val ldaps = configuration.getBoolean("ldap.ldaps")
  private val bindDN = configuration.getString("ldap.bindDN")
  private val password = configuration.getString("ldap.password")
  private val initialConnextions = configuration.getInt("ldap.initialConnextions")
  private val maxConnections = configuration.getInt("ldap.maxConnections")
  protected val baseDN = configuration.getString("ldap.baseDN")
  protected val uidAttributeName = configuration.getString("ldap.uidAttributeName")

  private val connectionOption = new LDAPConnectionOptions
  private def getConnectionOptions: LDAPConnectionOptions = {
    connectionOption.setConnectTimeoutMillis(configuration.getInt("ldap.connectTimeout"))
    connectionOption.setResponseTimeoutMillis(configuration.getInt("ldap.responseTimeout"))
    connectionOption.setAbandonOnTimeout(configuration.getBoolean("ldap.abandonOnTimeOut"))
    connectionOption
  }

  case class UserConnection(
    dn: String,
    connection: LDAPConnection
  //TODO: Add user's role.
  )

  /**
   * Create connection using by config user.
   * TODO: Support LDAPS
   */
  protected val defaultConnection = new LDAPConnection(getConnectionOptions, host, port, bindDN, password)

  /**
   * The connections store by user.
   */
  protected val connections: mutable.HashMap[UserId, UserConnection] = mutable.HashMap()

  /**
   * Create connection by users and store it.
   * TODO: Support LDAPS
   */
  def createConnectionByUser(uid: UserId, dn: String, password: String): Unit = {
    val connection = UserConnection(dn, new LDAPConnection(getConnectionOptions, host, port, dn, password))
    connections += (uid -> connection)
  }

  /**
   * Remove & close connection from connections store by User.
   */
  def removeConnectionByUser(uid: UserId): Unit = {
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
  def getConnectionByUser(uid: UserId): Option[UserConnection] = {
    connections.get(uid)
  }

}