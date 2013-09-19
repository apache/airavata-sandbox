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

function  test(){
    alert("Test works and mount also works");
}


function GateWay(name) {
    this.gatewayName = name;
}

function setHeaders(name, value, headers) {
    headers.push = {name: value};
}

function AiravataUser() {
    this.userName;
}
function HostDescriptor(hName, hAddress) {
    this.hostname = hName;
    this.hostAddress = hAddress;

    this.hostType = [];                     // String array
    this.gridFTPEndPoint = [];              // String array
    this.gateKeeperEndPoint = [];            // String array
    this.imageID = [];                      // String array
    this.instanceID = [];                   // String array
}

function ServiceDescriptor(sName, desc) {
    this.serviceName = sName;
    this.description = desc;
    this.inputParams = [];
    this.outputParams = [];
}

function ApplicationDescriptor() {
    this.name;
    this.hostdescName;
//    serviceName;
    this.executablePath;
    this.workingDir;
    this.jobType;
    this.projectNumber;
    this.projectDescription;
    this.queueName;
    this.applicationDescType;
    this.inputDir;
    this.outputDir;
    this.stdIn;
    this.stdOut;
    this.stdError;
    this.staticWorkingDir;
    // int set
    this.maxWallTime;
    this.cpuCount;
    this.nodeCount;
    this.processorsPerNode;
    this.minMemory;
    this.maxMemory;

    this.environmentVariables;     // HashMap<String, String>
    this.serviceDescriptor;      //  ServiceDescriptor

}

function WorkflowNodeGramData(nodeId, rsl, invockedHost, gramJobId, workflowInstanaceId) {
    this.NodeID = nodeId;
    this.rsl = rsl;
    this.invokedHost = invockedHost;
    this.gramJobID = gramJobId;
    this.workflowInstanceId = workflowInstanaceId;
}

function ExecutionError() {
    this.source;                // ExecutionErrorSource
    this.errorTime;              // Date
    this.errorCode;
    this.errorMessage;
    this.errorDescription;
    this.errorLocation;
    this.actionTaken;
    this.errorReported;
    this.errorReference;          // int
}

var ExecutionErrorSource = {
    ALL: 'ALL',
    EXPERIMENT: 'EXPERIMENT',
    WORKFLOW: 'WORKFLOW',
    NODE: 'NODE',
    APPLICATION: 'APPLICATION'
};

function ExperimentExecutionError() {
    ExecutionError.call(this);
    this.experimentId;
}

ExperimentExecutionError.prototype = Object.create(ExecutionError.prototype);

function WorkflowExecutionError() {
    ExecutionError.class(this);
    this.experimentId;
    this.workflowInstanceId;
}

WorkflowExecutionError.prototype = Object.create(ExecutionError.prototype);

function NodeExecutionError() {
    ExecutionError.call(this);
    this.experimentId;
    this.workflowInstanceId;
    this.nodeId;
}

NodeExecutionError.prototype = Object.create(ExecutionError.prototype);

function ApplicationJobExecutionError() {
    ExecutionError.call(this);
    this.experimentId;
    this.workflowInstanceId;
    this.nodeId;
    this.jobId;
}

ApplicationJobExecutionError.prototype = Object.create(ApplicationJobExecutionError.prototype);


var ApplicatonJobStatus = {
    VALIDATE_INPUT: "VALIDATE_INPUT",
    STAGING: "STAGING",
    AUTHENTICATE: "AUTHENTICATE",
    INITIALIZE: "INITIALIZE",
    SUBMITTED: "SUBMITTED",
    PENDING: "PENDING",
    EXECUTING: "EXECUTING",
    SUSPENDED: "SUSPENDED",
    WAIT_FOR_DATA: "WAIT_FOR_DATA",
    FINALIZE: "FINALIZE",
    RESULTS_GEN: "RESULTS_GEN",
    RESULTS_RETRIEVE: "RESULTS_RETRIEVE",
    VALIDATE_OUTPUT: "VALIDATE_OUTPUT",
    FINISHED: "FINISHED",
    FAILED: "FAILED",
    CANCELLED: "CANCELLED",
    UNKNOWN: "UNKNOWN"
}

function ApplicationJob() {
    this.experimentId;
    this.workflowExecutionId;
    this.nodeId;

    this.serviceDescriptionId;
    this.hostDescriptionId;
    this.applicationDescriptionId;

    this.jobId;
    this.jobData;

    this.submittedTime;          // Date
    this.statusUpdateTime;       // Date
    this.status;                  // ApplicationJobStatus

    this.metadata;
}

var airavataBasicHeaders = {"Authorization": "Basic YWRtaW46YWRtaW4=",
    "Accept": "application/json", "Content-Type": "application/json"};

var optionalBasicHeaders = {"Authorization": "Basic YWRtaW46YWRtaW4=",
    "Accept": "*/*", "Content-Type": "application/json"};

var formBasicHeaders = {"Authorization": "Basic YWRtaW46YWRtaW4=",
    "Accept": "*/*", "Content-Type": "application/x-www-form-urlencoded"};

var baseURL = {"BASE_RES_PATH": "/airavata/services/registry"};

var type = {get: "GET", post: "POST", delete: "DELETE"};

var basicResourcePaths = { "REGISTRY_API_BASICREGISTRY": "/basicregistry/",
    "GET_GATEWAY": "get/gateway",
    "GET_USER": "get/user",
    "SET_GATEWAY": "set/gateway",
    "SET_USER": "set/user",
    "VERSION": "get/version",
    "GET_SERVICE_URL": "get/serviceURL",
    "SET_SERVICE_URL": "set/serviceURL"};

function sendAndReceive(url, headers, type, data, onSuccess) {
    return $.ajax({
        url: url,
        headers: headers,
        type: type,
        data: data,
        async: false,
        success: onSuccess
    });
}

function sendRobust(url, headers, type, data, onSuccess) {
    return $.ajax({
        url: url,
        headers: headers,
        type: type,
        data: data,
        success: onSuccess
    });
}


// ====================================== Util functions =====================================================

function createSampleHostDesc() {
    this.hostDesc = new HostDescription();
    this.hostDesc.hostName = "Remote host";
    this.hostDesc.hostAddress = "100.10.2.233";
    return this.hostDesc;
}


// ====================================== Exception functions =====================================================

function Exception(msg) {
    if (msg) {
        this.message = msg;
    } else {
        this.message = "Exception thrown";
    }
}

Exception.prototype.getMessage = function () {
    return this.message;
};

Exception.prototype.setMessage = function (newMsg) {
    this.message = newMsg;
}

// ######### Throw this exception if subclass doesn't implement abstract method ############
function MethodNotImplementedException(msg) {
    Exception.call(this, msg);
    if (msg) {
        this.message = msg;
    }
}

MethodNotImplementedException.prototype = Object.create(Exception.prototype);

// ====================================== AiravataClient =====================================================

