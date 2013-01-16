package com.loops101.web.controllers

import impl.{BaseSecure, BaseJSON, BaseErrors}
import play.api.mvc._
import com.loops101.util._
import java.util.Date
import com.loops101.web.util.WebUtil
import org.joda.time.DateTime

trait BaseController
    extends Controller
    with BaseSecure with WebUtil with BaseErrors with BaseJSON with LogUtil with IDUtil {

    protected def gOk(data: String, minsize: Int) =
        Ok(data) // TODO ? does Cloudflare do this ?

    protected def code2id(code: String): Int =
        code match {
            case null | "" => -1
            case _ => idUtil.unobfuscate(code).toInt
        }

    protected def id2code(id: Long): String =
      idUtil.obfuscate(id)

    protected def date2code(id: DateTime): String =
      idUtil.urlShrink(id)

    protected def code2date(code: String): DateTime =
        new DateTime(idUtil.urlUnshrink(code))
}