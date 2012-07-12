package services.code

import services.code.CodeUtil.JavaDecoder

class Java {

    def decode(code: String) = {
        val java = new JavaDecoder()
        java.exec(code)
    }

}
