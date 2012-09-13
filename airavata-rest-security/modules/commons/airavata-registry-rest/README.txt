To build
mvn clean install

For development run
mvn cargo:start

Update Message beans
xjc src/main/resources/ServiceMessage.xsd -p org.apache.airavata.services.message -d src/main/java/