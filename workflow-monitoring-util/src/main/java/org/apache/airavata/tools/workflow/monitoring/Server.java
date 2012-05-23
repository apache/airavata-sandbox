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

package org.apache.airavata.tools.workflow.monitoring;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.airavata.tools.workflow.monitoring.db.JdbcStorage;
import org.apache.airavata.tools.workflow.monitoring.status.WorkflowStartedStatusHandler;
import org.apache.airavata.tools.workflow.monitoring.status.WorkflowTerminatedHandler;
import org.exolab.jms.util.CommandLine;

import wsmg.WseClientAPI;

public class Server {

    public static final String SENDING_FAULT = "sendingFault";
    public static final String WORKFLOW_INITIATED = "invokingService";
    public static final String WORKFLOW_TERMINATED = "workflowTerminated";

    private static final String XPATH_SENDING_FAULT = "/" + SENDING_FAULT;
    private static final String XPATH_WORKFLOW_INTIATED = "/" + WORKFLOW_INITIATED;
    private static final String XPATH_WORKFLOW_TERMINATED = "/" + WORKFLOW_TERMINATED;

    private static int faultListenerPort = 5555;
    private static int managementListenerPort = 5556;
    private static int successListenerPort = 5557;
    private static int initiatedListerPort = 5558;

    public static final String DB_CONFIG_NAME = "db.config";
    private static long senderThreadSleep = 120000;// 1 minutes

    public static String monitoringTopic = "MonitorWorkflow";

    public static String brokerLocation;

    public Server() {

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        CommandLine cmdline = new CommandLine(args);
        brokerLocation = cmdline.value("broker", "http://ogceportal.iu.teragrid.org:12346/");
        faultListenerPort = Integer.parseInt(cmdline.value("fport", "5555"));
        managementListenerPort = Integer.parseInt(cmdline.value("mport", "5556"));
        senderThreadSleep = Integer.parseInt(cmdline.value("sleepminutes", "2")) * 60000;
        new Server().start();

    }

    public void start() {
        ServerContext serverContext = new ServerContext(new JdbcStorage(DB_CONFIG_NAME, true), senderThreadSleep);
        FaultMessageHandler handler = new FaultMessageHandler(serverContext);

        WseClientAPI client = new WseClientAPI();
        int consumer = client.startConsumerService(faultListenerPort, handler);

        client.subscribe(brokerLocation, getConsumeEPR(consumer), null, XPATH_SENDING_FAULT);

        WseClientAPI successClient = new WseClientAPI();
        successListenerPort = successClient.startConsumerService(successListenerPort, new WorkflowTerminatedHandler(
                serverContext));

        successClient.subscribe(brokerLocation, getConsumeEPR(successListenerPort), null, XPATH_WORKFLOW_TERMINATED);

        WseClientAPI initiatedClient = new WseClientAPI();
        initiatedListerPort = initiatedClient.startConsumerService(initiatedListerPort,
                new WorkflowStartedStatusHandler(serverContext));

        initiatedClient.subscribe(brokerLocation, getConsumeEPR(initiatedListerPort), null, XPATH_WORKFLOW_INTIATED);

        ManagementHandler managementHandler = new ManagementHandler(serverContext);
        WseClientAPI managementClient = new WseClientAPI();
        int managementPort = managementClient.startConsumerService(managementListenerPort, managementHandler);

        client.subscribe(brokerLocation, getConsumeEPR(managementPort), monitoringTopic);

        new Thread(new FaultReportorThread(serverContext)).start();
    }

    private String getConsumeEPR(int port) {
        String epr;
        try {
            epr = InetAddress.getLocalHost().getHostAddress() + ":" + port;
        } catch (UnknownHostException e) {
            throw new WorkflowMonitoringException(e);
        }
        System.out.println(epr);
        return epr;
    }

}