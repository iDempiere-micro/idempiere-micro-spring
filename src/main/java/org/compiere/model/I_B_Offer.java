
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.idempiere.common.util.KeyNamePair;

/** Generated Interface for B_Offer
 *  @author iDempiere (generated) 
 *  @version Release 5.1
 */
public interface I_B_Offer 
{

    /** TableName=B_Offer */
    public static final String Table_Name = "B_Offer";

    /** AD_Table_ID=682 */
    public static final int Table_ID = 682;

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

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

    /** Column name B_Offer_ID */
    public static final String COLUMNNAME_B_Offer_ID = "B_Offer_ID";

	/** Set Offer.
	  * Offer for a Topic
	  */
	public void setB_Offer_ID (int B_Offer_ID);

	/** Get Offer.
	  * Offer for a Topic
	  */
	public int getB_Offer_ID();

    /** Column name B_Offer_UU */
    public static final String COLUMNNAME_B_Offer_UU = "B_Offer_UU";

	/** Set B_Offer_UU	  */
	public void setB_Offer_UU (String B_Offer_UU);

	/** Get B_Offer_UU	  */
	public String getB_Offer_UU();

    /** Column name B_SellerFunds_ID */
    public static final String COLUMNNAME_B_SellerFunds_ID = "B_SellerFunds_ID";

	/** Set Seller Funds.
	  * Seller Funds from Offers on Topics
	  */
	public void setB_SellerFunds_ID (int B_SellerFunds_ID);

	/** Get Seller Funds.
	  * Seller Funds from Offers on Topics
	  */
	public int getB_SellerFunds_ID();

	public I_B_SellerFunds getB_SellerFunds() throws RuntimeException;

    /** Column name B_Topic_ID */
    public static final String COLUMNNAME_B_Topic_ID = "B_Topic_ID";

	/** Set Topic.
	  * Auction Topic
	  */
	public void setB_Topic_ID (int B_Topic_ID);

	/** Get Topic.
	  * Auction Topic
	  */
	public int getB_Topic_ID();

	public I_B_Topic getB_Topic() throws RuntimeException;

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

    /** Column name IsWillingToCommit */
    public static final String COLUMNNAME_IsWillingToCommit = "IsWillingToCommit";

	/** Set Willing to commit	  */
	public void setIsWillingToCommit (boolean IsWillingToCommit);

	/** Get Willing to commit	  */
	public boolean isWillingToCommit();

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

    /** Column name PrivateNote */
    public static final String COLUMNNAME_PrivateNote = "PrivateNote";

	/** Set Private Note.
	  * Private Note - not visible to the other parties
	  */
	public void setPrivateNote (String PrivateNote);

	/** Get Private Note.
	  * Private Note - not visible to the other parties
	  */
	public String getPrivateNote();

    /** Column name TextMsg */
    public static final String COLUMNNAME_TextMsg = "TextMsg";

	/** Set Text Message.
	  * Text Message
	  */
	public void setTextMsg (String TextMsg);

	/** Get Text Message.
	  * Text Message
	  */
	public String getTextMsg();

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
