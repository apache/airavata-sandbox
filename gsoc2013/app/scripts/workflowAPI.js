/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */


function ContextHeader (){

    var userIdentifier;
    var soa_service_epr = new SOAServiceEPR();
    var workflow_monitoring_context = new WorkflowMonitoringContext();
    var workflow_scheduling_context = new WorkflowSchedulingContext();
    var workflow_output_data_handling = new WorkflowOutputDataHandling();
    var security_context = new SecurityContext();

    this.getUserIdentifier = function (){
        return userIdentifier;
    };
    this.setUserIdentifier = function (param){
        userIdentifier = param;
    };

    this.getSOAServiceEPR = function (){
        return soa_service_epr;
    };
    this.setSOAServiceEPR = function (param){
        if(param instanceof SOAServiceEPR){
            soa_service_epr = param;
        }
    };

    this.getWorkflowMonitoringContext = function (){
        return workflow_monitoring_context;
    };
    this.setWorkflowMonitoringContext = function (param){
        if(param instanceof  WorkflowMonitoringContext){
            workflow_monitoring_context = param;
        }
    };

    this.getWorkflowSchedulingContext = function (){
        return workflow_scheduling_context;
    };
    this.setWorkflowSchedulingContext = function (param){
        if(param instanceof WorkflowSchedulingContext){
            workflow_scheduling_context = param;
        }
    };

    this.getWorkflowOutputDataHandling = function (){
        return workflow_output_data_handling;
    };
    this.setWorkflowOutputDataHandling = function (param){
        if( param instanceof WorkflowOutputDataHandling) {
            workflow_output_data_handling = param;
        }
    };

    this.getSecurityContext = function () {
        return security_context;
    };
    this.setSecurityContext = function (param) {
        if(param instanceof SecurityContext){
            security_context = param;
        }
    };
}
/*
function UserIdentifier (){
    var  user_identifier;
    this.setIdentifier = function (identifier) {
        user_identifier = identifier;
    }
    this.getIdentifier = function (){
        return user_identifier;
    }

}*/

function SOAServiceEPR (){
    var gfacURL
    var registryURL;
    var resourceSchedulerURL;
    var hostDescriptor;

    this.getGfacURL = function (){
        return gfacURL;
    };
    this.setGfacURL = function (url){
        gfacURL = url;
    };

    this.getRegistryURL = function (){
        return registryURL;
    };
    this.setRegistryURL = function (regURL){
        registryURL = regURL;
    };

    this.getResorceSchedulerURL = function (){
        return resourceSchedulerURL;
    };
    this.setResourceSchedulerURL = function (resURL) {
        resourceSchedulerURL = resURL;
    };

    this.getHostDescriptor = function (){
        return hostDescriptor;
    };
    this.setHostDescriptor = function (hostDes){
        hostDescriptor = hostDes;
    };

}

function WorkflowMonitoringContext(){
    var experimentId,
        workflowInstanceId,
        workflowTemplateId,
        workflowNodeId,
        workflowTimeStep,
        serviceInstanceId,
        serviceReplicaId,
        eventPublishEPR,
        notificationTopic,
        errorPublishEPR,
        msgBoxEPR;

    this.getExperimentId   = function (){
        return experimentId;
    };
    this.setExperimentId  = function (exId){
        experimentId = exId;
    };

    this.getWorkflowInstanceId   = function (){
        return workflowInstanceId;
    };
    this.setWorkflowInstanceId  = function (wfId){
        workflowInstanceId = wfId;
    };

    this.getWorkflowTemplateId   = function (){
        return    workflowTemplateId;
    };
    this.setWorkflowTemplateId  = function (wfTemplateId){
        workflowTemplateId = wfTemplateId;
    };

    this.getWorkflowNodeId   = function (){
        return  workflowNodeId;
    };
    this.setWorkflowNodeId  = function (wfNodeId){
        workflowNodeId = wfNodeId;
    };

    this.getWorkflowTimeStep   = function (){
        return  workflowTimeStep;
    };
    this.setWorkflowTimeStep  = function (wts){
        workflowTimeStep =  wts;
    };

    this.getServiceInstanceId   = function (){
        return serviceInstanceId;
    };
    this.setServiceInstanceId  = function (serInsId){
        serviceInstanceId = serInsId;
    };

    this.getServiceReplicaId   = function (){
        return  serviceReplicaId;
    };
    this.setServiceReplicaId  = function (serRepId){
        serviceReplicaId = serRepId;
    };

    this.getEventPublishEPR   = function (){
        return   eventPublishEPR;
    };
    this.setEventPublishEPR  = function (eventPubEPR){
         eventPublishEPR = eventPubEPR;
    };

    this.getNotificationTopic   = function (){
        return  notificationTopic;
    };
    this.setNotificationTopic  = function (notTopic){
        notificationTopic = notTopic;
    };

    this.getErrorPublishEPR   = function (){
        return errorPublishEPR;
    };
    this.setErrorPublishEPR  = function (errorPubEPR){
        errorPublishEPR = errorPubEPR;
    };

    this.getMsgBoxEPR   = function (){
        return msgBoxEPR;
    };
    this.setMsgBoxEPR  = function (msgBoxEpr){
        msgBoxEPR = msgBoxEpr;
    };

}

