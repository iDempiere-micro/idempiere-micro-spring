package company.bigger.test.support

import org.compiere.orm.DefaultModelFactory
import org.compiere.orm.IModelFactory
import org.idempiere.common.util.Env
import org.idempiere.icommon.model.IPO
import org.junit.Before
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

abstract class BaseComponentTest : BaseTest() {
    protected fun loginClient(idClient: Int) {
        val ctx = Env.getCtx()
        val AD_CLIENT_ID = idClient
        val AD_CLIENT_ID_s = AD_CLIENT_ID.toString()
        ctx.setProperty(Env.AD_CLIENT_ID, AD_CLIENT_ID_s)
        Env.setContext(ctx, Env.AD_CLIENT_ID, AD_CLIENT_ID_s)
    }

    @Before
    fun prepareEnv() {
        loginClient(1000000)
    }

    fun <T : IPO> getById(id: Int, tableName: String): T {
        val modelFactory: IModelFactory = DefaultModelFactory()
        val result = modelFactory.getPO(tableName, id, null)
        println(result)
        assertNotNull(result)
        val obj = result as T
        assertNotNull(obj)
        assertEquals(id, obj._ID)
        return obj
    }

    val ctx get() = Env.getCtx()
    val AD_CLIENT_ID get() = ctx.getProperty(Env.AD_CLIENT_ID).toInt()
}