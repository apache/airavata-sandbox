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
 */
package org.apache.airavata.k8s.gfac.core;

import org.apache.airavata.k8s.api.resources.process.ProcessStatusResource;
import org.apache.airavata.k8s.api.resources.task.TaskOutPortResource;
import org.apache.airavata.k8s.api.resources.task.TaskResource;
import org.apache.airavata.k8s.api.resources.task.TaskStatusResource;
import org.apache.airavata.k8s.gfac.messaging.KafkaSender;
import org.apache.airavata.k8s.task.api.TaskContext;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class ProcessLifeCycleManager {

    private long processId;
    private List<TaskResource> tasks;
    private TaskResource currentTask;
    private Map<Long, Long> edgeMap;

    private KafkaSender kafkaSender;

    // Todo abstract out these parameters to reusable class
    private final RestTemplate restTemplate;
    private String apiServerUrl;

    public ProcessLifeCycleManager(long processId, List<TaskResource> tasks, Map<Long, Long> edgeMap,
                                   KafkaSender kafkaSender,
                                   RestTemplate restTemplate, String apiServerUrl) {
        this.processId = processId;
        this.tasks = tasks;
        this.edgeMap = edgeMap;
        this.kafkaSender = kafkaSender;
        this.restTemplate = restTemplate;
        this.apiServerUrl = apiServerUrl;
    }

    public void init() {

        Optional<TaskResource> startingTask = tasks.stream().filter(TaskResource::isStartingTask).findFirst();
        if (startingTask.isPresent()) {
            this.currentTask = startingTask.get();
        } else {
            System.out.println("No starting task for this process " + processId);
            updateProcessStatus(ProcessStatusResource.State.CANCELED, "No starting task for this process");
        }

    }

    public void start() {
        updateProcessStatus(ProcessStatusResource.State.EXECUTING);
        System.out.println("Starting process " + processId + " with task " + currentTask.getName());

        TaskContext startContext = new TaskContext();
        startContext.assignTask(currentTask);

        submitTaskToQueue(currentTask.getTaskType().getTopicName(), startContext);
    }

    public synchronized void onTaskStateChanged(TaskContext taskContext) {

        updateProcessStatus(ProcessStatusResource.State.MONITORING, "Task moved to state "
                + ProcessStatusResource.State.valueOf(taskContext.getStatus()).name());

        if (taskContext.getTaskId() != currentTask.getId()) {
            System.out.println("Incompatible task status received. " +
                    "Currently running task id " + currentTask.getId() + " received task id " + taskContext.getTaskId());
            updateProcessStatus(ProcessStatusResource.State.FAILED, "Incompatible task status received. " +
                    "Currently running task id " + currentTask.getId() + " received task id " + taskContext.getTaskId());
            return;
        } else {
            System.out.println("Compatible task status received");
        }

        switch (taskContext.getStatus()) {
            case TaskStatusResource.State.COMPLETED:

                if (currentTask.isStoppingTask()) {
                    System.out.println("Process completed with last task " + currentTask.getName());
                    updateProcessStatus(ProcessStatusResource.State.COMPLETED, "Process completed with last task " + currentTask.getName());

                } else {
                    Optional<TaskOutPortResource> nextOutPort = currentTask.getOutPorts().stream()
                            .filter(port -> port.getId() == taskContext.getOutPortId()).findFirst();
                    if (nextOutPort.isPresent()) {

                        if (edgeMap.containsKey(nextOutPort.get().getId())) {
                            Long nextTaskId = edgeMap.get(nextOutPort.get().getId());
                            Optional<TaskResource> nextTask = tasks.stream().filter(task -> task.getId() == nextTaskId).findFirst();

                            if (nextTask.isPresent()) {

                                this.currentTask = nextTask.get();
                                taskContext.assignTask(this.currentTask);
                                System.out.println("Submitting next task " + this.currentTask.getName() + " of process " + processId);
                                submitTaskToQueue(this.currentTask.getTaskType().getTopicName(), taskContext);

                            } else {
                                System.out.println("Next task with id " + nextTaskId + " can not be found");
                                updateProcessStatus(ProcessStatusResource.State.FAILED, "Next task with id "
                                        + nextTaskId + " can not be found");
                                return;
                            }

                        } else {
                            System.out.println("Incomplete graph. Next outport " + nextOutPort.get().getName()
                                    + " of task " + currentTask.getName() + " ends with a no endpoint");
                            updateProcessStatus(ProcessStatusResource.State.FAILED, "Incomplete graph. Next outport "
                                    + nextOutPort.get().getName() + " of task " + currentTask.getName()
                                    + " ends with a no endpoint");
                            return;
                        }
                    } else {
                        System.out.println("Invalid out port " + taskContext.getOutPortId() + " for task " + taskContext.getTaskId());
                        updateProcessStatus(ProcessStatusResource.State.FAILED,
                                "Invalid out port " + taskContext.getOutPortId() + " for task " + taskContext.getTaskId());
                    }
                }
                break;
            case TaskStatusResource.State.FAILED:
                updateProcessStatus(ProcessStatusResource.State.FAILED);
                break;
        }
    }

    private void submitTaskToQueue(String topicName, TaskContext taskContext) {
        updateProcessStatus(ProcessStatusResource.State.MONITORING, "Submitting task " + taskContext.getTaskId() + " to queue");
        kafkaSender.send(topicName, taskContext);
    }

    private void updateProcessStatus(ProcessStatusResource.State state) {
        updateProcessStatus(state, "");
    }

    private void updateProcessStatus(ProcessStatusResource.State state, String reason) {
        this.restTemplate.postForObject("http://" + apiServerUrl + "/process/" + this.processId + "/status",
                new ProcessStatusResource()
                        .setState(state.getValue())
                        .setReason(reason)
                        .setTimeOfStateChange(System.currentTimeMillis()),
                Long.class);
    }

}
