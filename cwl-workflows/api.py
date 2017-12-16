# API for Jupyter Notebook client

from apache.airavata.model.workspace.ttypes import *
from apache.airavata.model.experiment.ttypes import *

from thrift.protocol import TBinaryProtocol
from thrift.transport import TSSLSocket
from thrift.transport import TTransport

from apache.airavata.api import Airavata
from apache.airavata.model.security.ttypes import AuthzToken


def get_transport(hostname, port):
    """Create a socket to the Airavata Server

    :param hostname: Hostname of Airavata server
    :param port:     Port of Airavata server
    :return: Transport object
    """
    # TODO: validate server certificate
    transport = TSSLSocket.TSSLSocket(hostname, port, validate=False)
    # Use Buffered Protocol to speedup over raw sockets
    transport = TTransport.TBufferedTransport(transport)
    return transport


def get_airavata_client(transport):
    """Creates and returns airavata client object

    :param transport: Transport object
    :return: AiravataClient object
    """
    # Airavata currently uses Binary Protocol
    protocol = TBinaryProtocol.TBinaryProtocol(transport)

    # Create a Airavata client to use the protocol encoder
    airavataClient = Airavata.Client(protocol)
    return airavataClient


def get_authz_token(token, gateway_id, user_name):
    """Creates and returns a authorization token

    :param token:     Token string
    :param gateway_id: Gateway ID
    :param user_name:  User name of the user
    :return: AuthzToken object
    """
    return AuthzToken(accessToken=token, claimsMap={'gatewayID': gateway_id, 'userName': user_name})


def launch_experiment(hostname, port, user_name, token_string, experiment_id, gateway_id):
    """Launches an experiment identified by an experiment id

    :param hostname:      Hostname of the host where experiment is to be executed
    :param port:          Port of the host where experiment is to be executed
    :param user_name:     Airavata user
    :param token_string:  Access token passed as a string
    :param experiment_id: ID of the experiment that needs to be executed
    :param gateway_id:    ID of the gateway where the experiment needs to be executed
    """
    transport = get_transport(hostname, port)
    transport.open()
    airavata_client = get_airavata_client(transport)
    auth_token = get_authz_token(token_string, gateway_id, user_name)
    airavata_client.launchExperiment(auth_token, experiment_id, gateway_id)
    transport.close()


def create_experiment(hostname, port, token_string, gateway_id, project_id, user_name, experiment_name):
    """Creates an experiment in Airavata

    :param hostname:         Host name of the Airavata server
    :param port:             Port number of the Airavata server
    :param token_string:     Token string
    :param gateway_id:       Gateway ID
    :param project_id:       Project ID
    :param user_name:        Name of the user
    :param experiment_name:  Name of the experiment
    :return: Experiment ID
    """
    transport = get_transport(hostname, port)
    transport.open()
    airavata_client = get_airavata_client(transport)
    auth_token = get_authz_token(token_string, gateway_id, user_name)
    experiment_model = ExperimentModel(projectId=project_id, gatewayId=gateway_id,
                                       userName=user_name, experimentName=experiment_name)
    return airavata_client.createExperiment(auth_token, gateway_id, experiment_model)
