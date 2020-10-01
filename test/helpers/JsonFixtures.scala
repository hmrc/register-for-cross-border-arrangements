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

import java.time.LocalDate

import models._
import play.api.libs.json.Json

object JsonFixtures {
  val jsonPayload =
    """{
      |"registerWithoutIDRequest": {
      |"requestCommon": {
      |"receiptDate": "2001-12-17",
      |"regime": "DAC",
      |"acknowledgementReference":
      |"ec031b045855445e96f98569ds56cd22",
      |"parameters": null
      |},
      |"requestDetail": {
      |"individual": {
      |"firstName": "FIRST NAME",
      |"lastName": "LAST NAME",
      |"dateOfBirth": "1999-01-23"
      |},
      |"address": {
      |"addressLine1": "ADDRESS1",
      |"addressLine2": "ADDRESS2",
      |"addressLine3": "ADDRESS3",
      |"addressLine4": "ADDRESS4",
      |"postalCode": "bh1 3fg",
      |"countryCode": "GB"
      |},
      |"contactDetails": {
      |"phoneNumber": "878798798798",
      |"mobileNumber": "7698908090",
      |"faxNumber": "837587345",
      |"emailAddress": "ABC@YAHOO.COM"
      |}
      |}
      |}
      |}""".stripMargin

  val invalidJsonPayload =
    """{
      |"registerWithoutIDRequest": {
      |"requestCommon": {
      |"receiptDate": "2001-12-17",
      |"regime": "DAC",
      |"acknowledgementReference":
      |"ec031b045855445e96f98569ds56cd22",
      |"parameters": null
      |},
      |"requestDetail": {
      |"organisation": {
      |"organisationName": "org1"
      |},
      |"individual": {
      |"firstName": "FIRST NAME",
      |"lastName": "LAST NAME",
      |"dateOfBirth": "1999-01-23"
      |},
      |"address": {
      |"addressLine1": "ADDRESS1",
      |"addressLine2": "ADDRESS2",
      |"addressLine3": "ADDRESS3",
      |"addressLine4": "ADDRESS4",
      |"postalCode": "bh1 3fg",
      |"countryCode": "GB"
      |},
      |"contactDetails": {
      |"phoneNumber": "878798798798",
      |"mobileNumber": "7698908090",
      |"faxNumber": "837587345",
      |"emailAddress": "ABC@YAHOO.COM"
      |}
      |}
      |}
      |}""".stripMargin

  val invalidJsonPayloadDual =
    """{
      |"registerWithoutIDRequest": {
      |"requestCommon": {
      |"receiptDate": "2001-12-17",
      |"regime": "DAC",
      |"acknowledgementReference":
      |"ec031b045855445e96f98569ds56cd22",
      |"parameters": null
      |},
      |"requestDetail": {
      |"address": {
      |"addressLine1": "ADDRESS1",
      |"addressLine2": "ADDRESS2",
      |"addressLine3": "ADDRESS3",
      |"addressLine4": "ADDRESS4",
      |"postalCode": "bh1 3fg",
      |"countryCode": "GB"
      |},
      |"contactDetails": {
      |"phoneNumber": "878798798798",
      |"mobileNumber": "7698908090",
      |"faxNumber": "837587345",
      |"emailAddress": "ABC@YAHOO.COM"
      |}
      |}
      |}
      |}""".stripMargin

  val jsonSubWithOrg  =
    """{
      |"registerWithoutIDRequest": {
      |"requestCommon": {
      |"receiptDate": "2001-12-17",
      |"regime": "DAC",
      |"acknowledgementReference":
      |"ec031b045855445e96f98569ds56cd22",
      |"parameters": null
      |},
      |"requestDetail": {
      |"organisation": {
      |"organisationName": "org1"
      |},
      |"address": {
      |"addressLine1": "ADDRESS1",
      |"addressLine2": "ADDRESS2",
      |"addressLine3": "ADDRESS3",
      |"addressLine4": "ADDRESS4",
      |"postalCode": "bh1 3fg",
      |"countryCode": "GB"
      |},
      |"contactDetails": {
      |"phoneNumber": "878798798798",
      |"mobileNumber": "7698908090",
      |"faxNumber": "837587345",
      |"emailAddress": "ABC@YAHOO.COM"
      |}
      |}
      |}
      |}""".stripMargin

  val sub = Registration(
    RegisterWithoutIDRequest(
      RequestCommon("2001-12-17", "DAC", "ec031b045855445e96f98569ds56cd22", None),
      RequestDetails(
        None,
        Some(NoIdIndividual(
          Name("FIRST NAME", "LAST NAME"),
          LocalDate.parse("1999-01-23")
        )),
        Address(
          "ADDRESS1",
          Some("ADDRESS2"),
          "ADDRESS3",
          Some("ADDRESS4"),
          Some("bh1 3fg"),
          "GB"
        ),
        ContactDetails(
          Some("878798798798"),
          Some("7698908090"),
          Some("837587345"),
          Some("ABC@YAHOO.COM")
        ),
        None
      )
    )
  )

  val subWithOrg = Registration(
    RegisterWithoutIDRequest(
      RequestCommon("2001-12-17", "DAC", "ec031b045855445e96f98569ds56cd22", None),
      RequestDetails(
        Some(NoIdOrganisation("org1")),
        None,
        Address(
          "ADDRESS1",
          Some("ADDRESS2"),
          "ADDRESS3",
          Some("ADDRESS4"),
          Some("bh1 3fg"),
          "GB"
        ),
        ContactDetails(
          Some("878798798798"),
          Some("7698908090"),
          Some("837587345"),
          Some("ABC@YAHOO.COM")
        ),
        None
      )
    )
  )

