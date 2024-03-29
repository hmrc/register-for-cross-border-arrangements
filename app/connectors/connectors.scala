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

import config.AppConfig
import uk.gov.hmrc.http.{Authorization, HeaderCarrier, HeaderNames, HttpReads, HttpResponse}

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

package object connectors {

  implicit val httpReads: HttpReads[HttpResponse] =
    new HttpReads[HttpResponse] {

      override def read(method: String, url: String, response: HttpResponse): HttpResponse =
        response
    }

  private[connectors] def extraHeaders(config: AppConfig)(implicit headerCarrier: HeaderCarrier): Seq[(String, String)] = {
    val newHeaders = headerCarrier
      .copy(authorization = Some(Authorization(s"Bearer ${config.bearerToken}")))

    newHeaders.headers(Seq(HeaderNames.authorisation)) ++ addHeaders(config.eisEnvironment)
  }

  private def addHeaders(eisEnvironment: String)(implicit headerCarrier: HeaderCarrier): Seq[(String, String)] = {

    //HTTP-date format defined by RFC 7231 e.g. Fri, 01 Aug 2020 15:51:38 GMT+1
    val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss O")

    Seq(
      "x-forwarded-host" -> "mdtp",
      "date"             -> ZonedDateTime.now().format(formatter),
      "x-correlation-id" -> {
        UUID.randomUUID().toString
      },
      "x-conversation-id" -> {
        headerCarrier.sessionId
          .map(_.value)
          .getOrElse(UUID.randomUUID().toString)
          .replace("session-", "")
      },
      "content-type" -> "application/json",
      "accept"       -> "application/json",
      "Environment"  -> eisEnvironment
    )
  }
}
