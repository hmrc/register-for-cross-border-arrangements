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

package helpers

import models._
import play.api.libs.json.{JsObject, Json}

object SubscriptionJsonFixtures {

  // Individual Fixtures
  def jsonPayloadForIndividual(phone: String, mobile: String): String =
    s"""
      |{
      |  "createSubscriptionForDACRequest": {
      |    "requestCommon": {
      |      "regime": "DAC",
      |      "receiptDate": "2020-09-23T16:12:11Z",
      |      "acknowledgementReference": "AB123c",
      |      "originatingSystem": "MDTP",
      |      "requestParameters": [{
      |        "paramName":"Name",
      |        "paramValue":"Value"
      |      }]
      |    },
      |    "requestDetail": {
      |      "IDType": "idType",
      |      "IDNumber": "idNumber",
      |      "isGBUser": true,
      |      "primaryContact": {
      |        "individual": {
      |          "firstName": "Fairy",
      |          "lastName": "Liquid"
      |        },
      |        "email": "email2@email.com",
      |        "phone": "$phone",
      |        "mobile": "$mobile"
      |      }
      |    }
      |  }
      |}
      |""".stripMargin

  def individualSubscription(phone: String, mobile: String) =
    CreateSubscriptionForDACRequest(
      SubscriptionForDACRequest(
        RequestCommonForSubscription(
          "DAC",
          "2020-09-23T16:12:11Z",
          "AB123c",
          "MDTP",
          Some(
            Seq(
              RequestParameters(
                "Name",
                "Value"
              )
            )
          )
        ),
        RequestDetail(
          "idType",
          "idNumber",
          None,
          true,
          PrimaryContact(ContactInformationForIndividual(IndividualDetails("Fairy", None, "Liquid"), "email2@email.com", Some(phone), Some(mobile))),
          None
        )
      )
    )

  def IndividualSubscriptionJson(phone: String, mobile: String) =
    Json.obj(
      "createSubscriptionForDACRequest" -> Json.obj(
        "requestCommon" -> Json.obj(
          "regime"                   -> "DAC",
          "receiptDate"              -> "2020-09-23T16:12:11Z",
          "acknowledgementReference" -> "AB123c",
          "originatingSystem"        -> "MDTP",
          "requestParameters" -> Seq(
            Json.obj(
              "paramName"  -> "Name",
              "paramValue" -> "Value"
            )
          )
        ),
        "requestDetail" -> Json.obj(
          "IDType"   -> "idType",
          "IDNumber" -> "idNumber",
          "isGBUser" -> true,
          "primaryContact" -> Json.obj(
            "individual" -> Json.obj(
              "firstName" -> "Fairy",
              "lastName"  -> "Liquid"
            ),
            "email"  -> "email2@email.com",
            "phone"  -> s"$phone",
            "mobile" -> s"$mobile"
          )
        )
      )
    )

  //Organisation Fixtures
  def jsonPayloadForOrganisation(orgName: String, phoneNumber: String, mobile: String): String =
    s"""
      |{
      |  "createSubscriptionForDACRequest": {
      |    "requestCommon": {
      |      "regime": "DAC",
      |      "receiptDate": "2020-09-23T16:12:11Z",
      |      "acknowledgementReference": "AB123c",
      |      "originatingSystem": "MDTP",
      |      "requestParameters": [{
      |        "paramName":"Name",
      |        "paramValue":"Value"
      |      }]
      |    },
      |    "requestDetail": {
      |      "IDType": "idType",
      |      "IDNumber": "idNumber",
      |      "isGBUser": true,
      |      "primaryContact": {
      |        "organisation": {
      |          "organisationName": "$orgName"
      |        },
      |        "email": "email@email.com",
      |        "phone": "$phoneNumber",
      |        "mobile": "$mobile"
      |      }
      |    }
      |  }
      |}
      |""".stripMargin

