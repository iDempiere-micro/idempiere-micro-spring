package org.compiere.bo

import org.compiere.crm.MBPartnerLocation
import org.compiere.crm.MCrmCustomerCategory
import org.compiere.crm.MUser
import org.compiere.crm.SvrProcessBaseSql
import org.compiere.crm.MCrmCategory
import org.compiere.model.I_C_BPartner
import org.compiere.model.I_C_BPartner_Location
import org.compiere.orm.DefaultModelFactory
import org.compiere.orm.IModelFactory
import org.compiere.orm.Query
import org.idempiere.common.util.Env
import org.idempiere.common.util.Trx
import org.idempiere.orm.I_Persistent
import java.sql.Connection

data class CustomerProcessBaseResult(val C_BPartner_Id: Int) : java.io.Serializable

fun updateCustomerCategory(customerCategoryId: Int?, bpartner: I_C_BPartner, trx: Trx) {
    val query = Query(Env.getCtx(), "Crm_Customer_Category", "c_bpartner_id = ${bpartner.c_BPartner_ID}", null)
    val found = query.list<MCrmCustomerCategory>()
    val ctx = Env.getCtx()
    val crmCustomerCategory =
        if (found.isEmpty()) {
            val new = MCrmCustomerCategory(ctx, 0, trx.trxName)
            new
        } else {
            found.first()
        }
    if (customerCategoryId == null || customerCategoryId == 0) {
        crmCustomerCategory.delete(true)
    } else {
        val category = MCrmCategory(ctx, customerCategoryId, trx.trxName)
        crmCustomerCategory.category = category
        crmCustomerCategory.bPartner = bpartner
        crmCustomerCategory.setName("${bpartner.name} in ${category.getName()}")
        crmCustomerCategory.save()
    }
}

