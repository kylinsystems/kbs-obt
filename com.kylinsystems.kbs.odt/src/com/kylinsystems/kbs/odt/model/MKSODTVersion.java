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
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.I_AD_ToolBarButton;
import org.compiere.model.Lookup;
import org.compiere.model.MColumn;
import org.compiere.model.MLookup;
import org.compiere.model.MTable;
import org.compiere.model.Null;
import org.compiere.model.PO;
import org.compiere.model.POInfo;
import org.compiere.model.POInfoColumn;
import org.compiere.model.Query;
import org.compiere.model.X_AD_Column;
import org.compiere.model.X_AD_EntityType;
import org.compiere.model.X_AD_TreeNodeMM;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Trx;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MKSODTVersion extends X_KS_ODTVersion
{
	/**
	 *
	 */
	private static final long serialVersionUID = -3867829385607097983L;

	private CLogger log = CLogger.getCLogger(MKSODTVersion.class);

	public MKSODTVersion(Properties ctx, int KS_ODTVersion_ID, String trxName) {
		super(ctx, KS_ODTVersion_ID, trxName);
	}

	public MKSODTVersion(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public MKSODTVersion(MKSODTPackage parent, MKSODTVersion from) {
		this (parent.getCtx(), 0, parent.get_TrxName());
		copyValues(from, this);
		setClientOrg(parent);
		setKS_ODTPackage_ID(parent.getKS_ODTPackage_ID());
	}

	public List<MKSODTObjectData> getODTObjectDatas()
	{
		String whereClause = "KS_ODTVersion_ID=?";
		List<MKSODTObjectData> list = new Query(getCtx(), X_KS_ODTObjectData.Table_Name, whereClause, get_TrxName())
											.setParameters(get_ID())
											.setOrderBy(X_KS_ODTObjectData.COLUMNNAME_SeqNo)
											.list();
		return list;
	}



	public void packageInstall()
	{
		I_KS_ODTPackage odtPackage = getKS_ODTPackage();
		if (log.isLoggable(Level.INFO)) log.info("Package Install, ODTPackage:" + odtPackage.getName()
				//+ "|EntityType:" + odtPackage.getEntityType()
				+ "|ObjectType:" + odtPackage.getObjectType());

		int Syn_AD_Column_ID = 0;
		List<MKSODTObjectData> odtods = getODTObjectDatas();
		for (MKSODTObjectData odtod : odtods)
		{
			if (log.isLoggable(Level.FINE)) log.fine("Apply ObjectData, Name:" + odtod.getName());

			// generate PO
			PO po = null;

			MTable table = MTable.get(getCtx(), odtod.getAD_Table_ID());

			po = Utils.generatePO(odtod.getObjectData_UUID(), table);

			if (po.get_ID() != 0)
				odtod.setObjectData_Action(X_KS_ODTObjectData.OBJECTDATA_ACTION_Update);
			else
				odtod.setObjectData_Action(X_KS_ODTObjectData.OBJECTDATA_ACTION_Insert);
			odtod.saveEx();

			// generate PO value
			List<MKSODTObjectDataLine> odtodls = odtod.getODTObjectDataLines();
			for (MKSODTObjectDataLine odtodl : odtodls)
			{
				try {
					po = Utils.buildPO(po, odtodl.getAD_Column_ID(), odtodl.getNewID(), odtodl.getNewValue(), odtodl.isNewNullValue(), odtodl.getNewUUID(), table, getCtx(), log);
				} catch (Exception ex) {
					if (log.isLoggable(Level.SEVERE)) log.log(Level.SEVERE, "Failed to build PO", ex);
				}
			} // end of all ODT ODL

			if (po instanceof X_AD_TreeNodeMM && po.is_new())
			{
				// reload PO and reset value
				X_AD_TreeNodeMM poMM = (X_AD_TreeNodeMM)po;
				String whereClause_TNMM = "AD_Tree_ID=" + poMM.getAD_Tree_ID() + " AND Node_ID=" + poMM.getNode_ID();
				X_AD_TreeNodeMM reloadPO = (X_AD_TreeNodeMM)table.getPO(whereClause_TNMM, null);
				reloadPO.setSeqNo(poMM.getSeqNo());
				reloadPO.setParent_ID(poMM.getParent_ID());
				reloadPO.set_ValueNoCheck(reloadPO.getUUIDColumnName(), odtod.getObjectData_UUID());
				po = reloadPO;
			}

			po.saveEx();

			if (po instanceof X_AD_Column)
			{
				if (!table.isView()) {
					X_AD_Column mcol = (X_AD_Column)po;
					Utils.doSynchronizeColumn(mcol.getAD_Column_ID(), getCtx());
				}
			}
			
			// Apply SQL
			String applySQL = odtod.getSQL_Apply();
			if (applySQL != null & !"".equals(applySQL)) {
				String[] sqls = applySQL.split("--//--");
				
				int count = 0;
				PreparedStatement pstmt = null;
				for (String sql : sqls) {
					if (sql != null && !"".equals(sql)) {
						try {
							pstmt = DB.prepareStatement(sql, null);
							Statement stmt = null;
							try {
								stmt = pstmt.getConnection().createStatement();
								count = stmt.executeUpdate (sql);
								if (log.isLoggable(Level.INFO)) log.info("Executed SQL Statement for PostgreSQL: "+ sql + " ReturnValue="+count);
							} finally {
								DB.close(stmt);
								stmt = null;
							}
						} catch (Exception e) {
							log.log(Level.SEVERE,"SQLStatement", e);
						} finally {
							DB.close(pstmt);
							pstmt = null;
						}
					}
				}
			}
			
		} // end of all ODT OD

		Utils.postInstallPackage(getCtx());
	}

	public void packageUninstal()
	{

	}

	public void linkEntityType(String entityType) 
	{
		I_KS_ODTPackage odtPackage = getKS_ODTPackage();
		if (log.isLoggable(Level.INFO)) log.info("Version Link EntityType, ODTPackage:" + odtPackage.getName()
		+ "|EntityType:" + entityType
		+ "|ObjectType:" + odtPackage.getObjectType());
		
		// delete old OD & ODL of EntityType for this Version
		String sqlDeletOldODL_EntityType = "delete from KS_ODTObjectDataLine where KS_ODTObjectData_ID in " +
					"(select KS_ODTObjectData_ID from KS_ODTObjectData where KS_ODTVersion_ID = " + get_ID() + " and AD_Table_ID = 882)"; //882 = AD_EntityType
		DB.executeUpdate(sqlDeletOldODL_EntityType, get_TrxName());
		
		String sqlDeleteOldOD_EntityType = "delete from KS_ODTObjectData where KS_ODTVersion_ID = " + get_ID() + " and AD_Table_ID = 882";
		DB.executeUpdate(sqlDeleteOldOD_EntityType, get_TrxName());
	        
		
		// create new OD and ODL of EntityType for this Version
		String whereClause_entitytype = "EntityType ='" + entityType + "'";				
		exportObjectData("AD_EntityType", whereClause_entitytype, null);
		
		
		// update all ODL to new EntityType for this Version
		String updateOLD2NewEntityType = "update KS_ODTObjectDataLine " +
					"set NewValue = '" + entityType + "' " +
					"where KS_ODTObjectData_ID in " +
					"(select KS_ODTObjectData_ID from KS_ODTObjectData where KS_ODTVersion_ID = " + get_ID() +  " and AD_Table_ID <> 882) " + //882 = AD_EntityType
					"and AD_Column_ID in (select AD_Column_ID from AD_Column where AD_Element_ID = 1682)"; //1682 = EntityType Element
		DB.executeUpdate(updateOLD2NewEntityType, get_TrxName());
	}

	public void versionRefresh(String entityType)
	{
		// delete old OD and ODL from this Version
		String sqlDeleteODL = "Delete from KS_ODTObjectDataLine where KS_ODTObjectData_ID in " +
								"( " +
								"select KS_ODTObjectData_ID from KS_ODTObjectData where ObjectData_Type = 'AD Object' and KS_ODTVersion_ID =  " + get_ID() +
								")";
		DB.executeUpdate(sqlDeleteODL, null);

		String sqlDeleteOD = "Delete from KS_ODTObjectData where ObjectData_Type = 'AD Object' and KS_ODTVersion_ID = " + get_ID();
		DB.executeUpdate(sqlDeleteOD, null);

		// create new OD and ODL
		I_KS_ODTPackage odtPackage = getKS_ODTPackage();
		//String entityType = odtPackage.getEntityType();
		String whereClause_Client = "AD_Client_ID = " + odtPackage.getAD_Client_ID();
		String whereClause_entitytype = "EntityType ='" + entityType + "'";
		String whereClause = null;

		if (log.isLoggable(Level.INFO)) log.info("Version Refresh, ODTPackage:" + odtPackage.getName()
				+ "|EntityType:" + entityType
				+ "|ObjectType:" + odtPackage.getObjectType());

		exportObjectData("AD_EntityType", whereClause_entitytype, null);
		exportObjectData("AD_SysConfig", whereClause_entitytype, null);
		exportObjectData("AD_Message", whereClause_entitytype, null);
		exportObjectData("AD_Window", whereClause_entitytype, null);
		exportObjectData("AD_Form", whereClause_entitytype, null);
		exportObjectData("AD_Element", whereClause_entitytype, null);
		exportObjectData("AD_Val_Rule", whereClause_entitytype, null);
		exportObjectData("AD_Reference", whereClause_entitytype, null);
		exportObjectData("AD_Ref_List", whereClause_entitytype, null);
		exportObjectData("AD_Ref_Table", whereClause_entitytype, null);
		exportObjectData("AD_Process", whereClause_entitytype, null);
		exportObjectData("AD_Process_Para", whereClause_entitytype, null);
		exportObjectData("AD_Table", whereClause_entitytype, null);
		exportObjectData("AD_Column", whereClause_entitytype, "AD_Table_ID, isKey desc");
		exportObjectData("AD_Tab", whereClause_entitytype, "AD_Window_ID, seqNo");
		exportObjectData("AD_FieldGroup", whereClause_entitytype, null);
		exportObjectData("AD_Field", whereClause_entitytype, "AD_Tab_ID, seqNo");
		exportObjectData(I_AD_ToolBarButton.Table_Name, whereClause_entitytype, null);
		exportObjectData("AD_Menu", whereClause_entitytype, null);
		whereClause = "Node_ID in ("
			+ "select AD_Menu_ID from AD_Menu "
			+ "where " + whereClause_entitytype
			+ ")"
            + " And " + whereClause_Client;
		exportObjectData("AD_TreeNodeMM", whereClause, null);
	}

	private void exportObjectData(String tableName, String whereClause, String orderClause)
	{
		if (log.isLoggable(Level.FINE)) log.fine("Export OD, TableName:" + tableName + "|WhereClause:" + whereClause + "|OrderClause:" + orderClause);

		MTable table = MTable.get(getCtx(), tableName);
		PO[] pos = Utils.getPOsByQuery(table, whereClause, orderClause, log);
		for (int i = 0; i < pos.length; i++)
		{
			PO po = pos[i];
			// ODTObjectData
			X_KS_ODTObjectData newod = new X_KS_ODTObjectData(getCtx(), 0, null);
			newod.setKS_ODTVersion_ID(get_ID());
			newod.setName(tableName + "-" + po.get_ID());
			newod.setObjectData_Type(X_KS_ODTObjectData.OBJECTDATA_TYPE_ADObject);
			newod.setObjectData_Status(X_KS_ODTObjectData.OBJECTDATA_STATUS_Applied);
			newod.setObjectData_Action(X_KS_ODTObjectData.OBJECTDATA_ACTION_NA);
			newod.setAD_Table_ID(table.get_ID());
			newod.setRecord_ID(po.get_ID());
			int seqNo = 0;
			seqNo = DB.getSQLValue(get_TrxName(), "SELECT NVL(MAX(SeqNo),0)+10 AS DefaultValue FROM KS_ODTObjectData WHERE ObjectData_Type = 'AD Object' and KS_ODTVersion_ID=" + get_ID());
			newod.setSeqNo(seqNo);
			if (X_AD_EntityType.Table_Name.equalsIgnoreCase(tableName)) {
				newod.setSeqNo(10); // fixed to 10 for EntityType
			}
			
			String uuidColumnName = po.getUUIDColumnName();
			if (uuidColumnName == null) {
				if (log.isLoggable(Level.SEVERE)) log.severe("PO UUID Column Name is null" + "|TableName:" + tableName + "|PO ID:" + po.get_ID());
			}
			
			Object poValue = po.get_Value(uuidColumnName);
			if (poValue == null) {
				if (log.isLoggable(Level.SEVERE)) log.severe("PO Value is null" + "|TableName:" + tableName + "|PO ID:" + po.get_ID() + "|UUID Column Name:" + uuidColumnName);
			}
			
			newod.setObjectData_UUID(poValue.toString());
			newod.saveEx();

			// ODTObjectDataLine
			exportObjectDataLine(po, newod.get_ID());
		}
	}

	private void exportObjectDataLine(PO po, int newodID) {
		POInfo poInfo = POInfo.getPOInfo(po.getCtx(), po.get_Table_ID(), po.get_TrxName());
		int size = poInfo.getColumnCount();
		for (int j = 0; j < size; j++)
		{
			if (poInfo.isVirtualColumn(j))
				continue;

			Properties ctx = getCtx();
			//POInfoColumn infoCol = poInfo.getColumn(j);
			Object value = po.get_Value(j);
			String columnName = poInfo.getColumnName(j);
			Class<?> c = poInfo.getColumnClass(j);
			int columnDisplayType = poInfo.getColumnDisplayType(j);
			String refTableName = null;
			String uuid = null;

			if (log.isLoggable(Level.FINE)) log.fine("Export ODL, ColumnName:" + columnName + "|Value:" + value);

			if (columnDisplayType == DisplayType.TableDir)
			{
				refTableName = columnName.substring(0, columnName.indexOf("_ID"));
			}
			else if (columnDisplayType == DisplayType.Table)
			{
				Lookup lookup = poInfo.getColumnLookup(j);
				MLookup mLookup = null;
				if (lookup != null) {
					mLookup = (MLookup)lookup;
				}
				int AD_Reference_Value_ID = 0;
				if (mLookup != null) {
					AD_Reference_Value_ID = mLookup.getAD_Reference_Value_ID();
				}
				//int AD_Reference_Value_ID = infoCol.AD_Reference_Value_ID;
				int AD_Val_Rule_ID = 0;
				if (log.isLoggable(Level.FINE)) log.fine("AD_Reference_Value_ID=" + AD_Reference_Value_ID + " - AD_Val_Rule_ID=" + AD_Val_Rule_ID);
				refTableName = Utils.findLookupTableName(AD_Reference_Value_ID, AD_Val_Rule_ID, ctx, log);
			}
			else if (columnDisplayType == DisplayType.Search)
			{
				MTable stable = MTable.get(ctx, po.get_Table_ID());
				MColumn scolumn = stable.getColumn(columnName);
				int AD_Reference_Value_ID = scolumn.getAD_Reference_Value_ID();
				int AD_Val_Rule_ID = scolumn.getAD_Val_Rule_ID();
				if (log.isLoggable(Level.FINE)) log.fine("AD_Reference_Value_ID=" + AD_Reference_Value_ID + " - AD_Val_Rule_ID=" + AD_Val_Rule_ID);
				if (AD_Reference_Value_ID == 0 && AD_Val_Rule_ID == 0) //TODO: pls check
				{
					refTableName = columnName.substring(0, columnName.indexOf("_ID"));
				}
				else
					refTableName = Utils.findLookupTableName(AD_Reference_Value_ID, AD_Val_Rule_ID, ctx, log);
			}
			else if (columnDisplayType == DisplayType.ID)
			{
				// TODO:hardcode
				if ("AD_TreeNodeMM".equals(po.get_TableName())
					&& ("Node_ID".equals(columnName) || "Parent_ID".equals(columnName)))
				{
					refTableName = "AD_Menu";
				}
				else if (poInfo.isKey(j))
				{
					// skip, it is ok
				}
				else
					//	TODO: M_Product.C_SubscriptionType_ID
					if (log.isLoggable(Level.WARNING)) log.warning("DisplayType is ID, Unsupported");
			}
			else if (columnDisplayType == DisplayType.PAttribute)
			{
				//	TODO: M_Product.M_AttributeSetInstance_ID
				if (log.isLoggable(Level.WARNING)) log.warning("DisplayType is PAttribute, Unsupported");
			}
			else if (columnDisplayType == DisplayType.Locator)
			{
				refTableName = "M_Locator";
			}
			else if (columnDisplayType == DisplayType.Location)
			{
				refTableName = "C_Location";
			}
			else //	other DisplayType
			{
				// no refTableName
			}

			X_KS_ODTObjectDataLine newodl = new X_KS_ODTObjectDataLine(getCtx(), 0, null);
			newodl.setKS_ODTObjectData_ID(newodID);
			newodl.setAD_Column_ID(poInfo.getAD_Column_ID(columnName));

			if (refTableName != null)
			{
				MTable refTable = MTable.get(ctx, refTableName);

				if (value != null && !value.equals(Null.NULL) && value instanceof Integer)
				{
					int record_id = ((Integer)value).intValue();
					if (record_id == 0 && "AD_User".equals(refTableName)) {
						// hardcode to SuperUser
						value = Integer.valueOf(100);
						record_id = 100;
					}
					if (record_id == -1 && "AD_Menu".equals(refTableName)) {
						// hardcode to Top Menu
						value = Integer.valueOf(0);
						record_id = 0;
					}

					if (log.isLoggable(Level.FINE)) log.fine("Ref Table:" + refTableName + "|ID:" + record_id);

					if (record_id == 0
							&& !"AD_Client".equals(refTableName)
							&& !"AD_Org".equals(refTableName))
					{
						if (log.isLoggable(Level.FINE)) log.fine("id is 0");
					}

					String idValue = Utils.findLookupIdValue(refTable, record_id, ctx, log);

					c = String.class;
					value = idValue;

					PO refPO = refTable.getPO(record_id, get_TrxName());

					if ("AD_Client".equals(refTableName) && record_id == 0) {
						uuid = "11237b53-9592-4af1-b3c5-afd216514b5d"; // System Client
					} else if ("AD_Org".equals(refTableName) && record_id == 0) {
						uuid = "3ef41ffc-8ea9-454a-afa2-22949f402ff5"; // * Org
					} else if ("AD_Menu".equals(refTableName) && record_id == 0) {
						uuid = null; // Main Menu
					} else {
						uuid = refPO.get_Value(refPO.getUUIDColumnName()).toString();
					}

					newodl.setNewID(record_id);
					newodl.setNewUUID(uuid);
				}
				else
				{
					if (log.isLoggable(Level.FINE)) log.fine("Ref Table " + refTableName + ", value is null");
				}
			}

			if (value == null || Null.NULL.equals(value))
				newodl.setIsNewNullValue(true);
			else if (c == Object.class)
				newodl.setNewValue(value.toString());
			else if (value instanceof Integer || value instanceof BigDecimal)
				newodl.setNewValue(value.toString());
			else if (c == Boolean.class)
			{
				boolean bValue = false;
				if (value instanceof Boolean)
					bValue = ((Boolean)value).booleanValue();
				else
					bValue = "Y".equals(value);
				newodl.setNewValue(bValue ? "Y" : "N");
			}
			else if (value instanceof Timestamp)
			{
				Timestamp ts = (Timestamp)value;
				SimpleDateFormat sdf = new SimpleDateFormat(Utils.DEFAULT_DATEFORMAT_PATTERN);
				newodl.setNewValue(sdf.format(ts));
			}
			else if (c == String.class)
				newodl.setNewValue((String)value);
//				else if (DisplayType.isLOB(dt))
//					col.appendChild(document.createCDATASection(value.toString()));
			else
				newodl.setNewValue(value.toString());

			newodl.saveEx();
		} // end of all columns
	}

	public Node toXmlNode(Document document)
	{
		Element elemntodtversion = document.createElement("ODTVersion");
		elemntodtversion.setAttribute("ID", String.valueOf(get_ID()));
		elemntodtversion.setAttribute("UUID", get_Value(getUUIDColumnName()).toString());
		elemntodtversion.setAttribute(COLUMNNAME_VersionNo, String.valueOf(getVersionNo()));
		elemntodtversion.setAttribute(COLUMNNAME_Version_Status, getVersion_Status());

		Element elemnt = null;
		elemnt = document.createElement(COLUMNNAME_Name);
		elemnt.appendChild(document.createCDATASection(getName() == null ? "":getName().trim()));
		elemntodtversion.appendChild(elemnt);

		elemnt = document.createElement(COLUMNNAME_Description);
		elemnt.appendChild(document.createCDATASection(getDescription() == null ? "":getDescription().trim()));
		elemntodtversion.appendChild(elemnt);

		elemnt = document.createElement(COLUMNNAME_SystemVersion);
		elemnt.appendChild(document.createCDATASection(getSystemVersion() == null ? "":getSystemVersion()));
		elemntodtversion.appendChild(elemnt);

		for (MKSODTObjectData odtod : getODTObjectDatas())
		{
			elemntodtversion.appendChild(odtod.toXmlNode(document));
		}

		return elemntodtversion;
	}

	public static MKSODTVersion fromXmlNode(Element element, int ODTPackage_ID, Properties ctx)
	{
		int id = Integer.valueOf(element.getAttribute("ID"));
		String uuid = element.getAttribute("UUID");
		String VersionNo = element.getAttribute(COLUMNNAME_VersionNo);
		String Version_Status = element.getAttribute(COLUMNNAME_Version_Status);
		Node name = (Element)element.getElementsByTagName(COLUMNNAME_Name).item(0);
		Node description = (Element)element.getElementsByTagName(COLUMNNAME_Description).item(0);
		Node SystemVersion = (Element)element.getElementsByTagName(COLUMNNAME_SystemVersion).item(0);

		String where = getUUIDColumnName(Table_Name) + " = '" + uuid + "'";
		MKSODTVersion odtversion = new Query(ctx, X_KS_ODTVersion.Table_Name, where, null).firstOnly();
		if (odtversion == null) {
			odtversion = new MKSODTVersion(ctx, 0, null);
		}

		odtversion.setKS_ODTPackage_ID(ODTPackage_ID);
		odtversion.setVersionNo(Integer.valueOf(VersionNo));
		odtversion.setVersion_Status(Version_Status);
		odtversion.setName(name.getTextContent().trim());
		odtversion.setDescription(description.getTextContent().trim());
		odtversion.setSystemVersion(SystemVersion.getTextContent());
		odtversion.set_ValueNoCheck(odtversion.getUUIDColumnName(), uuid);

		return odtversion;
	}

	public static void importFromXmlNode(MKSODTPackage odtpackage, Element element)
	{
		MKSODTVersion odtversion = MKSODTVersion.fromXmlNode(element, odtpackage.get_ID(), odtpackage.getCtx());
		odtversion.saveEx();

		NodeList children = element.getElementsByTagName("ODTObjectData");
		for (int i = 0; i < children.getLength(); i++)
		{
			Element elemntOD = (Element)children.item(i);
			MKSODTObjectData.importFromXmlNode(odtversion, elemntOD);
		}
	}
 }
