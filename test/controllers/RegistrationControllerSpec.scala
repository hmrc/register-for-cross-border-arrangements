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
import connectors.RegistrationConnector
import generators.Generators
import models.Registration
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
import uk.gov.hmrc.http.HttpResponse

import scala.concurrent.Future

class RegistrationControllerSpec extends SpecBase
  with MockitoSugar
  with Generators
  with ScalaCheckPropertyChecks {

  val mockRegistrationConnector: RegistrationConnector = mock[RegistrationConnector]
  val application: Application = new GuiceApplicationBuilder()
    .configure(Configuration("metrics.enabled" -> "false"))
    .overrides(
      bind[RegistrationConnector].toInstance(mockRegistrationConnector)
    ).build()

  "Registration Controller" - {
    "for a user without id" - {
      "should send data and return ok" in {
        when(mockRegistrationConnector.sendWithoutIDInformation(any())(any(), any()))
          .thenReturn(Future.successful(HttpResponse(200, Json.obj(), Map.empty[String, Seq[String]])))

        forAll(arbitrary[Registration]) {
          individualNoIdRegistration =>
            val request =
              FakeRequest(POST, routes.RegistrationController.noIdRegistration().url)
                .withJsonBody(Json.toJson(individualNoIdRegistration))

            val result = route(application, request).value
            status(result) mustEqual OK
        }
      }

      "should return bad request when one is encountered" in {
        when(mockRegistrationConnector.sendWithoutIDInformation(any())(any(), any()))
          .thenReturn(Future.successful(HttpResponse(400, Json.obj(), Map.empty[String, Seq[String]])))

        forAll(arbitrary[Registration]) {
          individualNoIdSubscription =>
            val request =
              FakeRequest(POST, routes.RegistrationController.noIdRegistration.url)
                .withJsonBody(Json.toJson(individualNoIdSubscription))

            val result = route(application, request).value
            status(result) mustEqual BAD_REQUEST
        }
      }

      "should return forbidden error when authorisation is invalid" in {
        when(mockRegistrationConnector.sendWithoutIDInformation(any())(any(), any()))
          .thenReturn(Future.successful(HttpResponse(403, Json.obj(), Map.empty[String, Seq[String]])))

        forAll(arbitrary[Registration]) {
          individualNoIdRegistration =>
            val request =
              FakeRequest(POST, routes.RegistrationController.noIdRegistration.url)
                .withJsonBody(Json.toJson(individualNoIdRegistration))

            val result = route(application, request).value
            status(result) mustEqual FORBIDDEN
        }
      }
    }
  }
}
