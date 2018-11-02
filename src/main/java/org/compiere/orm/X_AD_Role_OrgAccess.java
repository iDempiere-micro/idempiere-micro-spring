package org.compiere.orm;

import org.compiere.model.I_AD_Role_OrgAccess;
import org.compiere.orm.MTable;
import org.idempiere.orm.I_Persistent;
import org.idempiere.orm.POInfo;


import java.sql.ResultSet;
import java.util.Properties;

/** Generated Model for AD_Role_OrgAccess
 *  @author iDempiere (generated) 
 *  @version Release 5.1 - $Id$ */
public class X_AD_Role_OrgAccess extends PO implements I_AD_Role_OrgAccess, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20171031L;

    /** Standard Constructor */
    public X_AD_Role_OrgAccess (Properties ctx, int AD_Role_OrgAccess_ID, String trxName)
    {
      super (ctx, AD_Role_OrgAccess_ID, trxName);
      /** if (AD_Role_OrgAccess_ID == 0)
        {
			setAD_Role_ID (0);
			setIsReadOnly (false);
        } */
    }

    /** Load Constructor */
    public X_AD_Role_OrgAccess (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 6 - System - Client 
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
      StringBuffer sb = new StringBuffer ("X_AD_Role_OrgAccess[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_AD_Role getAD_Role() throws RuntimeException
    {
		return (org.compiere.model.I_AD_Role)MTable.get(getCtx(), org.compiere.model.I_AD_Role.Table_Name)
			.getPO(getAD_Role_ID(), get_TrxName());	}

	/** Set Role.
		@param AD_Role_ID 
		Responsibility Role
	  */
	public void setAD_Role_ID (int AD_Role_ID)
	{
		if (AD_Role_ID < 0) 
			set_ValueNoCheck (COLUMNNAME_AD_Role_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AD_Role_ID, Integer.valueOf(AD_Role_ID));
	}

	/** Get Role.
		@return Responsibility Role
	  */
	public int getAD_Role_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Role_ID);
		if (ii == null)
			 return 0;
		return ii;
	}

	/** Set AD_Role_OrgAccess_UU.
		@param AD_Role_OrgAccess_UU AD_Role_OrgAccess_UU	  */
	public void setAD_Role_OrgAccess_UU (String AD_Role_OrgAccess_UU)
	{
		set_Value (COLUMNNAME_AD_Role_OrgAccess_UU, AD_Role_OrgAccess_UU);
	}

	/** Get AD_Role_OrgAccess_UU.
		@return AD_Role_OrgAccess_UU	  */
	public String getAD_Role_OrgAccess_UU () 
	{
		return (String)get_Value(COLUMNNAME_AD_Role_OrgAccess_UU);
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