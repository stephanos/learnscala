package com.loops101.boot

import com.loops101.util._
import com.loops101.util.EnvUtil

trait BaseBoot
    extends Logging {

    def NAME: String

    val manual = false
    var booted = false

    var subBoot: List[Class[_]] = List()
    var subRuns: List[BaseBoot] = List()

    //~ SETUP =====================================================================================

    if (!manual) onStartup()


    //~ INTERFACE =================================================================================

    def onStartup() {
        if (!booted) {
            booted = true
            log.info(highlight("STARTING UP '{}'"), NAME)
            log.info("... with mode: '{}' and env: '{}'", EnvUtil.mode, EnvUtil.env)

            // 1) load configuration
            configBoot()

            // 2) boot sub modules
            subRuns = subBoot.map {
                c =>
                    log.info("booting sub-module '{}'", c)
                    boot(c)
            }

            // 4) after booting completed
            afterBoot()

            // 5) shutdown hook
            addShutdownHook()
        }
    }

    final def onShutdown(force: Boolean = false) {
        if (force || booted) {
            booted = false
            log.info(highlight("SHUTTING DOWN '{}'"), NAME)

            // call custom shutdown logic
            customShutdown()

            // kill sub modules
            subRuns.foreach(_.onShutdown(force))
            subBoot = List()
            subRuns = List()
        }
    }

    def customShutdown() {
        // do nothing
    }

    //~ SHARED ====================================================================================

    protected def configBoot()

    protected def afterBoot() {}

    protected def addBoot(clazz: String) {
        try {
            subBoot = Class.forName(clazz) :: subBoot
        } catch {
            case e: ClassNotFoundException =>
                log.debug("unable to boot class '{}': not found", clazz)
        }
    }

    protected def addShutdownHook() {
        val me = this
        Runtime.getRuntime.addShutdownHook(new Thread(new Runnable {
            def run() {
                if (me != null) me.onShutdown()
            }
        }))
    }

    //~ INTERNALS =================================================================================

    private def boot(clazz: Class[_]) =
        clazz.newInstance.asInstanceOf[BaseBoot]
}