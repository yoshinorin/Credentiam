package app.models

import scala.collection.mutable
import scala.concurrent.Future
import com.mohiva.play.silhouette.api.{ Identity, LoginInfo }
import app.models.UserIdentify
import app.models.ldap.UserConnection
import app.services.cache.LDAPConnectionCache
import app.utils.types.UserId

import app.models.UserDAO._

/**
 * The companion object.
 */
object UserDAO {

  /**
   * The list of users.
   * TODO: Use EhCache
   */
  val users: mutable.HashMap[UserId, UserIdentify] = mutable.HashMap()

}

/**
 * Give access to the user object.
 */
class UserDAO extends UserDAOTrait {

  def find(loginInfo: LoginInfo) = {
    LDAPConnectionCache.cache.get[UserConnection](loginInfo.providerKey) match {
      case Some(uc) => {
        Future.successful(users.find { case (_, user) => user.loginInfo == loginInfo }.map(_._2))
      }
      case None => Future.successful(None)
    }
  }

  def find(userID: UserId) = Future.successful(users.get(userID))

  def save(user: UserIdentify) = {
    users += (user.userID -> user)
    Future.successful(user)
  }

}

trait UserDAOTrait {

  def find(loginInfo: LoginInfo): Future[Option[UserIdentify]]

  def find(userID: UserId): Future[Option[UserIdentify]]

  def save(user: UserIdentify): Future[UserIdentify]

}

case class UserIdentify(
  userID: UserId,
  loginInfo: LoginInfo,
  isAdmin: Boolean) extends Identity
