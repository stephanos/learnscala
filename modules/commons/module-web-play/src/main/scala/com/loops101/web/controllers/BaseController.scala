package com.loops101.web.controllers

import impl.{BaseSecure, BaseJSON, BaseErrors}
import play.api.mvc._
import com.loops101.util._
import java.util.Date
import com.loops101.web.util.WebUtil

trait BaseController
    extends Controller
    with BaseSecure with WebUtil with BaseErrors with BaseJSON with LogUtil {

    protected def gOk(data: String, minsize: Int) =
        Ok(data) // TODO ? does Cloudflare do this ?

    protected def code2id(code: String): Int =
        code match {
            case null | "" => -1
            case _ => IDUtil.unobfuscate(code).toInt
        }

    protected def id2code(id: Long): String =
        IDUtil.obfuscate(id)

    protected def date2code(id: Date): String =
        IDUtil.urlShrink(id)

    protected def code2date(code: String): Date =
        new Date(IDUtil.urlUnshrink(code).toLong)
}