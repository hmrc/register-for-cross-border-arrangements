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

import play.api.libs.json.{JsError, JsString, JsSuccess, Json, OFormat, OWrites, Reads, __}

sealed trait BusinessType {
  val value:String
}

case object partnerShip extends BusinessType {
  override val value: String = "Partnership"
}
case object limitedLiability extends BusinessType {
  override val value: String = "LLP"
}
case object corporateBody extends BusinessType {
  override val value: String = "Corporate Body"
}
case object unIncorporatedBody extends BusinessType {
  override val value: String = "Unincorporated Body"
}
case object other extends BusinessType {
  override val value: String = "Not Specified"
}

object BusinessType {
  implicit lazy val writes: OWrites[BusinessType] = OWrites[BusinessType] {
    businessType =>
      Json.obj( ("organisationType", JsString(businessType.value)))
  }

  implicit lazy val reads: Reads[BusinessType] = {
    case JsString("partnerShip") => JsSuccess(partnerShip)
    case JsString("limitedLiability") => JsSuccess(limitedLiability)
    case JsString("corporateBody") => JsSuccess(corporateBody)
    case JsString("unIncorporatedBody") => JsSuccess(unIncorporatedBody)
    case JsString("other") => JsSuccess(other)
    case _ => JsError()
  }
}
