from apache.airavata.model.experiment.ttypes import ExperimentModel, UserConfigurationDataModel
from apache.airavata.model.scheduling.ttypes import ComputationalResourceSchedulingModel
from apache.airavata.model.workspace.ttypes import Project

from oauthlib.oauth2 import LegacyApplicationClient
from requests_oauthlib import OAuth2Session
from oauthlib.oauth2 import BackendApplicationClient

import datetime
from datetime import datetime
import calendar


__author__ = 'syodage'
import sys

import random

from thrift.protocol import TBinaryProtocol
from thrift.transport import TSocket, TTransport, TSSLSocket

from apache.airavata.api import Airavata
from apache.airavata.model.security.ttypes import AuthzToken
from apache.airavata.model.application.io.ttypes import InputDataObjectType, OutputDataObjectType

class ExpCatalog:
    def __init__(self, hostName, port):
        # Create a socket to the Airavata Server
        transport = TSSLSocket.TSSLSocket(hostName,port, validate=False)
        # Use Buffered Protocol to speedup over raw sockets
        transport = TTransport.TBufferedTransport(transport)

        # Airavata currently uses Binary Protocol
        protocol = TBinaryProtocol.TBinaryProtocol(transport)

        # Create a Airavata client to use the protocol encoder
        self.airavataClient = Airavata.Client(protocol)

        transport.open()
        
        #client_id = r'XXXXXXXXX'
        #client_secret = r'XXXXXXXXX'

        #client = BackendApplicationClient(client_id=client_id)
        #oauth = OAuth2Session(client=client)
        #token = oauth.fetch_token(token_url='https://idp.scigap.org:9443/oauth2/token', client_id=client_id, client_secret=client_secret)
        #self.authzToken = AuthzToken(token["access_token"])
        self.authzToken = AuthzToken("empty-token")
        
        claimsMap = {"userName":"admin","gatewayID": "seagrid"}
        self.authzToken.claimsMap = claimsMap

        self.gateWayId = "seagrid"
        print(self.authzToken)
        print (self.airavataClient.getAPIVersion(self.authzToken))

    def getExperiment(self, expId):
        experiment = self.airavataClient.getExperiment(self.authzToken, expId)
        # print experiment
        return experiment

    def getJobModel(self,expId):
        jobM = self.airavataClient.getJobDetails(self.authzToken,expId)
        return jobM

    def getExperimentSummary(self, expId):
        expSum = ExperimentSummary()
        expSum.setExperimentModel(self.getExperiment(expId))
        expSum.setJobModels(self.getJobModel(expId))
        return expSum


    def getExperimentName(self):
        nameList = ["Darwin", "Faraday" , "Aristotle", "Tesla", "Edison", "Galileo", "Einstein", "Newton", "Dalton",
                    "Arthur" , "Clark", "Turing", "Hawking", "Maxwell", "Isaac", ""]
        limit = nameList.__len__()
        r = random.randint(0, limit-1)
        return nameList[r]

    def createExperiment(self, applicationName):
        # experiment = ExperimentModel()
        # experiment.experimentName(self.getExperimentName())
        if applicationName == "Amber":
            appInterface = self.getApplication(applicationName)
        else:
            print ("not yet support for application " + applicationName)

        inputs = appInterface.applicationInputs
        for input in inputs:
            if input.name == "Heat-Restart-File":
                input.value = "file://ogce@stampede.tacc.xsede.org:/scratch/01437/ogce/gta-work-dirs/PROCESS_e0610a6c-5778-4a69-a004-f440e29194af/02_Heat.rst"
            elif input.name == "Production-Control-File":
                input.value = "file://ogce@stampede.tacc.xsede.org:/scratch/01437/ogce/gta-work-dirs/PROCESS_e0610a6c-5778-4a69-a004-f440e29194af/03_Prod.in"
            elif input.name == "Parameter-Topology-File":
                input.value = "file://ogce@stampede.tacc.xsede.org:/scratch/01437/ogce/gta-work-dirs/PROCESS_e0610a6c-5778-4a69-a004-f440e29194af/prmtop"

        outputs = appInterface.applicationOutputs

        experiment = ExperimentModel()
        experiment.experimentName = self.getExperimentName()
        experiment.projectId = self.getDefaultProjectId()
        experiment.gatewayId = self.gateWayId
        experiment.userName = "admin"
        experiment.description = "Test Amber experiment"
        experiment.experimentInputs = inputs
        experiment.experimentOutputs = outputs
        experiment.executionId = appInterface.applicationInterfaceId

        computeResources = self.airavataClient.getAvailableAppInterfaceComputeResources(
            self.authzToken, appInterface.applicationInterfaceId)
        t = {"a": "b", "c": "d"}
        for computeResource in computeResources:
            if computeResource.startswith("stampede.tacc.xsede.org"):
                stampedCRId = computeResource
                break

        print (stampedCRId)
        computationalRS = ComputationalResourceSchedulingModel()
        computationalRS.resourceHostId = stampedCRId
        computationalRS.totalCPUCount = 16
        computationalRS.nodeCount = 1
        computationalRS.numberOfThreads = 1
        computationalRS.queueName = "normal"
        computationalRS.wallTimeLimit = 10
        computationalRS.totalPhysicalMemory = 1

        userConfig = UserConfigurationDataModel()
        userConfig.airavataAutoSchedule = True
        userConfig.overrideManualScheduledParams = False
        userConfig.computationalResourceScheduling = computationalRS


        experiment.userConfigurationData = userConfig
        expId = self.airavataClient.createExperiment(self.authzToken, self.gateWayId, experiment)
        print ("Experiment Id is " + expId)
        return expId

    def launchExperiment(self, expId):
        self.airavataClient.launchExperiment(self.authzToken, expId , self.gateWayId)


    def cancelExperiment(self, expId):
        self.airavataClient.terminateExperiment(self.authzToken, expId, self.gateWayId)

    def getApplication(self, nameStartWith):
        appNames = self.airavataClient.getAllApplicationInterfaceNames(self.authzToken,self.gateWayId )
        appInterFaces = self.airavataClient.getAllApplicationInterfaces(self.authzToken, self.gateWayId)
        for interface in appInterFaces:
            if interface.applicationName.startswith(nameStartWith):
                return interface
        # for name in appNames:
        #     if name.startswith(nameStartWith):
        #         return name
    # def createAndLaunchExperiment(self):

    def getDefaultProjectId(self):
        project = Project()
        project.name = "default"
        project.owner = "admin"
        project.description = "test project"
        projectId = self.airavataClient.createProject(self.authzToken, self.gateWayId,project)
        project.projectID = projectId
        return projectId

    def experiment_statistics(self,gatewayID, fromTime, toTime):
        statistics=self.airavataClient.getExperimentStatistics(self.authzToken, gatewayID,fromTime,toTime)
        return statistics
    
    def create_experiment(self,authzToken,gatewayId,experiment):
        experiment=self.airavataClient.createExperiment(self.authzToken, gatewayID,experiment)
        return experiment
    

class ExperimentSummary:

    def setExperimentModel(self, expModel):
        self.name = expModel.experimentName
        self.id = expModel.experimentId
        self.status = expModel.experimentStatus


    def setProcessModel(self, processModel):
        print ("setProcessModel is not yet implemented")

    def setJobModels(self, jobModel):
        self.jobs = jobModel


