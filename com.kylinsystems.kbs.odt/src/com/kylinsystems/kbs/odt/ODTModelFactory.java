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

package com.kylinsystems.kbs.odt;

import java.util.logging.Level;

import org.adempiere.base.DefaultModelFactory;
import org.adempiere.base.IModelFactory;
import org.compiere.model.MEntityType;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Util;

/**
 * KSYS model factory implementation.
 */
public class ODTModelFactory extends DefaultModelFactory implements IModelFactory {

	private static CCache<String,Class<?>> s_classCache = new CCache<String,Class<?>>(null, "PO_Class", 20, false);
	private final static CLogger s_log = CLogger.getCLogger(ODTModelFactory.class);

	/* (non-Javadoc)
	 * @see org.adempiere.base.IModelFactory#getClass(java.lang.String)
	 */
	@Override
	public Class<?> getClass(String tableName) {
		//	Not supported
		if (tableName == null || tableName.endsWith("_Trl"))
			return null;

		//check cache
		Class<?> cache = s_classCache.get(tableName);
		if (cache != null)
		{
			//Object.class indicate no generated PO class for tableName
			if (cache.equals(Object.class))
				return null;
			else
				return cache;
		}

		MTable table = MTable.get(Env.getCtx(), tableName);
		String entityType = table.getEntityType();

		if (entityType.startsWith("KSYS"))
		{
			MEntityType et = MEntityType.get(Env.getCtx(), entityType);
			String etmodelpackage = et.getModelPackage();
			if (etmodelpackage != null)
			{
				Class<?> clazz = null;
				clazz = getPOclass(etmodelpackage + ".M" + Util.replace(tableName, "_", ""), tableName);
				if (clazz != null) {
					s_classCache.put(tableName, clazz);
					return clazz;
				}
				clazz = getPOclass(etmodelpackage + ".X_" + tableName, tableName);
				if (clazz != null) {
					s_classCache.put(tableName, clazz);
					return clazz;
				}
				s_log.warning("No class for table with it entity: " + tableName);
			}
		}

		//Object.class to indicate no PO class for tableName
		s_classCache.put(tableName, Object.class);
		return null;
	}

	/**
	 * Get PO class
	 * @param className fully qualified class name
	 * @param tableName Optional. If specified, the loaded class will be validated for that table name
	 * @return class or null
	 */
	private Class<?> getPOclass (String className, String tableName)
	{
		try
		{
			Class<?> clazz = Class.forName(className);
			// Validate if the class is for specified tableName
			if (tableName != null)
			{
				String classTableName = clazz.getField("Table_Name").get(null).toString();
				if (!tableName.equals(classTableName))
				{
					if (s_log.isLoggable(Level.FINEST)) s_log.finest("Invalid class for table: " + className+" (tableName="+tableName+", classTableName="+classTableName+")");
					return null;
				}
			}
			//	Make sure that it is a PO class
			Class<?> superClazz = clazz.getSuperclass();
			while (superClazz != null)
			{
				if (superClazz == PO.class)
				{
					if (s_log.isLoggable(Level.FINE)) s_log.fine("Use: " + className);
					return clazz;
				}
				superClazz = superClazz.getSuperclass();
			}
		}
		catch (Exception e)
		{
		}
		if (s_log.isLoggable(Level.FINEST)) s_log.finest("Not found: " + className);
		return null;
	}	//	getPOclass

}
