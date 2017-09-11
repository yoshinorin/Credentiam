package utils.auth

import javax.inject.Inject

import scala.concurrent.duration._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.cache._
import play.api.Configuration
import play.api.mvc._
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO

import app.models.UserIdentify

class LDAPAuth @Inject() (
  cache: AsyncCacheApi,
  configuration: Configuration
) extends DelegableAuthInfoDAO[PasswordInfo] {

  val expiryDuration = configuration.get[FiniteDuration]("ldap.expiryDuration")

  def add(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    cache.set(loginInfo.providerKey, PersistentAuthInfo(loginInfo, authInfo), expiryDuration).map(_ => authInfo)
  }

  //TODO: Is this method work well ?
  def save(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    add(loginInfo, authInfo)
  }

  //TODO: Is this method work well ?
  def update(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    add(loginInfo, authInfo)
  }

  def remove(loginInfo: LoginInfo): Future[Unit] = {
    Future.successful(cache.remove(loginInfo.providerKey).map(_ => loginInfo))

  }

  //FIXME : Exception occur
  def find(loginInfo: LoginInfo): Future[Option[PasswordInfo]] = {
    cache.get(loginInfo.providerKey)
  }
}

case class PersistentAuthInfo(
  loginInfo: LoginInfo,
  passwordInfo: PasswordInfo
)