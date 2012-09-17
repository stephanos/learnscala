package views

class EUR(val v: Float)
class USD(val v: Float)

object Cashier {

    def pay(x: EUR) {
        println("payed %s€".format(x.v))
    }

    def pay(x: USD) {
        println("payed %s$".format(x.v))
    }
}