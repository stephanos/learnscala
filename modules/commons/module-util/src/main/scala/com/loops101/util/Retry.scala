package com.loops101.util

import scala.util.control.Exception._

// taken from: https://gist.github.com/983175
case class Retry[T](maxRetry: Int = 3) {

    private var retryCount = 0
    private var block: () => T = _

    def apply(op: => T): Retry[T] =
        retry(op)

    def retry(op: => T): Retry[T] = {
        block = () => {
            try {
                op
            } catch {
                case t: Throwable =>
                    retryCount += 1
                    if (retryCount == maxRetry) throw t
                    else block()
            }
        }
        this
    }

    def giveup(handler: => Catcher[T]): T = {
        catching(handler) {
            block()
        }
    }
}