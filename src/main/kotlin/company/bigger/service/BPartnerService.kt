package company.bigger.service

import company.bigger.dto.CreateCustomerModel
import org.compiere.bo.CreateCustomer
import org.compiere.crm.DetailResult
import org.compiere.process.ProcessInfo
import org.compiere.process.ProcessInfoParameter
import org.compiere.process.ProcessUtil
import org.idempiere.common.util.Env
import org.springframework.stereotype.Service

@Service
class BPartnerService {
    fun createBPartner(model: CreateCustomerModel): DetailResult {
        val ctx = Env.getCtx()
        val ad_Client_ID = Env.getAD_Client_ID(ctx)
        val ad_Org_ID = Env.getAD_Org_ID(ctx)
        val ad_User_ID = Env.getAD_User_ID(ctx)
        val parameters: MutableList<ProcessInfoParameter> = mutableListOf()
        val process = CreateCustomer(
            ad_Client_ID, ad_Org_ID, ad_User_ID,
            model.bpName, // name
            // description
            model.description, // description
            // friendly name
            model.value, // value
            // DUNS (IC)
            model.duns,
            // Tax ID (VAT ID)
            model.taxid,
            // place of business name
            model.locationName,
            // place of business address
            model.locationAddress, // address1
            // place of business city
            model.locationCity, // city
            // place of business postal
            model.locationPostal, // postal
            // place of business phone
            model.locationPhone, // phone
            // legal address
            model.legalAddress,
            // legal city
            model.legalCity,
            // legal postal
            model.legalPostal,
            // telefon na sídlo
            model.legalPhone,
            // order contact person
            model.orderContactPerson,
            // decision maker
            model.decisionMaker,
            // invoicing contact
            model.invoicingContact,
            // already a customer
            model.isCustomer,
            // customer category
            model.customerCategoryId,
            // flat discount
            model.discount,
            // account manager
            model.salesRepId,

            model.locationCountryId,
            model.legalCountryId
        )
        val processInfo = ProcessInfo("Create Business Partner", 0)
        processInfo.aD_Client_ID = ad_Client_ID
        processInfo.aD_User_ID = ad_User_ID
        processInfo.parameter = parameters.toTypedArray()
        processInfo.className = process.javaClass.canonicalName
        ProcessUtil.startJavaProcess(ctx, processInfo, null, false, null, process)
        return processInfo.serializableObject as DetailResult
    }
}