function AiravataJSClient() {
    var basicRegistry = new BasicRegistry();
    var descriptorRegistry = new DescriptorRegistry();
    var configurationRegistry = new ConfigurationRegistry();
    var provenanceRegistry = new ProvenanceRegistry();
    var projectRegistry = new ProjectRegistry();
    var publishWorkflowRegistry = new PublishWorkflowRegistry();
    var userWorkflowRegistry = new UserWorkflowRegistry();
    var experimentRegistry = new ExperimentRegistry();

    this.getGateWay = function () {
        return basicRegistry.getGateWay();
    };

    this.setGateway = function (gateway) {
        return basicRegistry.setGateway(gateway);
    };

    this.getUserName = function () {
        return basicRegistry.getUserName();
    };

    this.setAiravataUser = function (airavataUser) {
        return basicRegistry.setAiravataUser(airavataUser);
    };

    this.getVersion = function () {
        return basicRegistry.getVersion();
    };

    this.getConnectionURL = function () {
        return basicRegistry.getConnectionURL();
    };

    this.setConnectionURL = function (connectionURL) {
        return basicRegistry.setConnectionURL(connectionURL);
    };

    // =============== Descriptor wrapper =====================

    this.isHostDescriptorExists = function (hostDescName) {
        return descriptorRegistry.isHostDescriptorExists(hostDescName);
    };

    this.addHostDescriptor = function (hostDescriptor) {
        return descriptorRegistry.addHostDescriptor(hostDescriptor);
    };

    this.updateHostDescriptor = function (updatedHostDesc) {
        return descriptorRegistry.updateHostDescriptor(updatedHostDesc);
    };

    this.getHostDescriptor = function (hostName) {
        return descriptorRegistry.getHostDescriptor(hostName);
    };

    this.removeHostDescriptor = function (hostName) {
        return descriptorRegistry.removeHostDescriptor(hostName);
    };

    this.getHostDescriptors = function () {
        return descriptorRegistry.getHostDescriptors();
    };


    this.getHostDescriptorNames = function () {
        return descriptorRegistry.getHostDescriptorNames();
    };

    this.isServiceDescriptorExists = function (serviceDescName) {
        return descriptorRegistry.isServiceDescriptorExists(serviceDescName);
    };

    this.addServiceDescriptor = function (serviceDescriptor) {
        return descriptorRegistry.addServiceDescriptor(serviceDescriptor);
    };

    this.updateServiceDescriptor = function (serviceDescriptor) {
        return descriptorRegistry.updateServiceDescriptor(serviceDescriptor);
    };

    this.getServiceDescriptor = function (serviceDescName) {
        return descriptorRegistry.getServiceDescriptor(serviceDescName);
    };

    this.removeServiceDescriptor = function (serviceDescName) {
        return descriptorRegistry.removeServiceDescriptor(serviceDescName);
    };

    this.getServiceDescriptors = function () {
        return descriptorRegistry.getServiceDescriptors();
    };

    this.isApplicationDescriptorExists = function (appDesc, hostName, appDescName) {
        return descriptorRegistry.isApplicationDescriptorExists(appDesc, hostName, appDescName);
    };

    this.addApplicationDescriptor = function (appDescriptor) {
        return descriptorRegistry.addApplicationDescriptor(appDescriptor);
    };

    this.updateApplicationDescriptor = function (appDescriptor) {
        return descriptorRegistry.updateApplicationDescriptor(appDescriptor);
    };

    this.getApplicationDescriptor = function (serviceName, hostName, applicationName) {
        return descriptorRegistry.getApplicationDescriptor(serviceName, hostName, applicationName);
    };

    this.getApplicationDescriptorPerServiceHost = function (serviceName, hostName) {
        return descriptorRegistry.getApplicationDescriptorPerServiceHost(serviceName, hostName);
    };

    this.getApplicationDescriptors = function (serviceName) {
        if(serviceName) {
            return descriptorRegistry.getApplicationDescriptors(serviceName);
        }else{
            return descriptorRegistry.getApplicationDescriptors();
        }

    };

    this.getApplicationDescriptorNames = function () {
        return descriptorRegistry.getApplicationDescriptorNames();
    };

    this.removeApplicationDescriptor = function (serviceName, hostName, appName) {
        return descriptorRegistry.removeApplicationDescriptor(serviceName, hostName, appName);
    };

    // =============== Configuration wrapper =====================

    this.getConfiguration = function (key) {
        return configurationRegistry.getConfiguration(key);
    };

    this.getConfigurationList = function (key) {
        return configurationRegistry.getConfigurationList(key);
    };

    this.setConfiguration = function (key, value, date) {
        return configurationRegistry.setConfiguration(key, value, date);
    };

    this.updateConfiguration = function (key, value, date) {
        return configurationRegistry.updateConfiguration(key, value, date);
    };

    this.removeAllConfiguration = function (key) {
        return configurationRegistry.removeAllConfiguration(key);
    };

    this.removeConfiguration = function (key, value) {
        return configurationRegistry.removeConfiguration(key, value);
    };

    this.getGFacURIs = function () {
        return configurationRegistry.getGFacURIs();
    };

    this.getWorkflowInterpreterURIs = function () {
        return configurationRegistry.getWorkflowInterpreterURIs();
    };

    this.getEventingServiceURI = function () {
        return configurationRegistry.getEventingServiceURI();
    };

    this.getMessageBoxURI = function () {
        return configurationRegistry.getMessageBoxURI();
    };

    this.addGFacURI = function (uri) {
        return configurationRegistry.addGFacURI(uri);
    };

    this.addWorkflowInterpreterURI = function (uri) {
        return configurationRegistry.addWorkflowInterpreterURI(uri);
    };

    this.setEventingURI = function (uri) {
        return configurationRegistry.setEventingURI(uri);
    };

    this.setMessageBoxURI = function (uri) {
        return configurationRegistry.setMessageBoxURI(uri);
    };


    this.addGFacURIByDate = function (uri, date) {
        return configurationRegistry.addGFacURIByDate(uri, date);
    };

    this.addWorkflowInterpreterURI = function (uri, date) {
        return configurationRegistry.addWorkflowInterpreterURI(uri, date);
    };

    this.setEventingURIByDate = function (uri, date) {
        return configurationRegistry.setEventingURIByDate(uri, date);
    };

    this.setMessageBoxURIByDate = function (uri, date) {
        return configurationRegistry.setMessageBoxURIByDate(uri, date);
    };

    this.removeGFacURI = function (uri) {
        return configurationRegistry.removeGFacURI(uri);
    };

    this.removeAllGFacURI = function () {
        return configurationRegistry.removeAllGFacURI();
    };

    this.removeWorkflowInterpreterURI = function (uri) {
        return configurationRegistry.removeWorkflowInterpreterURI(uri);
    };

    this.removeAllWorkflowInterpreterURI = function () {
        return configurationRegistry.removeAllWorkflowInterpreterURI();
    };

    this.unsetEventingURI = function () {
        return configurationRegistry.unsetEventingURI();
    };

    this.unsetMessageBoxUwRI = function () {
        return configurationRegistry.unsetMessageBoxUwRI();
    };


// ========================================= Provenance Wrapper ==========================================

    this.updateExperimentExecutionUser = function (experimentId, user) {
        return provenanceRegistry.updateExperimentExecutionUser(experimentId, user);
    };

    this.getExperimentExecutionUser = function (experimentId) {
        return provenanceRegistry.getExperimentExecutionUser(experimentId);
    };

    this.getExperimentName = function (experimentId) {
        return provenanceRegistry.getExperimentName(experimentId);
    };

    this.updateExperimentName = function (experimentId, experimentName) {
        return provenanceRegistry.updateExperimentName(experimentId, experimentName);
    };

    this.getExperimentMetadata = function (experimentId) {
        return provenanceRegistry.getExperimentMetadata(experimentId);
    };


    this.updateExperimentMetadata = function (experimentId, metadata) {
        return provenanceRegistry.updateExperimentMetadata(experimentId, metadata);
    };


    this.getWorkflowExecutionTemplateName = function (workflowInstanceId) {
        return provenanceRegistry.getWorkflowExecutionTemplateName(workflowInstanceId);
    };


    this.setWorkflowInstanceTemplateName = function (workflowInstanceId, templateName) {
        return provenanceRegistry.setWorkflowInstanceTemplateName(workflowInstanceId, templateName);
    };

    this.getExperimentWorkflowInstances = function (experimentId) {
        return provenanceRegistry.getExperimentWorkflowInstances(experimentId);
    };

    this.isWorkflowInstanceExists = function (instanceId) {
        return provenanceRegistry.isWorkflowInstanceExists(instanceId);
    };

    this.isWorkflowInstanceExistsThenCreate = function (instanceId, createIfNotPresent) {
        return provenanceRegistry.isWorkflowInstanceExistsThenCreate(instanceId, createIfNotPresent);
    };

    this.updateWorkflowInstanceStatusByInstance = function (instanceId, executionStatus) {
        return provenanceRegistry.updateWorkflowInstanceStatusByInstance(instanceId, executionStatus);
    };

    this.updateWorkflowInstanceStatus = function (workflowInstanceId, executionStatus, statusUpdateTime) {
        return provenanceRegistry.updateWorkflowInstanceStatus(workflowInstanceId, executionStatus, statusUpdateTime);
    };

    this.getWorkflowInstanceStatus = function (instanceId) {
        return provenanceRegistry.getWorkflowInstanceStatus(instanceId);
    };

    this.updateWorkflowNodeInput = function (nodeId, workflowInstanceId, data) {
        return provenanceRegistry.updateWorkflowNodeInput(nodeId, workflowInstanceId, data);
    };

    this.updateWorkflowNodeOutput = function (nodeId, workflowInstanceId, data) {
        return provenanceRegistry.updateWorkflowNodeOutput(nodeId, workflowInstanceId, data);
    };

    this.getExperiment = function (experimentId) {
        return provenanceRegistry.getExperiment(experimentId);
    };

    this.getExperimentIdByUser = function (username) {
        return provenanceRegistry.getExperimentIdByUser(username);
    };

    this.getExperimentByUser = function (username) {
        return provenanceRegistry.getExperimentByUser(username);
    };


    this.updateWorkflowNodeStatus = function (workflowInstanceId, nodeId, executionStatus) {
        return provenanceRegistry.updateWorkflowNodeStatus(workflowInstanceId, nodeId, executionStatus);
    };

    this.getWorkflowNodeStatus = function (workflowInstanceId, nodeId) {
        return projectRegistry.getWorkflowNodeStatus(workflowInstanceId, nodeId);
    };

    this.getWorkflowNodeStartTime = function (workflowInstanceId, nodeId) {
        return provenanceRegistry.getWorkflowNodeStartTime(workflowInstanceId, nodeId);
    };

    this.getWorkflowStartTime = function (workflowInstanceId) {
        return provenanceRegistry.getWorkflowStartTime(workflowInstanceId);
    };

    this.updateWorkflowNodeGramData = function (workflowNodeGramData) {
        return provenanceRegistry.updateWorkflowNodeGramData(workflowNodeGramData);
    };

    this.getWorkflowInstanceData = function (workflowInstanceId) {
        return provenanceRegistry.getWorkflowInstanceData(workflowInstanceId);
    };

    this.isWorkflowInstanceNodePresent = function (workflowInstanceId, nodeId) {
        return provenanceRegistry.isWorkflowInstanceNodePresent(workflowInstanceId, nodeId);
    };

    this.isWorkflowInstanceNodePresentCreate = function (workflowInstanceId, nodeId, createIfNotPresent) {
        return provenanceRegistry.isWorkflowInstanceNodePresentCreate(workflowInstanceId, nodeId, createIfNotPresent);
    };

    this.getWorkflowInstanceNodeData = function (workflowInstanceId, nodeId) {
        return provenanceRegistry.getWorkflowInstanceNodeData(workflowInstanceId, nodeId);
    };

    this.addWorkflowInstance = function (experimentId, workflowInstanceId, templateName) {
        return provenanceRegistry.addWorkflowInstance(experimentId, workflowInstanceId, templateName);
    };

    this.updateWorkflowNodeType = function (workflowInstanceId, nodeId, nodeType) {
        return projectRegistry.updateWorkflowNodeType(workflowInstanceId, nodeId, nodeType);
    };

    this.addWorkflowInstanceNode = function (workflowInstanceId, nodeId) {
        return projectRegistry.addWorkflowInstanceNode(workflowInstanceId, nodeId);
    };

    this.isExperimentNameExist = function (experimentName) {
        return projectRegistry.isExperimentNameExist(experimentName);
    };

    this.getExperimentMetaInformation = function (experimentId) {
        return projectRegistry.getExperimentMetaInformation(experimentId);
    };

    this.getAllExperimentMetaInformation = function (user) {
        return projectRegistry.getAllExperimentMetaInformation(user);
    };

    this.searchExperiments = function (user, experimentNameRegex) {
        return projectRegistry.searchExperiments(user, experimentNameRegex);
    };

    this.getExperimentExecutionErrors = function (experimentId) {
        return projectRegistry.getExperimentExecutionErrors(experimentId);
    };

    this.getWorkflowExecutionErrors = function (experimentId, workflowInstanceId) {
        return projectRegistry.getWorkflowExecutionErrors(experimentId, workflowInstanceId);
    };

    this.getNodeExecutionErrors = function (experimentId, workflowInstanceId, nodeId) {
        return projectRegistry.getNodeExecutionErrors(experimentId, workflowInstanceId, nodeId);
    };

    this.getGFacJobErrors = function (experimentId, workflowInstanceId, nodeId, gfacJobId) {
        return projectRegistry.getGFacJobErrors(experimentId, workflowInstanceId, nodeId, gfacJobId);
    };

    this.getAllGFacJobErrors = function (gfacJobId) {
        return projectRegistry.getAllGFacJobErrors(gfacJobId);
    };

    this.getGFacJobErrors = function (experimentId, workflowInstanceId, nodeId, gfacJobId, sourceFilter) {
        return projectRegistry.getGFacJobErrors(experimentId, workflowInstanceId, nodeId, gfacJobId, sourceFilter);
    };

    this.addExperimentError = function (experimentExecutionError) {
        return projectRegistry.addExperimentError(experimentExecutionError);
    };

    this.addWorkflowError = function (workflowExecutionError) {
        return projectRegistry.addWorkflowError(workflowExecutionError);
    };

    this.addNodeExecutionError = function (nodeExecutionError) {
        return projectRegistry.addNodeExecutionError(nodeExecutionError);
    };

    this.addGFacJobExecutionError = function (applicationJobExecutionError) {
        return projectRegistry.addGFacJobExecutionError(applicationJobExecutionError);
    };

    this.addApplicationJob = function (applicationJob) {
        return projectRegistry.addApplicationJob(applicationJob);
    };

    this.updateApplicationJob = function (applicationJob) {
        return projectRegistry.updateApplicationJob(applicationJob);
    };

    this.updateApplicationJobStatus = function (gfacJobID, gfacJobStatus, statusUpdateDate) {
        return projectRegistry.updateApplicationJobStatus(gfacJobID, gfacJobStatus, statusUpdateDate);
    };

    this.updateApplicationJobData = function (gfacJobID, jobdata) {
        return projectRegistry.updateApplicationJobData(gfacJobID, jobdata);
    };

    this.updateApplicationJobSubmittedTime = function (gfacJobID, submittedDate) {
        return projectRegistry.updateApplicationJobSubmittedTime(gfacJobID, submittedDate);
    };

    this.updateApplicationJobMetadata = function (gfacJobID, metadata) {
        return projectRegistry.updateApplicationJobMetadata(gfacJobID, metadata);
    };

    this.getApplicationJob = function (gfacJobId) {
        return projectRegistry.getApplicationJob(gfacJobId);
    };

    this.getApplicationJobsForDescriptors = function (serviceDescriptionId, hostDescriptionId) {
        return projectRegistry.getApplicationJobsForDescriptors(serviceDescriptionId, hostDescriptionId);
    };

    this.getApplicationJobs = function (experimentId, workflowExecutionId, nodeId) {
        return projectRegistry.getApplicationJobs(experimentId, workflowExecutionId, nodeId);
    };

    this.isApplicationJobExists = function (gfacJobId) {
        return projectRegistry.isApplicationJobExists(gfacJobId);
    };

    this.getApplicationJobStatusHistory = function (gfacJobId) {
        return projectRegistry.getApplicationJobStatusHistory(gfacJobId);
    };


// ========================================= Experiment Wrapper ==========================================


    this.removeExperiment = function (experimentId) {
        return experimentRegistry.removeExperiment(experimentId);
    };

    this.getExperiments = function () {
        return experimentRegistry.getExperiments();
    };

    this.getExperimentsByProject = function (projectName) {
        return experimentRegistry.getExperimentsByProject(projectName);
    };

    this.getExperimentsByDate = function (fromDate, toDate) {
        return experimentRegistry.getExperimentsByDate(fromDate, toDate);
    };

    this.getExperimentsByProjectDate = function (projectName, fromDate, toDate) {
        return experimentRegistry.getExperimentsByProjectDate(projectName, fromDate, toDate);
    };


    this.addExperiment = function (projectName, experimentID, submittedDate) {
        return experimentRegistry.addExperiment(projectName, experimentID, submittedDate);
    };


    this.isExperimentExists = function (experimentID) {
        return experimentRegistry.isExperimentExists(experimentID);
    };

    this.isExperimentExistsThenCreate = function (experimentID, createIfNotPresent) {
        return experimentRegistry.isExperimentExistsThenCreate(experimentID, createIfNotPresent);
    };

// ========================================= Project Wrapper ==========================================

    this.isWorkspaceProjectExists = function (projectName) {
        return projectRegistry.isWorkspaceProjectExists(projectName);
    };

    this.isWorkspaceProjectExistsCreate = function (projectName, createIfNotPresent) {
        return projectRegistry.isWorkspaceProjectExistsCreate(projectName, createIfNotPresent);
    };

    this.addWorkspaceProject = function (projectName) {
        return projectRegistry.addWorkspaceProject(projectName);
    };

    this.updateWorkspaceProject = function (projectName) {
        return projectRegistry.updateWorkspaceProject(projectName);
    };

    this.deleteWorkspaceProject = function (projectName) {
        return projectRegistry.deleteWorkspaceProject(projectName);
    };

    this.getWorkspaceProject = function (projectName) {
        return projectRegistry.getWorkspaceProject(projectName);
    };

    this.getWorkspaceProjects = function () {
        return projectRegistry.getWorkspaceProjects();
    };


// ========================================= PublishWorkflow wrapper ==========================================


    this.isPublishedWorkflowExists = function (workflowName) {
        return publishWorkflowRegistry.isPublishedWorkflowExists(workflowName);
    };

    this.publishWorkflow = function (workflowName, publishWorkflowName) {
        return publishWorkflowRegistry.publishWorkflow(workflowName, publishWorkflowName);
    };

    this.publishDefaultWorkflow = function (workflowName) {
        return publishWorkflowRegistry.publishDefaultWorkflow(workflowName);
    };

    this.getPublishedWorkflowGraphXML = function (workflowName) {
        return publishWorkflowRegistry.getPublishedWorkflowGraphXML(workflowName);
    };

    this.getPublishedWorkflowNames = function () {
        return publishWorkflowRegistry.getPublishedWorkflowNames();
    };

    this.getPublishedWorkflows = function () {
        return publishWorkflowRegistry.getPublishedWorkflows();
    };

    this.removePublishedWorkflow = function (workflowName) {
        return publishWorkflowRegistry.removePublishedWorkflow(workflowName);
    };

// ========================================= UserWorkflow Wrapper ==========================================


    this.isWorkflowExists = function (workflowName) {
        return userWorkflowRegistry.isWorkflowExists(workflowName);
    };

    this.addWorkflow = function (workflowName, workflowGraphXml) {
        return userWorkflowRegistry.addWorkflow(workflowName, workflowGraphXml);
    };

    this.updateWorkflow = function (workflowName, workflowGraphXml) {
        return userWorkflowRegistry.updateWorkflow(workflowName, workflowGraphXml);
    };

    this.getWorkflowGraphXML = function (workflowName) {
        return userWorkflowRegistry.getWorkflowGraphXML(workflowName);
    };

    this.getWorkflows = function () {
        return userWorkflowRegistry.getWorkflows();
    };

    this.removeWorkflow = function (workflowName) {
        return userWorkflowRegistry.removeWorkflow(workflowName);
    };

} // END of AiravataClient function


