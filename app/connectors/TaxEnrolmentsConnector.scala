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
import models.EnrolmentRequest.EnrolmentInfo
import models.{EnrolmentRequest, Identifier, Verifier}
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

class TaxEnrolmentsConnector @Inject()(val config: AppConfig, val http: HttpClient) {

  def createEnrolment(enrolmentInfo: EnrolmentInfo)
                     (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[HttpResponse] = {

    val url = s"${config.taxEnrolmentsUrl}"

    val json = Json.toJson(createEnrolmentRequest(enrolmentInfo))
    http.PUT[JsValue, HttpResponse](url, json)
  }

  def createEnrolmentRequest(enrolmentInfo: EnrolmentInfo): EnrolmentRequest = {

    EnrolmentRequest(identifiers = Seq(Identifier("DAC6ID", enrolmentInfo.dac6UserID)),
      verifiers = buildVerifiers(enrolmentInfo))
  }


  private def buildVerifiers(enrolmentInfo: EnrolmentInfo): Seq[Verifier] = {

    val mandatoryVerifiers = Seq(Verifier("CONTACTNAME", enrolmentInfo.primaryContactName),
      Verifier("EMAIL", enrolmentInfo.primaryEmailAddress))

    mandatoryVerifiers ++
      buildOptionalVerifier(enrolmentInfo.primaryTelephoneNumber, "TELEPHONE") ++
      buildOptionalVerifier(enrolmentInfo.secondaryContactName, "SECCONTACTNAME") ++
      buildOptionalVerifier(enrolmentInfo.secondaryEmailAddress, "SECEMAIL") ++
      buildOptionalVerifier(enrolmentInfo.secondaryTelephoneNumber, "SECNUMBER") ++
      buildOptionalVerifier(enrolmentInfo.businessName, "BUSINESSNAME")

  }

  private def buildOptionalVerifier(optionalInfo: Option[String], key: String): Seq[Verifier] = {
    if (optionalInfo.isDefined) {
      Seq(Verifier(key, optionalInfo.get))
    } else Seq()

  }
}



