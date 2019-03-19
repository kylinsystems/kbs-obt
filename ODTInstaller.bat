@Title ... KSYS ODT Installer
@Echo off

cd %~dp0 

java -Dosgi.noShutdown=false -Dosgi.compatibility.bootdelegation=true -Dosgi.install.area=director -jar plugins/org.eclipse.osgi_3.10.1.v20140909-1633.jar -application org.eclipse.equinox.p2.director -consoleLog -profileProperties org.eclipse.update.install.features=true -destination $DESTINATION  -repository http://sourceforge.net/projects/idempiereksys/files/ksys.p2/odt/ -u com.kylinsystems.idempiere.odt3.feature.group

java -Dosgi.noShutdown=false -Dosgi.compatibility.bootdelegation=true -Dosgi.install.area=director -jar plugins/org.eclipse.osgi_3.10.1.v20140909-1633.jar -application org.eclipse.equinox.p2.director -consoleLog -profileProperties org.eclipse.update.install.features=true -destination $DESTINATION  -repository http://sourceforge.net/projects/idempiereksys/files/ksys.p2/odt/ -i com.kylinsystems.idempiere.odt3.feature.group

