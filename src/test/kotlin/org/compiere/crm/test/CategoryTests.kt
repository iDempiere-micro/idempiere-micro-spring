package org.compiere.crm.test

import company.bigger.test.support.BaseComponentTest
import org.compiere.crm.MCrmCategory
import org.idempiere.common.util.Env
import org.junit.Test

class CategoryTests : BaseComponentTest() {
    @Test
    fun `create crm category`() {
        val category = MCrmCategory(Env.getCtx(), 0, null)
        category.save()
    }
}