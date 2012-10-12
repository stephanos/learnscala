package views

object Test {

    case class Circle (x: Int, y: Int, radius: Int) {

        def move (t: (Int, Int)) =
            copy (x + t._1, y + t._2)
    }
}