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
    curl -H "Accept: text/plain" -X POST -H "Content-Type: text/xml" -d '<type:hostDescription xmlns:type="http://schemas.airavata.apache.org/gfac/type"><type:hostName>LocalHost111</type:hostName><type:hostAddress>127.0.0.1</type:hostAddress></type:hostDescription>' http://localhost:9080/airavata-services/registry/api/hostdescriptor/save
    curl -H "Accept: text/plain" -X POST -H "Content-Type: text/xml" -d '<type:hostDescription xmlns:type="http://schemas.airavata.apache.org/gfac/type"><type:hostName>aaaaa</type:hostName><type:hostAddress>dsdddssdd</type:hostAddress></type:hostDescription>' http://localhost:9080/airavata-services/registry/api/hostdescriptor/update    curl --request GET http://localhost:9080/airavata-services/registry/api/host/description?hostName=testHost1
    curl --request DELETE http://localhost:9080/airavata-services/registry/api/hostdescriptor/delete?hostName=testHost1
    curl --request GET http://localhost:9080/airavata-services/registry/api/get/hostdescriptors

    ############## Service descriptrors ##########################
    curl --request GET http://localhost:9080/airavata-services/registry/api/servicedescriptor/exist?descriptorName=echo
    curl -H "Accept: text/plain" -X POST -H "Content-Type: text/xml" -d '<type:serviceDescription xmlns:type="http://schemas.airavata.apache.org/gfac/type"><type:name>echo1</type:name><type:inputParameters><type:parameterName>echo1_input</type:parameterName><type:parameterDescription xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/><type:parameterType type="String"><name>String</name></type:parameterType></type:inputParameters><type:outputParameters><type:parameterName>echo1_output</type:parameterName><type:parameterDescription xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/><type:parameterType type="String"><name>String</name></type:parameterType></type:outputParameters></type:serviceDescription>' http://localhost:9080/airavata-services/registry/api/servicedescriptor/save
    curl -H "Accept: text/plain" -X POST -H "Content-Type: text/xml" -d '<type:serviceDescription xmlns:type="http://schemas.airavata.apache.org/gfac/type"><type:name>echo1</type:name><type:inputParameters><type:parameterName>echo11_input</type:parameterName><type:parameterDescription xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/><type:parameterType type="String"><name>String</name></type:parameterType></type:inputParameters><type:outputParameters><type:parameterName>echo11_output</type:parameterName><type:parameterDescription xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/><type:parameterType type="String"><name>String</name></type:parameterType></type:outputParameters></type:serviceDescription>' http://localhost:9080/airavata-services/registry/api/servicedescriptor/update
    curl --request GET http://localhost:9080/airavata-services/registry/api/servicedescriptor/description?serviceName=echo1
    curl --request DELETE http://localhost:9080/airavata-services/registry/api/servicedescriptor/delete?serviceName=echo1
    curl --request GET http://localhost:9080/airavata-services/registry/api/get/servicedescriptors

    ############## Application descriptrors ##########################
    curl --request GET 'http://localhost:9080/airavata-services/registry/api/applicationdescriptor/exist?serviceName=echo&hostName=LocalHost&descriptorName=LocalHost_application'
    curl -H "Accept: text/plain" -X POST -H "Content-Type: text/xml" -d '<type:serviceDescription xmlns:type="http://schemas.airavata.apache.org/gfac/type"><type:name>echo</type:name><type:inputParameters><type:parameterName>echo_input</type:parameterName><type:parameterDescription xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/><type:parameterType type="String"><name>String</name></type:parameterType></type:inputParameters><type:outputParameters><type:parameterName>echo_output</type:parameterName><type:parameterDescription xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/><type:parameterType type="String"><name>String</name></type:parameterType></type:outputParameters></type:serviceDescription>&host=<type:hostDescription xmlns:type="http://schemas.airavata.apache.org/gfac/type"><type:hostName>LocalHost</type:hostName><type:hostAddress>127.0.0.1</type:hostAddress></type:hostDescription>&application=<type:applicationDeploymentDescription xmlns:type="http://schemas.airavata.apache.org/gfac/type"><type:applicationName>LocalHost_application1</type:applicationName><type:executableLocation>/Users/chathuri/airavata/source/trunk_new/samples/echo.sh</type:executableLocation><type:scratchWorkingDirectory>/tmp1</type:scratchWorkingDirectory></type:applicationDeploymentDescription>' http://localhost:9080/airavata-services/registry/api/applicationdescriptor/build/save
    curl -H "Accept: text/plain" -X POST -H "Content-Type: text/xml" -d 'serviceName=echo&hostName=LocalHost&<type:applicationDeploymentDescription xmlns:type="http://schemas.airavata.apache.org/gfac/type"><type:applicationName>LocalHost_application2</type:applicationName><type:executableLocation>/Users/chathuri/airavata/source/trunk_new/samples/echo.sh</type:executableLocation><type:scratchWorkingDirectory>/tmp1</type:scratchWorkingDirectory></type:applicationDeploymentDescription>' http://localhost:9080/airavata-services/registry/api/applicationdescriptor/save
    curl -H "Accept: text/plain" -X POST -H "Content-Type: text/xml" -d 'service=<type:serviceDescription xmlns:type="http://schemas.airavata.apache.org/gfac/type"><type:name>echo</type:name><type:inputParameters><type:parameterName>echo_input</type:parameterName><type:parameterDescription xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/><type:parameterType type="String"><name>String</name></type:parameterType></type:inputParameters><type:outputParameters><type:parameterName>echo_output</type:parameterName><type:parameterDescription xsi:nil="true" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/><type:parameterType type="String"><name>String</name></type:parameterType></type:outputParameters></type:serviceDescription>&host=<type:hostDescription xmlns:type="http://schemas.airavata.apache.org/gfac/type"><type:hostName>LocalHost</type:hostName><type:hostAddress>127.0.0.1</type:hostAddress></type:hostDescription>&application=<type:applicationDeploymentDescription xmlns:type="http://schemas.airavata.apache.org/gfac/type"><type:applicationName>LocalHost_application2</type:applicationName><type:executableLocation>xccccccccxc</type:executableLocation><type:scratchWorkingDirectory>/sdddsdsds</type:scratchWorkingDirectory></type:applicationDeploymentDescription>' http://localhost:9080/airavata-services/registry/api/applicationdescriptor/update/descriptor
    curl -H "Accept: text/plain" -X POST -H "Content-Type: text/xml" -d 'serviceName=echo&hostName=LocalHost&application=<type:applicationDeploymentDescription xmlns:type="http://schemas.airavata.apache.org/gfac/type"><type:applicationName>LocalHost_application2</type:applicationName><type:executableLocation>xccccccccxc11111</type:executableLocation><type:scratchWorkingDirectory>/sdddsdsds1111</type:scratchWorkingDirectory></type:applicationDeploymentDescription>' http://localhost:9080/airavata-services/registry/api/applicationdescriptor/update
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
    curl --request GET 'http://localhost:9080/airavata-services/registry/api/get/projects'

