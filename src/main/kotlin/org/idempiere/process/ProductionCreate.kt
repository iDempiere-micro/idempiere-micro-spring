package org.idempiere.process

import java.math.BigDecimal
import java.util.logging.Level

import org.compiere.model.I_M_ProductionPlan
import org.compiere.orm.MSysConfig
import org.compiere.orm.Query
import org.compiere.process.SvrProcess
import org.compiere.production.MProduction
import org.compiere.production.MProductionPlan
import org.idempiere.common.util.AdempiereUserError
import org.idempiere.common.util.DB
import org.idempiere.common.util.Env

/**
 *
 * Process to create production lines based on the plans
 * defined for a particular production header
 * @author Paul Bowden
 */
class ProductionCreate(
    var p_M_Production_ID: Int = 0,
    var m_production: MProduction? = null,
    val mustBeStocked: Boolean = false, // not used
    var recreate: Boolean = false,
    var newQty: BigDecimal? = null
) : SvrProcess() {
    override fun prepare() {

        val para = parameter
        for (i in para.indices) {
            val name = para[i].parameterName
            if ("Recreate" == name)
                recreate = "Y" == para[i].parameter
            else if ("ProductionQty" == name)
                newQty = para[i].parameter as BigDecimal
            else
                log.log(Level.SEVERE, "Unknown Parameter: $name")
        }

        if (p_M_Production_ID == 0) p_M_Production_ID = record_ID
        if (m_production == null) m_production = MProduction(ctx, p_M_Production_ID, _TrxName)
    } // prepare

    @Throws(Exception::class)
    override fun doIt(): String {

        if (m_production!!.id == 0)
            throw AdempiereUserError("Could not load production header")

        return if (m_production!!.isProcessed) "Already processed" else createLines()
    }

    @Throws(AdempiereUserError::class)
    private fun costsOK(M_Product_ID: Int): Boolean {
        // Warning will not work if non-standard costing is used
        val sql = ("SELECT ABS(((cc.currentcostprice-(SELECT SUM(c.currentcostprice*bom.bomqty)" +
                " FROM m_cost c" +
                " INNER JOIN m_product_bom bom ON (c.m_product_id=bom.m_productbom_id)" +
                " INNER JOIN m_costelement ce ON (c.m_costelement_id = ce.m_costelement_id AND ce.costingmethod = 'S')" +
                " WHERE bom.m_product_id = pp.m_product_id)" +
                " )/cc.currentcostprice))" +
                " FROM m_product pp" +
                " INNER JOIN m_cost cc on (cc.m_product_id=pp.m_product_id)" +
                " INNER JOIN m_costelement ce ON (cc.m_costelement_id=ce.m_costelement_id)" +
                " WHERE cc.currentcostprice > 0 AND pp.M_Product_ID = ?" +
                " AND ce.costingmethod='S'")

        var costPercentageDiff: BigDecimal? = DB.getSQLValueBD(_TrxName, sql, M_Product_ID)

        if (costPercentageDiff == null) {
            costPercentageDiff = Env.ZERO
            val msg = "Could not retrieve costs"
            if (MSysConfig.getBooleanValue(MSysConfig.MFG_ValidateCostsOnCreate, false, adClientID)) {
                throw AdempiereUserError(msg)
            } else {
                log.warning(msg)
            }
        }

        return if (costPercentageDiff!!.compareTo(BigDecimal("0.005")) < 0) true else false
    }

    @Throws(Exception::class)
    protected fun createLines(): String {

        var created = 0
        if (!m_production!!.isUseProductionPlan) {
            validateEndProduct(m_production!!.m_Product_ID)

            if (!recreate && "Y".equals(m_production!!.isCreated, ignoreCase = true))
                throw AdempiereUserError("Production already created.")

            if (newQty != null)
                m_production!!.productionQty = newQty

            m_production!!.deleteLines(_TrxName)
            created = m_production!!.createLines(mustBeStocked)
        } else {
            val planQuery = Query(ctx, I_M_ProductionPlan.Table_Name, "M_ProductionPlan.M_Production_ID=?", _TrxName)
            val plans = planQuery.setParameters(m_production!!.m_Production_ID).list<MProductionPlan>()
            for (plan in plans) {
                validateEndProduct(plan.m_Product_ID)

                if (!recreate && "Y".equals(m_production!!.isCreated, ignoreCase = true))
                    throw AdempiereUserError("Production already created.")

                plan.deleteLines(_TrxName)
                val n = plan.createLines(mustBeStocked)
                if (n == 0) {
                    return "Failed to create production lines"
                }
                created = created + n
            }
        }
        if (created == 0) {
            return "Failed to create production lines"
        }

        m_production!!.isCreated = "Y"
        m_production!!.save(_TrxName)
        val msgreturn = StringBuilder().append(created).append(" production lines were created")
        return msgreturn.toString()
    }

    @Throws(Exception::class)
    private fun validateEndProduct(M_Product_ID: Int) {
        isBom(M_Product_ID)

        if (!costsOK(M_Product_ID)) {
            val msg = "Excessive difference in standard costs"
            if (MSysConfig.getBooleanValue(MSysConfig.MFG_ValidateCostsDifferenceOnCreate, false, adClientID)) {
                throw AdempiereUserError("Excessive difference in standard costs")
            } else {
                log.warning(msg)
            }
        }
    }

    @Throws(Exception::class)
    protected fun isBom(M_Product_ID: Int) {
        val bom = DB.getSQLValueString(_TrxName, "SELECT isbom FROM M_Product WHERE M_Product_ID = ?", M_Product_ID)
        if ("N".compareTo(bom) == 0) {
            throw AdempiereUserError("Attempt to create product line for Non Bill Of Materials")
        }
        val materials = DB.getSQLValue(_TrxName, "SELECT count(M_Product_BOM_ID) FROM M_Product_BOM WHERE M_Product_ID = ?", M_Product_ID)
        if (materials == 0) {
            throw AdempiereUserError("Attempt to create product line for Bill Of Materials with no BOM Products")
        }
    }
}