  val subJson = Json.obj(
    "registerWithoutIDRequest" -> Json.obj(
      "requestCommon" ->
        Json.obj(
          "receiptDate" -> "2001-12-17",
          "regime" -> "DAC",
          "acknowledgementReference" -> "ec031b045855445e96f98569ds56cd22"
        ),
      "requestDetail" -> Json.obj(
        "individual" -> Json.obj(
          "firstName" -> "FIRST NAME",
          "lastName" -> "LAST NAME",
          "dateOfBirth" -> "1999-01-23"
        ),
        "address" -> Json.obj(
          "addressLine1" -> "ADDRESS1",
          "addressLine2" -> "ADDRESS2",
          "addressLine3" -> "ADDRESS3",
          "addressLine4" -> "ADDRESS4",
          "postalCode" -> "bh1 3fg",
          "countryCode" -> "GB"
        ),
        "contactDetails" -> Json.obj(
          "phoneNumber" -> "878798798798",
          "mobileNumber" -> "7698908090",
          "faxNumber" -> "837587345",
          "emailAddress" -> "ABC@YAHOO.COM"
        )

      )
    )
  )

  val subJsonWithOrg = Json.obj(
    "registerWithoutIDRequest" -> Json.obj(
      "requestCommon" ->
        Json.obj(
          "receiptDate" -> "2001-12-17",
          "regime" -> "DAC",
          "acknowledgementReference" -> "ec031b045855445e96f98569ds56cd22"
        ),
      "requestDetail" -> Json.obj(
        "organisation" -> Json.obj("organisationName" -> "org1"),
        "address" -> Json.obj(
          "addressLine1" -> "ADDRESS1",
          "addressLine2" -> "ADDRESS2",
          "addressLine3" -> "ADDRESS3",
          "addressLine4" -> "ADDRESS4",
          "postalCode" -> "bh1 3fg",
          "countryCode" -> "GB"
        ),
        "contactDetails" -> Json.obj(
          "phoneNumber" -> "878798798798",
          "mobileNumber" -> "7698908090",
          "faxNumber" -> "837587345",
          "emailAddress" -> "ABC@YAHOO.COM"
        )

      )
    )
  )
  val registerWithIDPayload =
    """
      |{
      |"registerWithIDRequest": {
      |"requestCommon": {
      |"regime": "DAC",
      |"receiptDate": "2016-08-16T15:55:30Z",
      |"acknowledgementReference": "ec031b045855445e96f98a569ds56cd2",
      |"requestParameters": [
      |{
      |"paramName": "REGIME",
      |"paramValue": "DAC"
      |}
      |]
      |},
      |"requestDetail": {
      |"IDType": "NINO",
      |"IDNumber": "0123456789",
      |"requiresNameMatch": true,
      |"isAnAgent": false,
      |"individual": {
      |"firstName": "Fred",
      |"middleName": "Flintstone",
      |"lastName": "Flint",
      |"dateOfBirth": "1999-12-20"
      |}
      |}
      |}
      |}""".stripMargin

  //Subscription Fixtures

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
      PrimaryContact(
        IndividualDetails("Fairy", None, "Liquid"),
        "email2@email.com",
        Some("01910002222"),
        Some("07500000000")),
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
            "phone" -> "01910002222", //TODO - fix nullable values
            "mobile" -> "07500000000" //TODO - fix nullable values
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
            OrganisationDetails("Pizza for you"),
            "email@email.com",
            Some("0191 111 2222"),
            Some("07111111111")),
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

  //TODO - fix nullable values for phone & mobile fixture

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

  //TODO - fix nullable values for phone & mobile fixture

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
        IndividualDetails("Fairy", None, "Liquid"),
        "email2@email.com",
        None,
        None),
      Some(SecondaryContact(
        OrganisationDetails(
          "Pizza for you"),
        "email@email.com",
        Some("0191 111 2222"),
        Some("07111111111")
          )
        )
      )
    )
  )

  //TODO - fix nullable values for phone & mobile fixture

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
          "individual" -> Json.obj(
            "firstName" -> "JIMMY",
            "lastName" -> "NAIL"
          ),
          "email"-> "jimmy@toolsfortraders.com",
          "phone" -> "01910000000",
          "mobile" -> "07123456789"
        )
      )
    )
  )


val registerWithIDJson = Json.obj(
  "registerWithIDRequest" -> Json.obj(
    "requestCommon" -> Json.obj(
        "regime" -> "DAC",
              "receiptDate" -> "2016-08-16T15:55:30Z",
              "acknowledgementReference" -> "ec031b045855445e96f98a569ds56cd2",
              "requestParameters" -> Json.arr( Json.obj(
                "paramName" -> "REGIME",
                "paramValue" -> "DAC"
              ))
        ),
      "requestDetail" -> Json.obj(
        "IDType" -> "NINO",
        "IDNumber" -> "0123456789",
        "requiresNameMatch" -> true,
        "isAnAgent" -> false,
        "individual" -> Json.obj(
          "firstName" -> "Fred",
          "middleName" -> "Flintstone",
          "lastName" -> "Flint",
          "dateOfBirth" -> "1999-12-20"
        )
      )
    )
  )

  val registrationWithRequest = PayloadRegisterWithID(RegisterWithIDRequest(
    RequestCommon("2016-08-16T15:55:30Z", "DAC", "ec031b045855445e96f98a569ds56cd2",
  Some(Seq(RequestParameter("REGIME", "DAC")))),
    RequestWithIDDetails(
      "NINO",
      "0123456789",
      requiresNameMatch = true,
      isAnAgent = false,
      WithIDIndividual("Fred", Some("Flintstone"), "Flint", "1999-12-20")))
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
}
