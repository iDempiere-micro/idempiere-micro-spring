import company.bigger.test.support.BaseComponentTest
import company.bigger.test.support.randomString
import org.compiere.model.I_M_Product
import org.junit.Test

class ProductTests : BaseComponentTest() {
    @Test
    fun `getting a product using DefaultModelFactory should work`() {
        getProductById(1000000)
    }

    @Test
    fun `creating a new service product should work`() {
        createAProduct("S-" + randomString(10), I_M_Product.PRODUCTTYPE_Service)
    }
}