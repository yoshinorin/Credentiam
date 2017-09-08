package models.daos

import com.mohiva.play.silhouette.api.LoginInfo
import models.UserIdentify

import scala.concurrent.Future

/**
 * Give access to the user object.
 */
trait UserDAO {

  def find(loginInfo: LoginInfo): Future[Option[UserIdentify]]

  def find(userID: String): Future[Option[UserIdentify]]

  def save(user: UserIdentify): Future[UserIdentify]
}
