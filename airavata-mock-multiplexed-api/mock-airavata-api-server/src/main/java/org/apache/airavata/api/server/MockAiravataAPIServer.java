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

package org.apache.airavata.api.server;

import org.apache.airavata.api.credentials.CredentialManagementService;
import org.apache.airavata.api.gateway.management.GatewayManagementService;
import org.apache.airavata.api.handlers.CredentialManagementHandler;
import org.apache.airavata.api.handlers.GatewayManagementHandler;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockAiravataAPIServer {

    private final static Logger logger = LoggerFactory.getLogger(MockAiravataAPIServer.class);

    public static CredentialManagementHandler credentialManagementHandler;
    public static CredentialManagementService.Processor credentialManagementProcessor;

    public static GatewayManagementHandler gatewayManagementHandler;
    public static GatewayManagementService.Processor gatewayManagementProcessor;

    public static void main(String [] args) {
        try {
            credentialManagementHandler = new CredentialManagementHandler();
            credentialManagementProcessor = new CredentialManagementService.Processor(credentialManagementHandler);

            gatewayManagementHandler = new GatewayManagementHandler();
            gatewayManagementProcessor = new GatewayManagementService.Processor(gatewayManagementHandler);

            TMultiplexedProcessor airavataServerProcessor = new TMultiplexedProcessor();

            airavataServerProcessor.registerProcessor("CredentialManagementService",credentialManagementProcessor);
            airavataServerProcessor.registerProcessor("GatewayManagementService",gatewayManagementProcessor);

            TServerTransport serverTransport = new TServerSocket(9190);

            TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(airavataServerProcessor));

            System.out.println("Starting Mock Airavata API server...");
            server.serve();

        } catch (Exception x) {
            x.printStackTrace();
        }
    }


}
