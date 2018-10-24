package org.compiere.invoicing.test

import company.bigger.test.support.BaseComponentTest
import company.bigger.test.support.asResource
import company.bigger.test.support.executeSql
import company.bigger.test.support.randomString
import org.compiere.accounting.MOrder
import org.compiere.accounting.MOrderLine
import org.compiere.accounting.MPayment
import org.compiere.accounting.MProduct
import org.compiere.crm.MBPartner
import org.compiere.invoicing.MInvoice
import org.compiere.model.I_C_Invoice
import org.compiere.model.I_C_Payment
import org.compiere.model.I_M_Product
import org.compiere.model.I_M_Production
import org.compiere.orm.DefaultModelFactory
import org.compiere.orm.IModelFactory
import org.compiere.process.DocAction
import org.compiere.process.ProcessInfo
import org.compiere.product.MPriceList
import org.compiere.product.MProductPrice
import org.compiere.production.MProduction
import org.idempiere.common.util.Env
import org.idempiere.process.ProductionCreate
import org.junit.Test
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.fail

data class InvoiceImportantTestAttributes(
    val grandTotal: BigDecimal,
    val grandTotalVAT: BigDecimal,
    val reverseCharge: Boolean,
    val dueDate1: Date,
    val dueDate2: Date
)

data class MaterialMovementImportantTestAttributes(
    val productName: String,
    val moveDate: Date?,
    val amountIn: BigDecimal,
    val amountOut: BigDecimal
)

class InvoiceTests : BaseComponentTest() {
    companion object {
        const val QTY = 1
        const val PARTNER_ID = 1000000
        const val PROD_1 = 1000000
        const val BOM_1 = 1000001
        private var index = 0
    }

    @Test
    fun `get invoice by id`() {
        loginClient(11)
        val invoice_id = 106

        val invoice: MInvoice = getById(invoice_id, I_C_Invoice.Table_Name)
        assertNotNull(invoice)
        assertEquals(invoice_id, invoice._ID)
        val lines = invoice.lines
        assertNotNull(lines)
        assertEquals(6, lines.count())
    }

    private fun createOrder(c_DocType_ID: Int, product_id: Int): Triple<MOrder, Int, Int> {
        val order = MOrder(Env.getCtx(), 0, null)
        order.setAD_Org_ID(1000000)
        order.m_Warehouse_ID = 1000000
        order.setIsSOTrx(true)
        order.c_DocType_ID = c_DocType_ID // 133 on credit order (generates invoice), 130 prepay order, 132 standard order

        val id = PARTNER_ID
        val partner = MBPartner.get(Env.getCtx(), id)

        val product = MProduct.get(Env.getCtx(), product_id)

        order.setBPartner(partner)
        order.save()

        val orderLine = MOrderLine(order)
        orderLine.product = product
        val qty = QTY
        orderLine.setQty(qty.toBigDecimal())
        orderLine.save()

        return Triple(order, id, product_id)
    }

    @Test
    fun `create invoice from prepay order after receiving the payment`() {
        createInvoiceFromOrder(1000030, PROD_1, BigDecimal("1.10")) {
            val payment = MPayment(ctx, 0, null)
            payment.c_BPartner_ID = PARTNER_ID
            payment.setAD_Org_ID(1000000)
            payment.c_BankAccount_ID = 1000000
            payment.setC_Currency_ID(102) // EUR
            payment.payAmt = 1.10.toBigDecimal()
            payment.save()

            val pay: MPayment = getById(payment._ID, I_C_Payment.Table_Name)
            assertNotNull(pay)

            it.c_Payment_ID = pay._ID
            it.save()
        }
    }

