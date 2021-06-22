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
import connectors.SubscriptionConnector
import controllers.auth.{AuthAction, FakeAuthAction}
import generators.Generators
import models.{CreateSubscriptionForDACRequest, ErrorDetail, ErrorDetails, SourceFaultDetail}
import org.joda.time.DateTime
import org.mockito.ArgumentMatchers.any
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers.{POST, route, status, _}
import play.api.{Application, Configuration}
import uk.gov.hmrc.http.HttpResponse

import scala.concurrent.Future

class SubscriptionControllerSpec extends SpecBase
  with Generators
  with ScalaCheckPropertyChecks {

  val mockSubscriptionConnector: SubscriptionConnector = mock[SubscriptionConnector]
  val application: Application = new GuiceApplicationBuilder()
    .configure(Configuration("metrics.enabled" -> "false"))
    .overrides(
      bind[SubscriptionConnector].toInstance(mockSubscriptionConnector),
      bind[AuthAction].to[FakeAuthAction]
    ).build()

  "SubscriptionController" - {
    "for user with subscription details" - {
      "should send data and return OK" in {

        when(mockSubscriptionConnector.sendSubscriptionInformation(any())(any(), any()))
          .thenReturn(Future.successful(HttpResponse(200, Json.obj(), Map.empty[String, Seq[String]])))

        forAll(arbitrary[CreateSubscriptionForDACRequest]) {
          (subscriptionForDACRequest) =>
            val request =
              FakeRequest(POST, routes.SubscriptionController.createSubscription().url)
                .withJsonBody(Json.toJson(subscriptionForDACRequest))

            val result = route(application, request).value
            status(result) mustEqual OK
        }
      }

      "should return BAD_REQUEST when subscriptionForDacRequest ia invalid" in {
        when(mockSubscriptionConnector.sendSubscriptionInformation(any())(any(),any()))
          .thenReturn(Future.successful(HttpResponse(400, Json.obj(), Map.empty[String, Seq[String]])))

            val request =
              FakeRequest(POST, routes.SubscriptionController.createSubscription().url)
                .withJsonBody(Json.parse("""{"value": "field"}"""))

            val result = route(application, request).value
            status(result) mustEqual BAD_REQUEST
        }
      }

      "should return BAD_REQUEST when one is encountered" in {
        when(mockSubscriptionConnector.sendSubscriptionInformation(any())(any(),any()))
          .thenReturn(Future.successful(HttpResponse(400, Json.obj(), Map.empty[String, Seq[String]])))

        forAll(arbitrary[CreateSubscriptionForDACRequest]) {
          (subscriptionForDacRequest) =>
            val request =
              FakeRequest(POST, routes.SubscriptionController.createSubscription().url)
              .withJsonBody(Json.toJson(subscriptionForDacRequest))

            val result = route(application, request).value
            status(result) mustEqual BAD_REQUEST
        }
      }

      "should return FORBIDDEN when authorisation is invalid" in {
        when(mockSubscriptionConnector.sendSubscriptionInformation(any())(any(),any()))
          .thenReturn(Future.successful(HttpResponse(403, Json.obj(), Map.empty[String, Seq[String]])))

        forAll(arbitrary[CreateSubscriptionForDACRequest]) {
          (subscriptionForDacRequest) =>
            val request =
              FakeRequest(POST, routes.SubscriptionController.createSubscription().url)
              .withJsonBody(Json.toJson(subscriptionForDacRequest))

            val result = route(application, request).value
            status(result) mustEqual FORBIDDEN
        }
      }

      "should return SERVICE_UNAVAILABLE when EIS becomes unavailable" in {
        when(mockSubscriptionConnector.sendSubscriptionInformation(any())(any(),any()))
          .thenReturn(Future.successful(HttpResponse(503, Json.obj(), Map.empty[String, Seq[String]])))

        forAll(arbitrary[CreateSubscriptionForDACRequest]) {
          (subscriptionForDacRequest) =>
            val request =
              FakeRequest(POST, routes.SubscriptionController.createSubscription().url)
              .withJsonBody(Json.toJson(subscriptionForDacRequest))

            val result = route(application, request).value
            status(result) mustEqual SERVICE_UNAVAILABLE
        }
      }

      "should return CONFLICT when one occurs" in {
        val errorDetails = ErrorDetails(ErrorDetail(DateTime.now().toString,"xx","409","CONFLICT","", Some(SourceFaultDetail(Seq("a","b")))))
        when(mockSubscriptionConnector.sendSubscriptionInformation(any())(any(),any()))
          .thenReturn(Future.successful(HttpResponse(409, Json.toJson(errorDetails), Map.empty[String, Seq[String]])))


        forAll(arbitrary[CreateSubscriptionForDACRequest]) {
          (subscriptionForDacRequest) =>
            val request =
              FakeRequest(POST, routes.SubscriptionController.createSubscription().url)
              .withJsonBody(Json.toJson(subscriptionForDacRequest))

            val result = route(application, request).value
            status(result) mustEqual CONFLICT
        }
      }

      "should return INTERNAL_SERVER_ERROR for unspecified errors" in {
        when(mockSubscriptionConnector.sendSubscriptionInformation(any())(any(),any()))
          .thenReturn(Future.successful(HttpResponse(404, Json.obj(), Map.empty[String, Seq[String]])))

        forAll(arbitrary[CreateSubscriptionForDACRequest]) {
          (subscriptionForDacRequest) =>
            val request =
              FakeRequest(POST, routes.SubscriptionController.createSubscription().url)
              .withJsonBody(Json.toJson(subscriptionForDacRequest))

            val result = route(application, request).value
            status(result) mustEqual INTERNAL_SERVER_ERROR
        }
      }

      "downstream errors should be recoverable when not in json" in {
        when(mockSubscriptionConnector.sendSubscriptionInformation(any())(any(),any()))
          .thenReturn(Future.successful(HttpResponse(503, "Not Available", Map.empty[String, Seq[String]])))

        forAll(arbitrary[CreateSubscriptionForDACRequest]) {
          (subscriptionForDacRequest) =>
            val request =
              FakeRequest(POST, routes.SubscriptionController.createSubscription().url)
                .withJsonBody(Json.toJson(subscriptionForDacRequest))

            val result = route(application, request).value
            status(result) mustEqual SERVICE_UNAVAILABLE
        }
      }
    }
  }


