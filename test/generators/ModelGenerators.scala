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

package generators

import java.time.LocalDate

import models.EnrolmentRequest.SubscriptionInfo
import models._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}
import uk.gov.hmrc.domain.Nino
import wolfendale.scalacheck.regexp.RegexpGen

trait ModelGenerators {
 self: Generators =>

  implicit val arbitraryName: Arbitrary[Name] = Arbitrary {
    for {
      firstName <- stringsWithMaxLength(50)
      secondName <- stringsWithMaxLength(50)
    } yield Name(firstName, secondName)
  }

  implicit val arbitraryNino: Arbitrary[Nino] = Arbitrary{
    for {
      prefix <- Gen.oneOf(Nino.validPrefixes)
      number <- Gen.choose(0, 999999)
      suffix <- Gen.oneOf(Nino.validSuffixes)
    } yield Nino(f"$prefix$number%06d$suffix")
  }

  implicit val arbitraryUtr: Arbitrary[Utr] = Arbitrary {
    for {
      value <- Gen.listOfN(10, Gen.chooseNum(0, 9)).map(_.mkString)
    } yield Utr(value)
  }

  implicit lazy val arbitraryLocalDate: Arbitrary[LocalDate] = Arbitrary {
    datesBetween(LocalDate.of(1900, 1, 1), LocalDate.of(2100, 1, 1))
  }

  implicit val arbitraryIndividualMatchingSubmission: Arbitrary[IndividualMatchingSubmission] = Arbitrary {
    for {
      name <- arbitrary[Name]
      dob <- arbitrary[LocalDate]
    } yield
      IndividualMatchingSubmission("DACSIX",
        requiresNameMatch = true,
        isAnAgent = false,
        Individual(name, dob))
  }

  implicit val arbitraryBusinessMatchingSubmission: Arbitrary[BusinessMatchingSubmission] = Arbitrary {
    for {
      organisationName <- RegexpGen.from("^[a-zA-Z0-9 '&\\\\/]{1,105}$")
      organisationType <- Gen.oneOf(partnerShip, limitedLiability, corporateBody, unIncorporatedBody, other)
    } yield
      BusinessMatchingSubmission("DACSIX",
        requiresNameMatch = true,
        isAnAgent = false,
        Organisation(organisationName, organisationType))
  }



  implicit val arbitraryEnrolmentRequest: Arbitrary[EnrolmentRequest] = {
    implicit val arbitraryIdentifier: Arbitrary[Identifier] = Arbitrary {
      for {
        key <- arbitrary[String]
        value <- arbitrary[String]
      } yield Identifier(key, value)
    }

    implicit val arbitraryVerifier: Arbitrary[Verifier] = Arbitrary {
      for {
        key <- arbitrary[String]
        value <- arbitrary[String]
      } yield Verifier(key, value)
    }

    Arbitrary {
      for {
        identifiers <- Gen.listOf(arbitrary[Identifier])
        verifiers <- Gen.listOf(arbitrary[Verifier])
      } yield
        EnrolmentRequest(identifiers, verifiers)
    }
  }

  implicit val arbitraryEnrolmentInfo: Arbitrary[SubscriptionInfo] = Arbitrary {for {
    safeId <- arbitrary[String]
    saUtr <- Gen.option(arbitrary[String])
    ctUtr <- Gen.option(arbitrary[String])
    nino <- Gen.option(arbitrary[String])
    nonUkPostcode <- Gen.option(arbitrary[String])

  } yield
    SubscriptionInfo(
      safeID = safeId,
      saUtr = saUtr,
      ctUtr = ctUtr,
      nino = nino,
      nonUkPostcode = nonUkPostcode)
  }

}
