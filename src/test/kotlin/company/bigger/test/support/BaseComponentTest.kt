package company.bigger.test.support

import org.compiere.client.tests.SetupClientTests
import org.compiere.model.I_M_Product
import org.compiere.orm.DefaultModelFactory
import org.compiere.orm.IModelFactory
import org.compiere.orm.MClient
import org.compiere.orm.Query
import org.compiere.product.MProduct
import org.compiere.product.MUOM
import org.idempiere.common.util.Env
import org.idempiere.icommon.model.IPO
import org.junit.Before
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

abstract class BaseComponentTest : BaseTest() {
    companion object {
        val NEW_AD_CLIENT_ID = 1000000
    }

    protected fun loginClient(idClient: Int) {
        val ctx = Env.getCtx()
        val AD_CLIENT_ID = idClient
        val AD_CLIENT_ID_s = AD_CLIENT_ID.toString()
        ctx.setProperty(Env.AD_CLIENT_ID, AD_CLIENT_ID_s)
        Env.setContext(ctx, Env.AD_CLIENT_ID, AD_CLIENT_ID_s)
    }

    @Before
    fun prepareEnv() {
        val query = Query(this.ctx, "AD_Client", "ad_client_id=$NEW_AD_CLIENT_ID", null)
        val result = query.list<MClient>()
        if (result.isEmpty()) {
            SetupClientTests.createClient(ctx, { loginClient(0) })
        }

        loginClient(NEW_AD_CLIENT_ID)
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

    protected fun getProductById(product_id: Int): I_M_Product {
        val modelFactory: IModelFactory = DefaultModelFactory()
        val result = modelFactory.getPO(I_M_Product.Table_Name, product_id, null)
        println(result)
        assertNotNull(result)
        val product = result as I_M_Product
        assertNotNull(product)
        assertEquals(product_id, product._ID)
        return product
    }

    fun createAProduct(name: String, productType: String): I_M_Product {
        val standardProduct = getProductById(1000000)
        val ctx = Env.getCtx()
        val product = MProduct(ctx, 0, null)
        product.name = name
        product.value = name
        product.c_UOM_ID = MUOM.getDefault_UOM_ID(ctx)
        product.m_Product_Category_ID = standardProduct.m_Product_Category_ID
        product.c_TaxCategory_ID = standardProduct.c_TaxCategory_ID
        product.productType = productType // I_M_Product.PRODUCTTYPE_Service
        product.save()
        return getProductById(product._ID)
    }
}