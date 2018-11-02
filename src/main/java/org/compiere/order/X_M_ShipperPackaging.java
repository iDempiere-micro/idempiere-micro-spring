package org.compiere.order;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.model.I_M_ShipperPackaging;
import org.compiere.orm.MTable;
import org.compiere.orm.PO;
import org.idempiere.orm.I_Persistent;
import org.idempiere.common.util.Env;
import org.idempiere.orm.POInfo;


/** Generated Model for M_ShipperPackaging
 *  @author iDempiere (generated) 
 *  @version Release 5.1 - $Id$ */
public class X_M_ShipperPackaging extends PO implements I_M_ShipperPackaging, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20171031L;

    /** Standard Constructor */
    public X_M_ShipperPackaging (Properties ctx, int M_ShipperPackaging_ID, String trxName)
    {
      super (ctx, M_ShipperPackaging_ID, trxName);
      /** if (M_ShipperPackaging_ID == 0)
        {
			setIsDefault (false);
// N
			setM_Shipper_ID (0);
			setM_ShipperPackagingCfg_ID (0);
			setM_ShipperPackaging_ID (0);
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_M_ShipperPackaging (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_M_ShipperPackaging[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Default.
		@param IsDefault 
		Default value
	  */
	public void setIsDefault (boolean IsDefault)
	{
		set_Value (COLUMNNAME_IsDefault, Boolean.valueOf(IsDefault));
	}

	/** Get Default.
		@return Default value
	  */
	public boolean isDefault () 
	{
		Object oo = get_Value(COLUMNNAME_IsDefault);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public org.compiere.model.I_M_Shipper getM_Shipper() throws RuntimeException
    {
		return (org.compiere.model.I_M_Shipper)MTable.get(getCtx(), org.compiere.model.I_M_Shipper.Table_Name)
			.getPO(getM_Shipper_ID(), get_TrxName());	}

	/** Set Shipper.
		@param M_Shipper_ID 
		Method or manner of product delivery
	  */
	public void setM_Shipper_ID (int M_Shipper_ID)
	{
		if (M_Shipper_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_Shipper_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_Shipper_ID, Integer.valueOf(M_Shipper_ID));
	}

	/** Get Shipper.
		@return Method or manner of product delivery
	  */
	public int getM_Shipper_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Shipper_ID);
		if (ii == null)
			 return 0;
		return ii;
	}

	public org.compiere.model.I_M_ShipperPackagingCfg getM_ShipperPackagingCfg() throws RuntimeException
    {
		return (org.compiere.model.I_M_ShipperPackagingCfg)MTable.get(getCtx(), org.compiere.model.I_M_ShipperPackagingCfg.Table_Name)
			.getPO(getM_ShipperPackagingCfg_ID(), get_TrxName());	}

	/** Set Shipper Packaging Configuration.
		@param M_ShipperPackagingCfg_ID Shipper Packaging Configuration	  */
	public void setM_ShipperPackagingCfg_ID (int M_ShipperPackagingCfg_ID)
	{
		if (M_ShipperPackagingCfg_ID < 1) 
			set_Value (COLUMNNAME_M_ShipperPackagingCfg_ID, null);
		else 
			set_Value (COLUMNNAME_M_ShipperPackagingCfg_ID, Integer.valueOf(M_ShipperPackagingCfg_ID));
	}

	/** Get Shipper Packaging Configuration.
		@return Shipper Packaging Configuration	  */
	public int getM_ShipperPackagingCfg_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_ShipperPackagingCfg_ID);
		if (ii == null)
			 return 0;
		return ii;
	}

	/** Set Shipper Packaging.
		@param M_ShipperPackaging_ID Shipper Packaging	  */
	public void setM_ShipperPackaging_ID (int M_ShipperPackaging_ID)
	{
		if (M_ShipperPackaging_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_ShipperPackaging_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_ShipperPackaging_ID, Integer.valueOf(M_ShipperPackaging_ID));
	}

	/** Get Shipper Packaging.
		@return Shipper Packaging	  */
	public int getM_ShipperPackaging_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_ShipperPackaging_ID);
		if (ii == null)
			 return 0;
		return ii;
	}

	/** Set M_ShipperPackaging_UU.
		@param M_ShipperPackaging_UU M_ShipperPackaging_UU	  */
	public void setM_ShipperPackaging_UU (String M_ShipperPackaging_UU)
	{
		set_Value (COLUMNNAME_M_ShipperPackaging_UU, M_ShipperPackaging_UU);
	}

	/** Get M_ShipperPackaging_UU.
		@return M_ShipperPackaging_UU	  */
	public String getM_ShipperPackaging_UU () 
	{
		return (String)get_Value(COLUMNNAME_M_ShipperPackaging_UU);
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

	/** Set Weight.
		@param Weight 
		Weight of a product
	  */
	public void setWeight (BigDecimal Weight)
	{
		set_Value (COLUMNNAME_Weight, Weight);
	}

	/** Get Weight.
		@return Weight of a product
	  */
	public BigDecimal getWeight () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Weight);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}