package pg.org.compiere.dbPort

import org.idempiere.common.dbPort.Convert_SQL92
import org.idempiere.common.util.CLogger
import org.idempiere.common.util.Util
import pg.org.compiere.db.DB_PostgreSQL

import java.math.BigDecimal
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Convert Oracle SQL to PostgreSQL SQL
 *
 * @author Victor Perez, Low Heng Sin, Carlos Ruiz
 * @author Teo Sarca, SC ARHIPAC SERVICE SRL
 *  * BF [ 1824256 ] Convert sql casts
 */
class Convert_PostgreSQL : Convert_SQL92() {

    private val m_map: TreeMap<String, String>

    /**
     * Cosntructor
     */
    init {
        m_map = ConvertMap_PostgreSQL.convertMap
    } // Convert


    /**
     * Is Oracle DB
     *
     * @return true if connection is Oracle DB
     */
    override fun isOracle(): Boolean {
        return false
    } // isOracle

    override fun getConvertMap(): Map<String, String>? {
        return m_map
    }

    /**
     * Convert single Statements. - remove comments - process
     * FUNCTION/TRIGGER/PROCEDURE - process Statement
     *
     * @param sqlStatement
     * @return converted statement
     */
    override fun convertStatement(sqlStatement: String): ArrayList<String> {
        val result = ArrayList<String>()
        /** Vector to save previous values of quoted strings  */
        val retVars = Vector<String>()

        var statement = replaceQuotedStrings(sqlStatement, retVars)
        statement = convertWithConvertMap(statement)
        statement = statement.replace(DB_PostgreSQL.NATIVE_MARKER, "")

        val cmpString = statement.toUpperCase()
        val isCreate = cmpString.startsWith("CREATE ")

        // Process
        if (isCreate && cmpString.indexOf(" FUNCTION ") != -1)
        else if (isCreate && cmpString.indexOf(" TRIGGER ") != -1)
        else if (isCreate && cmpString.indexOf(" PROCEDURE ") != -1)
        else if (isCreate && cmpString.indexOf(" VIEW ") != -1)
        else if (cmpString.indexOf("ALTER TABLE") != -1) {
            statement = recoverQuotedStrings(statement, retVars)
            retVars.clear()
            statement = convertDDL(convertComplexStatement(statement))
            /*
		} else if (cmpString.indexOf("ROWNUM") != -1) {
			result.add(convertRowNum(convertComplexStatement(convertAlias(statement))));*/
        } else if (cmpString.indexOf("DELETE ") != -1 && cmpString.indexOf("DELETE FROM") == -1) {
            statement = convertDelete(statement)
            statement = convertComplexStatement(convertAlias(statement))
        } else if (cmpString.indexOf("DELETE FROM") != -1) {
            statement = convertComplexStatement(convertAlias(statement))
        } else if (cmpString.indexOf("UPDATE ") != -1) {
            statement = convertComplexStatement(convertUpdate(convertAlias(statement)))
        } else {
            statement = convertComplexStatement(convertAlias(statement))
        }
        if (retVars.size > 0)
            statement = recoverQuotedStrings(statement, retVars)
        result.add(statement)

        if ("true" == System.getProperty("org.idempiere.common.db.postgresql.debug")) {
            log.warning("Oracle -> $sqlStatement")
            log.warning("PgSQL  -> $statement")
        }
        return result
    } // convertStatement

    override fun escapeQuotedString(`in`: String): String {
        val out = StringBuilder()
        var escape = false
        val size = `in`.length
        for (i in 0 until size) {
            val c = `in`[i]
            out.append(c)
            if (c == '\\') {
                escape = true
                out.append(c)
            }
        }
        return if (escape) {
            "E" + out.toString()
        } else {
            out.toString()
        }
    }

    /***************************************************************************
     * Converts Decode and Outer Join.
     *
     * <pre>
     * DECODE (a, 1, 'one', 2, 'two', 'none')
     * =&gt; CASE WHEN a = 1 THEN 'one' WHEN a = 2 THEN 'two' ELSE 'none' END
     *
    </pre> *
     *
     * @param sqlStatement
     * @return converted statement
     */
    protected fun convertComplexStatement(sqlStatement: String): String {
        var retValue = sqlStatement

        // Convert all decode parts
        var found = retValue.toUpperCase().indexOf("DECODE")
        var fromIndex = 0
        while (found != -1) {
            retValue = convertDecode(retValue, fromIndex)
            fromIndex = found + 6
            found = retValue.toUpperCase().indexOf("DECODE", fromIndex)
        }

        // Outer Join Handling -----------------------------------------------
        val index = retValue.toUpperCase().indexOf("SELECT ")
        if (index != -1 && retValue.indexOf("(+)", index) != -1)
            retValue = convertOuterJoin(retValue)

        // Convert datatypes from CAST(.. as datatypes):
        retValue = convertCast(retValue)

        return retValue
    } // convertComplexStatement

