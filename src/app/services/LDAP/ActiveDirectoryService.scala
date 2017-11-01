package app.services.ldap

import javax.inject._
import scala.collection.JavaConverters._
import scala.collection.mutable

import com.unboundid.ldap.sdk._

import app.models.ldap.{ Attribute, ActiveDirectoryUser }
import utils.ClassUtil
import utils.types.UserId

@Singleton
class ActiveDirectoryService extends LDAPService[ActiveDirectoryUser] {

  /**
   * Find mapped ActiveDirectoryUser class.
   *
   * @param connectionUser The current user id.
   * @param targetUid The target uid.
   * @return ActiveDirectoryUser class or none.
   */
  override def findUser(connectionUser: UserId, targetUid: String): Option[app.models.ldap.ActiveDirectoryUser] = {
    search(connectionUser, Filter.createEqualityFilter(uidAttributeName, targetUid), ClassUtil.getLDAPAttributeFields[app.models.ldap.ActiveDirectoryUser]) match {
      case Some(sr) => Some(mapSearchResultEntryToLdapClass[ActiveDirectoryUser](sr).head)
      case None => None
    }
  }
}
