package connectors

import base.SpecBase
import com.github.tomakehurst.wiremock.client.WireMock.{aResponse, put, urlEqualTo}
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import generators.Generators
import helpers.WireMockServerHandler
import models.EnrolmentRequest
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

  "TaxEnrolmentsConnector" - {

    "must return status as 204 for successful Tax Enrolment call" in {

      val enrolmentRequest = EnrolmentRequest(Seq(), Seq())

         stubResponseForPutRequest("/tax-enrolments/service/HMRC-DAC6-ORG/enrolment", NO_CONTENT)

          val result = connector.createEnrolment(isInd = true, postCode = "", etmpSubscriptionId = "",
            utr = "", isNiSource = true)
          result.futureValue mustBe NO_CONTENT
    }

    "must return status as 503 for unsuccessful Tax Enrolment call" in {

      val enrolmentRequest = EnrolmentRequest(Seq(), Seq())

         stubResponseForPutRequest("/tax-enrolments/service/HMRC-DAC6-ORG/enrolment", SERVICE_UNAVAILABLE)

          val result = connector.createEnrolment(isInd = true, postCode = "", etmpSubscriptionId = "",
            utr = "", isNiSource = true)
          result.futureValue mustBe SERVICE_UNAVAILABLE
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

