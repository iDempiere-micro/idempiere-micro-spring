package org.compiere.invoicing.test

import company.bigger.test.support.BaseComponentTest
import org.compiere.invoicing.MInvoice
import org.compiere.model.I_C_Invoice
import org.compiere.orm.DefaultModelFactory
import org.compiere.orm.IModelFactory
import org.junit.Test
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
}