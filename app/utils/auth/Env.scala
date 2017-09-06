package utils.auth

import com.mohiva.play.silhouette.api.Env
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import models.UserIdentify

trait DefaultEnv extends Env {
  type I = UserIdentify
  type A = CookieAuthenticator
}
