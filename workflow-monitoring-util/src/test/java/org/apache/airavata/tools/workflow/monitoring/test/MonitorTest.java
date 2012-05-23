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
package org.apache.airavata.tools.workflow.monitoring.test;

import wsmg.WseClientAPI;
import org.apache.airavata.tools.workflow.monitoring.Server;
import junit.framework.TestCase;

public class MonitorTest extends TestCase {

    public static final String brokerurl = "http://127.0.0.1:8888";
    public static String message = "<wor:monitorWorkflow "
            + "xmlns:wor=\"http://lead.extreme.indiana.edu/namespaces/2006/06/workflow_tracking\">"
            + "<wor:notificationSource " + "wor:serviceID=\"urn:qname:http://www.extreme.indiana.edu/lead:WCS\""
            + "wor:workflowID=\"tag:gpel.leadproject.org,2006:6BD/WRFForecastWithADASInitializedData/instance7155555\""
            + "wor:workflowTimestep=\"18\" wor:workflowNodeID=\"WCS\" />"
            + "<wor:monitoringDuration>12</wor:monitoringDuration>" + "<wor:emailTo>cherath@indiana.edu</wor:emailTo>"
            + "<wor:timestamp>2007-01-20T10:53:28-05:00</wor:timestamp>" + "</wor:monitorWorkflow>";

    public void testMonitor() {
        // Server.main(null);
        WseClientAPI client = new WseClientAPI();
        client.publish(brokerurl, Server.monitoringTopic, message);
    }

}
