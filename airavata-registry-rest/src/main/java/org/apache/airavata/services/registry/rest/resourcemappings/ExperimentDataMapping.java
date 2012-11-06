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
public class ExperimentDataMapping {
    public String experimentID;
    public String topicID;
    public String user;
    public String experimentMetadata;
    public String experimentName;
    public WorkflowInstanceMapping[] workflowInstanceMappings;

    public ExperimentDataMapping() {
    }

    public void setExperimentID(String experimentID) {
        this.experimentID = experimentID;
    }

    public void setTopicID(String topicID) {
        this.topicID = topicID;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setExperimentMetadata(String experimentMetadata) {
        this.experimentMetadata = experimentMetadata;
    }

    public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
    }

    public void setWorkflowInstanceMappings(WorkflowInstanceMapping[] workflowInstanceMappings) {
        this.workflowInstanceMappings = workflowInstanceMappings;
    }

    public String getExperimentID() {
        return experimentID;
    }

    public String getTopicID() {
        return topicID;
    }

    public String getUser() {
        return user;
    }

    public String getExperimentMetadata() {
        return experimentMetadata;
    }

    public String getExperimentName() {
        return experimentName;
    }

    public WorkflowInstanceMapping[] getWorkflowInstanceMappings() {
        return workflowInstanceMappings;
    }
}
