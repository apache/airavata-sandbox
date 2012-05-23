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
import org.apache.airavata.tools.workflow.monitoring.SummeryDB;
import org.apache.airavata.tools.workflow.monitoring.Util;

import wsmg.NotificationHandler;
import xsul.MLogger;

public class WorkflowStartedStatusHandler implements NotificationHandler {

    private static MLogger log = MLogger.getLogger();

    private ServerContext context;;

    private SummeryDB summeryTbl;

    public WorkflowStartedStatusHandler(ServerContext context) {
        this.context = context;
        this.summeryTbl = new SummeryDB(context.getDatabase());
    }

    public void handleNotification(String message) {

        String workflowID = Util.getInitialID(message);
        if (null != workflowID) {
            log.finest("Adding workflowStarted entry " + message);
            this.context.removeWorkflow(workflowID);
            this.summeryTbl.insert(workflowID, null, Util.getDN(message));
        }else{
            log.warning("Got null for workflow started"+message);
        }

    }

}