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

package org.apache.airavata.services.registry.rest.resourcemappings;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WorkflowNodeIODataMapping {
    private String experimentId;
    private String workflowName;
    private String workflowId;
    private String workflowInstanceId;
    private String workflowNodeType;

    public WorkflowNodeIODataMapping() {
    }

    public String getExperimentId() {
        return experimentId;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public String getWorkflowInstanceId() {
        return workflowInstanceId;
    }

    public void setExperimentId(String experimentId) {
        this.experimentId = experimentId;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public void setWorkflowInstanceId(String workflowInstanceId) {
        this.workflowInstanceId = workflowInstanceId;
    }

    public String getWorkflowNodeType() {
        return workflowNodeType;
    }

    public void setWorkflowNodeType(String workflowNodeType) {
        this.workflowNodeType = workflowNodeType;
    }
}

