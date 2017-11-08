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
package org.apache.airavata.k8s.task.job.service;

import org.apache.airavata.k8s.api.resources.compute.ComputeResource;
import org.apache.airavata.k8s.api.resources.task.TaskResource;
import org.apache.airavata.k8s.api.resources.task.TaskStatusResource;
import org.apache.airavata.k8s.api.resources.task.type.TaskTypeResource;
import org.apache.airavata.k8s.compute.api.ExecutionResult;
import org.apache.airavata.k8s.task.api.AbstractTaskExecutionService;
import org.apache.airavata.k8s.task.api.TaskContext;
import org.apache.airavata.k8s.task.api.messaging.KafkaSender;
import org.apache.airavata.k8s.task.job.CommandTaskInfo;
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

    public TaskExecutionService(RestTemplate restTemplate, KafkaSender kafkaSender) {
        super(restTemplate, null, 10);
    }

    @Override
    public TaskTypeResource getType() {
        return CommandTaskInfo.getTaskType();
    }

    @Override
    public void initializeParameters(TaskResource taskResource, TaskContext taskContext) throws Exception {

        taskContext.getLocalContext().put(CommandTaskInfo.COMMAND, findInput(taskContext, taskResource, CommandTaskInfo.COMMAND, false));
        taskContext.getLocalContext().put(CommandTaskInfo.ARGUMENTS, findInput(taskContext, taskResource, CommandTaskInfo.ARGUMENTS, true));
        taskContext.getLocalContext().put(CommandTaskInfo.STD_OUT_PATH, findInput(taskContext, taskResource, CommandTaskInfo.STD_OUT_PATH, false));
        taskContext.getLocalContext().put(CommandTaskInfo.STD_ERR_PATH, findInput(taskContext, taskResource, CommandTaskInfo.STD_ERR_PATH, false));

        String computeId = findInput(taskContext, taskResource, CommandTaskInfo.COMPUTE_RESOURCE, false);
        taskContext.getLocalContext().put(CommandTaskInfo.COMPUTE_RESOURCE, this.getRestTemplate().getForObject("http://" + this.getApiServerUrl()
                + "/compute/" + Long.parseLong(computeId), ComputeResource.class));

    }

    @Override
    public void executeTask(TaskResource taskResource, TaskContext taskContext) {

        try {
            String command = (String) taskContext.getLocalContext().get(CommandTaskInfo.COMMAND);
            String arguments = (String) taskContext.getLocalContext().get(CommandTaskInfo.ARGUMENTS);
            ComputeResource computeResource = (ComputeResource) taskContext.getLocalContext().get(CommandTaskInfo.COMPUTE_RESOURCE);
            String stdOutPath = (String) taskContext.getLocalContext().get(CommandTaskInfo.STD_OUT_PATH);
            String stdErrPath = (String) taskContext.getLocalContext().get(CommandTaskInfo.STD_ERR_PATH);
            String stdOutSuffix = " > " + stdOutPath + " 2> " + stdErrPath;

            publishTaskStatus(taskContext, TaskStatusResource.State.EXECUTING);

            String finalCommand = command + (arguments != null ? arguments : "") + stdOutSuffix;

            System.out.println("Executing command " + finalCommand);

            ExecutionResult executionResult = fetchComputeResourceOperation(computeResource).executeCommand(finalCommand);

            if (executionResult.getExitStatus() == 0) {
                finishTaskExecution(taskContext, taskResource, "Out", TaskStatusResource.State.COMPLETED, "Task completed");
            } else if (executionResult.getExitStatus() == -1) {
                publishTaskStatus(taskContext, TaskStatusResource.State.FAILED, "Process didn't exit successfully");
            } else {
                publishTaskStatus(taskContext, TaskStatusResource.State.FAILED, "Process exited with error status " + executionResult.getExitStatus());
            }

        } catch (Exception e) {

            e.printStackTrace();
            publishTaskStatus(taskContext, TaskStatusResource.State.FAILED, e.getMessage());
        }
    }
}
