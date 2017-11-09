package app.services.ldap

import com.unboundid.ldap.sdk.{ LDAPConnection, LDAPConnectionOptions }
import app.models.ldap.UserConnection
import app.utils.types.UserId
import app.utils.cache.PlaySyncCacheLayer
import app.utils.config.LDAPConfig

trait LDAPConnectionProvider {

  private val connectionOption = new LDAPConnectionOptions
  connectionOption.setConnectTimeoutMillis(LDAPConfig.connectTimeout)
  connectionOption.setResponseTimeoutMillis(LDAPConfig.responseTimeout)
  connectionOption.setAbandonOnTimeout(LDAPConfig.abandonOnTimeOut)

  /**
   * Create connection using by config user.
   * TODO: Support LDAPS
   * TODO: Need exception handling when create LDAPConnection class's instance.
   */
  protected val defaultConnection = new LDAPConnection(connectionOption, LDAPConfig.host, LDAPConfig.port, LDAPConfig.bindDN, LDAPConfig.password)

  /**
   * Create connection by users and store it.
   * TODO: Support LDAPS
   */
  def createConnectionByUser(uid: UserId, dn: String, password: String): Unit = {
    val connection = UserConnection(dn, new LDAPConnection(connectionOption, LDAPConfig.host, LDAPConfig.port, dn, password))
    PlaySyncCacheLayer.cache.set(uid.value.toString, connection, LDAPConfig.expiryDuration)
  }

  /**
   * Remove & close connection from connections store by User.
   */
  def removeConnectionByUser(uid: UserId): Unit = {
    PlaySyncCacheLayer.cache.get[UserConnection](uid.value.toString) match {
      case Some(uc) => {
        uc.connection.close
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
