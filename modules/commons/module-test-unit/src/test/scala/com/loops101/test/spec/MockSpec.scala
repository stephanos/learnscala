package com.loops101.test.spec

import java.io.File
import org.specs2.mock.Mockito

trait MockSpec
    extends Mockito {


    // ==== STUBBING

    def anyFile = any[File]

    def anyClass = any[Class[_]]

    def anyThrowable = any[Throwable]


    // ==== REFLECTION

    protected def doReturn(toBeReturned: Any) =
        org.mockito.Mockito.doReturn(toBeReturned)

    protected def doThrow(toBeThrown: Throwable) =
        org.mockito.Mockito.doThrow(toBeThrown)

    protected def doThrow(toBeThrown: Class[_ <: Throwable]) =
        org.mockito.Mockito.doThrow(toBeThrown)
}
