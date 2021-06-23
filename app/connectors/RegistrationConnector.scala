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
import models.{PayloadRegisterWithID, Registration}
import uk.gov.hmrc.http.{Authorization, HeaderCarrier, HeaderNames, HttpClient, HttpResponse}

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class RegistrationConnector @Inject()(val config: AppConfig, val http: HttpClient) {

  def sendWithoutIDInformation(registration: Registration)
                              (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[HttpResponse] = {

    http.POST[Registration, HttpResponse](config.registerUrl, registration, headers = extraHeaders)(wts = Registration.format, rds = httpReads, hc = hc, ec = ec)
  }

  def sendWithID(registration: PayloadRegisterWithID)
                              (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[HttpResponse] = {

    http.POST[PayloadRegisterWithID, HttpResponse](config.registerWithIDUrl, registration, headers = extraHeaders)(wts = PayloadRegisterWithID.format, rds = httpReads, hc = hc, ec = ec)
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
