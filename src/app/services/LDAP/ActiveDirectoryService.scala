package app.services.ldap

import javax.inject._
import scala.collection.JavaConverters._
import com.unboundid.ldap.sdk._
import app.models.ldap.{ Attribute, ActiveDirectoryUser }
import app.utils.ClassUtil
import app.utils.types.UserId
import app.utils.config.LDAPConfig

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
    search(connectionUser, Filter.createEqualityFilter(LDAPConfig.uidAttributeName, targetUid), ClassUtil.getLDAPAttributeFields[app.models.ldap.ActiveDirectoryUser]) match {
      case Some(sr) => Some(mapSearchResultEntryToLdapClass[ActiveDirectoryUser](sr).head)
      case None => None
    }
  }
}
