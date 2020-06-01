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

import base.SpecBase
import generators.Generators
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.Json

class BusinessMatchingSubmissionSpec extends SpecBase
  with MockitoSugar
  with Generators
  with ScalaCheckPropertyChecks {

  "BusinessMatchingSubmission" - {
    "should marshall correctly from json" in {
      val jsonPayload =
        """{
          |   "regime":"DACSIX",
          |   "requiresNameMatch":true,
          |   "isAnAgent":false,
          |   "organisation":{
          |       "organisationName":"AAAA",
          |       "organisationType":"LLP"
          |   }
          |}""".stripMargin

      Json.parse(jsonPayload).validate[BusinessMatchingSubmission].get mustBe
        BusinessMatchingSubmission(
          "DACSIX",
          requiresNameMatch = true,
          isAnAgent = false,
          Organisation("AAAA", limitedLiability))
    }
  }


}