// ====================================== Basic Registry Resource =====================================================
function BasicRegistry() {
    this.basicRegistryResourcePath = baseURL.BASE_RES_PATH + basicResourcePaths.REGISTRY_API_BASICREGISTRY;
}

BasicRegistry.prototype.getGateWay = function () {
    var url, gWay;
    url = this.basicRegistryResourcePath + basicResourcePaths.GET_GATEWAY;
    gWay = sendAndReceive(url, airavataBasicHeaders, type.get, null, null);
    return gWay.responseJSON;

};

BasicRegistry.prototype.setGateway = function (gateway) {
    var url, data, gway;
    if (gateway instanceof GateWay) {
        url = this.basicRegistryResourcePath + basicResourcePaths.SET_GATEWAY;
        data = JSON.stringify(gateway);
        gWay = sendAndReceive(url, optionalBasicHeaders, type.post, data, null);
        return gWay.responseText;
    } else {
        return new Exception("Expected instanceof GateWay");
    }
};

BasicRegistry.prototype.getUserName = function () {
    var url, user;
    url = this.basicRegistryResourcePath + basicResourcePaths.GET_USER;
    user = sendAndReceive(url, airavataBasicHeaders, type.get, null, null);
    alert(user.responseText);
    return user.responseJSON;
};

BasicRegistry.prototype.setAiravataUser = function (airavataUser) {
    var url, res, data;
    url = this.basicRegistryResourcePath + basicResourcePaths.SET_USER;
    data = JSON.stringify(airavataUser);
    res = sendAndReceive(url, optionalBasicHeaders, type.post, data, null);
    return res;
};

BasicRegistry.prototype.getVersion = function () {
    var url , res;
    url = this.basicRegistryResourcePath + basicResourcePaths.VERSION;
    res = sendAndReceive(url, airavataBasicHeaders, type.get, null, null);
    return res.responseJSON;
};

BasicRegistry.prototype.getConnectionURL = function () {
    var url, res;
    url = this.basicRegistryResourcePath+ basicResourcePaths.GET_SERVICE_URL;
    res = sendAndReceive(url, optionalBasicHeaders, type.get, null, null);
    return res;
};

BasicRegistry.prototype.setConnectionURL = function (connectionURL) {
    var url,formParam, res;
    formParam = "?connectionurl=" + connectionURL;
    url = this.basicRegistryResourcePath + basicResourcePaths.SET_SERVICE_URL + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res;
};

// ========================================= Descriptor Registry Resource ==========================================

var descResourcePaths = { "DESC_RESOURCE_PATH": "/descriptorsregistry/",
    "HOST_DESC_EXISTS": "hostdescriptor/exist",
    "HOST_DESC_SAVE": "hostdescriptor/save",
    "HOST_DESC_UPDATE": "hostdescriptor/update",
    "HOST_DESC": "host/description",
    "HOST_DESC_DELETE": "hostdescriptor/delete",
    "GET_HOST_DESCS": "get/hostdescriptors",
    "GET_HOST_DESCS_NAMES": "get/hostdescriptor/names",
    "SERVICE_DESC_EXISTS": "servicedescriptor/exist",
    "SERVICE_DESC_SAVE": "servicedescriptor/save",
    "SERVICE_DESC_UPDATE": "servicedescriptor/update",
    "SERVICE_DESC": "servicedescriptor/description",
    "SERVICE_DESC_DELETE": "servicedescriptor/delete",
    "GET_SERVICE_DESCS": "get/servicedescriptors",
    "APPL_DESC_EXIST": "applicationdescriptor/exist",
    "APP_DESC_BUILD_SAVE": "applicationdescriptor/build/save",
    "APP_DESC_UPDATE": "applicationdescriptor/update",
    "APP_DESC_DESCRIPTION": "applicationdescriptor/description",
    "APP_DESC_PER_HOST_SERVICE": "applicationdescriptors/alldescriptors/host/service",
    "APP_DESC_ALL_DESCS_SERVICE": "applicationdescriptor/alldescriptors/service",
    "APP_DESC_ALL_DESCRIPTORS": "applicationdescriptor/alldescriptors",
    "APP_DESC_NAMES": "applicationdescriptor/names",
    "APP_DESC_DELETE": "applicationdescriptor/delete"
};

function DescriptorRegistry() {
    this.descRegistryResourcePath = baseURL.BASE_RES_PATH + descResourcePaths.DESC_RESOURCE_PATH;
};

DescriptorRegistry.prototype.isHostDescriptorExists = function (hostDescName) {
    var url, data, res;
    url = this.descRegistryResourcePath + descResourcePaths.HOST_DESC_EXISTS;
    data = {hostDescriptorName: hostDescName};
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res;
};

