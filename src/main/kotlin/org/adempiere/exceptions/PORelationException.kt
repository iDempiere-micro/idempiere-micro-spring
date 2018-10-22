package org.adempiere.exceptions

import java.util.logging.Level

import org.idempiere.common.exceptions.AdempiereException
import org.idempiere.common.util.CLogger
import org.compiere.util.Msg
import org.idempiere.orm.PO

/**
 *
 * @author Tobias Schoeneberg, www.metas.de - FR [ 2897194 ] Advanced Zoom and
 * RelationTypes
 */
class PORelationException private constructor(
    msg: String,
    val adMsg: String,
    vararg msgParams: Any
) : AdempiereException(msg) {

    val msgParams: Array<out Any>

    init {
        this.msgParams = msgParams
    }

    companion object {

        private val logger = CLogger
                .getCLogger(PORelationException::class.java)

        /**
         * Message indicates that a po has more or less than one key columns.
         *
         *  * Param 1: the po (toString)
         *  * Param 2: the number of key columns
         *
         */
        val MSG_ERR_KEY_COLUMNS_2P = "MRelationType_Err_KeyColumns_2P"

        /**
         * Message indicates that neither the reference nor the table have an
         * AD_Window_ID set.
         *
         *  * Param 1: The AD_Reference's name
         *  * Param 2: The Table name
         *  * Param 3: Whether we are in the ctx of a SO (Y or N)
         *
         */
        val MSG_ERR_WINDOW_3P = "MRelationType_Err_Window_3P"

        /**
         *
         */
        private val serialVersionUID = -906400765022362887L

        fun throwWrongKeyColumnCount(po: PO) {

            if (logger.isLoggable(Level.FINE)) logger.fine("Invoked with po $po")

            val msgParams = arrayOf(po.toString(), po._KeyColumns.size)

            val msg = Msg.getMsg(po.ctx, MSG_ERR_KEY_COLUMNS_2P,
                    msgParams)

            val sb = StringBuilder(msg)

            for (keyCol in po._KeyColumns) {
                sb.append("\n")
                sb.append(keyCol)
            }

            throw PORelationException(sb.toString(), MSG_ERR_KEY_COLUMNS_2P,
                    *msgParams)
        }

        fun throwMissingWindowId(
            po: PO,
            referenceName: String,
            tableName: String,
            isSOTrx: Boolean
        ) {

            val msgParams = arrayOf<Any>(referenceName, tableName, if (isSOTrx) "Y" else "N")

            val msg = Msg
                    .getMsg(po.ctx, MSG_ERR_WINDOW_3P, msgParams)

            throw PORelationException(msg, MSG_ERR_WINDOW_3P, *msgParams)
        }
    }
}