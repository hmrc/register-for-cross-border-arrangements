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

import com.google.inject.Inject
import config.AppConfig
import models.{EnrolmentRequest, Identifier, Verifier}
import play.api.Logger
import play.api.http.Status.{NO_CONTENT, OK}
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

class TaxEnrolmentsConnector @Inject()(val config: AppConfig, val http: HttpClient) {

  def createEnrolment(isInd: Boolean, postCode: String, utr: String, etmpSubscriptionId: String, isNiSource: Boolean)
                     (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[HttpResponse] = {

    val url = s"${config.taxEnrolmentsUrl}"
   // def utrType = if (isInd) SaUtrTypeKey else CtUtrTypeKey

    lazy val DAC6SubIdKey = "DAC6SUBID"
    lazy val PostCodeKey =  "POSTCODE"

    val json = Json.toJson(EnrolmentRequest(
      identifiers = Seq(Identifier(DAC6SubIdKey, etmpSubscriptionId)),
      verifiers = Seq(/*Verifier(utrType, utr), */Verifier(PostCodeKey, postCode))
    ))
    http.PUT[JsValue, HttpResponse](url, json)
  }
}