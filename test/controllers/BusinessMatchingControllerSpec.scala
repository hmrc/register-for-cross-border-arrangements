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
import connectors.BusinessMatchingConnector
import generators.Generators
import models.{BusinessMatchingSubmission, IndividualMatchingSubmission, Utr}
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

class BusinessMatchingControllerSpec extends SpecBase
  with MockitoSugar
  with Generators
  with ScalaCheckPropertyChecks {

  val mockBusinessMatchingConnector: BusinessMatchingConnector = mock[BusinessMatchingConnector]
  val application: Application = new GuiceApplicationBuilder()
      .configure(Configuration("metrics.enabled" -> "false"))
      .overrides(
        bind[BusinessMatchingConnector].toInstance(mockBusinessMatchingConnector)
      ).build()

  "Business Matching Controller" - {
    "for an individual match" - {
      "should return a found business partner match when one is found" in {
        when(mockBusinessMatchingConnector.sendIndividualMatchingInformation(any(), any())(any(), any()))
          .thenReturn(Future.successful(HttpResponse(200, Json.obj(), Map.empty[String, Seq[String]])))

        forAll(arbitrary[Nino], arbitrary[IndividualMatchingSubmission]) {
          (nino, individualMatchingSubmission) =>
            val request =
              FakeRequest(POST, routes.BusinessMatchingController.individualMatchingSubmission(nino).url)
                .withJsonBody(Json.toJson(individualMatchingSubmission))

            val result = route(application, request).value
            status(result) mustEqual OK
        }
      }

      "should return not found when one is not found" in {
        when(mockBusinessMatchingConnector.sendIndividualMatchingInformation(any(), any())(any(), any()))
          .thenReturn(Future.successful(HttpResponse(404, Json.obj(), Map.empty[String, Seq[String]])))

        forAll(arbitrary[Nino], arbitrary[IndividualMatchingSubmission]) {
          (nino, individualMatchingSubmission) =>
            val request =
              FakeRequest(POST, routes.BusinessMatchingController.individualMatchingSubmission(nino).url)
                .withJsonBody(Json.toJson(individualMatchingSubmission))

            val result = route(application, request).value
            status(result) mustEqual NOT_FOUND
        }
      }

      "should return authorisation errors when one is encountered" in {
        when(mockBusinessMatchingConnector.sendIndividualMatchingInformation(any(), any())(any(), any()))
          .thenReturn(Future.successful(HttpResponse(401, Json.obj(), Map.empty[String, Seq[String]])))

        forAll(arbitrary[Nino], arbitrary[IndividualMatchingSubmission]) {
          (nino, individualMatchingSubmission) =>
            val request =
              FakeRequest(POST, routes.BusinessMatchingController.individualMatchingSubmission(nino).url)
                .withJsonBody(Json.toJson(individualMatchingSubmission))

            val result = route(application, request).value
            status(result) mustEqual UNAUTHORIZED
        }
      }

      "should return bad request when one is encountered" in {
        when(mockBusinessMatchingConnector.sendIndividualMatchingInformation(any(), any())(any(), any()))
          .thenReturn(Future.successful(HttpResponse(400, Json.obj(), Map.empty[String, Seq[String]])))

        forAll(arbitrary[Nino], arbitrary[IndividualMatchingSubmission]) {
          (nino, individualMatchingSubmission) =>
            val request =
              FakeRequest(POST, routes.BusinessMatchingController.individualMatchingSubmission(nino).url)
                .withJsonBody(Json.toJson(individualMatchingSubmission))

            val result = route(application, request).value
            status(result) mustEqual BAD_REQUEST
        }
      }

      "should return gateway timeout when one is encountered" in {
        when(mockBusinessMatchingConnector.sendIndividualMatchingInformation(any(), any())(any(), any()))
          .thenReturn(Future.successful(HttpResponse(504, Json.obj(), Map.empty[String, Seq[String]])))

        forAll(arbitrary[Nino], arbitrary[IndividualMatchingSubmission]) {
          (nino, individualMatchingSubmission) =>
            val request =
              FakeRequest(POST, routes.BusinessMatchingController.individualMatchingSubmission(nino).url)
                .withJsonBody(Json.toJson(individualMatchingSubmission))

            val result = route(application, request).value
            status(result) mustEqual GATEWAY_TIMEOUT
        }
      }
    }

    "for a business match" - {
      "should return a found business partner match when one is found" in {
        when(mockBusinessMatchingConnector.sendBusinessMatchingInformation(any(), any())(any(), any()))
          .thenReturn(Future.successful(HttpResponse(200, Json.obj(), Map.empty[String, Seq[String]])))

        forAll(arbitrary[Utr], arbitrary[BusinessMatchingSubmission]) {
          (utr, businessMatchingSubmission) =>
            val request =
              FakeRequest(POST, routes.BusinessMatchingController.businessMatchingSubmission(utr.value).url)
                .withJsonBody(Json.toJson(businessMatchingSubmission))

            val result = route(application, request).value
            status(result) mustEqual OK
        }
      }
    }
  }

}
