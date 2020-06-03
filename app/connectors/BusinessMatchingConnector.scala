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

import java.util.UUID

import config.AppConfig
import javax.inject.Inject
import models.{BusinessMatchingSubmission, IndividualMatchingSubmission}
import uk.gov.hmrc.domain.Nino
import uk.gov.hmrc.http.logging.Authorization
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

class BusinessMatchingConnector @Inject()(val config: AppConfig, val http: HttpClient) {

  def sendIndividualMatchingInformation(nino: Nino, individualSubmission: IndividualMatchingSubmission)
                                       (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[HttpResponse] = {
    val submissionUrl = s"${config.businessMatchingUrl}/registration/individual/nino/$nino"

    val newHeaders = hc
      .copy(authorization = Some(Authorization(s"Bearer ${config.desBearerToken}")))
      .withExtraHeaders(addHeaders(): _*)

    http.POST[IndividualMatchingSubmission, HttpResponse](submissionUrl, individualSubmission)(wts = IndividualMatchingSubmission.format, rds = httpReads, hc = newHeaders, ec = ec)
  }

  def sendBusinessMatchingInformation(utr: String, businessSubmission: BusinessMatchingSubmission)
                                       (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[HttpResponse] = {
    val submissionUrl = s"${config.businessMatchingUrl}/registration/organisation/utr/$utr"

    val newHeaders = hc
      .copy(authorization = Some(Authorization(s"Bearer ${config.desBearerToken}")))
      .withExtraHeaders(addHeaders(): _*)

    http.POST[BusinessMatchingSubmission, HttpResponse](submissionUrl, businessSubmission)(wts = BusinessMatchingSubmission.format, rds = httpReads, hc = newHeaders, ec = ec)
  }

  private def addHeaders()(implicit headerCarrier: HeaderCarrier): Seq[(String, String)] =
    Seq(
      "X-Forwarded-Host" -> "mdtp",
      "X-Correlation-ID" -> {
        headerCarrier.sessionId
          .map(_.value)
          .getOrElse(UUID.randomUUID().toString)
      },
      "Environment" -> config.desEnvironment,
      "Content-Type"     -> "application/json",
      "Accept"           -> "application/json"
    )

}
