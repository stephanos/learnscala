package com.loops101.unit.model

import com.loops101.UnitSpec
import com.loops101.util.JSONUtil
import com.loops101.model.dto.mail._

class MailVOSpec extends UnitSpec {

    "Mail VO" should {

        import JSONUtil._

        "serialize and deserialize" >> {
            val s = MailSenderVO("me@example.com", None)
            val r = MailRecipientVO("him@example.com", None)
            val c = MailContentVO("fired", "you are fired")
            val m = MailVO(0, c, Map(), s, r)

            val res = cin[MailVO](cout(m))

            res.from.email === Some("me@example.com")
            res.to.email === List("him@example.com")
            res.content.subject === "fired"
            res.content.msg === "you are fired"
        }
    }
}