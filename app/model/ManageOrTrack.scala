/*
 * Copyright 2024 HM Revenue & Customs
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

package model

import enumeratum._

import scala.collection.immutable

case class ManageOrTrack(choice: Option[String])

sealed abstract class ManageOrTrackOption extends EnumEntry {
  val value: String
}

object ManageOrTrackOptions extends Enum[ManageOrTrackOption] {

  override def values: immutable.IndexedSeq[ManageOrTrackOption] = findValues

  case object vrt extends ManageOrTrackOption {
    override val value: String = "vrt"
  }

  case object bank extends ManageOrTrackOption {
    override val value: String = "bank"
  }

  case object dd extends ManageOrTrackOption {
    override val value: String = "dd"
  }

  case object nodd extends ManageOrTrackOption {
    override val value: String = "nodd"
  }

  case object nobank extends ManageOrTrackOption {
    override val value: String = "nobank"
  }

}

