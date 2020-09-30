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

import play.api.libs.json.{Json, OFormat, OWrites, Reads, __}

case class OrganisationDetails(organisationName: String)

object OrganisationDetails {
  implicit val format = Json.format[OrganisationDetails]
}

case class IndividualDetails(firstName: String, middleName: Option[String], lastName: String)

object IndividualDetails {
  implicit val format = Json.format[IndividualDetails]
}

sealed trait ContactInformation

case class ContactInformationForIndividual(individual: IndividualDetails,
                                           email: String,
                                           phone: Option[String],
                                           mobile: Option[String]) extends ContactInformation
object ContactInformationForIndividual {
  implicit val format: OFormat[ContactInformationForIndividual] = Json.format[ContactInformationForIndividual]
}

case class ContactInformationForOrganisation(organisation: OrganisationDetails,
                                             email: String,
                                             phone: Option[String],
                                             mobile: Option[String]) extends ContactInformation
object ContactInformationForOrganisation {
  implicit val format: OFormat[ContactInformationForOrganisation] = Json.format[ContactInformationForOrganisation]
}

case class PrimaryContact(contactInformation: ContactInformation)

object PrimaryContact{

  implicit lazy val writes: OWrites[PrimaryContact] = {
    case PrimaryContact(contactInformationForInd@ContactInformationForIndividual(_, _, _, _)) =>
      Json.toJsObject(contactInformationForInd)
    case PrimaryContact(contactInformationForOrg@ContactInformationForOrganisation(_, _, _, _)) =>
      Json.toJsObject(contactInformationForOrg)
  }

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
        PrimaryContact(ContactInformationForOrganisation(organisation.get, email, phone, mobile))
      } else {
        PrimaryContact(ContactInformationForIndividual(individual.get, email, phone, mobile))
      })
  }
}

case class SecondaryContact(contactInformation: ContactInformation)

object SecondaryContact{

  implicit lazy val writes: OWrites[SecondaryContact] = {
    case SecondaryContact(contactInformationForInd@ContactInformationForIndividual(_, _, _, _)) =>
      Json.toJsObject(contactInformationForInd)
    case SecondaryContact(contactInformationForOrg@ContactInformationForOrganisation(_, _, _, _)) =>
      Json.toJsObject(contactInformationForOrg)
  }

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
        SecondaryContact(ContactInformationForOrganisation(organisation.get, email, phone, mobile))
      } else {
        SecondaryContact(ContactInformationForIndividual(individual.get, email, phone, mobile))
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


//case class SubscriptionForDACRequest(requestCommon: RequestCommonForSubscription, requestDetail: RequestDetail)
//
//object SubscriptionForDACRequest {
//  implicit val writes: OWrites[SubscriptionForDACRequest] = OWrites[SubscriptionForDACRequest] {
//    dacRequest =>
//      Json.obj(
//        "createSubscriptionForDACRequest" -> Json.obj(
//          "requestCommon" -> dacRequest.requestCommon,
//          "requestDetail" -> dacRequest.requestDetail
//        )
//      )
//  }
//
//  implicit val reads: Reads[SubscriptionForDACRequest] = {
//    import play.api.libs.functional.syntax._
//    (
//      (__ \ "createSubscriptionForDACResponse" \ "responseCommon").read[RequestCommonForSubscription] and
//        (__ \ "createSubscriptionForDACResponse" \ "responseDetail").read[RequestDetail]
//      )((responseCommon, responseDetail) => SubscriptionForDACRequest(responseCommon, responseDetail))
//  }
//}



