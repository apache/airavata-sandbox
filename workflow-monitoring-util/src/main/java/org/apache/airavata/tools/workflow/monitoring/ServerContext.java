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

import java.util.Hashtable;

import org.apache.airavata.tools.workflow.monitoring.db.JdbcStorage;

public class ServerContext {

    private JdbcStorage db;

    private Hashtable<String, Workflow> monitoredWorkflows = new Hashtable<String, Workflow>();

    private long reportorThreadSleeptime = 300000;

    public ServerContext(JdbcStorage database, long sleepMilisecs) {
        this.db = database;
        this.reportorThreadSleeptime = sleepMilisecs;
    }

    public JdbcStorage getDatabase() {
        return db;
    }

    public Hashtable<String, Workflow> getMonitoredWorkflows() {
        return monitoredWorkflows;
    }

    public void addWorkflow(Workflow workflow) {
        if (this.monitoredWorkflows.get(workflow.getWorkflowID()) == null) {
            this.monitoredWorkflows.put(workflow.getWorkflowID(), workflow);
        }
    }

    public Workflow getWorkflow(String workflowID) {
        return this.monitoredWorkflows.get(workflowID);
    }

    public void removeWorkflow(String workflowID) {
        this.monitoredWorkflows.remove(workflowID);
    }

    public boolean checkAndRegistorMonitoredWorkflow(String workflowID, String message) {
        Workflow workflow = this.monitoredWorkflows.get(workflowID);
        if (workflow != null) {
            workflow.addFaultMessage(message);
            return true;
        } else {
            return false;
        }
    }

    public long getReportorSleepTime() {
        return this.reportorThreadSleeptime;
    }

    public void setReportorSleepTime(long time) {
        this.reportorThreadSleeptime = time;
    }

}