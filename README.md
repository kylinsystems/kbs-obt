# KBS ObjectData Tool from KSYS
The ObjectDataTool(ODT) plugin adds the functionality of import/export AD data.

The core design concept of ODT it is based on following assumption:
* The uuid value of standard AD data which released from iDempiere are unique and keeping unchanged during its life cycle.
* The each customized application we are creating by AD structure must be assigned to one specified EntityType.

The main features will be provided by ODT:
* Import ODT package
* Export ODT package
* Install application
* Uninstall application
* Refresh ODT package
* Link EntityType
	
Developed by ken.longnan@gmail.com, contributed by KSYS

Please refer to http://wiki.idempiere.org/en/Plugin:_ObjectDataTool

## Installation

### How to install the plugin by Felix Console

http://localhost:8080/osgi/system/console/bundles

### How to install the plugin by p2 Director scripts
  
Download the installer script (.bat or .sh) and run it inside your IDEMPIERE_HOME folder.

### How to install the plugin by p2 Console in OSGi shell
  
Refer to [http://wiki.eclipse.org/Equinox_p2_Console_Users_Guide](http://wiki.eclipse.org/Equinox_p2_Console_Users_Guide)

    * Connect to OSGi shell by "telnet localhost 12612"

    * You will get shell prompt "osgi>"

    * Search the plugin "org.eclipse.equinox.p2.console_*", and start it
        ```
        osgi > ss
        osgi > start xxx
        ```
    * In shell
        ```
        #Adds both metadata and artifact repository at URI
        osgi> provaddrepo https://sourceforge.net/projects/idempiereksys/files/idempiere-ksys.p2/odt/

        #Lists all IUs with group capabilities
        osgi> provlg

        com.kylinsystems.idempiere.odt3.feature.group 3.1.0.201603142055

        osgi> provinstall com.kylinsystems.idempiere.odt3.feature.group 3.1.0.201603142055

        Installation complete for com.kylinsystems.idempiere.odt3.feature.group 3.1.0.201603142055

        #Apply changes
        #To see the applied configuration changes at runtime, you have to use the confapply command.
        osgi> confapply
        ```
### How to install the plugin in Karaf CLI
    ```
    karaf@root()>install http://sourceforge.net/projects/idempiereksys/files/ksys.p2/odt/plugins/com.kylinsystems.idempiere.odt2_2.0.0.201406162201.jar

    karaf@root()>list
    ```
After installation, you have to start bundler manually.

## Build
```
mvn clean verify
```