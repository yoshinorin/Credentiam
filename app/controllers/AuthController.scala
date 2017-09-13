package app.controllers

import javax.inject.Inject

import scala.concurrent.duration._
import scala.concurrent.{ ExecutionContext, Future }

import play.api.Configuration
import play.api.i18n.{ I18nSupport, Messages }
import play.api.mvc.{ AbstractController, AnyContent, ControllerComponents, Request }
import play.api.data.Form
import play.api.data.Forms._

import com.mohiva.play.silhouette.api.Authenticator.Implicits._
import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.api.util.{ Credentials, PasswordHasherRegistry }
import com.mohiva.play.silhouette.impl.exceptions.IdentityNotFoundException
import com.mohiva.play.silhouette.impl.providers._
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.api.util.PasswordInfo
import net.ceedubs.ficus.Ficus._
import com.unboundid.ldap.sdk._

import controllers.AssetsFinder
import app.models.UserIdentify
import app.services.{ UserService, ActiveDirectoryService }
import utils.auth.DefaultEnv
import AuthController._

object AuthController {
  case class SignInFormData(uid: String, password: String)

  val signInForm = Form(
    mapping(
      "uid" -> nonEmptyText,
      "password" -> nonEmptyText,
    )(SignInFormData.apply)(SignInFormData.unapply)
  )
}

class AuthController @Inject() (
  components: ControllerComponents,
  silhouette: Silhouette[DefaultEnv],
  userService: UserService,
  authInfoRepository: AuthInfoRepository,
  passwordHasherRegistry: PasswordHasherRegistry,
  credentialsProvider: CredentialsProvider,
  configuration: Configuration
)(
  implicit
  assets: AssetsFinder,
  ex: ExecutionContext
) extends AbstractController(components) with I18nSupport {

  def view = silhouette.UnsecuredAction.async { implicit request: Request[AnyContent] =>
    Future.successful(Ok(views.html.signIn(signInForm)))
  }

  def signIn = silhouette.UnsecuredAction.async { implicit request =>
    signInForm.bindFromRequest.fold(
      form => Future.successful(BadRequest(views.html.signIn(signInForm))),
      data => {
        try {
          if (ActiveDirectoryService.bind(data.uid, data.password) == ResultCode.SUCCESS) {
            val loginInfo = LoginInfo(CredentialsProvider.ID, data.uid)
            val authInfo = passwordHasherRegistry.current.hash(data.password)
            val user = UserIdentify(data.uid, loginInfo)

            for {
              user <- userService.save(user)
              authInfo <- authInfoRepository.add(loginInfo, authInfo)
              authenticator <- silhouette.env.authenticatorService.create(loginInfo)
              value <- silhouette.env.authenticatorService.init(authenticator)
              result <- silhouette.env.authenticatorService.embed(value, Redirect(routes.ApplicationController.index()))
            } yield {
              silhouette.env.eventBus.publish(LoginEvent(user, request))
              result
            }
          } else {
            Future.successful(Redirect(routes.AuthController.view()).flashing("error" -> Messages("invalid.credentials")))
          }
        } catch {
          case e: Exception => Future.successful(Redirect(routes.AuthController.view()).flashing("error" -> Messages(e.getMessage)))
        }
      }
    )
  }

  def signOut = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    ActiveDirectoryService.removeConnectionByUser(request.identity.userID)
    authInfoRepository.remove[PasswordInfo](request.identity.loginInfo)
    val result = Redirect(app.controllers.routes.ApplicationController.index())
    silhouette.env.eventBus.publish(LogoutEvent(request.identity, request))
    silhouette.env.authenticatorService.discard(request.authenticator, result)
  }

}

