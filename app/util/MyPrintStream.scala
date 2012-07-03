//package util
//
//import java.io.PrintStream
//import scala.collection.mutable
//
//class MyPrintStream(val orig: PrintStream)
//    extends PrintStream(orig) {
//
//    private val threadStreams =
//        new mutable.HashMap[Long, PrintStream] with mutable.SynchronizedMap[Long, PrintStream]
//
//
//    //~ INTERFACE =====================================================================================================
//
//    def addThread(t: Thread, s: PrintStream) = {
//        orig.println("ADD " + t.getId)
//        threadStreams.put(t.getId, s)
//    }
//
//    def removeThread(t: Thread) = {
//        orig.println("REMOVE " + t.getId)
//        val s = threadStreams.remove(t.getId)
//        s.map(_.close())
//    }
//
//
//    //~ INTERCEPTED ===================================================================================================
//
//    override def checkError() =
//        chooseStream().checkError()
//
//    override def flush() {
//        chooseStream().flush()
//    }
//
//    override def print(b: Boolean) {
//        chooseStream().print(b)
//    }
//
//    override def print(c: Char) {
//        chooseStream().print(c)
//    }
//
//    override def print(d: Double) {
//        chooseStream().print(d)
//    }
//
//    override def print(f: Float) {
//        chooseStream().print(f)
//    }
//
//    override def print(i: Int) {
//        chooseStream().print(i)
//    }
//
//    override def print(l: Long) {
//        chooseStream().print(l)
//    }
//
//    override def print(obj: Any) {
//        chooseStream().print(obj)
//    }
//
//    override def print(s: Array[Char]) {
//        chooseStream().print(s)
//    }
//
//    override def print(s: String) {
//        chooseStream().print(s)
//    }
//
//    /*
//    override def format(format: String, args: Array[Any]) =
//        chooseStream().format(format, args)
//
//    override def format(l: util.Locale, format: String, args:  Array[Any]) =
//        chooseStream().format(l, format, args)
//    */
//
//    override def write(b: Array[Byte]) {
//        super.write(b)
//    }
//
//    override def println() {
//        chooseStream().println()
//    }
//
//    override def println(x: Boolean) {
//        chooseStream().println(x)
//    }
//
//    override def println(x: Char) {
//        chooseStream().println(x)
//    }
//
//    override def println(x: Array[Char]) {
//        chooseStream().println(x)
//    }
//
//    override def println(x: Double) {
//        chooseStream().println(x)
//    }
//
//    override def println(x: Float) {
//        chooseStream().println(x)
//    }
//
//    override def println(x: Int) {
//        chooseStream().println(x)
//    }
//
//    override def println(x: Long) {
//        chooseStream().println(x)
//    }
//
//    override def println(x: Any) {
//        chooseStream().println(x)
//    }
//
//    override def println(x: String) {
//        chooseStream().println(x)
//    }
//
//    override def write(b: Int) {
//        chooseStream().write(b)
//    }
//
//    override def write(buf: Array[Byte], off: Int, len: Int) {
//        chooseStream().write(buf, off, len)
//    }
//
//    /*
//    override def setError() {
//        chooseStream().setError()
//    }
//
//    override protected def clearError() {
//        chooseStream().clearError()
//    }
//    */
//
//    override def close() {
//        chooseStream().close()
//    }
//
//
//    //~ INTERNALS =====================================================================================================
//
//    private def chooseStream() =
//        getStream(Thread.currentThread()).getOrElse(orig)
//
//    private def getStream(t: Thread) =
//        threadStreams.get(t.getId)
//}
//
//object MyPrintStream {
//
//    lazy val stdout = new MyPrintStream(System.out)
//}