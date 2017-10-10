package app.services.ldap

import javax.inject._
import scala.collection.JavaConverters._
import scala.collection.mutable

import com.unboundid.ldap.sdk._

import app.models.{ LDAPAttribute, ActiveDirectoryUser }
import utils.ClassUtil
import utils.types.UserId

@Singleton
class ActiveDirectoryService extends LDAPService[ActiveDirectoryUser] {

  /**
   * Get user information by uid.
   *
   * @param connectionUser The current user id.
   * @param targetUid The target user's uid.
   * @return ActiveDirectoryUser
   */
  override def getUser(connectionUser: UserId, targetUid: String): Option[app.models.ActiveDirectoryUser] = {
    search(connectionUser, Filter.createEqualityFilter(uidAttributeName, targetUid), ClassUtil.getLDAPAttributeFields[app.models.ActiveDirectoryUser]) match {
      case Some(sr) => Some(mapSearchResultEntryToLdapClass[ActiveDirectoryUser](sr).head)
      case None => None
    }
  }
}