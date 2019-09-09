/*
 * Copyright 2019 HM Revenue & Customs
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

package views

import javax.inject.Inject

class Views @Inject() (
    val no_vat_repayments: _root_.views.html.no_vat_repayments,
    val one_payment:       _root_.views.html.one_payment,
    val helloPage:         _root_.views.html.hello_page,

    //error pages
    val errorTemplate: _root_.views.html.error.error_template

)
