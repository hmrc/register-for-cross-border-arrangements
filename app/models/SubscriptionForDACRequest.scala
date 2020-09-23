package models

import play.api.libs.json.Json

case class SubscriptionForDACRequest(requestDetail: RequestDetail)

object SubscriptionForDACRequest {
  implicit val format = Json.format[SubscriptionForDACRequest]
}

case class Organisation(name: String)

object Organisation {
  implicit val format = Json.format[Organisation]
}

case class Individual(firstName: String, middleName: Option[String], lastName: String)

object Individual {
  implicit val format = Json.format[Individual]
}

case class ContactInformation(email: String,
                              phone: Option[String],
                              mobile: Option[String],
                              individual: Option[Individual],
                              organisation: Option[Organisation])

object ContactInformation {
  implicit  val format = Json.format[ContactInformation]
}

case class PrimaryContact(contactInformation: ContactInformation)

object PrimaryContact{
  implicit val format = Json.format[ContactInformation]
}

case class SecondaryContact(contactInformation: ContactInformation)

object SecondaryContact{
  implicit val format = Json.format[ContactInformation]
}

case class RequestDetail(idType: String,
                         idNumber: String,
                         tradingName: String,
                         isGBUser: Boolean,
                         primaryContact: PrimaryContact,
                         secondaryContact: Option[SecondaryContact])
object RequestDetail {
  implicit val format = Json.format[RequestDetail]
}

