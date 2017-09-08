package app.models

import scala.collection.mutable
import scala.concurrent.Future

import com.mohiva.play.silhouette.api.{ Identity, LoginInfo }
import app.models.UserIdentify
import app.models.UserDAOImpl._

/**
  * The companion object.
  */
object UserDAOImpl {

  /**
    * The list of users.
    */
  val users: mutable.HashMap[String, UserIdentify] = mutable.HashMap()
}

/**
  * Give access to the user object.
  */
class UserDAOImpl extends UserDAO {

  def find(loginInfo: LoginInfo) = Future.successful(
    users.find { case (_, user) => user.loginInfo == loginInfo }.map(_._2)
  )

  def find(userID: String) = Future.successful(users.get(userID))

  def save(user: UserIdentify) = {
    users += (user.userID -> user)
    Future.successful(user)
  }
}

trait UserDAO {

  def find(loginInfo: LoginInfo): Future[Option[UserIdentify]]

  def find(userID: String): Future[Option[UserIdentify]]

  def save(user: UserIdentify): Future[UserIdentify]
}

case class UserIdentify(
  userID: String,
  loginInfo: LoginInfo,
) extends Identity