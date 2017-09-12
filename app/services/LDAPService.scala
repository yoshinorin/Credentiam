package app.services

import scala.collection.mutable
import com.typesafe.config.ConfigFactory

import com.unboundid.ldap.sdk._

object LDAPService {

  val configuration = ConfigFactory.load
  val host = configuration.getString("ldap.host")
  val port = configuration.getInt("ldap.port")
  val ldaps = configuration.getBoolean("ldap.ldaps")
  val bindDN = configuration.getString("ldap.bindDN")
  val baseDN = configuration.getString("ldap.baseDN")
  val uid = configuration.getString("ldap.uid")}  //def bind(uid: String, password: String) = TODO

}