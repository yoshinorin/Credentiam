package app.models

import com.unboundid.ldap.sdk.SearchResultEntry

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

//TODO: Clean up & Add attributeses
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

object OrganizationUnit {
  def store(sr: SearchResultEntry): OrganizationUnit = {
    new OrganizationUnit(
      LDAPAttribute.store("ldap.attribute.description", sr.getAttributeValue("description")),
      LDAPAttribute.store("ldap.attribute.distinguishedName", sr.getAttributeValue("distinguishedName")),
      LDAPAttribute.store("ldap.attribute.l", sr.getAttributeValue("l")),
      LDAPAttribute.store("ldap.attribute.name", sr.getAttributeValue("name")),
      LDAPAttribute.store("ldap.attribute.ou", sr.getAttributeValue("ou")),
      LDAPAttribute.store("ldap.attribute.postalCode", sr.getAttributeValue("postalCode")),
      LDAPAttribute.store("ldap.attribute.st", sr.getAttributeValue("st")),
      LDAPAttribute.store("ldap.attribute.street", sr.getAttributeValue("street")),
      LDAPAttribute.store("ldap.attribute.whenChanged", sr.getAttributeValue("whenChanged")),
      LDAPAttribute.store("ldap.attribute.whenCreated", sr.getAttributeValue("whenCreated"))
    )
  }
}

//TODO: Clean up & Add attributeses
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

object ActiveDirectoryUser {
  def store(sr: SearchResultEntry): ActiveDirectoryUser = {
    new ActiveDirectoryUser(
      LDAPAttribute.store("ldap.attribute.cn", sr.getAttributeValue("cn")),
      LDAPAttribute.store("ldap.attribute.company", sr.getAttributeValue("company")),
      LDAPAttribute.store("ldap.attribute.department", sr.getAttributeValue("department")),
      LDAPAttribute.store("ldap.attribute.description", sr.getAttributeValue("description")),
      LDAPAttribute.store("ldap.attribute.displayName", sr.getAttributeValue("displayName")),
      LDAPAttribute.store("ldap.attribute.distinguishedName", sr.getAttributeValue("distinguishedName")),
      LDAPAttribute.store("ldap.attribute.name", sr.getAttributeValue("name")),
      LDAPAttribute.store("ldap.attribute.sAMAccountName", sr.getAttributeValue("sAMAccountName")),
      LDAPAttribute.store("ldap.attribute.sn", sr.getAttributeValue("sn")),
      LDAPAttribute.store("ldap.attribute.userPrincipalName", sr.getAttributeValue("userPrincipalNames")),
      LDAPAttribute.store("ldap.attribute.whenChanged", sr.getAttributeValue("whenChanged")),
      LDAPAttribute.store("ldap.attribute.whenCreated", sr.getAttributeValue("whenCreated"))
    )
  }
}

//TODO: Clean up & Add attributeses
case class Computer(
  cn: LDAPAttribute,
  description: LDAPAttribute,
  distinguishedName: LDAPAttribute,
  managedBy: LDAPAttribute,
  name: LDAPAttribute,
  whenChanged: LDAPAttribute,
  whenCreated: LDAPAttribute
)

object Computer {
  def store(sr: SearchResultEntry): Computer = {
    new Computer(
      LDAPAttribute.store("ldap.attribute.cn", sr.getAttributeValue("cn")),
      LDAPAttribute.store("ldap.attribute.description", sr.getAttributeValue("description")),
      LDAPAttribute.store("ldap.attribute.distinguishedName", sr.getAttributeValue("distinguishedName")),
      LDAPAttribute.store("ldap.attribute.managedBy", sr.getAttributeValue("managedBy")),
      LDAPAttribute.store("ldap.attribute.name", sr.getAttributeValue("name")),
      LDAPAttribute.store("ldap.attribute.whenChanged", sr.getAttributeValue("whenChanged")),
      LDAPAttribute.store("ldap.attribute.whenCreated", sr.getAttributeValue("whenCreated"))
    )
  }
}