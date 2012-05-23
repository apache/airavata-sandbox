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

import java.io.StringReader;

import org.xmlpull.v1.builder.XmlElement;
import org.xmlpull.v1.builder.XmlInfosetBuilder;

import wsmg.NotificationHandler;
import xsul.MLogger;
import xsul.XmlConstants;

public class FaultMessageHandler implements NotificationHandler, StatusConstants {

    private static MLogger log = MLogger.getLogger();

    private ServerContext context;

    private FaultMessagesDB messageDB;

    private SummeryDB summeryTbl;

    public FaultMessageHandler(ServerContext context) {
        this.context = context;
        this.messageDB = new FaultMessagesDB(context.getDatabase());
        this.summeryTbl = new SummeryDB(context.getDatabase());
    }

    public void handleNotification(String message) {
        String workflowID = Util.getWorkflowID(message, Server.SENDING_FAULT, "workflowID");
        if (workflowID != null) {
            this.messageDB.insert(workflowID, message);
            boolean success = context.checkAndRegistorMonitoredWorkflow(workflowID, message);
            if (success) {
                log.finest("Received a fault with workflowid" + workflowID + message);
            } else {
                log.warning("Failed registring fault message" + workflowID);
            }
            this.summeryTbl.edit(workflowID, STATUS_FAILED);

        }

    }

}