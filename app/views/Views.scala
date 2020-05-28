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

package views

import javax.inject.Inject

class Views @Inject() (
    val no_vat_repayments:      _root_.views.html.no_vat_repayments,
    val view_repayment_account: _root_.views.html.view_repayment_account,
    val manage_or_track:        _root_.views.html.manage_or_track,
    val completed:              _root_.views.html.completed,
    val inprogress:             _root_.views.html.inprogress,
    val inprogress_completed:   _root_.views.html.inprogress_completed,
    val view_progress:          _root_.views.html.view_progress,
    //shutter
    val shuttered: _root_.views.html.shuttered,
    //error pages
    val errorTemplate: _root_.views.html.error.error_template,
    val non_mtd_user:  _root_.views.html.non_mtd_user,
    //classic
    val classic_none: _root_.views.html.classic.no_vat_repayments_classic,
    val classic_some: _root_.views.html.classic.vat_repayments_classic

)
