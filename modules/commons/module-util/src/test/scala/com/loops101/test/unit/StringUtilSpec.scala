package com.loops101.test.unit

import com.loops101.util.StringUtil._
import com.loops101.test.spec.UnitSpec

class StringUtilSpec
  extends UnitSpec {

  "String Util" should {

    "do extended trim" >> {
      extendedTrim("test") === "test"
      extendedTrim(" test ") === "test"
      extendedTrim("te  st") === "te st"
      extendedTrim("t e s t") === "t e s t"
      extendedTrim("t e  s t") === "t e s t"
      extendedTrim("   t    e     s     t     ") === "t e s t"
    }

    "limit a String's length" >> {

      val sTxt = "Java"
      val lTxt = "Java is to JavaScript what Car is to Carpet"

      "without ellipsis" >> {
        limit(sTxt, 2, None) === "Ja"
        limit(sTxt, 4, None) === "Java"
        limit(lTxt, 4, None) === "Java"

        limit(sTxt) === sTxt
        limit(lTxt) === lTxt
      }

      "with ellipsis" >> {
        limit(sTxt, 3) === "J.."
        limit(lTxt, 6) === "Java.."
        limit(lTxt, 12) === "Java is to.."

        limit(sTxt, 200) === sTxt
        limit(lTxt, 200) === lTxt
      }
    }

    "get first word" >> {
      getFirstWord("This is a test") === "This"
      getFirstWord(" test1 test2 ") === "test1"

      getFirstWord("test") === "test"
      getFirstWord("test ") === "test"
      getFirstWord(" test ") === "test"
      getFirstWord(" a test b ") === "a"
    }

    "get base words" >> {
      getBaseWords("This is 'shit'") ===
        "This is"
      getBaseWords("Problem found: it is in com.example.test") ===
        "Problem found it is in"
      getBaseWords("Problem in com.example.test: no value") ===
        "Problem in no value"
      getBaseWords("Table 'user' doesn't exist") ===
        "Table doesn't exist"
      getBaseWords("Table [user] doesn't exist") ===
        "Table doesn't exist"
      getBaseWords("Table (user) doesn't exist") ===
        "Table doesn't exist"
      getBaseWords("In attribute requires non-null value: chartQueryWizard.wizardModel") ===
        "In attribute requires non-null value"
      getBaseWords("Unknown entity: org.jboss.seam.example.jpa.User") ===
        "Unknown entity"
      getBaseWords("Parameter(s) 'title' are required for com.crashnote.web.components.Layout, but have not been bound") ===
        "Parameter(s) are required for but have not been bound"
      getBaseWords("Component app/Index:main.dash.item.htmltrace has rendered unbalanced elements; either it has started an element with MarkupWriter.element() and not followed up with MarkupWriter.end(), or it has invoked MarkupWriter.end() without first invoking MarkupWriter.element().") ===
        "Component has rendered unbalanced elements either it has started an element with and not followed up with or it has invoked without first invoking"
    }

    "split in half" >> {
      getHalfs("haha") ===("ha", "ha")
      getHalfs("harha") ===("ha", "rha")
      getHalfs("testtset") ===("test", "tset")
    }

    "check if string is enclosed" >> {
      isEnclosed("t") === false
      isEnclosed("tt") === false
      isEnclosed("test") === false
      isEnclosed("test:") === false
      isEnclosed("test.") === false
      isEnclosed("test?") === false

      isEnclosed("'t'") === true
      isEnclosed("'test'") === true
      isEnclosed("[test]") === true
      isEnclosed("(test)") === true
    }

    "get non-letters" >> {
      nonLetters("abc") === Array()
      nonLetters("abc!") === Array('!')
      nonLetters("!abc?") === Array('!', '?')
      nonLetters("#abc#") === Array('#', '#')
    }
  }
}
