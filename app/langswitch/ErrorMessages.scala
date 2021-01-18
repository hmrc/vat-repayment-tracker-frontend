/*
 * Copyright 2021 HM Revenue & Customs
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

package langswitch

import model.Vrn

object ErrorMessages {

  val badRequest400Title: Message = Message(
    "Bad request - 400"
  )
  val badRequest400Heading: Message = Message(
    "Bad request"
  )
  val badRequest400Message: Message = Message(
    "Please check that you have entered the correct web address."
  )

  val pageNotFound404Title: Message = Message(
    "Page not found - 404"
  )
  val pageNotFound404Heading: Message = Message(
    "This page can’t be found"
  )
  val pageNotFound404Message: Message = Message(
    "Please check that you have entered the correct web address."
  )

  val authProblemTitle: Message = Message(
    "More than one session is open"
  )
  val authProblemHeading: Message = Message(
    "More than one session is open"
  )
  val authProblemMessage: Message = Message(
    "To continue, please close one session or start again."
  )

  val internalServerError500Title: Message = Message(
    "Sorry, we are experiencing technical difficulties - 500"
  )
  val internalServerError500Heading: Message = Message(
    "Sorry, we’re experiencing technical difficulties"
  )
  val internalServerError500Message: Message = Message(
    "Please try again in a few minutes."
  )
  val `You do not have access to this service`: Message = Message(
    english = "You do not have access to this service"
  )
  val `choose an option`: Message = Message(
    "Select whether to manage your accounts or track a VAT repayment"
  )
  val `VRN missing`: Message = Message(
    "VRN missing"
  )

  val `general error title`: Message = Message(
    "Sorry, there is a problem with the service"
  )

  val `try again later`: Message = Message(
    "Try again later."
  )

  def `You do not have access to this service`(vrn: Vrn): Message = Message(
    english = s"""You do not have access to this service for VRN : ${vrn.value}"""
  )

}
