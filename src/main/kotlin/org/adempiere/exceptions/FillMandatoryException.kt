package org.adempiere.exceptions

import org.idempiere.common.exceptions.AdempiereException

/**
 * Throwed when there are some fields that are mandatory but unfilled.
 * @author Teo Sarca, SC ARHIPAC SERVICE SRL
 */
class FillMandatoryException(vararg fields: String) : AdempiereException("@FillMandatory@ " + buildMessage(*fields)) {
    companion object {
        /**
         *
         */
        private val serialVersionUID = 9074980284529933724L

        private fun buildMessage(vararg fields: String): String {
            val sb = StringBuilder()
            for (f in fields) {
                if (sb.length > 0)
                    sb.append(", ")
                sb.append("@").append(f).append("@")
            }
            return sb.toString()
        }
    }
}