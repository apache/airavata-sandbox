# Test script for api.py

import api
import configparser
import sys


def main():
    """Main function
    """
    config_file = sys.argv[1]
    config = configparser.ConfigParser()
    config.read(config_file)
    username = config['credentials']['Username']
    access_token = config['credentials']['AccessToken']
    gateway_id = config['config']['GatewayID']
    hostname = config['config']['HostName']
    port = config['config']['Port']
    experiment_id = config['config']['ExperimentID']

    api.launch_experiment(hostname, port, username, access_token, experiment_id, gateway_id)


if __name__ == '__main__':
    main()
