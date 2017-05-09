from apache.airavata.model.experiment.ttypes import ExperimentModel, UserConfigurationDataModel
from apache.airavata.model.scheduling.ttypes import ComputationalResourceSchedulingModel
from apache.airavata.model.workspace.ttypes import Project

from oauthlib.oauth2 import LegacyApplicationClient
from requests_oauthlib import OAuth2Session
from oauthlib.oauth2 import BackendApplicationClient

__author__ = 'syodage'
import sys

import random

from thrift.protocol import TBinaryProtocol
from thrift.transport import TSocket, TTransport, TSSLSocket

from apache.airavata.api import Airavata
from apache.airavata.model.security.ttypes import AuthzToken
from apache.airavata.model.application.io.ttypes import InputDataObjectType, OutputDataObjectType

class AppCatalog:
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
        
        #client_id = r'XXXXXXXXXX'
        #client_secret = r'XXXXXXXXXXX'

        #client = BackendApplicationClient(client_id=client_id)
        #oauth = OAuth2Session(client=client)
        #token = oauth.fetch_token(token_url='https://idp.scigap.org:9443/oauth2/token', client_id=client_id, client_secret=client_secret)
        #self.authzToken = AuthzToken(token["access_token"])
        self.authzToken = AuthzToken("")
        
        claimsMap = {"userName":"admin","gatewayID": "seagrid"}
        self.authzToken.claimsMap = claimsMap

        self.gateWayId = "seagrid"

        print self.airavataClient.getAPIVersion(self.authzToken)

    def computer_resources(self):
        resources = self.airavataClient.getAllComputeResourceNames(self.authzToken)
        return resources
    
    def list_of_applications(self, gatewayId):
        Applications= self.airavataClient.getAllApplicationInterfaces(self.authzToken,gatewayId)
        return Applications
    
    def application_deployments(self, applicationInterfaceId):
        deployments= self.airavataClient.getApplicationDeployment(self.authzToken,applicationInterfaceId)
        return deployments
    
    def module_descriptions(self,gatewayId):
        description = self.airavataClient.getAllAppModules(self.authzToken,gatewayId)
        return description
    
    def get_gatewaylist(self):
        gateway_list= self.airavataClient.getAllGateways(self.authzToken)
        return gateway_list
        


