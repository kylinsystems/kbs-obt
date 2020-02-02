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

/** Generated Interface for KS_ODTObjectData
 *  @author iDempiere (generated) 
 *  @version Release 7.1
 */
@SuppressWarnings("all")
public interface I_KS_ODTObjectData 
{

    /** TableName=KS_ODTObjectData */
    public static final String Table_Name = "KS_ODTObjectData";

    /** AD_Table_ID=1000003 */
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

    /** Column name AD_Table_ID */
    public static final String COLUMNNAME_AD_Table_ID = "AD_Table_ID";

	/** Set Table.
	  * Database Table information
	  */
	public void setAD_Table_ID (int AD_Table_ID);

	/** Get Table.
	  * Database Table information
	  */
	public int getAD_Table_ID();

	public org.compiere.model.I_AD_Table getAD_Table() throws RuntimeException;

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

    /** Column name isCoreID */
    public static final String COLUMNNAME_isCoreID = "isCoreID";

	/** Set Is Core ID.
	  * Is Core ID
	  */
	public void setisCoreID (boolean isCoreID);

	/** Get Is Core ID.
	  * Is Core ID
	  */
	public boolean isCoreID();

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

    /** Column name KS_ODTObjectData_UU */
    public static final String COLUMNNAME_KS_ODTObjectData_UU = "KS_ODTObjectData_UU";

	/** Set KS_ODTObjectData_UU.
	  * KS_ODTObjectData_UU
	  */
	public void setKS_ODTObjectData_UU (String KS_ODTObjectData_UU);

	/** Get KS_ODTObjectData_UU.
	  * KS_ODTObjectData_UU
	  */
	public String getKS_ODTObjectData_UU();

    /** Column name KS_ODTVersion_ID */
    public static final String COLUMNNAME_KS_ODTVersion_ID = "KS_ODTVersion_ID";

	/** Set ODTVersion ID.
	  * ODTVersion ID
	  */
	public void setKS_ODTVersion_ID (int KS_ODTVersion_ID);

	/** Get ODTVersion ID.
	  * ODTVersion ID
	  */
	public int getKS_ODTVersion_ID();

	public com.kylinsystems.kbs.odt.model.I_KS_ODTVersion getKS_ODTVersion() throws RuntimeException;

    /** Column name MessgeLog */
    public static final String COLUMNNAME_MessgeLog = "MessgeLog";

	/** Set Messge Log.
	  * Messge Log
	  */
	public void setMessgeLog (String MessgeLog);

	/** Get Messge Log.
	  * Messge Log
	  */
	public String getMessgeLog();

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

    /** Column name ObjectData_Action */
    public static final String COLUMNNAME_ObjectData_Action = "ObjectData_Action";

	/** Set ObjectData Action.
	  * ObjectData Action
	  */
	public void setObjectData_Action (String ObjectData_Action);

	/** Get ObjectData Action.
	  * ObjectData Action
	  */
	public String getObjectData_Action();

    /** Column name ObjectData_Status */
    public static final String COLUMNNAME_ObjectData_Status = "ObjectData_Status";

	/** Set ObjectData Status.
	  * ObjectData_Status
	  */
	public void setObjectData_Status (String ObjectData_Status);

	/** Get ObjectData Status.
	  * ObjectData_Status
	  */
	public String getObjectData_Status();

    /** Column name ObjectData_Type */
    public static final String COLUMNNAME_ObjectData_Type = "ObjectData_Type";

	/** Set ObjectData Type.
	  * ObjectData Type
	  */
	public void setObjectData_Type (String ObjectData_Type);

	/** Get ObjectData Type.
	  * ObjectData Type
	  */
	public String getObjectData_Type();

    /** Column name ObjectData_UUID */
    public static final String COLUMNNAME_ObjectData_UUID = "ObjectData_UUID";

	/** Set ObjectData UUID.
	  * ObjectData UUID
	  */
	public void setObjectData_UUID (String ObjectData_UUID);

	/** Get ObjectData UUID.
	  * ObjectData UUID
	  */
	public String getObjectData_UUID();

    /** Column name Record_ID */
    public static final String COLUMNNAME_Record_ID = "Record_ID";

	/** Set Record ID.
	  * Direct internal record ID
	  */
	public void setRecord_ID (int Record_ID);

	/** Get Record ID.
	  * Direct internal record ID
	  */
	public int getRecord_ID();

    /** Column name SeqNo */
    public static final String COLUMNNAME_SeqNo = "SeqNo";

	/** Set Sequence.
	  * Method of ordering records;
 lowest number comes first
	  */
	public void setSeqNo (int SeqNo);

	/** Get Sequence.
	  * Method of ordering records;
 lowest number comes first
	  */
	public int getSeqNo();

    /** Column name SQL_Apply */
    public static final String COLUMNNAME_SQL_Apply = "SQL_Apply";

	/** Set SQL Apply.
	  * SQL Apply
	  */
	public void setSQL_Apply (String SQL_Apply);

	/** Get SQL Apply.
	  * SQL Apply
	  */
	public String getSQL_Apply();

    /** Column name SQL_Unapply */
    public static final String COLUMNNAME_SQL_Unapply = "SQL_Unapply";

	/** Set SQL Unapply.
	  * SQL Unapply
	  */
	public void setSQL_Unapply (String SQL_Unapply);

	/** Get SQL Unapply.
	  * SQL Unapply
	  */
	public String getSQL_Unapply();

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
