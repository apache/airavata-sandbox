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

import org.apache.airavata.tools.workflow.monitoring.Server;
import wsmg.WseClientAPI;
import junit.framework.TestCase;

public class PublishSendingFaultTest extends TestCase {

    public static String workflowID = "tag:gpel.leadproject.org,2006:6BD/WRFForecastWithADASInitializedData/instance71";

    public static String message = "<wor:sendingFault "
            + "xmlns:wor=\"http://lead.extreme.indiana.edu/namespaces/2006/06/workflow_tracking\">"
            + "<wor:notificationSource"
            + " wor:serviceID=\"urn:qname:http://www.extreme.indiana.edu/lead:WRF_Forecasting_Model_Service\""
            + " wor:workflowID=\"tag:gpel.leadproject.org,2006:71N/TestCISimpleEchoWorkflow/instance36\""
            + " wor:workflowTimestep=\"18\"" + " wor:workflowNodeID=\"WRF_Forecasting_Model_Service\" />" +

            "</wor:sendingFault>";

    public void testPublish() {
        WseClientAPI client = new WseClientAPI();
        client.publish(MonitorTest.brokerurl, "anytopic-since-we-use-xpath", message);
    }

}
