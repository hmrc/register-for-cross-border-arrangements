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

package connectors

import config.AppConfig
import models.{BusinessMatchingSubmission, IndividualMatchingSubmission}
import uk.gov.hmrc.domain.Nino
import uk.gov.hmrc.http.{Authorization, HeaderCarrier, HeaderNames, HttpClient, HttpResponse}

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class BusinessMatchingConnector @Inject()(val config: AppConfig, val http: HttpClient) {

  def sendIndividualMatchingInformation(nino: Nino, individualSubmission: IndividualMatchingSubmission)
                                       (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[HttpResponse] = {
    val submissionUrl = s"${config.businessMatchingUrl}/registration/individual/nino/$nino"

    http.POST[IndividualMatchingSubmission, HttpResponse](submissionUrl, individualSubmission, headers = extraHeaders)(wts = IndividualMatchingSubmission.format, rds = httpReads, hc = hc, ec = ec)
  }

  def sendSoleProprietorMatchingInformation(utr: String, soleProprietorSubmission: BusinessMatchingSubmission)
                                       (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[HttpResponse] = {
    val submissionUrl = s"${config.businessMatchingUrl}/registration/individual/utr/$utr"

    http.POST[BusinessMatchingSubmission, HttpResponse](submissionUrl, soleProprietorSubmission, headers = extraHeaders)(wts = BusinessMatchingSubmission.format, rds = httpReads, hc = hc, ec = ec)
  }

  def sendBusinessMatchingInformation(utr: String, businessSubmission: BusinessMatchingSubmission)
                                       (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[HttpResponse] = {
    val submissionUrl = s"${config.businessMatchingUrl}/registration/organisation/utr/$utr"

    http.POST[BusinessMatchingSubmission, HttpResponse](submissionUrl, businessSubmission, headers = extraHeaders)(wts = BusinessMatchingSubmission.format, rds = httpReads, hc = hc, ec = ec)
  }

  private def extraHeaders(implicit headerCarrier: HeaderCarrier): Seq[(String, String)] = {
    val newHeaders = headerCarrier
      .copy(authorization = Some(Authorization(s"Bearer ${config.bearerToken}")))

    newHeaders.headers(Seq(HeaderNames.authorisation)) ++ addHeaders
  }

  private def addHeaders()(implicit headerCarrier: HeaderCarrier): Seq[(String, String)] = {

    //HTTP-date format defined by RFC 7231 e.g. Fri, 01 Aug 2020 15:51:38 GMT+1
    val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss O")

    Seq(
      "x-forwarded-host" -> "mdtp",
      "date" -> ZonedDateTime.now().format(formatter),
      "x-correlation-id" -> {
        headerCarrier.requestId
          .map(_.value)
          .getOrElse(UUID.randomUUID().toString)
      },
      "x-conversation-id" -> {
        headerCarrier.sessionId
          .map(_.value)
          .getOrElse(UUID.randomUUID().toString)
      },
      "content-type"    -> "application/json",
      "accept"          -> "application/json",
      "Environment"      -> config.eisEnvironment
    )
  }

}
