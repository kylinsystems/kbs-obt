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

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.AdempiereSystemError;

import com.kylinsystems.kbs.odt.model.MKSODTVersion;
             
public class LinkEntityTypeProcess extends SvrProcess
{
	/** Entity Type			*/
	private String	p_EntityType = "C";	//	ENTITYTYPE_Customization

	private int p_ODTVersion_ID = 0;

	private MKSODTVersion odtVersion;

	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("EntityType"))
				p_EntityType = (String)para[i].getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}

		p_ODTVersion_ID = getRecord_ID();
	}

	@Override
	protected String doIt() throws Exception
	{
		if (p_ODTVersion_ID == 0)
			throw new AdempiereSystemError("@NotFound@ @KS_ODTVersion_ID@ " + p_ODTVersion_ID);
		if (log.isLoggable(Level.INFO)) log.info("doIt - KS_ODTVersion_ID=" + p_ODTVersion_ID);

		try {
			odtVersion = new MKSODTVersion(getCtx(), p_ODTVersion_ID, get_TrxName());
			odtVersion.linkEntityType(p_EntityType);
		}
		catch (Exception e)
		{
			if (log.isLoggable(Level.SEVERE)) log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw e;
		}

		return "#" + "ODT Version Link EntityType Successful!";
	}

}