  def organisationSubscription(orgName: String, phoneNumber: String, mobile: String): CreateSubscriptionForDACRequest =
    CreateSubscriptionForDACRequest(
      SubscriptionForDACRequest(
        RequestCommonForSubscription(
          "DAC",
          "2020-09-23T16:12:11Z",
          "AB123c",
          "MDTP",
          Some(
            Seq(
              RequestParameters(
                "Name",
                "Value"
              )
            )
          )
        ),
        RequestDetail(
          "idType",
          "idNumber",
          None,
          true,
          PrimaryContact(
            ContactInformationForOrganisation(OrganisationDetails(orgName), "email@email.com", Some(phoneNumber), Some(mobile))
          ),
          None
        )
      )
    )

  def organisationSubscriptionJson(orgName: String, phoneNumber: String, mobile: String): JsObject =
    Json.obj(
      "createSubscriptionForDACRequest" -> Json.obj(
        "requestCommon" -> Json.obj(
          "regime"                   -> "DAC",
          "receiptDate"              -> "2020-09-23T16:12:11Z",
          "acknowledgementReference" -> "AB123c",
          "originatingSystem"        -> "MDTP",
          "requestParameters" -> Seq(
            Json.obj(
              "paramName"  -> "Name",
              "paramValue" -> "Value"
            )
          )
        ),
        "requestDetail" -> Json.obj(
          "IDType"   -> "idType",
          "IDNumber" -> "idNumber",
          "isGBUser" -> true,
          "primaryContact" -> Json.obj(
            "organisation" -> Json.obj(
              "organisationName" -> orgName
            ),
            "email"  -> "email@email.com",
            "phone"  -> phoneNumber,
            "mobile" -> mobile
          )
        )
      )
    )

  // SecondaryContact Fixtures
  def jsonPayloadForSecondaryContact(name: String, phone: String, mobile: String): String =
    s"""
      |{
      |  "createSubscriptionForDACRequest": {
      |    "requestCommon": {
      |      "regime": "DAC",
      |      "receiptDate": "2020-09-23T16:12:11Z",
      |      "acknowledgementReference": "AB123c",
      |      "originatingSystem": "MDTP"
      |    },
      |    "requestDetail": {
      |      "IDType": "idType",
      |      "IDNumber": "idNumber",
      |      "isGBUser": true,
      |      "primaryContact": {
      |        "individual": {
      |          "firstName": "Fairy",
      |          "lastName": "Liquid"
      |        },
      |        "email": "email2@email.com"
      |      },
      |      "secondaryContact": {
      |        "organisation": {
      |          "organisationName": "$name"
      |        },
      |        "email": "email@email.com",
      |        "phone": "$phone",
      |        "mobile": "$mobile"
      |      }
      |    }
      |  }
      |}
      |""".stripMargin

  def secondaryContactSubscription(name: String, phone: String, mobile: String) =
    CreateSubscriptionForDACRequest(
      SubscriptionForDACRequest(
        RequestCommonForSubscription("DAC", "2020-09-23T16:12:11Z", "AB123c", "MDTP", None),
        RequestDetail(
          "idType",
          "idNumber",
          None,
          true,
          PrimaryContact(
            ContactInformationForIndividual(IndividualDetails("Fairy", None, "Liquid"), "email2@email.com", None, None)
          ),
          Some(
            SecondaryContact(
              ContactInformationForOrganisation(
                OrganisationDetails(
                  name
                ),
                "email@email.com",
                Some(phone),
                Some(mobile)
              )
            )
          )
        )
      )
    )

  def secondaryContactSubscriptionJson(name: String, phone: String, mobile: String) =
    Json.obj(
      "createSubscriptionForDACRequest" -> Json.obj(
        "requestCommon" -> Json.obj(
          "regime"                   -> "DAC",
          "receiptDate"              -> "2020-09-23T16:12:11Z",
          "acknowledgementReference" -> "AB123c",
          "originatingSystem"        -> "MDTP"
        ),
        "requestDetail" -> Json.obj(
          "IDType"   -> "idType",
          "IDNumber" -> "idNumber",
          "isGBUser" -> true,
          "primaryContact" -> Json.obj(
            "individual" -> Json.obj(
              "firstName" -> "Fairy",
              "lastName"  -> "Liquid"
            ),
            "email" -> "email2@email.com"
          ),
          "secondaryContact" -> Json.obj(
            "organisation" -> Json.obj(
              "organisationName" -> name
            ),
            "email"  -> "email@email.com",
            "phone"  -> phone,
            "mobile" -> mobile
          )
        )
      )
    )

