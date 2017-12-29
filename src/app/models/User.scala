package app.models

import scala.collection.mutable
import scala.concurrent.Future
import com.mohiva.play.silhouette.api.{ Identity, LoginInfo }
import app.models.ldap.UserConnection
import app.services.cache.{ LDAPConnectionCache, UserAuthenticationCache }
import app.utils.config.LDAPConfig
import app.utils.types.UserId

/**
 * Give access to the user object.
 */
class UserDAO extends UserDAOTrait {

  def find(loginInfo: LoginInfo) = {
    LDAPConnectionCache.cache.get[UserConnection](loginInfo.providerKey) match {
      case Some(uc) => {
        UserAuthenticationCache.cache.get[UserIdentify](loginInfo.providerKey) match {
          case Some(user) => Future.successful(Option(user))
          case None => Future.successful(None)
        }
      }
      case None => Future.successful(None)
    }
  }

  def find(userID: UserId) = Future.successful(UserAuthenticationCache.cache.get[UserIdentify](userID.value.toString))

  def save(user: UserIdentify) = {
    UserAuthenticationCache.cache.set(user.userID.value.toString, user, LDAPConfig.expiryDuration)
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
