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

import config.AppConfig
import models.{PayloadRegisterWithID, Registration}
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient, HttpResponse}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class RegistrationConnector @Inject() (val config: AppConfig, val http: HttpClient) {

  def sendWithoutIDInformation(registration: Registration)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[HttpResponse] =
    http.POST[Registration, HttpResponse](config.registerUrl, registration, headers = extraHeaders(config))(wts = Registration.format,
                                                                                                            rds = httpReads,
                                                                                                            hc = hc,
                                                                                                            ec = ec
    )

  def sendWithID(registration: PayloadRegisterWithID)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[HttpResponse] =
    http.POST[PayloadRegisterWithID, HttpResponse](config.registerWithIDUrl, registration, headers = extraHeaders(config))(wts = PayloadRegisterWithID.format,
                                                                                                                           rds = httpReads,
                                                                                                                           hc = hc,
                                                                                                                           ec = ec
    )

}