  //Invalid Subscription Json
  def invalidJsonPayloadForIndividual: String =
    """
      |{
      |  "createSubscriptionForDACRequest": {
      |    "requestCommon": {
      |      "regime": "DAC",
      |      "receiptDate": "2020-09-23T16:12:11Z",
      |      "acknowledgementReference": "AB123c",
      |      "originatingSystem": "MDTP",
      |      "requestParameters": [{
      |        "paramName":"Name",
      |        "paramValue":"Value"
      |      }]
      |    },
      |    "requestDetail": {
      |      "IDType": "idType",
      |      "IDNumber": "idNumber",
      |      "isGBUser": true,
      |      "primaryContact": {
      |        "email": "email2@email.com",
      |        "phone": "01910002222",
      |        "mobile": "07500000000"
      |      }
      |    }
      |  }
      |}
      |""".stripMargin

  def invalidJsonPayloadForSecondaryContact: String =
    """
      |{
      |  "createSubscriptionForDACRequest": {
      |    "requestCommon": {
      |      "regime": "DAC",
      |      "receiptDate": "2020-09-23T16:12:11Z",
      |      "acknowledgementReference": "AB123c",
      |      "originatingSystem": "MDTP"
      |    },
      |    "requestDetail": {
      |      "IDType": "idType",
      |      "IDNumber": "idNumber",
      |      "isGBUser": true,
      |      "primaryContact": {
      |        "individual": {
      |          "firstName": "Fairy",
      |          "lastName": "Liquid"
      |        },
      |        "email": "email2@email.com"
      |      },
      |      "secondaryContact": {
      |        "email": "email@email.com",
      |        "phone": "0191 111 2222",
      |        "mobile": "07111111111"
      |      }
      |    }
      |  }
      |}
      |""".stripMargin

  //CreateSubscriptionForDacResponse Fixtures
  val jsonPayloadCreateSubscriptionForDACResponse: String =
    """
      |{
      |  "createSubscriptionForDACResponse": {
      |    "responseCommon": {
      |      "status": "OK",
      |      "statusText": "status",
      |      "processingDate": "2020-09-01T01:00:00Z",
      |      "returnParameters": [{
      |        "paramName":"Name",
      |        "paramValue":"Value"
      |      }]
      |    },
      |    "responseDetail": {
      |      "subscriptionID": "XADAC0000123456"
      |    }
      |  }
      |}
      |""".stripMargin

  val createSubscriptionForDACResponseJson = Json.obj(
    "createSubscriptionForDACResponse" -> Json.obj(
      "responseCommon" -> Json.obj(
        "status"         -> "OK",
        "statusText"     -> "status",
        "processingDate" -> "2020-09-01T01:00:00Z",
        "returnParameters" -> Json.arr(
          Json.obj(
            "paramName"  -> "Name",
            "paramValue" -> "Value"
          )
        )
      ),
      "responseDetail" -> Json.obj(
        "subscriptionID" -> "XADAC0000123456"
      )
    )
  )

  val createSubscriptionForDACResponse =
    CreateSubscriptionForDACResponse(
      SubscriptionForDACResponse(
        ResponseCommon(status = "OK",
                       statusText = Some("status"),
                       processingDate = "2020-09-01T01:00:00Z",
                       returnParameters = Some(Seq(ReturnParameters("Name", "Value")))
        ),
        ResponseDetailForDACSubscription(subscriptionID = "XADAC0000123456")
      )
    )

}