DescriptorRegistry.prototype.addHostDescriptor = function (hostDescriptor) {
    var url, data, res;
    if (hostDescriptor instanceof HostDescriptor) {
        url = this.descRegistryResourcePath + descResourcePaths.HOST_DESC_SAVE;
        data = JSON.stringify(hostDescriptor);
        res = sendAndReceive(url, airavataBasicHeaders, type.post, data, null);
        return res.responseJSON;
    } else {
        // TODO throw an exception
    }
};

DescriptorRegistry.prototype.updateHostDescriptor = function (updatedHostDesc) {
    var url, data, res;
    if (updatedHostDesc instanceof hostDescriptor) {
        url = this.descRegistryResourcePath + descResourcePaths.HOST_DESC_UPDATE;
        data = JSON.stringify(updatedHostDesc);
        res = sendAndReceive(url, airavataBasicHeaders, type.post, data, null);
        return res.responseJSON;
    } else {
        // TODO throw an exception
    }
};

DescriptorRegistry.prototype.getHostDescriptor = function (hostName) {
    var url , data, res;
    url = this.descRegistryResourcePath + descResourcePaths.HOST_DESC;
    data = {hostName: hostName};
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res.responseJSON;
};

DescriptorRegistry.prototype.removeHostDescriptor = function (hostName) {
    var url,formParam, res;
    formParam = "?hostName=" + hostName;
    url = this.descRegistryResourcePath + descResourcePaths.HOST_DESC_DELETE + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.delete, null, null);
    return res.responseText;
};

DescriptorRegistry.prototype.getHostDescriptors = function () {
    var url, res;
    url = this.descRegistryResourcePath + descResourcePaths.GET_HOST_DESCS;
    res = sendAndReceive(url, airavataBasicHeaders, type.get, null, null);
    return res.responseJOSN;
};


DescriptorRegistry.prototype.getHostDescriptorNames = function () {
    var url, res;
    url = this.descRegistryResourcePath + descResourcePaths.GET_HOST_DESCS_NAMES;
    res = sendAndReceive(url, airavataBasicHeaders, type.get, null, null);
    return res.responseJSON;
};

DescriptorRegistry.prototype.isServiceDescriptorExists = function (serviceDescName) {
    var url, data, res;
    url = this.descRegistryResourcePath + descResourcePaths.SERVICE_DESC_EXISTS;
    data = {serviceDescriptorName: serviceDescName};
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res.responseJSON;
};

DescriptorRegistry.prototype.addServiceDescriptor = function (serviceDescriptor) {
    var url, data, res;
    if (serviceDescriptor instanceof ServiceDescriptor) {
        url = this.descRegistryResourcePath + descResourcePaths.SERVICE_DESC_SAVE;
        data = JSON.stringify(serviceDescriptor);
        res = sendAndReceive(url, airavataBasicHeaders, type.post, data, null);
    } else {
        // TODO throw an exception
    }
};

DescriptorRegistry.prototype.updateServiceDescriptor = function (serviceDescriptor) {
    var url, data, res;
    if (serviceDescriptor instanceof ServiceDescriptor) {
        url = this.descRegistryResourcePath + descResourcePaths.SERVICE_DESC_UPDATE;
        data = JSON.stringify(serviceDescriptor);
        res = sendAndReceive(url, airavataBasicHeaders, type.post, data, null);
        return res.responseJSON;
    } else {
        // TODO throw an exception
    }
};

DescriptorRegistry.prototype.getServiceDescriptor = function (serviceDescName) {
    var url, data, res;
    if (serviceDescName) {
        url = this.descRegistryResourcePath + descResourcePaths.SERVICE_DESC;
        data = {serviceName: serviceDescName};
        res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
        return res.responseJSON;
    } else {
        // TODO throw an exception
    }
};

DescriptorRegistry.prototype.removeServiceDescriptor = function (serviceDescName) {
    var url, data, res;
    if (serviceDescName) {
        url = this.descRegistryResourcePath + descResourcePaths.SERVICE_DESC_DELETE;
        data = {serviceName: serviceDescName};
        res = sendAndReceive(url, airavataBasicHeaders, type.delete, data, null);
        return res.responseJSON;
    } else {
        // TODO throw an exception
    }
};

DescriptorRegistry.prototype.getServiceDescriptors = function () {
    var url, res;
    url = this.descRegistryResourcePath + descResourcePaths.GET_SERVICE_DESCS;
    res = sendAndReceive(url, airavataBasicHeaders, type.get, null, null);
    return res.responseJSON;
};

DescriptorRegistry.prototype.isApplicationDescriptorExists = function (appDesc, hostName, appDescName) {
    var url, data, res;
    url = this.descRegistryResourcePath + descResourcePaths.APPL_DESC_EXIST;
    data = {serviceName: appDesc, hostName: hostName, appDescName: appDescName};
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res.responseText;
    // TODO check this function
};

DescriptorRegistry.prototype.addApplicationDescriptor = function (appDescriptor) {
    var url, data, res;
    if (appDescriptor instanceof ApplicationDescriptor) {
        url = this.descRegistryResourcePath + descResourcePaths.APP_DESC_BUILD_SAVE;
        data = JSON.stringify(appDescriptor);
        res = sendAndReceive(url, airavataBasicHeaders, type.post, data, null);
        return res.responseJSON;
    } else {
        // TODO throw an exception
    }
};

DescriptorRegistry.prototype.updateApplicationDescriptor = function (appDescriptor) {
    var url, data, res;
    if (appDescriptor) {
        url = this.descRegistryResourcePath + descResourcePaths.APP_DESC_UPDATE;
        data = JSON.stringify(appDescriptor);
        res = sendAndReceive(url, airavataBasicHeaders, type.post, data, null);
        return res.responseJSON;
    }
};

DescriptorRegistry.prototype.getApplicationDescriptor = function (serviceName, hostName, applicationName) {
    var url, data, res;
    url = this.descRegistryResourcePath + descResourcePaths.APP_DESC_DESCRIPTION;
    data = {serviceName: serviceName, hostName: hostName, appDescName: applicationName};
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res.responseJSON;
};

DescriptorRegistry.prototype.getApplicationDescriptorPerServiceHost = function (serviceName, hostName) {
    var url, data, res;
    url = this.descRegistryResourcePath + descResourcePaths.APP_DESC_PER_HOST_SERVICE;
    data = {serviceName: serviceName, hostName: hostName };
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res.responseJSON;
};

DescriptorRegistry.prototype.getApplicationDescriptors = function (serviceName) {
    var url, data, res;
    if (serviceName) {
        url = this.descRegistryResourcePath + descResourcePaths.APP_DESC_ALL_DESCS_SERVICE;
        data = {serviceName: serviceName};
        res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
        return res.responseJSON;
    }else{
        url = this.descRegistryResourcePath + descResourcePaths.APP_DESC_ALL_DESCRIPTORS;
        res = sendAndReceive(url, airavataBasicHeaders, type.get, null, null);
        return res.responseJSON;
    }
};

DescriptorRegistry.prototype.getApplicationDescriptorNames = function () {
    var url , res;
    url = this.descRegistryResourcePath + descResourcePaths.APP_DESC_NAMES;
    res = sendAndReceive(url, airavataBasicHeaders, type.get, null, null);
    return res.responseJSON;
};

DescriptorRegistry.prototype.removeApplicationDescriptor = function (serviceName, hostName, appName) {
    var url , data, res;
    url = this.descRegistryResourcePath + descResourcePaths.APP_DESC_DELETE;
    data = {serviceName: serviceName, hostName: hostName, appName: appName};
    res = sendAndReceive(url, airavataBasicHeaders, type.delete, data, null);
    return res.responseJSON;
};

// ========================================= Configuration Registry Resource ==========================================


// Resource path constants
var configResourcePathConstants = {
    CONFIGURATION_REGISTRY_RESOURCE: "/congfigregistry/",
    GET_CONFIGURATION: "get/configuration",
    GET_CONFIGURATION_LIST: "get/configurationlist",
    SAVE_CONFIGURATION: "save/configuration",
    UPDATE_CONFIGURATION: "update/configuration",
    DELETE_ALL_CONFIGURATION: "delete/allconfiguration",
    DELETE_CONFIGURATION: "delete/configuration",
    GET_GFAC_URI_LIST: "get/gfac/urilist",
    GET_WFINTERPRETER_URI_LIST: "get/workflowinterpreter/urilist",
    GET_EVENTING_URI: "get/eventingservice/uri",
    GET_MESSAGE_BOX_URI: "get/messagebox/uri",
    ADD_GFAC_URI: "add/gfacuri",
    ADD_WFINTERPRETER_URI: "add/workflowinterpreteruri",
    ADD_EVENTING_URI: "add/eventinguri",
    ADD_MESSAGE_BOX_URI: "add/msgboxuri",
    ADD_GFAC_URI_DATE: "add/gfacuri/date",
    ADD_WFINTERPRETER_URI_DATE: "add/workflowinterpreteruri/date",
    ADD_EVENTING_URI_DATE: "add/eventinguri/date",
    ADD_MSG_BOX_URI_DATE: "add/msgboxuri/date",
    DELETE_GFAC_URI: "delete/gfacuri",
    DELETE_ALL_GFAC_URIS: "delete/allgfacuris",
    DELETE_WFINTERPRETER_URI: "delete/workflowinterpreteruri",
    DELETE_ALL_WFINTERPRETER_URIS: "delete/allworkflowinterpreteruris",
    DELETE_EVENTING_URI: "delete/eventinguri",
    DELETE_MSG_BOX_URI: "delete/msgboxuri"
};


function ConfigurationRegistry() {
    this.configResorcePath = baseURL.BASE_RES_PATH + configResourcePathConstants.CONFIGURATION_REGISTRY_RESOURCE;
}

ConfigurationRegistry.prototype.getConfiguration = function (key) {
    var url, data, res;
    if (key) {
        url = this.configResorcePath + configResourcePathConstants.GET_CONFIGURATION;
        data = {key: key};
        res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
        return res.responseJSON;
    } else {
        // TODO throw an exception
    }
};

ConfigurationRegistry.prototype.getConfigurationList = function (key) {
    var url , data , res;
    if (key) {
        url = this.configResorcePath + configResourcePathConstants.GET_CONFIGURATION_LIST;
        data = {key: key};
        res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
        return res.responseJSON;
    } else {
        // TODO throw an exception
    }
};

