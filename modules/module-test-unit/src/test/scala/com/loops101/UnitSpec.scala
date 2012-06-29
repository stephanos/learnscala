package com.loops101

import org.specs2.mutable.Specification
import org.specs2.matcher.DataTables

trait UnitSpec
    extends Specification with DataTables {

    def setSequential() = sequential
}