package org.compiere.crm.test

import company.bigger.service.CategoryService
import company.bigger.test.support.BaseComponentTest
import org.compiere.crm.MBPartner
import org.compiere.crm.MCrmCategory
import org.compiere.crm.MCrmCustomerCategory
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

class CategoryTests : BaseComponentTest() {
    @Autowired
    private lateinit var categoryService: CategoryService

    @Test
    fun `create crm category`() {
        val category = MCrmCategory(ctx, 0, null)
        category.setName("Test")
        category.setValue("Test")
        category.save()
        val cat: MCrmCategory = getById(category.id, MCrmCategory.Table_Name)
        assertNotNull(cat)

        `get all crm categories from the service works`()
    }

    @Test
    fun `create crm category, business partner and assign them`() {
        val category = MCrmCategory(ctx, 0, null)
        val catName = "Test-${randomString(10)}"
        category.setName(catName)
        category.setValue(catName)
        category.save()
        val cat: MCrmCategory = getById(category.id, MCrmCategory.Table_Name)
        assertNotNull(cat)

        val newPartner = MBPartner.getTemplate(ctx, AD_CLIENT_ID)
        val name = "Test " + randomString(10)
        newPartner.setName(name)
        val value = "t-" + randomString(5)
        newPartner.setValue(value)
        newPartner.save()

        val bp: MBPartner = getById(newPartner.id, MBPartner.Table_Name)
        assertNotNull(bp)

        val bpartnerInCategory = MCrmCustomerCategory(ctx, 0, null)
        bpartnerInCategory.bPartner = newPartner
        bpartnerInCategory.setName(randomString(10))
        bpartnerInCategory.category = cat

        bpartnerInCategory.save()

        val bpInCat: MCrmCustomerCategory = getById(bpartnerInCategory.id, MCrmCustomerCategory.Table_Name)
        assertNotNull(bpInCat)

        `get all crm categories from the service works`()
    }

    fun `get all crm categories from the service works`() {
        val result = categoryService.getAllCategories()
        assertFalse(result.isEmpty())
        result.first()
    }
}