ConfigurationRegistry.prototype.setConfiguration = function (key, value, date) {
    var url,formParam, res;
    formParam =  "?key=" + key + "&value=" + value + "&date=" + date;
    url = this.configResorcePath + configResourcePathConstants.SAVE_CONFIGURATION + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

ConfigurationRegistry.prototype.updateConfiguration = function (key, value, date) {
    var url,formParam, res;
    formParam = "?key=" + key + "&value=" + value + "&date=" + date;
    url = this.configResorcePath + configResourcePathConstants.UPDATE_CONFIGURATION + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

ConfigurationRegistry.prototype.removeAllConfiguration = function (key) {
    var url , res;
    url = this.configResorcePath + configResourcePathConstants.DELETE_ALL_CONFIGURATION;
    data = {key: key};
    res = sendAndReceive(url, optionalBasicHeaders, type.delete, data, null);
    return res.responseText;
};

ConfigurationRegistry.prototype.removeConfiguration = function (key, value) {
    var url, data , res;
    url = this.configResorcePath + configResourcePathConstants.DELETE_CONFIGURATION;
    data = {key: key, value: value};
    res = sendAndReceive(url, optionalBasicHeaders, type.delete, data, null);
    return res.responseText;
};

ConfigurationRegistry.prototype.getGFacURIs = function () {
    var url, res;
    url = this.configResorcePath + configResourcePathConstants.GET_GFAC_URI_LIST;
    res = sendAndReceive(url, optionalBasicHeaders, type.get, null, null);
    return res.responseJSON;
};

ConfigurationRegistry.prototype.getWorkflowInterpreterURIs = function () {
    var url, res;
    url = this.configResorcePath + configResourcePathConstants.GET_WFINTERPRETER_URI_LIST;
    res = sendAndReceive(url, airavataBasicHeaders, type.get, null, null);
    return res.responseJSON;
};

ConfigurationRegistry.prototype.getEventingServiceURI = function () {
    var url, res;
    url = this.configResorcePath + configResourcePathConstants.GET_EVENTING_URI;
    res = sendAndReceive(url, optionalBasicHeaders, type.get, null, null);
    return res.responseText;
};

ConfigurationRegistry.prototype.getMessageBoxURI = function () {
    var url, res;
    url = this.configResorcePath + configResourcePathConstants.GET_MESSAGE_BOX_URI;
    res = sendAndReceive(url, optionalBasicHeaders, type.get, null, null);
    return res.responseText;
};

ConfigurationRegistry.prototype.addGFacURI = function (uri) {
    var url,formParam, res;
    formParam = "?uri=" + uri;
    url = this.configResorcePath + configResourcePathConstants.ADD_GFAC_URI + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

ConfigurationRegistry.prototype.addWorkflowInterpreterURI = function (uri) {
    var url,formParam, res;
    formParam = "?uri=" + uri;
    if (uri) {
        url = this.configResorcePath + configResourcePathConstants.ADD_WFINTERPRETER_URI + formParam;
        res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
        return res.responseText;
    } else {
        // TODO throw an exception
    }
};

ConfigurationRegistry.prototype.setEventingURI = function (uri) {
    var url,formParam, res;
    formParam = "?uri=" + uri;
    if (uri) {
        url = this.configResorcePath + configResourcePathConstants.ADD_EVENTING_URI + formParam;
        res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
        return res.responseText;
    } else {
        // TODO throw an exception
    }
};

ConfigurationRegistry.prototype.setMessageBoxURI = function (uri) {
    var url, formParam, res;
    if (uri) {
        formParam = "?uri=" + uri;
        url = this.configResorcePath + configResourcePathConstants.ADD_MESSAGE_BOX_URI + formParam;
        res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
        return res.responseText;
    } else {
        // TODO throw an exception
    }
};


ConfigurationRegistry.prototype.addGFacURIByDate = function (uri, date) {
    var url, formParam, res;
    formParam = "?uri=" + uri + "&date=" + date;
    url = this.configResorcePath + configResourcePathConstants.ADD_GFAC_URI_DATE + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

ConfigurationRegistry.prototype.addWorkflowInterpreterURI = function (uri, date) {
    var url, formParam, res;
    formParam = "?uri=" + uri + "&date=" + date;
    url = this.configResorcePath + configResourcePathConstants.ADD_WFINTERPRETER_URI_DATE + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

ConfigurationRegistry.prototype.setEventingURIByDate = function (uri, date) {
    var url, formParam, res;
    formParam = "?uri=" + uri + "&date=" + date;
    url = this.configResorcePath + configResourcePathConstants.ADD_EVENTING_URI_DATE + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

ConfigurationRegistry.prototype.setMessageBoxURIByDate = function (uri, date) {
    var url, formParam, res;
    formParam = "?uri=" + uri + "&date=" + date;
    url = this.configResorcePath + configResourcePathConstants.ADD_MSG_BOX_URI_DATE + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

ConfigurationRegistry.prototype.removeGFacURI = function (uri) {
    var url, data, res;
    if (uri) {
        url = this.configResorcePath + configResourcePathConstants.DELETE_GFAC_URI;
        data = {uri: uri};
        res = sendAndReceive(url, optionalBasicHeaders, type.delete, data, null);
        return res.responseText;
    } else {
        // TODO throw an exception
    }
};

ConfigurationRegistry.prototype.removeAllGFacURI = function () {
    var url, res;
    url = this.configResorcePath + configResourcePathConstants.DELETE_ALL_GFAC_URIS;
    res = sendAndReceive(url, optionalBasicHeaders, type.delete, null, null);
    return res.responseText;
};

ConfigurationRegistry.prototype.removeWorkflowInterpreterURI = function (uri) {
    var url, data, res;
    url = this.configResorcePath + configResourcePathConstants.DELETE_WFINTERPRETER_URI;
    data = {uri: uri};
    res = sendAndReceive(url, optionalBasicHeaders, type.delete, data, null);
    return res.responseText;
};

ConfigurationRegistry.prototype.removeAllWorkflowInterpreterURI = function () {
    var url, res;
    url = this.configResorcePath + configResourcePathConstants.DELETE_ALL_WFINTERPRETER_URIS;
    res = sendAndReceive(url, optionalBasicHeaders, type.delete, null, null);
    return res.responseText;
};

ConfigurationRegistry.prototype.unsetEventingURI = function () {
    var url, res;
    url = this.configResorcePath + configResourcePathConstants.DELETE_EVENTING_URI;
    res = sendAndReceive(url, optionalBasicHeaders, type.delete, null, null);
    return res.responseText;
};

ConfigurationRegistry.prototype.unsetMessageBoxUwRI = function () {
    var url, res;
    url = this.configResorcePath + configResourcePathConstants.DELETE_MSG_BOX_URI;
    res = sendAndReceive(url, optionalBasicHeaders, type.delete, null, null);
    return res.responseText;
};


// ========================================= Provenance Registry Resource ==========================================

var provenanceResourcePathConstants = {

    REGISTRY_API_PROVENANCEREGISTRY: "/provenanceregistry/",
    UPDATE_EXPERIMENT_EXECUTIONUSER: "update/experiment/executionuser",
    GET_EXPERIMENT_EXECUTIONUSER: "get/experiment/executionuser",
    GET_EXPERIMENT_NAME: "get/experiment/name",
    UPDATE_EXPERIMENTNAME: "update/experimentname",
    GET_EXPERIMENTMETADATA: "get/experimentmetadata",
    UPDATE_EXPERIMENTMETADATA: "update/experimentmetadata",
    GET_WORKFLOWTEMPLATENAME: "get/workflowtemplatename",
    UPDATE_WORKFLOWINSTANCETEMPLATENAME: "update/workflowinstancetemplatename",
    GET_EXPERIMENTWORKFLOWINSTANCES: "get/experimentworkflowinstances",
    WORKFLOWINSTANCE_EXIST_CHECK: "workflowinstance/exist/check",
    WORKFLOWINSTANCE_EXIST_CREATE: "workflowinstance/exist/create",
    UPDATE_WORKFLOWINSTANCESTATUS_INSTANCEID: "update/workflowinstancestatus/instanceid",
    UPDATE_WORKFLOWINSTANCESTATUS: "update/workflowinstancestatus",
    GET_WORKFLOWINSTANCESTATUS: "get/workflowinstancestatus",
    UPDATE_WORKFLOWNODEINPUT: "update/workflownodeinput",
    UPDATE_WORKFLOWNODEOUTPUT: "update/workflownodeoutput",
    GET_EXPERIMENT: "get/experiment",
    GET_EXPERIMENT_ID_USER: "get/experimentId/user",
    GET_EXPERIMENT_USER: "get/experiment/user",
    UPDATE_WORKFLOWNODE_STATUS: "update/workflownode/status",
    GET_WORKFLOWNODE_STATUS: "get/workflownode/status",
    GET_WORKFLOWNODE_STARTTIME: "get/workflownode/starttime",
    GET_WORKFLOW_STARTTIME: "get/workflow/starttime",
    UPDATE_WORKFLOWNODE_GRAMDATA: "update/workflownode/gramdata",
    GET_WORKFLOWINSTANCEDATA: "get/workflowinstancedata",
    WORKFLOWINSTANCE_NODE_EXIST: "wfnode/exist",
    WORKFLOWINSTANCE_NODE_EXIST_CREATE: "wfnode/exist/create",
    WORKFLOWINSTANCE_NODE_DATA: "workflowinstance/nodeData",
    ADD_WORKFLOWINSTANCE: "add/workflowinstance",
    UPDATE_WORKFLOWNODETYPE: "update/workflownodetype",
    ADD_WORKFLOWINSTANCENODE: "add/workflowinstancenode",
    EXPERIMENTNAME_EXISTS: "experimentname/exists",

    GET_EXPERIMENT_METAINFORMATION: "get/experiment/metainformation",
    GET_ALL_EXPERIMENT_METAINFORMATION: "get/all/experiment/metainformation",
    SEARCH_EXPERIMENTS: "search/experiments",

    GET_EXPERIMENT_ERRORS: "experiment/errors",
    GET_WORKFLOW_ERRORS: "workflow/errors",
    GET_NODE_ERRORS: "node/errors",
    GET_GFAC_ERRORS: "gfac/errors",
    GET_ALL_GFAC_ERRORS: "gfac/all/errors",
    GET_EXECUTION_ERRORS: "execution/errors",
    ADD_EXPERIMENT_ERROR: "add/experiment/errors",
    ADD_WORKFLOW_ERROR: "add/workflow/errors",
    ADD_NODE_ERROR: "add/node/errors",
    ADD_GFAC_ERROR: "add/gfac/errors",
    ADD_APPLICATION_JOB: "add/application/job",
    UPDATE_APPLICATION_JOB: "update/application/job",
    UPDATE_APPLICATION_JOB_STATUS: "update/application/jobstatus",
    UPDATE_APPLICATION_JOB_DATA: "update/application/jobdata",
    UPDATE_APPLICATION_JOB_SUBMITTED_TIME: "update/application/job/submit",
    UPDATE_APPLICATION_JOB_COMPLETED_TIME: "update/application/job/complete",
    UPDATE_APPLICATION_JOB_METADATA: "update/application/job/metadata",
    GET_APPLICATION_JOB: "get/application/job",
    GET_APPLICATION_JOBS_FOR_DESCRIPTORS: "get/application/jobs/descriptors",
    GET_APPLICATION_JOBS: "get/application/jobs",
    APPLICATION_JOB_EXIST: "application/job/exists",
    GET_APPLICATION_JOBS_STATUS_HISTORY: "get/application/status/history"

};

function ProvenanceRegistry() {
    this.provenanceResourcePath = baseURL.BASE_RES_PATH + provenanceResourcePathConstants.REGISTRY_API_PROVENANCEREGISTRY;
}


ProvenanceRegistry.prototype.updateExperimentExecutionUser = function (experimentId, user) {
    var url, formParam, res;
    formParam = "?experimentId=" + experimentId + "&user=" + user;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.UPDATE_EXPERIMENT_EXECUTIONUSER + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

ProvenanceRegistry.prototype.getExperimentExecutionUser = function (experimentId) {
    var url, data, res;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.GET_EXPERIMENT_EXECUTIONUSER;
    data = {experimentId: experimentId};
    res = sendAndReceive(url, optionalBasicHeaders, type.get, data, null);
    return res.responseText;
};

ProvenanceRegistry.prototype.getExperimentName = function (experimentId) {
    var url, data, res;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.GET_EXPERIMENT_NAME;
    data = {experimentId: experimentId};
    res = sendAndReceive(url, optionalBasicHeaders, type.get, data, null);
    return res.responseText;
};

ProvenanceRegistry.prototype.updateExperimentName = function (experimentId, experimentName) {
    var url, formParam, res;
    formParam = "?experimentId=" + experimentId + "&experimentName=" + experimentName;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.UPDATE_EXPERIMENTNAME + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

ProvenanceRegistry.prototype.getExperimentMetadata = function (experimentId) {
    var url, data, res;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.GET_EXPERIMENTMETADATA;
    data = {experimentId: experimentId};
    res = sendAndReceive(url, optionalBasicHeaders, type.get, data, null);
    return res.responseText;
};


ProvenanceRegistry.prototype.updateExperimentMetadata = function (experimentId, metadata) {
    var url, formParam, res;
    formParam = "?experimentId=" + experimentId + "&metadata=" + metadata;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.UPDATE_EXPERIMENTMETADATA + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};


ProvenanceRegistry.prototype.getWorkflowExecutionTemplateName = function (workflowInstanceId) {
    var url, data, res;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.GET_WORKFLOWTEMPLATENAME;
    data = {workflowInstanceId: workflowInstanceId};
    res = sendAndReceive(url, optionalBasicHeaders, type.get, data, null);
    return res.responseText;
};


ProvenanceRegistry.prototype.setWorkflowInstanceTemplateName = function (workflowInstanceId, templateName) {
    var url, formParam, res;
    formParam = "?workflowInstanceId=" + workflowInstanceId + "&templateName=" + templateName;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.UPDATE_WORKFLOWINSTANCETEMPLATENAME + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

ProvenanceRegistry.prototype.getExperimentWorkflowInstances = function (experimentId) {
    var url, data, res;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.GET_EXPERIMENTWORKFLOWINSTANCES;
    data = {experimentId: experimentId};
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res.responseJSON;
};

ProvenanceRegistry.prototype.isWorkflowInstanceExists = function (instanceId) {
    var url, data, res;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.WORKFLOWINSTANCE_EXIST_CHECK;
    data = {instanceId: instanceId};
    res = sendAndReceive(url, optionalBasicHeaders, type.get, data, null);
    return res.responseText;
};

ProvenanceRegistry.prototype.isWorkflowInstanceExistsThenCreate = function (instanceId, createIfNotPresent) {
    var url, formParam, res;
    formParam = "?instanceId=" + instanceId + "&createIfNotPresent=" + createIfNotPresent;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.WORKFLOWINSTANCE_EXIST_CREATE + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

ProvenanceRegistry.prototype.updateWorkflowInstanceStatusByInstance = function (instanceId, executionStatus) {
    var url, formParam, res;
    formParam = "?instanceId=" + instanceId + "&executionStatus=" + executionStatus;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.UPDATE_WORKFLOWINSTANCESTATUS_INSTANCEID + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

ProvenanceRegistry.prototype.updateWorkflowInstanceStatus = function (workflowInstanceId, executionStatus, statusUpdateTime) {
    var url, formParam, res;
    formParam = "?workflowInstanceId=" + workflowInstanceId + "&executionStatus=" + executionStatus + "&statusUpdateTime=" + statusUpdateTime;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.UPDATE_WORKFLOWINSTANCESTATUS + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

ProvenanceRegistry.prototype.getWorkflowInstanceStatus = function (instanceId) {
    var url, data, res;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.GET_WORKFLOWINSTANCESTATUS;
    data = {instanceId: instanceId};
    res = sendAndReceive(url, optionalBasicHeaders, type.get, data, null);
    return res.responseText;
};

ProvenanceRegistry.prototype.updateWorkflowNodeInput = function (nodeId, workflowInstanceId, data) {
    var url, formParam, res;
    formParam = "?nodeId=" + nodeId + "&workflowInstanceId=" + workflowInstanceId + "&data=" + data;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.UPDATE_WORKFLOWNODEINPUT + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

ProvenanceRegistry.prototype.updateWorkflowNodeOutput = function (nodeId, workflowInstanceId, data) {
    var url, formParam, res;
    formParam = "?nodeId=" + nodeId + "&workflowInstanceId=" + workflowInstanceId + "&data=" + data;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.UPDATE_WORKFLOWNODEOUTPUT + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

ProvenanceRegistry.prototype.getExperiment = function (experimentId) {
    var url, data, res;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.GET_EXPERIMENT;
    data = {experimentId: experimentId};
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res.responseJSON;
};

ProvenanceRegistry.prototype.getExperimentIdByUser = function (username) {
    var url, data, res;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.GET_EXPERIMENT_ID_USER;
    data = {username: username};
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res.responseJSON;
};

ProvenanceRegistry.prototype.getExperimentByUser = function (username) {
    var url, data, res;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.GET_EXPERIMENT_USER;
    data = {username: username};
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res.responseJSON;
};


ProvenanceRegistry.prototype.updateWorkflowNodeStatus = function (workflowInstanceId, nodeId, executionStatus) {
    var url, formParam, res;
    formParam = "?workflowInstanceId=" + workflowInstanceId + "&nodeId=" + nodeId + "&executionStatus=" + executionStatus;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.UPDATE_WORKFLOWNODEOUTPUT + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

ProvenanceRegistry.prototype.getWorkflowNodeStatus = function (workflowInstanceId, nodeId) {
    var url, data, res;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.GET_WORKFLOWNODE_STATUS;
    data = {workflowInstanceId: workflowInstanceId, nodeId: nodeId};
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res.responseJSON;
};

ProvenanceRegistry.prototype.getWorkflowNodeStartTime = function (workflowInstanceId, nodeId) {
    var url, data, res;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.GET_WORKFLOWNODE_STARTTIME;
    data = {workflowInstanceId: workflowInstanceId, nodeId: nodeId};
    res = sendAndReceive(url, optionalBasicHeaders, type.get, data, null);
    return res.responseText;
};

ProvenanceRegistry.prototype.getWorkflowStartTime = function (workflowInstanceId) {
    var url, data, res;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.GET_WORKFLOW_STARTTIME;
    data = {workflowInstanceId: workflowInstanceId};
    res = sendAndReceive(url, optionalBasicHeaders, type.get, data, null);
    return res.responseText;
};

ProvenanceRegistry.prototype.updateWorkflowNodeGramData = function (workflowNodeGramData) {
    var url, data, res;
    if (workflowNodeGramData instanceof  WorkflowNodeGramData) {
        url = this.provenanceResourcePath + provenanceResourcePathConstants.UPDATE_WORKFLOWNODE_GRAMDATA;
        data = workflowNodeGramData;
        res = sendAndReceive(url, optionalBasicHeaders, type.post, data, null);
        return res.responseText;
    } else {
        // TODO throw an exception
    }
};

ProvenanceRegistry.prototype.getWorkflowInstanceData = function (workflowInstanceId) {
    var url, data, res;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.GET_WORKFLOWINSTANCEDATA;
    data = {workflowInstanceId: workflowInstanceId};
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res.responseJSON;
};

ProvenanceRegistry.prototype.isWorkflowInstanceNodePresent = function (workflowInstanceId, nodeId) {
    var url, data, res;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.WORKFLOWINSTANCE_NODE_EXIST;
    data = {workflowInstanceId: workflowInstanceId, nodeId: nodeId};
    res = sendAndReceive(url, optionalBasicHeaders, type.get, data, null);
    return res.responseJSON;
};

ProvenanceRegistry.prototype.isWorkflowInstanceNodePresentCreate = function (workflowInstanceId, nodeId, createIfNotPresent) {
    var url, formParam, res;
    formParam = "?workflowInstanceId=" + workflowInstanceId + "&nodeId=" + nodeId + "&createIfNotPresent=" + createIfNotPresent;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.WORKFLOWINSTANCE_NODE_EXIST_CREATE + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

ProvenanceRegistry.prototype.getWorkflowInstanceNodeData = function (workflowInstanceId, nodeId) {
    var url, data, res;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.WORKFLOWINSTANCE_NODE_DATA;
    data = {workflowInstanceId: workflowInstanceId, nodeId: nodeId};
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res.responseJSON;
};

ProvenanceRegistry.prototype.addWorkflowInstance = function (experimentId, workflowInstanceId, templateName) {
    var url, formParam, res;
    formParam = "?experimentId=" + experimentId + "&workflowInstanceId=" + workflowInstanceId + "&templateName=" + templateName;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.ADD_WORKFLOWINSTANCE + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

ProvenanceRegistry.prototype.updateWorkflowNodeType = function (workflowInstanceId, nodeId, nodeType) {
    var url, formParam, res;
    formParam = "?workflowInstanceId=" + workflowInstanceId + "&nodeId=" + nodeId + "&nodeType=" + nodeType;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.UPDATE_WORKFLOWNODETYPE + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

ProvenanceRegistry.prototype.addWorkflowInstanceNode = function (workflowInstanceId, nodeId) {
    var url, formParam, res;
    formParam = "?workflowInstanceId=" + workflowInstanceId + "&nodeId=" + nodeId;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.ADD_WORKFLOWINSTANCENODE + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

ProvenanceRegistry.prototype.isExperimentNameExist = function (experimentName) {
    var url, data, res;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.WORKFLOWINSTANCE_NODE_DATA;
    data = {experimentName: experimentName};
    res = sendAndReceive(url, optionalBasicHeaders, type.get, data, null);
    return res.responseText;
};

ProvenanceRegistry.prototype.getExperimentMetaInformation = function (experimentId) {
    var url, data, res;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.GET_EXPERIMENT_METAINFORMATION;
    data = {experimentId: experimentId};
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res.responseJSON;
};

ProvenanceRegistry.prototype.getAllExperimentMetaInformation = function (user) {
    var url, data, res;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.GET_ALL_EXPERIMENT_METAINFORMATION;
    data = {user: user};
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res.responseJSON;
};

ProvenanceRegistry.prototype.searchExperiments = function (user, experimentNameRegex) {
    var url, data, res;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.SEARCH_EXPERIMENTS;
    data = {user: user, experimentNameRegex: experimentNameRegex};
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res.responseJSON;
};

ProvenanceRegistry.prototype.getExperimentExecutionErrors = function (experimentId) {
    var url, data, res;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.GET_EXPERIMENT_ERRORS;
    data = {experimentId: experimentId};
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res.responseJSON;
};

ProvenanceRegistry.prototype.getWorkflowExecutionErrors = function (experimentId, workflowInstanceId) {
    var url, data, res;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.GET_EXPERIMENT_ERRORS;
    data = {experimentId: experimentId, workflowInstanceId: workflowInstanceId};
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res.responseJSON;
};

ProvenanceRegistry.prototype.getNodeExecutionErrors = function (experimentId, workflowInstanceId, nodeId) {
    var url, data, res;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.GET_NODE_ERRORS;
    data = {experimentId: experimentId, workflowInstanceId: workflowInstanceId, nodeId: nodeId};
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res.responseJSON;
};

ProvenanceRegistry.prototype.getGFacJobErrors = function (experimentId, workflowInstanceId, nodeId, gfacJobId) {
    var url, data, res;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.GET_GFAC_ERRORS;
    data = {experimentId: experimentId, workflowInstanceId: workflowInstanceId, nodeId: nodeId, gfacJobId: gfacJobId};
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res.responseJSON;
};

ProvenanceRegistry.prototype.getAllGFacJobErrors = function (gfacJobId) {
    var url, data, res;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.GET_ALL_GFAC_ERRORS;
    data = {gfacJobId: gfacJobId};
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res.responseJSON;
};

ProvenanceRegistry.prototype.getGFacJobErrors = function (experimentId, workflowInstanceId, nodeId, gfacJobId, sourceFilter) {
    var url, data, res;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.GET_EXECUTION_ERRORS;
    data = {experimentId: experimentId, workflowInstanceId: workflowInstanceId, nodeId: nodeId, gfacJobId: gfacJobId, sourceFilter: sourceFilter};
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res.responseJSON;
};

ProvenanceRegistry.prototype.addExperimentError = function (experimentExecutionError) {
    var url, data, res;
    if (experimentExecutionError instanceof ExperimentExecutionError) {
        url = this.provenanceResourcePath + provenanceResourcePathConstants.ADD_EXPERIMENT_ERROR;
        data = experimentExecutionError;
        res = sendAndReceive(url, optionalBasicHeaders, type.post, data, null);
        return res.responseText;
    } else {
        // TODO throw an exception
    }
};

ProvenanceRegistry.prototype.addWorkflowError = function (workflowExecutionError) {
    var url, data, res;
    if (workflowExecutionError instanceof WorkflowExecutionError) {
        url = this.provenanceResourcePath + provenanceResourcePathConstants.ADD_WORKFLOW_ERROR;
        data = workflowExecutionError;
        res = sendAndReceive(url, optionalBasicHeaders, type.post, data, null);
        return res.responseText;
    } else {
        // TODO throw an exception
    }
};

ProvenanceRegistry.prototype.addNodeExecutionError = function (nodeExecutionError) {
    var url, data, res;
    if (nodeExecutionError instanceof NodeExecutionError) {
        url = this.provenanceResourcePath + provenanceResourcePathConstants.ADD_NODE_ERROR;
        data = nodeExecutionError;
        res = sendAndReceive(url, optionalBasicHeaders, type.post, data, null);
        return res.responseText;
    } else {
        // TODO throw an exception
    }
};

ProvenanceRegistry.prototype.addGFacJobExecutionError = function (applicationJobExecutionError) {
    var url, data, res;
    if (applicationJobExecutionError instanceof ApplicationJobExecutionError) {
        url = this.provenanceResourcePath + provenanceResourcePathConstants.ADD_GFAC_ERROR;
        data = applicationJobExecutionError;
        res = sendAndReceive(url, optionalBasicHeaders, type.post, data, null);
        return res.responseText;
    } else {
        // TODO throw an exception
    }
};

ProvenanceRegistry.prototype.addApplicationJob = function (applicationJob) {
    var url, data, res;
    if (applicationJob instanceof ApplicationJob) {
        url = this.provenanceResourcePath + provenanceResourcePathConstants.ADD_APPLICATION_JOB;
        data = applicationJob;
        res = sendAndReceive(url, optionalBasicHeaders, type.post, data, null);
        return res.responseText;
    } else {
        // TODO throw an exception
    }
};

ProvenanceRegistry.prototype.updateApplicationJob = function (applicationJob) {
    var url, data, res;
    if (applicationJob instanceof ApplicationJob) {
        url = this.provenanceResourcePath + provenanceResourcePathConstants.UPDATE_APPLICATION_JOB;
        data = applicationJob;
        res = sendAndReceive(url, optionalBasicHeaders, type.post, data, null);
        return res.responseText;
    } else {
        // TODO throw an exception
    }
};

ProvenanceRegistry.prototype.updateApplicationJobStatus = function (gfacJobID, gfacJobStatus, statusUpdateDate) {
    var url, formParam, res;
    formParam = "?gfacJobID=" + gfacJobIDd + "&gfacJobStatus=" + gfacJobStatus + "&statusUpdateDate=" + statusUpdateDate;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.UPDATE_APPLICATION_JOB_STATUS + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

ProvenanceRegistry.prototype.updateApplicationJobData = function (gfacJobID, jobdata) {
    var url, formParam, res;
    formParam = "?gfacJobID=" + gfacJobIDd + "&jobdata=" + jobdata;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.UPDATE_APPLICATION_JOB_DATA + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

ProvenanceRegistry.prototype.updateApplicationJobSubmittedTime = function (gfacJobID, submittedDate) {
    var url, formParam, res;
    formParam = "?gfacJobID=" + gfacJobIDd + "&submittedDate=" + submittedDate;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.UPDATE_APPLICATION_JOB_SUBMITTED_TIME + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

ProvenanceRegistry.prototype.updateApplicationJobMetadata = function (gfacJobID, metadata) {
    var url, formParam, res;
    formParam = "?gfacJobID=" + gfacJobIDd + "&metadata=" + metadata;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.UPDATE_APPLICATION_JOB_METADATA + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

ProvenanceRegistry.prototype.getApplicationJob = function (gfacJobId) {
    var url, data, res;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.GET_APPLICATION_JOB;
    data = {gfacJobId: gfacJobId};
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res.responseJSON;
};

ProvenanceRegistry.prototype.getApplicationJobsForDescriptors = function (serviceDescriptionId, hostDescriptionId) {
    var url, data, res;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.GET_APPLICATION_JOBS_FOR_DESCRIPTORS;
    data = {serviceDescriptionId: serviceDescriptionId, hostDescriptionId: hostDescriptionId, applicationDescriptionId: applicationDescriptionId};
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res.responseJSON;
};

ProvenanceRegistry.prototype.getApplicationJobs = function (experimentId, workflowExecutionId, nodeId) {
    var url, data, res;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.GET_APPLICATION_JOBS;
    data = {experimentId: experimentId, workflowExecutionId: workflowExecutionId, nodeId: nodeId};
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res.responseJSON;
};

ProvenanceRegistry.prototype.isApplicationJobExists = function (gfacJobId) {
    var url, data, res;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.APPLICATION_JOB_EXIST;
    data = {gfacJobId: gfacJobId};
    res = sendAndReceive(url, optionalBasicHeaders, type.get, data, null);
    return res.responseText;
};

ProvenanceRegistry.prototype.getApplicationJobStatusHistory = function (gfacJobId) {
    var url, data, res;
    url = this.provenanceResourcePath + provenanceResourcePathConstants.APPLICATION_JOB_EXIST;
    data = {gfacJobId: gfacJobId};
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res.responseJSON;
};


// ========================================= Experiment Registry Resource ==========================================

var experimentResourcePathConstants = {
    EXP_RESOURCE_PATH: "/experimentregistry/",
    DELETE_EXP: "delete/experiment",
    GET_ALL_EXPS: "get/experiments/all",
    GET_EXPS_BY_PROJECT: "get/experiments/project",
    GET_EXPS_BY_DATE: "get/experiments/date",
    GET_EXPS_PER_PROJECT_BY_DATE: "get/experiments/project/date",
    ADD_EXP: "add/experiment",
    EXP_EXISTS: "experiment/exist",
    EXP_EXISTS_CREATE: "experiment/notexist/create"
};

function ExperimentRegistry() {
    this.experimentRegistryPath = baseURL.BASE_RES_PATH + experimentResourcePathConstants.EXP_RESOURCE_PATH;
}

ExperimentRegistry.prototype.removeExperiment = function (experimentId) {
    var url , data, res;
    if (experimentId) {
        url = this.experimentRegistryPath + experimentResourcePathConstants.DELETE_EXP;
        data = { experimentId: experimentId}
        res = sendAndReceive(url, optionalBasicHeaders, type.delete, data, null);
        return res.responseText;
    } else {
        // TODO throw an exception
    }
};

ExperimentRegistry.prototype.getExperiments = function () {
    var url , data, res;
    url = this.experimentRegistryPath + experimentResourcePathConstants.GET_ALL_EXPS;
    data = { experimentId: experimentId}
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res.responseJSON;
};

ExperimentRegistry.prototype.getExperimentsByProject = function (projectName) {
    var url , data, res;
    url = this.experimentRegistryPath + experimentResourcePathConstants.GET_EXPS_BY_PROJECT;
    data = {projectName: projectName}
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res.responseJSON;
};

ExperimentRegistry.prototype.getExperimentsByDate = function (fromDate, toDate) {
    var url , data, res;
    url = this.experimentRegistryPath + experimentResourcePathConstants.GET_EXPS_BY_DATE;
    data = {fromDate: fromDate, toDate: toDate}
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res.responseJSON;
};

ExperimentRegistry.prototype.getExperimentsByProjectDate = function (projectName, fromDate, toDate) {
    var url , data, res;
    url = this.experimentRegistryPath + experimentResourcePathConstants.GET_EXPS_PER_PROJECT_BY_DATE;
    data = {projectName: projectName, fromDate: fromDate, toDate: toDate}
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res.responseJSON;
};


ExperimentRegistry.prototype.addExperiment = function (projectName, experimentID, submittedDate) {
    var url , formParam, res;
    formParam = "?projectName=" + projectName + "&experimentID=" + experimentID + "&submittedDate=" + submittedDate;
    url = this.experimentRegistryPath + experimentResourcePathConstants.GET_EXPS_PER_PROJECT_BY_DATE + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};


ExperimentRegistry.prototype.isExperimentExists = function (experimentID) {
    var url , data, res;
    url = this.experimentRegistryPath + experimentResourcePathConstants.EXP_EXISTS;
    data = {experimentID: experimentID}
    res = sendAndReceive(url, optionalBasicHeaders, type.get, data, null);
    return res.responseText;
};

ExperimentRegistry.prototype.isExperimentExistsThenCreate = function (experimentID, createIfNotPresent) {
    var url , formParam, res;
    formParam = "?experimentID=" + experimentID + "&createIfNotPresent=" + createIfNotPresent;
    url = this.experimentRegistryPath + experimentResourcePathConstants.EXP_EXISTS_CREATE + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

// ========================================= Project Registry Resource ==========================================


var projectResourcePathConstant = {
    REGISTRY_API_PROJECTREGISTRY: "/projectregistry/",
    PROJECT_EXIST: "project/exist",
    PROJECT_EXIST_CREATE: "project/exist",
    ADD_PROJECT: "add/project",
    UPDATE_PROJECT: "update/project",
    DELETE_PROJECT: "delete/project",
    GET_PROJECT: "get/project",
    GET_PROJECTS: "get/projects"
};

function ProjectRegistry() {
    this.projectRegistryPath = baseURL.BASE_RES_PATH + projectResourcePathConstant.REGISTRY_API_PROJECTREGISTRY;

}

ProjectRegistry.prototype.isWorkspaceProjectExists = function (projectName) {
    var url, data, res;
    url = this.projectRegistryPath + projectResourcePathConstant.PROJECT_EXIST;
    data = {projectName: projectName};
    res = sendAndReceive(url, optionalBasicHeaders, type.get, data, null);
    return res.responseText;
};

ProjectRegistry.prototype.isWorkspaceProjectExistsCreate = function (projectName, createIfNotPresent) {
    var url , formParam, res;
    formParam = "?projectName=" + projectName + "&createIfNotPresent=" + createIfNotPresent;
    url = this.projectRegistryPath + projectResourcePathConstant.PROJECT_EXIST_CREATE + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

ProjectRegistry.prototype.addWorkspaceProject = function (projectName) {
    var url , formParam, res;
    formParam = "?projectName=" + projectName;
    url = this.projectRegistryPath + projectResourcePathConstant.PROJECT_EXIST_CREATE + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

ProjectRegistry.prototype.updateWorkspaceProject = function (projectName) {
    var url , formParam, res;
    formParam = "?projectName=" + projectName;
    url = this.projectRegistryPath + projectResourcePathConstant.UPDATE_PROJECT + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

ProjectRegistry.prototype.deleteWorkspaceProject = function (projectName) {
    var url, data, res;
    url = this.projectRegistryPath + projectResourcePathConstant.DELETE_PROJECT;
    data = {projectName: projectName};
    res = sendAndReceive(url, optionalBasicHeaders, type.delete, data, null);
    return res.responseText;
};

ProjectRegistry.prototype.getWorkspaceProject = function (projectName) {
    var url, data, res;
    url = this.projectRegistryPath + projectResourcePathConstant.GET_PROJECT;
    data = {projectName: projectName};
    res = sendAndReceive(url, optionalBasicHeaders, type.get, data, null);
    return res.responseJSON;
};

ProjectRegistry.prototype.getWorkspaceProjects = function () {
    var url, data, res;
    url = this.projectRegistryPath + projectResourcePathConstant.GET_PROJECTS;
    res = sendAndReceive(url, optionalBasicHeaders, type.get, null, null);
    return res.responseJSON;
};


// ========================================= PublishWorkflow Registry Resource ==========================================


var publishedWFConstants = {

    REGISTRY_API_PUBLISHWFREGISTRY: "/publishwfregistry/",
    PUBLISHWF_EXIST: "publishwf/exist",
    PUBLISH_WORKFLOW: "publish/workflow",
    PUBLISH_DEFAULT_WORKFLOW: "publish/default/workflow",
    GET_PUBLISHWORKFLOWGRAPH: "get/publishworkflowgraph",
    GET_PUBLISHWORKFLOWNAMES: "get/publishworkflownames",
    GET_PUBLISHWORKFLOWS: "get/publishworkflows",
    REMOVE_PUBLISHWORKFLOW: "remove/publishworkflow"
};

function PublishWorkflowRegistry() {
    this.publishWorkflowResourcePath = baseURL.BASE_RES_PATH + publishedWFConstants.REGISTRY_API_PUBLISHWFREGISTRY;
}

PublishWorkflowRegistry.prototype.isPublishedWorkflowExists = function (workflowName) {
    var url, data, res;
    url = this.publishWorkflowResourcePath + publishedWFConstants.PUBLISHWF_EXIST;
    data = {workflowName: workflowName};
    res = sendAndReceive(url, optionalBasicHeaders, type.get, data, null);
    return res.responseText;
};

PublishWorkflowRegistry.prototype.publishWorkflow = function (workflowName, publishWorkflowName) {
    var url , formParam, res;
    formParam = "?workflowName=" + workflowName + "&publishWorkflowName=" + publishWorkflowName;
    url = this.publishWorkflowResourcePath + publishedWFConstants.PUBLISH_WORKFLOW + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

PublishWorkflowRegistry.prototype.publishDefaultWorkflow = function (workflowName) {
    var url , formParam, res;
    formParam = "?workflowName=" + workflowName;
    url = this.publishWorkflowResourcePath + publishedWFConstants.PUBLISH_DEFAULT_WORKFLOW + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

PublishWorkflowRegistry.prototype.getPublishedWorkflowGraphXML = function (workflowName) {
    var url, data, res;
    url = this.publishWorkflowResourcePath + publishedWFConstants.GET_PUBLISHWORKFLOWGRAPH;
    data = {workflowName: workflowName};
    res = sendAndReceive(url, optionalBasicHeaders, type.get, data, null);
    // TODO this will return xml file, fix this to return JSON object
    return res.responseText;
};

PublishWorkflowRegistry.prototype.getPublishedWorkflowNames = function () {
    var url, res;
    url = this.publishWorkflowResourcePath + publishedWFConstants.GET_PUBLISHWORKFLOWNAMES;
    res = sendAndReceive(url, airavataBasicHeaders, type.get, null, null);
    return res.responseJSON;
};

PublishWorkflowRegistry.prototype.getPublishedWorkflows = function () {
    var url, res;
    url = this.publishWorkflowResourcePath + publishedWFConstants.GET_PUBLISHWORKFLOWS;
    res = sendAndReceive(url, airavataBasicHeaders, type.get, null, null);
    return res.responseJSON;
};

PublishWorkflowRegistry.prototype.removePublishedWorkflow = function (workflowName) {
    var url, data, res;
    url = this.publishWorkflowResourcePath + publishedWFConstants.REMOVE_PUBLISHWORKFLOW;
    data = {workflowName: workflowName};
    res = sendAndReceive(url, optionalBasicHeaders, type.delete, data, null);
    return res.responseText;
};

// ========================================= UserWorkflow Registry Resource ==========================================


var userWFConstants = {

    REGISTRY_API_USERWFREGISTRY: "/userwfregistry/",
    WORKFLOW_EXIST: "workflow/exist",
    ADD_WORKFLOW: "add/workflow",
    UPDATE_WORKFLOW: "update/workflow",
    GET_WORKFLOWGRAPH: "get/workflowgraph",
    GET_WORKFLOWS: "get/workflows",
    REMOVE_WORKFLOW: "remove/workflow"
};

function UserWorkflowRegistry() {
    this.userWorkflowResourcePath = baseURL.BASE_RES_PATH + userWFConstants.REGISTRY_API_USERWFREGISTRY;
}

UserWorkflowRegistry.prototype.isWorkflowExists = function (workflowName) {
    var url, data, res;
    url = this.userWorkflowResourcePath + userWFConstants.WORKFLOW_EXIST;
    data = {workflowName: workflowName};
    res = sendAndReceive(url, optionalBasicHeaders, type.get, data, null);
    return res.responseText;
};

UserWorkflowRegistry.prototype.addWorkflow = function (workflowName, workflowGraphXml) {
    var url , formParam, res;
    formParam = "?workflowName=" + workflowName + "&workflowGraphXml=" + workflowGraphXml;
    url = this.userWorkflowResourcePath + userWFConstants.ADD_WORKFLOW + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

UserWorkflowRegistry.prototype.updateWorkflow = function (workflowName, workflowGraphXml) {
    var url , formParam, res;
    formParam = "?workflowName=" + workflowName + "&workflowGraphXml=" + workflowGraphXml;
    url = this.userWorkflowResourcePath + userWFConstants.UPDATE_WORKFLOW + formParam;
    res = sendAndReceive(url, formBasicHeaders, type.post, null, null);
    return res.responseText;
};

UserWorkflowRegistry.prototype.getWorkflowGraphXML = function (workflowName) {
    var url, data, res;
    url = this.userWorkflowResourcePath + userWFConstants.GET_WORKFLOWGRAPH;
    data = {workflowName: workflowName, isJson:"true"};
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res.responseText;
};

UserWorkflowRegistry.prototype.getWorkflows = function () {
    var url, res, data;
    data = {isJson:"true"}
    url = this.userWorkflowResourcePath + userWFConstants.GET_WORKFLOWS;
    res = sendAndReceive(url, airavataBasicHeaders, type.get, data, null);
    return res.responseJSON;
};

UserWorkflowRegistry.prototype.removeWorkflow = function (workflowName) {
    var url, data, res;
    url = this.userWorkflowResourcePath + userWFConstants.REMOVE_WORKFLOW;
    data = {workflowName: workflowName};
    res = sendAndReceive(url, optionalBasicHeaders, type.delete, data, null);
    return res.responseText;
};
