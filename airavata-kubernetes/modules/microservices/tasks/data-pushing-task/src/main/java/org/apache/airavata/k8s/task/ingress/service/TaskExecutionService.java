/**
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
 */
package org.apache.airavata.k8s.task.ingress.service;

import org.apache.airavata.k8s.api.resources.compute.ComputeResource;
import org.apache.airavata.k8s.api.resources.task.TaskResource;
import org.apache.airavata.k8s.api.resources.task.TaskStatusResource;
import org.apache.airavata.k8s.api.resources.task.type.TaskTypeResource;
import org.apache.airavata.k8s.task.api.AbstractTaskExecutionService;
import org.apache.airavata.k8s.task.api.TaskContext;
import org.apache.airavata.k8s.task.api.messaging.KafkaSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
@Service
public class TaskExecutionService extends AbstractTaskExecutionService {

    private final String COMPUTE_RESOURCE =  "compute_resource";
    private final String REMOTE_TARGET_PATH = "remote_target_path";
    private final String DATA_LOCATION_ID = "data_location_id";

    public TaskExecutionService(RestTemplate restTemplate, KafkaSender kafkaSender) {
        super(restTemplate, kafkaSender, 10);
    }

    @Override
    public TaskTypeResource getType() {
        return null;
    }

    @Override
    public void initializeParameters(TaskResource taskResource, TaskContext taskContext) throws Exception {

        taskContext.getLocalContext().put(DATA_LOCATION_ID, findInput(taskContext, taskResource, DATA_LOCATION_ID, false));
        taskContext.getLocalContext().put(REMOTE_TARGET_PATH, findInput(taskContext, taskResource, REMOTE_TARGET_PATH, false));

        String computeId = findInput(taskContext, taskResource, COMPUTE_RESOURCE, false);
        taskContext.getLocalContext().put(COMPUTE_RESOURCE, this.getRestTemplate().getForObject("http://" + this.getApiServerUrl()
                + "/compute/" + Long.parseLong(computeId), ComputeResource.class));
    }

    @Override
    public void executeTask(TaskResource taskResource, TaskContext taskContext) {

        String remoteTargetPath = (String) taskContext.getLocalContext().get(REMOTE_TARGET_PATH);
        String dataLocationId = (String) taskContext.getLocalContext().get(DATA_LOCATION_ID);
        ComputeResource computeResource = (ComputeResource) taskContext.getLocalContext().get(COMPUTE_RESOURCE);

        try {
            publishTaskStatus(taskContext, TaskStatusResource.State.EXECUTING);
            fetchComputeResourceOperation(computeResource).transferDataIn(dataLocationId, remoteTargetPath, "SCP");
            finishTaskExecution(taskContext, taskResource, "Out", TaskStatusResource.State.COMPLETED, "Task completed");


        } catch (Exception e) {

            e.printStackTrace();
            publishTaskStatus(taskContext, TaskStatusResource.State.FAILED);
        }
    }
}
