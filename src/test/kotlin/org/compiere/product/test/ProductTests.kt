import company.bigger.test.support.BaseComponentTest
import org.compiere.orm.DefaultModelFactory
import org.compiere.orm.IModelFactory
import org.compiere.product.MProduct
import org.compiere.model.I_M_Product
import org.compiere.product.MUOM
import org.idempiere.common.util.Env
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ProductTests : BaseComponentTest() {
    private fun getProductById(product_id: Int): I_M_Product {
        val modelFactory: IModelFactory = DefaultModelFactory()
        val result = modelFactory.getPO(I_M_Product.Table_Name, product_id, null)
        println(result)
        assertNotNull(result)
        val product = result as I_M_Product
        assertNotNull(product)
        assertEquals(product_id, product._ID)
        return product
    }

    @Test
    fun `getting a product using DefaultModelFactory should work`() {
        getProductById(1000000)
    }

    @Test
    fun `creating a new service product should work`() {
        val standardProduct = getProductById(1000000)
        val ctx = Env.getCtx()
        val product = MProduct(ctx, 0, null)
        product.name = "Service"
        product.value = "service"
        product.c_UOM_ID = MUOM.getDefault_UOM_ID(ctx)
        product.m_Product_Category_ID = standardProduct.m_Product_Category_ID
        product.c_TaxCategory_ID = standardProduct.c_TaxCategory_ID
        product.productType = I_M_Product.PRODUCTTYPE_Service
        product.save()
        getProductById(product._ID)
    }
}