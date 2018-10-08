package company.bigger.test.support

import org.idempiere.common.util.Env
import org.junit.Before

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
}