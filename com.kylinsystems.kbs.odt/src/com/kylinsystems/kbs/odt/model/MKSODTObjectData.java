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

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

import org.compiere.model.Query;
import org.compiere.util.Trx;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MKSODTObjectData extends X_KS_ODTObjectData
{
	/**
	 *
	 */
	private static final long serialVersionUID = -8160311609286185674L;

	public MKSODTObjectData(Properties ctx, int KS_ODTObjectData_ID,
			String trxName) {
		super(ctx, KS_ODTObjectData_ID, trxName);
	}


	public MKSODTObjectData(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public MKSODTObjectData(MKSODTVersion parent, MKSODTObjectData from) {
		this (parent.getCtx(), 0, parent.get_TrxName());
		copyValues(from, this);
		setClientOrg(parent);
		setKS_ODTVersion_ID(parent.getKS_ODTVersion_ID());
	}

	public List<MKSODTObjectDataLine> getODTObjectDataLines()
	{
		String whereClause = "KS_ODTObjectData_ID=?";
		List<MKSODTObjectDataLine> list = new Query(getCtx(), X_KS_ODTObjectDataLine.Table_Name, whereClause, get_TrxName())
											.setParameters(get_ID())
											.list();
		return list;
	}

	public Node toXmlNode(Document document)
	{
		Element elemntodtod = document.createElement("ODTObjectData");
		elemntodtod.setAttribute("ID", String.valueOf(get_ID()));
		elemntodtod.setAttribute("UUID", get_Value(getUUIDColumnName()).toString());
		elemntodtod.setAttribute(COLUMNNAME_ObjectData_Type, getObjectData_Type());
		elemntodtod.setAttribute(COLUMNNAME_ObjectData_Status, OBJECTDATA_STATUS_Unapplied);
		elemntodtod.setAttribute(COLUMNNAME_ObjectData_Action, OBJECTDATA_ACTION_NA);
		elemntodtod.setAttribute(COLUMNNAME_SeqNo, String.valueOf(getSeqNo()));
		elemntodtod.setAttribute(COLUMNNAME_ObjectData_UUID, getObjectData_UUID());
		elemntodtod.setAttribute(COLUMNNAME_AD_Table_ID, String.valueOf(getAD_Table_ID()));
		elemntodtod.setAttribute(COLUMNNAME_Record_ID, String.valueOf(getRecord_ID()));

		Element elemnt = null;
		elemnt = document.createElement(COLUMNNAME_Name);
		elemnt.appendChild(document.createCDATASection(getName() == null? "":getName()));
		elemntodtod.appendChild(elemnt);

		elemnt = document.createElement(COLUMNNAME_SQL_Apply);
		elemnt.appendChild(document.createCDATASection(getSQL_Apply() == null? "":getSQL_Apply()));
		elemntodtod.appendChild(elemnt);

		elemnt = document.createElement(COLUMNNAME_SQL_Unapply);
		elemnt.appendChild(document.createCDATASection(getSQL_Unapply() == null? "":getSQL_Unapply()));
		elemntodtod.appendChild(elemnt);

		elemnt = document.createElement(COLUMNNAME_MessgeLog);
		elemnt.appendChild(document.createCDATASection(getMessgeLog() == null? "":getMessgeLog()));
		elemntodtod.appendChild(elemnt);

		for (MKSODTObjectDataLine odtodl : getODTObjectDataLines())
		{
			elemntodtod.appendChild(odtodl.toXmlNode(document));
		}

		return elemntodtod;
	}

	public static MKSODTObjectData fromXmlNode(Element element, int ODTVersion_ID, Properties ctx)
	{
		int id = Integer.valueOf(element.getAttribute("ID"));
		String uuid = element.getAttribute("UUID");
		String AD_Table_ID = element.getAttribute(COLUMNNAME_AD_Table_ID);
		String ObjectData_Action = element.getAttribute(COLUMNNAME_ObjectData_Action);
		String ObjectData_Status = element.getAttribute(COLUMNNAME_ObjectData_Status);
		String ObjectData_Type = element.getAttribute(COLUMNNAME_ObjectData_Type);
		String ObjectData_UUID = element.getAttribute(COLUMNNAME_ObjectData_UUID);
		String Record_ID = element.getAttribute(COLUMNNAME_Record_ID);
		String SeqNo = element.getAttribute(COLUMNNAME_SeqNo);
		Node Name = (Element)element.getElementsByTagName(COLUMNNAME_Name).item(0);
		Node SQL_Apply = (Element)element.getElementsByTagName(COLUMNNAME_SQL_Apply).item(0);
		Node SQL_Unapply = (Element)element.getElementsByTagName(COLUMNNAME_SQL_Unapply).item(0);
		Node MessgeLog = (Element)element.getElementsByTagName(COLUMNNAME_MessgeLog).item(0);

		MKSODTObjectData odtod = null;
		odtod = new MKSODTObjectData(ctx, 0, null);
		odtod.setKS_ODTVersion_ID(ODTVersion_ID);
		odtod.setAD_Table_ID(Integer.valueOf(AD_Table_ID));
		odtod.setObjectData_Action(ObjectData_Action);
		odtod.setObjectData_Status(ObjectData_Status);
		odtod.setObjectData_Type(ObjectData_Type);
		odtod.setObjectData_UUID(ObjectData_UUID);
		odtod.setRecord_ID(Integer.valueOf(Record_ID));
		odtod.setSeqNo(Integer.valueOf(SeqNo));
		odtod.setName(Name.getTextContent());
		odtod.setSQL_Apply(SQL_Apply.getTextContent());
		odtod.setSQL_Unapply(SQL_Unapply.getTextContent());
		odtod.setMessgeLog(MessgeLog.getTextContent());
		odtod.set_ValueNoCheck(odtod.getUUIDColumnName(), uuid);

		return odtod;
	}


	public static void importFromXmlNode(MKSODTVersion odtversion, Element element)
	{
		MKSODTObjectData odtod = MKSODTObjectData.fromXmlNode(element, odtversion.get_ID(), odtversion.getCtx());
		odtod.saveEx();

		NodeList children = element.getElementsByTagName("ODTObjectDataLine");
		for (int i = 0; i < children.getLength(); i++)
		{
			Element elemntODL = (Element)children.item(i);
			MKSODTObjectDataLine.importFromXmlNode(odtod, elemntODL);
		}
	}
}
