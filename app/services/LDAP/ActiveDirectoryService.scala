package app.services.ldap

import javax.inject._
import scala.collection.JavaConverters._
import scala.collection.mutable

import com.unboundid.ldap.sdk._

import app.models.{ LDAPAttribute, ActiveDirectoryUser }
import utils.ClassUtil
import utils.types.UserId

@Singleton
class ActiveDirectoryService extends LDAPService {

  /**
   * Mapping com.unboundid.ldap.sdk.SearchResultEntry to ActiveDirectoryUser
   *
   * @param com.unboundid.ldap.sdk.SearchResultEntry
   * @return ActiveDirectoryUsers
   * TODO: More Abstractly
   */
  def mapActiveDirectoryUser(sr: Seq[com.unboundid.ldap.sdk.SearchResultEntry]): Seq[ActiveDirectoryUser] = {
    var users = mutable.ListBuffer.empty[ActiveDirectoryUser]
    sr.foreach(v =>
      users += ActiveDirectoryUser(
        LDAPAttribute("ldap.attribute.cn", v.getAttributeValue("cn")),
        LDAPAttribute("ldap.attribute.displayName", v.getAttributeValue("displayName")),
        LDAPAttribute("ldap.attribute.distinguishedName", v.getAttributeValue("distinguishedName")),
        LDAPAttribute("ldap.attribute.name", v.getAttributeValue("name")),
        LDAPAttribute("ldap.attribute.sAMAccountName", v.getAttributeValue("sAMAccountName")),
        LDAPAttribute("ldap.attribute.sn", v.getAttributeValue("sn")),
        LDAPAttribute("ldap.attribute.userPrincipalName", v.getAttributeValue("userPrincipalNames"))
      )
    )
    users.toSeq
  }

  /**
   * Get user information by uid.
   * @param Uid for connection user.
   * @param Serach user's uid
   * @return ActiveDirectoryUser
   */
  override def getUser(connectionUser: UserId, targetUid: String): Option[ActiveDirectoryUser] = {
    search(connectionUser, Filter.createEqualityFilter(uidAttributeName, targetUid), ClassUtil.getFields[ActiveDirectoryUser]) match {
      case Some(sr) => Some(mapActiveDirectoryUser(sr).head)
      case None => None
    }
  }
}