function WorkflowSchedulingContext (){
    var appSchedulingContexts = [];
    var nameValuePairs = [];

    this.getApplicationSchedulingContexts =  function (){
        return appSchedulingContexts;
    };

    this.addApplicationSchedulingContext = function (appScheContext){
        if(appScheContext instanceof ApplicationSchedulingContext) {
            appSchedulingContexts.push(appScheContext);
        }else{
            // TODO handle this.
        }
    };

    this.getNameValuePairs = function (){
        return nameValuePairs;
    };
    this.addNameValuePair = function (nvPair) {
        if(nvPair instanceof NameValuePair) {
            nameValuePairs.push(nvPair)
        }else{
            // TODO handle this.
        }
    };

}

function WorkflowOutputDataHandling(){
    var appOutputDataHandles = [];

    this.getApplicationOutputDataHandles =  function (){
        return appOutputDataHandles;
    };

    this.addApplicationOutputDataHandle = function (appOutputDataContext){
        if(appOutputDataContext instanceof ApplicationOutputDataContext) {
            appOutputDataHandles.push(appOutputDataContext);
        }else{
            // TODO handle this.
        }
    };



}

function SecurityContext(){
    var gridProxy,
        gridMyProxyRepo = new GridProxyRepository(),
        sshAuthentication = new SSHAuthentication(),
        credentialManagementService = new CredentialManagementService(),
        amazonWebservice = new AmazonWebservice();

    this.getGridProxy = function (){
        return gridProxy;
    };
    this.setGridProxy = function (param){
        gridProxy = param;
    };

    this.getGridMyProxyRepo = function (){
        return gridMyProxyRepo;
    };
    this.setGridMyProxyRepo = function (param){
        if(param instanceof GridProxyRepository){
            gridMyProxyRepo = param;
        }
    };

    this.getSSHAuthentication = function (){
        return sshAuthentication;
    };
    this.setSSHAuthentication = function (param){
        if(param instanceof SSHAuthentication){
            sshAuthentication = param;
        }
    };

    this.getCredentialManagementService = function (){
       return credentialManagementService;
    };
    this.setCredentialManagementService = function (param){
        if(param instanceof  CredentialManagementService){
            credentialManagementService  = param;
        }
    };

    this.getAmazonWebservice = function (){
        return amazonWebservice;
    };
    this.setAmazonWebservice = function (param){
        if(param instanceof AmazonWebservice) {
            amazonWebservice = param;
        }
    };

}

