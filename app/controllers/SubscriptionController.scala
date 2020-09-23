package controllers

import config.AppConfig
import connectors.SubscriptionConnector
import javax.inject.Inject
import models.SubscriptionForDACRequest
import play.api.libs.json.{JsResult, JsValue}
import play.api.mvc.{Action, ControllerComponents, Result}
import uk.gov.hmrc.http.HttpResponse
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import scala.concurrent.{ExecutionContext, Future}

class SubscriptionController @Inject()(
  val config: AppConfig,
  subscriptionConnector: SubscriptionConnector,
  override val controllerComponents: ControllerComponents
)(implicit executionContext: ExecutionContext) extends  BackendController(controllerComponents) {

  def subscriptionSubmission: Action[JsValue] = Action(parse.json).async {
    implicit request =>
      val subscriptionSubmissionResult: JsResult[SubscriptionForDACRequest] =
        request.body.validate[SubscriptionForDACRequest]

      //TODO - add businessResponseError NOT_OK

      subscriptionSubmissionResult.fold(
        invalid = _ => Future.successful(BadRequest("")),
        valid = sub =>
          for {
          response <- subscriptionConnector.sendSubscriptionInformation(sub)
          result = convertToResult(response)
          } yield result
      )
  }

  private def convertToResult(httpResponse: HttpResponse): Result = {
    httpResponse.status match {
      case OK => Ok(httpResponse.body)
      case BAD_REQUEST => BadRequest(httpResponse.body)
      case FORBIDDEN => Forbidden(httpResponse.body)
      case NOT_FOUND => NotFound(httpResponse.body)
      case METHOD_NOT_ALLOWED => MethodNotAllowed(httpResponse.body)
      case _ => InternalServerError(httpResponse.body)

    }
  }
}
