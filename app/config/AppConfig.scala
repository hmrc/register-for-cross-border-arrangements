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

package config

import javax.inject.{Inject, Singleton}
import play.api.Configuration

@Singleton
class AppConfig @Inject() (config: Configuration) {

  lazy val businessMatchingUrl: String =
    s"${config.get[Service]("microservice.services.business-matching").baseUrl}${config.get[String]("microservice.services.business-matching.startUrl")}"

  lazy val registrationUrl: String =
    s"${config.get[Service]("microservice.services.registration").baseUrl}${config.get[String]("microservice.services.registration.startUrl")}"

  lazy val taxEnrolmentsUrl: String =
    s"${config.get[Service]("microservice.services.tax-enrolments").baseUrl}${config.get[String]("microservice.services.tax-enrolments.url")}"

  lazy val registerUrl: String       = s"$registrationUrl/dac6/dct01/v1"
  lazy val registerWithIDUrl: String = s"$registrationUrl/dac6/dct02/v1"

  lazy val bearerToken: String    = config.get[String]("microservice.services.business-matching.bearer-token")
  lazy val eisEnvironment: String = config.get[String]("microservice.services.business-matching.environment")

  lazy val subscriptionURL: String = s"$registrationUrl/dac6/dct03/v1"

}
