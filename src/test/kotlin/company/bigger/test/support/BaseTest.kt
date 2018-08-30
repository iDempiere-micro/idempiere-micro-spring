package company.bigger.test.support

import company.bigger.common.db.CConnection
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import company.bigger.util.Ini
import org.springframework.test.context.ContextConfiguration
import software.hsharp.api.icommon.ICConnection

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes=[company.bigger.Application::class])
abstract class BaseTest {
    @Autowired
    protected lateinit var ini: Ini

    @Autowired
    protected lateinit var cconnection: ICConnection
}