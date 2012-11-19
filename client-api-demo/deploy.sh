#!/bin/bash 
echo "Building the source."
mvn clean install -Dmaven.test.skip=true -o

echo "Removing the old airavata-rest-services ..."
rm -r -f /home/heshan/Dev/sc12/demo1/apache-tomcat-7.0.28/webapps/ airavata-rest-services*
echo "Copying the airavata-rest-services.war to webapps"
cp /home/heshan/Dev/apache/trunk/airavata/trunk/modules/rest/service/target/airavata-rest-services.war /home/heshan/Dev/sc12/demo1/apache-tomcat-7.0.28/webapps

echo "Removing the old client-api-demo war ..."
rm -r -f /home/heshan/Dev/sc12/demo1/apache-tomcat-7.0.28/webapps/client-api-demo*
echo "Copying the client-api-demo war to webapps"
cp target/client-api-demo.war /home/heshan/Dev/sc12/demo1/apache-tomcat-7.0.28/webapps

sleep 15

echo "Copying mysql jar to airavata-rest-services module"
cp ~/Downloads/mysql-connector-java-5.0.8/mysql-connector-java-5.0.8-bin.jar ~/Dev/sc12/demo1/apache-tomcat-7.0.28/webapps/airavata-rest-services/WEB-INF/lib/

#sleep 10
#echo "Copying the repository.properties to webapp ..."
#cp /home/heshan/Dev/sc12/demo1/repository.properties /home/heshan/Dev/sc12/demo1/apache-tomcat-7.0.28/webapps/client-api-demo/WEB-INF/classes/ -v
#echo "Copying the deployment.properties to webapp ..."
#cp /home/heshan/Dev/sc12/demo1/deployment.properties /home/heshan/Dev/sc12/demo1/apache-tomcat-7.0.28/webapps/client-api-demo/ -v
#echo "Copying the deployment.properties to webapp ... again to classes TODO : Fix this"
#cp /home/heshan/Dev/sc12/demo1/deployment.properties /home/heshan/Dev/sc12/demo1/apache-tomcat-7.0.28/webapps/client-api-demo/WEB-INF/classes/

#echo "Copying jars from airavata/lib to webapps.../lib"
#cp /home/heshan/Dev/apache/trunk/airavata/trunk/modules/distribution/target/apache-airavata-0.5-SNAPSHOT/standalone-server/lib/*.jar /home/heshan/Dev/sc12/demo1/apache-tomcat-7.0.28/webapps/client-api-demo/WEB-INF/lib
