package org.adempiere.install

/**
 *
 * @author hengsin
 */
class DBConfigStatus
/**
 * @param statusType
 * @param resourseString
 * @param pass
 * @param critical
 * @param errorMessage
 */(
        /**
         * @return the statusType
         */
     /**
      * @param statusType the statusType to set
      */
     var statusType: String?,
        /**
         * @return the resourseString
         */
     /**
      * @param resourseString the resourseString to set
      */
     var resourseString: String?,
        /**
         * @return the pass
         */
     /**
      * @param pass the pass to set
      */
     var isPass: Boolean,
        /**
         * @return the critical
         */
     /**
      * @param critical the critical to set
      */
     var isCritical: Boolean,
        /**
         * @return the errorMessage
         */
     /**
      * @param errorMessage the errorMessage to set
      */
     var errorMessage: String?
 ) {
    companion object {

        val DATABASE_SERVER = "DATABASE_SERVER"
        val DATABASE_SYSTEM_PASSWORD = "DATABASE_SYSTEM_PASSWORD"
        val DATABASE_USER = "DATABASE_USER"
        val DATABASE_SQL_TEST = "DATABASE_SQL_TEST"
    }
}