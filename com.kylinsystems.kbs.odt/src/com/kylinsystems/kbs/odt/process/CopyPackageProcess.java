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

package com.kylinsystems.kbs.odt.process;

import java.util.logging.Level;

import org.compiere.model.MEntityType;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.AdempiereUserError;

import com.kylinsystems.kbs.odt.model.MKSODTObjectData;
import com.kylinsystems.kbs.odt.model.MKSODTObjectDataLine;
import com.kylinsystems.kbs.odt.model.MKSODTPackage;
import com.kylinsystems.kbs.odt.model.MKSODTVersion;

public class CopyPackageProcess extends SvrProcess
{
	private int p_KS_ODTPackageTo_ID = 0;
	private int p_KS_ODTPackageFrom_ID = 0;
	private String p_EntityType;
	private boolean p_IsNewUUID = true;

	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() ==null)
				;
			else if ("KS_ODTPackage_ID".equals(name))
				p_KS_ODTPackageFrom_ID = para[i].getParameterAsInt();
			else if ("EntityType".equals(name))
				p_EntityType = para[i].getParameterAsString();
			else if ("IsNewUUID".equals(name))
				p_IsNewUUID = para[i].getParameterAsBoolean();
			else
				if (log.isLoggable(Level.INFO)) log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		p_KS_ODTPackageTo_ID = getRecord_ID();
	}

	@Override
	protected String doIt() throws Exception
	{
		return "Not implemented yet!";

//		if (log.isLoggable(Level.INFO)) log.info("doIt - " +
//				"To KS_ODTPackage_ID=" + p_KS_ODTPackageTo_ID +
//				", From=" + p_KS_ODTPackageFrom_ID +
//				", EntityType=" + p_EntityType);
//		MKSODTPackage fromODTPkg = new MKSODTPackage(getCtx(), p_KS_ODTPackageFrom_ID, get_TrxName());
//		if (fromODTPkg.get_ID() == 0)
//			throw new AdempiereUserError("@NotFound@ (from->) @p_KS_ODTPackageFrom_ID@");
//		MKSODTPackage toODTPkg = new MKSODTPackage(getCtx(), p_KS_ODTPackageTo_ID, get_TrxName());
//		if (toODTPkg.get_ID() == 0)
//			throw new AdempiereUserError("@NotFound@ (to<-) @p_KS_ODTPackageTo_ID@");
//
//		MKSODTVersion fromODTVer = fromODTPkg.getNewestVersion();
//		if (fromODTVer == null)
//			throw new AdempiereUserError("@NotFound@ (From ODTVersion) @KS_ODTVersion_ID@");
//
//		MEntityType entityType = MEntityType.get(getCtx(), p_EntityType);
//
//		MKSODTVersion toODTVer = toODTPkg.getNewestVersion();

//
//		int dataCount = 0;
//		int dataLineCount = 0;
//

//		if (newVersion != null)
//		{
//			// Copy ODT data & data line
//			for (MKSODTObjectData oldData : oldVersion.getODTObjectDatas())
//			{
//				MKSODTObjectData newData = new MKSODTObjectData(newVersion, oldData);
//
//				if ("AD_EntityType".equals(newData.getAD_Table().getTableName())
//						continue; // ignore AD_EntityType Object
//
//				if (newData.save())
//				{
//					dataCount++;
//					for (MKSODTObjectDataLine oldDataLine : oldData.getODTObjectDataLines())
//					{
//						MKSODTObjectDataLine newDataLine = new MKSODTObjectDataLine(newData, oldDataLine);
//						// TODO Change Entity Type
//						newDataLine.
//						if (newDataLine.save())
//						{
//							dataLineCount++;
//						}
//						else
//							throw new AdempiereUserError("@Error@ @KS_ODTObjectDataLine_ID@");
//					}
//				}
//				else
//					throw new AdempiereUserError("@Error@ @KS_ODTObjectData_ID@");
//			}
//		}
//		else
//			throw new AdempiereUserError("@Error@ @KS_ODTVersion_ID@");
//
//		StringBuilder msgreturn = new StringBuilder("@Copied@ #").append(dataCount).append("/").append(dataLineCount);
//		return msgreturn.toString();
	}
}