function ApplicationSchedulingContext(){
    var workflowNodeId,
        serviceId,
        hostName,
        wsgramPreferred,
        gatekeeperEPR,
        jobManager,
        cpuCount,
        nodeCount,
        queueName,
        maxWallTime;

    this.getWorkflowNodeId = function (){
        return  workflowNodeId;
    };
    this.setWorkflowNodeId = function (param){
        workflowNodeId  = param;
    };
    this.getServiceId = function (){
        return  serviceId;
    };
    this.setServiceId = function (param){
        serviceId  = param;
    };
    this.getHostName = function (){
        return hostName;
    };
    this.setHostName = function (param){
         hostName = param;
    };
    this.getWsgramPreferred = function (){
        return wsgramPreferred;
    };
    this.setWsgramPreferred = function (param){
         wsgramPreferred = param;
    };
    this.getGatekeeperEPR = function (){
        return  gatekeeperEPR;
    };
    this.setGatekeeperEPR = function (param){
         gatekeeperEPR = param;
    };
    this.getJobManager = function (){
        return  jobManager;
    };
    this.setJobManager = function (param){
         jobManager = param;
    };
    this.getCpuCount = function (){
        return cpuCount;
    };
    this.setCpuCount = function (param){
         cpuCount = param;
    };
    this.getNodeCount = function (){
        return nodeCount;
    };
    this.setNodeCount = function (param){
         nodeCount = param;
    };
    this.getQueueName = function (){
        return  queueName
    };
    this.setQueueName = function (param){
         queueName = param;
    };
    this.getMaxWallTime = function (){
        return  maxWallTime;
    };
    this.setMaxWallTime = function (param){
         maxWallTime = param;
    };
}

function NameValuePair (){
    var workflowNodeId,
        name,
        value,
        description;

    this.getWorkflowNodeId = function (){
        return workflowNodeId;
    };
    this.setWorkflowNodeId = function (param){
        workflowNodeId = param;
    };

    this.getName = function (){
        return name;
    };
    this.setName = function (param){
        name = param;
    };

    this.getValue = function (){
        return value;
    };
    this.setValue = function (param){
        value = param;
    };

    this.getDescription = function (){
        return description;
    };
    this.setDescription = function (param){
        description = param;
    };

}

function ApplicationOutputDataContext () {
    var nodeId,
       dataRegistryURL,
       outputDataDirectory,
       dataPersistence;

    this.getNodeId = function (){
       return nodeId;
    };
    this.setNodeId = function (param){
       nodeId  = param;
    };

    this.getDataRegistryURL = function (){
       return dataRegistryURL;
    };
    this.setDataRegistryURL = function (param){
        dataRegistryURL = param;
    };

    this.getOutputDataDirectory = function (){
       return outputDataDirectory;
    };
    this.setOutputDataDirectory = function (param){
       outputDataDirectory  = param;
    };

    this.getDataPersistence = function (){
       return dataPersistence;
    };
    this.setDataPersistence = function (param){
        dataPersistence = param;
    };

}

function GridProxyRepository (){
    var myproxyServer,
        username,
        password,
        lifeTimeInHours;

    this.getMyproxyServer = function (){
        return myproxyServer;
    };
    this.setMyproxyServer = function (param){
        myproxyServer = param;
    };

    this.getUsername = function (){
        return username;
    };
    this.setUsername = function (param){
        username = param;
    };

    this.getPassword = function (){
        return password;
    };
    this.setPassword = function (param){
        password = param;
    };

    this.getLifeTimeInHours = function (){
        return lifeTimeInHours;
    };
    this.setLifeTimeInHours = function (param){
        lifeTimeInHours = param;
    };

}

function SSHAuthentication(){
    var accessKeyId,
        secretAccessKey;

    this.getAccessKeyId = function (){
        return accessKeyId;
    };
    this.setAccessKeyId = function (param){
        accessKeyId = param;
    };

    this.getSecretAccessKey = function (){
        return secretAccessKey;
    };
    this.setSecretAccessKey = function (param){
        secretAccessKey = param;
    };

}

function CredentialManagementService(){
    var tokenId,
        portalUser;

    this.getTokenId = function (){
        return tokenId;
    };
    this.setTokenId = function (param){
        tokenId = param;
    };

    this.getPortalUser = function (){
        return portalUser;
    };
    this.setPortalUser = function (param){
        portalUser = param;
    };

}

