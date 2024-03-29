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
import models.{BusinessMatchingSubmission, IndividualMatchingSubmission, Utr}
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.Application
import play.api.http.Status._
import play.api.inject.guice.GuiceApplicationBuilder
import uk.gov.hmrc.domain.Nino
import wolfendale.scalacheck.regexp.RegexpGen

import scala.concurrent.ExecutionContext.Implicits.global

class BusinessMatchingConnectorSpec extends SpecBase with WireMockServerHandler with Generators with ScalaCheckPropertyChecks {

  override lazy val app: Application = new GuiceApplicationBuilder()
    .configure(
      conf = "microservice.services.business-matching.port" -> server.port()
    )
    .build()

  lazy val connector: BusinessMatchingConnector = app.injector.instanceOf[BusinessMatchingConnector]

  "BusinessMatchingConnector" - {
    "for an individual matching submission" - {
      "must return status as OK for submission of valid Individual Matching Submission" in {

        forAll(arbitrary[Nino], arbitrary[IndividualMatchingSubmission]) {
          (nino, ims) =>
            stubResponse(s"/register-for-cross-border-arrangement-stubs/registration/individual/nino/$nino", OK)

            val result = connector.sendIndividualMatchingInformation(nino, ims)
            result.futureValue.status mustBe OK
        }
      }

      "must return status as BAD_REQUEST for submission of invalid arrival notification" in {

        forAll(arbitrary[Nino], arbitrary[IndividualMatchingSubmission]) {
          (nino, ims) =>
            stubResponse(s"/register-for-cross-border-arrangement-stubs/registration/individual/nino/$nino", BAD_REQUEST)

            val result = connector.sendIndividualMatchingInformation(nino, ims)
            result.futureValue.status mustBe BAD_REQUEST
        }
      }

      "must return status as INTERNAL_SERVER_ERROR for technical error incurred" in {

        forAll(arbitrary[Nino], arbitrary[IndividualMatchingSubmission]) {
          (nino, ims) =>
            stubResponse(s"/register-for-cross-border-arrangement-stubs/registration/individual/nino/$nino", INTERNAL_SERVER_ERROR)

            val result = connector.sendIndividualMatchingInformation(nino, ims)
            result.futureValue.status mustBe INTERNAL_SERVER_ERROR
        }
      }
    }

    "for a sole proprietor matching submission" - {
      "must return status as OK for submission of valid Sole Proprietor Matching Submission" in {

        forAll(RegexpGen.from(Utr.regex), arbitrary[BusinessMatchingSubmission]) {
          (utr, bms) =>
            stubResponse(s"/register-for-cross-border-arrangement-stubs/registration/individual/utr/$utr", OK)

            val result = connector.sendSoleProprietorMatchingInformation(utr, bms)
            result.futureValue.status mustBe OK
        }
      }

      "must return status as BAD_REQUEST for submission of invalid arrival notification" in {

        forAll(RegexpGen.from(Utr.regex), arbitrary[BusinessMatchingSubmission]) {
          (utr, bms) =>
            stubResponse(s"/register-for-cross-border-arrangement-stubs/registration/individual/utr/$utr", BAD_REQUEST)

            val result = connector.sendSoleProprietorMatchingInformation(utr, bms)
            result.futureValue.status mustBe BAD_REQUEST
        }
      }

      "must return status as INTERNAL_SERVER_ERROR for technical error incurred" in {

        forAll(RegexpGen.from(Utr.regex), arbitrary[BusinessMatchingSubmission]) {
          (utr, bms) =>
            stubResponse(s"/register-for-cross-border-arrangement-stubs/registration/individual/utr/$utr", INTERNAL_SERVER_ERROR)

            val result = connector.sendSoleProprietorMatchingInformation(utr, bms)
            result.futureValue.status mustBe INTERNAL_SERVER_ERROR
        }
      }
    }

    "for an business matching submission" - {
      "must return status as OK for submission of valid Business Matching Submission" in {

        forAll(RegexpGen.from(Utr.regex), arbitrary[BusinessMatchingSubmission]) {
          (utr, bms) =>
            stubResponse(s"/register-for-cross-border-arrangement-stubs/registration/organisation/utr/$utr", OK)

            val result = connector.sendBusinessMatchingInformation(utr, bms)
            result.futureValue.status mustBe OK
        }
      }

      "must return status as BAD_REQUEST for submission of invalid arrival notification" in {

        forAll(RegexpGen.from(Utr.regex), arbitrary[BusinessMatchingSubmission]) {
          (utr, bms) =>
            stubResponse(s"/register-for-cross-border-arrangement-stubs/registration/organisation/utr/$utr", BAD_REQUEST)

            val result = connector.sendBusinessMatchingInformation(utr, bms)
            result.futureValue.status mustBe BAD_REQUEST
        }
      }

      "must return status as INTERNAL_SERVER_ERROR for technical error incurred" in {

        forAll(RegexpGen.from(Utr.regex), arbitrary[BusinessMatchingSubmission]) {
          (utr, bms) =>
            stubResponse(s"/register-for-cross-border-arrangement-stubs/registration/organisation/utr/$utr", INTERNAL_SERVER_ERROR)

            val result = connector.sendBusinessMatchingInformation(utr, bms)
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
