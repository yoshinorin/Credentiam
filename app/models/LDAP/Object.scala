package app.models

case class LDAPAttribute(
  name: String,
  //TODO: Should another type. ex) byte
  value: String
)

//TODO: Add attributeses
case class ActiveDirectoryUser(
  cn: LDAPAttribute,
  displayName: LDAPAttribute,
  distinguishedName: LDAPAttribute,
  name: LDAPAttribute,
  sAMAccountName: LDAPAttribute,
  sn: LDAPAttribute,
  userPrincipalName: LDAPAttribute
)