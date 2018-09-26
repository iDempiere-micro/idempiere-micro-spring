package company.bigger.dto

data class CreateCustomerModel(
        // legal name
        val bpName: String? = null, // name
        // description
        val description: String? = null, // description
        // friendly name
        val value: String? = null, // value
        // DUNS (IC)
        val duns: String? = null,
        // Tax ID (VAT ID)
        val taxid: String? = null,
        // place of business name
        val locationName: String? = null,
        // place of business address
        val locationAddress: String? = null, // address1
        // place of business city
        val locationCity: String? = null, // city
        // place of business postal
        val locationPostal: String? = null, // postal
        // place of business phone
        val locationPhone: String? = null, // phone
        // legal address
        val legalAddress: String? = null,
        // legal city
        val legalCity: String? = null,
        // legal postal
        val legalPostal: String? = null,
        // legal phone
        val legalPhone: String? = null,
        // order contact person
        val orderContactPerson: String? = null,
        // decision maker
        val decisionMaker: String? = null,
        // invoicing contact
        val invoicingContact: String? = null,
        // already a customer
        val isCustomer: Boolean? = null,
        // customer category
        val customerCategoryId: Int? = null,
        // flat discount
        val discount: Int? = null,
        // account manager
        val salesRepId: Int? = null,

        val locationCountryId: Int? = null,
        val legalCountryId: Int? = null
)