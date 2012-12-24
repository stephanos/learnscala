package com.loops101.data.mongo.conf

import com.loops101.util._
import com.loops101.data.mongo.model._

import com.foursquare.rogue._
import com.foursquare.rogue.Rogue._
import com.foursquare.rogue.QueryHelpers.QueryLogger

/**
 * Index Rules
 * -----------
 *
 * When assuming 'db.foo.ensureIndex({a: 1, b: 1, c: 1})' this applies:
 *
 * 1) The sort column must be the last column used in the index.
 *
 * Good:
 * find(a=1).sort(a)
 * find(a=1).sort(b)
 * find(a=1, b=2).sort(c)
 *
 * Bad:
 * find(a=1).sort(c) (even though c is the last column used in the index, a is that last column used)
 *
 *
 * 2) The range query must also be the last column in an index.
 *
 * Good:
 * find(a=1,b>2)
 * find(a>1 and a<10)
 * find(a>1 and a<10).sort(a)
 *
 * Bad:
 * find(a>1, b=2)
 *
 *
 * 3) Only use a range query or sort on one column.
 *
 * Good:
 * find(a=1,b=2).sort(c)
 * find(a=1,b>2)
 * find(a=1,b>2 and b<4)
 * find(a=1,b>2).sort(b)
 *
 * Bad:
 * find(a>1,b>2)
 * find(a=1,b>2).sort(c)
 *
 *
 * 4) Conserve indexes by re-ordering columns used on equality (non-range) queries.
 *
 * find(a=1,b=1,d=1)
 * find(a=1,b=1,c=1,d=1)
 *
 * A single index defined on a, b, c, and d can be used for both queries.
 * If, however, you need to sort on the final value, you might need two indexes.
 *
 */
trait DocDatabase {

    val dbName: String
    val docs: List[MyMongoMetaRecord[_]]

    def initDB() {
        startDB()
        docs.foreach(_.createColl())
        QueryHelpers.logger = new MongoQueryLogger()
    }

    private def dropDB() {
        docs.foreach(_.dropColl())
    }

    def startDB() {
        DocConnection.connect(dbName)
    }

    def stopDB() {
        DocConnection.disconnect()
    }

    def recreateDB() {
        startDB()
        dropDB()
        initDB()
    }

    lazy val maxSize =
        if (EnvUtil.isCloud)
            if (EnvUtil.isStaging) 16 else 1024 // see https://addons.heroku.com/mongohq
        else
            1024
}

class MongoQueryLogger
    extends QueryLogger with LogUtil {

    import net.liftweb.json._

    override def log(query: GenericQuery[_, _], descr: => String, timeMillis: Long) {
        val cmnt = query.comment.getOrElse("")
        if (isDebugEnabled && cmnt != "explain")
            try {
                val comment = query.comment.map("'" + _ + "': ").getOrElse("")
                val explain = parse(query.comment("explain").explain())
                val n = compact(render(explain \ "n"))
                val cursor = compact(render(explain \ "cursor"))
                val nscannedObjs = compact(render(explain \ "nscannedObjects"))
                val scanAndOrder = (explain \ "scanAndOrder") != JBool(false)

                val stats = n + " (" + nscannedObjs + ") items in " + timeMillis + "ms"
                if ((cursor contains "BasicCursor") || scanAndOrder) {
                    val prob = if (scanAndOrder) "SCAN & O" else "SCAN"
                    warn("[{}] {} for {}[{}]", prob, stats, comment, descr)
                }
                else {
                    if (n != "0" || timeMillis > 100)
                        debug("{} for {}[{}]", stats, comment, descr)
                }
            } catch {
                case e: Throwable => error("unable to print query", e)
            }
    }

    override def logIndexMismatch(query: GenericQuery[_, _], msg: => String) {
        warn("{}: {}", query.toString(), msg)
    }

    override def logIndexHit(query: GenericQuery[_, _], index: MongoIndex[_]) {
    }

    override def warn(query: GenericQuery[_, _], msg: => String) {
        warn("{}: {}", query.toString(), msg)
    }
}