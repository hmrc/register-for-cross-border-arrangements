/*
 * Copyright 2022 HM Revenue & Customs
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

import base.SpecBase
import generators.Generators
import helpers.JsonFixtures._
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.Json

class RegistrationSpec extends SpecBase with Generators with ScalaCheckPropertyChecks {

  "Subscription" - {
    "marshal from Json individual" in {
      Json.parse(jsonPayload).validate[Registration].get mustBe sub
    }
    "marshal from json organisation" in {
      Json.parse(jsonSubWithOrg).validate[Registration].get mustBe subWithOrg
    }
    "marshall into json indiviual" in {
      Json.toJson(sub) mustBe subJson
    }
    "marshall into json organisation" in {
      Json.toJson(subWithOrg) mustBe subJsonWithOrg
    }
    "catch error if neither organisation or individual is present" in {
      val error = intercept[Exception] {
        Json.parse(invalidJsonPayloadDual).validate[Registration] mustBe sub
      }
      error.getMessage mustBe "Request Details must have either an organisation or individual element"
    }

    "catch error if both an organisation and an individual is present" in {
      val error = intercept[Exception] {
        Json.parse(invalidJsonPayload).validate[Registration] mustBe sub
      }
      error.getMessage mustBe "Request details cannot have both and organisation or individual element"
    }
  }
}
