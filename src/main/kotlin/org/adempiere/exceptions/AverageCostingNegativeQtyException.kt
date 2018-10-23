package org.adempiere.exceptions

import org.idempiere.common.exceptions.AdempiereException

/**
 *
 * @author hengsin
 */
class AverageCostingNegativeQtyException : AdempiereException {

    constructor() : super() {}

    constructor(message: String) : super(message) {}

    companion object {

        /**
         * generated serial version id
         */
        private val serialVersionUID = 4165497320719149773L
    }
}