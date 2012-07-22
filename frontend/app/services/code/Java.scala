package services.code

import services.code.CodeUtil.JavaDecoder

class Java {

    def decode(code: String) = {
        val java = new JavaDecoder()
        java.exec(code)
    }

}

object JDecoder {

    def apply(code: String) =
        new Java().decode(code)
}
