package connectors

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

import config.AppConfig
import javax.inject.Inject
import models.SubscriptionForDACRequest
import uk.gov.hmrc.http.logging.Authorization
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient, HttpResponse}

import scala.concurrent.{ExecutionContext, Future}

class SubscriptionConnector @Inject()(val config: AppConfig, val http: HttpClient) {

  def sendSubscriptionInformation(
    subscription: SubscriptionForDACRequest
  )(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[HttpResponse] = {

    val newHeaders = hc
      .copy(authorization = Some(Authorization(s"Bearer ${config.desBearerToken}")))
      .withExtraHeaders(addHeaders(): _*)

    http.POST[SubscriptionForDACRequest, HttpResponse](config.subscriptionURL, subscription)(wts =
    SubscriptionForDACRequest.format, rds = httpReads, hc = newHeaders, ec = ec)
  }

  private def addHeaders()(implicit headerCarrier: HeaderCarrier): Seq[(String,String)] = {

    //HTTP-date format defined by RFC 7231 e.g. Fri, 01 Aug 2020 15:51:38 GMT+1
    val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss O")

    Seq(
      "x-forwarded-host" -> "mdtp",
      "date" -> ZonedDateTime.now().format(formatter),
      "x-correlation-id" -> {
        headerCarrier.sessionId
          .map(_.value)
          .getOrElse(UUID.randomUUID().toString)
      },
      "x-conversation-id" -> {
        headerCarrier.requestId
          .map(_.value)
          .getOrElse(UUID.randomUUID().toString)
      },
      "content-type"    -> "application/json",
      "accept"          -> "application/json"
    )
  }
}
