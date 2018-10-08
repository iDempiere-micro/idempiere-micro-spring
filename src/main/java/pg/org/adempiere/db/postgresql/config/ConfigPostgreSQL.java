package pg.org.adempiere.db.postgresql.config;

import org.adempiere.install.IDatabaseConfig;
import org.idempiere.icommon.db.AdempiereDatabase;
import org.idempiere.common.db.Database;
import org.idempiere.common.util.CLogger;

import java.sql.Connection;

/**
 *	PostgreSQL Configuration
 *
 *  @author Victor Perez e-Evolution
 *  @version $Id: ConfigPostgreSQL.java,v 1.0 2005/01/31 06:08:15 vpj-cd Exp $
 */
public class ConfigPostgreSQL implements IDatabaseConfig
{

	private final static CLogger log = CLogger.getCLogger(ConfigPostgreSQL.class);

	/**
	 * 	ConfigPostgreSQL
	 */
	public ConfigPostgreSQL ()
	{
	}	//	ConfigPostgreSQL

	/** Discovered TNS			*/
	private String[] 			p_discovered = null;

	private AdempiereDatabase p_db = Database.getDatabase(Database.DB_POSTGRESQL);

	/**
	 * 	Discover Databases.
	 * 	To be overwritten by database configs
	 *	@param selected selected database
	 *	@return array of databases
	 */
	public String[] discoverDatabases(String selected)
	{
		if (p_discovered != null)
			return p_discovered;
		p_discovered = new String[]{};
		return p_discovered;
	}	//	discoveredDatabases


	/**
	 * 	Test JDBC Connection to Server
	 * 	@param url connection string
	 *  @param uid user id
	 *  @param pwd password
	 * 	@return true if OK
	 */
	private boolean testJDBC (String url, String uid, String pwd)
	{
		try
		{
			@SuppressWarnings("unused")
			Connection conn = p_db.getDriverConnection(url, uid, pwd);
		}
		catch (Exception e)
		{
			log.severe(e.toString());
			return false;
		}
		return true;
	}	//	testJDBC

	@Override
	public String getDatabaseName(String nativeConnectioName) {
		return nativeConnectioName;
	}

	@Override
	public String getName() {
		return Database.DB_POSTGRESQL;
	}

}	//	ConfigPostgreSQL