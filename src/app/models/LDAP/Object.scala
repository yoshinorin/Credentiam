package app.models.ldap

import com.unboundid.ldap.sdk.SearchResultEntry

case class Attribute(
  name: String,
  //TODO: Should another type. ex) byte
  //TODO: value have to change array or list.
  value: String = "-")

object Attribute {
  def store(name: String, value: String): Attribute = {
    Option(value) match {
      case Some(v) => new Attribute(name, v)
      case None => new Attribute(name, "-")
    }
  }
}

class LDAPObjectOverview(sr: SearchResultEntry) {

  val description: Attribute = Attribute.store("ldap.attribute.description", sr.getAttributeValue("description"))
  val distinguishedName: Attribute = Attribute.store("ldap.attribute.distinguishedName", sr.getAttributeValue("distinguishedName"))
  val name: Attribute = Attribute.store("ldap.attribute.name", sr.getAttributeValue("name"))

}

class Domain(sr: SearchResultEntry) {

  val dc: Attribute = Attribute.store("ldap.attribute.dc", sr.getAttributeValue("dc"))
  val distinguishedName: Attribute = Attribute.store("ldap.attribute.distinguishedName", sr.getAttributeValue("distinguishedName"))
  val name: Attribute = Attribute.store("ldap.attribute.name", sr.getAttributeValue("name"))
  val whenChanged: Attribute = Attribute.store("ldap.attribute.whenChanged", sr.getAttributeValue("whenChanged"))
  val whenCreated: Attribute = Attribute.store("ldap.attribute.whenCreated", sr.getAttributeValue("whenCreated"))

}

//TODO: Clean up & Add attributeses
class OrganizationUnit(sr: SearchResultEntry) {

  val description: Attribute = Attribute.store("ldap.attribute.description", sr.getAttributeValue("description"))
  val distinguishedName: Attribute = Attribute.store("ldap.attribute.distinguishedName", sr.getAttributeValue("distinguishedName"))
  val l: Attribute = Attribute.store("ldap.attribute.l", sr.getAttributeValue("l"))
  val name: Attribute = Attribute.store("ldap.attribute.name", sr.getAttributeValue("name"))
  val ou: Attribute = Attribute.store("ldap.attribute.ou", sr.getAttributeValue("ou"))
  val postalCode: Attribute = Attribute.store("ldap.attribute.postalCode", sr.getAttributeValue("postalCode"))
  val st: Attribute = Attribute.store("ldap.attribute.st", sr.getAttributeValue("st"))
  val street: Attribute = Attribute.store("ldap.attribute.street", sr.getAttributeValue("street"))
  val whenChanged: Attribute = Attribute.store("ldap.attribute.whenChanged", sr.getAttributeValue("whenChanged"))
  val whenCreated: Attribute = Attribute.store("ldap.attribute.whenCreated", sr.getAttributeValue("whenCreated"))

}

class ActiveDirectoryUserOverView(sr: SearchResultEntry) extends LDAPObjectOverview(sr: SearchResultEntry) {
  val mail: Attribute = Attribute.store("ldap.attribute.mail", sr.getAttributeValue("mail"))
}

//TODO: Clean up & Add attributeses
class ActiveDirectoryUser(sr: SearchResultEntry) {

  val cn: Attribute = Attribute.store("ldap.attribute.cn", sr.getAttributeValue("cn"))
  val company: Attribute = Attribute.store("ldap.attribute.company", sr.getAttributeValue("company"))
  val department: Attribute = Attribute.store("ldap.attribute.department", sr.getAttributeValue("department"))
  val description: Attribute = Attribute.store("ldap.attribute.description", sr.getAttributeValue("description"))
  val displayName: Attribute = Attribute.store("ldap.attribute.displayName", sr.getAttributeValue("displayName"))
  val distinguishedName: Attribute = Attribute.store("ldap.attribute.distinguishedName", sr.getAttributeValue("distinguishedName"))
  val mail: Attribute = Attribute.store("ldap.attribute.mail", sr.getAttributeValue("mail"))
  val name: Attribute = Attribute.store("ldap.attribute.name", sr.getAttributeValue("name"))
  val sAMAccountName: Attribute = Attribute.store("ldap.attribute.sAMAccountName", sr.getAttributeValue("sAMAccountName"))
  val sn: Attribute = Attribute.store("ldap.attribute.sn", sr.getAttributeValue("sn"))
  val userPrincipalName: Attribute = Attribute.store("ldap.attribute.userPrincipalName", sr.getAttributeValue("userPrincipalNames"))
  val whenChanged: Attribute = Attribute.store("ldap.attribute.whenChanged", sr.getAttributeValue("whenChanged"))
  val whenCreated: Attribute = Attribute.store("ldap.attribute.whenCreated", sr.getAttributeValue("whenCreated"))

}

//TODO: Clean up & Add attributeses
class Computer(sr: SearchResultEntry) {

  val cn: Attribute = Attribute.store("ldap.attribute.cn", sr.getAttributeValue("cn"))
  val description: Attribute = Attribute.store("ldap.attribute.description", sr.getAttributeValue("description"))
  val distinguishedName: Attribute = Attribute.store("ldap.attribute.distinguishedName", sr.getAttributeValue("distinguishedName"))
  val managedBy: Attribute = Attribute.store("ldap.attribute.managedBy", sr.getAttributeValue("managedBy"))
  val name: Attribute = Attribute.store("ldap.attribute.name", sr.getAttributeValue("name"))
  val whenChanged: Attribute = Attribute.store("ldap.attribute.whenChanged", sr.getAttributeValue("whenChanged"))
  val whenCreated: Attribute = Attribute.store("ldap.attribute.whenCreated", sr.getAttributeValue("whenCreated"))

}
