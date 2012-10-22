To build
mvn clean install

For development run
mvn cargo:start

To test
* Start database according what you specified in the airavata-server.properties
* for simple methods you can use curl for testing.

**************For Configuration releated methods********************
   curl -v 'http://localhost:9080/airavata-services/registry/api/configuration?key=key1'
   curl -v 'http://localhost:9080/airavata-services/registry/api/configurationlist?key=key1'
   curl -H "Accept: text/plain" -X POST -d "key=key1&value=value4&date=2012-09-21 04:09:56" http://localhost:9080/airavata-services/registry/api/save/configuration
   curl -H "Accept: text/plain" -X POST -d "key=key1&value=value5&date=2012-09-23 04:09:56" http://localhost:9080/airavata-services/registry/api/update/configuration
   curl --request DELETE http://localhost:9080/airavata-services/registry/api/delete/allconfiguration?key=key1
   curl --request DELETE 'http://localhost:9080/airavata-services/registry/api/delete/configuration?key=key2&value=value2'
   curl --request GET http://localhost:9080/airavata-services/registry/api/gfac/urilist
   curl --request GET http://localhost:9080/airavata-services/registry/api/workflowinterpreter/urilist
   curl --request GET http://localhost:9080/airavata-services/registry/api/eventingservice/uri
   curl --request GET http://localhost:9080/airavata-services/registry/api/messagebox/uri
   curl -H "Accept: text/plain" -X POST -d "uri=http://192.168.17.1:8080/axis2/services/GFacService2" http://localhost:9080/airavata-services/registry/api/add/gfacuri
   curl -H "Accept: text/plain" -X POST -d "uri=http://192.168.17.1:8080/axis2/services/WorkflowInterpretor2" http://localhost:9080/airavata-services/registry/api/add/workflowinterpreteruri
   curl -H "Accept: text/plain" -X POST -d "uri=http://192.168.17.1:8080/axis2/services/EventingService2" http://localhost:9080/airavata-services/registry/api/add/eventinguri
   curl -H "Accept: text/plain" -X POST -d "uri=http://192.168.17.1:8080/axis2/services/MsgBoxService2" http://localhost:9080/airavata-services/registry/api/add/msgboxuri
   curl -H "Accept: text/plain" -X POST -d "uri=http://192.168.17.1:8080/axis2/services/GFacService2&date=2012-10-18 00:00:00" http://localhost:9080/airavata-services/registry/api/add/gfacuri/date
   curl -H "Accept: text/plain" -X POST -d "uri=http://192.168.17.1:8080/axis2/services/WorkflowInterpretor2&date=2012-10-18 00:00:00" http://localhost:9080/airavata-services/registry/api/add/workflowinterpreteruri/date
   curl -H "Accept: text/plain" -X POST -d "uri=http://192.168.17.1:8080/axis2/services/MsgBoxService2&date=2012-10-18 00:00:00" http://localhost:9080/airavata-services/registry/api/add/msgboxuri/date
   curl --request DELETE http://localhost:9080/airavata-services/registry/api/delete/gfacuri?uri=http://192.168.17.1:8080/axis2/services/GFacService2
   curl --request DELETE http://localhost:9080/airavata-services/registry/api/delete/allgfacuris
   curl --request DELETE http://localhost:9080/airavata-services/registry/api/delete/workflowinterpreteruri?uri=http://192.168.17.1:8080/axis2/services/WorkflowInterpretor2
   curl --request DELETE http://localhost:9080/airavata-services/registry/api/delete/allworkflowinterpreteruris
   curl --request DELETE http://localhost:9080/airavata-services/registry/api/delete/eventinguri
   curl --request DELETE http://localhost:9080/airavata-services/registry/api/delete/msgboxuri

************For descriptiors*********************************
    curl --request GET http://localhost:9080/airavata-services/registry/api/hostdescriptor/exist?descriptorName=ember
    curl -H "Accept: text/plain" -X POST -d 'host=<type:hostDescription xmlns:type="http://schemas.airavata.apache.org/gfac/type"><type:hostName>testHost1</type:hostName><type:hostAddress>aaaa</type:hostAddress></type:hostDescription>' http://localhost:9080/airavata-services/registry/api/hostdescriptor/save
    curl -H "Accept: text/plain" -X POST -d 'host=<type:hostDescription xmlns:type="http://schemas.airavata.apache.org/gfac/type"><type:hostName>testHost1</type:hostName><type:hostAddress>aaaabbbbbbb</type:hostAddress></type:hostDescription>' http://localhost:9080/airavata-services/registry/api/hostdescriptor/update





