package company.bigger.service

import org.compiere.crm.MCountry
import org.compiere.orm.Query
import org.idempiere.common.util.Env
import org.springframework.stereotype.Service

@Service
class CountryService {
    fun getAllCountries(): List<MCountry> {
        val query = Query(Env.getCtx(), "Country", "", null)
        val found = query.list<MCountry>()
        return found
    }
}