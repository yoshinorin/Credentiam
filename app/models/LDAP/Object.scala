package app.models

case class LDAPAttribute(
  name: String,
  //TODO: Should another type. ex) byte
  value: String
)

//TODO: Add attributeses
case class ActiveDirectoryUser(
  cn: String,
  displayName: String,
  distinguishedName: String,
  name: String,
  sAMAccountName: String,
  sn: String,
  userPrincipalName: String
)