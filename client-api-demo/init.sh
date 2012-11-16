#!/bin/bash

echo "Removing previous Tomcat ..."
rm -rf /home/heshan/Dev/sc12/demo1/apache-tomcat-7.0.28/

echo "Copying Tomcat ..."
cd /home/heshan/Dev/sc12/demo1/
cp ~/Downloads/apache-tomcat-7.0.28.zip /home/heshan/Dev/sc12/demo1/ -v
unzip /home/heshan/Dev/sc12/demo1/apache-tomcat-7.0.28.zip
cp /home/heshan/Dev/apache/trunk/airavata/sandbox/client-api-demo/src/main/resources/server.xml /home/heshan/Dev/sc12/demo1/apache-tomcat-7.0.28/conf
chmod a+x /home/heshan/Dev/sc12/demo1/apache-tomcat-7.0.28/bin/*.sh
