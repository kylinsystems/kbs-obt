/******************************************************************************
 * Copyright (C) 2016-2019 ken.longnan@gmail.com                                   *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/

package com.kylinsystems.kbs.odt.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.util.ProcessUtil;
import org.compiere.model.I_AD_User;
import org.compiere.model.I_C_BPartner;
import org.compiere.model.I_PA_Report;
import org.compiere.model.Lookup;
import org.compiere.model.MColumn;
import org.compiere.model.MLookup;
import org.compiere.model.MPInstance;
import org.compiere.model.MTable;
import org.compiere.model.Null;
import org.compiere.model.PO;
import org.compiere.model.POInfo;
import org.compiere.model.POInfoColumn;
import org.compiere.model.Query;
import org.compiere.model.X_AD_Ref_Table;
import org.compiere.model.X_AD_Reference;
import org.compiere.process.ProcessInfo;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Msg;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Utils {
	public static PO generatePO(String uuid, MTable table) {
		PO po;
		String tableName = table.getTableName();
		String whereClauseUUID = PO.getUUIDColumnName(tableName) + "='" + uuid + "'" ;
		po = table.getPO(whereClauseUUID, null);
		if (po == null) {
			// create new PO
			po = table.getPO(0, null);
			po.set_ValueNoCheck(po.getUUIDColumnName(), uuid);
		}
		return po;
	}

	public static PO buildPO(PO po, Integer AD_Column_ID, Integer NewID, String NewValue, Boolean IsNewNullValue, String NewUUID, MTable table, Properties ctx, CLogger log)
	{
		String tableName = table.getTableName();
		String columnName = MColumn.getColumnName(ctx, AD_Column_ID);
		int columnIndex = po.get_ColumnIndex(columnName);

		MColumn column = table.getColumn(columnName);
		POInfo poInfo = POInfo.getPOInfo(ctx, table.getAD_Table_ID(), po.get_TrxName());
		boolean isColumnUpdateable = poInfo.isColumnUpdateable(columnIndex);
		//POInfoColumn poInfoCol = poInfo.getColumn(po.get_ColumnIndex(columnName));

		if (log.isLoggable(Level.FINE)) log.fine("Apply ObjectDataLine, Column Name:" + columnName);

		if ("AD_Chart_ID".equalsIgnoreCase(columnName)) {
			return po; // skip
		}

		if ("AD_Client_ID".equalsIgnoreCase(columnName)
				|| "Created".equalsIgnoreCase(columnName)
				|| "Updated".equalsIgnoreCase(columnName)
				|| "UpdatedBy".equalsIgnoreCase(columnName)
				)
			return po; // skip those columns

		if (poInfo.isKey(columnIndex) && !"AD_EntityType".equalsIgnoreCase(columnName))
			return po; // skip Key column

		if (I_PA_Report.Table_Name.equalsIgnoreCase(tableName)
				&& "AD_PrintFormat_ID".equalsIgnoreCase(columnName))
			return po;

		if (I_C_BPartner.Table_Name.equalsIgnoreCase(tableName)
				&& ("TOTALOPENBALANCE".equalsIgnoreCase(columnName)
					|| "ACTUALLIFETIMEVALUE".equalsIgnoreCase(columnName)
					|| "FIRSTSALE".equalsIgnoreCase(columnName)
					|| "SO_CREDITUSED".equalsIgnoreCase(columnName)))
			return po;

		if (I_AD_User.Table_Name.equalsIgnoreCase(tableName)
				&& ("EMAILVERIFY".equalsIgnoreCase(columnName)
					|| "EMAILVERIFYDATE".equalsIgnoreCase(columnName)
					|| "LASTCONTACT".equalsIgnoreCase(columnName)
					|| "LASTRESULT".equalsIgnoreCase(columnName)))
			return po;

		// setup AD_Org_ID
		if ("AD_Org_ID".equalsIgnoreCase(columnName) &&
				!"AD_Org".equalsIgnoreCase(po.get_TableName())) {
			po.setAD_Org_ID(NewID);
		}

		String refTableName = null;
		String columnValue = NewValue;
		int displayType = poInfo.getColumnDisplayType(columnIndex);
		if (displayType == DisplayType.TableDir) {
			refTableName = columnName.substring(0, columnName.indexOf("_ID"));
		} else if (displayType == DisplayType.Locator) {
			refTableName = "M_Locator";
		} else if (displayType == DisplayType.Location) {
			refTableName = "C_Location";
		} else if (displayType == DisplayType.Table) {
			Lookup lookup = poInfo.getColumnLookup(columnIndex);
			MLookup mLookup = null;
			if (lookup != null) {
				mLookup = (MLookup)lookup;
			}
			int AD_Reference_Value_ID = 0;
			if (mLookup != null) {
				AD_Reference_Value_ID = mLookup.getAD_Reference_Value_ID();
			}
			//int AD_Reference_Value_ID = poInfo. poInfoCol.AD_Reference_Value_ID;
			int AD_Val_Rule_ID = 0;
			refTableName = findLookupTableName(AD_Reference_Value_ID,
					AD_Val_Rule_ID, ctx, log);
		} else if (displayType == DisplayType.Integer) {
			if (!IsNewNullValue && isColumnUpdateable && !"".equals(columnValue)) {
				po.set_ValueNoCheck(columnName, new Integer(columnValue));
			} else if (!isColumnUpdateable) {
				if (log.isLoggable(Level.FINE)) log.fine("Updateable is false, Column Name:" + columnName);
			}
		} else if (displayType == DisplayType.Number
				|| displayType == DisplayType.Amount
				|| displayType == DisplayType.CostPrice
				|| displayType == DisplayType.Quantity) {
			if (!IsNewNullValue && isColumnUpdateable && !"".equals(columnValue)) {
				po.set_ValueNoCheck(columnName, new BigDecimal(columnValue));
			} else if (!isColumnUpdateable) {
				if (log.isLoggable(Level.FINE)) log.fine("Updateable is false, Column Name:" + columnName);
			}

			if ("PriceActual".equalsIgnoreCase(columnName) && !"".equals(columnValue) &&
					(tableName.equals("C_InvoiceLine")
				  || tableName.equals("C_OrderLine"))) {
				po.set_ValueNoCheck(columnName, new BigDecimal(columnValue));
			}
		} else if (displayType == DisplayType.DateTime
				|| displayType == DisplayType.Date
				|| displayType == DisplayType.Time) {
			SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATEFORMAT_PATTERN);
			if (!IsNewNullValue && isColumnUpdateable && !"".equals(columnValue)) {
				try {
					po.set_ValueNoCheck(columnName, new Timestamp(sdf.parse(columnValue).getTime()));
				} catch (ParseException e) {
					if (log.isLoggable(Level.SEVERE)) log.log(Level.SEVERE, "", e);
				}
			} else if (!isColumnUpdateable) {
				if (log.isLoggable(Level.FINE)) log.fine("Updateable is false, Column Name:" + columnName);
			}
		} else if (displayType == DisplayType.PAttribute) {
			po.set_ValueNoCheck(columnName, 0); // TODO?
		} else if (displayType == DisplayType.Search) {
			int AD_Reference_Value_ID = column.getAD_Reference_Value_ID();
			int AD_Val_Rule_ID = column.getAD_Val_Rule_ID();

			if (AD_Reference_Value_ID == 0 && AD_Val_Rule_ID == 0)
				refTableName = columnName.substring(0, columnName.indexOf("_ID"));
			else
				refTableName = findLookupTableName(AD_Reference_Value_ID, AD_Val_Rule_ID, ctx, log);
		} else if (displayType == DisplayType.ID) {
			if ("AD_TreeNodeMM".equals(tableName)
					&& ("Node_ID".equals(columnName)
					 || "Parent_ID".equals(columnName))) {
				refTableName = "AD_Menu";
			}
		} else { // other DisplayType
			if (!IsNewNullValue && columnValue != null && !"".equals(columnValue)) {
				if (isColumnUpdateable)
					po.set_ValueNoCheck(columnName, columnValue);
				else {
					if (columnName.equals("DocumentNo")
					 || columnName.equals("MovementType")
					 || columnName.equals("IsSOTrx")
					 || columnName.equals("IsTransferred")
                     || (columnName.equals("TreeType") && tableName.equals("AD_Tree"))
					 || (columnName.equals("EntityType") && tableName.equals("AD_EntityType")))
						po.set_ValueNoCheck(columnName, columnValue);
					else
						if (log.isLoggable(Level.FINE)) log.fine("Updateable is false, Column Name:" + columnName);
				}
			}
		}

		// set ID for refTable
		if (refTableName != null) {
			MTable reftable = MTable.get(ctx, refTableName);
			String whereClauseUUIDRef =PO.getUUIDColumnName(refTableName) + "='" + NewUUID + "'" ;
			PO refPO = reftable.getPO(whereClauseUUIDRef, null);
			if (refPO != null) {
				po.set_ValueNoCheck(columnName, refPO.get_ID());
			} else {
				if (log.isLoggable(Level.FINE)) log.fine("RefPO is null, RefTableName:" + refTableName + "|UUID:" + NewUUID);
			}
		}

		return po;
	}

	public static void postInstallPackage(Properties ctx) {
		doSequencCheck(ctx);
		doRoleAccessUpdate(ctx);
	}

	public static ProcessInfo doSynchronizeColumn(int AD_Column_ID, Properties ctx) {
		ProcessInfo pi = new ProcessInfo("KSYS ODT Synchronize Column", 181, 101, AD_Column_ID);
		pi.setAD_Client_ID(0); // System
		pi.setAD_User_ID(100); // SuperUser
		pi.setIsBatch(false);
		pi.setClassName("org.compiere.process.ColumnSync");
		MPInstance instance = new MPInstance(ctx, pi.getAD_Process_ID(), pi.getRecord_ID());
		if (!instance.save()) {
			pi.setSummary(Msg.getMsg(ctx, "ProcessNoInstance"));
			pi.setError(true);
		}
		pi.setAD_PInstance_ID(instance.getAD_PInstance_ID());
		ProcessUtil.startJavaProcess(ctx, pi, null);
		return pi;
	}

	public static ProcessInfo doSequencCheck(Properties ctx) {
		ProcessInfo pi = new ProcessInfo("Sequence Check", 258);
		pi.setAD_Client_ID(0);
		pi.setAD_User_ID(100);
		pi.setClassName("org.compiere.process.SequenceCheck");
		MPInstance instance = new MPInstance(ctx, pi.getAD_Process_ID(), pi.getRecord_ID());
		if (!instance.save()) {
			pi.setSummary(Msg.getMsg(ctx, "ProcessNoInstance"));
			pi.setError(true);
		}
		pi.setAD_PInstance_ID(instance.getAD_PInstance_ID());
		ProcessUtil.startJavaProcess(ctx, pi, null);
		return pi;
	}

	public static ProcessInfo doRoleAccessUpdate(Properties ctx) {
		ProcessInfo pi = new ProcessInfo("Role Access Update", 295);
		pi.setAD_Client_ID(0);
		pi.setAD_User_ID(100);
		pi.setClassName("org.compiere.process.RoleAccessUpdate");
		MPInstance instance = new MPInstance(ctx, pi.getAD_Process_ID(), pi.getRecord_ID());
		if (!instance.save()) {
			pi.setSummary(Msg.getMsg(ctx, "ProcessNoInstance"));
			pi.setError(true);
		}
		pi.setAD_PInstance_ID(instance.getAD_PInstance_ID());
		ProcessUtil.startJavaProcess(ctx, pi, null);
		return pi;
	}

	/**
	 * 	findLookupTableName
	 *	@param AD_Reference_Value_ID
	 *	@param AD_Val_Rule_ID
	 *	@param ctx
	 *	@return
	 */
	public static String findLookupTableName(int AD_Reference_Value_ID, int AD_Val_Rule_ID, Properties ctx, CLogger log)
	{
		String refTableName = null;

		if (AD_Reference_Value_ID != 0)
		{
			X_AD_Reference adRef = new X_AD_Reference(ctx, AD_Reference_Value_ID, null);
			if (X_AD_Reference.VALIDATIONTYPE_TableValidation.equals(adRef.getValidationType()))
			{
				X_AD_Ref_Table tableRef = new X_AD_Ref_Table(ctx, AD_Reference_Value_ID, null);
				MTable refTable = MTable.get(ctx, tableRef.getAD_Table_ID());
				refTableName = refTable.getTableName();
			} else {
				if (log.isLoggable(Level.SEVERE)) log.severe("Unsupported AD_Reference.ValidationType" +
					" - AD_Reference_Value_ID=" +  AD_Reference_Value_ID +
					" - ValidationType=" + adRef.getValidationType());
			}
		}
		else //TODO: It is OK?
		{
			if (AD_Val_Rule_ID == 230) //C_BPartner (Trx)
			{
				refTableName = "C_BPartner";
			}
			else if (AD_Val_Rule_ID == 231) //M_Product (Trx)
			{
				refTableName = "M_Product";
			}
			else if (AD_Val_Rule_ID == 184)
			{
				refTableName = "M_Lot";
			}
			else if (AD_Val_Rule_ID == 272)
			{
				refTableName = "C_Order";
			}
			else if (AD_Val_Rule_ID == 220)
			{
				refTableName = "C_Invoice";
			}
			else if (AD_Val_Rule_ID == 218)
			{
				refTableName = "C_Order";
			}
                        else if (AD_Val_Rule_ID == 158)
                        {
                                refTableName = "AD_Role";
                        }
			else
				if (log.isLoggable(Level.SEVERE)) log.severe("Unsupported AD_Val_Rule_ID" +
							" - AD_Val_Rule_ID=" + AD_Val_Rule_ID +
							" - AD_Reference_Value_ID=" +  AD_Reference_Value_ID);
		}
		return refTableName;
	}

	public static final String DEFAULT_DATEFORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String IdValueSeperator = "#@#";


	/**
	 *
	 * 	findLookupIdValue
	 * 	ParentIdCol1#@#ParentIdCol2#@#100000#@#IdCol1#@#IdCol2
	 *	@param refTable
	 *	@param record_id
	 *	@return
	 */
	public static String findLookupIdValue(MTable refTable, int record_id, Properties ctx, CLogger log)
	{
		String resultIdValue = null;

		PO refPO = refTable.getPO(record_id, null);
		String refTableName = refTable.getTableName();
		POInfo poInfo = POInfo.getPOInfo(ctx, refTable.getAD_Table_ID(), refPO.get_TrxName());

		MColumn[] idcols = getIdentifierColumnsByTable(refTable.getAD_Table_ID(), ctx, log);
		for (int i = 0; i < idcols.length; i++)
		{
			MColumn idcol = idcols[i];

			if (log.isLoggable(Level.FINE)) log.fine(
				"Reference Table Name=" + refTableName +
				" - Identifier Columns(" + idcol.getSeqNo() + ")=" + idcol.getColumnName() +
				" - SeqNo=" + idcol.getSeqNo() +
				" - isIdentifier=" + idcol.isIdentifier() +
				" - isParent=" + idcol.isParent() +
				" - DisplayType=" + idcol.getAD_Reference_ID()) ;

			if (idcol.isParent())
				continue; //ignore the Identifier column when it is parent both!

			if (!isValidIdentifierColumn(refTableName, idcol))
				continue;

			Object value = refPO.get_ValueOfColumn(idcol.getAD_Column_ID());
			Class<?> c = poInfo.getColumnClass(poInfo.getColumnIndex(idcol.getAD_Column_ID()));

			if (refTableName.equals("AD_Org") && record_id == 0)
				value = "*";

			if (refTableName.equals("AD_User") && record_id == 0)
				value = "System";

			if (refTableName.equals("AD_Role") && record_id == 0)
				value = "System Administrator";

			if (value == null || value.equals (Null.NULL))
			{
				if (log.isLoggable(Level.FINE)) log.fine("Value is Null! " + " - Identifier Columns =" + idcol.getColumnName());
				resultIdValue = (resultIdValue == null? "" : resultIdValue + IdValueSeperator + "");
			}
			else if (c == Object.class)
				resultIdValue = (resultIdValue == null? value.toString() : resultIdValue + IdValueSeperator + value.toString());
			else if (value instanceof Integer || value instanceof BigDecimal)
				resultIdValue = (resultIdValue == null? value.toString() : resultIdValue + IdValueSeperator + value.toString());
			else if (c == Boolean.class)
				resultIdValue = (resultIdValue == null? value.toString() : resultIdValue + IdValueSeperator + value.toString());
			else if (value instanceof Timestamp)
			{
				Timestamp ts = (Timestamp)value;
				SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATEFORMAT_PATTERN);
				resultIdValue = (resultIdValue == null? sdf.format(ts) : resultIdValue + IdValueSeperator + sdf.format(ts));
			}
			else if (c == String.class)
				resultIdValue = (resultIdValue == null? (String)value : resultIdValue + IdValueSeperator + (String)value);
			else
				resultIdValue = (resultIdValue == null? value.toString() : resultIdValue + IdValueSeperator + value.toString());
		}

		MColumn[] pcols = getParentColumnsByTable(refTable.getAD_Table_ID(), ctx, log);
		for (int i = 0; i < pcols.length; i++)
		{
			MColumn pcol = pcols[i];
			if (log.isLoggable(Level.FINE)) log.fine("Reference Table Name=" + refTableName +
				" - Parent Columns(" + pcol.getSeqNo() + ")=" + pcol.getColumnName());

			String parentRefTableName = findLookupRefTableName(refTableName, pcol, ctx, log);
			MTable parentRefTable = MTable.get(ctx, parentRefTableName);
			Object value = refPO.get_ValueOfColumn(pcol.getAD_Column_ID());
			int precord_id = Integer.valueOf(value.toString());
			String parentIdValue = findLookupIdValue(parentRefTable, precord_id, ctx, log);
			resultIdValue = parentIdValue + IdValueSeperator + resultIdValue;
		}
		return resultIdValue;
	}

	public static boolean isValidIdentifierColumn(String tableName, MColumn idcol)
	{
		if ((tableName.equals("C_Invoice") && !idcol.getColumnName().equals("DocumentNo"))
			|| (tableName.equals("M_InOut") && !idcol.getColumnName().equals("DocumentNo"))
			|| (tableName.equals("C_Order") && !idcol.getColumnName().equals("DocumentNo"))
			|| (tableName.equals("C_Payment") && !idcol.getColumnName().equals("DocumentNo"))
			|| (tableName.equals("M_Requisition") && !idcol.getColumnName().equals("DocumentNo"))
			)
			return false;

		return true;
	}

	public static String findLookupRefTableName(String tableName, MColumn col, Properties ctx, CLogger log)
	{
		String refTableName = null;
		String colName = col.getColumnName();

		if (col.getAD_Reference_ID() == DisplayType.Search)
		{
			//	support: C_BPartner, M_Product

			int AD_Reference_Value_ID = col.getAD_Reference_Value_ID();
			int AD_Val_Rule_ID = col.getAD_Val_Rule_ID();
			if (log.isLoggable(Level.FINE)) log.fine("AD_Reference_Value_ID=" + AD_Reference_Value_ID + " - AD_Val_Rule_ID=" + AD_Val_Rule_ID);
			if (AD_Reference_Value_ID == 0 && AD_Val_Rule_ID == 0) //TODO: pls check(C_Bank_ID)
			{
				refTableName = col.getColumnName().substring(0, col.getColumnName().indexOf("_ID"));
			}
			else
				refTableName = findLookupTableName(AD_Reference_Value_ID, AD_Val_Rule_ID, ctx, log);
		}
		else if (col.getAD_Reference_ID() == DisplayType.TableDir)
		{
			refTableName = colName.substring(0, colName.indexOf("_ID"));
		}
		else if (col.getAD_Reference_ID() == DisplayType.ID)
		{
			if ("AD_TreeNodeMM".equals(tableName) && "Node_ID".equals(colName))
			{
				refTableName = "AD_Menu";
			}
		}
		else //	other DisplayType
		{
		}
		return refTableName;
	}

	/**
	 *
	 * 	getParentColumns
	 *	@return
	 */
	public static MColumn[] getParentColumnsByTable(int AD_Table_ID, Properties ctx, CLogger log)
	{
		String sql = "SELECT * FROM AD_Column WHERE AD_Table_ID=? and isParent = 'Y' order by seqNo, ColumnName";
		ArrayList<MColumn> list = new ArrayList<MColumn>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, AD_Table_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MColumn (ctx, rs, null));
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		//
		MColumn[] pcols = new MColumn[list.size ()];
		list.toArray (pcols);
		return pcols;
	}

	/**
	 *
	 * 	getIdentifierColumns
	 *	@return
	 */
	public static MColumn[] getIdentifierColumnsByTable(int AD_Table_ID, Properties ctx, CLogger log)
	{
		String sql = "SELECT * FROM AD_Column WHERE AD_Table_ID=? and isIdentifier = 'Y' order by seqNo";
		ArrayList<MColumn> list = new ArrayList<MColumn>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, AD_Table_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MColumn (ctx, rs, null));
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		//
		MColumn[] idcols = new MColumn[list.size ()];
		list.toArray (idcols);
		return idcols;
	}

	/**
	 * 	Get POs Class Instance
	 *
	 * 	@param whereClause optional where clause
	 *	@param orderClause optional order by
	 *	@param trxName transaction
	 *	@return PO for Record or null
	 */
	public static PO[] getPOsByQuery (MTable table, String whereClause, String orderClause, CLogger log)
	{
		ArrayList<PO> list = new ArrayList<PO>();
		String sql = "SELECT * FROM " + table.getTableName();
		if (whereClause != null && whereClause.length() > 0)
			sql += " WHERE " + whereClause;
		if (orderClause != null && orderClause.length() > 0)
			sql += " ORDER BY " + orderClause;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				PO po = table.getPO(rs, null);
				list.add (po);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}

		PO[] retValue = new PO[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getPOs

}
