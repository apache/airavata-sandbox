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

package org.apache.airavata.tools.workflow.monitoring.status;

import org.apache.airavata.tools.workflow.monitoring.Server;
import org.apache.airavata.tools.workflow.monitoring.ServerContext;
import org.apache.airavata.tools.workflow.monitoring.StatusConstants;
import org.apache.airavata.tools.workflow.monitoring.SummeryDB;
import org.apache.airavata.tools.workflow.monitoring.Util;
import org.apache.airavata.tools.workflow.monitoring.db.JdbcStorage;

import sun.security.acl.WorldGroupImpl;
import wsmg.NotificationHandler;
import xsul.MLogger;

public class WorkflowTerminatedHandler implements NotificationHandler, StatusConstants {

    private static MLogger log = MLogger.getLogger();

    private ServerContext context;

    private SummeryDB summeryTbl;

    public WorkflowTerminatedHandler(ServerContext context) {
        this.context = context;
        this.summeryTbl = new SummeryDB(context.getDatabase());
    }

    public void handleNotification(String message) {

        String workflowID = Util.getWorkflowID(message, Server.WORKFLOW_TERMINATED, "serviceID");
        if (workflowID != null) {
            this.summeryTbl.edit(workflowID, STATUS_SUCCESSFUL);
            this.context.removeWorkflow(workflowID);
            log.finest("Adding Finished entry " + message);
        } else {
            log.warning("goat a null workflowid for terminated handler" + message);
        }

    }

}