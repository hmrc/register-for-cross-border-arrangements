package models

import play.api.libs.json.{Json, OWrites, Reads, __}

case class SubscriptionForDACRequest(requestDetail: RequestDetail)

object SubscriptionForDACRequest {
  implicit val format = Json.format[SubscriptionForDACRequest]
}

case class OrganisationName(name: String)

object OrganisationName {
  implicit val format = Json.format[OrganisationName]
}

case class PrimaryContact(individual: Name, email: String, phone: String, mobile: String)
object PrimaryContact {
  implicit lazy val writes: OWrites[PrimaryContact] = OWrites[PrimaryContact] {
    primaryContact =>
      Json.obj(
        "firstName" -> primaryContact.individual.firstName,
        "lastName" -> primaryContact.individual.secondName,
        "email" -> primaryContact.email,
        "phone" -> primaryContact.phone,
        "mobile" -> primaryContact.mobile,
      )
  }

  implicit lazy val reads: Reads[PrimaryContact] = {
    import play.api.libs.functional.syntax._
    (
      (__ \ "firstName").read[String] and
        (__ \ "lastName").read[String] and
          (__ \ "email").read[String] and
            (__ \ "phone").read[String] and
              (__ \ "mobile").read[String]
      )((firstName, secondName, email, phone, mobile) => PrimaryContact(Name(firstName, secondName), email, phone, mobile))
  }
}

case class SecondaryContact(organisation: OrganisationName, email: String, phone: String)
object SecondaryContact {
  implicit lazy val writes: OWrites[SecondaryContact] = OWrites[SecondaryContact] {
    secondaryContact =>
      Json.obj(
        "organisation" -> secondaryContact.organisation.name,
        "email" -> secondaryContact.email,
        "phone" -> secondaryContact.phone
      )
  }

  implicit lazy val reads: Reads[SecondaryContact] = {
    import play.api.libs.functional.syntax._
    (
      (__ \ "organisation").read[String] and
        (__ \ "email").read[String] and
        (__ \ "phone").read[String]
      )((organisation, email, phone) => SecondaryContact(OrganisationName(organisation), email, phone))
  }
}

case class RequestDetail(idType: String,
                         idNumber: String,
                         tradingName: String,
                         isGBUser: Boolean,
                         primaryContact: PrimaryContact,
                         secondaryContact: SecondaryContact)
object RequestDetail {
  implicit val format = Json.format[RequestDetail]
}

