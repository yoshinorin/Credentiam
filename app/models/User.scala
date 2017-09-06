package models

import java.util.UUID
import com.mohiva.play.silhouette.api.{ Identity, LoginInfo }

case class UserIdentify(
  userID: UUID,
  loginInfo: LoginInfo,
) extends Identity