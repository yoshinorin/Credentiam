package app.controllers

import javax.inject.Inject
import scala.concurrent.Future
import play.api.i18n.{ I18nSupport, Messages }
import play.api.mvc.{ AbstractController, AnyContent, ControllerComponents }
import play.api.data.Form
import play.api.data.Forms._
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.api.{ LogoutEvent, Silhouette }
import com.unboundid.ldap.sdk.Filter
import controllers.AssetsFinder
import app.models.ldap.{ ActiveDirectoryUser, Computer, Attribute, LDAPObjectOverview, OrganizationUnit }
import app.services.ldap.LDAPService
import app.utils.auth.DefaultEnv
import app.utils.config.LDAPSearchableAttributes

import LDAPController._

object LDAPController {

  case class SearchFormData(objectType: String, word: String)

  val SearchForm = Form(
    mapping(
      "objectType" -> nonEmptyText,
      "word" -> nonEmptyText,
    )(SearchFormData.apply)(SearchFormData.unapply)
  )

}

class LDAPController @Inject() (
  components: ControllerComponents,
  silhouette: Silhouette[DefaultEnv])(
  implicit
  assets: AssetsFinder) extends AbstractController(components) with I18nSupport {

  def search = TODO

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

  def users = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    //TODO: Exception handling
    Future.successful(Ok(views.html.users(request.identity, (LDAPService.server.findUsers(request.identity.userID)))))
  }

}
