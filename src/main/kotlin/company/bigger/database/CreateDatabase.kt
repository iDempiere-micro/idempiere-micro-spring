package company.bigger.database

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.util.regex.Pattern
import org.springframework.boot.util.LambdaSafe.callback


import org.flywaydb.core.api.FlywayException
import org.flywaydb.core.api.callback.BaseFlywayCallback

/**
 * Creates the database, if it doesn't already exist.
 *
 * @author Gili Tzabari
 */
class CreateDatabase : BaseFlywayCallback() {
    override fun beforeMigrate(connection: Connection) {
        val databaseName = flywayConfiguration.getPlaceholders().get("DATABASE.NAME")
        if (!Pattern.matches("^[a-zA-Z0-9_]+$", databaseName)) {
            // http://stackoverflow.com/q/43375955/14731
            throw IllegalArgumentException("databaseName may only contain alphanumeric characters.\n" +
                    "Actual: " + databaseName)
        }
        try {
            connection.prepareStatement("SELECT 1 FROM pg_database WHERE datname = ?").use { databaseExists ->
                databaseExists.setString(1, databaseName)
                databaseExists.executeQuery().use { rs ->
                    if (!rs.next()) {
                        connection.createStatement().use { createDatabase ->
                            // Postgresql complains "CREATE DATABASE cannot run inside a transaction block"
                            // http://stackoverflow.com/q/26482777/14731
                            val transactionsEnabled = !connection.autoCommit
                            if (transactionsEnabled)
                                connection.autoCommit = true
                            createDatabase.executeUpdate("CREATE DATABASE $databaseName")
                            if (transactionsEnabled)
                                connection.autoCommit = false
                        }
                    }
                }
            }
        } catch (e: SQLException) {
            throw FlywayException(e)
        }

    }
}