package com.loops101.data.mongo.model

import com.mongodb._
import java.util.Date

import com.loops101.util.StringUtil._
import com.loops101.data.mongo.util.OIDUtil

import net.liftweb.record._
import net.liftweb.record.field._
import net.liftweb.mongodb.record.field._
import net.liftweb.common._
import net.liftweb.util.TimeHelpers._
import net.liftweb.json.JsonAST._
import net.liftweb.http.js.JE._
import net.liftweb.mongodb.record._
import net.liftweb.mongodb.MongoDB
import net.liftweb.util.StringValidators
import com.loops101.util._

//~ GENERAL =======================================================================================

trait MyBaseField {
    self: TypedField[_] =>

    // NAMING
    def naming: (String, String)

    protected def n(s: String) =
        (s, s)

    override def name =
        if(asJSON) naming._1 else naming._2 // for DB persisting

    override def displayName =
        naming._1 // for JSON parsing


    // NAME MODE
    private var asJSON = false

    def withJSON() = {
        asJSON = true
        this
    }

    def withDBO() = {
        asJSON = false
        this
    }
}

trait MyField extends MyBaseField {
    self: MandatoryTypedField[_] =>

}

trait MyOptField extends MyBaseField {
    self: OptionalTypedField[_] =>

    def getOrDefault =
        valueBox openOr (defaultValueBox open_!)

    /*
    override protected def set_!(in: Box[MyType]): Box[MyType] =
        if (!optional_? || in != defaultValueBox) // prevent default value from being set
            runFilters(in, setFilterBox)
        else
            defaultValueBox
    */
}

trait DocMongoId[OwnerType <: MongoRecord[OwnerType]] {
    self: OwnerType =>

    object _id extends ObjectIdField(this.asInstanceOf[OwnerType]) with MyField {
        val naming = ("_id", "_id")
    }

    def id =
        oid

    def oid =
        _id.value

    def pid: String =
        OIDUtil.obfuscate(oid)

    val createdOn: Date =
        new Date(createdAt)

    def createdAt: Long =
        oid.getTime

    /*
    * Get the DBRef for this record
    */
    def getRef: DBRef = {
        MongoDB.use(meta.mongoIdentifier)(db =>
            new DBRef(db, meta.collectionName, _id.value)
        )
    }
}

trait DocIntId[OwnerType <: MongoRecord[OwnerType]] {

    object _id extends IntField(this.asInstanceOf[OwnerType]) {
        def naming = ("_id", "_id")
    }

    object creation extends DateField(this.asInstanceOf[OwnerType]) with MyField {
        def naming = ("created", "@")
    }

    def id =
        _id.value

    def pid: String =
        IDUtil.obfuscate(id)

    val createdOn: Date =
        creation.value

    def createdAt: Long =
        createdOn.getTime
}


abstract class GroupField[O <: BsonRecord[O], S <: BsonRecord[S]](rec: O, meta: BsonMetaRecord[S])(implicit m: Manifest[S])
    extends BsonRecordField[O, S](rec, meta) with MyField {

    def _meta = meta

    /*
    def apply[V](fn: (S) => Option[V]) =
        valueBox.map(v => fn(v)) match {
            case f: EnumField => f.valueBox
            case f: StringField => f.valueBox
            case _ => None
        }
    */
}

abstract class GroupListField[O <: BsonRecord[O], S <: BsonRecord[S]](rec: O, meta: BsonMetaRecord[S])(implicit m: Manifest[S])
    extends BsonRecordListField[O, S](rec, meta) with MyField {

    def _meta = meta
}


//~ TYPED =========================================================================================


// === ENUM

class EnumerationField[OwnerType <: Record[OwnerType], EnumType <: Enumeration](rec: OwnerType, enum: EnumType)(implicit m: Manifest[EnumType#Value])
  extends EnumField[OwnerType, EnumType](rec, enum)

class OptEnumerationField[OwnerType <: Record[OwnerType], EnumType <: Enumeration](rec: OwnerType, enum: EnumType)(implicit m: Manifest[EnumType#Value])
  extends OptionalEnumField[OwnerType, EnumType](rec, enum)


// === TEXT

class OptionalTextField[OwnerType <: Record[OwnerType]](rec: OwnerType, mlen: Int = Integer.MAX_VALUE)
    extends OptionalStringField(rec, mlen) with MyStringField

class TextField[OwnerType <: Record[OwnerType]](rec: OwnerType, mlen: Int = Integer.MAX_VALUE)
    extends StringField(rec, mlen) with MyStringField

trait MyStringField {

    self: StringTypedField with StringValidators =>

    override def setFilter = List(crop _)
}


// === DATE

trait TimeTypedField
    extends TypedField[Date] {

    def setFromAny(in: Any): Box[Date] =
        toDate(in).flatMap(d => setBox(Full(d)))

    def setFromString(s: String): Box[Date] =
        s match {
            case "" if optional_? => setBox(Empty)
            case _ => setFromAny(s)
        }

    def toForm =
        None

    def asJs =
        valueBox.map(v => Num(v.getTime)) openOr JsNull

    def asJValue =
        valueBox.map(v => JInt(v.getTime)) openOr (JNothing: JValue)

    def setFromJValue(jvalue: JValue) =
        jvalue match {
            case JNothing | JNull if optional_? => setBox(Empty)
            case JInt(n) => setFromAny(n)
            case other => setBox(FieldHelpers.expectedA("JInt", other))
        }
}

class TimeField[OwnerType <: Record[OwnerType]](rec: OwnerType)
    extends Field[Date, OwnerType] with MandatoryTypedField[Date] with TimeTypedField {

    def owner = rec

    def this(rec: OwnerType, value: Date) = {
        this(rec)
        setBox(Full(value))
    }

    def defaultValue = new Date
}

class OptionalTimeField[OwnerType <: Record[OwnerType]](rec: OwnerType)
    extends Field[Date, OwnerType] with OptionalTypedField[Date] with TimeTypedField {

    def owner = rec

    def this(rec: OwnerType, value: Box[Date]) = {
        this(rec)
        setBox(value)
    }
}


// === MOMENT

trait MomentTypedField {

    self: IntTypedField =>
}

class MomentField[OwnerType <: Record[OwnerType]](rec: OwnerType)
    extends IntField(rec) with MomentTypedField

class OptionalMomentField[OwnerType <: Record[OwnerType]](rec: OwnerType)
    extends OptionalIntField(rec) with MomentTypedField


// === MAP

abstract class MapField[OwnerType <: BsonRecord[OwnerType], MapValueType](rec: OwnerType, mlen: Int = 255)
    extends MongoMapField[OwnerType, MapValueType](rec) with MyField {

    val REPLACE = List(('.', '∴'), ('$', '∮'))


    // OUT
    override def asDBObject = {
        val dbo = new BasicDBObject
        value.keys.foreach {
            k => dbo.put(escape(limit(k.toString, 64)),
                limit(value.getOrElse(k, "").asInstanceOf[String], mlen))
        }
        dbo
    }

    private def escape(s: String) =
        REPLACE.foldLeft(s)((s,r) => s.replace(r._1, r._2))


    // IN
    override def setFromDBObject(dbo: DBObject) = {
        import scala.collection.JavaConversions._

        setBox(Full(
            Map() ++ dbo.keySet.map {
                k => (unescape(k.toString), dbo.get(k).asInstanceOf[MapValueType])
            }
        ))
    }

    private def unescape(s: String) =
        REPLACE.foldLeft(s)((s,r) => s.replace(r._2, r._1))
}