    /**
     * Convert datatypes from CAST sentences
     * <pre>
     * cast(NULL as NVARCHAR2(255))
     * =&gt;cast(NULL as VARCHAR)
    </pre> *
     */
    private fun convertCast(sqlStatement: String): String {
        val PATTERN_String = "\'([^']|(''))*\'"
        val PATTERN_DataType = "([\\w]+)(\\(\\d+\\))?"
        val pattern = ("\\bCAST\\b[\\s]*\\([\\s]*"                    // CAST<sp>(<sp>

                + "((" + PATTERN_String + ")|([^\\s]+))"        //	arg1				1(2,3)

                + "[\\s]*AS[\\s]*"                        //	<sp>AS<sp>

                + "(" + PATTERN_DataType + ")"                //	arg2 (datatype)		4

                + "\\s*\\)")                                //	<sp>)
        val gidx_arg1 = 1
        val gidx_arg2 = 7    // datatype w/o length
        val p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE)
        val m = p.matcher(sqlStatement)

        val convertMap = convertMap as TreeMap<String, String>?
        val retValue = StringBuffer(sqlStatement.length)
        while (m.find()) {
            val arg1 = m.group(gidx_arg1)
            val arg2 = m.group(gidx_arg2)
            //
            var datatype: String? = convertMap!!["\\b" + arg2.toUpperCase() + "\\b"]
            if (datatype == null)
                datatype = arg2
            m.appendReplacement(retValue, "cast($arg1 as $datatype)")
        }
        m.appendTail(retValue)
        return retValue.toString()
    }

    /**
     * Convert RowNum.
     *
     * <pre>
     * SELECT Col1 FROM tableA WHERE ROWNUM=1
     * =&gt; SELECT Col1 FROM tableA LIMIT 1
     * Assumptions/Limitations:
     * - RowNum not used in SELECT part
    </pre> *
     *
     * @param sqlStatement
     * @return converted statement
     */
    /*
	private String convertRowNum(String sqlStatement) {
		// log.info("RowNum<== " + sqlStatement);

        sqlStatement = Pattern.compile("rownum",REGEX_FLAGS).matcher(sqlStatement).replaceAll("ROWNUM");

		String retValue = null;

		// find into (select from where)

		int s_end = 0;
		int s_start = -1;
		String select = sqlStatement;
		String convert = "";
		while (true) {
			s_end = 0;
			s_start = select.indexOf("(SELECT");

			if (s_start == -1)
				break;

			convert = convert + select.substring(0, s_start);
			// System.out.println("convert:" + convert);
			int open = -1;
			for (int i = s_start; i < select.length(); i++) {
				char c = select.charAt(i);
				if (c == '(')
					open++;

				if (c == ')')
					open--;

				if (open == -1) {
					s_end = i + 1;
					break;
				}
			}

			String subselect = select.substring(s_start, s_end);
			// System.out.println("subselect:" +subselect);
			// System.out.println("select:" +select);

			if (subselect.indexOf("AND ROWNUM=1") > 1) {
				subselect = subselect.substring(0, subselect.length() - 1)
						+ " LIMIT 1 )";
				// System.out.println("subselect:" +subselect);
				convert = convert + Util.replace(subselect, "AND ROWNUM=1", "");
				// System.out.println("convert:" + convert);
			} else if (subselect.indexOf(" WHERE ROWNUM=1 AND") > 1) {
				subselect = subselect.substring(0, subselect.length() - 1)
						+ " LIMIT 1 )";
				// System.out.println("subselect:" +subselect);
				convert = convert
						+ Util.replace(subselect, " WHERE ROWNUM=1 AND",
								" WHERE ");
				// System.out.println("convert:" + convert);
			} else {
				convert = convert + subselect;
			}

			select = select.substring(s_end);
			retValue = select;

		}
		// System.out.println("convert:" + convert);
		// System.out.println("select:" + select);
		if (retValue == null)
			retValue = sqlStatement;

		if (retValue.indexOf("AND ROWNUM=1") > 1) {
			int rownum = retValue.indexOf("AND ROWNUM=1");
			if (retValue.substring(0, rownum).contains("WHERE")) {
				retValue = Util.replace(retValue, "AND ROWNUM=1", " LIMIT 1");
				return convert + retValue;
			} else {
				retValue = Util.replace(retValue, "AND ROWNUM=1", "");
				return convert + retValue + " LIMIT 1";
			}

		} else if (retValue.indexOf("AND ROWNUM= 1") > 1) {
			int rownum = retValue.indexOf("AND ROWNUM= 1");
			if (retValue.substring(0, rownum).contains("WHERE")) {

				retValue = Util.replace(retValue, "AND ROWNUM= 1", " LIMIT 1");
				return convert + retValue;
			} else {
				retValue = Util.replace(retValue, "AND ROWNUM= 1", "");
				return convert + retValue + " LIMIT 1";
			}
		} else if (retValue.indexOf("AND ROWNUM = 1") > 1) {
			int rownum = retValue.indexOf("AND ROWNUM = 1");
			if (retValue.substring(0, rownum).contains("WHERE")) {

				retValue = Util.replace(sqlStatement, "AND ROWNUM = 1",
						" LIMIT 1");
				return convert + retValue;
			} else {
				retValue = Util.replace(sqlStatement, "AND ROWNUM = 1", "");
				return convert + retValue + " LIMIT 1";
			}
		} else if (retValue.indexOf("AND ROWNUM =1") > 1) {
			int rownum = retValue.indexOf("AND ROWNUM =1");
			if (retValue.substring(0, rownum).contains("WHERE")) {

				retValue = Util.replace(retValue, "AND ROWNUM =1", " LIMIT 1");
				return convert + retValue;
			} else {
				retValue = Util.replace(retValue, "AND ROWNUM =1", "");
				return convert + retValue + " LIMIT 1";
			}
		} else if (retValue.indexOf("ROWNUM=1") > 1) {
			int rownum = retValue.indexOf("ROWNUM=1");
			//System.out.println("retValue" + retValue);
			if (retValue.substring(0, rownum).contains("WHERE")) {
				retValue = Util.replace(retValue, "ROWNUM=1 ", " LIMIT 1");
				return convert + retValue;
			} else {
				retValue = Util.replace(retValue, "ROWNUM=1", "");
				return convert + retValue + " LIMIT 1";
			}
		}
		// log.info("RowNum==> " + retValue);
		return convert + retValue;

		//
		// log.info("RowNum==> " + retValue);
		// return retValue;
		// end e-evolution PostgreSQL
	} // convertRowNum
	*/

    /***************************************************************************
     * Converts Update.
     *
     * <pre>
     * UPDATE C_Order i SET
     * =&gt; UPDATE C_Order SET
    </pre> *
     *
     * @param sqlStatement
     * @return converted statement
     */

    private fun convertUpdate(sqlStatement: String): String {
        var targetTable: String? = null
        var targetAlias: String? = null

        val sqlUpper = sqlStatement.toUpperCase()
        var token = StringBuilder()
        var previousToken: String? = null
        var charIndex = 0
        val sqlLength = sqlUpper.length
        var cnt = 0
        var isUpdate = false

        //get target table and alias
        while (charIndex < sqlLength) {
            val c = sqlStatement[charIndex]
            if (Character.isWhitespace(c)) {
                if (token.length > 0) {
                    cnt++
                    if (cnt == 1)
                        isUpdate = "UPDATE".equals(token.toString(), ignoreCase = true)
                    else if (cnt == 2)
                        targetTable = token.toString()
                    else if (cnt == 3) {
                        targetAlias = token.toString().trim { it <= ' ' }
                        if ("SET".equals(targetAlias, ignoreCase = true))
                        //no alias
                            targetAlias = targetTable
                    }
                    previousToken = token.toString()
                    token = StringBuilder()
                }
            } else {
                if ("SET".equals(previousToken!!, ignoreCase = true))
                    break
                else
                    token.append(c)
            }
            charIndex++
        }

        if (isUpdate && targetTable != null && sqlUpper[charIndex] == '(') {
            val updateFieldsBegin = charIndex
            var updateFields: String? = null

            var select = ""

            //get the sub query
            var beforePreviousToken: String? = null
            previousToken = null
            token = StringBuilder()
            while (charIndex < sqlLength) {
                val c = sqlUpper[charIndex]
                if (Character.isWhitespace(c)) {
                    if (token.length > 0) {
                        val currentToken = token.toString()
                        if ("(" == currentToken || currentToken != null && currentToken.startsWith("(")) {
                            if ((")" == beforePreviousToken || beforePreviousToken != null && beforePreviousToken.endsWith(")")) && "=" == previousToken) {
                                select = sqlStatement.substring(charIndex - currentToken.length)
                                updateFields = sqlStatement.substring(updateFieldsBegin, charIndex)
                                updateFields = updateFields.substring(0, updateFields.lastIndexOf(')'))
                                break
                            } else if (")=" == previousToken) {
                                select = sqlStatement.substring(charIndex - currentToken.length)
                                updateFields = sqlStatement.substring(updateFieldsBegin, charIndex)
                                updateFields = updateFields.substring(0, updateFields.lastIndexOf(')'))
                                break
                            } else if (previousToken != null && previousToken.endsWith(")=")) {
                                select = sqlStatement.substring(charIndex - currentToken.length)
                                updateFields = sqlStatement.substring(updateFieldsBegin, charIndex)
                                updateFields = updateFields.substring(0, updateFields.lastIndexOf(')'))
                                break
                            }

                        }
                        if (")=(" == currentToken) {
                            select = sqlStatement.substring(charIndex - 1)
                            updateFields = sqlStatement.substring(updateFieldsBegin, charIndex)
                            updateFields = updateFields.substring(0, updateFields.lastIndexOf(')'))
                            break
                        } else if (currentToken.endsWith(")=(SELECT")) {
                            select = sqlStatement.substring(charIndex - 7)
                            updateFields = sqlStatement.substring(updateFieldsBegin, charIndex)
                            updateFields = updateFields.substring(0, updateFields.lastIndexOf(')'))
                            break
                        } else if ("=(" == currentToken || currentToken != null && currentToken.startsWith("=(")) {
                            if (")" == previousToken || previousToken != null && previousToken.endsWith(")")) {
                                select = sqlStatement.substring(charIndex - currentToken.length)
                                updateFields = sqlStatement.substring(updateFieldsBegin, charIndex)
                                updateFields = updateFields.substring(0, updateFields.lastIndexOf(')'))
                                break
                            }
                        }
                        beforePreviousToken = previousToken
                        previousToken = token.toString()
                        token = StringBuilder()
                    }
                } else {
                    token.append(c)
                }
                charIndex++
            }
            if (updateFields != null && updateFields.startsWith("("))
                updateFields = updateFields.substring(1)

            var subQueryEnd = 0
            val subQueryStart = select.indexOf('(')
            var subWhere: String? = null
            var open = -1
            for (i in subQueryStart until select.length) {
                val c = select[i]
                if (c == '(')
                    open++

                if (c == ')')
                    open--

                if (open == -1) {
                    subQueryEnd = i + 1
                    break
                }
            }

            var mainWhere: String? = ""
            var otherUpdateFields = ""
            //get update where clause
            token = StringBuilder()
            for (i in subQueryEnd until select.length) {
                val c = select[i]
                if (Character.isWhitespace(c)) {
                    if (token.length > 0) {
                        if ("WHERE".equals(token.toString(), ignoreCase = true)) {
                            otherUpdateFields = select.substring(subQueryEnd, i - 5).trim { it <= ' ' }
                            mainWhere = select.substring(i + 1)
                            break
                        }
                        token = StringBuilder()
                    }
                } else {
                    token.append(c)
                }
            }

            val subQuery = select.substring(subQueryStart, subQueryEnd)

            //get join table and alias
            var joinTable: String? = null
            var joinAlias: String? = null
            token = StringBuilder()
            previousToken = null
            var joinFieldsBegin = 0
            var joinFields: String? = null
            var joinFromClause: String? = null
            var joinFromClauseStart = 0
            open = -1
            for (i in 0 until subQuery.length) {
                val c = subQuery[i]
                if (Character.isWhitespace(c)) {
                    if (token.length > 0 && open < 0) {
                        if ("FROM".equals(previousToken!!, ignoreCase = true)) {
                            joinTable = token.toString()
                        }
                        if ("WHERE".equals(token.toString(), ignoreCase = true)) {
                            subWhere = subQuery.substring(i + 1, subQuery.length - 1)
                            joinFromClause = subQuery.substring(joinFromClauseStart, i - 5).trim { it <= ' ' }
                            break
                        }
                        if ("FROM".equals(token.toString(), ignoreCase = true)) {
                            joinFields = subQuery.substring(joinFieldsBegin, i - 4)
                            joinFromClauseStart = i
                        }
                        if (previousToken != null && previousToken == joinTable) {
                            joinAlias = token.toString()
                        }
                        previousToken = token.toString()
                        token = StringBuilder()
                    }
                } else {
                    if (joinFieldsBegin == 0) {
                        if (token.length == 0 && ("SELECT".equals(previousToken!!, ignoreCase = true) || previousToken != null && previousToken.toUpperCase().endsWith("SELECT")))
                            joinFieldsBegin = i
                    } else if (c == '(')
                        open++
                    else if (c == ')')
                        open--
                    token.append(c)
                }
            }
            if (joinFromClause == null) joinFromClause = subQuery.substring(joinFromClauseStart).trim { it <= ' ' }
            if (joinAlias == null) joinAlias = joinTable

            //construct update clause
            val Update = StringBuilder("UPDATE ")
            Update.append(targetTable)
            if (targetAlias != targetTable)
                Update.append(" " + targetAlias!!)

            Update.append(" SET ")

            var f = updateFields?.length ?: 0
            var fj = joinFields!!.length
            var updateField: String? = null
            var joinField: String? = null

            var useSubQuery = false
            if (useAggregateFunction(joinFields))
                useSubQuery = true

            while (f > 0) {
                f = Util.findIndexOf(updateFields, ',')
                if (f < 0) {
                    updateField = updateFields
                    joinField = joinFields!!.trim { it <= ' ' }
                    if (joinField.indexOf('.') < 0 && isIdentifier(joinField)) {
                        joinField = "$joinAlias.$joinField"
                    }

                    Update.append(updateField!!.trim { it <= ' ' })
                    Update.append("=")
                    if (useSubQuery) {
                        Update.append("( SELECT ")
                        Update.append(joinField)
                        Update.append(" FROM ")
                        Update.append(joinFromClause)
                        Update.append(" WHERE ")
                        Update.append(subWhere!!.trim { it <= ' ' })
                        Update.append(" ) ")
                        Update.append(otherUpdateFields)
                        if (mainWhere != null) {
                            Update.append(" WHERE ")
                            Update.append(mainWhere)
                        }
                    } else {
                        Update.append(joinField)
                        Update.append(otherUpdateFields)
                        Update.append(" FROM ")
                        Update.append(joinFromClause)
                        Update.append(" WHERE ")
                        subWhere = addAliasToIdentifier(subWhere, joinAlias)
                        Update.append(subWhere.trim { it <= ' ' })

                        if (mainWhere != null)
                            mainWhere = " AND $mainWhere"
                        else
                            mainWhere = ""

                        mainWhere = addAliasToIdentifier(mainWhere, targetAlias)
                        Update.append(mainWhere)
                    }
                } else {

                    updateField = updateFields!!.substring(0, f)
                    fj = Util.findIndexOf(joinFields, ',')
                    // fieldsjoin.indexOf(',');

                    joinField = if (fj > 0) joinFields!!.substring(0, fj).trim { it <= ' ' } else joinFields!!.trim { it <= ' ' }
                    if (joinField.indexOf('.') < 0 && isIdentifier(joinField)) {
                        joinField = "$joinAlias.$joinField"
                    }
                    Update.append(updateField.trim { it <= ' ' })
                    Update.append("=")
                    if (useSubQuery) {
                        Update.append("( SELECT ")
                        Update.append(joinField)
                        Update.append(" FROM ")
                        Update.append(joinFromClause)
                        Update.append(" WHERE ")
                        Update.append(subWhere!!.trim { it <= ' ' })
                        Update.append(" ) ")
                    } else {
                        Update.append(joinField)
                    }
                    Update.append(",")
                    joinFields = joinFields.substring(fj + 1)
                }

                updateFields = updateFields!!.substring(f + 1)

                // System.out.println("Update" + Update);
            }

            return Update.toString()

        }
        // System.out.println("Convert Update:"+sqlUpdate);
        return sqlStatement

    } // convertDecode

    /**
     * Check if one of the field is using standard sql aggregate function
     * @param fields
     * @return boolean
     */
    private fun useAggregateFunction(fields: String): Boolean {
        val fieldsUpper = fields.toUpperCase()
        val size = fieldsUpper.length
        var buffer = StringBuilder()
        var token: String? = null
        for (i in 0 until size) {
            val ch = fieldsUpper[i]
            if (Character.isWhitespace(ch)) {
                if (buffer.length > 0) {
                    token = buffer.toString()
                    buffer = StringBuilder()
                }
            } else {
                if (isOperator(ch)) {
                    if (buffer.length > 0) {
                        token = buffer.toString()
                        buffer = StringBuilder()
                    } else {
                        token = null
                    }
                    if (ch == '(' && token != null) {
                        if (token == "SUM" || token == "MAX" || token == "MIN"
                                || token == "COUNT" || token == "AVG") {
                            return true
                        }
                    }
                } else
                    buffer.append(ch)
            }
        }

        return false
    }

    /**
     * Add table alias to identifier in where clause
     * @param where
     * @param alias
     * @return converted where clause
     */
    private fun addAliasToIdentifier(where: String?, alias: String?): String {
        val sqlkey = "AND,OR,FROM,WHERE,JOIN,BY,GROUP,IN,INTO,SELECT,NOT,SET,UPDATE,DELETE,HAVING,IS,NULL,EXISTS,BETWEEN,LIKE,INNER,OUTER"

        val st = StringTokenizer(where!!)
        var result = ""
        var token = ""
        var o = -1
        while (true) {
            token = st.nextToken()
            val test = if (token.startsWith("(")) token.substring(1) else token
            if (sqlkey.indexOf(test) == -1) {

                token = token.trim { it <= ' ' }
                //skip subquery, non identifier and fully qualified identifier
                if (o != -1)
                    result = "$result $token"
                else {
                    result = "$result "
                    var t = StringBuilder()
                    for (i in 0 until token.length) {
                        val c = token[i]
                        if (isOperator(c)) {
                            if (t.length > 0) {
                                if (c == '(')
                                    result = result + t.toString()
                                else if (isIdentifier(t.toString()) && t.toString().indexOf('.') == -1)
                                    result = result + alias + "." + t.toString()
                                else
                                    result = result + t.toString()
                                t = StringBuilder()
                            }
                            result = result + c
                        } else {
                            t.append(c)
                        }
                    }
                    if (t.length > 0) {
                        if ("SELECT".equals(t.toString().toUpperCase(), ignoreCase = true)) {
                            o = 0
                            result = result + t.toString()
                        } else if (isIdentifier(t.toString()) && t.toString().indexOf('.') == -1)
                            result = result + alias + "." + t.toString()
                        else
                            result = result + t.toString()
                    }
                }

                if (o != -1) {
                    for (i in 0 until token.length) {
                        val c = token[i]
                        if (c == '(')
                            o++
                        if (c == ')')
                            o--
                    }
                }

            } else {
                result = "$result $token"
                if ("SELECT".equals(test, ignoreCase = true)) {
                    o = 0
                }
            }
            if (!st.hasMoreElements())
                break
        }
        return result
    }

    /**
     * Check if token is a valid sql identifier
     * @param token
     * @return True if token is a valid sql identifier, false otherwise
     */
    private fun isIdentifier(token: String): Boolean {
        val size = token.length
        for (i in 0 until size) {
            val c = token[i]
            if (isOperator(c))
                return false
        }
        if (token.startsWith("'") && token.endsWith("'"))
            return false
        else {
            try {
                BigDecimal(token)
                return false
            } catch (e: NumberFormatException) {
            }

        }

        return if (isSQLFunctions(token)) false else true

    }

    private fun isSQLFunctions(token: String): Boolean {
        if (token.equals("current_timestamp", ignoreCase = true))
            return true
        else if (token.equals("current_time", ignoreCase = true))
            return true
        else if (token.equals("current_date", ignoreCase = true))
            return true
        else if (token.equals("localtime", ignoreCase = true))
            return true
        else if (token.equals("localtimestamp", ignoreCase = true))
            return true
        return false
    }

    // begin vpj-cd e-evolution 08/02/2005
    /***************************************************************************
     * convertAlias - for compatibility with 8.1
     *
     * @param sqlStatement
     * @return converted statementf
     */
    private fun convertAlias(sqlStatement: String): String {
        val tokens = sqlStatement.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var table: String? = null
        var alias: String? = null
        if ("UPDATE".equals(tokens[0], ignoreCase = true)) {
            if ("SET".equals(tokens[2], ignoreCase = true)) return sqlStatement
            table = tokens[1]
            alias = tokens[2]
        } else if ("INSERT".equals(tokens[0], ignoreCase = true)) {
            if ("VALUES".equals(tokens[3], ignoreCase = true) || "SELECT".equals(tokens[3], ignoreCase = true))
                return sqlStatement
            if (tokens[2].indexOf('(') > 0)
                return sqlStatement
            else if (tokens[3].indexOf('(') < 0 || tokens[3].indexOf('(') > 0) {
                table = tokens[2]
                alias = tokens[3]
            } else {
                return sqlStatement
            }
        } else if ("DELETE".equals(tokens[0], ignoreCase = true)) {
            if (tokens.size < 4) return sqlStatement
            if ("WHERE".equals(tokens[3], ignoreCase = true)) return sqlStatement
            table = tokens[2]
            alias = tokens[3]
        }
        if (table != null && alias != null) {
            if (alias.indexOf('(') > 0) alias = alias.substring(0, alias.indexOf('('))
            var converted = sqlStatement.replaceFirst("\\s$alias\\s".toRegex(), " ")
            converted = converted.replace("\\b$alias\\.".toRegex(), "$table.")
            converted = converted.replace("[+]$alias\\.".toRegex(), "+$table.")
            converted = converted.replace("[-]$alias\\.".toRegex(), "-$table.")
            converted = converted.replace("[*]$alias\\.".toRegex(), "*$table.")
            converted = converted.replace("[/]$alias\\.".toRegex(), "/$table.")
            converted = converted.replace("[%]$alias\\.".toRegex(), "%$table.")
            converted = converted.replace("[<]$alias\\.".toRegex(), "<$table.")
            converted = converted.replace("[>]$alias\\.".toRegex(), ">$table.")
            converted = converted.replace("[=]$alias\\.".toRegex(), "=$table.")
            converted = converted.replace("[|]$alias\\.".toRegex(), "|$table.")
            converted = converted.replace("[(]$alias\\.".toRegex(), "($table.")
            converted = converted.replace("[)]$alias\\.".toRegex(), ")$table.")
            return converted
        } else {
            return sqlStatement
        }
    } //
    // end vpj-cd e-evolution 02/24/2005 PostgreSQL

    // begin vpj-cd 08/02/2005
    // ALTER TABLE AD_FieldGroup MODIFY IsTab CHAR(1) DEFAULT N;
    // ALTER TABLE AD_FieldGroup ALTER COLUMN IsTab TYPE CHAR(1); ALTER TABLE
    // AD_FieldGroup ALTER COLUMN SET DEFAULT 'N';
    private fun convertDDL(sqlStatement: String): String {
        if (sqlStatement.toUpperCase().indexOf("ALTER TABLE ") == 0) {
            var action: String? = null
            var begin_col = -1
            if (sqlStatement.toUpperCase().indexOf(" MODIFY ") > 0) {
                action = " MODIFY "
                begin_col = sqlStatement.toUpperCase().indexOf(" MODIFY ") + action.length
            } else if (sqlStatement.toUpperCase().indexOf(" ADD ") > 0) {
                if (sqlStatement.toUpperCase().indexOf(" ADD CONSTRAINT ") < 0 && sqlStatement.toUpperCase().indexOf(" ADD FOREIGN KEY ") < 0) {
                    action = " ADD "
                    begin_col = sqlStatement.toUpperCase().indexOf(" ADD ") + action.length
                }
            }

            // System.out.println( "MODIFY :" +
            // sqlStatement.toUpperCase().indexOf(" MODIFY "));
            // System.out.println( "ADD :" +
            // sqlStatement.toUpperCase().indexOf(" ADD "));
            // System.out.println( "begincolumn:" + sqlStatement +
            // "begincolumn:" + begin_col );

            if (begin_col < 0)
                return sqlStatement

            var end_col = 0
            var begin_default = -1

            var column: String? = null
            var type: String? = null
            var defaultvalue: String? = null
            var nullclause: String? = null
            var DDL: String? = null

            if (begin_col != -1) {
                column = sqlStatement.substring(begin_col)
                end_col = begin_col + column.indexOf(' ')
                column = sqlStatement.substring(begin_col, end_col)
                // System.out.println(" column:" + column + " begincolumn:" +
                // begin_col + "en column:" + end_col );
                // System.out.println(" type " + sqlStatement.substring(end_col
                // + 1));
                var rest: String? = sqlStatement.substring(end_col + 1)

                if (action == " ADD ") {
                    if (rest!!.toUpperCase().indexOf(" DEFAULT ") != -1) {
                        val beforeDefault = rest.substring(0, rest.toUpperCase().indexOf(" DEFAULT "))
                        begin_default = rest.toUpperCase().indexOf(
                                " DEFAULT ") + 9
                        defaultvalue = rest.substring(begin_default)
                        val nextspace = defaultvalue.indexOf(' ')
                        if (nextspace > -1) {
                            rest = defaultvalue.substring(nextspace)
                            defaultvalue = defaultvalue.substring(0, defaultvalue.indexOf(' '))
                        } else {
                            rest = ""
                        }
                        if (defaultvalue.equals("NULL", ignoreCase = true) || defaultvalue.equals("statement_timestamp()", ignoreCase = true)) {
                            DDL = (sqlStatement.substring(0, begin_col - action.length)
                                    + " ADD COLUMN "
                                    + column
                                    + " " + beforeDefault.trim { it <= ' ' }
                                    + " DEFAULT "
                                    + defaultvalue.trim { it <= ' ' } + " " + rest.trim { it <= ' ' })
                        } else {
                            // Check if default value is already quoted, no need to double quote
                            if (defaultvalue.startsWith("'") && defaultvalue.endsWith("'")) {
                                DDL = (sqlStatement.substring(0, begin_col - action.length)
                                        + " ADD COLUMN "
                                        + column
                                        + " " + beforeDefault.trim { it <= ' ' }
                                        + " DEFAULT "
                                        + defaultvalue.trim { it <= ' ' } + " " + rest.trim { it <= ' ' })
                            } else {
                                DDL = (sqlStatement.substring(0, begin_col - action.length)
                                        + " ADD COLUMN "
                                        + column
                                        + " " + beforeDefault.trim { it <= ' ' }
                                        + " DEFAULT '"
                                        + defaultvalue.trim { it <= ' ' } + "' " + rest.trim { it <= ' ' })
                            }
                        }
                    } else {
                        DDL = (sqlStatement
                                .substring(0, begin_col - action.length)
                                + action + "COLUMN " + column + " " + rest.trim { it <= ' ' })
                    }
                } else if (action == " MODIFY ") {
                    rest = rest!!.trim { it <= ' ' }
                    if (rest.toUpperCase().startsWith("NOT ") || rest.toUpperCase().startsWith("NULL ")
                            || rest.toUpperCase() == "NULL" || rest.toUpperCase() == "NOT NULL") {
                        type = null
                    } else {
                        val typeEnd = rest.indexOf(' ')
                        type = if (typeEnd > 0) rest.substring(0, typeEnd).trim { it <= ' ' } else rest
                        rest = if (typeEnd > 0) rest.substring(typeEnd) else ""
                    }

                    if (rest.toUpperCase().indexOf(" DEFAULT ") != -1) {
                        begin_default = rest.toUpperCase().indexOf(
                                " DEFAULT ") + 9
                        defaultvalue = rest.substring(begin_default)
                        val nextspace = defaultvalue.indexOf(' ')
                        if (nextspace > -1) {
                            rest = defaultvalue.substring(nextspace)
                            defaultvalue = defaultvalue.substring(0, defaultvalue.indexOf(' '))
                        } else {
                            rest = ""
                        }
                        // Check if default value is already quoted
                        defaultvalue = defaultvalue.trim { it <= ' ' }
                        if (defaultvalue.startsWith("'") && defaultvalue.endsWith("'"))
                            defaultvalue = defaultvalue.substring(1, defaultvalue.length - 1)

                        if (rest != null && rest.toUpperCase().indexOf("NOT NULL") >= 0)
                            nullclause = "NOT NULL"
                        else if (rest != null && rest.toUpperCase().indexOf("NULL") >= 0)
                            nullclause = "NULL"

                        // return DDL;
                    } else if (rest != null && rest.toUpperCase().indexOf("NOT NULL") >= 0) {
                        nullclause = "NOT NULL"

                    } else if (rest != null && rest.toUpperCase().indexOf("NULL") >= 0) {
                        nullclause = "NULL"

                    }

                    DDL = "INSERT INTO t_alter_column values('"
                    var tableName = sqlStatement.substring(0, begin_col - action.length)
                    tableName = tableName.toUpperCase().replace("ALTER TABLE".toRegex(), "")
                    tableName = tableName.trim { it <= ' ' }.toLowerCase()
                    DDL = "$DDL$tableName','$column',"
                    if (type != null)
                        DDL = "$DDL'$type',"
                    else
                        DDL = DDL + "null,"
                    if (nullclause != null)
                        DDL = "$DDL'$nullclause',"
                    else
                        DDL = DDL + "null,"
                    if (defaultvalue != null)
                        DDL = "$DDL'$defaultvalue'"
                    else
                        DDL = DDL + "null"
                    DDL = "$DDL)"
                }
                return DDL!!
            }
        }

        return sqlStatement
    }

    companion object {

        /** RegEx: insensitive and dot to include line end characters  */
        val REGEX_FLAGS = Pattern.CASE_INSENSITIVE or Pattern.DOTALL

        /** Logger  */
        private val log = CLogger.getCLogger(Convert_PostgreSQL::class.java)
    }
} // Convert