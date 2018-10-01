package company.bigger.test.support

import org.idempiere.common.util.Env
import org.junit.Before

open class BaseComponentTest : BaseTest() {
    @Before
    fun prepareEnv() {
        val ctx = Env.getCtx()
        val AD_CLIENT_ID = 11
        val AD_CLIENT_ID_s = AD_CLIENT_ID.toString()
        ctx.setProperty(Env.AD_CLIENT_ID, AD_CLIENT_ID_s)
        Env.setContext(ctx, Env.AD_CLIENT_ID, AD_CLIENT_ID_s)
    }
}