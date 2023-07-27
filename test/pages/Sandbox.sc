object SandBox {
  object Expected {
    val mainText: String =
      """You cannot use this service
        |You cannot use the track your VAT repayments service because your VAT registration has been cancelled.
        |Call us on 0300 200 3835 if you cannot track VAT repayments online.
        |Our opening times are Monday to Friday, 8am to 6pm. We are closed on weekends and bank holidays.
        |If you need extra support
        |Find out the different ways to deal with HMRC if you need some help.
        |You can also use Relay UK if you cannot hear or speak on the phone: dial 18001 then 0345 300 3900.
        |If you are outside the UK: +44 2890 538 192
        |Before you call, make sure you have:
        |your VAT registration number. This is 9 numbers, for example, 123456789
        |your bank details
        """.stripMargin
  }

  val expectedLines = Expected.mainText.stripSpaces().split("/n")

  implicit class StringOps(s: String) {
    /**
     * Transforms string so it's easier it to compare.
     * It also replaces `unchecked`
     *
     */
    def stripSpaces(): String = s
      .replaceAll("unchecked", "") //when you run tests from intellij webdriver.getText adds extra 'unchecked' around selection
      .replaceAll("[^\\S\\r\\n]+", " ") //replace many consecutive white-spaces (but not new lines) with one space
      .replaceAll("[\r\n]+", "\n") //replace many consecutive new lines with one new line
      .split("\n").map(_.trim) //trim each line
      .filterNot(_ == "") //remove any empty lines
      .mkString("\n")
  }

}

println(SandBox.expectedLines.toString)



