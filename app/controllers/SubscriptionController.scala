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
import models.CreateSubscriptionForDACRequest
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
      case BAD_REQUEST => BadRequest(httpResponse.body)
      case FORBIDDEN => Forbidden(httpResponse.body)
      case SERVICE_UNAVAILABLE => ServiceUnavailable(httpResponse.body)
      case _ => InternalServerError(httpResponse.body)

    }
  }
}
