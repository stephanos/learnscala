package views

object Test {

    def div(a: Int, b: Int): Option[Int] =
        if (b <= 0) None
        else if (a < b) Some(0)
        else Some(1 + div(a - b, b).get)

    div(13, 0) match {
      case Some(x) => println(x)
      case None => println("No result")
    }


    def getUser(id: Long) =
        if (id < 0) null
        else (id, "Bob", 42)

    val user = getUser(-1)

    val user5 = getUser(2)
}