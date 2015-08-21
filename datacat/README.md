DataCat
=========

DataCat is a scientific data cataloging system designed for efficient and effective management of scientific data.

This is a project done by students of University of Moratuwa, Sri Lanka as a part of their final year project. The goal is to contribute this code to Apache Airavata project and integrate it with Airavata workflow software and continuing to develop it further.

The project development team:
  * Supun Nakandala – supun.nakandala@gmail.com
  * Sachith Withana - swsachith@gmail.com
  * Dinu Kumarasiri - sandarumk@gmail.com
  * Hirantha Sankalpa - hiranthasankalpa@gmail.com

Before Running the Program
==========================
* Add WSO2 server certificate found in the top level directory to the java key store using the below command 
    $JAVA_HOME/jre/lib/security$ keytool -import -file wso2_is.cer -keystore cacerts

* Start Solr server using the following command
    $java -jar SOLR_HOME/start.jar
    
* Start WSO2 IS using the following command
     $sh WSO2_IS_HOME/bin/wso2server.sh
