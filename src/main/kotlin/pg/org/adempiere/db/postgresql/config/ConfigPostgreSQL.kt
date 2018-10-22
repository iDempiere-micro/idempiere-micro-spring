package pg.org.adempiere.db.postgresql.config

import org.adempiere.install.IDatabaseConfig
import org.idempiere.common.db.Database
import org.idempiere.common.util.CLogger

/**
 * PostgreSQL Configuration
 *
 * @author Victor Perez e-Evolution
 * @version $Id: ConfigPostgreSQL.java,v 1.0 2005/01/31 06:08:15 vpj-cd Exp $
 */
/**
 * ConfigPostgreSQL
 */
class ConfigPostgreSQL : IDatabaseConfig {

    /** Discovered TNS			 */
    private var p_discovered: Array<String>? = null

    private val p_db = Database.getDatabase(Database.DB_POSTGRESQL)

    /**
     * Discover Databases.
     * To be overwritten by database configs
     * @param selected selected database
     * @return array of databases
     */
    override fun discoverDatabases(selected: String): Array<String> {
        val discovered = p_discovered
        if (discovered != null)
            return discovered
        p_discovered = arrayOf()
        return arrayOf()
    } // 	discoveredDatabases

    /**
     * Test JDBC Connection to Server
     * @param url connection string
     * @param uid user id
     * @param pwd password
     * @return true if OK
     */
    private fun testJDBC(url: String, uid: String, pwd: String): Boolean {
        try {
            val conn = p_db.getDriverConnection(url, uid, pwd)
        } catch (e: Exception) {
            log.severe(e.toString())
            return false
        }

        return true
    } // 	testJDBC

    override fun getDatabaseName(nativeConnectioName: String): String {
        return nativeConnectioName
    }

    override fun getName(): String {
        return Database.DB_POSTGRESQL
    }

    companion object {

        private val log = CLogger.getCLogger(ConfigPostgreSQL::class.java)
    }
} // 	ConfigPostgreSQL
// 	ConfigPostgreSQL