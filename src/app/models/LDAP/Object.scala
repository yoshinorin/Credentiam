package app.models

import com.unboundid.ldap.sdk.SearchResultEntry

case class LDAPAttribute(
  name: String,
  //TODO: Should another type. ex) byte
  //TODO: value have to change array or list.
  value: String = "-")

object LDAPAttribute {
  def store(name: String, value: String): LDAPAttribute = {
    Option(value) match {
      case Some(v) => new LDAPAttribute(name, v)
      case None => new LDAPAttribute(name, "-")
    }
  }
}

class LDAPObjectOverview(sr: SearchResultEntry) {

  val description: LDAPAttribute = LDAPAttribute.store("ldap.attribute.description", sr.getAttributeValue("description"))
  val distinguishedName: LDAPAttribute = LDAPAttribute.store("ldap.attribute.distinguishedName", sr.getAttributeValue("distinguishedName"))
  val name: LDAPAttribute = LDAPAttribute.store("ldap.attribute.name", sr.getAttributeValue("name"))

}

//TODO: Clean up & Add attributeses
class OrganizationUnit(sr: SearchResultEntry) {

  val description: LDAPAttribute = LDAPAttribute.store("ldap.attribute.description", sr.getAttributeValue("description"))
  val distinguishedName: LDAPAttribute = LDAPAttribute.store("ldap.attribute.distinguishedName", sr.getAttributeValue("distinguishedName"))
  val l: LDAPAttribute = LDAPAttribute.store("ldap.attribute.l", sr.getAttributeValue("l"))
  val name: LDAPAttribute = LDAPAttribute.store("ldap.attribute.name", sr.getAttributeValue("name"))
  val ou: LDAPAttribute = LDAPAttribute.store("ldap.attribute.ou", sr.getAttributeValue("ou"))
  val postalCode: LDAPAttribute = LDAPAttribute.store("ldap.attribute.postalCode", sr.getAttributeValue("postalCode"))
  val st: LDAPAttribute = LDAPAttribute.store("ldap.attribute.st", sr.getAttributeValue("st"))
  val street: LDAPAttribute = LDAPAttribute.store("ldap.attribute.street", sr.getAttributeValue("street"))
  val whenChanged: LDAPAttribute = LDAPAttribute.store("ldap.attribute.whenChanged", sr.getAttributeValue("whenChanged"))
  val whenCreated: LDAPAttribute = LDAPAttribute.store("ldap.attribute.whenCreated", sr.getAttributeValue("whenCreated"))

}

//TODO: Clean up & Add attributeses
class ActiveDirectoryUser(sr: SearchResultEntry) {

  val cn: LDAPAttribute = LDAPAttribute.store("ldap.attribute.cn", sr.getAttributeValue("cn"))
  val company: LDAPAttribute = LDAPAttribute.store("ldap.attribute.company", sr.getAttributeValue("company"))
  val department: LDAPAttribute = LDAPAttribute.store("ldap.attribute.department", sr.getAttributeValue("department"))
  val description: LDAPAttribute = LDAPAttribute.store("ldap.attribute.description", sr.getAttributeValue("description"))
  val displayName: LDAPAttribute = LDAPAttribute.store("ldap.attribute.displayName", sr.getAttributeValue("displayName"))
  val distinguishedName: LDAPAttribute = LDAPAttribute.store("ldap.attribute.distinguishedName", sr.getAttributeValue("distinguishedName"))
  val name: LDAPAttribute = LDAPAttribute.store("ldap.attribute.name", sr.getAttributeValue("name"))
  val sAMAccountName: LDAPAttribute = LDAPAttribute.store("ldap.attribute.sAMAccountName", sr.getAttributeValue("sAMAccountName"))
  val sn: LDAPAttribute = LDAPAttribute.store("ldap.attribute.sn", sr.getAttributeValue("sn"))
  val userPrincipalName: LDAPAttribute = LDAPAttribute.store("ldap.attribute.userPrincipalName", sr.getAttributeValue("userPrincipalNames"))
  val whenChanged: LDAPAttribute = LDAPAttribute.store("ldap.attribute.whenChanged", sr.getAttributeValue("whenChanged"))
  val whenCreated: LDAPAttribute = LDAPAttribute.store("ldap.attribute.whenCreated", sr.getAttributeValue("whenCreated"))

}

//TODO: Clean up & Add attributeses
class Computer(sr: SearchResultEntry) {

  val cn: LDAPAttribute = LDAPAttribute.store("ldap.attribute.cn", sr.getAttributeValue("cn"))
  val description: LDAPAttribute = LDAPAttribute.store("ldap.attribute.description", sr.getAttributeValue("description"))
  val distinguishedName: LDAPAttribute = LDAPAttribute.store("ldap.attribute.distinguishedName", sr.getAttributeValue("distinguishedName"))
  val managedBy: LDAPAttribute = LDAPAttribute.store("ldap.attribute.managedBy", sr.getAttributeValue("managedBy"))
  val name: LDAPAttribute = LDAPAttribute.store("ldap.attribute.name", sr.getAttributeValue("name"))
  val whenChanged: LDAPAttribute = LDAPAttribute.store("ldap.attribute.whenChanged", sr.getAttributeValue("whenChanged"))
  val whenCreated: LDAPAttribute = LDAPAttribute.store("ldap.attribute.whenCreated", sr.getAttributeValue("whenCreated"))

}