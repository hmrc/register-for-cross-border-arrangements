/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers

import config.AppConfig
import connectors.SubscriptionConnector
import javax.inject.Inject
import models.{CreateSubscriptionForDACRequest, ErrorDetails}
import play.api.Logger
import play.api.libs.json.{JsError, JsResult, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, ControllerComponents, Result}
import uk.gov.hmrc.http.HttpResponse
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import scala.concurrent.{ExecutionContext, Future}

class SubscriptionController @Inject()(
  val config: AppConfig,
  subscriptionConnector: SubscriptionConnector,
  override val controllerComponents: ControllerComponents
)(implicit executionContext: ExecutionContext) extends  BackendController(controllerComponents) {

  private val logger: Logger = Logger(this.getClass)

  def createSubscription: Action[JsValue] = Action(parse.json).async {
    implicit request =>
      val subscriptionSubmissionResult: JsResult[CreateSubscriptionForDACRequest] =
        request.body.validate[CreateSubscriptionForDACRequest]

      subscriptionSubmissionResult.fold(
        invalid = _ => Future.successful(BadRequest("")),
        valid = sub =>
          for {
          response <- subscriptionConnector.sendSubscriptionInformation(sub)
          } yield
            convertToResult(response)
      )
  }

  private def convertToResult(httpResponse: HttpResponse): Result = {
    httpResponse.status match {
      case OK => Ok(httpResponse.body)

      case BAD_REQUEST => {
        val error = Json.parse(httpResponse.body).validate[ErrorDetails]
        error match {
          case JsSuccess(value, _) =>
            logger.error(s"Error with submission: ${value.errorDetail.sourceFaultDetail.map(_.detail.mkString).getOrElse("")}")
          case JsError(errors) => logger.error("Error with submission but return is not a valid json")
        }
        BadRequest(httpResponse.body)
      }

      case FORBIDDEN => {
        val error = Json.parse(httpResponse.body).validate[ErrorDetails]
        error match {
          case JsSuccess(value, _) =>
            logger.error(s"Error with authenticating submission: ${value.errorDetail.sourceFaultDetail.map(_.detail.mkString).getOrElse("")}")
          case JsError(errors) => logger.error("Error with authenticating submission but return is not a valid json")
        }
        Forbidden(httpResponse.body)
      }

      case SERVICE_UNAVAILABLE => {
        val error = Json.parse(httpResponse.body).validate[ErrorDetails]
        error match {
          case JsSuccess(value, _) =>
            logger.error(s"Error with submission: ${value.errorDetail.sourceFaultDetail.map(_.detail.mkString).getOrElse("")}")
          case JsError(errors) => logger.error("Error with submission but return is not a valid json")
        }
        ServiceUnavailable(httpResponse.body)
      }

      case _ => {
        val error = Json.parse(httpResponse.body).validate[ErrorDetails]
        error match {
          case JsSuccess(value, _) =>
            logger.error(s"Error with responding to submission: ${value.errorDetail.sourceFaultDetail.map(_.detail.mkString).getOrElse("")}")
          case JsError(errors) => logger.error("Error with responding to submission but return is not a valid json")
        }
        InternalServerError(httpResponse.body)
      }

    }
  }
}
