package org.compiere.bo.tests

import org.compiere.bo.CustomerProcessBase
import org.compiere.bo.CustomerProcessBaseResult
import org.compiere.crm.MBPartner
import company.bigger.util.DatabaseImpl
import org.compiere.crm.MCrmCategory
import org.idempiere.common.util.Env
import java.util.Random
// import org.compiere.bo.updateCustomerCategory
import kotlin.test.assertEquals

abstract class BaseCustomerTest : BaseProcessTest() {
    abstract fun getPartnerId(): Int?
    abstract fun getProcess(): CustomerProcessBase

    // generates random string with small letters of a given length
    fun randomString(length: Int): String {
        // helper extension function to generate random string based on a set
        fun ClosedRange<Char>.randomString(length: Int) =
            (1..length)
                .map { (Random().nextInt(endInclusive.toInt() - start.toInt()) + start.toInt()).toChar() }
                .joinToString("")
        return ('a'..'z').randomString(length)
    }

    fun doTheTest() {
        val ctx = Env.getCtx()
        val AD_CLIENT_ID = 11
        val AD_CLIENT_ID_s = AD_CLIENT_ID.toString()
        ctx.setProperty(Env.AD_CLIENT_ID, AD_CLIENT_ID_s)
        Env.setContext(ctx, Env.AD_CLIENT_ID, AD_CLIENT_ID_s)

        val bPartnerId = getPartnerId()

        val category = MCrmCategory(ctx, 0, null)
        val name = "T-" + randomString(10)
        category.setName(name)
        category.setValue(name)
        category.save()

        val bodyParams = arrayOf(
            "id" to if (bPartnerId == null) { 0 } else { bPartnerId },
            // legal name
            Pair("bpName", "bp-" + randomString(10)), // name
            // description
            Pair("description", "description"), // description
            // friendly name
            Pair("value", "v-" + randomString(10)), // value
            // DUNS (IC)
            Pair("duns", "duns"),
            // Tax ID (VAT ID)
            Pair("taxid", "taxid"),
            // place of business name
            Pair("locationName", "locationName"),
            // place of business address
            Pair("locationAddress", "locationAddress"), // address1
            // place of business city
            Pair("locationCity", "locationCity"), // city
            // place of business postal
            Pair("locationPostal", "1234567890"), // postal
            // place of business country code
            Pair("locationCountryCode", "CZ"), // countryCode
            // place of business phone
            Pair("locationPhone", "locationPhone"), // phone
            // legal address
            Pair("legalAddress", "legalAddress"),
            // legal city
            Pair("legalCity", "legalCity"),
            // legal postal
            Pair("legalPostal", "1234567890"),
            // telefon na sídlo
            Pair("legalPhone", "legalPhone"),
            // order contact person
            Pair("orderContactPerson", "orderContactPerson"),
            // decision maker
            Pair("decisionMaker", "decisionMaker"),
            // invoicing contact
            Pair("invoicingContact", "invoicingContact"),
            // already a customer
            Pair("isCustomer", true),
            // customer category
            Pair("customerCategoryId", category.id),
            // flat discount
            Pair("discount", 7),
            // account manager
            Pair("salesRepId", 101),
            Pair("locationCountryId", 166), // country_id "CZ"
            Pair("legalCountryId", 166) // country_id "CZ"
        )
        val processResult = runProcess(DatabaseImpl(), getProcess(), bodyParams) as CustomerProcessBaseResult

        val newPartner2 = MBPartner.get(Env.getCtx(), processResult.C_BPartner_Id)

        assertEquals(bodyParams.first { it.first == "bpName" }.second, newPartner2.name)
        assertEquals(bodyParams.first { it.first == "discount" }.second.toString(), newPartner2.flatDiscount.toString())
        assertEquals(bodyParams.first { it.first == "description" }.second, newPartner2.description)
        assertEquals(bodyParams.first { it.first == "isCustomer" }.second, newPartner2.isCustomer)

        assertEquals(3, newPartner2.contacts.count())
        assertEquals(2, newPartner2.locations.count())
    }
}