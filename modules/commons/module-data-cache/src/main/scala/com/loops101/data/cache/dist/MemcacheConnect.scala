package com.loops101.data.cache.dist

import com.loops101.util.LogUtil
import com.loops101.util.SystemUtil

// see https://github.com/killme2008/xmemcached
trait MemcacheConnect {

    self: SystemUtil with LogUtil =>


    class MemcacheConnectImpl {

        import net.rubyeye.xmemcached._
        import net.rubyeye.xmemcached.auth.AuthInfo
        import net.rubyeye.xmemcached.utils.AddrUtil._
        import net.rubyeye.xmemcached.command.BinaryCommandFactory

        private var client: MemcachedClient =
            null


        //~ INTERFACE =================================================================================

        def isOnline: Boolean =
            getClient.map(!_.isShutdown).getOrElse(false)

        def startup() {
            getClient
        }

        def shutdown() {
            getClient.map(_.shutdown())
            client = null
        }

        def getClient: Option[MemcachedClient] =
            client match {
                case null => connect()
                case c => Some(c)
            }


        //~ INTERNALS =================================================================================

        private def connect(): Option[MemcachedClient] =
            try {
                // read config
                val user = getEnvProperty("MEMCACHIER_USERNAME")
                val pass = getEnvProperty("MEMCACHIER_PASSWORD")
                val url = getEnvProperty("MEMCACHIER_SERVERS").getOrElse("localhost")

                // build client
                val server = url + ":11211"
                val addr = getAddresses(server)
                val factory = new XMemcachedClientBuilder(addr)
                if (pass.isDefined && user.isDefined) {
                    log.info("connecting to remote memcached: {}", server)
                    factory.addAuthInfo(getOneAddress(server), AuthInfo.plain(user.get, pass.get))
                } else
                    log.info("connecting to local memcached")
                factory.setCommandFactory(new BinaryCommandFactory())
                client = factory.build()
                Some(client)
            } catch {
                case e: Throwable => None
            }
    }

    val _cacheConnect: MemcacheConnectImpl
}
