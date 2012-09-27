package com.loops101.data.mongo.model

import com.mongodb._
import scala.collection.JavaConversions._

import com.foursquare.rogue._
import net.liftweb.json.JsonDSL._
import net.liftweb.mongodb.record._
import net.liftweb.record._
import net.liftweb.mongodb._
import net.liftweb.json.JsonAST._
import net.liftweb.common.Box
import com.loops101.util._

trait MyMongoMetaRecord[T <: MongoRecord[T]]
    extends IndexedRecord[T] with MyBsonMetaRecord[T] {

    self: MongoMetaRecord[T] with MongoMeta[T] =>

    val maxSize: Int = 0
    val maxLength: Int = 0

    def createColl() {
        useDb {
            db =>
                if (!db.getCollectionNames.find(_ == collectionName).isDefined) {
                    val opt: DBObject =
                        if (maxSize > 0) {
                            val r = BasicDBObjectBuilder.start
                                .append("capped", true)
                                .append("size", maxSize * 1024 * 1024)
                            if(maxLength > 0) r.append("max", maxLength)
                            r.get
                        } else
                            BasicDBObjectBuilder.start.get()
                    db.createCollection(collectionName, opt)
                }
        }
        initColl()
    }

    protected def initColl() {}

    def dropColl() {
        useColl {
            col => col.drop()
        }
    }

    def name(n: String, prefix: String) =
        prefix + "." + EnvUtil.modeCode + "." + n

    protected lazy val sparse: JObject = ("sparse" -> true)
    protected lazy val unique: JObject = ("unique" -> true)
    protected lazy val bg: JObject = ("background" -> true)

    protected def idx(i: MongoIndex1[_ <: MongoRecord[T], _ <: Field[_, T], _ <: IndexModifier]): JObject =
        (i.f1.name -> i.m1.value.toString.toLong)

    protected def idx(i: MongoIndex2[_ <: MongoRecord[T],
        _ <: Field[_, T], _ <: IndexModifier,
        _ <: Field[_, T], _ <: IndexModifier]): JObject =
        (i.f1.name -> i.m1.value.toString.toLong) ~ (i.f2.name -> i.m2.value.toString.toLong)

    protected def idx(i: MongoIndex3[_ <: MongoRecord[T],
        _ <: Field[_, T], _ <: IndexModifier,
        _ <: Field[_, T], _ <: IndexModifier,
        _ <: Field[_, T], _ <: IndexModifier]): JObject =
        (i.f1.name -> i.m1.value.toString.toLong) ~ (i.f2.name -> i.m2.value.toString.toLong) ~
            (i.f3.name -> i.m3.value.toString.toLong)
}

trait MyBsonMetaRecord[T <: BsonRecord[T]] {

    self: BsonMetaRecord[T] =>

    def create =
        self.createRecord

    /**
     * Use "display name" for long names and "name" for short ones
     */
    override def fieldByName(name: String, inst: T): Box[Field[_, T]] =
        name.length == 1 match {
            case true => Box(fields(inst).filter(_.name == name))
            case _ => Box(fields(inst).filter(_.displayName == name))
        }

    /**
     * Use "display name" (long) instead of "name" (short code)
     */
    override def asJValue(rec: T): JObject =
        JObject(fields(rec).flatMap {
            f =>
                (f.asJValue match {
                    case JObject(List()) => None
                    case JArray(List()) => None
                    case JNothing => None
                    case v => Some(v)
                }).map(JField(f.displayName, _)) // filter out empty objects
        })

    /**
     * Overrides default to use short key names
     */
    /*
    override def asDBObject(inst: T): DBObject = {
        val dbo = BasicDBObjectBuilder.start
        for (field <- myFields(inst); dbValue <- fieldDbValue(field.asInstanceOf[Field[_, T]])) {
            dbo.add(field.code, dbValue)
        }
        dbo.get
    }

    override def setFieldsFromDBObject(inst: T, dbo: DBObject) {
        val fields = myFields(inst)
        for (k <- dbo.keySet; field <- fields.find(_.code == k)) {
            field.asInstanceOf[Field[_, T]].setFromAny(dbo.get(k))
        }
        inst.runSafe {
            fields.foreach(_.asInstanceOf[Field[_, T]].resetDirty)
        }
    }

    private def myFields(inst: T): List[MyField] =
        inst.fields().asInstanceOf[List[MyField]]
    */
}
