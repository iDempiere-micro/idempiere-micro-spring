package org.compiere.accounting;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.model.I_C_Calendar;
import org.compiere.orm.PO;
import org.idempiere.orm.I_Persistent;
import org.idempiere.common.util.KeyNamePair;
import org.idempiere.orm.POInfo;


/** Generated Model for C_Calendar
 *  @author iDempiere (generated) 
 *  @version Release 5.1 - $Id$ */
public class X_C_Calendar extends PO implements I_C_Calendar, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20171031L;

    /** Standard Constructor */
    public X_C_Calendar (Properties ctx, int C_Calendar_ID, String trxName)
    {
      super (ctx, C_Calendar_ID, trxName);
      /** if (C_Calendar_ID == 0)
        {
			setC_Calendar_ID (0);
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_C_Calendar (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 2 - Client 
      */
    protected int get_AccessLevel()
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
      StringBuffer sb = new StringBuffer ("X_C_Calendar[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Calendar.
		@param C_Calendar_ID 
		Accounting Calendar Name
	  */
	public void setC_Calendar_ID (int C_Calendar_ID)
	{
		if (C_Calendar_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Calendar_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Calendar_ID, Integer.valueOf(C_Calendar_ID));
	}

	/** Get Calendar.
		@return Accounting Calendar Name
	  */
	public int getC_Calendar_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Calendar_ID);
		if (ii == null)
			 return 0;
		return ii;
	}

	/** Set C_Calendar_UU.
		@param C_Calendar_UU C_Calendar_UU	  */
	public void setC_Calendar_UU (String C_Calendar_UU)
	{
		set_Value (COLUMNNAME_C_Calendar_UU, C_Calendar_UU);
	}

	/** Get C_Calendar_UU.
		@return C_Calendar_UU	  */
	public String getC_Calendar_UU () 
	{
		return (String)get_Value(COLUMNNAME_C_Calendar_UU);
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

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (HasName.Companion.getCOLUMNNAME_Name(), Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(HasName.Companion.getCOLUMNNAME_Name());
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
    }
}