package com.loops101.util

import scala.reflect.Manifest

class ClassUtil {

    def manifestOf(clazz: Class[_]) =
        Manifest.classType(clazz)

    def packageOf(cls: String): String =
        cls.replaceAll("class ", "").split('.').reverse.tail.reverse.mkString(".")

    def packageOf(cls: Class[_]): String =
        packageOf(cls.getName)

    def nameOf(cls: String): String =
        cls.split('.').last

    def nameOf(cls: Class[_]): String =
        nameOf(cls.getName)

    //    def checkIfClasspathContains(pckage: String) = {
    //        val provider = new ClassPathScanningCandidateComponentProvider(false)
    //        provider.addIncludeFilter(new AssignableTypeFilter(classOf[java.lang.Object]))
    //        provider.findCandidateComponents(pckage).size > 0
    //    }
    //
    //    def findSubClasses(cls: Class[_], basePath: String) = {
    //        import scala.collection.JavaConversions._
    //        val provider = new ClassPathScanningCandidateComponentProvider(true)
    //        provider.addIncludeFilter(new AssignableTypeFilter(cls))
    //        provider.findCandidateComponents(basePath).map {
    //            comp => Class.forName(comp.getBeanClassName)
    //        }
    //    }

    def getAndSetMethodsOf(cls: Class[_]) = {
        val fieldNames = cls.getDeclaredFields.map {
            f => f.getName
        }
        cls.getMethods.filter {
            m => fieldNames contains m.getName
        }
    }
}

object ClassUtil extends ClassUtil