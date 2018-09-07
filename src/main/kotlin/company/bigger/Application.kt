package company.bigger

import company.bigger.common.db.CConnection
import company.bigger.service.UserService
import company.bigger.util.DatabaseImpl
import company.bigger.util.DummyEventManager
import company.bigger.util.DummyService
import company.bigger.util.Ini
import company.bigger.web.jwt.ExposeResponseInterceptor
import org.compiere.orm.MClient
import org.compiere.orm.MSystem
import org.compiere.validation.ModelValidationEngine
import org.idempiere.common.db.Database
import org.idempiere.common.util.CLogMgt
import org.idempiere.common.util.CLogger
import org.idempiere.common.util.DB
import org.idempiere.common.util.Env
import org.idempiere.common.util.SecureInterface
import org.idempiere.common.util.SecureEngine
import org.idempiere.common.util.Trx
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import java.util.concurrent.ScheduledThreadPoolExecutor

@Configuration
@EnableCaching
@SpringBootApplication
open class Application: WebMvcConfigurerAdapter() {
    override fun addInterceptors(registry: InterceptorRegistry?) {
        registry!!.addInterceptor(exposeResponseInterceptor())
    }

    override fun addCorsMappings(registry: CorsRegistry?) {
        registry!!.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600)
        super.addCorsMappings(registry)
    }

    @Bean
    open fun exposeResponseInterceptor() = ExposeResponseInterceptor()

    @Bean
    open fun methodValidationPostProcessor(): MethodValidationPostProcessor {
        val mv = MethodValidationPostProcessor()
        mv.setValidator(validator())
        return mv
    }

    @Bean
    open fun validator() = LocalValidatorFactoryBean()
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}

@Component
open class Micro {

    companion object {
        private var singleton : Micro? = null
        val instance get() = singleton!!
    }

    init {
        singleton = this
    }

    @Autowired
    private lateinit var ini: Ini

    @Autowired
    private lateinit var cconnection: CConnection

    @Autowired
    var userService: UserService? = null

    fun getThreadPoolExecutor(): ScheduledThreadPoolExecutor {
        return threadPoolExecutor!!
    }

    protected var log: CLogger? = null
    private var threadPoolExecutor: ScheduledThreadPoolExecutor? = null

    private fun createThreadPool() {
        val defaultMax = Runtime.getRuntime().availableProcessors() * 20
        val maxSize = ini.maxThreadPoolSize

        val max =
            if (maxSize <= 0) {
                defaultMax
            } else { maxSize }

        // start thread pool
        threadPoolExecutor = ScheduledThreadPoolExecutor(max)
    }

    fun startup() {
        if (log != null) return

        DummyService.setup()
        DummyEventManager.setup()
        val db = Database()
        db.setDatabase(DatabaseImpl())
        DB.setDBTarget(CConnection.get())
        DB.isConnected()

        CLogMgt.initialize(false)
        log = CLogger.getCLogger(Micro::class.java)

        CLogMgt.setLevel(ini.traceLevel)
        DB.setDBTarget(cconnection)
        createThreadPool()

        if (!DB.isConnected()) {
            this.log!!.severe("No Database")
        }

        if (!DB.isBuildOK(Env.getCtx())) {
            log = null
            return
        }

        val system = MSystem.get(Env.getCtx()) ?: return

        // 	Initialize main cached Singletons
        ModelValidationEngine.get()
        try {
            var className: String? = system.getEncryptionKey()
            if (className == null || className.length == 0) {
                className = System.getProperty(SecureInterface.ADEMPIERE_SECURE)
                if (className != null && className.length > 0 &&
                        className != SecureInterface.ADEMPIERE_SECURE_DEFAULT) {
                    SecureEngine.init(className) // 	test it
                    system.setEncryptionKey(className)
                    system.saveEx()
                }
            }
            SecureEngine.init(className)

            MClient.getAll(Env.getCtx())
        } catch (e: Exception) {
            this.log!!.warning("Environment problems: " + e.toString())
        }

        // 	Start Workflow Document Manager (in other package) for PO
        var className: String? = null
        try {
            className = "org.compiere.wf.DocWorkflowManager"
            Class.forName(className)
        } catch (e: Exception) {
            this.log!!.warning("Not started: " + className + " - " + e.message)
        }
        Trx.startTrxMonitor(getThreadPoolExecutor())
    }
}