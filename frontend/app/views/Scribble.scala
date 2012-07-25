package views


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
