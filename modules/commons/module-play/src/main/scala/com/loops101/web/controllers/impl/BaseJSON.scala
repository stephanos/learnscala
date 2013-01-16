package com.loops101.web.controllers.impl

import play.api.mvc._
import play.api.libs.json._
import Json._

trait BaseJSON {

    self: Controller =>


    // REPLY

    protected lazy val jOk: Result = jOk()

    protected def jOk(data: JsValue = Json.obj()): Result =
        jOk(stringify(data))

    protected def jOk(data: String): Result =
        Ok(data).as("application/json")

    protected def jBadRequest(msg: String) =
        BadRequest(stringify(Json.obj("errors" -> msg))).as("application/json")

    protected def jNotFound(msg: String) =
        NotFound(stringify(Json.obj("msg" -> msg))).as("application/json")


    // PRODUCE

    protected def stringify(js: JsValue): String =
        Json.stringify(js)


    // PARSE

    /*
    protected def json: BodyParser[JValue] =
        json(parse.DEFAULT_MAX_TEXT_LENGTH)

    protected def json(maxLength: Int): BodyParser[JValue] =
        parse.when(
            _.contentType.exists(m => m == "text/json" || m == "application/json"),
            parseJson(maxLength),
            request =>
                Play.maybeApplication.map(_.global.onBadRequest(request, "Expecting text/json or application/json body")).getOrElse(Results.BadRequest)
        )

    protected def parseJson(maxLength: Int): BodyParser[JValue] =
        BodyParser("json, maxLength=" + maxLength) {
            request =>
                Traversable.takeUpTo[Array[Byte]](maxLength).apply(Iteratee.consume[Array[Byte]]().map {
                    bytes =>
                        scala.util.control.Exception.allCatch[JValue].either {
                            net.liftweb.json.parse(new String(bytes, request.charset.getOrElse("utf-8")))
                        }.left.map {
                            e =>
                                (Play.maybeApplication.map(_.global.onBadRequest(request, "Invalid Json")).getOrElse(Results.BadRequest), bytes)
                        }
                }).flatMap(Iteratee.eofOrElse(Results.EntityTooLarge))
                    .flatMap {
                    case Left(b) => Done(Left(b), Empty)
                    case Right(it) => it.flatMap {
                        case Left((r, in)) => Done(Left(r), El(in))
                        case Right(xml) => Done(Right(xml), Empty)
                    }
                }
        }
        */
}
