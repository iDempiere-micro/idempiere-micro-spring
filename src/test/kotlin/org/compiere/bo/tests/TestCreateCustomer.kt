package org.compiere.bo.tests

import org.compiere.bo.CreateCustomer
import org.compiere.bo.CustomerProcessBase
import org.junit.Test

class TestCreateCustomer : BaseCustomerTest() {
    override fun getPartnerId(): Int? {
        return null
    }

    override fun getProcess(): CustomerProcessBase {
        return CreateCustomer()
    }

    @Test
    fun `Create customer works`() {
        doTheTest()
    }
}