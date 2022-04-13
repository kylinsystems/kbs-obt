/******************************************************************************
 * Copyright (C) 2016-2020 ken.longnan@gmail.com                                   *
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.adempiere.plugin.utils.AbstractActivator;
import org.adempiere.plugin.utils.Version;
import org.adempiere.util.ServerContext;
import org.compiere.Adempiere;
import org.compiere.model.MClient;
import org.compiere.model.MSession;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.model.ServerStateChangeEvent;
import org.compiere.model.ServerStateChangeListener;
import org.compiere.model.X_AD_Column;
import org.compiere.model.X_AD_Package_Imp;
import org.compiere.model.X_AD_Package_Imp_Proc;
import org.compiere.model.X_AD_TreeNodeMM;
import org.compiere.util.AdempiereSystemError;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;
import org.osgi.framework.BundleContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.kylinsystems.kbs.odt.model.MKSODTPackage;
import com.kylinsystems.kbs.odt.model.Utils;
import com.kylinsystems.kbs.odt.model.X_KS_ODTObjectData;
import com.kylinsystems.kbs.odt.model.X_KS_ODTObjectDataLine;
import com.kylinsystems.kbs.odt.model.X_KS_ODTPackage;
import com.kylinsystems.kbs.odt.model.X_KS_ODTVersion;

public class ODT2PackActivator extends AbstractActivator {
	protected final static CLogger logger = CLogger.getCLogger(ODT2PackActivator.class.getName());
	private File currentFile;
	private HashMap<String, URL> file2urlMap = new HashMap<String, URL>();
	
	public String getName() {
		if (currentFile != null)
			return currentFile.getName();
		else
			return context.getBundle().getSymbolicName();
	}

	@Override
	public String getVersion() {
		String version = (String) context.getBundle().getHeaders().get("Bundle-Version");
		// e.g. 1.0.0.qualifier, check only the "1.0.0" part
		String[] components = version.split("[.]");
		StringBuilder versionBuilder = new StringBuilder(components[0]);
		if (components.length >= 3) {
			versionBuilder.append(".").append(components[1]).append(".").append(components[2]);
		} else if (components.length == 2) {
			versionBuilder.append(".").append(components[1]).append(".0");
		} else {
			versionBuilder.append(".0.0");
		}
		return versionBuilder.toString();
	}

	public String getDescription() {
		return getName();
	}
	
	protected void preInstallPackage() {
		// to be override in sub class
	}
	protected void postInstallPackage() {
		// to be override in sub class
	}

	private void installPackage() {
		logger.log(Level.WARNING, "Pre of Installing KBS OPTPackage from bundle:" + getName());
		preInstallPackage();
		
		logger.log(Level.WARNING, "Installing KBS OPTPackage from bundle:" + getName());

		String where = "Name=? AND PK_Status = 'Completed successfully'";
		Query q = new Query(Env.getCtx(), X_AD_Package_Imp.Table_Name,
				where.toString(), null);
		q.setParameters(new Object[] { getName() });
		List<X_AD_Package_Imp> pkgs = q.list();
		List<String> installedVersions = new ArrayList<String>();
		if (pkgs != null && !pkgs.isEmpty()) {
			for(X_AD_Package_Imp pkg : pkgs) {
				String packageVersionPart = pkg.getPK_Version();
				String[] part = packageVersionPart.split("[.]");
				if (part.length > 3 && (packageVersionPart.indexOf(".v") > 0 || packageVersionPart.indexOf(".qualifier") > 0)) {
					packageVersionPart = part[0]+"."+part[1]+"."+part[2];
				}
				installedVersions.add(packageVersionPart);				
			}
		}
		beforePackIn();
		packIn(installedVersions);
		packInFolder();
		afterPackIn();
		
		logger.log(Level.WARNING, "Installed KBS OPTPackage from bundle:" + getName());
		
		postInstallPackage();
		logger.log(Level.WARNING, "Post of Installed KBS OPTPackage from bundle:" + getName());
	}
	
	private static class TwoPackEntry {
		private URL url;
		private String version;
		private TwoPackEntry(URL url, String version) {
			this.url=url;
			this.version = version;
		}
	}
	
	private void packInFolder() {
		Enumeration<URL> urls = context.getBundle().findEntries("/META-INF",  "20*.zip", false);
		if (urls == null)
			return;
		
		setProcessInfo(getProcessInfo());
		
		List<File> filesToProcess = new ArrayList<>();
		
		while (urls.hasMoreElements()) {
			URL u = urls.nextElement();
			File toProcess = new File(u.getFile());
			currentFile = toProcess;
			
			if (installedPackage(null)) {
				logger.log(Level.INFO, currentFile.getName() + " already installed.");
			} else {
				filesToProcess.add(toProcess);
				file2urlMap.put(currentFile.getName(), u);
			}
			currentFile = null;
		}

		File[] fileArray = filesToProcess.toArray(new File[filesToProcess.size()]);
		// Sort files by name
		Arrays.sort(fileArray, new Comparator<File>() {
			@Override
			public int compare(File f1, File f2) {
				return f1.getName().compareTo(f2.getName());
			}
		});
		
		if (fileArray.length <= 0) {
			setSummary(Level.INFO, "No zip files to process in PackIn Folder");
			return;
		}
		
		try {
			if (getDBLock()) {
				//Create Session to be able to create records in AD_ChangeLog
				if (Env.getContextAsInt(Env.getCtx(), Env.AD_SESSION_ID) <= 0)
					MSession.get(Env.getCtx(), true);
				for(File zipFile : fileArray) {
					currentFile = zipFile;
					if (!packIn(zipFile)) {
						// stop processing further packages if one fail
						String msg = "Failed application of " + zipFile;
						addLog(Level.WARNING, msg);
						if (getProcessInfo() != null) {
							getProcessInfo().setError(true);
							getProcessInfo().setSummary("@Error@: " + msg);
						}
						break;
					}
					addLog(Level.INFO, "Successful application of " + zipFile);
					filesToProcess.remove(zipFile);
				}
			} else {
				addLog(Level.WARNING, "Could not acquire the DB lock to automatically install the packins");
				return;
			}
		} catch (AdempiereSystemError e) {
			e.printStackTrace();
			addLog(Level.WARNING, e.getLocalizedMessage());
		} finally {
			releaseLock();
			currentFile = null;
		}
		
		if (filesToProcess.size() > 0) {
			StringBuilder pending = new StringBuilder("The following packages were not applied: ");
			for (File file : filesToProcess) {
				pending.append("\n").append(file.getName());
			}
			addLog(Level.WARNING, pending.toString());
		}
		
	}
	
	private void packIn(List<String> installedVersions) {
		List<TwoPackEntry> list = new ArrayList<TwoPackEntry>();
				
		//ODT2Pack_1.0.0_*.zip, ODT2Pack_1.0.1_*.zip, etc
		Enumeration<URL> urls = context.getBundle().findEntries("/META-INF", "ODT2Pack_*.zip", false);
		if (urls == null)
			return;

		while(urls.hasMoreElements()) {
			URL u = urls.nextElement(); 
			String version = extractVersionString(u);
			list.add(new TwoPackEntry(u, version));
		}
		
		X_AD_Package_Imp firstImp = new Query(Env.getCtx(), X_AD_Package_Imp.Table_Name, "Name=? AND PK_Version=? AND PK_Status=?", null)
				.setParameters(getName(), "0.0.0", "Completed successfully")
				.setClient_ID()
				.first();
		if (firstImp == null) {
			Trx trx = Trx.get(Trx.createTrxName(), true);
			trx.setDisplayName(getClass().getName()+"_packIn");
			try {
				Env.getCtx().put("#AD_Client_ID", 0);
				
				firstImp = new X_AD_Package_Imp(Env.getCtx(), 0, trx.getTrxName());
				firstImp.setName(getName());
				firstImp.setPK_Version("0.0.0");
				firstImp.setPK_Status("Completed successfully");
				firstImp.setProcessed(true);
				firstImp.saveEx();
				
				if (list.size() > 0 && installedVersions.size() > 0) {
					List<TwoPackEntry> newList = new ArrayList<TwoPackEntry>();
					for(TwoPackEntry entry : list) {
						boolean patch = false;
						for(String v : installedVersions) {
							Version v1 = new Version(entry.version);
							Version v2 = new Version(v);
							int c = v2.compareTo(v1);
							if (c == 0) {
								patch = false;
								break;
							} else if (c > 0) {
								patch = true;
							}
						}
						if (patch) {
							logger.log(Level.WARNING, "Patch Meta Data for " + getName() + " " + entry.version + " ...");
							
							X_AD_Package_Imp pi = new X_AD_Package_Imp(Env.getCtx(), 0, trx.getTrxName());
							pi.setName(getName());
							pi.setPK_Version(entry.version);
							pi.setPK_Status("Completed successfully");
							pi.setProcessed(true);
							pi.saveEx();
													
						} else {
							newList.add(entry);
						}
					}
					list = newList;
				}
				trx.commit(true);
			} catch (Exception e) {
				trx.rollback();
				logger.log(Level.WARNING, e.getLocalizedMessage(), e);
			} finally {
				trx.close();
			}
		}
		Collections.sort(list, new Comparator<TwoPackEntry>() {
			@Override
			public int compare(TwoPackEntry o1, TwoPackEntry o2) {
				return new Version(o1.version).compareTo(new Version(o2.version));
			}
		});		
				
		try {
			if (getDBLock()) {
				for(TwoPackEntry entry : list) {
					if (!installedVersions.contains(entry.version)) {
						if (!packIn(entry.url)) {
							// stop processing further packages if one fail
							break;
						}
					}
				}
			} else {
				logger.log(Level.WARNING, "Could not acquire the DB lock to install:" + getName());
			}
		} catch (AdempiereSystemError e) {
			e.printStackTrace();
		} finally {
			releaseLock();
		}
	}

	private String extractVersionString(URL u) {
		String p = u.getPath();
		int upos=p.lastIndexOf("2Pack_");
		int dpos=p.lastIndexOf(".");
		if (p.indexOf("_") != p.lastIndexOf("_"))
			dpos=p.lastIndexOf("_");
		String v = p.substring(upos+"2Pack_".length(), dpos);
		return v;
	}

	private boolean packIn(URL packout) {
		if (packout != null && service != null) {
			//Create Session to be able to create records in AD_ChangeLog
			MSession.get(Env.getCtx(), true);
			String path = packout.getPath();
			String suffix = "_"+path.substring(path.lastIndexOf("2Pack_"));
			logger.log(Level.WARNING, "Installing " + getName() + " " + path + " ...");
			FileOutputStream zipstream = null;
			try {
				// copy the resource to a temporary file to process it with 2pack
				InputStream stream = packout.openStream();
				File zipfile = File.createTempFile(getName()+"_", suffix);
				zipstream = new FileOutputStream(zipfile);
			    byte[] buffer = new byte[1024];
			    int read;
			    while((read = stream.read(buffer)) != -1){
			    	zipstream.write(buffer, 0, read);
			    }
			    // call 2pack
				if (!merge(zipfile, extractVersionString(packout)))
					return false;
			} catch (Throwable e) {
				logger.log(Level.WARNING, "Pack in failed.", e);
				return false;
			} finally{
				if (zipstream != null) {
					try {
						zipstream.close();
					} catch (Exception e2) {}
				}
			}
			logger.log(Level.WARNING, getName() + " " + packout.getPath() + " installed");
		} 
		return true;
	}
	
	private int[] getClientIDs(String clientValue) {
		String where = "Value = ?";
		Query q = new Query(Env.getCtx(), MClient.Table_Name, where, null)
				.setParameters(clientValue)
				.setOnlyActiveRecords(true);
		return q.getIDs();
	}

	private boolean packIn(File packinFile) {
		if (packinFile != null) {
			String fileName = packinFile.getName();
			logger.warning("Installing " + fileName + " ...");

			// The convention for package names is: yyyymmddHHMM_ClientValue_InformationalDescription.zip
			String [] parts = fileName.split("_");
			String clientValue = parts[1];
			
			boolean allClients = clientValue.startsWith("ALL-CLIENTS");
			
			int[] clientIDs;
			if (allClients) {
				int[] seedClientIDs = new int[0];
				String seedClientValue = "";
				if (clientValue.startsWith("ALL-CLIENTS-")) {
					seedClientValue = clientValue.split("-")[2];
					seedClientIDs = getClientIDs(seedClientValue);				
					if (seedClientIDs.length == 0) {
						logger.log(Level.WARNING, "Seed client does not exist: " + seedClientValue);
						return false;
					}
				}
				int[] allClientIDs = new Query(Env.getCtx(), MClient.Table_Name, "AD_Client_ID>0 AND Value!=?", null)
						.setOnlyActiveRecords(true)
						.setParameters(seedClientValue)
						.setOrderBy("AD_Client_ID")
						.getIDs();
				// Process first the seed client, put seed in front of the array
				int shift = 0;
				if (seedClientIDs.length > 0)
					shift = 1;
				clientIDs = new int[allClientIDs.length + shift];
				if (seedClientIDs.length > 0)
					clientIDs[0] = seedClientIDs[0];
				for (int i = 0; i < allClientIDs.length; i++) {
					clientIDs[i+shift] = allClientIDs[i];
				}
			} else {
				clientIDs = getClientIDs(clientValue);
				if (clientIDs.length == 0) {
					logger.log(Level.WARNING, "Client does not exist: " + clientValue);
					return false;
				}
			}

			for (int clientID : clientIDs) {
				MClient client = MClient.get(Env.getCtx(), clientID);
				if  (allClients) {
					String message = "Installing " + fileName + " in client " + client.getValue() + "/" + client.getName();
					statusUpdate(message);
				}
				Env.setContext(Env.getCtx(), Env.AD_CLIENT_ID, client.getAD_Client_ID());
				
				FileOutputStream zipstream = null;
				try {
					// copy the resource to a temporary file to process it with 2pack
					URL packout = file2urlMap.get(fileName);
					InputStream stream = packout.openStream();
					File zipfile = File.createTempFile(fileName, null);
					zipstream = new FileOutputStream(zipfile);
				    byte[] buffer = new byte[1024];
				    int read;
				    while((read = stream.read(buffer)) != -1){
				    	zipstream.write(buffer, 0, read);
				    }
				    
				    // call 2pack
					if (service != null) {
						if (!merge(zipfile, null)) {
							return false;
						}
					} else {
						if (!directMerge(zipfile, null)) {
							return false;
						}
					}
				} catch (Throwable e) {
					logger.log(Level.WARNING, "Pack in failed.", e);
					return false;
				} finally {
					if (zipstream != null) {
						try {
							zipstream.close();
						} catch (Exception e2) {}
					}
					Env.setContext(Env.getCtx(), Env.AD_CLIENT_ID, 0);
				}
				logger.warning(packinFile.getPath() + " installed");
			}
			if (allClients ) {
				// when arriving here it means an ALL-CLIENTS 2pack was processed successfully
				// register a record on System to avoid future reprocesses of the same file
				X_AD_Package_Imp_Proc pimpr = new X_AD_Package_Imp_Proc(Env.getCtx(), 0, null);
				pimpr.setName(fileName);
				pimpr.setDateProcessed(new Timestamp(System.currentTimeMillis()));
				pimpr.setP_Msg("This ALL-CLIENT 2Pack was applied successfully in all tenants");
				pimpr.setAD_Package_Source_Type(X_AD_Package_Imp_Proc.AD_PACKAGE_SOURCE_TYPE_File);
				pimpr.saveEx();
				X_AD_Package_Imp pimp = new X_AD_Package_Imp(Env.getCtx(), 0, null);
				pimp.setAD_Package_Imp_Proc_ID(pimpr.getAD_Package_Imp_Proc_ID());
				pimp.setName(fileName);
				pimp.setPK_Status("Completed successfully");
				pimp.setDescription("This ALL-CLIENT 2Pack was applied successfully in all tenants");
				pimp.setProcessed(true);
				pimp.saveEx();
			}
		}

		return true;
	}
	

	private BundleContext getContext() {
		return context;
	}

	private void setContext(BundleContext context) {
		this.context = context;
	}

	private static final String XMLTag_ODTPackage 		 = "ODTPackage";
	private static final String XMLTag_ODTVersion		 = "ODTVersion";
	private static final String XMLTag_ODTPackageName    = "Name";
	private static final String XMLTag_ODTObjectData 	 = "ODTObjectData";
	private static final String XMLTag_ODTObjectDataLine = "ODTObjectDataLine";
	

	private boolean isEmpty(String node) {
		if (node != null && !"".equals(node)) {
			return false;
		} else {
			return true;
		}
	}
	
	private void beforePackIn() {
		URL configURL = getContext().getBundle().getEntry("META-INF/ODTPackage-Before2Pack.xml");
		if (configURL != null)
			odtInstall(configURL);		
		configURL = getContext().getBundle().getEntry("META-INF/ODTPackage.xml");
		if (configURL != null)
			odtInstall(configURL);
	}
	
	private void afterPackIn() {
		URL configURL = getContext().getBundle().getEntry("META-INF/ODTPackage-After2Pack.xml");
		if (configURL != null)
			odtInstall(configURL);
	}
	
	private void odtInstall(URL configURL) {
		if (configURL != null) {
			InputStream input = null;
			try {
				input = configURL.openStream();

				logger.log(Level.INFO, "Loading KBS OPTPackage from bundle:" + getName());
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				dbf.setNamespaceAware(true);
				dbf.setIgnoringElementContentWhitespace(true);

				DocumentBuilder builder = dbf.newDocumentBuilder();
				InputSource is1 = new InputSource(input);
				Document doc = builder.parse(is1);

				NodeList migrations = doc.getDocumentElement().getElementsByTagName(XMLTag_ODTPackage);
				for ( int j = 0; j < migrations.getLength(); j++ )
				{
					Properties ctx = Env.getCtx();

					Element eODTPackage = (Element)migrations.item(j);
					Element eODTVersion = (Element)eODTPackage.getElementsByTagName(XMLTag_ODTVersion).item(0);

					String packageName = ((Element)eODTPackage.getElementsByTagName(XMLTag_ODTPackageName).item(0)).getTextContent().trim();
					int versionNo = Integer.valueOf(eODTVersion.getAttribute(X_KS_ODTVersion.COLUMNNAME_VersionNo));
					
					X_KS_ODTPackage odtPkg = null;
					X_KS_ODTVersion odtVer = null;
					int oldVersionNo = 0;

					// validate package version
					try {
						Query q_pkg = new Query(ctx, X_KS_ODTPackage.Table_Name, "Name='" + packageName + "'", null);
						odtPkg = q_pkg.firstOnly();
												
						Query q_ver = new Query(ctx, X_KS_ODTVersion.Table_Name, "KS_ODTPackage_ID=" + odtPkg.get_ID(), null);
						q_ver.setOrderBy(X_KS_ODTVersion.COLUMNNAME_VersionNo + " DESC ");
						odtVer = q_ver.firstOnly();
					} catch (Exception ex) {
						logger.log(Level.WARNING, "ODT Package Not Existed.", ex.getMessage());
					}
					
					if (odtVer != null)
					{
						oldVersionNo = odtVer.getVersionNo();
					}

					logger.log(Level.INFO, "Install new version for ODT Package:" + packageName 
							+ "(New VersionNo:" + versionNo + " | " + "Old VersionNo:" + oldVersionNo + ")");
					if (versionNo <= oldVersionNo)
					{
						logger.log(Level.INFO, "The version is invalid!"
								+ "(New VersionNo:" + versionNo + " <= " + "Old VersionNo:" + oldVersionNo + ")");
						return;
					}

					if (versionNo > oldVersionNo && oldVersionNo != 0)
					{
						// @TODO: uninstall old version
						logger.log(Level.INFO, "Uninstall old version...@TODO@");
					}


					// start installation ODT
					NodeList childrenOD = eODTVersion.getElementsByTagName(XMLTag_ODTObjectData);

					//int Syn_AD_Column_ID = 0;
					for (int i = 0; i < childrenOD.getLength(); i++)
					{
						Element eODTOD = (Element)childrenOD.item(i);
						String AD_Table_ID = eODTOD.getAttribute(X_KS_ODTObjectData.COLUMNNAME_AD_Table_ID);
						String ObjectData_UUID = eODTOD.getAttribute(X_KS_ODTObjectData.COLUMNNAME_ObjectData_UUID);

						// generate PO
						PO po = null;

						MTable table = MTable.get(Env.getCtx(),Integer.valueOf(AD_Table_ID));

//						if ("AD_TreeNodeMM".equalsIgnoreCase(table.getTableName())) {
//							//po = Utils.generatePO2AD_TreeNodeMM(uuid, table);
//							continue;
//						} else {
//							po = Utils.generatePO(ObjectData_UUID, table);
//						}

						po = Utils.generatePO(ObjectData_UUID, table);

						// generate PO value
						NodeList childrenODL = eODTOD.getElementsByTagName(XMLTag_ODTObjectDataLine);
						for (int ii = 0; ii < childrenODL.getLength(); ii++)
						{
							Element eODTODL = (Element)childrenODL.item(ii);
							String AD_Column_ID = eODTODL.getAttribute(X_KS_ODTObjectDataLine.COLUMNNAME_AD_Column_ID);
							Node NewIDNode = (Element)eODTODL.getElementsByTagName(X_KS_ODTObjectDataLine.COLUMNNAME_NewID).item(0);
							Integer NewID = isEmpty(NewIDNode.getTextContent()) == true ? null:Integer.valueOf(NewIDNode.getTextContent());
							Node NewValueNode = (Element)eODTODL.getElementsByTagName(X_KS_ODTObjectDataLine.COLUMNNAME_NewValue).item(0);
							String NewValue = isEmpty(NewValueNode.getTextContent()) == true ? null:NewValueNode.getTextContent();
							Node IsNewNullValueNode = (Element)eODTODL.getElementsByTagName(X_KS_ODTObjectDataLine.COLUMNNAME_IsNewNullValue).item(0);
							Boolean IsNewNullValue = isEmpty(IsNewNullValueNode.getTextContent()) == true ? null:Boolean.valueOf(IsNewNullValueNode.getTextContent());
							Node NewUUIDNode = (Element)eODTODL.getElementsByTagName(X_KS_ODTObjectDataLine.COLUMNNAME_NewUUID).item(0);
							String NewUUID = isEmpty(NewUUIDNode.getTextContent()) == true ? null:NewUUIDNode.getTextContent();

							try {
								po = Utils.buildPO(po, Integer.valueOf(AD_Column_ID), NewID,
										NewValue, IsNewNullValue, NewUUID, table, ctx, logger);
							} catch (Exception ex) {
								logger.log(Level.SEVERE, "Failed to build PO", ex);
								continue;
							}
						}

						if (po instanceof X_AD_TreeNodeMM && po.is_new())
						{
							// reload PO and reset value
							X_AD_TreeNodeMM poMM = (X_AD_TreeNodeMM)po;
							String whereClause_TNMM = "AD_Tree_ID=" + poMM.getAD_Tree_ID() + " AND Node_ID=" + poMM.getNode_ID();
							X_AD_TreeNodeMM reloadPO = (X_AD_TreeNodeMM)table.getPO(whereClause_TNMM, null);
							reloadPO.setSeqNo(poMM.getSeqNo());
							reloadPO.setParent_ID(poMM.getParent_ID());
							reloadPO.set_ValueNoCheck(reloadPO.getUUIDColumnName(), ObjectData_UUID);
							po = reloadPO;
						}
						
						try {
							po.saveEx();
						} catch (Exception poex) {
							logger.log(Level.SEVERE, "Can't save PO!" + "|AD_Table:" + table.getName() + "|ObjectData_UUID:" + ObjectData_UUID);
							throw poex;
						}

						if (po instanceof X_AD_Column)
						{
							X_AD_Column mcol = (X_AD_Column)po;
							if (!mcol.getAD_Table().isView()) {
								Utils.doSynchronizeColumn(mcol.getAD_Column_ID(), ctx);
							}
						}
						
						// apply SQL
						Node SQL_ApplyNode = (Element)eODTOD.getElementsByTagName(X_KS_ODTObjectData.COLUMNNAME_SQL_Apply).item(0);
						String SQL_Apply = isEmpty(SQL_ApplyNode.getTextContent()) == true ? null:SQL_ApplyNode.getTextContent().trim();
						if (SQL_Apply != null && !"".equals(SQL_Apply)) {
							logger.fine("Apply SQL :" + SQL_Apply);
							String[] sqls = SQL_Apply.split("--//--");
							
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
											if (logger.isLoggable(Level.INFO)) logger.info("Executed SQL Statement for PostgreSQL: "+ sql + " ReturnValue="+count);
										} finally {
											DB.close(stmt);
											stmt = null;
										}
									} catch (Exception e) {
										logger.log(Level.SEVERE,"SQLStatement", e);
									} finally {
										DB.close(pstmt);
										pstmt = null;
									}
								}
							}
						}
					} // end of all ODT OD

					Utils.postInstallPackage(ctx);

					// Import into ODTPackage table
					MKSODTPackage.importFromXmlNode(ctx, eODTPackage);
				}

				logger.log(Level.INFO, "Finished install KSYS OPTPackage from bundle:" + getName());
			} catch (Exception ex) {
				logger.log(Level.SEVERE, ex.getMessage());
			} finally {
				try {
					input.close();
				} catch (IOException e) {
				}
			}
		}
	}


	private void setupPackInContext() {
		Properties serverContext = new Properties();
		serverContext.setProperty(Env.AD_CLIENT_ID, "0");
		ServerContext.setCurrentInstance(serverContext);
	};

	@Override
	protected void frameworkStarted() {
		if (service != null) {
			if (Adempiere.getThreadPoolExecutor() != null) {
				Adempiere.getThreadPoolExecutor().execute(new Runnable() {			
					@Override
					public void run() {
						ClassLoader cl = Thread.currentThread().getContextClassLoader();
						try {
							Thread.currentThread().setContextClassLoader(ODT2PackActivator.class.getClassLoader());
							setupPackInContext();
							installPackage();
						} finally {
							ServerContext.dispose();
							service = null;
							Thread.currentThread().setContextClassLoader(cl);
						}
					}
				});
			} else {
				Adempiere.addServerStateChangeListener(new ServerStateChangeListener() {				
					@Override
					public void stateChange(ServerStateChangeEvent event) {
						if (event.getEventType() == ServerStateChangeEvent.SERVER_START && service != null) {
							ClassLoader cl = Thread.currentThread().getContextClassLoader();
							try {
								Thread.currentThread().setContextClassLoader(ODT2PackActivator.class.getClassLoader());
								setupPackInContext();
								installPackage();
							} finally {
								ServerContext.dispose();
								service = null;
								Thread.currentThread().setContextClassLoader(cl);
							}
						}					
					}
				});
			}
		}
	}
}
