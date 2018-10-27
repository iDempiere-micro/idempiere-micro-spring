package org.compiere.accounting

import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Savepoint

import org.compiere.model.IDoc
import org.compiere.model.IDocFactory
import org.idempiere.common.base.IServicesHolder
import org.idempiere.common.base.Service
import org.idempiere.common.base.ServiceQuery
import org.idempiere.common.exceptions.AdempiereException
import org.idempiere.common.exceptions.DBException
import org.compiere.orm.MTable
import org.idempiere.common.util.AdempiereUserError
import org.idempiere.common.util.CLogger
import org.idempiere.common.util.DB
import org.idempiere.common.util.Env
import org.idempiere.common.util.Trx

/**
 * This class contains methods to manage the posting of financial document. Most of the code is adapted from the legacy code in Doc.java
 * @author Jorg Janke
 * @author hengsin
 */
object DocManager {

    private val s_log = CLogger.getCLogger(DocManager::class.java)

    /** AD_Table_ID's of documents           */
    private var documentsTableID: IntArray? = null

    /** Table Names of documents           */
    private var documentsTableName: Array<String>? = null

    /*
	 * Array of tables with Post column
	 */
    fun getDocumentsTableID(): IntArray? {
        fillDocumentsTableArrays()
        return documentsTableID
    }

    fun getDocumentsTableName(): Array<String>? {
        fillDocumentsTableArrays()
        return documentsTableName
    }

    @Synchronized
    private fun fillDocumentsTableArrays() {
        if (documentsTableID == null) {
            val sql = "SELECT t.AD_Table_ID, t.TableName " +
                    "FROM AD_Table t, AD_Column c " +
                    "WHERE t.AD_Table_ID=c.AD_Table_ID AND " +
                    "c.ColumnName='Posted' AND " +
                    "IsView='N' " +
                    "ORDER BY t.AD_Table_ID"
            val tableIDs: MutableList<Int> = mutableListOf()
            val tableNames: MutableList<String> = mutableListOf()
            var pstmt: PreparedStatement? = null
            var rs: ResultSet? = null
            try {
                pstmt = DB.prepareStatement(sql, null)
                rs = pstmt!!.executeQuery()
                while (rs!!.next()) {
                    tableIDs.add(rs.getInt(1))
                    tableNames.add(rs.getString(2))
                }
            } catch (e: SQLException) {
                throw DBException(e, sql)
            } finally {
                DB.close(rs, pstmt)
            }
            // 	Convert to array
            documentsTableID = tableIDs.toIntArray()
            documentsTableName = tableNames.toTypedArray()
        }
    }

    /**
     * Create Posting document
     * @param as accounting schema
     * @param AD_Table_ID Table ID of Documents
     * @param Record_ID record ID to load
     * @param trxName transaction name
     * @return Document or null
     */
    fun getDocument(`as`: MAcctSchema, AD_Table_ID: Int, Record_ID: Int, trxName: String): IDoc? {
        var TableName: String? = null
        for (i in 0 until DocManager.getDocumentsTableID()!!.size) {
            if (DocManager.getDocumentsTableID()!![i] == AD_Table_ID) {
                TableName = DocManager.getDocumentsTableName()!![i]
                break
            }
        }
        if (TableName == null) {
            s_log.severe("Not found AD_Table_ID=$AD_Table_ID")
            return null
        }

        val query = ServiceQuery()
        query["gaap"] = `as`.gaap
        var holder: IServicesHolder<IDocFactory>? = Service.locator()!!.list(IDocFactory::class.java, query)
        if (holder == null) {
            holder = DefaultDocumentFactoryHolder()
        }
        var factoryList: List<IDocFactory>? = holder.services
        if (factoryList == null || factoryList.isEmpty()) {
            factoryList = DefaultDocumentFactoryHolder().services
        }
        for (factory in factoryList) {
            val doc = factory.getDocument(`as`, AD_Table_ID, Record_ID, trxName)
            if (doc != null)
                return doc
        }

        query.clear()
        query["gaap"] = "*"
        holder = Service.locator()!!.list(IDocFactory::class.java, query)
        factoryList = holder.services
        if (factoryList.isEmpty()) {
            factoryList = DefaultDocumentFactoryHolder().services
        }
        for (factory in factoryList) {
            val doc = factory.getDocument(`as`, AD_Table_ID, Record_ID, trxName)
            if (doc != null)
                return doc
        }

        return null
    }

    /**
     * Create Posting document
     * @param as accounting schema
     * @param AD_Table_ID Table ID of Documents
     * @param rs ResultSet
     * @param trxName transaction name
     * @return Document
     * @throws AdempiereUserError
     */
    fun getDocument(`as`: MAcctSchema, AD_Table_ID: Int, rs: ResultSet, trxName: String): IDoc? {
        val query = ServiceQuery()
        query["gaap"] = `as`.gaap
        var holder: IServicesHolder<IDocFactory>? = Service.locator()!!.list(IDocFactory::class.java, query)
        if (holder == null) {
            holder = DefaultDocumentFactoryHolder()
        }
        var factoryList: List<IDocFactory>? = holder.services
        if (factoryList == null || factoryList.isEmpty()) {
            factoryList = DefaultDocumentFactoryHolder().services
        }
        for (factory in factoryList) {
            val doc = factory.getDocument(`as`, AD_Table_ID, rs, trxName)
            if (doc != null)
                return doc
        }

        query.clear()
        query["gaap"] = "*"
        holder = Service.locator()!!.list(IDocFactory::class.java, query)
        factoryList = holder.services
        if (factoryList.isEmpty()) {
            factoryList = DefaultDocumentFactoryHolder().services
        }
        for (factory in factoryList) {
            val doc = factory.getDocument(`as`, AD_Table_ID, rs, trxName)
            if (doc != null)
                return doc
        }

        return null
    }

