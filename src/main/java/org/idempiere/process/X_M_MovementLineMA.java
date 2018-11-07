package org.idempiere.process;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.I_M_AttributeSetInstance;
import org.compiere.model.I_M_MovementLineMA;
import org.compiere.orm.MTable;
import org.compiere.orm.PO;
import org.idempiere.common.util.Env;
import org.idempiere.common.util.KeyNamePair;
import org.idempiere.orm.I_Persistent;
import org.idempiere.orm.POInfo;

public class X_M_MovementLineMA extends PO implements I_M_MovementLineMA, I_Persistent
{

    /**
     *
     */
    private static final long serialVersionUID = 20171031L;

    /** Standard Constructor */
    public X_M_MovementLineMA (Properties ctx, int M_MovementLineMA_ID, String trxName)
    {
        super (ctx, M_MovementLineMA_ID, trxName);
        /** if (M_MovementLineMA_ID == 0)
         {
         setM_AttributeSetInstance_ID (0);
         setM_MovementLine_ID (0);
         } */
    }

    /** Load Constructor */
    public X_M_MovementLineMA (Properties ctx, ResultSet rs, String trxName)
    {
        super (ctx, rs, trxName);
    }

    /** AccessLevel
     * @return 1 - Org
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
        StringBuffer sb = new StringBuffer ("X_M_MovementLineMA[")
            .append(getId()).append("]");
        return sb.toString();
    }

    /** Set Date  Material Policy.
     @param DateMaterialPolicy
     Time used for LIFO and FIFO Material Policy
     */
    public void setDateMaterialPolicy (Timestamp DateMaterialPolicy)
    {
        set_ValueNoCheck (COLUMNNAME_DateMaterialPolicy, DateMaterialPolicy);
    }

    /** Get Date  Material Policy.
     @return Time used for LIFO and FIFO Material Policy
     */
    public Timestamp getDateMaterialPolicy ()
    {
        return (Timestamp)get_Value(COLUMNNAME_DateMaterialPolicy);
    }

    /** Set Auto Generated.
     @param IsAutoGenerated Auto Generated	  */
    public void setIsAutoGenerated (boolean IsAutoGenerated)
    {
        set_ValueNoCheck (COLUMNNAME_IsAutoGenerated, Boolean.valueOf(IsAutoGenerated));
    }

    /** Get Auto Generated.
     @return Auto Generated	  */
    public boolean isAutoGenerated ()
    {
        Object oo = get_Value(COLUMNNAME_IsAutoGenerated);
        if (oo != null)
        {
            if (oo instanceof Boolean)
                return ((Boolean)oo).booleanValue();
            return "Y".equals(oo);
        }
        return false;
    }

    public I_M_AttributeSetInstance getMAttributeSetInstance() throws RuntimeException
    {
        return (I_M_AttributeSetInstance) MTable.get(getCtx(), I_M_AttributeSetInstance.Table_Name)
            .getPO(getMAttributeSetInstance_ID(), get_TrxName());	}

    /** Set Attribute Set Instance.
     @param M_AttributeSetInstance_ID
     Product Attribute Set Instance
     */
    public void setM_AttributeSetInstance_ID (int M_AttributeSetInstance_ID)
    {
        if (M_AttributeSetInstance_ID < 0)
            set_ValueNoCheck (COLUMNNAME_M_AttributeSetInstance_ID, null);
        else
            set_ValueNoCheck (COLUMNNAME_M_AttributeSetInstance_ID, Integer.valueOf(M_AttributeSetInstance_ID));
    }

    /** Get Attribute Set Instance.
     @return Product Attribute Set Instance
     */
    public int getMAttributeSetInstance_ID ()
    {
        Integer ii = (Integer)get_Value(COLUMNNAME_M_AttributeSetInstance_ID);
        if (ii == null)
            return 0;
        return ii;
    }

    public org.compiere.model.I_M_MovementLine getM_MovementLine() throws RuntimeException
    {
        return (org.compiere.model.I_M_MovementLine)MTable.get(getCtx(), org.compiere.model.I_M_MovementLine.Table_Name)
            .getPO(getM_MovementLine_ID(), get_TrxName());	}

    /** Set Move Line.
     @param M_MovementLine_ID
     Inventory Move document Line
     */
    public void setM_MovementLine_ID (int M_MovementLine_ID)
    {
        if (M_MovementLine_ID < 1)
            set_ValueNoCheck (COLUMNNAME_M_MovementLine_ID, null);
        else
            set_ValueNoCheck (COLUMNNAME_M_MovementLine_ID, Integer.valueOf(M_MovementLine_ID));
    }

    /** Get Move Line.
     @return Inventory Move document Line
     */
    public int getM_MovementLine_ID ()
    {
        Integer ii = (Integer)get_Value(COLUMNNAME_M_MovementLine_ID);
        if (ii == null)
            return 0;
        return ii;
    }

    /** Get Record ID/ColumnName
     @return ID/ColumnName pair
     */
    public KeyNamePair getKeyNamePair()
    {
        return new KeyNamePair(getId(), String.valueOf(getM_MovementLine_ID()));
    }

    /** Set M_MovementLineMA_UU.
     @param M_MovementLineMA_UU M_MovementLineMA_UU	  */
    public void setM_MovementLineMA_UU (String M_MovementLineMA_UU)
    {
        set_Value (COLUMNNAME_M_MovementLineMA_UU, M_MovementLineMA_UU);
    }

    /** Get M_MovementLineMA_UU.
     @return M_MovementLineMA_UU	  */
    public String getM_MovementLineMA_UU ()
    {
        return (String)get_Value(COLUMNNAME_M_MovementLineMA_UU);
    }

    /** Set Movement Quantity.
     @param MovementQty
     Quantity of a product moved.
     */
    public void setMovementQty (BigDecimal MovementQty)
    {
        set_Value (COLUMNNAME_MovementQty, MovementQty);
    }

    /** Get Movement Quantity.
     @return Quantity of a product moved.
     */
    public BigDecimal getMovementQty ()
    {
        BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MovementQty);
        if (bd == null)
            return Env.ZERO;
        return bd;
    }
}