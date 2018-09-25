
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.idempiere.common.util.KeyNamePair;

/** Generated Interface for AD_TreeBar
 *  @author iDempiere (generated) 
 *  @version Release 5.1
 */
public interface I_AD_TreeBar  extends IBasePO
{

    /** TableName=AD_TreeBar */
    public static final String Table_Name = "AD_TreeBar";

    /** AD_Table_ID=456 */
    public static final int Table_ID = 456;

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 7 - System - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(7);

    /** Load Meta Data */

    
    

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Menu_ID */
    public static final String COLUMNNAME_AD_Menu_ID = "AD_Menu_ID";

	/** Set Menu.
	  * Identifies a Menu
	  */
	public void setAD_Menu_ID (int AD_Menu_ID);

	/** Get Menu.
	  * Identifies a Menu
	  */
	public int getAD_Menu_ID();

	public I_AD_Menu getAD_Menu() throws RuntimeException;

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name AD_TreeBar_UU */
    public static final String COLUMNNAME_AD_TreeBar_UU = "AD_TreeBar_UU";

	/** Set AD_TreeBar_UU	  */
	public void setAD_TreeBar_UU (String AD_TreeBar_UU);

	/** Get AD_TreeBar_UU	  */
	public String getAD_TreeBar_UU();

    /** Column name AD_Tree_ID */
    public static final String COLUMNNAME_AD_Tree_ID = "AD_Tree_ID";

	/** Set Tree.
	  * Identifies a Tree
	  */
	public void setAD_Tree_ID (int AD_Tree_ID);

	/** Get Tree.
	  * Identifies a Tree
	  */
	public int getAD_Tree_ID();

	public I_AD_Tree getAD_Tree() throws RuntimeException;

    /** Column name AD_User_ID */
    public static final String COLUMNNAME_AD_User_ID = "AD_User_ID";

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name IsFavourite */
    public static final String COLUMNNAME_IsFavourite = "IsFavourite";

	/** Set Favourite.
	  * This record is a favourite
	  */
	public void setIsFavourite (boolean IsFavourite);

	/** Get Favourite.
	  * This record is a favourite
	  */
	public boolean isFavourite();

    /** Column name LoginOpenSeqNo */
    public static final String COLUMNNAME_LoginOpenSeqNo = "LoginOpenSeqNo";

	/** Set Login automatic open sequence.
	  * Determine the order items will be automatically opened when user logs in
	  */
	public void setLoginOpenSeqNo (int LoginOpenSeqNo);

	/** Get Login automatic open sequence.
	  * Determine the order items will be automatically opened when user logs in
	  */
	public int getLoginOpenSeqNo();

    /** Column name Node_ID */
    public static final String COLUMNNAME_Node_ID = "Node_ID";

	/** Set Node	  */
	public void setNode_ID (int Node_ID);

	/** Get Node	  */
	public int getNode_ID();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();
}
