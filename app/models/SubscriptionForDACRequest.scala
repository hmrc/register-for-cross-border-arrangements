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

import play.api.libs.json.{Json, Reads, __}

case class OrganisationDetails(name: String)

object OrganisationDetails {
  implicit val format = Json.format[OrganisationDetails]
}

case class IndividualDetails(firstName: String, middleName: Option[String], lastName: String)

object IndividualDetails {
  implicit val format = Json.format[IndividualDetails]
}

case class ContactInformation(email: String,
                              phone: Option[String],
                              mobile: Option[String],
                              individual: Option[IndividualDetails],
                              organisation: Option[OrganisationDetails])

object ContactInformation {

  implicit lazy val residentWrites = Json.writes[ContactInformation]

  implicit lazy val reads: Reads[ContactInformation] = {
    import play.api.libs.functional.syntax._
    (
      (__ \ "email").read[String] and
        (__ \ "phone").readNullable[String]  and
        (__ \ "mobile").readNullable[String]  and
        (__ \ "individual").readNullable[IndividualDetails]  and
        (__ \ "organisation").readNullable[OrganisationDetails]
      )((email, phone, mobile, individual, organisation ) => (organisation, individual) match {
      case (None, None) => throw new Exception("Contact information must have either org or individual elem")
      case (Some(_), Some(_)) => throw new Exception("Contact Information cannot contain both org & Individual elem")
      case (organisation, individual) => ContactInformation(email, phone, mobile, individual, organisation)
    })
  }
}

case class PrimaryContact(contactInformation: ContactInformation)

object PrimaryContact{
  implicit val format = Json.format[PrimaryContact]

  implicit lazy val reads: Reads[PrimaryContact] = {
    import play.api.libs.functional.syntax._
    (
      (__ \ "individual").readNullable[IndividualDetails]  and
        (__ \ "organisation").readNullable[OrganisationDetails] and
        (__ \ "email").read[String] and
        (__ \ "phone").readNullable[String]  and
        (__ \ "mobile").readNullable[String]
      )((individual, organisation, email, phone, mobile) =>
      if (organisation.isDefined){
        PrimaryContact(ContactInformation(email, phone, mobile, None, organisation))
      } else {
        PrimaryContact(ContactInformation(email, phone, mobile, individual, None))
      })
  }
}

case class SecondaryContact(contactInformation: ContactInformation)

object SecondaryContact{
  implicit val format = Json.format[SecondaryContact]

  implicit lazy val reads: Reads[SecondaryContact] = {
    import play.api.libs.functional.syntax._
    (
      (__ \ "individual").readNullable[IndividualDetails] and
        (__ \ "organisation").readNullable[OrganisationDetails] and
        (__ \ "email").read[String] and
        (__ \ "phone").readNullable[String] and
        (__ \ "mobile").readNullable[String]
      ) ((individual, organisation, email, phone, mobile) =>
      if (organisation.isDefined) {
        SecondaryContact(ContactInformation(email, phone, mobile, None, organisation))
      } else {
        SecondaryContact(ContactInformation(email, phone, mobile, individual, None))
      })
  }
}

case class RequestDetail(idType: String,
                         idNumber: String,
                         tradingName: Option[String],
                         isGBUser: Boolean,
                         primaryContact: PrimaryContact,
                         secondaryContact: Option[SecondaryContact])
object RequestDetail {

implicit lazy val residentWrites = Json.writes[RequestDetail]

  implicit lazy val reads: Reads[RequestDetail] = {
    import play.api.libs.functional.syntax._
    (
      (__ \ "idType").read[String] and
        (__ \ "idNumber").read[String] and
        (__ \ "tradingName").readNullable[String] and
        (__ \ "isGBUser").read[Boolean] and
        (__ \ "primaryContact").read[PrimaryContact] and
        (__ \ "secondaryContact").readNullable[SecondaryContact]
      )(
      (idType, idNumber,tradingName, isGBUser, primaryContact, secondaryContact) => RequestDetail(
      idType, idNumber, tradingName, isGBUser,primaryContact, secondaryContact))
  }
}

case class RequestParameters(paramName: String, paramValue: String)

object RequestParameters {
  implicit val indentifierFormats = Json.format[RequestParameters]
}

case class RequestCommon(regime: String,
                         receiptDate: String,
                         acknowledgementReference: String,
                         originatingSystem: String,
                         requestParameters: Option[Seq[RequestParameters]])

object RequestCommon {
  implicit val requestCommonFormats = Json.format[RequestCommon]
}


case class SubscriptionForDACRequest(requestCommon: RequestCommon, requestDetail: RequestDetail)

object SubscriptionForDACRequest {
  implicit val format = Json.format[SubscriptionForDACRequest]
}



