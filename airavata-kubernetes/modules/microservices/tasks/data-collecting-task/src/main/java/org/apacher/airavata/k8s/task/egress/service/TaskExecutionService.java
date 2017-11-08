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
package org.apacher.airavata.k8s.task.egress.service;

import org.apache.airavata.k8s.api.resources.compute.ComputeResource;
import org.apache.airavata.k8s.api.resources.task.TaskResource;
import org.apache.airavata.k8s.api.resources.task.TaskStatusResource;
import org.apache.airavata.k8s.api.resources.task.type.TaskTypeResource;
import org.apache.airavata.k8s.task.api.AbstractTaskExecutionService;
import org.apache.airavata.k8s.task.api.TaskContext;
import org.apache.airavata.k8s.task.api.messaging.KafkaSender;
import org.apacher.airavata.k8s.task.egress.DataCollectingTaskInfo;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
@Service
public class TaskExecutionService extends AbstractTaskExecutionService {

    public TaskExecutionService(RestTemplate restTemplate, KafkaSender kafkaSender) {
        super(restTemplate, kafkaSender, 10);
    }

    @Override
    public TaskTypeResource getType() {
        return DataCollectingTaskInfo.getTaskType();
    }

    @Override
    public void initializeParameters(TaskResource taskResource, TaskContext taskContext) throws Exception {

        taskContext.getLocalContext().put(DataCollectingTaskInfo.REMOTE_SOURCE_PATH, findInput(taskContext, taskResource, DataCollectingTaskInfo.REMOTE_SOURCE_PATH, false));
        taskContext.getLocalContext().put(DataCollectingTaskInfo.IDENTIFIER, findInput(taskContext, taskResource, DataCollectingTaskInfo.IDENTIFIER, false));

        String computeId = findInput(taskContext, taskResource, DataCollectingTaskInfo.COMPUTE_RESOURCE, false);
        taskContext.getLocalContext().put(DataCollectingTaskInfo.COMPUTE_RESOURCE, this.getRestTemplate().getForObject("http://" + this.getApiServerUrl()
                + "/compute/" + Long.parseLong(computeId), ComputeResource.class));

    }

    public void executeTask(TaskResource taskResource, TaskContext taskContext) {

        try {

            ComputeResource computeResource = (ComputeResource) taskContext.getLocalContext().get(DataCollectingTaskInfo.COMPUTE_RESOURCE);
            String identifier = (String) taskContext.getLocalContext().get(DataCollectingTaskInfo.IDENTIFIER);
            String remoteSourcePath = (String) taskContext.getLocalContext().get(DataCollectingTaskInfo.REMOTE_SOURCE_PATH);

            publishTaskStatus(taskContext, TaskStatusResource.State.EXECUTING);

            String temporaryFile = "/tmp/" + UUID.randomUUID().toString();
            System.out.println("Downloading " + remoteSourcePath + " to " + temporaryFile + " from compute resource "
                    + computeResource.getName());

            fetchComputeResourceOperation(computeResource).transferDataOut(remoteSourcePath, temporaryFile, "SCP");

            LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            map.add("file", new FileSystemResource(temporaryFile));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);

            System.out.println("Uploading data file with task id " + taskResource.getId() + " and identifier "
                    + identifier + " to data store");

            getRestTemplate().exchange("http://" + getApiServerUrl() + "/data/" + taskResource.getId()+ "/"
                            + identifier + "/upload", HttpMethod.POST, requestEntity, Long.class);

            finishTaskExecution(taskContext, taskResource, "Out", TaskStatusResource.State.COMPLETED, "Task completed");

        } catch (Exception e) {
            e.printStackTrace();
            publishTaskStatus(taskContext, TaskStatusResource.State.FAILED, e.getMessage());

        }
    }

}
