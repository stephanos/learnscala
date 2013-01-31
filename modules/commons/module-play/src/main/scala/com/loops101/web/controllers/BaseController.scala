package com.loops101.web.controllers

import com.loops101.system.config.ConfigUtil
import com.loops101.util._
import com.loops101.web.util.AboutUtil
import impl.{SecurityUtil, JsonUtil, IError}
import org.joda.time.DateTime
import play.api.mvc._

trait BaseController
  extends Controller
  with SecurityUtil
  with AboutUtil
  with JsonUtil
  with LogUtil
  with IdUtil {

  self: IError
    with ConfigUtil =>


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