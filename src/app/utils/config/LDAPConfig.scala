package app.utils.config

import scala.concurrent.duration._
import com.typesafe.config.ConfigFactory

object LDAPConfig {

  private val configuration = ConfigFactory.load

  val host = configuration.getString("ldap.host")
  val port = configuration.getInt("ldap.port")
  val ldaps = configuration.getBoolean("ldap.ldaps")
  val bindDN = configuration.getString("ldap.bindDN")
  val password = configuration.getString("ldap.password")
  val initialConnextions = configuration.getInt("ldap.initialConnextions")
  val maxConnections = configuration.getInt("ldap.maxConnections")
  val connectTimeout = configuration.getInt("ldap.connectTimeout")
  val responseTimeout = configuration.getInt("ldap.responseTimeout")
  val abandonOnTimeOut = configuration.getBoolean("ldap.abandonOnTimeOut")
  val expiryDuration: Duration = Duration(configuration.getInt("ldap.expiryDuration"), "minutes")
  val maxResults = configuration.getInt("ldap.maxResult")
  val baseDN = configuration.getString("ldap.baseDN")
  val uidAttributeName = configuration.getString("ldap.uidAttributeName")

}
