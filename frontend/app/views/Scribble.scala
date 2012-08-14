package views

object Scribble extends App {

}

package airline {

package machines {

        class Plane {

            protected val s1 = 1

            protected[machines] val s3 = 3

            protected[airline] val s4 = 4

            protected[this] val s5 = 5
        }

        object Plane {
            //println(new Plane().s1)
        }
    }
}

package test {

import views.airline.machines.Plane

    class Test extends Plane {
        s3
    }
}