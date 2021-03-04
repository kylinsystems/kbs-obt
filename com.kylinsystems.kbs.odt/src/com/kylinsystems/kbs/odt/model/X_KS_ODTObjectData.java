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
/** Generated Model - DO NOT CHANGE */
package com.kylinsystems.kbs.odt.model;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for KS_ODTObjectData
 *  @author iDempiere (generated) 
 *  @version Release 8.2 - $Id$ */
public class X_KS_ODTObjectData extends PO implements I_KS_ODTObjectData, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20210304L;

    /** Standard Constructor */
    public X_KS_ODTObjectData (Properties ctx, int KS_ODTObjectData_ID, String trxName)
    {
      super (ctx, KS_ODTObjectData_ID, trxName);
      /** if (KS_ODTObjectData_ID == 0)
        {
			setAD_Table_ID (0);
			setisCoreID (false);
// N
			setKS_ODTObjectData_ID (0);
			setKS_ODTObjectData_UU (null);
			setKS_ODTVersion_ID (0);
			setName (null);
			setObjectData_Action (null);
// 'N/A'
			setObjectData_Status (null);
			setObjectData_Type (null);
			setObjectData_UUID (null);
        } */
    }

    /** Load Constructor */
    public X_KS_ODTObjectData (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 4 - System 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuilder sb = new StringBuilder ("X_KS_ODTObjectData[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_AD_Table getAD_Table() throws RuntimeException
    {
		return (org.compiere.model.I_AD_Table)MTable.get(getCtx(), org.compiere.model.I_AD_Table.Table_Name)
			.getPO(getAD_Table_ID(), get_TrxName());	}

	/** Set Table.
		@param AD_Table_ID 
		Database Table information
	  */
	public void setAD_Table_ID (int AD_Table_ID)
	{
		if (AD_Table_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AD_Table_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AD_Table_ID, Integer.valueOf(AD_Table_ID));
	}

	/** Get Table.
		@return Database Table information
	  */
	public int getAD_Table_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Table_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Is Core ID.
		@param isCoreID 
		Is Core ID
	  */
	public void setisCoreID (boolean isCoreID)
	{
		set_Value (COLUMNNAME_isCoreID, Boolean.valueOf(isCoreID));
	}

	/** Get Is Core ID.
		@return Is Core ID
	  */
	public boolean isCoreID () 
	{
		Object oo = get_Value(COLUMNNAME_isCoreID);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set KS ODTObjectData ID.
		@param KS_ODTObjectData_ID 
		KS ODTObjectData ID
	  */
	public void setKS_ODTObjectData_ID (int KS_ODTObjectData_ID)
	{
		if (KS_ODTObjectData_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_KS_ODTObjectData_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_KS_ODTObjectData_ID, Integer.valueOf(KS_ODTObjectData_ID));
	}

	/** Get KS ODTObjectData ID.
		@return KS ODTObjectData ID
	  */
	public int getKS_ODTObjectData_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_KS_ODTObjectData_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set KS_ODTObjectData_UU.
		@param KS_ODTObjectData_UU 
		KS_ODTObjectData_UU
	  */
	public void setKS_ODTObjectData_UU (String KS_ODTObjectData_UU)
	{
		set_Value (COLUMNNAME_KS_ODTObjectData_UU, KS_ODTObjectData_UU);
	}

	/** Get KS_ODTObjectData_UU.
		@return KS_ODTObjectData_UU
	  */
	public String getKS_ODTObjectData_UU () 
	{
		return (String)get_Value(COLUMNNAME_KS_ODTObjectData_UU);
	}

	public com.kylinsystems.kbs.odt.model.I_KS_ODTVersion getKS_ODTVersion() throws RuntimeException
    {
		return (com.kylinsystems.kbs.odt.model.I_KS_ODTVersion)MTable.get(getCtx(), com.kylinsystems.kbs.odt.model.I_KS_ODTVersion.Table_Name)
			.getPO(getKS_ODTVersion_ID(), get_TrxName());	}

	/** Set ODTVersion ID.
		@param KS_ODTVersion_ID 
		ODTVersion ID
	  */
	public void setKS_ODTVersion_ID (int KS_ODTVersion_ID)
	{
		if (KS_ODTVersion_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_KS_ODTVersion_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_KS_ODTVersion_ID, Integer.valueOf(KS_ODTVersion_ID));
	}

	/** Get ODTVersion ID.
		@return ODTVersion ID
	  */
	public int getKS_ODTVersion_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_KS_ODTVersion_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Messge Log.
		@param MessgeLog 
		Messge Log
	  */
	public void setMessgeLog (String MessgeLog)
	{
		set_ValueNoCheck (COLUMNNAME_MessgeLog, MessgeLog);
	}

	/** Get Messge Log.
		@return Messge Log
	  */
	public String getMessgeLog () 
	{
		return (String)get_Value(COLUMNNAME_MessgeLog);
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

	/** N/A = N/A */
	public static final String OBJECTDATA_ACTION_NA = "N/A";
	/** Insert = Insert */
	public static final String OBJECTDATA_ACTION_Insert = "Insert";
	/** Update = Update */
	public static final String OBJECTDATA_ACTION_Update = "Update";
	/** Delete = Delete */
	public static final String OBJECTDATA_ACTION_Delete = "Delete";
	/** Set ObjectData Action.
		@param ObjectData_Action 
		ObjectData Action
	  */
	public void setObjectData_Action (String ObjectData_Action)
	{

		set_Value (COLUMNNAME_ObjectData_Action, ObjectData_Action);
	}

	/** Get ObjectData Action.
		@return ObjectData Action
	  */
	public String getObjectData_Action () 
	{
		return (String)get_Value(COLUMNNAME_ObjectData_Action);
	}

	/** Applied = Applied */
	public static final String OBJECTDATA_STATUS_Applied = "Applied";
	/** Unapplied = Unapplied */
	public static final String OBJECTDATA_STATUS_Unapplied = "Unapplied";
	/** Failed = Failed */
	public static final String OBJECTDATA_STATUS_Failed = "Failed";
	/** Set ObjectData Status.
		@param ObjectData_Status 
		ObjectData_Status
	  */
	public void setObjectData_Status (String ObjectData_Status)
	{

		set_ValueNoCheck (COLUMNNAME_ObjectData_Status, ObjectData_Status);
	}

	/** Get ObjectData Status.
		@return ObjectData_Status
	  */
	public String getObjectData_Status () 
	{
		return (String)get_Value(COLUMNNAME_ObjectData_Status);
	}

	/** AD Object = AD Object */
	public static final String OBJECTDATA_TYPE_ADObject = "AD Object";
	/** SQL Statement = SQL */
	public static final String OBJECTDATA_TYPE_SQLStatement = "SQL";
	/** Set ObjectData Type.
		@param ObjectData_Type 
		ObjectData Type
	  */
	public void setObjectData_Type (String ObjectData_Type)
	{

		set_Value (COLUMNNAME_ObjectData_Type, ObjectData_Type);
	}

	/** Get ObjectData Type.
		@return ObjectData Type
	  */
	public String getObjectData_Type () 
	{
		return (String)get_Value(COLUMNNAME_ObjectData_Type);
	}

	/** Set ObjectData UUID.
		@param ObjectData_UUID 
		ObjectData UUID
	  */
	public void setObjectData_UUID (String ObjectData_UUID)
	{
		set_ValueNoCheck (COLUMNNAME_ObjectData_UUID, ObjectData_UUID);
	}

	/** Get ObjectData UUID.
		@return ObjectData UUID
	  */
	public String getObjectData_UUID () 
	{
		return (String)get_Value(COLUMNNAME_ObjectData_UUID);
	}

	/** Set Record ID.
		@param Record_ID 
		Direct internal record ID
	  */
	public void setRecord_ID (int Record_ID)
	{
		if (Record_ID < 0) 
			set_Value (COLUMNNAME_Record_ID, null);
		else 
			set_Value (COLUMNNAME_Record_ID, Integer.valueOf(Record_ID));
	}

	/** Get Record ID.
		@return Direct internal record ID
	  */
	public int getRecord_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Record_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Sequence.
		@param SeqNo 
		Method of ordering records; lowest number comes first
	  */
	public void setSeqNo (int SeqNo)
	{
		set_ValueNoCheck (COLUMNNAME_SeqNo, Integer.valueOf(SeqNo));
	}

	/** Get Sequence.
		@return Method of ordering records; lowest number comes first
	  */
	public int getSeqNo () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SeqNo);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SQL Apply.
		@param SQL_Apply 
		SQL Apply
	  */
	public void setSQL_Apply (String SQL_Apply)
	{
		set_Value (COLUMNNAME_SQL_Apply, SQL_Apply);
	}

	/** Get SQL Apply.
		@return SQL Apply
	  */
	public String getSQL_Apply () 
	{
		return (String)get_Value(COLUMNNAME_SQL_Apply);
	}

	/** Set SQL Unapply.
		@param SQL_Unapply 
		SQL Unapply
	  */
	public void setSQL_Unapply (String SQL_Unapply)
	{
		set_Value (COLUMNNAME_SQL_Unapply, SQL_Unapply);
	}

	/** Get SQL Unapply.
		@return SQL Unapply
	  */
	public String getSQL_Unapply () 
	{
		return (String)get_Value(COLUMNNAME_SQL_Unapply);
	}
}