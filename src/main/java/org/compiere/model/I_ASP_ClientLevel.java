
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.idempiere.common.util.KeyNamePair;

/** Generated Interface for ASP_ClientLevel
 *  @author iDempiere (generated) 
 *  @version Release 5.1
 */
public interface I_ASP_ClientLevel 
{

    /** TableName=ASP_ClientLevel */
    public static final String Table_Name = "ASP_ClientLevel";

    /** AD_Table_ID=53056 */
    public static final int Table_ID = 53056;

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 2 - Client 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(2);

    /** Load Meta Data */

    
    

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

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

    /** Column name ASP_ClientLevel_ID */
    public static final String COLUMNNAME_ASP_ClientLevel_ID = "ASP_ClientLevel_ID";

	/** Set Client Level	  */
	public void setASP_ClientLevel_ID (int ASP_ClientLevel_ID);

	/** Get Client Level	  */
	public int getASP_ClientLevel_ID();

    /** Column name ASP_ClientLevel_UU */
    public static final String COLUMNNAME_ASP_ClientLevel_UU = "ASP_ClientLevel_UU";

	/** Set ASP_ClientLevel_UU	  */
	public void setASP_ClientLevel_UU (String ASP_ClientLevel_UU);

	/** Get ASP_ClientLevel_UU	  */
	public String getASP_ClientLevel_UU();

    /** Column name ASP_Level_ID */
    public static final String COLUMNNAME_ASP_Level_ID = "ASP_Level_ID";

	/** Set ASP Level	  */
	public void setASP_Level_ID (int ASP_Level_ID);

	/** Get ASP Level	  */
	public int getASP_Level_ID();

	public I_ASP_Level getASP_Level() throws RuntimeException;

    /** Column name ASP_Module_ID */
    public static final String COLUMNNAME_ASP_Module_ID = "ASP_Module_ID";

	/** Set ASP Module	  */
	public void setASP_Module_ID (int ASP_Module_ID);

	/** Get ASP Module	  */
	public int getASP_Module_ID();

	public I_ASP_Module getASP_Module() throws RuntimeException;

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

    /** Column name Help */
    public static final String COLUMNNAME_Help = "Help";

	/** Set Comment/Help.
	  * Comment or Hint
	  */
	public void setHelp (String Help);

	/** Get Comment/Help.
	  * Comment or Hint
	  */
	public String getHelp();

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
