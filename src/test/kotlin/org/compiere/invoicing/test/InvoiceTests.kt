package org.compiere.invoicing.test

import company.bigger.test.support.BaseComponentTest
import org.compiere.accounting.MOrder
import org.compiere.accounting.MOrderLine
import org.compiere.accounting.MProduct
import org.compiere.accounting.MWarehouse
import org.compiere.crm.MBPartner
import org.compiere.invoicing.MInvoice
import org.compiere.model.I_C_Invoice
import org.compiere.model.I_C_Order
import org.compiere.orm.DefaultModelFactory
import org.compiere.orm.IModelFactory
import org.idempiere.common.util.Env
import org.junit.Test
import java.sql.Timestamp
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class InvoiceTests : BaseComponentTest() {
    @Test
    fun `get invoice by id`() {
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
        order.aD_Org_ID = 50001
        order.m_Warehouse_ID = 50002

        val id = 118
        val partner = MBPartner.get(Env.getCtx(), id)

        val product_id = 136
        val product = MProduct.get(Env.getCtx(), product_id)

        order.setBPartner(partner)
        order.save()

        val orderLine = MOrderLine(order)
        orderLine.product = product
        orderLine.setQty(1.toBigDecimal())
        orderLine.save()
    }

}