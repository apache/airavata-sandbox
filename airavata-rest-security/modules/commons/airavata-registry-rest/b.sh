#!/bin/bash

cd ../../credential-store/;mvn -Dmaven.test.skip=true install;cd ../commons/airavata-registry-rest ;mvn clean install
cp target/airavata-registry-rest-services.war /Users/thejaka/development/tools/apache-tomcat-6.0.35/webapps/
#scp target/airavata-registry-rest-services.war amila@156.56.179.104:/home/amila/development/tools/apache-tomcat-7.0.29/webapps/
