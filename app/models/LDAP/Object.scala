package app.models

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