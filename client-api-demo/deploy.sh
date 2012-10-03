#!/bin/bash 
echo "Building the source."
mvn clean install 

echo "Copying generated classes directory to Tomcat"
cp -rf target/classes/ /home/heshan/Dev/xsede12/tools/apache-tomcat-7.0.28/webapps/demo/
echo "Copying generated jar to Tomcat"
cp target/client-api-sample-1.0-SNAPSHOT.jar /home/heshan/Dev/xsede12/tools/apache-tomcat-7.0.28/webapps/demo/WEB-INF/lib/ -v

echo "Copying JSPs to tomcat"
cp /home/heshan/Dev/xsede12/simplegrid-airavata-sample/client-api-sample/src/main/java/*.jsp /home/heshan/Dev/xsede12/tools/apache-tomcat-7.0.28/webapps/demo 
echo "Copying deployment.properties to tomcat"
cp /home/heshan/Dev/xsede12/simplegrid-airavata-sample/client-api-sample/src/main/java/deployment.properties /home/heshan/Dev/xsede12/tools/apache-tomcat-7.0.28/webapps/demo 

echo "Copying all the jars to the WEB-INF/lib for now. TODO: should clean this up"
cp /home/heshan/Dev/incubator/airavata/trunk/modules/distribution/target/apache-airavata-0.4-incubating-SNAPSHOT/standalone-server/lib/*.jar /home/heshan/Dev/xsede12/tools/apache-tomcat-7.0.28/webapps/demo/WEB-INF/lib/ 

cp /home/heshan/Dev/incubator/airavata/trunk/modules/airavata-client/target/airavata-client-api-0.4-incubating-SNAPSHOT.jar /home/heshan/Dev/xsede12/tools/apache-tomcat-7.0.28/webapps/demo/WEB-INF/lib/ 

cp /home/heshan/Dev/incubator/airavata/trunk/modules/distribution/target/apache-airavata-0.4-incubating-SNAPSHOT/standalone-server/repository/services/*.jar /home/heshan/Dev/xsede12/tools/apache-tomcat-7.0.28/webapps/demo/WEB-INF/lib/ 


