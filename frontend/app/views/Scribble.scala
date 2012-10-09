package views

class Fruit
case class Apple() extends Fruit

class Box[A <: Fruit](val f: A)

object Test {
    val fruitBox: Box[Fruit] = new Box(new Fruit)
}