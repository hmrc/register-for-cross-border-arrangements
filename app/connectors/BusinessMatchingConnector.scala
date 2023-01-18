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
import models.{BusinessMatchingSubmission, IndividualMatchingSubmission}
import uk.gov.hmrc.domain.Nino
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient, HttpResponse}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class BusinessMatchingConnector @Inject() (val config: AppConfig, val http: HttpClient) {

  def sendIndividualMatchingInformation(nino: Nino, individualSubmission: IndividualMatchingSubmission)(implicit
    hc: HeaderCarrier,
    ec: ExecutionContext
  ): Future[HttpResponse] = {
    val submissionUrl = s"${config.businessMatchingUrl}/registration/individual/nino/$nino"

    http.POST[IndividualMatchingSubmission, HttpResponse](submissionUrl, individualSubmission, headers = extraHeaders(config))(
      wts = IndividualMatchingSubmission.format,
      rds = httpReads,
      hc = hc,
      ec = ec
    )
  }

  def sendSoleProprietorMatchingInformation(utr: String, soleProprietorSubmission: BusinessMatchingSubmission)(implicit
    hc: HeaderCarrier,
    ec: ExecutionContext
  ): Future[HttpResponse] = {
    val submissionUrl = s"${config.businessMatchingUrl}/registration/individual/utr/$utr"

    http.POST[BusinessMatchingSubmission, HttpResponse](submissionUrl, soleProprietorSubmission, headers = extraHeaders(config))(
      wts = BusinessMatchingSubmission.format,
      rds = httpReads,
      hc = hc,
      ec = ec
    )
  }

  def sendBusinessMatchingInformation(utr: String, businessSubmission: BusinessMatchingSubmission)(implicit
    hc: HeaderCarrier,
    ec: ExecutionContext
  ): Future[HttpResponse] = {
    val submissionUrl = s"${config.businessMatchingUrl}/registration/organisation/utr/$utr"

    http.POST[BusinessMatchingSubmission, HttpResponse](submissionUrl, businessSubmission, headers = extraHeaders(config))(wts =
                                                                                                                             BusinessMatchingSubmission.format,
                                                                                                                           rds = httpReads,
                                                                                                                           hc = hc,
                                                                                                                           ec = ec
    )
  }
}
