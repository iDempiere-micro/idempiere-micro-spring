
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.idempiere.common.util.KeyNamePair;

/** Generated Interface for B_Bid
 *  @author iDempiere (generated) 
 *  @version Release 5.1
 */
public interface I_B_Bid  extends IBasePO
{

    /** TableName=B_Bid */
    public static final String Table_Name = "B_Bid";

    /** AD_Table_ID=686 */
    public static final int Table_ID = 686;

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

    /** Column name AD_User_ID */
    public static final String COLUMNNAME_AD_User_ID = "AD_User_ID";

    /** Column name B_Bid_ID */
    public static final String COLUMNNAME_B_Bid_ID = "B_Bid_ID";

	/** Set Bid.
	  * Bid for a Topic
	  */
	public void setB_Bid_ID (int B_Bid_ID);

	/** Get Bid.
	  * Bid for a Topic
	  */
	public int getB_Bid_ID();

    /** Column name B_Bid_UU */
    public static final String COLUMNNAME_B_Bid_UU = "B_Bid_UU";

	/** Set B_Bid_UU	  */
	public void setB_Bid_UU (String B_Bid_UU);

	/** Get B_Bid_UU	  */
	public String getB_Bid_UU();

    /** Column name B_BuyerFunds_ID */
    public static final String COLUMNNAME_B_BuyerFunds_ID = "B_BuyerFunds_ID";

	/** Set Buyer Funds.
	  * Buyer Funds for Bids on Topics
	  */
	public void setB_BuyerFunds_ID (int B_BuyerFunds_ID);

	/** Get Buyer Funds.
	  * Buyer Funds for Bids on Topics
	  */
	public int getB_BuyerFunds_ID();

	public I_B_BuyerFunds getB_BuyerFunds() throws RuntimeException;

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
