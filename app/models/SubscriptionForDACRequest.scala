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

sealed trait ContactInformation

case class OrganisationDetails(organisationName: String) extends ContactInformation

object OrganisationDetails {
  implicit val format = Json.format[OrganisationDetails]
}

case class IndividualDetails(firstName: String, middleName: Option[String], lastName: String) extends ContactInformation

object IndividualDetails {
  implicit val format = Json.format[IndividualDetails]
}

case class PrimaryContact(indOrOrg: ContactInformation,
                          email: String,
                          phone: Option[String],
                          mobile: Option[String]
                         )
object PrimaryContact {
  implicit lazy val primaryContactReads: Reads[PrimaryContact] = {
    import play.api.libs.functional.syntax._
    (
      (__ \ "organisation").readNullable[OrganisationDetails] and
        (__ \ "individual").readNullable[IndividualDetails] and
        (__ \ "email").read[String] and
        (__ \ "phone").readNullable[String] and
        (__ \ "mobile").readNullable[String]
      ) (
      (organisation, individual, email, phone, mobile) => (organisation, individual) match {
        case (Some(_), Some(_)) => throw new Exception("SecondaryContact cannot have both and organisation or individual element")
        case (Some(org), _) => PrimaryContact(org, email, phone, mobile)
        case (_, Some(ind)) => PrimaryContact(ind, email, phone, mobile)
        case (None, None) => throw new Exception("SecondaryContact must have either an organisation or individual element")
      }
    )
  }

  implicit lazy val primaryContactWrites: OWrites[PrimaryContact] = OWrites[PrimaryContact] {
    case PrimaryContact(individual@IndividualDetails(_, _, _), email, phone, mobile) =>
      Json.obj(
        "individual" -> individual,
        "email" -> email,
        "phone" -> phone,
        "mobile" -> mobile
      )
    case PrimaryContact(organisation@OrganisationDetails(_), email, phone, mobile) =>
      Json.obj(
        "organisation" -> organisation,
        "email" -> email,
        "phone" -> phone,
        "mobile" -> mobile
      )
  }
}

case class SecondaryContact(indOrOrg: ContactInformation,
                            email: String,
                            phone: Option[String],
                            mobile: Option[String]
                           )

object SecondaryContact{
  implicit lazy val SecondaryContactReads: Reads[SecondaryContact] = {
    import play.api.libs.functional.syntax._
    (
      (__ \ "organisation").readNullable[OrganisationDetails] and
        (__ \ "individual").readNullable[IndividualDetails] and
        (__ \ "email").read[String] and
        (__ \ "phone").readNullable[String] and
        (__ \ "mobile").readNullable[String]
      ) (
      (organisation, individual, email, phone, mobile) => (organisation, individual) match {
        case (Some(_), Some(_)) => throw new Exception("SecondaryContact cannot have both and organisation or individual element")
        case (Some(org), _) => SecondaryContact(org, email, phone, mobile)
        case (_, Some(ind)) => SecondaryContact(ind, email, phone, mobile)
        case (None, None) => throw new Exception("SecondaryContact must have either an organisation or individual element")
      }
    )
  }

  implicit lazy val SecondaryContactWrites: OWrites[SecondaryContact] = OWrites[SecondaryContact] {
    case SecondaryContact(individual@IndividualDetails(_, _, _), email, phone, mobile) =>
      Json.obj(
        "individual" -> individual,
        "email" -> email,
        "phone" -> phone,
        "mobile" -> mobile
      )
    case SecondaryContact(organisation@OrganisationDetails(_), email, phone, mobile) =>
      Json.obj(
        "organisation" -> organisation,
        "email" -> email,
        "phone" -> phone,
        "mobile" -> mobile
      )
  }
}

case class RequestDetail(idType: String,
                         idNumber: String,
                         tradingName: Option[String],
                         isGBUser: Boolean,
                         primaryContact: PrimaryContact,
                         secondaryContact: Option[SecondaryContact])
object RequestDetail {

  implicit val requestDetailFormats = Json.format[RequestDetail]

}

case class RequestParameters(paramName: String, paramValue: String)

object RequestParameters {
  implicit val indentifierFormats = Json.format[RequestParameters]
}

case class RequestCommonForSubscription(regime: String,
                         receiptDate: String,
                         acknowledgementReference: String,
                         originatingSystem: String,
                         requestParameters: Option[Seq[RequestParameters]])

object RequestCommonForSubscription {
  implicit val requestCommonFormats = Json.format[RequestCommonForSubscription]

}

case class SubscriptionForDACRequest(requestCommon: RequestCommonForSubscription, requestDetail: RequestDetail)

object SubscriptionForDACRequest {
  implicit val format = Json.format[SubscriptionForDACRequest]

}

case class CreateSubscriptionForDACRequest(createSubscriptionForDACRequest: SubscriptionForDACRequest)

object CreateSubscriptionForDACRequest {
  implicit val format = Json.format[CreateSubscriptionForDACRequest]
}




