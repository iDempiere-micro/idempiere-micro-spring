package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.idempiere.common.util.KeyNamePair;

/**
 * Generated Interface for M_Product_BOM
 *
 * @author iDempiere (generated)
 * @version Release 5.1
 */
public interface I_M_Product_BOM {

  /** TableName=M_Product_BOM */
  public static final String Table_Name = "M_Product_BOM";

  /** AD_Table_ID=383 */
  public static final int Table_ID = 383;

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

  /** Column name BOMQty */
  public static final String COLUMNNAME_BOMQty = "BOMQty";

  /** Set BOM Quantity. Bill of Materials Quantity */
  public void setBOMQty(BigDecimal BOMQty);

  /** Get BOM Quantity. Bill of Materials Quantity */
  public BigDecimal getBOMQty();

  /** Column name BOMType */
  public static final String COLUMNNAME_BOMType = "BOMType";

  /** Set BOM Type. Type of BOM */
  public void setBOMType(String BOMType);

  /** Get BOM Type. Type of BOM */
  public String getBOMType();

  /** Column name CostStandard */
  public static final String COLUMNNAME_CostStandard = "CostStandard";

  /** Set Standard Cost. Standard Costs */
  public void setCostStandard(BigDecimal CostStandard);

  /** Get Standard Cost. Standard Costs */
  public BigDecimal getCostStandard();

  /** Column name CostStandardCumAmt */
  public static final String COLUMNNAME_CostStandardCumAmt = "CostStandardCumAmt";

  /** Set Std Cost Amount Sum. Standard Cost Invoice Amount Sum (internal) */
  public void setCostStandardCumAmt(BigDecimal CostStandardCumAmt);

  /** Get Std Cost Amount Sum. Standard Cost Invoice Amount Sum (internal) */
  public BigDecimal getCostStandardCumAmt();

  /** Column name Created */
  public static final String COLUMNNAME_Created = "Created";

  /** Get Created. Date this record was created */
  public Timestamp getCreated();

  /** Column name CreatedBy */
  public static final String COLUMNNAME_CreatedBy = "CreatedBy";

  /** Get Created By. User who created this records */
  public int getCreatedBy();

  /** Column name Description */
  public static final String COLUMNNAME_Description = "Description";

  /** Set Description. Optional short description of the record */
  public void setDescription(String Description);

  /** Get Description. Optional short description of the record */
  public String getDescription();

  /** Column name IsActive */
  public static final String COLUMNNAME_IsActive = "IsActive";

  /** Set Active. The record is active in the system */
  public void setIsActive(boolean IsActive);

  /** Get Active. The record is active in the system */
  public boolean isActive();

  /** Column name IsBillOfMaterial */
  public static final String COLUMNNAME_IsBillOfMaterial = "IsBillOfMaterial";

  /** Set Bill of Materials. Bill of Materials */
  public void setIsBillOfMaterial(boolean IsBillOfMaterial);

  /** Get Bill of Materials. Bill of Materials */
  public boolean isBillOfMaterial();

  /** Column name Line */
  public static final String COLUMNNAME_Line = "Line";

  /** Set Line No. Unique line for this document */
  public void setLine(int Line);

  /** Get Line No. Unique line for this document */
  public int getLine();

  /** Column name M_PartType_ID */
  public static final String COLUMNNAME_M_PartType_ID = "M_PartType_ID";

  /** Set Part Type */
  public void setM_PartType_ID(int M_PartType_ID);

  /** Get Part Type */
  public int getM_PartType_ID();

  public I_M_PartType getM_PartType() throws RuntimeException;

  /** Column name M_Product_BOM_ID */
  public static final String COLUMNNAME_M_Product_BOM_ID = "M_Product_BOM_ID";

  /** Set BOM Line */
  public void setM_Product_BOM_ID(int M_Product_BOM_ID);

  /** Get BOM Line */
  public int getM_Product_BOM_ID();

  /** Column name M_ProductBOM_ID */
  public static final String COLUMNNAME_M_ProductBOM_ID = "M_ProductBOM_ID";

  /** Set BOM Product. Bill of Material Component Product */
  public void setM_ProductBOM_ID(int M_ProductBOM_ID);

  /** Get BOM Product. Bill of Material Component Product */
  public int getM_ProductBOM_ID();

  public I_M_Product getM_ProductBOM() throws RuntimeException;

  /** Column name M_Product_BOM_UU */
  public static final String COLUMNNAME_M_Product_BOM_UU = "M_Product_BOM_UU";

  /** Set M_Product_BOM_UU */
  public void setM_Product_BOM_UU(String M_Product_BOM_UU);

  /** Get M_Product_BOM_UU */
  public String getM_Product_BOM_UU();

  /** Column name M_Product_ID */
  public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";

  /** Set Product. Product, Service, Item */
  public void setM_Product_ID(int M_Product_ID);

  /** Get Product. Product, Service, Item */
  public int getM_Product_ID();

  public I_M_Product getM_Product() throws RuntimeException;

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
