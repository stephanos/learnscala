package com.loops101.unit

import com.loops101.util.CompressUtil._
import com.loops101.UnitSpec

class CompressUtilSpec extends UnitSpec {

    "Compress Util" should {

        val data = "1234567890!%&/()=,.-#+*'_:;"
        val data2 = """{"logs":[{"time":1309853465171,"lvl":"ERROR","excp":[{"cls":"RuntimeException","trace":["com.logbox.test.services.BusinessLogic:BusinessLogic.java:doSomething:9","com.logbox.Main:Main.java:main:22"],"msg":"invalid logic"},{"cls":"org.springframework.beans.factory.NoSuchBeanDefinitionException","trace":["org.springframework.beans.factory.support.DefaultListableBeanFactory:DefaultListableBeanFactory.java:getBeanDefinition:527","org.springframework.beans.factory.support.AbstractBeanFactory:AbstractBeanFactory.java:getMergedLocalBeanDefinition:1083","org.springframework.beans.factory.support.AbstractBeanFactory:AbstractBeanFactory.java:doGetBean:274","org.springframework.beans.factory.support.AbstractBeanFactory:AbstractBeanFactory.java:getBean:190","org.springframework.context.support.AbstractApplicationContext:AbstractApplicationContext.java:getBean:1075","com.logbox.test.services.Database:Database.java:doSomething:9","com.logbox.test.services.BusinessLogic:BusinessLogic.java:doSomething:7","com.logbox.Main:Main.java:main:22"],"msg":"No bean named 'bean-b' is defined"}],"pckg":"com.logbox","thread":"main","env":1309853465016,"msg":"argh"}],"env":{"app":{"logFramework":"slf4j","webFramework":"tapestry","client":"${client.name}:${client.version}","lang.runtime":"Java HotSpot(TM) 64-Bit Server VM","lang.v":"1.6.0_22","lang":"java","props":{"run.mode":"production"},"classpath":"alt-rt.jar:charsets.jar:deploy.jar:javaws.jar:jce.jar:jsse.jar:management-agent.jar:plugin.jar:resources.jar:rt.jar:dnsns.jar:localedata.jar:sunjce_provider.jar:classes:log4j-1.2.14.jar:tapestry-core-5.3.0.jar:antlr-runtime-3.3.jar:stringtemplate-3.2.1.jar:antlr-2.7.7.jar:tapestry-ioc-5.3.0.jar:slf4j-api-1.6.1.jar:plastic-5.3.0.jar:slf4j-log4j12-1.6.1.jar:tapestry-func-5.3.0.jar:javax.inject-1.jar:tapestry-json-5.3.0.jar:tapestry-annotations-5.3.0.jar:javassist-3.12.1.GA.jar:testng-5.14.9.jar:junit-4.7.jar:bsh-2.0b4.jar:guice-2.0.jar:aopalliance-1.0.jar:jcommander-1.12.jar:snakeyaml-1.6.jar:commons-codec-1.3.jar:tapestry-spring-5.3.0.jar:spring-web-3.0.0.RELEASE.jar:spring-beans-3.0.5.RELEASE.jar:spring-core-3.0.5.RELEASE.jar:spring-asm-3.0.5.RELEASE.jar:commons-_logging-1.1.1.jar:spring-context-3.0.5.RELEASE.jar:spring-expression-3.0.5.RELEASE.jar:servlet-api-2.5.jar:spring-aop-3.0.5.RELEASE.jar:classes:animal-sniffer-annotations-1.6.jar:idea_rt.jar:"},"time":1309853465016,"machine":{"uid":134663474856545,"vars":{"USERPROFILE":"C:\\Users\\Stephan","JAVAFX_HOME":"D:\\Applications\\Java\\javafx1.3","JAVA_HOME":"D:\\Applications\\Java\\jdk","SystemDrive":"C:","Path":"D:\\Applications\\IntelliJ IDEA 10.5\\bin\\..\\.\\bin;C:\\Program Files (x86)\\NVIDIA Corporation\\PhysX\\Common;C:\\Program Files\\Common Files\\Microsoft Shared\\Windows Live;D:\\Applications\\Java\\javafx1.3\\bin;D:\\Applications\\Java\\javafx1.3\\emulator\\bin;C:\\Program Files (x86)\\PC Connectivity Solution\\;D:\\Applications\\Java\\javafx-sdk1.2\\bin;D:\\Applications\\Java\\javafx-sdk1.2\\emulator\\bin;C:\\Windows\\system32;C:\\Windows;C:\\Windows\\System32\\Wbem;C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\;D:\\Applications\\SlikSVN\\bin\\;D:\\Applications\\Git\\cmd;D:\\Applications\\TortoiseSVN\\bin;D:\\Applications\\Calibre\\;D:\\Applications\\MKVtoolnix;D:\\Applications\\QuickTime\\QTSystem\\;D:\\Applications\\Ruby\\bin;D:\\Applications\\Tcl\\bin;D:\\work\\dev\\tools\\maven3\\bin;D:\\Applications\\Java\\jdk\\bin;D:\\Applications\\Python25;C:\\MinGW\\bin;C:\\msys\\1.0\\bin;D:\\work\\dev\\tools;D:\\work\\dev\\workspace\\tendo\\web-play;D:\\work\\dev\\tools\\sbt;D:\\Applications\\R/bin;D:\\work\\dev\\tools\\ant\\bin;D:\\work\\dev\\tools\\appengine\\bin;d:\\applications\\java\\jdk\\jre\\bin","PROCESSOR_REVISION":"1706","USERDOMAIN":"Stephan-PC","ALLUSERSPROFILE":"C:\\ProgramData","VBOX_INSTALL_PATH":"D:\\Applications\\VirtualBox\\","tvdumpflags":"8","SESSIONNAME":"Console","IDEA_JDK":"D:\\Applications\\Java\\jdk","TMP":"D:\\tmp","CommonProgramFiles":"C:\\Program Files\\Common Files","=::":"::\\","LOGONSERVER":"\\\\STEPHAN-PC","M2_HOME":"D:\\work\\dev\\tools\\maven3","PROCESSOR_LEVEL":"6","LOCALAPPDATA":"C:\\Users\\Stephan\\AppData\\Local","WXADDITIONS":"D:\\work\\dev\\sdk\\wxWidgets2.8\\additions","COMPUTERNAME":"STEPHAN-PC","SystemRoot":"C:\\Windows","asl.log":"Destination=file;OnFirstLog=command,environment","USERNAME":"Stephan","APPDATA":"D:\\work\\AppData\\Roaming","ProgramData":"C:\\ProgramData","PATHEXT":".COM;.EXE;.BAT;.CMD;.VBS;.VBE;.JS;.JSE;.WSF;.WSH;.MSC;.RB;.RBW","ANDROID_SDK_HOME":"D:\\work","ERLANG_HOME":"D:\\Applications\\Erlang","ProgramFiles(x86)":"C:\\Program Files (x86)","PYTHONPATH":"D:\\Applications\\Python25","TEMP":"D:\\tmp","ProgramFiles":"C:\\Program Files","HOMEDRIVE":"C:","EXE4J_JAVA_HOME":"D:\\Applications\\Java\\jdk","QTJAVA":"D:\\Applications\\Quicktime\\Java\\jre_32\\lib\\ext\\QTJava.zip","ProgramW6432":"C:\\Program Files","PROCESSOR_IDENTIFIER":"Intel64 Family 6 Model 23 Stepping 6, GenuineIntel","OGRE_HOME":"D:\\work\\dev\\sdk\\OgreSDK","ULTRAMON_LANGDIR":"C:\\Program Files\\UltraMon\\Resources\\en","R_HOME":"D:\\Applications\\R","MAVEN_OPTS":"-Xms512m -Xmx1024m -XX:MaxPermSize=256m -XX:PermSize=64m","CLASSPATH":".;D:\\Applications\\Quicktime\\Java\\jre_32\\lib\\ext\\QTJava.zip","PROCESSOR_ARCHITECTURE":"AMD64","FP_NO_HOST_CHECK":"NO","OS":"Windows_NT","HOMEPATH":"\\Users\\Stephan","CommonProgramW6432":"C:\\Program Files\\Common Files","windir":"C:\\Windows","NUMBER_OF_PROCESSORS":"2","PUBLIC":"C:\\Users\\Public","PSModulePath":"C:\\Windows\\system32\\WindowsPowerShell\\v1.0\\Modules\\","CommonProgramFiles(x86)":"C:\\Program Files (x86)\\Common Files","ComSpec":"C:\\Windows\\system32\\cmd.exe"},"os.version":"6.1","os":"Windows 7","os.arch":"amd64","name":"Stephan-PC","cores":2,"ip":"192.168.56.1"}}}"""

        "do snappy compression" >> {
            val c = fastCompress(data)
            //log.debug("snapped: {} [{}]", new String(c), c.length)
            val s = new String(fastUncompress(c))
            s === data
        }

        "know zip compression" >> {
            "recognize" >> {
                isZipped(data) === false
                isZipped(zip(data)) === true
            }
            "do " >> {
                val c = zip(data)
                //log.debug("zipped: {} [{}]", new String(c), c.length)
                val s = new String(unzip(c))
                s === data
            }
        }

        "know deflate compression" >> {
            "recognize" >> {
                isDeflated(data) === false
                //isDeflated(deflate(data)) === true TODO?
            }
            "do " >> {
                val c = deflate(data)
                //log.debug("deflated: {} [{}]", new String(c), c.length)
                val s = new String(inflate(c))
                s === data
            }
        }
    }
}