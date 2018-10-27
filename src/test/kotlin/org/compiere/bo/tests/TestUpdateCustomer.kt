package org.compiere.bo.tests

import org.compiere.bo.CustomerProcessBase
import org.compiere.bo.UpdateCustomer
import org.compiere.crm.MBPartner
import org.compiere.crm.MBPartnerLocation
import org.compiere.crm.MCountry
import org.compiere.crm.MLocation
import org.compiere.crm.MRegion
import org.compiere.model.I_C_BPartner
import org.idempiere.common.util.Env
import org.junit.Test

class TestUpdateCustomer : BaseCustomerTest() {
    var newPartner: I_C_BPartner? = null

    override fun getPartnerId(): Int? {
        return newPartner!!.c_BPartner_ID
    }

    override fun getProcess(): CustomerProcessBase {
        return UpdateCustomer()
    }

    @Test
    fun `Update customer works`() {
        /**
         * We create the business partner before running the transactional update process, because that is
         * how the update is supposed to work (you have the business partner in the database before running the update)
         */
        val ctx = Env.getCtx()
        val AD_CLIENT_ID = 11
        newPartner = MBPartner.getTemplate(ctx, AD_CLIENT_ID)
        val bp = newPartner!!
        bp.setName("bp-o-" + randomString(10))
        bp.setValue("v-o-" + randomString(10))
        bp.save()

        val defaultCountry = MCountry.getDefault(ctx)
        val defaultRegion = MRegion.getDefault(ctx)
        val location = MLocation(defaultCountry, defaultRegion)
        location.save()
        val partnerLocation = MBPartnerLocation(newPartner)
        partnerLocation.c_Location_ID = location.c_Location_ID
        partnerLocation.save()

        doTheTest()
    }
}
