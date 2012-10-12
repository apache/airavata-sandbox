#!/bin/bash 
echo "Building the source."
mvn clean install -Dmaven.test.skip=true -o

echo "Removing the old war ..."
rm -r -f /home/heshan/Dev/sc12/demo1/apache-tomcat-7.0.28/webapps/client-api-demo*
echo "Copying the client-api-demo war to webapps"
cp target/client-api-demo.war /home/heshan/Dev/sc12/demo1/apache-tomcat-7.0.28/webapps

sleep 10
echo "Copying the repository.properties to webapp ..."
cp /home/heshan/Dev/sc12/demo1/repository.properties /home/heshan/Dev/sc12/demo1/apache-tomcat-7.0.28/webapps/client-api-demo/WEB-INF/classes/
echo "Copying the deployment.properties to webapp ..."
cp /home/heshan/Dev/sc12/demo1/deployment.properties /home/heshan/Dev/sc12/demo1/apache-tomcat-7.0.28/webapps/client-api-demo/
echo "Copying the deployment.properties to webapp ... again to classes TODO : Fix this"
cp /home/heshan/Dev/sc12/demo1/deployment.properties /home/heshan/Dev/sc12/demo1/apache-tomcat-7.0.28/webapps/client-api-demo/WEB-INF/classes/

