package com.loops101.util

import org.slf4j.{Logger => SLF4JLogger}
import org.slf4j.helpers.MessageFormatter

/**
 * Scala front-end to a SLF4J logger.
 *
 * See https://github.com/bmc/grizzled-slf4j/blob/master/src/main/scala/grizzled/slf4j/slf4j.scala
 */
class Logger(val logger: SLF4JLogger) {

    /**
     * Get the name associated with this logger.
     *
     * @return the name.
     */
    @inline final def name = logger.getName

    /**
     * Determine whether trace logging is enabled.
     */
    @inline final def isTraceEnabled = logger.isTraceEnabled

    /**
     * Issue a trace logging message.
     */
    @inline final def trace(t: Throwable, msg: String) {
        if (isTraceEnabled) logger.trace(msg, t)
    }

    @inline final def trace(msg: String, args: AnyRef*) {
        if (isTraceEnabled) logger.trace(msg, args.toArray)
    }

    /**
     * Determine whether debug logging is enabled.
     */
    @inline final def isDebugEnabled = logger.isDebugEnabled

    /**
     * Issue a debug logging message.
     */
    @inline final def debug(t: Throwable, msg: String) {
        if (isDebugEnabled) logger.debug(msg, t)
    }

    @inline final def debug(msg: String, args: AnyRef*) {
        if (isDebugEnabled) logger.debug(msg, args.toArray)
    }

    /**
     * Determine whether trace logging is enabled.
     */
    @inline final def isErrorEnabled = logger.isErrorEnabled

    /**
     * Issue a trace logging message.
     */
    @inline final def error(t: Throwable, msg: String) {
        if (isErrorEnabled) logger.error(msg, t)
    }

    @inline final def error(msg: String, args: AnyRef*) {
        if (isErrorEnabled) logger.error(msg, args.toArray)
    }

    /**
     * Determine whether trace logging is enabled.
     */
    @inline final def isInfoEnabled = logger.isInfoEnabled

    /**
     * Issue a trace logging message.
     */
    @inline final def info(t: Throwable, msg: String) {
        if (isInfoEnabled) logger.info(msg, t)
    }

    @inline final def info(msg: String, args: AnyRef*) {
        if (isInfoEnabled) logger.info(msg, args.toArray)
    }

    /**
     * Determine whether trace logging is enabled.
     */
    @inline final def isWarnEnabled = logger.isWarnEnabled

    /**
     * Issue a trace logging message.
    */
    @inline final def warn(t: Throwable, msg: String) {
        if (isWarnEnabled) logger.warn(msg, t)
    }

    @inline final def warn(msg: String, args: AnyRef*) {
        if (isWarnEnabled) logger.warn(msg, args.toArray)
    }

    /**
     * Converts any type to a String. In case the object is null, a null
     * String is returned. Otherwise the method `toString()` is called.
     *
     * @param msg  the message object to be converted to String
     *
     * @return the String representation of the message.
     */
    private implicit def _any2String(msg: Any): String =
        msg match {
            case null => "<null>"
            case _ => msg.toString
        }
}

/**
 * Mix the `Logging` trait into a class to get:
 *
 * - Logging methods
 * - A `Logger` object, accessible via the `log` property
 *
 * Does not affect the public API of the class mixing it in.
 */
trait Logging {

    // The logger. Instantiated the first time it's used.
    private lazy val _logger = Logger(getClass)

    /**
     * Get the `Logger` for the class that mixes this trait in. The `Logger`
     * is created the first time this method is call. The other methods (e.g.,
     * `error`, `info`, etc.) call this method to get the logger.
     *
     * @return the `Logger`
     */
    protected def logger: Logger = _logger

    protected def log = _logger

    protected def highlight(msg: => Any) =
        "######## " + msg + " ########"

    /**
     * Get the name associated with this logger.
     *
     * @return the name.
     */
    protected def loggerName =
        logger.name

    /**
     * Determine whether trace logging is enabled.
     */
    protected def isTraceEnabled =
        logger.isTraceEnabled

