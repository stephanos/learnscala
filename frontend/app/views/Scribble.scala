package views

class Person(val name: String)

object Person {
  def apply(n: String) =
    new Person(n)
}