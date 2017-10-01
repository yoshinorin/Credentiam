package app.controllers

import javax.inject.Inject

import scala.concurrent.Future
import play.api.i18n.I18nSupport
import play.api.mvc.{ AbstractController, AnyContent, ControllerComponents }
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.api.{ LogoutEvent, Silhouette }
import controllers.AssetsFinder

import app.services.ldap.LDAPService
import utils.auth.DefaultEnv

class LDAPController @Inject() (
  components: ControllerComponents,
  silhouette: Silhouette[DefaultEnv]
)(
  implicit
  assets: AssetsFinder
) extends AbstractController(components) with I18nSupport {

  def organization(dn: String) = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    //TODO: Exception handling
    Future.successful(Ok(views.html.organization(request.identity, (LDAPService.server.getOrganization(request.identity.userID, dn)))))
  }

  def organizations = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    //TODO: Exception handling
    Future.successful(Ok(views.html.organizations("view.common.organizations", request.identity, (LDAPService.server.getOrganizations(request.identity.userID)))))
  }

  def computers = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    //TODO: Exception handling
    Future.successful(Ok(views.html.computers("view.common.computers", request.identity, (LDAPService.server.getComputers(request.identity.userID)))))
  }

}