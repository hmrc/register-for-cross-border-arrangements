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

package models

import play.api.libs.json.{Json, OWrites}

case class Identifier(key: String, value: String)

object Identifier {
  implicit val format = Json.format[Identifier]

  implicit lazy val writes: OWrites[Identifier] = OWrites[Identifier] {
    identifier =>
      Json.obj(
        "key"   -> identifier.key,
        "value" -> identifier.value
      )
  }
}

case class Verifier(key: String, value: String)

object Verifier {
  implicit val format = Json.format[Verifier]

  implicit lazy val writes: OWrites[Verifier] = OWrites[Verifier] {
    verifier =>
      Json.obj(
        "key"   -> verifier.key,
        "value" -> verifier.value
      )
  }
}

case class EnrolmentRequest(identifiers: Seq[Identifier], verifiers: Seq[Verifier])

object EnrolmentRequest {
  implicit val format = Json.format[EnrolmentRequest]

  implicit lazy val writes: OWrites[EnrolmentRequest] = OWrites[EnrolmentRequest] {
    enrolmentRequest =>
      Json.obj(
        "identifiers" -> enrolmentRequest.identifiers,
        "verifiers"   -> enrolmentRequest.verifiers
      )
  }

  case class SubscriptionInfo(safeID: String,
                              saUtr: Option[String] = None,
                              ctUtr: Option[String] = None,
                              nino: Option[String] = None,
                              nonUkPostcode: Option[String] = None,
                              dac6Id: String
  ) {

    def convertToEnrolmentRequest: EnrolmentRequest =
      EnrolmentRequest(identifiers = Seq(Identifier("DAC6ID", dac6Id)), verifiers = buildVerifiers)

    def buildVerifiers: Seq[Verifier] = {

      val mandatoryVerifiers = Seq(Verifier("SAFEID", safeID))

      mandatoryVerifiers ++
        buildOptionalVerifier(saUtr, "SAUTR") ++
        buildOptionalVerifier(ctUtr, "CTUTR") ++
        buildOptionalVerifier(nino, "NINO") ++
        buildOptionalVerifier(nonUkPostcode, "NonUKPostalCode")

    }

    def buildOptionalVerifier(optionalInfo: Option[String], key: String): Seq[Verifier] =
      optionalInfo
        .map(
          info => Verifier(key, info)
        )
        .toSeq

  }

  object SubscriptionInfo {
    implicit val format = Json.format[SubscriptionInfo]

  }

}
