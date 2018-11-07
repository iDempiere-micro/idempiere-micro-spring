package org.compiere.production.test

import company.bigger.test.support.BaseComponentTest
import org.compiere.model.I_R_Request
import org.compiere.production.MRequest
import org.compiere.production.MRequestType
import org.compiere.production.MStatus
import org.compiere.production.MStatusCategory
import org.compiere.production.MProduction
import org.idempiere.common.util.Env
import org.junit.Test
import kotlin.test.assertEquals

class RequestTests : BaseComponentTest() {
    companion object {
        const val SUMMARY = "summary"
    }

    @Test
    fun `creating a new request should work`() {
        val ctx = Env.getCtx()
        val requestStatusCategory = MStatusCategory(ctx, 0, null)
        requestStatusCategory.name = "Default"
        requestStatusCategory.save()

        val requestStatus = MStatus(ctx, 0, null)
        requestStatus.name = "Default"
        requestStatus.value = "Default"
        requestStatus.setIsDefault(true)
        requestStatus.r_StatusCategory_ID = requestStatusCategory.id
        requestStatus.save()

        val requestType = MRequestType(ctx, 0, null)
        requestType.name = "General Request"
        requestType.setIsDefault(true)
        requestType.r_StatusCategory_ID = requestStatusCategory.id
        requestType.save()

        val request = MRequest(ctx, 1000001, requestType.id, SUMMARY, true, null)
        request.setR_Status_ID()
        request.save()
        val req: I_R_Request = getById(request.id, I_R_Request.Table_Name)

        assertEquals(SUMMARY, req.summary)
    }

    @Test
    fun `creating a new production should work`() {
        val production = MProduction(ctx, 0, null)
        production.save()
    }
}