package com.loops101.test.spec

import org.specs2.specification._

abstract class UnitFactorySpec[T](implicit t: Manifest[T])
    extends UnitSpec with BeforeExample {

    def factory: T

    var target: T =
        null.asInstanceOf[T]

    def before() {
        target = factory
    }
}