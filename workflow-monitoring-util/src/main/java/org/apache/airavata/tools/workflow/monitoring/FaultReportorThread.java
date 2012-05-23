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

import java.util.Date;
import java.util.Vector;

import xsul.MLogger;

public class FaultReportorThread implements Runnable {

    private static MLogger log = MLogger.getLogger();

    private ServerContext context;

    public FaultReportorThread(ServerContext context) {
        this.context = context;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(context.getReportorSleepTime());
                log.finest("Running Reporter thread after sleeptime secs=" + (context.getReportorSleepTime() / 1000));
                Date now = new Date(System.currentTimeMillis());
                for (String key : context.getMonitoredWorkflows().keySet()) {
                    Workflow workflow = context.getMonitoredWorkflows().get(key);
                    if (workflow.getExpirationTime().before(now)) {
                        // synchronized since once workflow is removed from context no one
                        // could add more
                        context.removeWorkflow(workflow.getWorkflowID());

                    }
                    Vector messages = workflow.getAllMessagesAndReset();
                    if (messages.size() > 0) {
                        log.finest("Reporting faults on workflow" + workflow.getWorkflowID());
                        this.sendFaults(messages, workflow.getAlertHandler(), workflow.getWorkflowID());

                    }
                }

            } catch (Exception e) {
                log.caught(e);
            }
        }
    }

    public void sendFaults(Vector faultMessages, AlertHandler alertHandler, String workflowid) {
        alertHandler.alert(faultMessages, workflowid);
    }

}