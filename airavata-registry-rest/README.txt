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
    ############## Host descriptrors ##########################

    curl --request GET http://localhost:9080/airavata-services/registry/api/hostdescriptor/exist?descriptorName=ember
    curl -H "Accept: text/plain" -X POST -d 'host=<type:hostDescription xmlns:type="http://schemas.airavata.apache.org/gfac/type"><type:hostName>testHost1</type:hostName><type:hostAddress>aaaa</type:hostAddress></type:hostDescription>' http://localhost:9080/airavata-services/registry/api/hostdescriptor/save
    curl -H "Accept: text/plain" -X POST -d 'host=<type:hostDescription xmlns:type="http://schemas.airavata.apache.org/gfac/type"><type:hostName>testHost1</type:hostName><type:hostAddress>aaaabbbbbbb</type:hostAddress></type:hostDescription>' http://localhost:9080/airavata-services/registry/api/hostdescriptor/update
    curl --request GET http://localhost:9080/airavata-services/registry/api/host/description?hostName=testHost1
    curl --request DELETE http://localhost:9080/airavata-services/registry/api/hostdescriptor/delete?hostName=testHost1
    curl --request GET http://localhost:9080/airavata-services/registry/api/get/hostdescriptors

    ############## Service descriptrors ##########################
    curl --request GET http://localhost:9080/airavata-services/registry/api/servicedescriptor/exist?descriptorName=echo
    curl -H "Accept: text/plain" -X POST -d 'service=<type:serviceDescription xmlns:type="http://schemas.airavata.apache.org/gfac/type"><type:name>echo1</type:name><type:inputParameters><type:parameterName>echo1_input</type:parameterName><type:parameterDescription xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/><type:parameterType type="String"><name>String</name></type:parameterType></type:inputParameters><type:outputParameters><type:parameterName>echo1_output</type:parameterName><type:parameterDescription xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/><type:parameterType type="String"><name>String</name></type:parameterType></type:outputParameters></type:serviceDescription>' http://localhost:9080/airavata-services/registry/api/servicedescriptor/save
    curl -H "Accept: text/plain" -X POST -d 'service=<type:serviceDescription xmlns:type="http://schemas.airavata.apache.org/gfac/type"><type:name>echo1</type:name><type:inputParameters><type:parameterName>echo11_input</type:parameterName><type:parameterDescription xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/><type:parameterType type="String"><name>String</name></type:parameterType></type:inputParameters><type:outputParameters><type:parameterName>echo11_output</type:parameterName><type:parameterDescription xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/><type:parameterType type="String"><name>String</name></type:parameterType></type:outputParameters></type:serviceDescription>' http://localhost:9080/airavata-services/registry/api/servicedescriptor/update
    curl --request GET http://localhost:9080/airavata-services/registry/api/servicedescriptor/description?serviceName=echo1
    curl --request DELETE http://localhost:9080/airavata-services/registry/api/servicedescriptor/delete?serviceName=echo1
    curl --request GET http://localhost:9080/airavata-services/registry/api/get/servicedescriptors

    ############## Application descriptrors ##########################
    curl --request GET 'http://localhost:9080/airavata-services/registry/api/applicationdescriptor/exist?serviceName=echo&hostName=LocalHost&descriptorName=LocalHost_application'
    curl -H "Accept: text/plain" -X POST -d 'service=<type:serviceDescription xmlns:type="http://schemas.airavata.apache.org/gfac/type"><type:name>echo</type:name><type:inputParameters><type:parameterName>echo_input</type:parameterName><type:parameterDescription xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/><type:parameterType type="String"><name>String</name></type:parameterType></type:inputParameters><type:outputParameters><type:parameterName>echo_output</type:parameterName><type:parameterDescription xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/><type:parameterType type="String"><name>String</name></type:parameterType></type:outputParameters></type:serviceDescription>&host=<type:hostDescription xmlns:type="http://schemas.airavata.apache.org/gfac/type"><type:hostName>LocalHost</type:hostName><type:hostAddress>127.0.0.1</type:hostAddress></type:hostDescription>&application=<type:applicationDeploymentDescription xmlns:type="http://schemas.airavata.apache.org/gfac/type"><type:applicationName>LocalHost_application1</type:applicationName><type:executableLocation>/Users/chathuri/airavata/source/trunk_new/samples/echo.sh</type:executableLocation><type:scratchWorkingDirectory>/tmp1</type:scratchWorkingDirectory></type:applicationDeploymentDescription>' http://localhost:9080/airavata-services/registry/api/applicationdescriptor/build/save
    curl -H "Accept: text/plain" -X POST -d 'serviceName=echo&hostName=LocalHost&application=<type:applicationDeploymentDescription xmlns:type="http://schemas.airavata.apache.org/gfac/type"><type:applicationName>LocalHost_application2</type:applicationName><type:executableLocation>/Users/chathuri/airavata/source/trunk_new/samples/echo.sh</type:executableLocation><type:scratchWorkingDirectory>/tmp1</type:scratchWorkingDirectory></type:applicationDeploymentDescription>' http://localhost:9080/airavata-services/registry/api/applicationdescriptor/save
    curl -H "Accept: text/plain" -X POST -d 'service=<type:serviceDescription xmlns:type="http://schemas.airavata.apache.org/gfac/type"><type:name>echo</type:name><type:inputParameters><type:parameterName>echo_input</type:parameterName><type:parameterDescription xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/><type:parameterType type="String"><name>String</name></type:parameterType></type:inputParameters><type:outputParameters><type:parameterName>echo_output</type:parameterName><type:parameterDescription xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/><type:parameterType type="String"><name>String</name></type:parameterType></type:outputParameters></type:serviceDescription>&host=<type:hostDescription xmlns:type="http://schemas.airavata.apache.org/gfac/type"><type:hostName>LocalHost</type:hostName><type:hostAddress>127.0.0.1</type:hostAddress></type:hostDescription>&application=<type:applicationDeploymentDescription xmlns:type="http://schemas.airavata.apache.org/gfac/type"><type:applicationName>LocalHost_application2</type:applicationName><type:executableLocation>xccccccccxc</type:executableLocation><type:scratchWorkingDirectory>/sdddsdsds</type:scratchWorkingDirectory></type:applicationDeploymentDescription>' http://localhost:9080/airavata-services/registry/api/applicationdescriptor/update/descriptor
    curl -H "Accept: text/plain" -X POST -d 'serviceName=echo&hostName=LocalHost&application=<type:applicationDeploymentDescription xmlns:type="http://schemas.airavata.apache.org/gfac/type"><type:applicationName>LocalHost_application2</type:applicationName><type:executableLocation>xccccccccxc11111</type:executableLocation><type:scratchWorkingDirectory>/sdddsdsds1111</type:scratchWorkingDirectory></type:applicationDeploymentDescription>' http://localhost:9080/airavata-services/registry/api/applicationdescriptor/update
    curl --request GET 'http://localhost:9080/airavata-services/registry/api/applicationdescriptor/description?serviceName=echo&hostName=LocalHost&applicationName=LocalHost_application2'
    curl --request GET 'http://localhost:9080/airavata-services/registry/api/applicationdescriptors/alldescriptors/host/service?serviceName=echo&hostName=LocalHost'
    curl --request GET 'http://localhost:9080/airavata-services/registry/api/applicationdescriptor/alldescriptors/service?serviceName=echo'
    curl --request GET 'http://localhost:9080/airavata-services/registry/api/applicationdescriptor/alldescriptors'
    curl --request DELETE 'http://localhost:9080/airavata-services/registry/api/applicationdescriptor/delete?serviceName=echo&hostName=LocalHost&appName=LocalHost_application2'

************ Project Registry *********************************
    curl --request GET 'http://localhost:9080/airavata-services/registry/api/project/exist?projectName=default'
    curl -H "Accept: text/plain" -X POST -d 'projectName=project1' http://localhost:9080/airavata-services/registry/api/add/project
    curl -H "Accept: text/plain" -X POST -d 'projectName=project1' http://localhost:9080/airavata-services/registry/api/update/project
    curl --request DELETE 'http://localhost:9080/airavata-services/registry/api/delete/project?projectName=project1'
    curl --request GET 'http://localhost:9080/airavata-services/registry/api/get/project?projectName=project1'



