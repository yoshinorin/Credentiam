package models.daos

import com.mohiva.play.silhouette.api.LoginInfo
import models.UserIdentify
import models.daos.UserDAOImpl._

import scala.collection.mutable
import scala.concurrent.Future

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
