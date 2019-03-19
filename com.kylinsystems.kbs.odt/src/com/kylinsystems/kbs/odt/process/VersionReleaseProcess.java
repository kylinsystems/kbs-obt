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

import org.compiere.process.SvrProcess;
import org.compiere.util.AdempiereSystemError;

import com.kylinsystems.kbs.odt.model.MKSODTVersion;

public class VersionReleaseProcess extends SvrProcess
{
	private int p_ODTVersion_ID = 0;

	private MKSODTVersion odtVersion;

	@Override
	protected void prepare()
	{
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
			//odtVersion.versionRefresh();
		}
		catch (Exception e)
		{
			if (log.isLoggable(Level.SEVERE)) log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw e;
		}

		return "#" + "ODT Version Release Successful!";
	}
}
