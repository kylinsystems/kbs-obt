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
import org.compiere.util.KeyNamePair;

/** Generated Model for KS_ODTPackage
 *  @author iDempiere (generated) 
 *  @version Release 5.1 - $Id$ */
public class X_KS_ODTPackage extends PO implements I_KS_ODTPackage, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20171129L;

    /** Standard Constructor */
    public X_KS_ODTPackage (Properties ctx, int KS_ODTPackage_ID, String trxName)
    {
      super (ctx, KS_ODTPackage_ID, trxName);
      /** if (KS_ODTPackage_ID == 0)
        {
			setIsImportedODT (false);
// N
			setKS_ODTPackage_ID (0);
			setKS_ODTPackage_UU (null);
			setName (null);
			setObjectType (null);
        } */
    }

    /** Load Constructor */
    public X_KS_ODTPackage (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_KS_ODTPackage[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Copy Package.
		@param CopyPackage 
		Copy Package
	  */
	public void setCopyPackage (String CopyPackage)
	{
		set_Value (COLUMNNAME_CopyPackage, CopyPackage);
	}

	/** Get Copy Package.
		@return Copy Package
	  */
	public String getCopyPackage () 
	{
		return (String)get_Value(COLUMNNAME_CopyPackage);
	}

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set Export Package.
		@param ExportPackage 
		Export Package
	  */
	public void setExportPackage (String ExportPackage)
	{
		set_Value (COLUMNNAME_ExportPackage, ExportPackage);
	}

	/** Get Export Package.
		@return Export Package
	  */
	public String getExportPackage () 
	{
		return (String)get_Value(COLUMNNAME_ExportPackage);
	}

	/** Set Is imported ODT.
		@param IsImportedODT 
		Is imported ODT
	  */
	public void setIsImportedODT (boolean IsImportedODT)
	{
		set_ValueNoCheck (COLUMNNAME_IsImportedODT, Boolean.valueOf(IsImportedODT));
	}

	/** Get Is imported ODT.
		@return Is imported ODT
	  */
	public boolean isImportedODT () 
	{
		Object oo = get_Value(COLUMNNAME_IsImportedODT);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set ODTPackage ID.
		@param KS_ODTPackage_ID 
		ODTPackage ID
	  */
	public void setKS_ODTPackage_ID (int KS_ODTPackage_ID)
	{
		if (KS_ODTPackage_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_KS_ODTPackage_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_KS_ODTPackage_ID, Integer.valueOf(KS_ODTPackage_ID));
	}

	/** Get ODTPackage ID.
		@return ODTPackage ID
	  */
	public int getKS_ODTPackage_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_KS_ODTPackage_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set KS_ODTPackage_UU.
		@param KS_ODTPackage_UU 
		KS_ODTPackage_UU
	  */
	public void setKS_ODTPackage_UU (String KS_ODTPackage_UU)
	{
		set_Value (COLUMNNAME_KS_ODTPackage_UU, KS_ODTPackage_UU);
	}

	/** Get KS_ODTPackage_UU.
		@return KS_ODTPackage_UU
	  */
	public String getKS_ODTPackage_UU () 
	{
		return (String)get_Value(COLUMNNAME_KS_ODTPackage_UU);
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

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
    }

	/** Application = ODT_APP */
	public static final String OBJECTTYPE_Application = "ODT_APP";
	/** Set Object Type.
		@param ObjectType 
		Object Type
	  */
	public void setObjectType (String ObjectType)
	{

		set_Value (COLUMNNAME_ObjectType, ObjectType);
	}

	/** Get Object Type.
		@return Object Type
	  */
	public String getObjectType () 
	{
		return (String)get_Value(COLUMNNAME_ObjectType);
	}
}