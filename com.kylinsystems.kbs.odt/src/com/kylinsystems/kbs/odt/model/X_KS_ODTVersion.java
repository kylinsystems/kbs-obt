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

/** Generated Model for KS_ODTVersion
 *  @author iDempiere (generated) 
 *  @version Release 7.1 - $Id$ */
public class X_KS_ODTVersion extends PO implements I_KS_ODTVersion, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200202L;

    /** Standard Constructor */
    public X_KS_ODTVersion (Properties ctx, int KS_ODTVersion_ID, String trxName)
    {
      super (ctx, KS_ODTVersion_ID, trxName);
      /** if (KS_ODTVersion_ID == 0)
        {
			setKS_ODTPackage_ID (0);
			setKS_ODTVersion_ID (0);
			setKS_ODTVersion_UU (null);
			setName (null);
			setVersionNo (0);
			setVersion_Status (null);
        } */
    }

    /** Load Constructor */
    public X_KS_ODTVersion (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_KS_ODTVersion[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	public com.kylinsystems.kbs.odt.model.I_KS_ODTPackage getKS_ODTPackage() throws RuntimeException
    {
		return (com.kylinsystems.kbs.odt.model.I_KS_ODTPackage)MTable.get(getCtx(), com.kylinsystems.kbs.odt.model.I_KS_ODTPackage.Table_Name)
			.getPO(getKS_ODTPackage_ID(), get_TrxName());	}

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

	/** Set KS_ODTVersion_UU.
		@param KS_ODTVersion_UU 
		KS_ODTVersion_UU
	  */
	public void setKS_ODTVersion_UU (String KS_ODTVersion_UU)
	{
		set_Value (COLUMNNAME_KS_ODTVersion_UU, KS_ODTVersion_UU);
	}

	/** Get KS_ODTVersion_UU.
		@return KS_ODTVersion_UU
	  */
	public String getKS_ODTVersion_UU () 
	{
		return (String)get_Value(COLUMNNAME_KS_ODTVersion_UU);
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_ValueNoCheck (COLUMNNAME_Name, Name);
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

	/** Set Install Package.
		@param Package_Install 
		Install Package
	  */
	public void setPackage_Install (String Package_Install)
	{
		set_Value (COLUMNNAME_Package_Install, Package_Install);
	}

	/** Get Install Package.
		@return Install Package
	  */
	public String getPackage_Install () 
	{
		return (String)get_Value(COLUMNNAME_Package_Install);
	}

	/** Set Uninstall Package.
		@param Package_Uninstall 
		Uninstall Package
	  */
	public void setPackage_Uninstall (String Package_Uninstall)
	{
		set_Value (COLUMNNAME_Package_Uninstall, Package_Uninstall);
	}

	/** Get Uninstall Package.
		@return Uninstall Package
	  */
	public String getPackage_Uninstall () 
	{
		return (String)get_Value(COLUMNNAME_Package_Uninstall);
	}

	/** Set System Version.
		@param SystemVersion 
		System Version
	  */
	public void setSystemVersion (String SystemVersion)
	{
		set_ValueNoCheck (COLUMNNAME_SystemVersion, SystemVersion);
	}

	/** Get System Version.
		@return System Version
	  */
	public String getSystemVersion () 
	{
		return (String)get_Value(COLUMNNAME_SystemVersion);
	}

	/** Set Version Link EntityType.
		@param Version_LinkEntityType 
		Version Link EntityType
	  */
	public void setVersion_LinkEntityType (String Version_LinkEntityType)
	{
		set_Value (COLUMNNAME_Version_LinkEntityType, Version_LinkEntityType);
	}

	/** Get Version Link EntityType.
		@return Version Link EntityType
	  */
	public String getVersion_LinkEntityType () 
	{
		return (String)get_Value(COLUMNNAME_Version_LinkEntityType);
	}

	/** Set Version No.
		@param VersionNo 
		Version Number
	  */
	public void setVersionNo (int VersionNo)
	{
		set_ValueNoCheck (COLUMNNAME_VersionNo, Integer.valueOf(VersionNo));
	}

	/** Get Version No.
		@return Version Number
	  */
	public int getVersionNo () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_VersionNo);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Version Refresh.
		@param Version_Refresh 
		Version Refresh
	  */
	public void setVersion_Refresh (String Version_Refresh)
	{
		set_Value (COLUMNNAME_Version_Refresh, Version_Refresh);
	}

	/** Get Version Refresh.
		@return Version Refresh
	  */
	public String getVersion_Refresh () 
	{
		return (String)get_Value(COLUMNNAME_Version_Refresh);
	}

	/** Set Version Release.
		@param Version_Release 
		Version Release
	  */
	public void setVersion_Release (String Version_Release)
	{
		set_Value (COLUMNNAME_Version_Release, Version_Release);
	}

	/** Get Version Release.
		@return Version Release
	  */
	public String getVersion_Release () 
	{
		return (String)get_Value(COLUMNNAME_Version_Release);
	}

	/** Released = Released */
	public static final String VERSION_STATUS_Released = "Released";
	/** Draft = Draft */
	public static final String VERSION_STATUS_Draft = "Draft";
	/** Set Version Status.
		@param Version_Status 
		Version Status
	  */
	public void setVersion_Status (String Version_Status)
	{

		set_ValueNoCheck (COLUMNNAME_Version_Status, Version_Status);
	}

	/** Get Version Status.
		@return Version Status
	  */
	public String getVersion_Status () 
	{
		return (String)get_Value(COLUMNNAME_Version_Status);
	}
}