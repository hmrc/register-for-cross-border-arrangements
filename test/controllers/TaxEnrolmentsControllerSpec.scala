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

import base.SpecBase
import connectors.TaxEnrolmentsConnector
import controllers.auth.{AuthAction, FakeAuthAction}
import generators.Generators
import models.EnrolmentRequest.SubscriptionInfo
import org.mockito.ArgumentMatchers.any
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers.{route, status, _}
import play.api.{Application, Configuration}
import uk.gov.hmrc.http.HttpResponse

import scala.concurrent.Future

class TaxEnrolmentsControllerSpec extends SpecBase
  with Generators
  with ScalaCheckPropertyChecks {

  val mockTaxEnrolmentsConnector: TaxEnrolmentsConnector = mock[TaxEnrolmentsConnector]
  val application: Application = new GuiceApplicationBuilder()
    .configure(Configuration("metrics.enabled" -> "false"))
    .overrides(
      bind[TaxEnrolmentsConnector].toInstance(mockTaxEnrolmentsConnector),
      bind[AuthAction].to[FakeAuthAction]
    ).build()

  "Business Matching Controller" - {
    "should return a found business partner match when one is found" in {
      when(mockTaxEnrolmentsConnector.createEnrolment(any())(any(), any()))
        .thenReturn(Future.successful(HttpResponse(200, Json.obj(), Map.empty[String, Seq[String]])))

      forAll(arbitrary[SubscriptionInfo]){
        (enrolmentInfo) =>
          val request =
            FakeRequest(PUT, routes.TaxEnrolmentsController.createEnrolment().url)
              .withJsonBody(Json.toJson(enrolmentInfo))

          val result  = route(application, request).value
          status(result) mustEqual OK
      }
    }

  "should return error when invalid json encountered" in {

      when(mockTaxEnrolmentsConnector.createEnrolment(any())(any(), any()))
        .thenReturn(Future.successful(HttpResponse(200, Json.obj(), Map.empty[String, Seq[String]])))

          val request =
            FakeRequest(PUT, routes.TaxEnrolmentsController.createEnrolment().url)
              .withJsonBody(Json.parse("""{"field": "value"}"""))

          val result  = route(application, request).value
          status(result) mustEqual BAD_REQUEST
  }

    "should handle range of expected http responses" in {
      val expectedResponses = List(NO_CONTENT , NOT_FOUND , BAD_REQUEST, UNAUTHORIZED , SERVICE_UNAVAILABLE, BAD_GATEWAY, GATEWAY_TIMEOUT, INTERNAL_SERVER_ERROR )

      expectedResponses foreach  { responseStatus =>

        when(mockTaxEnrolmentsConnector.createEnrolment(any())(any(), any()))
        .thenReturn(Future.successful(HttpResponse(responseStatus, Json.obj(), Map.empty[String, Seq[String]])))

        forAll(arbitrary[SubscriptionInfo]){
          (enrolmentInfo) =>
            val request =
              FakeRequest(PUT, routes.TaxEnrolmentsController.createEnrolment().url)
                .withJsonBody(Json.toJson(enrolmentInfo))

            val result  = route(application, request).value
            status(result) mustEqual responseStatus
        }
      }
    }
  }

}
