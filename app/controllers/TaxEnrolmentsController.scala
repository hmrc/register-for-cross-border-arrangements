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

import connectors.TaxEnrolmentsConnector
import controllers.auth.AuthAction
import javax.inject.Inject
import models.EnrolmentRequest.SubscriptionInfo
import play.api.libs.json.{JsResult, JsValue}
import play.api.mvc.{Action, ControllerComponents, Result}
import uk.gov.hmrc.http.HttpResponse
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import scala.concurrent.{ExecutionContext, Future}

class TaxEnrolmentsController @Inject()(
                                         authenticate: AuthAction,
                                         taxEnrolmentsConnector: TaxEnrolmentsConnector,
                                        override val controllerComponents: ControllerComponents)
                                       (implicit executionContext: ExecutionContext) extends
  BackendController(controllerComponents) {

  def createEnrolment: Action[JsValue] = authenticate(parse.json).async {
    implicit request =>
      val enrolmentInfoJs: JsResult[SubscriptionInfo] =
        request.body.validate[SubscriptionInfo]

      enrolmentInfoJs.fold(
        invalid = _ => Future.successful(BadRequest("malformed EnrolmentInfo")),
        valid = enrolmentInfo =>
          for {
            response <- taxEnrolmentsConnector.createEnrolment(enrolmentInfo)
            result = convertToResult(response)
          } yield result
      )
  }

  private def convertToResult(httpResponse: HttpResponse): Result = {
    httpResponse.status match {
      case OK => Ok(httpResponse.body)
      case NO_CONTENT => NoContent
      case NOT_FOUND => NotFound(httpResponse.body)
      case BAD_REQUEST => BadRequest(httpResponse.body)
      case UNAUTHORIZED => Unauthorized(httpResponse.body)
      case SERVICE_UNAVAILABLE => ServiceUnavailable(httpResponse.body)
      case BAD_GATEWAY => BadGateway(httpResponse.body)
      case GATEWAY_TIMEOUT => GatewayTimeout(httpResponse.body)
      case _ => InternalServerError(httpResponse.body)
    }
  }
}
