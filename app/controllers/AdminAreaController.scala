package app.controllers

import javax.inject.Inject

import scala.concurrent.Future

import play.api.i18n.I18nSupport
import play.api.mvc.{ AbstractController, AnyContent, ControllerComponents }

import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.api.{ LogoutEvent, Silhouette }
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider

import controllers.AssetsFinder
import app.services.ldap.LDAPService
import utils.auth.DefaultEnv
import utils.auth.WithAdmin

class AdminAreaController @Inject() (
  components: ControllerComponents,
  credentialsProvider: CredentialsProvider,
  silhouette: Silhouette[DefaultEnv]
)(
  implicit
  assets: AssetsFinder
) extends AbstractController(components) with I18nSupport {

  def index = silhouette.SecuredAction(WithAdmin[DefaultEnv#A](CredentialsProvider.ID)).async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    Future.successful(Ok(views.html.admin.index(request.identity)))
  }

}
