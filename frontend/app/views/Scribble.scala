package views

package object Scribble {

    /*
    val planes = List("A", "B", "C")
    val planeLocs = List(
        "A" -> ("Paris", 6), "A" -> ("Berlin", 12), "A" -> ("London", 24),
        "B" -> ("Paris", 6), "B" -> ("Berlin", 12), "B" -> ("London", 24),
        "C" -> ("Paris", 6), "C" -> ("Berlin", 12), "C" -> ("London", 24)
    )

    for {
      p <- planes
      pl <- planeLocs
    } yield p -> pl
    */

    /*
    for {
      plane <- planes
      planeLoc <- planeLocs.
    } yield timeAndStation._1 -> train
    */

    /*
    case class Passenger(firstName: String,
                         lastName: String,
                         middleName: Option[String] = None)

    def createPassenger(p: Map[String, String]): Option[Passenger] =
      for {firstName <- p.get("first")
           lastName <- p.get("last")
           middleName <- p.get("middle")}
       yield new Passenger(firstName, lastName, Option(middleName))

    def isPrime(n: Int) =
        List.range(2, n) forall (x => n % x != 0)
    */
}

/*
class Blackbox {
    def append(msg: String) =
        println(msg)
}

class Plane {

}

trait Logger {
    def log(msg: String)
}

class Cockpit extends Logger {
    val blackbox = new Blackbox

    def log(msg: String) =
        blackbox.append(msg)
}

class Engine extends Logger {
    val blackbox = new Blackbox

    def log(msg: String) =
        blackbox.append(msg)
}
*/


/*
class Passenger(val name: String,
                val hasCriminalRecord: Boolean)

trait Check {
    def check(p: Passenger)
}

trait BasicCheck extends Check {
    def check(p: Passenger ) =
        println("Doing basic check of " + p.name)
}

trait ExtensiveCheck extends Check {
    def check(p: Passenger) =
        println("Doing extensive check of " + p.name)
}

object SecurityGate {

    def check(p: Passenger) {
        if(p.hasCriminalRecord)

    }
}
*/


/*
class Employee(val name: String)

class Pilot(name: String)
    extends Employee(name)

class Stewardess(name: String)
    extends Employee(name)
*/


/*
trait Logger {
    self: Plane =>

    def log(msg: String)

    def info(msg: String) { log("INFO: " + msg) }
    def warn(msg: String) { log("WARN: " + msg) }
}

class Blackbox {
    def append(msg: String) { println(msg) }
}

class Plane extends Cockpit {
    private val box = new Blackbox
    def logToBox(msg: String) { box.append(msg) }
}

class Cockpit extends Logger {
    self: Plane =>

    def log(msg: String) { self.logToBox(msg) }
}
*/