    /**
     * Post Document
     * @param ass accounting schema
     * @param AD_Table_ID Transaction table
     * @param Record_ID Record ID of this document
     * @param force force posting
     * @param repost Repost document
     * @param trxName transaction
     * @return null if the document was posted or error message
     */
    fun postDocument(
        ass: Array<MAcctSchema>,
        AD_Table_ID: Int,
        Record_ID: Int,
        force: Boolean,
        repost: Boolean,
        trxName: String?
    ): String? {

        var tableName: String? = null
        for (i in 0 until getDocumentsTableID()!!.size) {
            if (getDocumentsTableID()!![i] == AD_Table_ID) {
                tableName = getDocumentsTableName()!![i]
                break
            }
        }
        if (tableName == null) {
            s_log.severe("Table not a financial document. AD_Table_ID=$AD_Table_ID")
            val msgreturn = StringBuilder("Table not a financial document. AD_Table_ID=").append(AD_Table_ID)
            return msgreturn.toString()
        }

        val sql = StringBuilder("SELECT * FROM ")
                .append(tableName)
                .append(" WHERE ").append(tableName).append("_ID=? AND Processed='Y'")
        var pstmt: PreparedStatement? = null
        var rs: ResultSet? = null
        try {
            pstmt = DB.prepareStatement(sql.toString(), trxName)
            pstmt!!.setInt(1, Record_ID)
            rs = pstmt.executeQuery()
            if (rs!!.next()) {
                return postDocument(ass, AD_Table_ID, rs, force, repost, trxName)
            } else {
                s_log.severe("Not Found: " + tableName + "_ID=" + Record_ID)
                return "NoDoc"
            }
        } catch (e: Exception) {
            if (e is RuntimeException)
                throw e
            else
                throw AdempiereException(e)
        } finally {
            DB.close(rs, pstmt)
        }
    }

    /**
     * Post Document
     * @param ass accounting schema
     * @param AD_Table_ID Transaction table
     * @param rs Result set
     * @param force force posting
     * @param repost Repost document
     * @param trxName transaction
     * @return null if the document was posted or error message
     */
    fun postDocument(
        ass: Array<MAcctSchema>,
        AD_Table_ID: Int,
        rs: ResultSet,
        force: Boolean,
        repost: Boolean,
        trxName: String?
    ): String? {
        var trxName1 = trxName
        var localTrxName: String? = null
        if (trxName1 == null) {
            localTrxName = Trx.createTrxName("Post")
            trxName1 = localTrxName
        }

        val trx = Trx.get(trxName1, true)
        if (localTrxName != null)
            trx!!.displayName = DocManager::class.java.name + "_postDocument"
        var error: String? = null
        var savepoint: Savepoint? = null
        try {
            savepoint = if (localTrxName == null) trx!!.setSavepoint(null) else null
            var status = ""
            for (`as` in ass) {
                val doc = Doc.get(`as`, AD_Table_ID, rs, trxName1)
                if (doc != null) {
                    error = doc.post(force, repost) // 	repost
                    status = doc.postStatus
                    if (error != null && error.trim { it <= ' ' }.length > 0) {
                        if (savepoint != null) {
                            trx!!.rollback(savepoint)
                            savepoint = null
                        } else
                            trx!!.rollback()
                        s_log.info("Error Posting $doc to $`as` Error: $error")
                        break
                    }
                } else {
                    if (savepoint != null) {
                        trx!!.rollback(savepoint)
                        savepoint = null
                    } else
                        trx!!.rollback()

                    s_log.info("Error Posting $doc to $`as` Error:  NoDoc")
                    return "NoDoc"
                }
            }

            val table = MTable.get(Env.getCtx(), AD_Table_ID)
            val Record_ID = rs.getInt(table.keyColumns[0])
            //  Commit Doc
            if (!save(trxName1, AD_Table_ID, Record_ID, status)) {
                val dbError = CLogger.retrieveError()
                // log.log(Level.SEVERE, "(doc not saved) ... rolling back");
                if (localTrxName != null) {
                    trx?.rollback()
                } else if (trx != null && savepoint != null) {
                    trx.rollback(savepoint)
                    savepoint = null
                }
                if (dbError != null)
                    error = dbError.value
                else
                    error = "SaveError"
            }
            if (savepoint != null) {
                try {
                    trx!!.releaseSavepoint(savepoint)
                } catch (e1: SQLException) {
                }

                savepoint = null
            }
            if (localTrxName != null && error == null) {
                trx?.commit()
            }
        } catch (e: Exception) {
            if (localTrxName != null) {
                trx?.rollback()
            } else if (trx != null && savepoint != null) {
                try {
                    trx.rollback(savepoint)
                } catch (e1: SQLException) {
                }
            }
            if (e is RuntimeException)
                throw e
            else
                throw AdempiereException(e)
        } finally {
            if (localTrxName != null) {
                trx?.close()
            }
        }
        return error
    }

    /**************************************************************************
     * Save to Disk - set posted flag
     * @param trxName transaction name
     * @return true if saved
     */
    private fun save(trxName: String?, AD_Table_ID: Int, Record_ID: Int, status: String): Boolean {
        val table = MTable.get(Env.getCtx(), AD_Table_ID)
        val sql = StringBuilder("UPDATE ")
        sql.append(table.tableName).append(" SET Posted='").append(status)
                .append("',Processing='N' ")
                .append("WHERE ")
                .append(table.tableName).append("_ID=").append(Record_ID)
        CLogger.resetLast()
        val no = DB.executeUpdate(sql.toString(), trxName)
        return no == 1
    } //  save
}