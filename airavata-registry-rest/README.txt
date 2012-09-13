To build
mvn clean install

For development run
mvn cargo:start

Update Message beans
xjc src/main/resources/ServiceMessage.xsd -p org.apache.airavata.services.message -d src/main/java/
To build
mvn clean install

For development run
mvn cargo:start

Update Message beans
xjc src/main/resources/ServiceMessage.xsd -p org.apache.airavata.services.message -d src/main/java/
To build
mvn clean install

For development run
mvn cargo:start

To test
* Start jackrabbit on port 8081
* for simple methods you can use curl for testing. For example, if you want to get the usename, you can run the command
curl -v http://localhost:9080/airavata-services/registry/api/username. This will return "admin" for the moment.

