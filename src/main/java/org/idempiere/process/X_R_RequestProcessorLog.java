package org.idempiere.process;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.I_R_RequestProcessorLog;
import org.compiere.orm.MTable;
import org.compiere.orm.PO;
import org.idempiere.orm.I_Persistent;
import org.idempiere.orm.POInfo;

public class X_R_RequestProcessorLog extends PO implements I_R_RequestProcessorLog, I_Persistent
{

    /**
     *
     */
    private static final long serialVersionUID = 20171031L;

    /** Standard Constructor */
    public X_R_RequestProcessorLog (Properties ctx, int R_RequestProcessorLog_ID, String trxName)
    {
        super (ctx, R_RequestProcessorLog_ID, trxName);
        /** if (R_RequestProcessorLog_ID == 0)
         {
         setIsError (false);
         setR_RequestProcessor_ID (0);
         setR_RequestProcessorLog_ID (0);
         } */
    }

    /** Load Constructor */
    public X_R_RequestProcessorLog (Properties ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
    }

    /** AccessLevel
     * @return 2 - Client
     */
    protected int getAccessLevel()
    {
        return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
        POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
        return poi;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer ("X_R_RequestProcessorLog[")
            .append(getId()).append("]");
        return sb.toString();
    }

    /** Set Binary Data.
     @param BinaryData
     Binary Data
     */
    public void setBinaryData (byte[] BinaryData)
    {
        set_Value (COLUMNNAME_BinaryData, BinaryData);
    }

    /** Get Binary Data.
     @return Binary Data
     */
    public byte[] getBinaryData ()
    {
        return (byte[])get_Value(COLUMNNAME_BinaryData);
    }

    /** Set Description.
     @param Description
     Optional short description of the record
     */
    public void setDescription (String Description)
    {
        set_Value (COLUMNNAME_Description, Description);
    }

    /** Get Description.
     @return Optional short description of the record
     */
    public String getDescription ()
    {
        return (String)get_Value(COLUMNNAME_Description);
    }

    /** Set Error.
     @param IsError
     An Error occurred in the execution
     */
    public void setIsError (boolean IsError)
    {
        set_Value (COLUMNNAME_IsError, Boolean.valueOf(IsError));
    }

    /** Get Error.
     @return An Error occurred in the execution
     */
    public boolean isError ()
    {
        Object oo = get_Value(COLUMNNAME_IsError);
        if (oo != null)
        {
            if (oo instanceof Boolean)
                return ((Boolean)oo).booleanValue();
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set Reference.
     @param Reference
     Reference for this record
     */
    public void setReference (String Reference)
    {
        set_Value (COLUMNNAME_Reference, Reference);
    }

    /** Get Reference.
     @return Reference for this record
     */
    public String getReference ()
    {
        return (String)get_Value(COLUMNNAME_Reference);
    }

    public org.compiere.model.I_R_RequestProcessor getR_RequestProcessor() throws RuntimeException
    {
        return (org.compiere.model.I_R_RequestProcessor) MTable.get(getCtx(), org.compiere.model.I_R_RequestProcessor.Table_Name)
            .getPO(getR_RequestProcessor_ID(), get_TrxName());	}

    /** Set Request Processor.
     @param R_RequestProcessor_ID
     Processor for Requests
     */
    public void setR_RequestProcessor_ID (int R_RequestProcessor_ID)
    {
        if (R_RequestProcessor_ID < 1)
            set_ValueNoCheck (COLUMNNAME_R_RequestProcessor_ID, null);
        else
            set_ValueNoCheck (COLUMNNAME_R_RequestProcessor_ID, Integer.valueOf(R_RequestProcessor_ID));
    }

    /** Get Request Processor.
     @return Processor for Requests
     */
    public int getR_RequestProcessor_ID ()
    {
        Integer ii = (Integer)get_Value(COLUMNNAME_R_RequestProcessor_ID);
        if (ii == null)
            return 0;
        return ii;
    }

    /** Set Request Processor Log.
     @param R_RequestProcessorLog_ID
     Result of the execution of the Request Processor
     */
    public void setR_RequestProcessorLog_ID (int R_RequestProcessorLog_ID)
    {
        if (R_RequestProcessorLog_ID < 1)
            set_ValueNoCheck (COLUMNNAME_R_RequestProcessorLog_ID, null);
        else
            set_ValueNoCheck (COLUMNNAME_R_RequestProcessorLog_ID, Integer.valueOf(R_RequestProcessorLog_ID));
    }

    /** Get Request Processor Log.
     @return Result of the execution of the Request Processor
     */
    public int getR_RequestProcessorLog_ID ()
    {
        Integer ii = (Integer)get_Value(COLUMNNAME_R_RequestProcessorLog_ID);
        if (ii == null)
            return 0;
        return ii;
    }

    /** Set R_RequestProcessorLog_UU.
     @param R_RequestProcessorLog_UU R_RequestProcessorLog_UU	  */
    public void setR_RequestProcessorLog_UU (String R_RequestProcessorLog_UU)
    {
        set_Value (COLUMNNAME_R_RequestProcessorLog_UU, R_RequestProcessorLog_UU);
    }

    /** Get R_RequestProcessorLog_UU.
     @return R_RequestProcessorLog_UU	  */
    public String getR_RequestProcessorLog_UU ()
    {
        return (String)get_Value(COLUMNNAME_R_RequestProcessorLog_UU);
    }

    /** Set Summary.
     @param Summary
     Textual summary of this request
     */
    public void setSummary (String Summary)
    {
        set_Value (COLUMNNAME_Summary, Summary);
    }

    /** Get Summary.
     @return Textual summary of this request
     */
    public String getSummary ()
    {
        return (String)get_Value(COLUMNNAME_Summary);
    }

    /** Set Text Message.
     @param TextMsg
     Text Message
     */
    public void setTextMsg (String TextMsg)
    {
        set_Value (COLUMNNAME_TextMsg, TextMsg);
    }

    /** Get Text Message.
     @return Text Message
     */
    public String getTextMsg ()
    {
        return (String)get_Value(COLUMNNAME_TextMsg);
    }
}