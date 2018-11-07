package org.compiere.orm;

import org.compiere.model.I_AD_User_OrgAccess;
import org.idempiere.orm.I_Persistent;
import org.idempiere.orm.POInfo;


import java.sql.ResultSet;
import java.util.Properties;

/** Generated Model for AD_User_OrgAccess
 *  @author iDempiere (generated) 
 *  @version Release 5.1 - $Id$ */
public class X_AD_User_OrgAccess extends PO implements I_AD_User_OrgAccess, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20171031L;

    /** Standard Constructor */
    public X_AD_User_OrgAccess (Properties ctx, int AD_User_OrgAccess_ID, String trxName)
    {
      super (ctx, AD_User_OrgAccess_ID, trxName);
      /** if (AD_User_OrgAccess_ID == 0)
        {
			setAD_User_ID (0);
			setIsReadOnly (false);
// N
        } */
    }

    /** Load Constructor */
    public X_AD_User_OrgAccess (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 6 - System - Client 
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
      StringBuffer sb = new StringBuffer ("X_AD_User_OrgAccess[")
        .append(getId()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_AD_User getAD_User() throws RuntimeException
    {
		return (org.compiere.model.I_AD_User)MTable.get(getCtx(), org.compiere.model.I_AD_User.Table_Name)
			.getPO(getAD_User_ID(), get_TrxName());	}

	/** Set User/Contact.
		@param AD_User_ID 
		User within the system - Internal or Business Partner Contact
	  */
	public void setAD_User_ID (int AD_User_ID)
	{
		if (AD_User_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AD_User_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AD_User_ID, Integer.valueOf(AD_User_ID));
	}

	/** Get User/Contact.
		@return User within the system - Internal or Business Partner Contact
	  */
	public int getAD_User_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_User_ID);
		if (ii == null)
			 return 0;
		return ii;
	}

	/** Set AD_User_OrgAccess_UU.
		@param AD_User_OrgAccess_UU AD_User_OrgAccess_UU	  */
	public void setAD_User_OrgAccess_UU (String AD_User_OrgAccess_UU)
	{
		set_Value (COLUMNNAME_AD_User_OrgAccess_UU, AD_User_OrgAccess_UU);
	}

	/** Get AD_User_OrgAccess_UU.
		@return AD_User_OrgAccess_UU	  */
	public String getAD_User_OrgAccess_UU () 
	{
		return (String)get_Value(COLUMNNAME_AD_User_OrgAccess_UU);
	}

	/** Set Read Only.
		@param IsReadOnly 
		Field is read only
	  */
	public void setIsReadOnly (boolean IsReadOnly)
	{
		set_Value (COLUMNNAME_IsReadOnly, Boolean.valueOf(IsReadOnly));
	}

	/** Get Read Only.
		@return Field is read only
	  */
	public boolean isReadOnly () 
	{
		Object oo = get_Value(COLUMNNAME_IsReadOnly);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}
}