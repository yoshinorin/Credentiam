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
import app.models.ldap.{ ActiveDirectoryUser, Computer, Domain, Attribute, LDAPObjectOverview, OrganizationUnit }
import app.services.ldap.{ LDAPService, LDAPQueryService }
import app.utils.auth.DefaultEnv
import app.utils.config.LDAPSearchableAttributes
import app.utils.types.SearchRelations
import app.utils.Converter._

import LDAPController._

object LDAPController {

  case class SearchFormData(objectType: String, relation: String, word: String)

  val SearchForm = Form(
    mapping(
      "objectType" -> nonEmptyText,
      "relation" -> nonEmptyText,
      "word" -> nonEmptyText,
    )(SearchFormData.apply)(SearchFormData.unapply)
  )

}

class LDAPController @Inject() (
  components: ControllerComponents,
  silhouette: Silhouette[DefaultEnv])(
  implicit
  assets: AssetsFinder) extends AbstractController(components) with I18nSupport {

  def profile = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    Future.successful(Ok(views.html.user("profile.title", request.identity, (LDAPService.server.findUser(request.identity.userID, request.identity.userID.value.toString)))))
  }

  def search = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    SearchForm.bindFromRequest.fold(
      form => Future.successful(Ok(views.html.search(request.identity, LDAPController.SearchForm, None, app.utils.types.LDAPObjectType.ORGANIZATION))),
      data => {
        val ldapObjectType = data.objectType.toLDAPObjectType
        val result = LDAPService.server.find[LDAPObjectOverview](request.identity.userID, LDAPQueryService.filterBuilder(ldapObjectType, data.relation.toSearchRelation, data.word))
        Future.successful(Ok(views.html.search(request.identity, LDAPController.SearchForm, result, ldapObjectType))),
      }
    )
  }

  def domain(dn: String) = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    Future.successful(Ok(views.html.domain(request.identity, (LDAPService.server.findByDN[Domain](request.identity.userID, dn)))))
  }

  def domains = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    Future.successful(Ok(views.html.domains(request.identity, (LDAPService.server.findDomains(request.identity.userID)))))
  }

  def organization(dn: String) = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    Future.successful(Ok(views.html.organization(request.identity, (LDAPService.server.findByDN[OrganizationUnit](request.identity.userID, dn)))))
  }

  def organizations = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    Future.successful(Ok(views.html.organizations(request.identity, (LDAPService.server.findOrganizations(request.identity.userID)))))
  }

  def computer(dn: String) = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    Future.successful(Ok(views.html.computer(request.identity, (LDAPService.server.findByDN[Computer](request.identity.userID, dn)))))
  }

  def computers = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    Future.successful(Ok(views.html.computers(request.identity, (LDAPService.server.findComputers(request.identity.userID)))))
  }

  def user(dn: String) = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    Future.successful(Ok(views.html.user("common.user" ,request.identity, (LDAPService.server.findByDN[ActiveDirectoryUser](request.identity.userID, dn)))))
  }

  def users = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    Future.successful(Ok(views.html.users(request.identity, (LDAPService.server.findUsers(request.identity.userID)))))
  }

}
