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
   * Mapping SearchResultEntries to ActiveDirectoryUsers
   *
   * @param SearchResultEntries
   * @return ActiveDirectoryUsers
   * TODO: More Abstractly
   */
  def mapActiveDirectoryUser(sr: Seq[com.unboundid.ldap.sdk.SearchResultEntry]): Seq[ActiveDirectoryUser] = {
    var users = mutable.ListBuffer.empty[ActiveDirectoryUser]
    sr.foreach(v =>
      users += ActiveDirectoryUser.store(v)
    )
    users.toSeq
  }

  /**
   * Get user information by uid.
   *
   * @param connectionUser The current user id.
   * @param targetUid The target user's uid.
   * @return ActiveDirectoryUser
   */
  override def getUser(connectionUser: UserId, targetUid: String): Option[ActiveDirectoryUser] = {
    search(connectionUser, Filter.createEqualityFilter(uidAttributeName, targetUid), ClassUtil.getFields[ActiveDirectoryUser]) match {
      case Some(sr) => Some(mapActiveDirectoryUser(sr).head)
      case None => None
    }
  }
}