package org.compiere.crm.test

import company.bigger.test.support.BaseComponentTest
import org.compiere.crm.MCrmCategory
import org.idempiere.common.util.Env
import org.junit.Test
import kotlin.test.assertNotNull

class CategoryTests : BaseComponentTest() {
    @Test
    fun `create crm category`() {
        val category = MCrmCategory(Env.getCtx(), 0, null)
        category.setName("Test")
        category.setValue("Test")
        category.save()
        val cat: MCrmCategory = getById(category._ID, MCrmCategory.Table_Name)
        assertNotNull(cat)
    }
}