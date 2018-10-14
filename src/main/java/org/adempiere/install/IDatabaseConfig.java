package org.adempiere.install;

/**
 *
 * @author hengsin
 *
 */
public interface IDatabaseConfig {

	/**
	 * Get real database name from native connection profile name
	 * return from discoverDatabases
	 * @param nativeConnectioName
	 * @return Database name
	 */
	public String getDatabaseName(String nativeConnectioName);


	/**
	 * 	Discover Databases.
	 * 	To be overwritten by database configs
	 *	@param selected selected database
	 *	@return array of databases
	 */
	public String[] discoverDatabases(String selected);


	/**
	 *  Get Database Name
	 *  @return database short name
	 */
	public String getName();
}