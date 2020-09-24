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

