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

sealed trait NameValue {
  def key: String

  def value: String
}

case class Identifier(key: String, value: String) extends NameValue

object Identifier {
  implicit val format = Json.format[Identifier]
}

case class Verifier(key: String, value: String) extends NameValue

object Verifier {
  implicit val format = Json.format[Verifier]
}

case class EnrolmentRequest(identifiers: Seq[Identifier], verifiers: Seq[Verifier])

object EnrolmentRequest {
  implicit val format = Json.format[EnrolmentRequest]
}
