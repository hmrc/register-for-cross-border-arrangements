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

  val subscriptionIndividualJsonPayload =
    """{
      |"createSubscriptionForDACRequest": {
      |"requestCommon": {
      |"regime": "DAC",
      |"receiptDate": "2020-09-12T18:03:45Z",
      |"acknowledgementReference": "abcdefghijklmnopqrstuvwxyz123456",
      |"originatingSystem": "MDTP",
      |"parameters": null
      |},
      |"requestDetail": {
      |"IDType": "SAFE",
      |"IDNumber": "AB123456Z",
      |"tradingName": "Tools for Traders Limited",
      |"isGBUser": true,
      |"primaryContact": {
      |"individual": {
      |"firstName": "TIMMY",
      |"lastName": "MALLET"
      |},
      |"email": "timmy@toolsfortraders.com",
      |"phone": "0191000000",
      |"mobile": "07123456789"
      |},
      |"secondaryContact": {
      |"individual": {
      |"firstName": "JIMMY",
      |"lastName": "NAIL"
      |},
      |"email": "Jimmy@toolsfortraders.com",
      |"phone": "0191000000",
      |"mobile": "07123456789"
      |}
      |}
      |}
      |}""".stripMargin


  val subscriptionIndividual =
    CreateSubscriptionForDACRequest(
    SubscriptionForDACRequest(
    RequestCommonForSubscription(
      "DAC",
      "2020-09-12T18:03:45Z",
      "abcdefghijklmnopqrstuvwxyz123456",
      "MDTP",
      None),
    RequestDetail(
      "SAFE",
      "AB123456Z",
      Some("Tools for Traders Limited"),
      true,
      PrimaryContact(ContactInformationForIndividual(
        IndividualDetails("TIMMY", None, "MALLET"),
        "timmy@toolsfortraders.com",
        Some("01910000000"),
        Some("07123456789"))),
      Some(SecondaryContact(ContactInformationForIndividual(
        IndividualDetails("JIMMY", None, "NAIL"),
        "jimmy@toolsfortraders.com",
        Some("01910000000"),
        Some("07123456789")
          )
        )
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

}
