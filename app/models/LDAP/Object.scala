package app.models

//TODO: Add attributeses
case class ActiveDirectoryUser(
  dn: Option[String],
  cn: Option[String],
  displayName: Option[String],
  distinguishedName: Option[String],
  name: Option[String],
  sAMAccountName: Option[String],
  sn: Option[String],
  userPrincipalName: Option[String]
)