abstract class CustomerProcessBase(
    _AD_CLIENT_ID: Int = 0,
    _AD_ORG_ID: Int = 0,
    _AD_USER_ID: Int = 0,
        // legal name
    var bpName: String? = null, // name
    // description
    var description: String? = null, // description
    // friendly name
    var value: String? = null, // value
    // DUNS (IC)
    var duns: String? = null,
    // Tax ID (VAT ID)
    var taxid: String? = null,
    // place of business name
    var locationName: String? = null,
    // place of business address
    var locationAddress: String? = null, // address1
    // place of business city
    var locationCity: String? = null, // city
    // place of business postal
    var locationPostal: String? = null, // postal
    // place of business phone
    var locationPhone: String? = null, // phone
    // legal address
    var legalAddress: String? = null,
    // legal city
    var legalCity: String? = null,
    // legal postal
    var legalPostal: String? = null,
    // telefon na sídlo
    var legalPhone: String? = null,
    // order contact person
    var orderContactPerson: String? = null,
    // decision maker
    var decisionMaker: String? = null,
    // invoicing contact
    var invoicingContact: String? = null,
    // already a customer
    var isCustomer: Boolean? = null,
    // customer category
    var customerCategoryId: Int? = null,
    // flat discount
    var discount: Int? = null,
    // account manager
    var salesRepId: Int? = null,

    var locationCountryId: Int? = null,
    var legalCountryId: Int? = null

) : SvrProcessBaseSql(_AD_CLIENT_ID, _AD_ORG_ID, _AD_USER_ID) {
    override val isRO: Boolean
        get() = false

    abstract val trxName: String

    val modelFactory: IModelFactory = DefaultModelFactory()

    override fun prepare() {
        super.prepare()
        for (para in parameter) {
            if (para.parameterName == "bpName") {
                bpName = para.parameterAsString
            } else if (para.parameterName == "description") {
                description = para.parameterAsString
            } else if (para.parameterName == "value") {
                value = para.parameterAsString
            } else if (para.parameterName == "duns") {
                duns = para.parameterAsString
            } else if (para.parameterName == "taxid") {
                taxid = para.parameterAsString
            } else if (para.parameterName == "locationName") {
                locationName = para.parameterAsString
            } else if (para.parameterName == "locationAddress") {
                locationAddress = para.parameterAsString
            } else if (para.parameterName == "locationCity") {
                locationCity = para.parameterAsString
            } else if (para.parameterName == "locationPostal") {
                locationPostal = para.parameterAsString
            } else if (para.parameterName == "locationPhone") {
                locationPhone = para.parameterAsString
            } else if (para.parameterName == "legalAddress") {
                legalAddress = para.parameterAsString
            } else if (para.parameterName == "legalCity") {
                legalCity = para.parameterAsString
            } else if (para.parameterName == "legalPostal") {
                legalPostal = para.parameterAsString
            } else if (para.parameterName == "legalPhone") {
                legalPhone = para.parameterAsString
            } else if (para.parameterName == "orderContactPerson") {
                orderContactPerson = para.parameterAsString
            } else if (para.parameterName == "decisionMaker") {
                decisionMaker = para.parameterAsString
            } else if (para.parameterName == "invoicingContact") {
                invoicingContact = para.parameterAsString
            } else if (para.parameterName == "isCustomer") {
                isCustomer = para.parameterAsBoolean
            } else if (para.parameterName == "customerCategoryId") {
                val _customerCategoryId: Int? = para.parameterAsInt
                if (_customerCategoryId == null || _customerCategoryId < 1) { customerCategoryId = null } else { customerCategoryId = _customerCategoryId }
            } else if (para.parameterName == "discount") {
                discount = para.parameterAsInt
            } else if (para.parameterName == "salesRepId") {
                salesRepId = para.parameterAsInt
            } else if (para.parameterName == "locationCountryId") {
                locationCountryId = para.parameterAsInt
            } else if (para.parameterName == "legalCountryId") {
                legalCountryId = para.parameterAsInt
            }
        }
    }

    fun setLocParams(location: I_C_BPartner_Location) {
        if (locationName != null) {
            location.name = locationName
        }
        val loc = location.location
        if (locationAddress != null) {
            loc.setAddress1(locationAddress)
        }
        if (locationCity != null) {
            loc.setCity(locationCity)
        }
        if (locationPostal != null) {
            loc.postal = locationPostal
        }
        locationCountryId?.let { loc.c_Country_ID = it }
        (loc as I_Persistent).save()
        location.setIsBillTo(false)
        if (locationPhone != null) {
            location.phone = locationPhone
        }
        location.c_Location_ID = loc.c_Location_ID
    }

    fun setLegalParams(location: I_C_BPartner_Location) {
        val loc = location.location
        legalAddress?.let { loc.address1 = it }
        legalCity?.let { loc.city = it }
        legalPostal?.let { loc.postal = it }
        legalCountryId?.let { loc.c_Country_ID = it }
        (loc as I_Persistent).save()
        location.setIsShipTo(false)
        legalPhone.let { location.phone = it }
        location.c_Location_ID = loc.c_Location_ID
    }

    abstract fun getData(m_trx: Trx): I_C_BPartner

    fun coreAction(m_trx: Trx): CustomerProcessBaseResult {
        val result = getData(m_trx)

        if (bpName != null) {
            result.setName(bpName)
        }
        if (value != null) {
            result.setValue(value)
        }
        if (description != null) {
            result.description = description
        }
        if (duns != null) {
            result.duns = duns
        }
        if (taxid != null) {
            result.taxID = taxid
        }
        result.save()

        val location = result.locations.firstOrNull { it.isShipTo }
        val updatedLocation =
            if (location != null) {
                setLocParams(location)
                location
            } else {
                val newLocation = MBPartnerLocation(result)
                newLocation.setIsShipTo(true)
                setLocParams(newLocation)
                newLocation
            }
        updatedLocation.save()

        val legal = result.locations.firstOrNull { it.isBillTo && !it.isShipTo }
        val updatedLegal =
            if (legal != null) {
                setLegalParams(legal)
                legal
            } else {
                val newLocation = MBPartnerLocation(result)
                newLocation.setIsBillTo(true)
                setLegalParams(newLocation)
                newLocation
            }
        updatedLegal.save()

        val contactPersons = result.getContacts()
        val orderContact =
            if (contactPersons.count() > 0) {
                val orderContact = contactPersons[0]
                orderContact.name = "" + orderContactPerson
                orderContact.value = orderContact.name
                orderContact
            } else {
                val orderContact = MUser(result)
                orderContact.setName("" + orderContactPerson)
                orderContact.value = orderContact.name
                orderContact
            }
        (orderContact as I_Persistent).save()

        val decisionMaker =
            if (contactPersons.count() > 1) {
                val decisionMaker = contactPersons[1]
                decisionMaker.name = "" + this.decisionMaker
                decisionMaker.value = decisionMaker.name
                decisionMaker
            } else {
                val decisionMaker = MUser(result)
                decisionMaker.setName("" + this.decisionMaker)
                decisionMaker.value = decisionMaker.name
                decisionMaker
            }
        (decisionMaker as I_Persistent).save()

        val invoicingPerson =
            if (contactPersons.count() > 2) {
                val invoicingPerson = contactPersons[2]
                invoicingPerson.name = "" + invoicingContact
                invoicingPerson.value = invoicingPerson.name
                invoicingPerson
            } else {
                val invoicingPerson = MUser(result)
                invoicingPerson.setName("" + invoicingContact)
                invoicingPerson.value = invoicingPerson.name
                invoicingPerson
            }
        (invoicingPerson as I_Persistent).save()

        isCustomer?.let { result.setIsCustomer(it) }
        discount?.let { result.flatDiscount = it.toBigDecimal() }
        salesRepId?.let { result.salesRep_ID = it }

        result.save()

        doUpdateCustomerCategory(result, m_trx)

        return CustomerProcessBaseResult(result.c_BPartner_ID)
    }

    private fun doUpdateCustomerCategory(bpartner: I_C_BPartner, trx: Trx) {
        val _customerCategoryId = customerCategoryId
        updateCustomerCategory(_customerCategoryId, bpartner, trx)
    }

    override fun getSqlResult(cnn: Connection): java.io.Serializable {
        val m_trx = Trx.get(Trx.createTrxName(trxName), true)
        try {
            m_trx.start()
            val result = coreAction(m_trx)
            m_trx.commit()
            return result
        } catch (e: Exception) {
            m_trx.rollback()
            throw e
        } finally {
            m_trx.close()
        }
    }
}