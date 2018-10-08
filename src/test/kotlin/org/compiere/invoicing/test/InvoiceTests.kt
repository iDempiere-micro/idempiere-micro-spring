package org.compiere.invoicing.test

import company.bigger.test.support.BaseComponentTest
import company.bigger.test.support.asResource
import company.bigger.test.support.executeSql
import org.compiere.accounting.MOrder
import org.compiere.accounting.MOrderLine
import org.compiere.accounting.MProduct
import org.compiere.crm.MBPartner
import org.compiere.invoicing.MInvoice
import org.compiere.model.I_C_Invoice
import org.compiere.orm.DefaultModelFactory
import org.compiere.orm.IModelFactory
import org.compiere.process.DocAction
import org.idempiere.common.util.Env
import org.junit.Test
import java.math.BigDecimal
import java.sql.Date
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

data class InvoiceImportantTestAttributes(
    val grandTotal: BigDecimal,
    val grandTotalVAT: BigDecimal,
    val reverseCharge: Boolean,
    val dueDate1: Date,
    val dueDate2: Date
)

data class MaterialMovementImportantTestAttributes(
    val productName: String,
    val moveDate: Date,
    val amountIn: BigDecimal,
    val amountOut: BigDecimal
)

class InvoiceTests : BaseComponentTest() {
    @Test
    fun `get invoice by id`() {
        loginClient(11)
        val invoice_id = 106

        val modelFactory: IModelFactory = DefaultModelFactory()
        val result = modelFactory.getPO(I_C_Invoice.Table_Name, invoice_id, "pokus")
        println(result)
        assertNotNull(result)
        val invoice = result as MInvoice
        assertNotNull(invoice)
        assertEquals(invoice_id, invoice._ID)
        val lines = invoice.lines
        assertNotNull(lines)
        assertEquals(6, lines.count())
    }

    @Test
    fun `create invoice from order`() {
        val order = MOrder(Env.getCtx(), 0, null)
        order.aD_Org_ID = 1000000
        order.m_Warehouse_ID = 1000000
        order.setIsSOTrx(true)
        // order.setC_DocTypeTarget_ID()
        order.c_DocType_ID = 1000033 // 133 on credit order (generates invoice), 130 prepay order, 132 standard order

        val id = 1000000
        val partner = MBPartner.get(Env.getCtx(), id)

        val product_id = 1000000
        val product = MProduct.get(Env.getCtx(), product_id)

        order.setBPartner(partner)
        order.save()

        val orderLine = MOrderLine(order)
        orderLine.product = product
        val qty = 1
        orderLine.setQty(qty.toBigDecimal())
        orderLine.save()

        order.docAction = DocAction.STATUS_Completed
        order.completeIt()
        order.save()

        assertEquals(1, order.invoices.count())
        val invoice = order.invoices.first()
        assertEquals(id, invoice.c_BPartner_ID)
        val lines = invoice.getLines(false)
        assertEquals(1, lines.count())
        val line = lines.first()
        assertEquals(product_id, line.m_Product_ID)
        assertEquals(qty.toBigDecimal(), line.qtyInvoiced)

        val invoice_id = 1000001

        val modelFactory: IModelFactory = DefaultModelFactory()
        val result = modelFactory.getPO(I_C_Invoice.Table_Name, invoice_id, null)
        println(result)
        assertNotNull(result)
        val invoice1 = result as MInvoice
        assertNotNull(invoice1)
        assertEquals(invoice_id, invoice1._ID)
        assertEquals(BigDecimal("1.10"), invoice1.getGrandTotal(false))
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
        "/sql/recent_material_movements.sql".asResource {
            val list = it.executeSql {
                MaterialMovementImportantTestAttributes(
                    it.getString("pro_name"), it.getDate("move_date"),
                    it.getBigDecimal("amout_in"), it.getBigDecimal("amout_out")
                )
            }
            assertEquals(2, list.count())
            assertEquals(2, list.filter { it.productName == "Standard" }.count())
            assertEquals(1000000 - 1, list.sumBy { (it.amountIn - it.amountOut).toInt() })
        }
    }
}