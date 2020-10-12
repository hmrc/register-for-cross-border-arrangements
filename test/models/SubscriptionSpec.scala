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
import helpers.SubscriptionJsonFixtures._
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.Json


class SubscriptionSpec extends SpecBase
  with MockitoSugar
  with Generators
  with ScalaCheckPropertyChecks {

  "create Subscription for DAC Request" - {

    "should marshall correctly from json for individual " in  {
      forAll(validContactNumber, validContactNumber) {
        (phone, mobile) =>
          Json.parse(jsonPayloadForIndividual(phone, mobile)).validate[CreateSubscriptionForDACRequest].get mustBe individualSubcription(phone, mobile)
      }
    }

    "marshall into json subscription for individual" in {
      forAll(validContactNumber, validContactNumber) {
        (phone, mobile) =>
          Json.toJson(individualSubcription(phone, mobile)) mustBe IndividualSubscriptionJson(phone, mobile)
      }
    }

    "should marshall correctly from json for organisation" in  {
      forAll(stringsWithMaxLength(105), validContactNumber, validContactNumber) {
        (orgName, phone, mobile) =>
          Json.parse(jsonPayloadForOrganisation(
            orgName, phone, mobile)
          ).validate[CreateSubscriptionForDACRequest].get mustBe organisationSubscription(orgName, phone, mobile)
      }
    }

    "marshall into json subscription for organisation" in {
      forAll(stringsWithMaxLength(105), validContactNumber, validContactNumber) {
        (orgName, phone, mobile) =>
        Json.toJson(organisationSubscription(
          orgName, phone, mobile)) mustBe organisationSubscriptionJson(orgName, phone, mobile)
      }
    }

    "should marshall correctly from json for individual with Secondary Contact as org" in  {
      forAll(stringsWithMaxLength(105), validContactNumber, validContactNumber) {
        (orgName, phone, mobile) =>
          Json.parse(jsonPayloadForSecondaryContact(
            orgName, phone, mobile
          )).validate[CreateSubscriptionForDACRequest].get mustBe secondaryContactSubscription(orgName, phone, mobile)
      }
    }

    "marshall into json subscription for individual with Secondary Contact as org" in {
      forAll(stringsWithMaxLength(105), validContactNumber, validContactNumber) {
        (orgName, phone, mobile) =>
          Json.toJson(secondaryContactSubscription(
            orgName, phone, mobile)) mustBe secondaryContactSubscriptionJson(orgName, phone, mobile)
      }
    }
  }

  "catch error if neither organisation or individual is present in PrimaryContact" in {
    forAll(validContactNumber, validContactNumber) {
      (phone, mobile) =>
        val error = intercept[Exception] {
          Json.parse(invalidJsonPayloadForIndividual).validate[CreateSubscriptionForDACRequest] mustBe individualSubcription(phone, mobile)
        }
        error.getMessage mustBe "Primary Contact must have either an organisation or individual element"
    }
  }

  "catch error if neither organisation or individual is present in SecondaryContact" in {
    forAll(stringsWithMaxLength(105), validContactNumber, validContactNumber) {
      (orgName, phone, mobile) =>
        val error = intercept[Exception] {
          Json.parse(invalidJsonPayloadForSecondaryContact).validate[CreateSubscriptionForDACRequest] mustBe secondaryContactSubscription(orgName, phone, mobile)
        }
        error.getMessage mustBe "Secondary Contact must have either an organisation or individual element"
    }
  }

  "must deserialise CreateSubscriptionForDACResponse" in {
    Json.parse(jsonPayloadCreateSubscriptionForDACResponse).validate[CreateSubscriptionForDACResponse].get mustBe createSubscriptionForDACResponse
  }

  "must serialise CreateSubscriptionForDACResponse" in {
    Json.toJson(createSubscriptionForDACResponse) mustBe createSubscriptionForDACResponseJson
  }
}
