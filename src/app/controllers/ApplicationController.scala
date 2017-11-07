package app.controllers

import javax.inject.Inject

import scala.concurrent.Future
import play.api.i18n.I18nSupport
import play.api.mvc.{ AbstractController, AnyContent, ControllerComponents }
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.api.{ LogoutEvent, Silhouette }
import controllers.AssetsFinder

import app.services.ldap.LDAPService
import app.utils.auth.DefaultEnv

class ApplicationController @Inject() (
  components: ControllerComponents,
  silhouette: Silhouette[DefaultEnv])(
  implicit
  assets: AssetsFinder) extends AbstractController(components) with I18nSupport {

  def index = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    Future.successful(Ok(views.html.home(request.identity)))
  }

  def about = Action { implicit request =>
    Ok(views.html.about())
  }

  def help = Action { implicit request =>
    Ok(views.html.help())
  }

  def profile = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    //TODO: Exception handling
    Future.successful(Ok(views.html.user("profile.title", request.identity, (LDAPService.server.findUser(request.identity.userID, request.identity.userID.value.toString)))))
  }

}
