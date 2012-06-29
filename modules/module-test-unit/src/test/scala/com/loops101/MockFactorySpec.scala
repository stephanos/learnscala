package com.loops101

import java.lang.reflect._
import annotation.tailrec

abstract class MockFactorySpec[T](implicit t: Manifest[T])
    extends UnitFactorySpec[T] with MockSpec {


    def defaultMock() {}

    override def before() {
        super.before()
        defaultMock()
    }

    // ==== VERIFYING

    def expect[T](t: => T) = there was t


    // ==== MOCKING

    protected def _mock[F](implicit m: Manifest[F]): F =
        _set(mock(m))

    protected def _mock[F](fieldName: String)(implicit m: Manifest[F]): F =
        _mock[F](t.erasure.getDeclaredField(fieldName))

    protected def _mock[F](field: Field)(implicit m: Manifest[F]): F =
        _set(field, mock(m))

    protected def reset[T](mocks: T*) =
        org.mockito.Mockito.reset(mocks: _*)


    // ==== REFLECTION

    protected def _set[F](value: F)(implicit m: Manifest[F]): F = {
        val clazz = t.erasure
        val fields = findFields(clazz)
        for (fld <- fields)
            if (fld.getType.equals(m.erasure))
                return _set(fld, value)
        for (fld <- fields)
            if (fld.getType.isAssignableFrom(m.erasure))
                return _set(fld, value)

        sys.error("was unable to set field of type '" + m.erasure + "' to '" + value + "'")
    }

    protected def _set[F](field: Field, value: F)(implicit m: Manifest[F]): F = {
        field.setAccessible(true)

        val modifier = classOf[Field].getDeclaredField("modifiers");
        modifier.setAccessible(true);
        modifier.setInt(field, field.getModifiers & ~Modifier.FINAL);

        field.set(target, value)

        value
    }

    @tailrec
    private def findFields(clazz: Class[_], acc: List[Field] = List()): List[Field] = {
        val fields = clazz.getDeclaredFields.toList ::: acc
        clazz.getSuperclass match {
            case null => fields
            case sc => findFields(sc, fields)
        }
    }
}
