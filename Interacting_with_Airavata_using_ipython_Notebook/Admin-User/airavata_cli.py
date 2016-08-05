import sys,random,time
import threading
from expcatalog.expcatalog import ExpCatalog
from appcatalog.appcatalog import AppCatalog

VERSION = "0.0.1"

BANNER = """
Welcome to Airavata CLI v%s - Wirtten in python

""" % VERSION

exitMsg = "Bye, See you soon :)"
cli_prompt = "=> "


class AiravataCLI:

    def __init__(self, hostName, port):
        self.exit = False
        self.expCatalog = ExpCatalog(hostName, port)
        self.AppCatalog = AppCatalog(hostName, port)
        self.experimentState = "Hukarz"
        self.jobStateMapper = {
            0: "SUBMITTED",
            1: "QUEUED",
            2: "ACTIVE",
            3: "COMPLETE",
            4: "CANCELED",
            5: "FAILED",
            6: "SUSPENDED",
            7: "UNKNOWN"
        }

        self.experimentStateMapper = {
            0: "CREATED",
            1: "VALIDATED",
            2: "SCHEDULED",
            3: "LAUNCHED",
            4: "EXECUTING",
            5: "CANCELLING",
            6: "CANCELED",
            7: "COMPLETED",
            8: "FAILED"
        }

        # set initial properties
    def printVersion(self):
        print (BANNER)

    def start(self):
        command ='expid gaussianfromclient11_62221967-646d-475c-9c46-ed093d09c610'
        while not self.exit:
            cInputs = command.split(" ")
            if cInputs[0] == 'q' or cInputs[0] == 'quit':
                print (exitMsg)
                self.exit = True
            elif cInputs[0] == 'expid':
                self.monitorExperiment(cInputs[1])
            else:
                print ("not yet implemented")

            if not self.exit:
                command = raw_input(cli_prompt)

    def monitorExperiment(self, expId):
        experimentSum = self.expCatalog.getExperimentSummary(expId)  ## get experiment id from input and pass it to this
        self.experimentState = self.experimentStateMapper.get(experimentSum.status.state, "Hukarz")
        indent = " |- "
        print (indent + "name :" + experimentSum.name)
        print (indent + "Id :" + experimentSum.id)
        print (indent + "status :" + self.experimentState)
        indent = "    " + indent
        for job in experimentSum.jobs:
            print (indent + "jobId :" + job.jobId)
            print (indent + "jobStatus :" + self.jobStateMapper.get(job.jobStatus.jobState, "Hukarz"))
            print (indent + " ---- ")

        return self.experimentState

    def createExperiment(self, applicationName):
        return self.expCatalog.createExperiment(applicationName)

    def cancelExperiment(self, expId):
        return self.expCatalog.cancelExperiment(expId)
    
    def computer_resources(self):
        return self.AppCatalog.computer_resources()
    
    def list_of_applications(self, gatewayId):
        return self.AppCatalog.list_of_applications(gatewayId)
    
    def application_deployments(self, applicationInterfaceId):
        return self.AppCatalog.application_deployments(applicationInterfaceId)
    
    def module_descriptions(self,gatewayId):
        return self.AppCatalog.module_descriptions(gatewayId)
    
    def get_gatewaylist(self):
        return self.AppCatalog.get_gatewaylist()

    def launchExperiment(self, expId):
        self.expCatalog.launchExperiment(expId)
        # while True:
        #     self.monitorExperiment(expId)
        

    def monitorStatus(self, expId):
        self.sendCancel = False
        delay = 7
        while True:
            state = (self.monitorExperiment(expId))
            if (state == "EXECUTING"):
                if (not self.sendCancel):
                    if delay == 0:
                        print ("sending cancel request for " + expId)
                        self.cancelExperiment(expId)
                        print ("sent cancel request for " + expId)
                        self.sendCancel = True

                    else:
                        delay -= 1

            if (state == "COMPLETED" or state == "FAILED" or state == "CANCELLED"):
                return
                # airavata_cli.monitorExperiment("Einstein_c33d855b-8c10-4d2a-961a-4b55aff807f7")
            time.sleep(2)
           
    def experiment_statistics(self, gatewayId, fromTime, toTime):
        return self.expCatalog.experiment_statistics(gatewayId,fromTime,toTime)
    
    def createExperiment1(self,authzToken,gatewayId,experiment):
        return self.expCatalog.create_experiment1(authzToken,gatewayId,experiment)
    
