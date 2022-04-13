/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2012 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package com.kylinsystems.kbs.odt.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for KS_ODTObjectDataLine
 *  @author iDempiere (generated) 
 *  @version Development 9.0
 */
@SuppressWarnings("all")
public interface I_KS_ODTObjectDataLine 
{

    /** TableName=KS_ODTObjectDataLine */
    public static final String Table_Name = "KS_ODTObjectDataLine";

    /** AD_Table_ID=1000002 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 4 - System 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(4);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Column_ID */
    public static final String COLUMNNAME_AD_Column_ID = "AD_Column_ID";

	/** Set Column.
	  * Column in the table
	  */
	public void setAD_Column_ID (int AD_Column_ID);

	/** Get Column.
	  * Column in the table
	  */
	public int getAD_Column_ID();

	public org.compiere.model.I_AD_Column getAD_Column() throws RuntimeException;

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

    /** Column name IsNewNullValue */
    public static final String COLUMNNAME_IsNewNullValue = "IsNewNullValue";

	/** Set New Null Value.
	  * New Null Value
	  */
	public void setIsNewNullValue (boolean IsNewNullValue);

	/** Get New Null Value.
	  * New Null Value
	  */
	public boolean isNewNullValue();

    /** Column name IsOldNullValue */
    public static final String COLUMNNAME_IsOldNullValue = "IsOldNullValue";

	/** Set Old Null Value.
	  * Old Null Value
	  */
	public void setIsOldNullValue (boolean IsOldNullValue);

	/** Get Old Null Value.
	  * Old Null Value
	  */
	public boolean isOldNullValue();

    /** Column name KS_ODTObjectData_ID */
    public static final String COLUMNNAME_KS_ODTObjectData_ID = "KS_ODTObjectData_ID";

	/** Set KS ODTObjectData ID.
	  * KS ODTObjectData ID
	  */
	public void setKS_ODTObjectData_ID (int KS_ODTObjectData_ID);

	/** Get KS ODTObjectData ID.
	  * KS ODTObjectData ID
	  */
	public int getKS_ODTObjectData_ID();

	public com.kylinsystems.kbs.odt.model.I_KS_ODTObjectData getKS_ODTObjectData() throws RuntimeException;

    /** Column name KS_ODTObjectDataLine_ID */
    public static final String COLUMNNAME_KS_ODTObjectDataLine_ID = "KS_ODTObjectDataLine_ID";

	/** Set KS ODTObjectDataLine ID.
	  * KS ODTObjectDataLine ID
	  */
	public void setKS_ODTObjectDataLine_ID (int KS_ODTObjectDataLine_ID);

	/** Get KS ODTObjectDataLine ID.
	  * KS ODTObjectDataLine ID
	  */
	public int getKS_ODTObjectDataLine_ID();

    /** Column name KS_ODTObjectDataLine_UU */
    public static final String COLUMNNAME_KS_ODTObjectDataLine_UU = "KS_ODTObjectDataLine_UU";

	/** Set KS_ODTObjectDataLine_UU.
	  * KS_ODTObjectDataLine_UU
	  */
	public void setKS_ODTObjectDataLine_UU (String KS_ODTObjectDataLine_UU);

	/** Get KS_ODTObjectDataLine_UU.
	  * KS_ODTObjectDataLine_UU
	  */
	public String getKS_ODTObjectDataLine_UU();

    /** Column name NewID */
    public static final String COLUMNNAME_NewID = "NewID";

	/** Set New ID.
	  * New ID
	  */
	public void setNewID (int NewID);

	/** Get New ID.
	  * New ID
	  */
	public int getNewID();

    /** Column name NewUUID */
    public static final String COLUMNNAME_NewUUID = "NewUUID";

	/** Set New UUID.
	  * New UUID
	  */
	public void setNewUUID (String NewUUID);

	/** Get New UUID.
	  * New UUID
	  */
	public String getNewUUID();

    /** Column name NewValue */
    public static final String COLUMNNAME_NewValue = "NewValue";

	/** Set New Value.
	  * New field value
	  */
	public void setNewValue (String NewValue);

	/** Get New Value.
	  * New field value
	  */
	public String getNewValue();

    /** Column name OldID */
    public static final String COLUMNNAME_OldID = "OldID";

	/** Set Old ID.
	  * Old ID
	  */
	public void setOldID (int OldID);

	/** Get Old ID.
	  * Old ID
	  */
	public int getOldID();

    /** Column name OldUUID */
    public static final String COLUMNNAME_OldUUID = "OldUUID";

	/** Set Old UUID.
	  * Old UUID
	  */
	public void setOldUUID (String OldUUID);

	/** Get Old UUID.
	  * Old UUID
	  */
	public String getOldUUID();

    /** Column name OldValue */
    public static final String COLUMNNAME_OldValue = "OldValue";

	/** Set Old Value.
	  * The old file data
	  */
	public void setOldValue (String OldValue);

	/** Get Old Value.
	  * The old file data
	  */
	public String getOldValue();

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