    private fun createInvoiceFromOrder(c_DocType_ID: Int, productId: Int, expectedPrice: BigDecimal, doAfterOrderTask: (MOrder) -> Unit) {
        val (order, id, product_id) = createOrder(c_DocType_ID, productId)
        doAfterOrderTask(order)

        order.setDocAction(DocAction.STATUS_Completed)
        val completion = order.completeIt()
        order.save()

        val invoice = completion.result as I_C_Invoice
        assertEquals(1, order.invoices.count())
        assertEquals(invoice, order.invoices.first())
        assertEquals(id, invoice.c_BPartner_ID)
        val lines = invoice.getLines(false)
        assertEquals(1, lines.count())
        val line = lines.first()
        assertEquals(product_id, line.m_Product_ID)
        assertEquals(QTY.toBigDecimal(), line.qtyInvoiced)

        val modelFactory: IModelFactory = DefaultModelFactory()
        val result = modelFactory.getPO(I_C_Invoice.Table_Name, invoice._ID, null)
        println(result)
        assertNotNull(result)
        val invoice1 = result as MInvoice
        assertNotNull(invoice1)
        assertEquals(invoice._ID, invoice1._ID)
        assertEquals(expectedPrice, invoice1.getGrandTotal(false))
        val lines1 = invoice1.lines
        assertNotNull(lines1)
        assertEquals(1, lines1.count())

        "/sql/invoice_details.sql".asResource {
            val list = it.executeSql {
                InvoiceImportantTestAttributes(
                        it.getBigDecimal("grandtotal"), it.getBigDecimal("grandtotalvat"),
                        it.getBoolean("reverse_charge"), it.getDate("due_previous_business_day"),
                        it.getDate("due_previous_5business_days")
                )
            }
            assertEquals(1, list.count())
            val details = list.first()
            assertFalse(details.reverseCharge)
            assertEquals(BigDecimal("1.10"), details.grandTotal)
            assertEquals(BigDecimal("0.10"), details.grandTotalVAT)
            val now = java.util.Date()
            assertTrue(details.dueDate1 < now)
            assertTrue(details.dueDate2 < now)
        }
        index++
        `should have 3 material movements after 2 runs`()
    }

    @Test
    fun `create invoice from order (on credit)`() {
        createInvoiceFromOrder(1000033, PROD_1, BigDecimal("1.10")) {}
    }

    @Test
    // @Ignore
    fun `create invoice from BOM order with production step in between (on credit)`() {
        createInvoiceFromOrder(1000033, BOM_1, BigDecimal("12.10")) {
            val orderLine = it.lines.first()
            val production = MProduction(orderLine)
            production.setAD_Org_ID(1000000)
            production.m_Product_ID = orderLine.m_Product_ID // TODO: Why? Should not this be done automatically in the constructor?
            production.productionQty = orderLine.qtyOrdered // TODO: Why? Should not this be done automatically in the constructor?
            production.m_Locator_ID = 1000000
            production.save()

            val productionCreate = ProductionCreate(m_production = production)
            val pi = ProcessInfo("", 0)
            productionCreate.startProcess(ctx, pi, null)

            val prod: MProduction = getById(production._ID, I_M_Production.Table_Name)

            prod.setDocAction(DocAction.STATUS_Completed)
            prod.save()

            prod.completeIt()
        }
    }

    @Test
    fun `create invoice from order (on credit) without pricelist should fail`() {
        val product = createAProduct("Other 1-" + randomString(5), I_M_Product.PRODUCTTYPE_Item)
        try {
            createInvoiceFromOrder(1000033, product._ID, BigDecimal("1.10")) {}
            fail("Invoice was created for a product not on a pricelist")
        } catch (e: Exception) {
        }
    }

    @Test
    fun `create invoice from order (on credit) without amount on hand should fail`() {
        val product = createAProduct("Other 1-" + randomString(5), I_M_Product.PRODUCTTYPE_Item)
        val pl = MPriceList(ctx, 1000000, null)
        val plv = pl.getPriceListVersion(Timestamp.from(Instant.now()))
        val price = 10.toBigDecimal()
        val pp = MProductPrice(ctx, plv._ID, product._ID, price, price, price, null)
        pp.save()
        try {
            createInvoiceFromOrder(1000033, product._ID, BigDecimal("11.00")) {}
            fail("Invoice was created for a product with negative inventory")
        } catch (e: Exception) {
        }
    }

    fun `should have 3 material movements after 2 runs`() {
        if (index == 3) {
            "/sql/recent_material_movements.sql".asResource {
                val list = it.executeSql {
                    MaterialMovementImportantTestAttributes(
                            it.getString("pro_name"), it.getDate("move_date"),
                            it.getBigDecimal("amout_in"), it.getBigDecimal("amout_out")
                    )
                }
                kotlin.test.assertEquals(6, list.count())
                val standards = list.filter { it.productName == "Standard" }
                kotlin.test.assertEquals(4, standards.count())
                kotlin.test.assertEquals(1000000 - 2 * 1 - 10, standards.sumBy { (it.amountIn - it.amountOut).toInt() })
            }
        }
    }
}