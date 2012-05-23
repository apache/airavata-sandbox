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
package org.apache.airavata.tools.workflow.monitoring.bean;

import java.util.Date;

public class WorkflowInfo implements Comparable {
    private String workflowId;
    private String templateId;
    private String username = "";
    private String status;
    private String startTime;
    private String endTime;
    private boolean faultsAvailable;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isFaultsAvailable() {
        return faultsAvailable;
    }

    public void setFaultsAvailable(boolean faultsAvailable) {
        this.faultsAvailable = faultsAvailable;
    }

    public int compareTo(Object o) {
        if (o instanceof WorkflowInfo) {
            WorkflowInfo workflowInfo = (WorkflowInfo) o;
            return new Date(startTime).before(new Date(workflowInfo.getStartTime())) ? -1 : 1;
        }
        // If this < o, return a negative value
        // If this = o, return 0
        // If this > o, return a positive value
        return 0;
    }
}
