/*
 * Copyright 2021 HM Revenue & Customs
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

import connectors.BusinessMatchingConnector
import controllers.auth.AuthAction
import javax.inject.Inject
import models.{BusinessMatchingSubmission, ErrorDetails, IndividualMatchingSubmission}
import play.api.Logger
import play.api.libs.json.{JsResult, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, ControllerComponents, Result}
import uk.gov.hmrc.domain.Nino
import uk.gov.hmrc.http.HttpResponse
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Success, Try}

class BusinessMatchingController @Inject()(
                                            authenticate: AuthAction,
                                            businessMatchingConnector: BusinessMatchingConnector,
                                            override val controllerComponents: ControllerComponents
                                          )
                                          (implicit executionContext: ExecutionContext) extends
  BackendController(controllerComponents) {

  private val logger: Logger = Logger(this.getClass)

  def individualMatchingSubmission(nino: Nino): Action[JsValue] = authenticate(parse.json).async {
    implicit request =>
      val individualMatchingSubmissionResult: JsResult[IndividualMatchingSubmission] =
        request.body.validate[IndividualMatchingSubmission]

      individualMatchingSubmissionResult.fold(
        invalid = _ => Future.successful(BadRequest("")),
        valid = ims =>
          for {
            response <- businessMatchingConnector.sendIndividualMatchingInformation(nino, ims)
            result = convertToResult(response)
          } yield result
      )
  }

  def soleProprietorMatchingSubmission(utr: String): Action[JsValue] = authenticate(parse.json).async {
    implicit request =>
      //Note: ETMP data suggests sole trader business partner accounts are individual records
      val soleProprietorMatchingSubmission: JsResult[BusinessMatchingSubmission] =
        request.body.validate[BusinessMatchingSubmission]

      soleProprietorMatchingSubmission.fold(
        invalid = _ => Future.successful(BadRequest("")),
        valid = bms =>
          for {
            response <- businessMatchingConnector.sendSoleProprietorMatchingInformation(utr, bms)
            result = convertToResult(response)
          } yield result
      )
  }

  def businessMatchingSubmission(utr: String): Action[JsValue] = authenticate(parse.json).async {
    implicit request =>
      val businessMatchingSubmissionResult: JsResult[BusinessMatchingSubmission] =
        request.body.validate[BusinessMatchingSubmission]

      businessMatchingSubmissionResult.fold(
        invalid = _ => Future.successful(BadRequest("")),
        valid = bms =>
          for {
            response <- businessMatchingConnector.sendBusinessMatchingInformation(utr, bms)
            result = convertToResult(response)
          } yield result
      )
  }

  private def convertToResult(httpResponse: HttpResponse): Result = {
    httpResponse.status match {
      case OK => Ok(httpResponse.body)
      case BAD_REQUEST => {
       logDownStreamError(httpResponse.body)

        BadRequest(httpResponse.body)
      }
      case FORBIDDEN => {
        logDownStreamError(httpResponse.body)

        Forbidden(httpResponse.body)
      }
      case _ => {
        logDownStreamError(httpResponse.body)

        InternalServerError(httpResponse.body)
      }
    }
  }

  private def logDownStreamError(body: String): Unit = {
    val error = Try(Json.parse(body).validate[ErrorDetails])
    error match {
      case Success(JsSuccess(value, _)) =>
        logger.error(s"Error with submission: ${value.errorDetail.sourceFaultDetail.map(_.detail.mkString)}")
      case _ => logger.error("Error with submission but return is not a valid json")
    }
  }
}
