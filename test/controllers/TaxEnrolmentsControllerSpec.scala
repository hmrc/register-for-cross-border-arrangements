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

import base.SpecBase
import connectors.{BusinessMatchingConnector, TaxEnrolmentsConnector}
import generators.Generators
import models.EnrolmentRequest.EnrolmentInfo
import models.{EnrolmentRequest, IndividualMatchingSubmission}
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers.{POST, route, status, _}
import play.api.{Application, Configuration}
import uk.gov.hmrc.domain.Nino
import uk.gov.hmrc.http.HttpResponse

import scala.concurrent.Future

class TaxEnrolmentsControllerSpec extends SpecBase
  with MockitoSugar
  with Generators
  with ScalaCheckPropertyChecks {

  val mockTaxEnrolmentsConnector: TaxEnrolmentsConnector = mock[TaxEnrolmentsConnector]
  val application: Application = new GuiceApplicationBuilder()
    .configure(Configuration("metrics.enabled" -> "false"))
    .overrides(
      bind[TaxEnrolmentsConnector].toInstance(mockTaxEnrolmentsConnector)
    ).build()

  "Business Matching Controller" - {
    "should return a found business partner match when one is found" in {
      when(mockTaxEnrolmentsConnector.createEnrolment(any(), any(), any(), any(), any())(any(), any()))
        .thenReturn(Future.successful(HttpResponse(200, responseJson = Some(Json.obj()))))

      forAll(arbitrary[EnrolmentInfo]){
        (enrolmentInfo) =>
          val request =
            FakeRequest(PUT, routes.TaxEnrolmentsController.createEnrolment().url)
              .withJsonBody(Json.toJson(enrolmentInfo))

          val result  = route(application, request).value
          status(result) mustEqual OK
      }
    }


    "should return authorisation errors when one is encountered" in {
      when(mockTaxEnrolmentsConnector.createEnrolment(any(), any(), any(), any(), any())(any(), any()))
        .thenReturn(Future.successful(HttpResponse(401, responseJson = Some(Json.obj()))))

      forAll(arbitrary[EnrolmentInfo]){
        (enrolmentInfo) =>
          val request =
            FakeRequest(PUT, routes.TaxEnrolmentsController.createEnrolment().url)
              .withJsonBody(Json.toJson(enrolmentInfo))

          val result  = route(application, request).value
          status(result) mustEqual UNAUTHORIZED
      }
    }

    "should return bad request when one is encountered" in {
      when(mockTaxEnrolmentsConnector.createEnrolment(any(), any(), any(), any(), any())(any(), any()))
        .thenReturn(Future.successful(HttpResponse(400, responseJson = Some(Json.obj()))))

      forAll(arbitrary[EnrolmentInfo]){
        (enrolmentInfo) =>
          val request =
            FakeRequest(PUT, routes.TaxEnrolmentsController.createEnrolment().url)
              .withJsonBody(Json.toJson(enrolmentInfo))

          val result  = route(application, request).value
          status(result) mustEqual BAD_REQUEST
      }
    }

    "should return gateway timeout when one is encountered" in {
      when(mockTaxEnrolmentsConnector.createEnrolment(any(), any(), any(), any(), any())(any(), any()))
        .thenReturn(Future.successful(HttpResponse(504, responseJson = Some(Json.obj()))))

      forAll(arbitrary[EnrolmentInfo]){
        (enrolmentInfo) =>
          val request =
            FakeRequest(PUT, routes.TaxEnrolmentsController.createEnrolment().url)
              .withJsonBody(Json.toJson(enrolmentInfo))

          val result  = route(application, request).value
          status(result) mustEqual GATEWAY_TIMEOUT
      }
    }
  }

}
