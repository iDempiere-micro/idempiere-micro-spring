
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.idempiere.common.util.KeyNamePair;

/** Generated Interface for R_IssueProject
 *  @author iDempiere (generated) 
 *  @version Release 5.1
 */
public interface I_R_IssueProject 
{

    /** TableName=R_IssueProject */
    public static final String Table_Name = "R_IssueProject";

    /** AD_Table_ID=842 */
    public static final int Table_ID = 842;

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 6 - System - Client 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(6);

    /** Load Meta Data */

    /** Column name A_Asset_ID */
    public static final String COLUMNNAME_A_Asset_ID = "A_Asset_ID";

	/** Set Asset.
	  * Asset used internally or by customers
	  */
	public void setA_Asset_ID (int A_Asset_ID);

	/** Get Asset.
	  * Asset used internally or by customers
	  */
	public int getA_Asset_ID();

	public I_A_Asset getA_Asset() throws RuntimeException;

    
    

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

    /** Column name C_Project_ID */
    public static final String COLUMNNAME_C_Project_ID = "C_Project_ID";

	/** Set Project.
	  * Financial Project
	  */
	public void setC_Project_ID (int C_Project_ID);

	/** Get Project.
	  * Financial Project
	  */
	public int getC_Project_ID();

	public I_C_Project getC_Project() throws RuntimeException;

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

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

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

    /** Column name Name */
    public static final String COLUMNNAME_Name = "Name";

	/** Set Name.
	  * Alphanumeric identifier of the entity
	  */
	public void setName (String Name);

	/** Get Name.
	  * Alphanumeric identifier of the entity
	  */
	public String getName();

    /** Column name ProfileInfo */
    public static final String COLUMNNAME_ProfileInfo = "ProfileInfo";

	/** Set Profile.
	  * Information to help profiling the system for solving support issues
	  */
	public void setProfileInfo (String ProfileInfo);

	/** Get Profile.
	  * Information to help profiling the system for solving support issues
	  */
	public String getProfileInfo();

    /** Column name R_IssueProject_ID */
    public static final String COLUMNNAME_R_IssueProject_ID = "R_IssueProject_ID";

	/** Set Issue Project.
	  * Implementation Projects
	  */
	public void setR_IssueProject_ID (int R_IssueProject_ID);

	/** Get Issue Project.
	  * Implementation Projects
	  */
	public int getR_IssueProject_ID();

    /** Column name R_IssueProject_UU */
    public static final String COLUMNNAME_R_IssueProject_UU = "R_IssueProject_UU";

	/** Set R_IssueProject_UU	  */
	public void setR_IssueProject_UU (String R_IssueProject_UU);

	/** Get R_IssueProject_UU	  */
	public String getR_IssueProject_UU();

    /** Column name StatisticsInfo */
    public static final String COLUMNNAME_StatisticsInfo = "StatisticsInfo";

	/** Set Statistics.
	  * Information to help profiling the system for solving support issues
	  */
	public void setStatisticsInfo (String StatisticsInfo);

	/** Get Statistics.
	  * Information to help profiling the system for solving support issues
	  */
	public String getStatisticsInfo();

    /** Column name SystemStatus */
    public static final String COLUMNNAME_SystemStatus = "SystemStatus";

	/** Set System Status.
	  * Status of the system - Support priority depends on system status
	  */
	public void setSystemStatus (String SystemStatus);

	/** Get System Status.
	  * Status of the system - Support priority depends on system status
	  */
	public String getSystemStatus();

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
