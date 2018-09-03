package company.bigger.test.support

import company.bigger.common.db.CConnection
import company.bigger.util.DatabaseImpl
import company.bigger.util.DummyEventManager
import company.bigger.util.DummyService
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import company.bigger.util.Ini
import org.compiere.bo.tests.TestUpdateCustomer
import org.idempiere.common.db.Database
import org.idempiere.common.util.CLogger
import org.idempiere.common.util.DB
import org.junit.Before
import org.springframework.test.context.ContextConfiguration
import software.hsharp.api.icommon.ICConnection

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = [company.bigger.Application::class])
abstract class BaseTest {
    @Autowired
    protected lateinit var ini: Ini

    @Autowired
    protected lateinit var cconnection: ICConnection

    @Before
    open fun prepare() {
        DummyService.setup()
        DummyEventManager.setup()
        CLogger.getCLogger(TestUpdateCustomer::class.java)
        val db = Database()
        db.setDatabase(DatabaseImpl())
        DB.setDBTarget(CConnection.get())
        DB.isConnected()
    }
}