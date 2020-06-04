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
import models.EnrolmentRequest.EnrolmentInfo
import models.{EnrolmentRequest, Identifier, Verifier}
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

  val enrolmentInfo = EnrolmentInfo(dac6UserID = "id",
    businessName = None,
    primaryContactName = "name",
    primaryEmailAddress = "primaryEmail",
    primaryTelephoneNumber = None,
    secondaryContactName = None,
    secondaryEmailAddress = None,
    secondaryTelephoneNumber = None)

  "TaxEnrolmentsConnector" - {

    "createEnrolment" - {

      "must return status as 204 for successful Tax Enrolment call" in {

        stubResponseForPutRequest("/tax-enrolments/service/HMRC-DAC6-ORG/enrolment", NO_CONTENT)

        val result = connector.createEnrolment(enrolmentInfo)
        result.futureValue.status mustBe NO_CONTENT
      }

      "must return status as 401 for unauthorized request" in {

        stubResponseForPutRequest("/tax-enrolments/service/HMRC-DAC6-ORG/enrolment", UNAUTHORIZED)

        val result = connector.createEnrolment(enrolmentInfo)
        result.futureValue.status mustBe UNAUTHORIZED
      }

      "must return status as 400 for a bad request" in {

        stubResponseForPutRequest("/tax-enrolments/service/HMRC-DAC6-ORG/enrolment", BAD_REQUEST)

        val result = connector.createEnrolment(enrolmentInfo)
        result.futureValue.status mustBe BAD_REQUEST
      }

      "must return status as 503 for unsuccessful Tax Enrolment call" in {

        stubResponseForPutRequest("/tax-enrolments/service/HMRC-DAC6-ORG/enrolment", SERVICE_UNAVAILABLE)

        val result = connector.createEnrolment(enrolmentInfo)
        result.futureValue.status mustBe SERVICE_UNAVAILABLE
      }
    }
    "createEnrolmentRequest" - {

      "must return correct EnrolmentRequest when all values populated in EnrolmentInfo" in {

      val enrolmentInfo = EnrolmentInfo(dac6UserID = "id",
          primaryContactName = "priConName",
          primaryEmailAddress = "primaryEmail",
          primaryTelephoneNumber = Some("priConNumber"),
          secondaryContactName = Some("secConName"),
          secondaryEmailAddress = Some("secConEmail"),
          secondaryTelephoneNumber = Some("secConNumber"),
          businessName = Some("businessName"))


        val expectedVerifiers = Seq(Verifier("CONTACTNAME", "priConName"),
                                    Verifier("EMAIL", "primaryEmail"),
                                    Verifier("TELEPHONE", "priConNumber"),
                                    Verifier("SECCONTACTNAME", "secConName"),
                                    Verifier("SECEMAIL", "secConEmail"),
                                    Verifier("SECNUMBER", "secConNumber"),
                                    Verifier("BUSINESSNAME", "businessName"))

        val expectedEnrolmentRequest = EnrolmentRequest(identifiers = Seq(Identifier("DAC6ID", "id")),
                                                        verifiers = expectedVerifiers)

      enrolmentInfo.convertToEnrolmentRequest mustBe expectedEnrolmentRequest

      }

     "must return correct EnrolmentRequest when all only mandatory values populated in EnrolmentInfo" in {

      val enrolmentInfo = EnrolmentInfo(dac6UserID = "id",
                                        primaryContactName = "priConName",
                                        primaryEmailAddress = "primaryEmail")

        val expectedVerifiers = Seq(Verifier("CONTACTNAME", "priConName"),
                                    Verifier("EMAIL", "primaryEmail"))

        val expectedEnrolmentRequest = EnrolmentRequest(identifiers = Seq(Identifier("DAC6ID", "id")),
                                                        verifiers = expectedVerifiers)

       enrolmentInfo.convertToEnrolmentRequest mustBe expectedEnrolmentRequest
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

