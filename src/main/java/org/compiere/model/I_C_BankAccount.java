package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.idempiere.common.util.KeyNamePair;
import org.idempiere.icommon.model.IPO;

/**
 * Generated Interface for C_BankAccount
 *
 * @author iDempiere (generated)
 * @version Release 5.1
 */
public interface I_C_BankAccount extends IPO {

  /** TableName=C_BankAccount */
  public static final String Table_Name = "C_BankAccount";

  /** AD_Table_ID=297 */
  public static final int Table_ID = 297;

  KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

  /** AccessLevel = 3 - Client - Org */
  BigDecimal accessLevel = BigDecimal.valueOf(3);

  /** Load Meta Data */

  /** Column name AccountNo */
  public static final String COLUMNNAME_AccountNo = "AccountNo";

  /** Set Account No. Account Number */
  public void setAccountNo(String AccountNo);

  /** Get Account No. Account Number */
  public String getAccountNo();

  /** Get Client. Client/Tenant for this installation. */
  public int getADClientID();

  /** Column name AD_Org_ID */
  public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

  /** Set Organization. Organizational entity within client */
  public void setAD_Org_ID(int AD_Org_ID);

  /** Get Organization. Organizational entity within client */
  public int getAD_Org_ID();

  /** Column name BankAccountType */
  public static final String COLUMNNAME_BankAccountType = "BankAccountType";

  /** Set Bank Account Type. Bank Account Type */
  public void setBankAccountType(String BankAccountType);

  /** Get Bank Account Type. Bank Account Type */
  public String getBankAccountType();

  /** Column name BBAN */
  public static final String COLUMNNAME_BBAN = "BBAN";

  /** Set BBAN. Basic Bank Account Number */
  public void setBBAN(String BBAN);

  /** Get BBAN. Basic Bank Account Number */
  public String getBBAN();

  /** Column name C_BankAccount_ID */
  public static final String COLUMNNAME_C_BankAccount_ID = "C_BankAccount_ID";

  /** Set Bank Account. Account at the Bank */
  public void setC_BankAccount_ID(int C_BankAccount_ID);

  /** Get Bank Account. Account at the Bank */
  public int getC_BankAccount_ID();

  /** Column name C_BankAccount_UU */
  public static final String COLUMNNAME_C_BankAccount_UU = "C_BankAccount_UU";

  /** Set C_BankAccount_UU */
  public void setC_BankAccount_UU(String C_BankAccount_UU);

  /** Get C_BankAccount_UU */
  public String getC_BankAccount_UU();

  /** Column name C_Bank_ID */
  public static final String COLUMNNAME_C_Bank_ID = "C_Bank_ID";

  /** Set Bank. Bank */
  public void setC_Bank_ID(int C_Bank_ID);

  /** Get Bank. Bank */
  public int getC_Bank_ID();

  public I_C_Bank getC_Bank() throws RuntimeException;

  /** Column name C_Currency_ID */
  public static final String COLUMNNAME_C_Currency_ID = "C_Currency_ID";

  /** Set Currency. The Currency for this record */
  public void setC_Currency_ID(int C_Currency_ID);

  /** Get Currency. The Currency for this record */
  public int getC_Currency_ID();

  public I_C_Currency getC_Currency() throws RuntimeException;

  /** Column name Created */
  public static final String COLUMNNAME_Created = "Created";

  /** Get Created. Date this record was created */
  public Timestamp getCreated();

  /** Column name CreatedBy */
  public static final String COLUMNNAME_CreatedBy = "CreatedBy";

  /** Get Created By. User who created this records */
  public int getCreatedBy();

  /** Column name CreditLimit */
  public static final String COLUMNNAME_CreditLimit = "CreditLimit";

  /** Set Credit limit. Amount of Credit allowed */
  public void setCreditLimit(BigDecimal CreditLimit);

  /** Get Credit limit. Amount of Credit allowed */
  public BigDecimal getCreditLimit();

  /** Column name CurrentBalance */
  public static final String COLUMNNAME_CurrentBalance = "CurrentBalance";

  /** Set Current balance. Current Balance */
  public void setCurrentBalance(BigDecimal CurrentBalance);

  /** Get Current balance. Current Balance */
  public BigDecimal getCurrentBalance();

  /** Column name Description */
  public static final String COLUMNNAME_Description = "Description";

  /** Set Description. Optional short description of the record */
  public void setDescription(String Description);

  /** Get Description. Optional short description of the record */
  public String getDescription();

  /** Column name IBAN */
  public static final String COLUMNNAME_IBAN = "IBAN";

  /** Set IBAN. International Bank Account Number */
  public void setIBAN(String IBAN);

  /** Get IBAN. International Bank Account Number */
  public String getIBAN();

  /** Column name IsActive */
  public static final String COLUMNNAME_IsActive = "IsActive";

  /** Set Active. The record is active in the system */
  public void setIsActive(boolean IsActive);

  /** Get Active. The record is active in the system */
  public boolean isActive();

  /** Column name IsDefault */
  public static final String COLUMNNAME_IsDefault = "IsDefault";

  /** Set Default. Default value */
  public void setIsDefault(boolean IsDefault);

  /** Get Default. Default value */
  public boolean isDefault();

  /** Set Name. Alphanumeric identifier of the entity */
  public void setName(String Name);

  /** Get Name. Alphanumeric identifier of the entity */
  public String getName();

  /** Column name PaymentExportClass */
  public static final String COLUMNNAME_PaymentExportClass = "PaymentExportClass";

  /** Set Payment Export Class */
  public void setPaymentExportClass(String PaymentExportClass);

  /** Get Payment Export Class */
  public String getPaymentExportClass();

  /** Column name Updated */
  public static final String COLUMNNAME_Updated = "Updated";

  /** Get Updated. Date this record was updated */
  public Timestamp getUpdated();

  /** Column name UpdatedBy */
  public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

  /** Get Updated By. User who updated this records */
  public int getUpdatedBy();

  /** Column name Value */
  public static final String COLUMNNAME_Value = "Value";

  /** Set Search Key. Search key for the record in the format required - must be unique */
  public void setValue(String Value);

  /** Get Search Key. Search key for the record in the format required - must be unique */
  public String getValue();
}
