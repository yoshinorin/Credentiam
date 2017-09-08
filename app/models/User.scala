package models

import com.mohiva.play.silhouette.api.{ Identity, LoginInfo }

case class UserIdentify(
  userID: String,
  loginInfo: LoginInfo,
) extends Identity