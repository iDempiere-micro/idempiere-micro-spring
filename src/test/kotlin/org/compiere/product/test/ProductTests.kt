import org.compiere.orm.DefaultModelFactory
import org.compiere.orm.IModelFactory
import org.compiere.product.MProduct
import company.bigger.test.support.BaseTest
import org.idempiere.common.util.Env
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ProductTests : BaseTest() {
    @Test
    fun getUsingDefaultModelFactoryById() {
        val ctx = Env.getCtx()
        val AD_CLIENT_ID = 11
        val AD_CLIENT_ID_s = AD_CLIENT_ID.toString()
        ctx.setProperty(Env.AD_CLIENT_ID, AD_CLIENT_ID_s)
        Env.setContext(ctx, Env.AD_CLIENT_ID, AD_CLIENT_ID_s)
        val product_id = 123

        val modelFactory: IModelFactory = DefaultModelFactory()
        val result = modelFactory.getPO("M_Product", product_id, "pokus")
        println(result)
        assertNotNull(result)
        val product = result as MProduct
        assertNotNull(product)
        assertEquals(product_id, product._ID)
    }
}