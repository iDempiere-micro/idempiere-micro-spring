package company.bigger.util

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
open class Ini() {
    @Value("\${connection}")
    private lateinit var m_connection: String

    @Value("\${maxthreadpoolsize:100}")
    private var m_maxThreadPoolSize = 100

    @Value("\${tracelevel:WARNING}")
    private lateinit var m_traceLevel: String

    @Value("\${showacct:true}")
    private var m_showAcct = true

    val connection get() = m_connection
    val maxThreadPoolSize get() = m_maxThreadPoolSize
    val traceLevel get() = m_traceLevel
    val showAcct get() = m_showAcct
}