************* Experiments *************************************
    curl --request DELETE 'http://localhost:9080/airavata-services/registry/api/delete/experiment?experimentId=eb9e67cf-6fe3-46f1-b50b-7b42936d347d
    curl --request GET 'http://localhost:9080/airavata-services/registry/api/get/experiments/all'
    curl --request GET 'http://localhost:9080/airavata-services/registry/api/get/experiments/project?projectName=default'
    curl --request GET 'http://localhost:9080/airavata-services/registry/api/get/experiments/date?fromDate=2012-10-16%2000:00:00&toDate=2012-10-18%2000:00:00'
    curl --request GET 'http://localhost:9080/airavata-services/registry/api/get/experiments/project/date?projectName=default&fromDate=2012-10-16%2000:00:00&toDate=2012-10-18%2000:00:00'
    curl -H "Accept: text/plain" -X POST -d 'projectName=project1&experimentID=testexpID1&submittedDate=2012-10-18 00:00:00' http://localhost:9080/airavata-services/registry/api/add/experiment
    curl --request GET 'http://localhost:9080/airavata-services/registry/api/experiment/exist?experimentId=testexpID1'
    curl --request GET 'http://localhost:9080/airavata-services/registry/api/experiment/notexist/create?experimentId=testExpID2&createIfNotPresent=true'
    curl -H "Accept: text/plain" -X POST -d 'experimentId=testExpID2&user=abc' http://localhost:9080/airavata-services/registry/api/update/experiment
    curl --request GET 'http://localhost:9080/airavata-services/registry/api/get/experiment/executionuser?experimentId=testExpID2'
    curl --request GET 'http://localhost:9080/airavata-services/registry/api/get/experiment/name?experimentId=testExpID2'
    curl -H "Accept: text/plain" -X POST -d 'experimentId=testExpID2&experimentName=ddscsddsss111' http://localhost:9080/airavata-services/registry/api/update/experimentname
    curl --request GET 'http://localhost:9080/airavata-services/registry/api/get/experimentmetadata?experimentId=testExpID2'
    curl -H "Accept: text/plain" -X POST -d 'experimentId=testExpID2&metadata=aaaaaaa' http://localhost:9080/airavata-services/registry/api/update/experimentmetadata

