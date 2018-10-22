package org.adempiere.exceptions

import org.idempiere.common.exceptions.AdempiereException

/**
 * Throw when there is no Warehouse for Organization.
 * @author victor.perez@e-evolution.com, www.e-evolution.com
 *  * FR [ 2457781 ] Introduce WarehouseInvalidForOrgException
 *  * https://sourceforge.net/tracker/index.php?func=detail&aid=2877521&group_id=176962&atid=879335
 */
class WarehouseInvalidForOrgException
/**
 * @param productName M_Product Name
 */
(wname: String, oname: String) : AdempiereException("$wname @M_Warehouse_ID@  @NotValid@  @AD_Org_ID@  $oname") {
    companion object {

        /**
         *
         */
        private val serialVersionUID = -8637554073291880392L
    }
}