package com.crashnote.util

/*
package com.crashnote.util

import com.mongodb.casbah.Imports._
import com.crashnote.model.dto.BaseDoc

object DocUtil {

    implicit val ctx = new Context {
        val name = Some("MyContext")
        override val defaultEnumStrategy = EnumStrategy.BY_ID
    }

    // ==== DBOBJECT -> DOCUMENT

    def toDOCs[D <: BaseDoc with CaseClass : Manifest](dbos: List[DBObject]): List[D] =
        dbos.map(dbo => toDOC(dbo))

    def toDOC[D <: BaseDoc with CaseClass : Manifest](dbo: Option[DBObject]): Option[D] = dbo match {
        case Some(obj) => Some(toDOC(obj))
        case None => None
    }

    def toDOC[D <: BaseDoc with CaseClass : Manifest](dbo: DBObject): D = {
        val doc = grater[D].asObject(dbo)
        val id = dbo.get("_id")
        if (id != null) doc.setId(id.toString)
        doc
    }

    // ==== DOCUMENT -> DBOBJECT

    def toDBO[D <: BaseDoc with CaseClass : Manifest](doc: D): DBObject = {
        val dbo = grater[D].asDBObject(doc)
        if (doc.hasId) dbo.put("_id", doc.id()) // prevent id generation if doc already has one
        shrinkDBO(dbo)
    }

    private def shrinkDBO(dbo: DBObject): DBObject = {
        def isDBO(v: AnyRef) = v.isInstanceOf[DBObject]
        def isEmptyDBO(v: AnyRef) = isDBO(v) && v.asInstanceOf[DBObject].isEmpty
        def shrink(v: AnyRef) = if (isDBO(v)) shrinkDBO(v.asInstanceOf[DBObject]) else v

        dbo match {
            case _: BasicDBList =>
                val ls = new BasicDBList()
                (dbo.mapValues(shrink(_)).filter(!isEmptyDBO(_)).values).foreach(ls.add(_))
                ls
            case _ =>
                dbo.filter(e => e._1.length <= 3) // don't allow keys with more than 3 characters
                        .mapValues(v => shrink(v)) // recursively
                        .filter(e => !isEmptyDBO(e._2)) // finally: purge empty lists
        }
    }

}*/
