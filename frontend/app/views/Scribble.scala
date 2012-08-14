package views

object Scribble extends App {

    class Foo extends Dynamic {
      def selectDynamic(name: String) {
        println("select " + name)
      }
      def applyDynamic(name: String)(args: Any*) {
        println("apply " + name + " with args " + args.toList.mkString(","))
      }
      def applyDynamicNamed(name: String)(args: (String, Any)*) {
        println("apply " + name + " with args " + args.mkString(","))
      }
      def updateDynamic(name: String)(arg: Any) {
        println("update " + name + " with arg " + arg)
      }
    }

    /*
    object Test {
      def main(args: Array[String]) {
        val foo = new Foo
        foo.bar(5)     //1
        foo.bar(x = 5) //2
        foo.bar        //3
        foo.baz = 5    //4
      }
    }
    */
}