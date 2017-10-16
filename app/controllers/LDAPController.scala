package app.controllers

import javax.inject.Inject

import scala.concurrent.Future
import play.api.i18n.I18nSupport
import play.api.mvc.{ AbstractController, AnyContent, ControllerComponents }
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.api.{ LogoutEvent, Silhouette }
import controllers.AssetsFinder

import app.models.{ ActiveDirectoryUser, Computer, LDAPAttribute, OrganizationUnit }
import app.services.ldap.LDAPService
import utils.auth.DefaultEnv

class LDAPController @Inject() (
  components: ControllerComponents,
  silhouette: Silhouette[DefaultEnv]
)(
  implicit
  assets: AssetsFinder
) extends AbstractController(components) with I18nSupport {

  def search = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    Future.successful(Ok(views.html.search(request.identity)))
  }

  def domains = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    //TODO: Exception handling
    Future.successful(Ok(views.html.domains(request.identity, (LDAPService.server.findDomains(request.identity.userID)))))
  }

  def organization(dn: String) = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    //TODO: Exception handling
    Future.successful(Ok(views.html.organization(request.identity, (LDAPService.server.findByDN[OrganizationUnit](request.identity.userID, dn)))))
  }

  def organizations = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    //TODO: Exception handling
    Future.successful(Ok(views.html.organizations(request.identity, (LDAPService.server.findOrganizations(request.identity.userID)))))
  }

  def computers = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    //TODO: Exception handling
    Future.successful(Ok(views.html.computers(request.identity, (LDAPService.server.findComputers(request.identity.userID)))))
  }

}