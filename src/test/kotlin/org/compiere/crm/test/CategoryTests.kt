package org.compiere.crm.test

import company.bigger.test.support.BaseComponentTest
import org.compiere.crm.MBPartner
import org.compiere.crm.MCrmCategory
import org.compiere.crm.MCrmCustomerCategory
import org.junit.Test
import kotlin.test.assertNotNull

class CategoryTests : BaseComponentTest() {
    @Test
    fun `create crm category`() {
        val category = MCrmCategory(ctx, 0, null)
        category.setName("Test")
        category.setValue("Test")
        category.save()
        val cat: MCrmCategory = getById(category._ID, MCrmCategory.Table_Name)
        assertNotNull(cat)
    }

    @Test
    fun `create crm category, business partner and assign them`() {
        val category = MCrmCategory(ctx, 0, null)
        val catName = "Test-${randomString(10)}"
        category.setName(catName)
        category.setValue(catName)
        category.save()
        val cat: MCrmCategory = getById(category._ID, MCrmCategory.Table_Name)
        assertNotNull(cat)

        val newPartner = MBPartner.getTemplate(ctx, AD_CLIENT_ID)
        val name = "Test " + randomString(10)
        newPartner.setName(name)
        val value = "t-" + randomString(5)
        newPartner.setValue(value)
        newPartner.save()

        val bp: MBPartner = getById(newPartner._ID, MBPartner.Table_Name)
        assertNotNull(bp)

        val bpartnerInCategory = MCrmCustomerCategory(ctx, 0, null)
        bpartnerInCategory.bPartner = newPartner
        bpartnerInCategory.setName(randomString(10))
        bpartnerInCategory.category = cat

        bpartnerInCategory.save()

        val bpInCat: MCrmCustomerCategory = getById(bpartnerInCategory._ID, MCrmCustomerCategory.Table_Name)
        assertNotNull(bpInCat)
    }
}