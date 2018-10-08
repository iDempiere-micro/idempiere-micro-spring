package company.bigger.test.support

import org.idempiere.common.util.DB
import java.sql.ResultSet
import java.util.Random

internal fun randomString(length: Int): String {
    fun ClosedRange<Char>.randomString(length: Int) =
            (1..length)
                    .map { (Random().nextInt(endInclusive.toInt() - start.toInt()) + start.toInt()).toChar() }
                    .joinToString("")
    return ('a'..'z').randomString(length)
}

internal fun String.asResource(work: (String) -> Unit) {
    val content = BaseTest::class.java.getResource(this).readText()
    work(content)
}

internal fun <T : Any> String.executeSql(factory: (ResultSet) -> T): List<T> {
    val cnn = DB.getConnectionRO()
    val statement = cnn.prepareStatement(this)
    val resultSet = statement.executeQuery()
    return resultSet.use {
        generateSequence {
            if (resultSet.next()) factory(resultSet) else null
        }.toList() // must be inside the use() block
    }
}
