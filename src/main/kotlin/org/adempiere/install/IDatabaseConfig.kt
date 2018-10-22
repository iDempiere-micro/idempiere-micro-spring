package org.adempiere.install

/**
 *
 * @author hengsin
 */
interface IDatabaseConfig {

    /**
     * Get Database Name
     * @return database short name
     */
    val name: String

    /**
     * Get real database name from native connection profile name
     * return from discoverDatabases
     * @param nativeConnectioName
     * @return Database name
     */
    fun getDatabaseName(nativeConnectioName: String): String

    /**
     * Discover Databases.
     * To be overwritten by database configs
     * @param selected selected database
     * @return array of databases
     */
    fun discoverDatabases(selected: String): Array<String>
}