package de.learnscala.specs

import de.learnscala.uebungen.U060._
import de.learnscala.base.BaseTest
import java.io._

class S060 extends BaseTest {

    spec("U060", enabled) {

        "half" >> {
            "must calculate half of 10" >> {
                half(10) === 5
            }
            "must calculate half of 100" >> {
                half(100) === 50
            }
            "must calculate half of -10" >> {
                half(-10) === -5
            }
            "must throw Exception for 3" >> {
                half(3) must throwA[Throwable]
            }
            "must throw Exception for 5" >> {
                half(5) must throwA[Throwable]
            }
        }

        "readCharFromFile" >> {
            "must return '@' for '1' and close connection" >> {
                val f = getMock
                readCharFromFile(f) === '@'
                there was one(f).close()
            }
            "must print 'not found' if 'FileNotFoundException' is thrown, close connection and return 0" >> {
                val f = getMock
                f.read() throws new FileNotFoundException()
                val (r, out) = captureOutput(readCharFromFile(f))
                r.getOrElse(null) === 0
                out.trim === "not found"
                there was one(f).close()
            }
            "must print 'cannot read' if 'IOException' is thrown, close connection and return 0" >> {
                val f = getMock
                f.read() throws new IOException()
                val (r, out) = captureOutput(readCharFromFile(f))
                r.getOrElse(null) === 0
                out.trim === "cannot read"
                there was one(f).close()
            }
            "must print 'unknown error' if 'RuntimeException' is thrown; and return 0" >> {
                val f = getMock
                f.read() throws new RuntimeException()
                val (r, out) = captureOutput(readCharFromFile(f))
                r.getOrElse(null) === 0
                out.trim === "unknown error"
                there was one(f).close()
            }
        }
    }
    
    private def getMock =  {
        val m = mock[FileReader]
        m.read() returns '@'
        m.ready() returns true
        m
    }
}