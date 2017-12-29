package app.services.cache

object LDAPConnectionCache {

  val cache = new EhCacheProvider("ldap-connection")

}
