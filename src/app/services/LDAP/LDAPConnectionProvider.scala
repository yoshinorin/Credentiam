package app.services.ldap

import scala.collection.mutable

import com.typesafe.config.ConfigFactory
import com.unboundid.ldap.sdk._
import app.models.ldap.UserConnection
import utils.types.UserId
import utils.cache.PlaySyncCacheLayer

trait LDAPConnectionProvider {

  private val configuration = ConfigFactory.load
  private val host = configuration.getString("ldap.host")
  private val port = configuration.getInt("ldap.port")
  private val ldaps = configuration.getBoolean("ldap.ldaps")
  private val bindDN = configuration.getString("ldap.bindDN")
  private val password = configuration.getString("ldap.password")
  private val initialConnextions = configuration.getInt("ldap.initialConnextions")
  private val maxConnections = configuration.getInt("ldap.maxConnections")
  protected val maxResults = configuration.getInt("ldap.maxResult")
  protected val baseDN = configuration.getString("ldap.baseDN")
  protected val uidAttributeName = configuration.getString("ldap.uidAttributeName")

  private val connectionOption = new LDAPConnectionOptions
  connectionOption.setConnectTimeoutMillis(configuration.getInt("ldap.connectTimeout"))
  connectionOption.setResponseTimeoutMillis(configuration.getInt("ldap.responseTimeout"))
  connectionOption.setAbandonOnTimeout(configuration.getBoolean("ldap.abandonOnTimeOut"))

  /**
   * Create connection using by config user.
   * TODO: Support LDAPS
   */
  protected val defaultConnection = new LDAPConnection(connectionOption, host, port, bindDN, password)

  /**
   * Create connection by users and store it.
   * TODO: Support LDAPS
   */
  def createConnectionByUser(uid: UserId, dn: String, password: String): Unit = {
    val connection = UserConnection(dn, new LDAPConnection(connectionOption, host, port, dn, password))
    //TODO: Set cache duration.
    PlaySyncCacheLayer.cache.set(uid.value.toString, connection)
  }

  /**
   * Remove & close connection from connections store by User.
   */
  def removeConnectionByUser(uid: UserId): Unit = {
    PlaySyncCacheLayer.cache.get[UserConnection](uid.value.toString) match {
      case Some(uc) => {
        PlaySyncCacheLayer.cache.remove(uid.value.toString)
      }
      case None => None
    }
  }

  /**
   * Find Connection by uid.
   */
  def findConnectionByUser(uid: UserId): Option[UserConnection] = {
    PlaySyncCacheLayer.cache.get[UserConnection](uid.value.toString)
  }

}
