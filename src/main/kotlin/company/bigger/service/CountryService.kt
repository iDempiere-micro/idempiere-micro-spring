package company.bigger.service

import org.compiere.crm.MCountry
import org.compiere.model.I_C_Country
import org.compiere.orm.Query
import org.idempiere.common.util.Env
import org.springframework.stereotype.Service

@Service
class CountryService {
    fun getAllCountries(): List<MCountry> {
        val query = Query(Env.getCtx(), I_C_Country.Table_Name, "", null)
        val found = query.list<MCountry>()
        return found
    }
}