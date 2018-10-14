package org.compiere.orm

import org.compiere.model.I_AD_Table.COLUMNNAME_Name
import org.compiere.model.I_AD_User.COLUMNNAME_Value
import org.idempiere.common.util.KeyNamePair
import java.sql.ResultSet
import java.util.*

abstract class BasePOExt: BasePO {
    constructor(ctx: Properties, ID: Int, trxName: String?) : super(ctx, ID, trxName)
    constructor (ctx: Properties, rs: ResultSet, trxName: String?) : super(ctx, rs, trxName)
    constructor (ctx: Properties, rs: ResultSet, trxName: String?, a: String?) : super(ctx, rs, trxName, a)

    /** Set Name.
     * @param Name
     * Alphanumeric identifier of the entity
     */
    fun setName(Name: String) {
        set_Value(COLUMNNAME_Name, Name)
    }

    /** Get Name.
     * @return Alphanumeric identifier of the entity
     */
    fun getName(): String {
        return get_Value(COLUMNNAME_Name) as String
    }

    /** Get Record ID/ColumnName
     * @return ID/ColumnName pair
     */
    fun getKeyNamePair(): KeyNamePair {
        return KeyNamePair(_ID, getName())
    }

    /** Set Search Key.
     * @param Value
     * Search key for the record in the format required - must be unique
     */
    fun setValue(Value: String) {
        set_Value(COLUMNNAME_Value, Value)
    }

    /** Get Search Key.
     * @return Search key for the record in the format required - must be unique
     */
    fun getValue(): String {
        return get_Value(COLUMNNAME_Value) as String
    }
}