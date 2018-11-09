package company.bigger

import com.coxautodev.graphql.tools.SchemaParser
import company.bigger.common.db.CConnection
import company.bigger.util.DatabaseImpl
import company.bigger.util.DummyEventManager
import company.bigger.util.DummyService
import company.bigger.util.Ini
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
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.util.concurrent.ScheduledThreadPoolExecutor
import com.rollbar.notifier.config.ConfigBuilder.withAccessToken
import com.rollbar.notifier.Rollbar
import company.bigger.service.CategoryService
import company.bigger.service.CountryService
import company.bigger.service.UserService
import company.bigger.web.resolver.QueryResolver
import graphql.schema.GraphQLSchema
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.event.ContextRefreshedEvent

/**
 * The main iDempiere-Micro Spring Boot application.
 *
 */
@Configuration
@EnableCaching
@SpringBootApplication
open class Application : WebMvcConfigurer {

    /**
     * The GraphQL schema. The path "graphql/app.graphqls" is `resources`-relative
     */
    @Bean
    open fun schema(): GraphQLSchema {
        return SchemaParser.newParser()
                .file("graphql/app.graphqls")
                .resolvers(QueryResolver())
                .build().makeExecutableSchema()
    }

    /**
     * Unlimited CORS allowed
     */
    override fun addCorsMappings(registry: CorsRegistry?) {
        registry!!.addMapping("*")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600)
    }
}

/**
 * Main application entry point. We try to get the ROLLBAR_TOKEN and setup Rollbar for error handling.
 */
fun main(args: Array<String>) {
    val token = System.getenv("ROLLBAR_TOKEN") ?: ""
    if (token.isEmpty()) {
        println("Rollbar not setup.")
    } else {
        val rollbar = Rollbar.init(withAccessToken("602b880210304119b6435c4129061714").build())
        rollbar.log("idempiere-micro-spring started")
    }
    SpringApplication.run(Application::class.java, *args)
}

/**
 * [Micro] needs to be started-up. This listener makes sure all the Spring Boot is already started and available.
 */
@Component
open class StartupApplicationListener : ApplicationListener<ContextRefreshedEvent> {
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        Micro.instance!!.startup()
    }
}

/**
 * The main application component. This is heavily based on the original Adempiere class from iDempiere.
 * For non-Spring classes the companion object inside this class is the main entrypoint to all the services.
 */
@Component
open class Micro(
    private val ini: Ini,
    private val cconnection: CConnection,
    internal val userService: UserService,
    internal val categoryService: CategoryService,
    internal val countryService: CountryService
) {
    companion object {
        /**
         * The singleton instance of the main application. Is used for starting the application
         * and also for non-Spring classes to access the services (e.g. UserService)
         */
        private var singleton: Micro? = null
        val instance get() = singleton
        private fun setInstance(instance: Micro) {
            singleton = instance
        }
    }

    /**
     * We know this is in fact singleton in Spring Boot so it is safe to copy the instance to the static variable
     * in the companion object.
     */
    init {
        setInstance(this)
    }

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

    /**
     * The startup function taken basically from the Adempiere class in iDempiere.
     * The main task is to connect to the database and to setup logging.
     */
    fun startup() {
        // make sure we run once only
        if (log != null) return

        // we are not OSGi and not all is already mapped to Spring IoC
        // so we need some fallback classes to be setup
        // for org.idempiere.common.base.Service
        DummyService.setup()
        // and also org.idempiere.orm.EventManager
        DummyEventManager.setup()

        // setup the database engine and make sure it can connect
        val db = Database()
        db.setDatabase(DatabaseImpl())
        DB.setDBTarget(cconnection)
        DB.isConnected()

        // setup log
        CLogMgt.initialize(false)
        log = CLogger.getCLogger(Micro::class.java)
        CLogMgt.setLevel(ini.traceLevel)

        // create the thread pool for code like transaction closing etc.
        createThreadPool()

        if (!DB.isConnected()) {
            this.log!!.severe("No Database")
        }

        if (!DB.isBuildOK(Env.getCtx())) {
            log = null
            return
        }

        // start the system
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

        // 	Start Workflow Document Manager for PO
        var className: String? = null
        try {
            className = org.compiere.wf.DocWorkflowManager::class.java.simpleName
            Class.forName(className)
        } catch (e: Exception) {
            this.log!!.warning("Not started: " + className + " - " + e.message)
        }

        // start transaction monitor to make sure transactions do not live forever
        Trx.startTrxMonitor(getThreadPoolExecutor())
    }
}