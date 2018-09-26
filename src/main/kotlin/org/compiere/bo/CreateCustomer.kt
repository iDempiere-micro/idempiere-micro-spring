package org.compiere.bo

import org.compiere.crm.MBPartner
import org.compiere.model.I_C_BPartner
import org.idempiere.common.util.Env
import org.idempiere.common.util.Trx

class CreateCustomer(
        _AD_CLIENT_ID: Int = 0,
        _AD_ORG_ID: Int = 0,
        _AD_USER_ID: Int = 0,
        // legal name
        _bpName: String? = null, // name
        // description
        _description: String? = null, // description
        // friendly name
        _value: String? = null, // value
        // DUNS (IC)
        _duns: String? = null,
        // Tax ID (VAT ID)
        _taxid: String? = null,
        // place of business name
        _locationName: String? = null,
        // place of business address
        _locationAddress: String? = null, // address1
        // place of business city
        _locationCity: String? = null, // city
        // place of business postal
        _locationPostal: String? = null, // postal
        // place of business phone
        _locationPhone: String? = null, // phone
        // legal address
        _legalAddress: String? = null,
        // legal city
        _legalCity: String? = null,
        // legal postal
        _legalPostal: String? = null,
        // telefon na sídlo
        _legalPhone: String? = null,
        // order contact person
        _orderContactPerson: String? = null,
        // decision maker
        _decisionMaker: String? = null,
        // invoicing contact
        _invoicingContact: String? = null,
        // already a customer
        _isCustomer: Boolean? = null,
        // customer category
        _customerCategoryId: Int? = null,
        // flat discount
        _discount: Int? = null,
        // account manager
        _salesRepId: Int? = null,

        _locationCountryId: Int? = null,
        _legalCountryId: Int? = null
) : CustomerProcessBase(
    _AD_CLIENT_ID,
    _AD_ORG_ID,
    _AD_USER_ID,
    // legal name
    _bpName, // name
    // description
    _description, // description
    // friendly name
    _value, // value
    // DUNS (IC)
    _duns,
    // Tax ID (VAT ID)
    _taxid,
    // place of business name
    _locationName,
    // place of business address
    _locationAddress, // address1
    // place of business city
    _locationCity, // city
    // place of business postal
    _locationPostal, // postal
    // place of business phone
    _locationPhone, // phone
    // legal address
    _legalAddress,
    // legal city
    _legalCity,
    // legal postal
    _legalPostal,
    // telefon na sídlo
    _legalPhone,
    // order contact person
    _orderContactPerson,
    // decision maker
    _decisionMaker,
    // invoicing contact
    _invoicingContact,
    // already a customer
    _isCustomer,
    // customer category
    _customerCategoryId,
    // flat discount
    _discount,
    // account manager
    _salesRepId,
    
    _locationCountryId,
    _legalCountryId

) {
    override fun getData(m_trx: Trx): I_C_BPartner {
        return MBPartner.getTemplate(Env.getCtx(), AD_CLIENT_ID)
    }

    override val trxName: String
        get() = "CreateCustomer"
}