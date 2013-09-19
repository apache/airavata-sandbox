/**
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */

'use strict';

// Service for Workflow API
angular.module('WebUI').factory('workflowAPI', function(){

  var ContextHeader = function  (){

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
  var SOAServiceEPR = function (){
    var gfacURL;
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
  var WorkflowMonitoringContext = function (){
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
  var WorkflowSchedulingContext = function  (){
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
  var WorkflowOutputDataHandling = function (){
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
  var SecurityContext = function (){
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
  var ApplicationSchedulingContext = function (){
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
  var NameValuePair = function  (){
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
  var ApplicationOutputDataContext = function  () {
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
  var GridProxyRepository= function  (){
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
  var SSHAuthentication = function (){
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
  var CredentialManagementService = function (){
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
  var AmazonWebservice = function  (){
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
  var WorkflowContextHeader = function  (){

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

  //Workflow API , returns instance of objects
  return{
    ContextHeader: ContextHeader,
    SOAServiceEPR: SOAServiceEPR,
    WorkflowMonitoringContext: WorkflowMonitoringContext,
    WorkflowSchedulingContext: WorkflowSchedulingContext,
    WorkflowOutputDataHandling: WorkflowOutputDataHandling,
    SecurityContext: SecurityContext,
    ApplicationSchedulingContext: ApplicationSchedulingContext,
    NameValuePair: NameValuePair,
    ApplicationOutputDataContext: ApplicationOutputDataContext,
    GridProxyRepository: GridProxyRepository,
    SSHAuthentication: SSHAuthentication,
    CredentialManagementService: CredentialManagementService,
    AmazonWebservice: AmazonWebservice,
    WorkflowContextHeader: WorkflowContextHeader
  };
});