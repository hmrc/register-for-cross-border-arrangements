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

package models

import play.api.libs.json.{Json, OWrites, Reads, __}

case class Identifier(key: String, value: String)

object Identifier {
  implicit val format = Json.format[Identifier]

  implicit lazy val writes: OWrites[Identifier] = OWrites[Identifier] {
    identifier =>
      Json.obj(
        "key" -> identifier.key,
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
        "key" -> verifier.key,
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
        "verifiers" -> enrolmentRequest.verifiers
      )
  }

  case class EnrolmentInfo (dac6UserID: String,
                            primaryContactName: String,
                            primaryEmailAddress: String,
                            primaryTelephoneNumber: Option[String] = None,
                            secondaryContactName: Option[String] = None,
                            secondaryEmailAddress: Option[String] = None,
                            secondaryTelephoneNumber: Option[String] = None,
                            businessName: Option[String] = None,
                           ) {

    def convertToEnrolmentRequest: EnrolmentRequest = {

      EnrolmentRequest(identifiers = Seq(Identifier("DAC6ID", dac6UserID)),
                        verifiers = buildVerifiers)
    }

     def buildVerifiers: Seq[Verifier] = {

      val mandatoryVerifiers = Seq(Verifier("CONTACTNAME", primaryContactName),
        Verifier("EMAIL", primaryEmailAddress))

      mandatoryVerifiers ++
        buildOptionalVerifier(primaryTelephoneNumber, "TELEPHONE") ++
        buildOptionalVerifier(secondaryContactName, "SECCONTACTNAME") ++
        buildOptionalVerifier(secondaryEmailAddress, "SECEMAIL") ++
        buildOptionalVerifier(secondaryTelephoneNumber, "SECNUMBER") ++
        buildOptionalVerifier(businessName, "BUSINESSNAME")

    }

     def buildOptionalVerifier(optionalInfo: Option[String], key: String): Seq[Verifier] = {
      optionalInfo.map(info => Verifier(key, info)).toSeq

    }

  }
  object EnrolmentInfo {
    implicit val format = Json.format[EnrolmentInfo]

  }

}
