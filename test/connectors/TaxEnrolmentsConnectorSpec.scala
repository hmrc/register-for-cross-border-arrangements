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
import com.github.tomakehurst.wiremock.client.WireMock.{aResponse, put, urlEqualTo}
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import generators.Generators
import helpers.WireMockServerHandler
import models.EnrolmentRequest.SubscriptionInfo
import models.Verifier
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.Application
import play.api.http.Status._
import play.api.inject.guice.GuiceApplicationBuilder

import scala.concurrent.ExecutionContext.Implicits.global

class TaxEnrolmentsConnectorSpec extends SpecBase
  with WireMockServerHandler
  with Generators
  with ScalaCheckPropertyChecks {

  override lazy val app: Application = new GuiceApplicationBuilder()
    .configure(
      conf = "microservice.services.tax-enrolments.port" -> server.port()
    )
    .build()

  lazy val connector: TaxEnrolmentsConnector = app.injector.instanceOf[TaxEnrolmentsConnector]
  val dac6Id = "XNDAC0000000025"


  val enrolmentInfo = SubscriptionInfo(safeID = "safeId", saUtr = Some("utr"), dac6Id = dac6Id)

  "TaxEnrolmentsConnector" - {

    "createEnrolment" - {

      "must return status as 204 for successful Tax Enrolment call" in {
        forAll(validSafeID, validSubscriptionID, validUtr) {
          (safeID, subID, utr) =>

            val enrolmentInfo = SubscriptionInfo(safeID = safeID, saUtr = Some(utr), dac6Id = subID)

            stubResponseForPutRequest("/tax-enrolments/service/HMRC-DAC6-ORG/enrolment", NO_CONTENT)

            val result = connector.createEnrolment(enrolmentInfo)
            result.futureValue.status mustBe NO_CONTENT
        }
      }

      "must return status as 401 for unauthorized request" in {

        forAll(validSafeID, validSubscriptionID, validUtr) {
          (safeID, subID, utr) =>

            val enrolmentInfo = SubscriptionInfo(safeID = safeID, saUtr = Some(utr), dac6Id = subID)

            stubResponseForPutRequest("/tax-enrolments/service/HMRC-DAC6-ORG/enrolment", UNAUTHORIZED)

            val result = connector.createEnrolment(enrolmentInfo)
            result.futureValue.status mustBe UNAUTHORIZED
        }
      }

      "must return status as 400 for a bad request" in {

        forAll(validSafeID, validSubscriptionID, validUtr) {
          (safeID, subID, utr) =>

            val enrolmentInfo = SubscriptionInfo(safeID = safeID, saUtr = Some(utr), dac6Id = subID)
            stubResponseForPutRequest("/tax-enrolments/service/HMRC-DAC6-ORG/enrolment", BAD_REQUEST)

            val result = connector.createEnrolment(enrolmentInfo)
            result.futureValue.status mustBe BAD_REQUEST
        }
      }

      "must return status as 503 for unsuccessful Tax Enrolment call" in {
        forAll(validSafeID, validSubscriptionID, validUtr) {
          (safeID, subID, utr) =>

            val enrolmentInfo = SubscriptionInfo(safeID = safeID, saUtr = Some(utr), dac6Id = subID)
            stubResponseForPutRequest("/tax-enrolments/service/HMRC-DAC6-ORG/enrolment", SERVICE_UNAVAILABLE)

            val result = connector.createEnrolment(enrolmentInfo)
            result.futureValue.status mustBe SERVICE_UNAVAILABLE
        }
      }
    }

    "createEnrolmentRequest" - {

      "must return correct EnrolmentRequest nino provided" in {
        forAll(validSafeID, validSubscriptionID, validNino) {
          (safeID, subID, nino) =>

            val enrolmentInfo = SubscriptionInfo(safeID = safeID, nino = Some(nino), dac6Id = subID)


            val expectedVerifiers = Seq(Verifier("SAFEID", enrolmentInfo.safeID),
              Verifier("NINO", enrolmentInfo.nino.get))


            enrolmentInfo.convertToEnrolmentRequest.verifiers mustBe expectedVerifiers

        }
      }
     "must return correct EnrolmentRequest when saUtr provided as verifier" in {

       forAll(validSafeID, validSubscriptionID, validUtr) {
         (safeID, subID, utr) =>
           val enrolmentInfo = SubscriptionInfo(safeID = safeID,
             saUtr = Some(utr), dac6Id = subID)

           val expectedVerifiers = Seq(Verifier("SAFEID", enrolmentInfo.safeID),
             Verifier("SAUTR", enrolmentInfo.saUtr.get))


           enrolmentInfo.convertToEnrolmentRequest.verifiers mustBe expectedVerifiers
       }
     }

      "must return correct EnrolmentRequest when ctUtr provided as verifier" in {

        forAll(validSafeID, validSubscriptionID, validUtr) {
          (safeID, subID, utr) =>
            val enrolmentInfo = SubscriptionInfo(safeID = safeID,
              ctUtr = Some(utr), dac6Id = subID)

            val expectedVerifiers = Seq(Verifier("SAFEID", enrolmentInfo.safeID),
              Verifier("CTUTR", enrolmentInfo.ctUtr.get))


            enrolmentInfo.convertToEnrolmentRequest.verifiers mustBe expectedVerifiers
        }
      }
    }
  }
  private def stubResponseForPutRequest(expectedUrl: String, expectedStatus: Int): StubMapping =
    server.stubFor(
      put(urlEqualTo(expectedUrl))
        .willReturn(
          aResponse()
            .withStatus(expectedStatus)
        )
    )
}

