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

package connectors

import base.SpecBase
import com.github.tomakehurst.wiremock.client.WireMock.{aResponse, post, urlEqualTo}
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import generators.Generators
import helpers.WireMockServerHandler
import models.{PayloadRegisterWithID, Registration}
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.Application
import play.api.http.Status._
import play.api.inject.guice.GuiceApplicationBuilder

import scala.concurrent.ExecutionContext.Implicits.global

class RegistrationConnectorSpec extends SpecBase
  with WireMockServerHandler
  with Generators
  with ScalaCheckPropertyChecks {

  override lazy val app: Application = new GuiceApplicationBuilder()
    .configure(
      conf = "microservice.services.registration.port" -> server.port()
    )
    .build()

  lazy val connector: RegistrationConnector = app.injector.instanceOf[RegistrationConnector]

  "SubscriptionConnector" - {
    "for a registration without id submission" - {
      "must return status as OK for submission of Subscription" in {


        forAll(arbitrary[Registration]) {
          sub =>
            stubResponse("/register-for-cross-border-arrangement-stubs/dac6/dct01/v1", OK)

            val result = connector.sendWithoutIDInformation(sub)
            result.futureValue.status mustBe OK
        }
      }

      "must return status as BAD_REQUEST for submission of invalid subscription" in {


        forAll(arbitrary[Registration]) {
          sub =>
            stubResponse("/register-for-cross-border-arrangement-stubs/dac6/dct01/v1", BAD_REQUEST)

            val result = connector.sendWithoutIDInformation(sub)
            result.futureValue.status mustBe BAD_REQUEST
        }
      }

      "must return status as INTERNAL_SERVER_ERROR for submission for a technical error" in {


        forAll(arbitrary[Registration]) {
          sub =>
            stubResponse("/register-for-cross-border-arrangement-stubs/dac6/dct01/v1", INTERNAL_SERVER_ERROR)

            val result = connector.sendWithoutIDInformation(sub)
            result.futureValue.status mustBe INTERNAL_SERVER_ERROR
        }
      }
    }

    "for a registration with id submission" - {
      "must return status as OK for submission of Subscription" in {


        forAll(arbitrary[PayloadRegisterWithID]) {
          sub =>
            stubResponse("/register-for-cross-border-arrangement-stubs/dac6/dct02/v1", OK)

            val result = connector.sendWithID(sub)
            result.futureValue.status mustBe OK
        }
      }

      "must return status as BAD_REQUEST for submission of invalid subscription" in {


        forAll(arbitrary[PayloadRegisterWithID]) {
          sub =>
            stubResponse("/register-for-cross-border-arrangement-stubs/dac6/dct02/v1", BAD_REQUEST)

            val result = connector.sendWithID(sub)
            result.futureValue.status mustBe BAD_REQUEST
        }
      }

      "must return status as INTERNAL_SERVER_ERROR for submission for a technical error" in {


        forAll(arbitrary[PayloadRegisterWithID]) {
          sub =>
            stubResponse("/register-for-cross-border-arrangement-stubs/dac6/dct02/v1", INTERNAL_SERVER_ERROR)

            val result = connector.sendWithID(sub)
            result.futureValue.status mustBe INTERNAL_SERVER_ERROR
        }
      }
    }
  }

      private def stubResponse(expectedUrl: String, expectedStatus: Int): StubMapping =
        server.stubFor(
          post(urlEqualTo(expectedUrl))
            .willReturn(
              aResponse()
                .withStatus(expectedStatus)
            )
        )
}

