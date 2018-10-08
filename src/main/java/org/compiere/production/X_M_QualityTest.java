package org.compiere.production;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.model.I_M_QualityTest;
import org.compiere.orm.PO;
import org.idempiere.orm.I_Persistent;
import org.idempiere.common.util.KeyNamePair;
import org.idempiere.orm.POInfo;


/** Generated Model for M_QualityTest
 *  @author iDempiere (generated) 
 *  @version Release 5.1 - $Id$ */
public class X_M_QualityTest extends PO implements I_M_QualityTest, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20171031L;

    /** Standard Constructor */
    public X_M_QualityTest (Properties ctx, int M_QualityTest_ID, String trxName)
    {
      super (ctx, M_QualityTest_ID, trxName);
      /** if (M_QualityTest_ID == 0)
        {
			setM_QualityTest_ID (0);
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_M_QualityTest (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
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
      StringBuffer sb = new StringBuffer ("X_M_QualityTest[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set Comment/Help.
		@param Help 
		Comment or Hint
	  */
	public void setHelp (String Help)
	{
		set_Value (COLUMNNAME_Help, Help);
	}

	/** Get Comment/Help.
		@return Comment or Hint
	  */
	public String getHelp () 
	{
		return (String)get_Value(COLUMNNAME_Help);
	}

	/** Set Quality Test.
		@param M_QualityTest_ID Quality Test	  */
	public void setM_QualityTest_ID (int M_QualityTest_ID)
	{
		if (M_QualityTest_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_QualityTest_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_QualityTest_ID, Integer.valueOf(M_QualityTest_ID));
	}

	/** Get Quality Test.
		@return Quality Test	  */
	public int getM_QualityTest_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_QualityTest_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set M_QualityTest_UU.
		@param M_QualityTest_UU M_QualityTest_UU	  */
	public void setM_QualityTest_UU (String M_QualityTest_UU)
	{
		set_Value (COLUMNNAME_M_QualityTest_UU, M_QualityTest_UU);
	}

	/** Get M_QualityTest_UU.
		@return M_QualityTest_UU	  */
	public String getM_QualityTest_UU () 
	{
		return (String)get_Value(COLUMNNAME_M_QualityTest_UU);
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
    }
}