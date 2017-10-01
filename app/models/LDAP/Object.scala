package app.models

case class LDAPAttribute(
  name: String,
  //TODO: Should another type. ex) byte
  //TODO: value have to change array or list.
  value: String = "-"
)

object LDAPAttribute {
  def store(name: String, value: String): LDAPAttribute = {
    if (value == null || value.isEmpty) {
      return new LDAPAttribute(name, "-")
    }
    new LDAPAttribute(name, value)
  }
}

//TODO: Add attributeses
case class OrganizationUnit(
  description: LDAPAttribute,
  distinguishedName: LDAPAttribute,
  l: LDAPAttribute,
  name: LDAPAttribute,
  ou: LDAPAttribute,
  postalCode: LDAPAttribute,
  st: LDAPAttribute,
  street: LDAPAttribute,
  whenChanged: LDAPAttribute,
  whenCreated: LDAPAttribute
)

//TODO: Add attributeses
case class ActiveDirectoryUser(
  cn: LDAPAttribute,
  company: LDAPAttribute,
  department: LDAPAttribute,
  description: LDAPAttribute,
  displayName: LDAPAttribute,
  distinguishedName: LDAPAttribute,
  name: LDAPAttribute,
  sAMAccountName: LDAPAttribute,
  sn: LDAPAttribute,
  userPrincipalName: LDAPAttribute,
  whenChanged: LDAPAttribute,
  whenCreated: LDAPAttribute
)