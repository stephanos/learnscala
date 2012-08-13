package views

object Scribble2 extends App {

    class WildcardUseCases {

        import scala.math._
        import java.sql.{Date => _}

        private var name: String = _

        def sizeOf(l: List[_]) =
            l.size

        def mult(m: Int, l: List[Int]) =
            l map (m * _)

        def double(nums: Int*): List[Int] = {
            val f = mult(2, (_: List[Int]))
            f(nums.toList)
        }

        def double(l: List[Int]): List[Int] =
            double(l: _*)

        def process(tuple: (_, _)) =
            tuple match {
                case (0, _) =>
                case t: Tuple2[String, _] =>
                case _ =>
            }
    }
}