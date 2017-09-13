package app.services

import javax.inject.Inject

import scala.concurrent.{ ExecutionContext, Future }
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import app.models.UserIdentify
import app.models.UserDAO

trait UserService extends IdentityService[UserIdentify] {

  def retrieve(id: String): Future[Option[UserIdentify]]

  def save(user: UserIdentify): Future[UserIdentify]

}

/**
 * Handles actions to users.
 */
class UserServiceImpl @Inject() (userDAO: UserDAO)(implicit ex: ExecutionContext) extends UserService {

  def retrieve(id: String) = userDAO.find(id)

  def retrieve(loginInfo: LoginInfo): Future[Option[UserIdentify]] = userDAO.find(loginInfo)

  def save(user: UserIdentify) = userDAO.save(user)

}