************* Workflow Execution *************************************
    curl --request GET 'http://localhost:9080/airavata-services/registry/api/get/workflowtemplatename?workflowInstanceId=e00ddc5e-f8d5-4492-9eb2-10372efb103c'
    curl -H "Accept: text/plain" -X POST -d 'workflowInstanceId=e00ddc5e-f8d5-4492-9eb2-10372efb103c&templateName=wftemplate1' http://localhost:9080/airavata-services/registry/api/update/workflowinstancetemplatename
    curl --request GET 'http://localhost:9080/airavata-services/registry/api/get/experimentworkflowinstances?experimentId=ff7338c9-f9ad-4d86-b486-1e8e9c3a9cc4'
    curl --request GET 'http://localhost:9080/airavata-services/registry/api/workflowinstance/exist/check?instanceId=e00ddc5e-f8d5-4492-9eb2-10372efb103c'
    curl --request GET 'http://localhost:9080/airavata-services/registry/api/workflowinstance/exist/create?instanceId=testWFInstanceID&createIfNotPresent=true'
    curl -H "Accept: text/plain" -X POST -d 'instanceId=testWFInstanceID&executionStatus=FINISHED' http://localhost:9080/airavata-services/registry/api/update/workflowinstancestatus/instanceid
    curl -H "Accept: text/plain" -X POST -d 'experimentId=testWFInstanceID&workflowInstanceId=testWFInstanceID&executionStatus=STARTED&statusUpdateTime=2012-10-23 00:00:00' http://localhost:9080/airavata-services/registry/api/update/workflowinstancestatus/experimentid
    curl --request GET 'http://localhost:9080/airavata-services/registry/api/get/workflowinstancestatus?instanceId=testWFInstanceID'
    curl -H "Accept: text/plain" -X POST -d 'experimentID=ff7338c9-f9ad-4d86-b486-1e8e9c3a9cc4&nodeID=TempConvertSoap_FahrenheitToCelsius&workflowInstanceId=ff7338c9-f9ad-4d86-b486-1e8e9c3a9cc4&data=testInputdata' http://localhost:9080/airavata-services/registry/api/update/workflownodeinput

********* Sample JSON message for Application and service *****************

{
   "applicationName":"Tesing",
   "cpuCount":"12",
   "hostdescName":"localhost",
   "maxMemory":"0",
   "maxWallTime":"0",
   "minMemory":"0",
   "nodeCount":"1",
   "processorsPerNode":"12",
   "serviceDesc":{
      "inputParams":[
         {
            "dataType":"input",
            "description":"my input",
            "name":"myinput",
            "type":"String"
         },
         {
            "dataType":"input",
            "description":"my input",
            "name":"myinput",
            "type":"String"
         }
      ],
      "outputParams":[
         {
            "dataType":"output",
            "description":"my output",
            "name":"myoutput",
            "type":"String"
         },
         {
            "dataType":"output",
            "description":"my output",
            "name":"myoutput",
            "type":"String"
         }
      ]
   }
}

*** Sample XML message to create application and service

<?xml version="1.0" encoding="UTF-8" standalone="yes"?><application><applicationName>Testing</applicationName><cpuCount>0</cpuCount><hostdescName>localhost</hostdescName><maxMemory>0</maxMemory><maxWallTime>0</maxWallTime><minMemory>0</minMemory><nodeCount>0</nodeCount><processorsPerNode>0</processorsPerNode><serviceDesc><inputParams><dataType>input</dataType><description>my input</description><name>myinput</name><type>String</type></inputParams><inputParams><dataType>input</dataType><description>my input</description><name>myinput</name><type>String</type></inputParams><outputParams><dataType>output</dataType><description>my output</description><name>myoutput</name><type>String</type></outputParams><outputParams><dataType>output</dataType><description>my output</description><name>myoutput</name><type>String</type></outputParams></serviceDesc></application>





