/*
 * Copyright 2023 HM Revenue & Customs
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
import models.CreateSubscriptionForDACRequest
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.Application
import play.api.http.Status.{BAD_REQUEST, INTERNAL_SERVER_ERROR, OK, SERVICE_UNAVAILABLE}
import play.api.inject.guice.GuiceApplicationBuilder

import scala.concurrent.ExecutionContext.Implicits.global

class SubscriptionConnectorSpec extends SpecBase with WireMockServerHandler with Generators with ScalaCheckPropertyChecks {

  override lazy val app: Application = new GuiceApplicationBuilder()
    .configure(
      conf = "microservice.services.registration.port" -> server.port()
    )
    .build()

  lazy val connector: SubscriptionConnector = app.injector.instanceOf[SubscriptionConnector]

  "SubscriptionConnector" - {
    "for a create DAC6 subscription submission" - {
      "must return status OK for valid subscription details" in {

        forAll(arbitrary[CreateSubscriptionForDACRequest]) {
          sub =>
            stubResponse("/register-for-cross-border-arrangement-stubs/dac6/dct03/v1", OK)

            val result = connector.sendSubscriptionInformation(sub)
            result.futureValue.status mustBe OK
        }
      }

      "must return status BAD_REQUEST for invalid subscription details" in {

        forAll(arbitrary[CreateSubscriptionForDACRequest]) {
          sub =>
            stubResponse("/register-for-cross-border-arrangement-stubs/dac6/dct03/v1", BAD_REQUEST)

            val result = connector.sendSubscriptionInformation(sub)
            result.futureValue.status mustBe BAD_REQUEST
        }
      }

      "must return status INTERNAL_SERVER_ERROR for technical error" in {

        forAll(arbitrary[CreateSubscriptionForDACRequest]) {
          sub =>
            stubResponse("/register-for-cross-border-arrangement-stubs/dac6/dct03/v1", INTERNAL_SERVER_ERROR)

            val result = connector.sendSubscriptionInformation(sub)
            result.futureValue.status mustBe INTERNAL_SERVER_ERROR
        }
      }

      "must return status SERVICE_UNAVAILABLE for unexpected technical issue" in {

        forAll(arbitrary[CreateSubscriptionForDACRequest]) {
          sub =>
            stubResponse("/register-for-cross-border-arrangement-stubs/dac6/dct03/v1", SERVICE_UNAVAILABLE)

            val result = connector.sendSubscriptionInformation(sub)
            result.futureValue.status mustBe SERVICE_UNAVAILABLE
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
