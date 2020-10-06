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

package helpers

import models._
import play.api.libs.json.Json

object SubscriptionJsonFixtures {

  // Individual Fixtures
  val jsonPayloadForIndividual: String =
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
      |      "idType": "idType",
      |      "idNumber": "idNumber",
      |      "isGBUser": true,
      |      "primaryContact": {
      |        "individual": {
      |          "firstName": "Fairy",
      |          "lastName": "Liquid"
      |        },
      |        "email": "email2@email.com",
      |        "phone": "01910002222",
      |        "mobile": "07500000000"
      |      }
      |    }
      |  }
      |}
      |""".stripMargin

  val individualSubcription =
    CreateSubscriptionForDACRequest(
      SubscriptionForDACRequest(
        RequestCommonForSubscription(
          "DAC",
          "2020-09-23T16:12:11Z",
          "AB123c",
          "MDTP",
          Some(Seq(RequestParameters(
            "Name",
            "Value"
          )))
        ),
        RequestDetail(
          "idType",
          "idNumber",
          None,
          true,
          PrimaryContact(ContactInformationForIndividual(
            IndividualDetails("Fairy", None, "Liquid"),
            "email2@email.com",
            Some("01910002222"),
            Some("07500000000"))
          ),
          None
        )
      )
    )

  val IndividualSubscriptionJson =
    Json.obj(
      "createSubscriptionForDACRequest" -> Json.obj(
        "requestCommon" -> Json.obj(
          "regime" -> "DAC",
          "receiptDate" -> "2020-09-23T16:12:11Z",
          "acknowledgementReference" -> "AB123c",
          "originatingSystem" -> "MDTP",
          "requestParameters" -> Seq(Json.obj(
            "paramName" -> "Name",
            "paramValue" -> "Value"
          )
          )),
        "requestDetail" -> Json.obj(
          "idType" -> "idType",
          "idNumber" -> "idNumber",
          "isGBUser" -> true,
          "primaryContact" -> Json.obj(
            "individual" -> Json.obj(
              "firstName" -> "Fairy",
              "lastName" -> "Liquid"
            ),
            "email" -> "email2@email.com",
            "phone" -> "01910002222",
            "mobile" -> "07500000000"
          )
        )
      )
    )

  //Organisation Fixtures
  val jsonPayloadForOrganisation: String =
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
      |      "idType": "idType",
      |      "idNumber": "idNumber",
      |      "isGBUser": true,
      |      "primaryContact": {
      |        "organisation": {
      |          "organisationName": "Pizza for you"
      |        },
      |        "email": "email@email.com",
      |        "phone": "0191 111 2222",
      |        "mobile": "07111111111"
      |      }
      |    }
      |  }
      |}
      |""".stripMargin

  val organisationSubscription: CreateSubscriptionForDACRequest =
    CreateSubscriptionForDACRequest(
      SubscriptionForDACRequest(
        RequestCommonForSubscription(
          "DAC",
          "2020-09-23T16:12:11Z",
          "AB123c",
          "MDTP",
          Some(Seq(RequestParameters(
            "Name",
            "Value"
          )))
        ),
        RequestDetail(
          "idType",
          "idNumber",
          None,
          true,
          PrimaryContact(
            ContactInformationForOrganisation(
              OrganisationDetails("Pizza for you"),
              "email@email.com",
              Some("0191 111 2222"),
              Some("07111111111"))
          ),
          None
        )
      )
    )

  val organisationSubscriptionJson =
    Json.obj(
      "createSubscriptionForDACRequest" -> Json.obj(
        "requestCommon" -> Json.obj(
          "regime" -> "DAC",
          "receiptDate" -> "2020-09-23T16:12:11Z",
          "acknowledgementReference" -> "AB123c",
          "originatingSystem" -> "MDTP",
          "requestParameters" -> Seq(Json.obj(
            "paramName" -> "Name",
            "paramValue" -> "Value"
          ))
        ),
        "requestDetail" -> Json.obj(
          "idType" -> "idType",
          "idNumber" -> "idNumber",
          "isGBUser" -> true,
          "primaryContact" -> Json.obj(
            "organisation" -> Json.obj(
              "organisationName" -> "Pizza for you"
            ),
            "email" -> "email@email.com",
            "phone" -> "0191 111 2222",
            "mobile" -> "07111111111"
          )
        )
      )
    )

  // SecondaryContact Fixtures
  val jsonPayloadForSecondaryContact: String =
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
      |      "idType": "idType",
      |      "idNumber": "idNumber",
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
      |          "organisationName": "Pizza for you"
      |        },
      |        "email": "email@email.com",
      |        "phone": "0191 111 2222",
      |        "mobile": "07111111111"
      |      }
      |    }
      |  }
      |}
      |""".stripMargin

  val secondaryContactSubscription =
    CreateSubscriptionForDACRequest(
      SubscriptionForDACRequest(
        RequestCommonForSubscription(
          "DAC",
          "2020-09-23T16:12:11Z",
          "AB123c",
          "MDTP",
          None),
        RequestDetail(
          "idType",
          "idNumber",
          None,
          true,
          PrimaryContact(
            ContactInformationForIndividual(
              IndividualDetails("Fairy", None, "Liquid"),
              "email2@email.com",
              None,
              None)
          ),
          Some(SecondaryContact(
            ContactInformationForOrganisation(
              OrganisationDetails(
                "Pizza for you"
              ),
              "email@email.com",
              Some("0191 111 2222"),
              Some("07111111111")
            )
          )
          )
        )
      ))

  val secondaryContactSubscriptionJson =
    Json.obj(
      "createSubscriptionForDACRequest" -> Json.obj(
        "requestCommon" -> Json.obj(
          "regime" -> "DAC",
          "receiptDate" -> "2020-09-23T16:12:11Z",
          "acknowledgementReference" -> "AB123c",
          "originatingSystem" -> "MDTP"
        ),
        "requestDetail" -> Json.obj(
          "idType" -> "idType",
          "idNumber" -> "idNumber",
          "isGBUser" -> true,
          "primaryContact" -> Json.obj(
            "individual" -> Json.obj(
              "firstName" -> "Fairy",
              "lastName" -> "Liquid"
            ),
            "email" -> "email2@email.com"
          ),
          "secondaryContact" -> Json.obj(
            "organisation" -> Json.obj(
              "organisationName" -> "Pizza for you"
            ),
            "email" -> "email@email.com",
            "phone" -> "0191 111 2222",
            "mobile" -> "07111111111"
          )
        )
      )
    )

  val subscriptionOrganisationJson =
    Json.obj(
      "createSubscriptionForDACRequest" -> Json.obj(
        "requestCommon" -> Json.obj(
          "regime" -> "DAC",
          "receiptDate" -> "2020-09-12T18:03:45Z",
          "acknowledgementReference" -> "abcdefghijklmnopqrstuvwxyz123456",
          "originatingSystem" -> "MDTP"
        ),
        "requestDetail" -> Json.obj(
          "idType" -> "SAFE",
          "idNumber" -> "AB123456Z",
          "tradingName" -> "Tools for Traders Limited",
          "isGBUser" -> true,
          "primaryContact" -> Json.obj(
            "organisation" -> Json.obj(
              "organisationName" -> "Tools for Traders Limited"
            ),
            "email" -> "timmy@toolsfortraders.com",
            "phone" -> "01910000000",
            "mobile" -> "07123456789"
          )
        )
      )
    )

  //Invalid Subscription Json
  val invalidJsonPayloadForIndividual: String =
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
      |      "idType": "idType",
      |      "idNumber": "idNumber",
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

  val invalidJsonPayloadForSecondaryContact: String =
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
      |      "idType": "idType",
      |      "idNumber": "idNumber",
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
        "status" -> "OK",
        "statusText" -> "status",
        "processingDate" -> "2020-09-01T01:00:00Z",
        "returnParameters" -> Json.arr(
          Json.obj(
            "paramName" -> "Name",
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
          returnParameters = Some(Seq(ReturnParameters("Name", "Value")))),
        ResponseDetailForDACSubscription(
          subscriptionID = "XADAC0000123456")
      )
    )

}
