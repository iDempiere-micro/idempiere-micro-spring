package org.adempiere.install

/**
 *
 * @author hengsin
 */
interface IDBConfigMonitor {

    /**
     *
     * @param status
     */
    fun update(status: DBConfigStatus)
}