function AmazonWebservice (){
    var accessKeyId,
        secretAccessKey,
        amiId,
        instanceId,
        instanceType,
        username;

    this.getAccessKeyId = function (){
        return accessKeyId;
    };
    this.setAccessKeyId = function (param){
        accessKeyId = param;
    };

    this.getSecretAccessKey = function (){
        return secretAccessKey;
    };
    this.setSecretAccessKey = function (param){
        secretAccessKey = param;
    };

    this.getAmiId = function (){
        return amiId;
    };
    this.setAmiId = function (param){
        amiId = param;
    };

    this.getInstanceId = function (){
        return instanceId;
    };
    this.setInstanceId = function (param){
        instanceId = param;
    };

    this.getInstanceType = function (){
        return instanceType;
    };
    this.setInstanceType = function (param){
        instanceType = param;
    };

    this.getUsername = function (){
        return username;
    };
    this.setUsername = function (param){
        username = param;
    };

}


function WorkflowContextHeader (){

    this.getContextHeader_JSON = function (contextHeader){
        if(contextHeader instanceof ContextHeader) {
            var json_contextHeader = {"context-header" :{
                "user-identifier" : contextHeader.getUserIdentifier(),
                "soa-service-eprs": getSOAServiceEPR_JSON(contextHeader.getSOAServiceEPR()),
                "workflow-monitoring-context": getWorkflowMonitoringContext_JSON(contextHeader.getWorkflowMonitoringContext()),
                "workflow-scheduling-context": getWorkflowSchedulingContext_JSON(contextHeader.getWorkflowSchedulingContext()),
                "workflow-output-data-handling": getWorkflowOutputDataHandling_JSON(contextHeader.getWorkflowOutputDataHandling()),
                "security-context": getSecurityContext_JSON(contextHeader.getSecurityContext())
            }};
            return JSON.stringify(json_contextHeader);
        }
    };

    var getSOAServiceEPR_JSON = function (soaServiceEPR){
        if(soaServiceEPR instanceof SOAServiceEPR){
            return {"gfac-url": soaServiceEPR.getGfacURL(),
                "registry-url": soaServiceEPR.getRegistryURL(),
                "resource-scheduler-url": soaServiceEPR.getResorceSchedulerURL(),
                "hostDescriptor": soaServiceEPR.getHostDescriptor()
            };

        }
    };

    var getWorkflowMonitoringContext_JSON = function (wfMonitoringContext){
        if(wfMonitoringContext instanceof WorkflowMonitoringContext) {
            return {"experiment-id" :wfMonitoringContext.getExperimentId(),
                "workflow-instance-id": wfMonitoringContext.getWorkflowInstanceId(),
                "workflow-template-id": wfMonitoringContext.getWorkflowTemplateId(),
                "workflow-node-id": wfMonitoringContext.getWorkflowNodeId(),
                "workflow-time-step": wfMonitoringContext.getWorkflowTimeStep(),
                "service-instance-id": wfMonitoringContext.getServiceInstanceId(),
                "service-replica-id": wfMonitoringContext.getServiceReplicaId(),
                "event-publish-epr": wfMonitoringContext.getEventPublishEPR(),
                "notification-topic": wfMonitoringContext.getNotificationTopic(),
                "error-publish-epr": wfMonitoringContext.getErrorPublishEPR(),
                "msg-box-epr": wfMonitoringContext.getMsgBoxEPR()
            };
        }

    };

    var getWorkflowSchedulingContext_JSON = function(wfSchedulingContext){
        if(wfSchedulingContext instanceof WorkflowSchedulingContext) {
            var applicationArray = getApplicationSchedulingContexts_JSON(wfSchedulingContext.getApplicationSchedulingContexts());
            var nameValueArray = getNameValuePairs_JSON(wfSchedulingContext.getNameValuePairs());

            var test = {"application-scheduling-context": eval(applicationArray),
                "nameValuePairType":eval(nameValueArray)
            };
            return (test);
        }
    };

    var getWorkflowOutputDataHandling_JSON = function (wfoDataHandling){
        if(wfoDataHandling instanceof WorkflowOutputDataHandling) {
            return {"application-output-data-handling": eval(getApplicationOutputDataHandlings_JSON(wfoDataHandling.getApplicationOutputDataHandles()))}

        }
    };

    var getSecurityContext_JSON = function (secContext){
        if(secContext instanceof SecurityContext) {
            return {
                "grid-proxy": secContext.getGridProxy(),
                "grid-myproxy-repository":{
                    "myproxy-server": secContext.getGridMyProxyRepo().getMyproxyServer(),
                    "username": secContext.getGridMyProxyRepo().getUsername(),
                    "password": secContext.getGridMyProxyRepo().getPassword(),
                    "life-time-inhours": secContext.getGridMyProxyRepo().getLifeTimeInHours()
                },
                "ssh-authentication":{
                    "access-key-id": secContext.getSSHAuthentication().getAccessKeyId(),
                    "secret-access-key": secContext.getSSHAuthentication().getSecretAccessKey()
                },
                "credential-management-service": {
                    "token_id": secContext.getCredentialManagementService().getTokenId(),
                    "portal-user": secContext.getCredentialManagementService().getPortalUser()
                },
                "amazon-webservices": {
                    "access-key-id": secContext.getAmazonWebservice().getAccessKeyId(),
                    "secret-access-key": secContext.getAmazonWebservice().getSecretAccessKey(),
                    "ami-id": secContext.getAmazonWebservice().getAmiId(),
                    "instance-id": secContext.getAmazonWebservice().getInstanceId(),
                    "instance-type": secContext.getAmazonWebservice().getInstanceType(),
                    "username": secContext.getAmazonWebservice().getUsername()
                }
            }
        }
    };

    var getApplicationSchedulingContexts_JSON = function (appContexts){
        var buffer =[];
        if(appContexts){
            for(var i=0; i < appContexts.length; i++) {
                buffer.push(eval(getApplicationScheduleContext_JSON(appContexts[i])))
            }
        }
        var j = buffer;
        return j;



    };

    var getApplicationScheduleContext_JSON = function (appContext){
        if(appContext instanceof ApplicationSchedulingContext){
           return {
               "@workflow-node-id": appContext.getWorkflowNodeId(),
               "@service-id": appContext.getServiceId(),
               "@host-name": appContext.getHostName(),
               "@wsgram-preferred": appContext.getWsgramPreferred(),
               "@gatekeeper-epr": appContext.getGatekeeperEPR(),
               "@job-manager": appContext.getJobManager(),
               "@cpu-count": appContext.getCpuCount(),
               "@node-count": appContext.getNodeCount(),
               "@queue-name": appContext.getQueueName(),
               "@max-wall-time": appContext.getMaxWallTime()
           };
        }
    };

    var getNameValuePairs_JSON = function (nameValuePairs){
        var buffer=[];
        if(nameValuePairs){
            for(var i=0; i < nameValuePairs.length; i++) {
                buffer.push(eval(getNameValuePair_JSON(nameValuePairs[i])))
            }
        }
        return buffer;
    };

    var getNameValuePair_JSON = function (nameValue){
        if(nameValue instanceof NameValuePair) {
            return {
                "@workflow-node-id": nameValue.getWorkflowNodeId(),
                "@name": nameValue.getName(),
                "@value": nameValue.getValue(),
                "@description": nameValue.getDescription()
            }
        }

    };

    var getApplicationOutputDataHandlings_JSON = function (appOutputDataHandlings){
        var buffer =[];
        if(appOutputDataHandlings){
            for(var i =0 ; i < appOutputDataHandlings.length; i ++) {
                buffer.push(eval(getApplicationOutputData_JSON(appOutputDataHandlings[i])))
            }
        }
        return buffer;
    };

    var getApplicationOutputData_JSON = function (appOutputData){
        if(appOutputData instanceof ApplicationOutputDataContext) {
            return {
                "node-id": appOutputData.getNodeId(),
                "data-registry-url": appOutputData.getDataRegistryURL(),
                "output-data-directory": appOutputData.getOutputDataDirectory(),
                "data-persistance": appOutputData.getDataPersistence()
            }
        }
    };
}

