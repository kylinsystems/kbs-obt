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

/** Generated Model for KS_ODTObjectDataLine
 *  @author iDempiere (generated) 
 *  @version Release 7.1 - $Id$ */
public class X_KS_ODTObjectDataLine extends PO implements I_KS_ODTObjectDataLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200202L;

    /** Standard Constructor */
    public X_KS_ODTObjectDataLine (Properties ctx, int KS_ODTObjectDataLine_ID, String trxName)
    {
      super (ctx, KS_ODTObjectDataLine_ID, trxName);
      /** if (KS_ODTObjectDataLine_ID == 0)
        {
			setAD_Column_ID (0);
			setKS_ODTObjectData_ID (0);
			setKS_ODTObjectDataLine_ID (0);
			setKS_ODTObjectDataLine_UU (null);
        } */
    }

    /** Load Constructor */
    public X_KS_ODTObjectDataLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_KS_ODTObjectDataLine[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_AD_Column getAD_Column() throws RuntimeException
    {
		return (org.compiere.model.I_AD_Column)MTable.get(getCtx(), org.compiere.model.I_AD_Column.Table_Name)
			.getPO(getAD_Column_ID(), get_TrxName());	}

	/** Set Column.
		@param AD_Column_ID 
		Column in the table
	  */
	public void setAD_Column_ID (int AD_Column_ID)
	{
		if (AD_Column_ID < 1) 
			set_Value (COLUMNNAME_AD_Column_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Column_ID, Integer.valueOf(AD_Column_ID));
	}

	/** Get Column.
		@return Column in the table
	  */
	public int getAD_Column_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Column_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set New Null Value.
		@param IsNewNullValue 
		New Null Value
	  */
	public void setIsNewNullValue (boolean IsNewNullValue)
	{
		set_Value (COLUMNNAME_IsNewNullValue, Boolean.valueOf(IsNewNullValue));
	}

	/** Get New Null Value.
		@return New Null Value
	  */
	public boolean isNewNullValue () 
	{
		Object oo = get_Value(COLUMNNAME_IsNewNullValue);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Old Null Value.
		@param IsOldNullValue 
		Old Null Value
	  */
	public void setIsOldNullValue (boolean IsOldNullValue)
	{
		set_Value (COLUMNNAME_IsOldNullValue, Boolean.valueOf(IsOldNullValue));
	}

	/** Get Old Null Value.
		@return Old Null Value
	  */
	public boolean isOldNullValue () 
	{
		Object oo = get_Value(COLUMNNAME_IsOldNullValue);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public com.kylinsystems.kbs.odt.model.I_KS_ODTObjectData getKS_ODTObjectData() throws RuntimeException
    {
		return (com.kylinsystems.kbs.odt.model.I_KS_ODTObjectData)MTable.get(getCtx(), com.kylinsystems.kbs.odt.model.I_KS_ODTObjectData.Table_Name)
			.getPO(getKS_ODTObjectData_ID(), get_TrxName());	}

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

	/** Set KS ODTObjectDataLine ID.
		@param KS_ODTObjectDataLine_ID 
		KS ODTObjectDataLine ID
	  */
	public void setKS_ODTObjectDataLine_ID (int KS_ODTObjectDataLine_ID)
	{
		if (KS_ODTObjectDataLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_KS_ODTObjectDataLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_KS_ODTObjectDataLine_ID, Integer.valueOf(KS_ODTObjectDataLine_ID));
	}

	/** Get KS ODTObjectDataLine ID.
		@return KS ODTObjectDataLine ID
	  */
	public int getKS_ODTObjectDataLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_KS_ODTObjectDataLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set KS_ODTObjectDataLine_UU.
		@param KS_ODTObjectDataLine_UU 
		KS_ODTObjectDataLine_UU
	  */
	public void setKS_ODTObjectDataLine_UU (String KS_ODTObjectDataLine_UU)
	{
		set_Value (COLUMNNAME_KS_ODTObjectDataLine_UU, KS_ODTObjectDataLine_UU);
	}

	/** Get KS_ODTObjectDataLine_UU.
		@return KS_ODTObjectDataLine_UU
	  */
	public String getKS_ODTObjectDataLine_UU () 
	{
		return (String)get_Value(COLUMNNAME_KS_ODTObjectDataLine_UU);
	}

	/** Set New ID.
		@param NewID 
		New ID
	  */
	public void setNewID (int NewID)
	{
		set_ValueNoCheck (COLUMNNAME_NewID, Integer.valueOf(NewID));
	}

	/** Get New ID.
		@return New ID
	  */
	public int getNewID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_NewID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set New UUID.
		@param NewUUID 
		New UUID
	  */
	public void setNewUUID (String NewUUID)
	{
		set_ValueNoCheck (COLUMNNAME_NewUUID, NewUUID);
	}

	/** Get New UUID.
		@return New UUID
	  */
	public String getNewUUID () 
	{
		return (String)get_Value(COLUMNNAME_NewUUID);
	}

	/** Set New Value.
		@param NewValue 
		New field value
	  */
	public void setNewValue (String NewValue)
	{
		set_Value (COLUMNNAME_NewValue, NewValue);
	}

	/** Get New Value.
		@return New field value
	  */
	public String getNewValue () 
	{
		return (String)get_Value(COLUMNNAME_NewValue);
	}

	/** Set Old ID.
		@param OldID 
		Old ID
	  */
	public void setOldID (int OldID)
	{
		set_ValueNoCheck (COLUMNNAME_OldID, Integer.valueOf(OldID));
	}

	/** Get Old ID.
		@return Old ID
	  */
	public int getOldID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_OldID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Old UUID.
		@param OldUUID 
		Old UUID
	  */
	public void setOldUUID (String OldUUID)
	{
		set_ValueNoCheck (COLUMNNAME_OldUUID, OldUUID);
	}

	/** Get Old UUID.
		@return Old UUID
	  */
	public String getOldUUID () 
	{
		return (String)get_Value(COLUMNNAME_OldUUID);
	}

	/** Set Old Value.
		@param OldValue 
		The old file data
	  */
	public void setOldValue (String OldValue)
	{
		set_Value (COLUMNNAME_OldValue, OldValue);
	}

	/** Get Old Value.
		@return The old file data
	  */
	public String getOldValue () 
	{
		return (String)get_Value(COLUMNNAME_OldValue);
	}
}