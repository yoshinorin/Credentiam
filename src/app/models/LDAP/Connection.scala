package app.models.ldap

import com.unboundid.ldap.sdk.LDAPConnection

case class UserConnection(
  dn: String,
  connection: LDAPConnection
//TODO: Add user's role.
)