    /**
     * Issue a trace logging message.
     */
    protected def trace(t: Throwable, msg: String) {
        logger.trace(t, msg)
    }

    protected def trace(msg: => String, args: AnyRef*) {
        logger.trace(msg, args: _*)
    }

    /**
     * Determine whether debug logging is enabled.
     */
    protected def isDebugEnabled =
        logger.isDebugEnabled

    /**
     * Issue a debug logging message.
     */
    protected def debug(t: Throwable, msg: String) {
        logger.debug(t, msg)
    }

    protected def debug(msg: => String, args: AnyRef*) {
        logger.debug(msg, args: _*)
    }

    /**
     * Determine whether trace logging is enabled.
     */
    protected def isErrorEnabled =
        logger.isErrorEnabled

    /**
     * Issue a trace logging message.
     */
    def error(msg: String, args: AnyRef*) {
        logger.error(msg, args: _*)
    }

    protected def error(t: Throwable, msg: String) {
        logger.error(t, msg)
    }

    /**
     * Determine whether trace logging is enabled.
     */
    protected def isInfoEnabled =
        logger.isInfoEnabled

    /**
     * Issue a trace logging message.
     */
    protected def info(t: Throwable, msg: String) {
        logger.info(t, msg)
    }

    protected def info(msg: => String, args: AnyRef*) {
        logger.info(msg, args: _*)
    }

    /**
     * Determine whether trace logging is enabled.
     */
    protected def isWarnEnabled =
        logger.isWarnEnabled

    /**
     * Issue a trace logging message.
     */
    protected def warn(t: Throwable, msg: String) {
        logger.warn(t, msg)
    }

    protected def warn(msg: String, args: AnyRef*) {
        logger.warn(msg, args: _*)
    }
}

/**
 * A factory for retrieving an SLF4JLogger.
 */
object Logger {

    /**
     * The name associated with the root logger.
     */
    val RootLoggerName = SLF4JLogger.ROOT_LOGGER_NAME

    /**
     * Get the logger with the specified name. Use `RootName` to get the
     * root logger.
     *
     * @param name  the logger name
     *
     * @return the `Logger`.
     */
    def apply(name: String): Logger =
        new Logger(org.slf4j.LoggerFactory.getLogger(name))

    /**
     * Get the logger for the specified class, using the class's fully
     * qualified name as the logger name.
     *
     * @param cls  the class
     *
     * @return the `Logger`.
     */
    def apply(cls: Class[_]): Logger = apply(cls.getName)

    /**
     * Get the logger for the specified class type, using the class's fully
     * qualified name as the logger name.
     *
     * @return the `Logger`.
     */
    def apply[C](implicit m: Manifest[C]): Logger = apply(m.erasure.getName)

    /**
     * Get the root logger.
     *
     * @return the root logger
     */
    def rootLogger = apply(RootLoggerName)

    /*
    // ==== EVENTS

    def logEvent(typeOf: EventType, param1: AnyRef = null, param2: AnyRef = null) {
        log.info("", Array(typeOf, param1, param2))
    }

    def logUserEvent(userId: Long, typeOf: EventType, p1: AnyRef = null, p2: AnyRef = null) {
        eventWithContext(logEvent(typeOf, p1, p2), List(("userId", userId)))
    }

    def logProfileEvent(profileId: Long, typeOf: EventType, p1: AnyRef = null, p2: AnyRef = null) {
        eventWithContext(logEvent(typeOf, p1, p2), List(("profileId", profileId)))
    }

    def logUserProfileEvent(userId: Long, profileId: Long, typeOf: EventType, p1: AnyRef = null, p2: AnyRef = null) {
        eventWithContext(logEvent(typeOf, p1, p2), List(("userId", userId), ("profileId", profileId)))
    }

    private def eventWithContext(evt: => Unit, args: List[(String, Any)]) {
        args.foreach(a => MDC.put(a._1, a._2.toString))
        evt
        args.foreach(a => MDC.remove(a._1))
    }
    */
}