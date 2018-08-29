package software.hsharp.core.util

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.io.IOException
import java.io.OutputStream
import java.io.FileOutputStream
import java.util.Properties

@Component
open class Ini() {
    @Value("\${Connection}")
    private lateinit var m_connection: String

    @Value("\${MaxThreadPoolSize}")
    private var m_maxThreadPoolSize = 100

    @Value("\${TraceLevel}")
    private lateinit var m_traceLevel: String

    @Value("\${ShowAcct}")
    private var m_showAcct= true


    val connection get() = m_connection
    val maxThreadPoolSize get() = m_maxThreadPoolSize
    val traceLevel get() = m_traceLevel
    val showAcct get() = m_showAcct
}