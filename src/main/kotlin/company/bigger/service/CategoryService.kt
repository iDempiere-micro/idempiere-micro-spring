package company.bigger.service

import org.compiere.crm.MCrmCategory
import org.compiere.orm.Query
import org.idempiere.common.util.Env
import org.springframework.stereotype.Service

@Service
class CategoryService {
    fun getAllCategories(): List<MCrmCategory> {
        val query = Query(Env.getCtx(), MCrmCategory.Table_Name, "", null)
        val found = query.list<MCrmCategory>()
        return found
    }
}