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

import java.time.LocalDate

import play.api.libs.json._

trait IndividualAndBusinessMatchingSubmission

case class IndividualMatchingSubmission(regime: String, requiresNameMatch: Boolean, isAnAgent: Boolean, individual: Individual)
    extends IndividualAndBusinessMatchingSubmission

object IndividualMatchingSubmission {
  implicit val format = Json.format[IndividualMatchingSubmission]
}

case class Individual(name: Name, dateOfBirth: LocalDate)

object Individual {

  implicit lazy val writes: OWrites[Individual] = OWrites[Individual] {
    individual =>
      Json.obj(
        "firstName"   -> individual.name.firstName,
        "lastName"    -> individual.name.secondName,
        "dateOfBirth" -> individual.dateOfBirth.toString
      )
  }

  implicit lazy val reads: Reads[Individual] = {
    import play.api.libs.functional.syntax._
    (
      (__ \ "firstName").read[String] and
        (__ \ "lastName").read[String] and
        (__ \ "dateOfBirth").read[LocalDate]
    )(
      (firstName, secondName, dob) => Individual(Name(firstName, secondName), dob)
    )
  }
}

//orgName between 1 and 105 "^[a-zA-Z0-9 '&\\/]{1,105}$"
case class Organisation(organisationName: String, organisationType: BusinessType)

object Organisation {

  implicit lazy val writes: OWrites[Organisation] = OWrites[Organisation] {
    organisation =>
      Json.obj(
        "organisationName" -> organisation.organisationName,
        "organisationType" -> organisation.organisationType
      )
  }

  implicit lazy val reads: Reads[Organisation] = {
    import play.api.libs.functional.syntax._
    (
      (__ \ "organisationName").read[String] and
        (__ \ "organisationType").read[BusinessType]
    )(
      (organisationName, organisationType) => Organisation(organisationName, organisationType)
    )
  }
}

case class BusinessMatchingSubmission(regime: String, requiresNameMatch: Boolean, isAnAgent: Boolean, organisation: Organisation)
    extends IndividualAndBusinessMatchingSubmission

object BusinessMatchingSubmission {
  implicit val format: OFormat[BusinessMatchingSubmission] = Json.format[BusinessMatchingSubmission]
}
