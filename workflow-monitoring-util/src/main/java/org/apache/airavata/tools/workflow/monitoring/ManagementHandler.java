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

import wsmg.NotificationHandler;
import xsul.MLogger;

public class ManagementHandler implements NotificationHandler {

    private static MLogger log = MLogger.getLogger();

    private ServerContext context;

    public ManagementHandler(ServerContext context) {
        this.context = context;

    }

    /*
     * (non-Javadoc)
     * 
     * @see wsmg.NotificationHandler#handleNotification(java.lang.String)
     */
    public void handleNotification(String message) {
        try {
            Workflow workflow = new Workflow();
            if (workflow.parse(message)) {
                context.addWorkflow(workflow);
                log.finest("Added a workflow to monitor with workflow id=" + workflow.getWorkflowID());
            } else {
                log.fine("The monitoring request is ignored due to message format error:" + message);
            }
        } catch (WorkflowMonitoringException e) {
            log.caught("Error occured in intiating a monitor", e.getMessage(), e);
        }

    }

}