function JsonSchemaTest (){
    var contextHeader = new ContextHeader();
    contextHeader.setUserIdentifier("testIdentifier");

    var soa = new SOAServiceEPR();
    soa.setGfacURL("http://localhost/gfac");
    soa.setRegistryURL("http://localhost/reg");
    soa.setResourceSchedulerURL("http://localhost/resource");
    soa.setHostDescriptor("sampleHost");
    contextHeader.setSOAServiceEPR(soa);

    var wfMonitor = new WorkflowMonitoringContext();
    wfMonitor.setExperimentId("unique124");
    wfMonitor.setWorkflowInstanceId("inst123");
    wfMonitor.setWorkflowTemplateId("template123");
    wfMonitor.setWorkflowNodeId("node123");
    wfMonitor.setWorkflowTimeStep(123);
    wfMonitor.setServiceInstanceId("serInst123");
    wfMonitor.setServiceReplicaId("serRep123");
    wfMonitor.setEventPublishEPR("http://localhost/eventpub");
    wfMonitor.setNotificationTopic("notificationTopic");
    wfMonitor.setErrorPublishEPR("http://localhost/errorPubEpr");
    wfMonitor.setMsgBoxEPR("http://localhost/msg/box");
    contextHeader.setWorkflowMonitoringContext(wfMonitor);

    var wfScheduler = new WorkflowSchedulingContext();
    var appSchduler1 = new ApplicationSchedulingContext();
    appSchduler1.setWorkflowNodeId("wfNodeId123");
    appSchduler1.setServiceId("appService123");
    appSchduler1.setHostName("host1");
    appSchduler1.setWsgramPreferred(true);
    appSchduler1.setGatekeeperEPR("http://localhost/gatekeeper");
    appSchduler1.setJobManager("shamJM");
    appSchduler1.setCpuCount(12);
    appSchduler1.setNodeCount(3);
    appSchduler1.setQueueName("queue1");
    appSchduler1.setMaxWallTime(321);
    wfScheduler.addApplicationSchedulingContext(appSchduler1);

    var nameValue_1 = new NameValuePair();
    nameValue_1.setWorkflowNodeId("node123");
    nameValue_1.setName("John");
    nameValue_1.setValue("carter");
    nameValue_1.setDescription("Simple description");
    var nameValue_2 = new NameValuePair();
    nameValue_2.setWorkflowNodeId("node1234");
    nameValue_2.setName("John2");
    nameValue_2.setValue("carter2");
    nameValue_2.setDescription("Simple description");

    wfScheduler.addNameValuePair(nameValue_1);
    wfScheduler.addNameValuePair(nameValue_2);
    contextHeader.setWorkflowSchedulingContext(wfScheduler);

    var wfOutput = new WorkflowOutputDataHandling();
    var appOutput = new ApplicationOutputDataContext();
    appOutput.setNodeId("node123");
    appOutput.setDataRegistryURL("http://localhost/data/reg");
    appOutput.setOutputDataDirectory("/home/user/Desktop");
    appOutput.setDataPersistence(false);

    wfOutput.addApplicationOutputDataHandle(appOutput);
    contextHeader.setWorkflowOutputDataHandling(wfOutput);

    var secCon = new SecurityContext();
    secCon.setGridProxy("sample.proxy.com");
    var gridRepo = new GridProxyRepository();
    gridRepo.setMyproxyServer("my.sample.proxy.com");
    gridRepo.setUsername("grid");
    gridRepo.setPassword("grid123");
    gridRepo.setLifeTimeInHours(34);
    secCon.setGridMyProxyRepo(gridRepo);
    var sshAuth = new SSHAuthentication();
    sshAuth.setAccessKeyId("sshActionkey");
    sshAuth.setSecretAccessKey("sectretSSHKey");
    secCon.setSSHAuthentication(sshAuth);
    var credMan = new CredentialManagementService();
    credMan.setTokenId("credToken");
    credMan.setPortalUser("Smith");
    secCon.setCredentialManagementService(credMan);
    var amazon = new AmazonWebservice();
    amazon.setAccessKeyId("amazonKeyId");
    amazon.setSecretAccessKey("amozonSecretKey");
    amazon.setAmiId("amozonAMI");
    amazon.setInstanceId("amIns123");
    amazon.setInstanceType("amzInsType");
    amazon.setUsername("amazonUser");
    secCon.setAmazonWebservice(amazon);

    contextHeader.setSecurityContext(secCon);

    var wfch = new WorkflowContextHeader();
    var jsonData = wfch.getContextHeader_JSON(contextHeader);

    console.log(jsonData);
//    var schema = tv4.getSchema('#/js/model/workflow_execution_context.json');
//    var valid = t4.validate(jsonData, schema);
//    alert(valid);

}



