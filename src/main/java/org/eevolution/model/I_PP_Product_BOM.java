package org.eevolution.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.idempiere.common.util.KeyNamePair;

/**
 * Generated Interface for PP_Product_BOM
 *
 * @author iDempiere (generated)
 * @version Release 5.1
 */
@SuppressWarnings("all")
public interface I_PP_Product_BOM {

  /** TableName=PP_Product_BOM */
  public static final String Table_Name = "PP_Product_BOM";

  /** AD_Table_ID=53018 */
  public static final int Table_ID = 53018;

  KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

  /** AccessLevel = 3 - Client - Org */
  BigDecimal accessLevel = BigDecimal.valueOf(3);

  /** Load Meta Data */

  /** Get Client. Client/Tenant for this installation. */
  public int getADClientID();

  /** Column name AD_Org_ID */
  public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

  /** Set Organization. Organizational entity within client */
  public void setAD_Org_ID(int AD_Org_ID);

  /** Get Organization. Organizational entity within client */
  public int getAD_Org_ID();

  /** Column name BOMType */
  public static final String COLUMNNAME_BOMType = "BOMType";

  /** Set BOM Type. Type of BOM */
  public void setBOMType(String BOMType);

  /** Get BOM Type. Type of BOM */
  public String getBOMType();

  /** Column name BOMUse */
  public static final String COLUMNNAME_BOMUse = "BOMUse";

  /** Set BOM Use. The use of the Bill of Material */
  public void setBOMUse(String BOMUse);

  /** Get BOM Use. The use of the Bill of Material */
  public String getBOMUse();

  /** Column name CopyFrom */
  public static final String COLUMNNAME_CopyFrom = "CopyFrom";

  /** Set Copy From. Copy From Record */
  public void setCopyFrom(String CopyFrom);

  /** Get Copy From. Copy From Record */
  public String getCopyFrom();

  /** Column name Created */
  public static final String COLUMNNAME_Created = "Created";

  /** Get Created. Date this record was created */
  public Timestamp getCreated();

  /** Column name CreatedBy */
  public static final String COLUMNNAME_CreatedBy = "CreatedBy";

  /** Get Created By. User who created this records */
  public int getCreatedBy();

  /** Column name C_UOM_ID */
  public static final String COLUMNNAME_C_UOM_ID = "C_UOM_ID";

  /** Set UOM. Unit of Measure */
  public void setC_UOM_ID(int C_UOM_ID);

  /** Get UOM. Unit of Measure */
  public int getC_UOM_ID();

  public org.compiere.model.I_C_UOM getC_UOM() throws RuntimeException;

  /** Column name Description */
  public static final String COLUMNNAME_Description = "Description";

  /** Set Description. Optional short description of the record */
  public void setDescription(String Description);

  /** Get Description. Optional short description of the record */
  public String getDescription();

  /** Column name DocumentNo */
  public static final String COLUMNNAME_DocumentNo = "DocumentNo";

  /** Set Document No. Document sequence number of the document */
  public void setDocumentNo(String DocumentNo);

  /** Get Document No. Document sequence number of the document */
  public String getDocumentNo();

  /** Column name Help */
  public static final String COLUMNNAME_Help = "Help";

  /** Set Comment/Help. Comment or Hint */
  public void setHelp(String Help);

  /** Get Comment/Help. Comment or Hint */
  public String getHelp();

  /** Column name IsActive */
  public static final String COLUMNNAME_IsActive = "IsActive";

  /** Set Active. The record is active in the system */
  public void setIsActive(boolean IsActive);

  /** Get Active. The record is active in the system */
  public boolean isActive();

  /** Column name M_AttributeSetInstance_ID */
  public static final String COLUMNNAME_M_AttributeSetInstance_ID = "M_AttributeSetInstance_ID";

  /** Set Attribute Set Instance. Product Attribute Set Instance */
  public void setM_AttributeSetInstance_ID(int M_AttributeSetInstance_ID);

  /** Get Attribute Set Instance. Product Attribute Set Instance */
  public int getMAttributeSetInstance_ID();

  public I_M_AttributeSetInstance getMAttributeSetInstance() throws RuntimeException;

  /** Column name M_ChangeNotice_ID */
  public static final String COLUMNNAME_M_ChangeNotice_ID = "M_ChangeNotice_ID";

  /** Set Change Notice. Bill of Materials (Engineering) Change Notice (Version) */
  public void setM_ChangeNotice_ID(int M_ChangeNotice_ID);

  /** Get Change Notice. Bill of Materials (Engineering) Change Notice (Version) */
  public int getM_ChangeNotice_ID();

  public org.compiere.model.I_M_ChangeNotice getM_ChangeNotice() throws RuntimeException;

  /** Column name M_Product_ID */
  public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";

  /** Set Product. Product, Service, Item */
  public void setM_Product_ID(int M_Product_ID);

  /** Get Product. Product, Service, Item */
  public int getM_Product_ID();

  public org.compiere.model.I_M_Product getM_Product() throws RuntimeException;

  /** Set Name. Alphanumeric identifier of the entity */
  public void setName(String Name);

  /** Get Name. Alphanumeric identifier of the entity */
  public String getName();

  /** Column name PP_Product_BOM_ID */
  public static final String COLUMNNAME_PP_Product_BOM_ID = "PP_Product_BOM_ID";

  /** Set BOM & Formula. BOM & Formula */
  public void setPP_Product_BOM_ID(int PP_Product_BOM_ID);

  /** Get BOM & Formula. BOM & Formula */
  public int getPP_Product_BOM_ID();

  /** Column name PP_Product_BOM_UU */
  public static final String COLUMNNAME_PP_Product_BOM_UU = "PP_Product_BOM_UU";

  /** Set PP_Product_BOM_UU */
  public void setPP_Product_BOM_UU(String PP_Product_BOM_UU);

  /** Get PP_Product_BOM_UU */
  public String getPP_Product_BOM_UU();

  /** Column name Processing */
  public static final String COLUMNNAME_Processing = "Processing";

  /** Set Process Now */
  public void setProcessing(boolean Processing);

  /** Get Process Now */
  public boolean isProcessing();

  /** Column name Revision */
  public static final String COLUMNNAME_Revision = "Revision";

  /** Set Revision */
  public void setRevision(String Revision);

  /** Get Revision */
  public String getRevision();

  /** Column name Updated */
  public static final String COLUMNNAME_Updated = "Updated";

  /** Get Updated. Date this record was updated */
  public Timestamp getUpdated();

  /** Column name UpdatedBy */
  public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

  /** Get Updated By. User who updated this records */
  public int getUpdatedBy();

  /** Column name ValidFrom */
  public static final String COLUMNNAME_ValidFrom = "ValidFrom";

  /** Set Valid from. Valid from including this date (first day) */
  public void setValidFrom(Timestamp ValidFrom);

  /** Get Valid from. Valid from including this date (first day) */
  public Timestamp getValidFrom();

  /** Column name ValidTo */
  public static final String COLUMNNAME_ValidTo = "ValidTo";

  /** Set Valid to. Valid to including this date (last day) */
  public void setValidTo(Timestamp ValidTo);

  /** Get Valid to. Valid to including this date (last day) */
  public Timestamp getValidTo();

  /** Column name Value */
  public static final String COLUMNNAME_Value = "Value";

  /** Set Search Key. Search key for the record in the format required - must be unique */
  public void setValue(String Value);

  /** Get Search Key. Search key for the record in the format required - must be unique */
  public String getValue();
}
