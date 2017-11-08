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
package org.apache.airavata.k8s.api.server.service.task;

import org.apache.airavata.k8s.api.resources.task.TaskDagResource;
import org.apache.airavata.k8s.api.resources.task.TaskResource;
import org.apache.airavata.k8s.api.resources.task.TaskStatusResource;
import org.apache.airavata.k8s.api.server.ServerRuntimeException;
import org.apache.airavata.k8s.api.server.model.task.*;
import org.apache.airavata.k8s.api.server.repository.process.ProcessRepository;
import org.apache.airavata.k8s.api.server.repository.task.*;
import org.apache.airavata.k8s.api.server.repository.task.type.TaskTypeRepository;
import org.apache.airavata.k8s.api.server.service.util.ToResourceUtil;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
@Service
public class TaskService {

    private ProcessRepository processRepository;
    private TaskRepository taskRepository;
    private TaskInputRepository taskInputRepository;
    private TaskOutputRepository taskOutputRepository;
    private TaskStatusRepository taskStatusRepository;
    private TaskTypeRepository taskTypeRepository;
    private TaskOutPortRepository taskOutPortRepository;
    private TaskDAGRepository taskDAGRepository;


    public TaskService(ProcessRepository processRepository,
                       TaskRepository taskRepository,
                       TaskInputRepository taskInputRepository,
                       TaskOutputRepository taskOutputRepository,
                       TaskStatusRepository taskStatusRepository,
                       TaskTypeRepository taskTypeRepository,
                       TaskOutPortRepository taskOutPortRepository,
                       TaskDAGRepository taskDAGRepository) {

        this.processRepository = processRepository;
        this.taskRepository = taskRepository;
        this.taskInputRepository = taskInputRepository;
        this.taskOutputRepository = taskOutputRepository;
        this.taskStatusRepository = taskStatusRepository;
        this.taskTypeRepository = taskTypeRepository;
        this.taskOutPortRepository = taskOutPortRepository;
        this.taskDAGRepository = taskDAGRepository;
    }

    public long create(TaskResource resource) {
        TaskModel taskModel = new TaskModel();
        taskModel.setName(resource.getName());
        taskModel.setCreationTime(resource.getCreationTime());
        taskModel.setLastUpdateTime(resource.getLastUpdateTime());
        taskModel.setOrderIndex(resource.getOrder());
        taskModel.setStartingTask(resource.isStartingTask());
        taskModel.setStoppingTask(resource.isStoppingTask());
        taskModel.setTaskDetail(resource.getTaskDetail());
        taskModel.setReferenceId(resource.getReferenceId());
        taskModel.setParentProcess(processRepository.findById(resource.getParentProcessId())
                .orElseThrow(() -> new ServerRuntimeException("Can not find process with id " +
                        resource.getParentProcessId())));
        taskModel.setTaskType(taskTypeRepository.findById(resource.getTaskType().getId())
                .orElseThrow(() -> new ServerRuntimeException("Can not find task type with id " +
                resource.getTaskType().getId())));

        TaskModel savedTask = taskRepository.save(taskModel);

        Optional.ofNullable(resource.getInputs()).ifPresent(inputs -> inputs.forEach(input -> {
            TaskInput taskInput = new TaskInput();
            taskInput.setName(input.getName());
            taskInput.setValue(input.getValue());
            taskInput.setType(input.getType());
            taskInput.setImportFrom(input.getImportFrom());
            taskInput.setTaskModel(savedTask);
            taskInputRepository.save(taskInput);
        }));

        Optional.ofNullable(resource.getOutputs()).ifPresent(outputs -> outputs.forEach(output -> {
            TaskOutput taskOutput = new TaskOutput();
            taskOutput.setName(output.getName());
            taskOutput.setValue(output.getValue());
            taskOutput.setType(output.getType());
            taskOutput.setExportTo(output.getExportTo());
            taskOutput.setTaskModel(savedTask);
            taskOutputRepository.save(taskOutput);
        }));

        Optional.ofNullable(resource.getOutPorts()).ifPresent(outPorts -> outPorts.forEach(outPort -> {
            TaskOutPort taskOutPort = new TaskOutPort();
            taskOutPort.setName(outPort.getName());
            taskOutPort.setReferenceId(outPort.getReferenceId());
            taskOutPort.setTaskModel(taskModel);
            taskOutPortRepository.save(taskOutPort);
        }));
        return savedTask.getId();
    }

    public long addTaskStatus(long taskId, TaskStatusResource resource) {

        TaskModel taskModel = taskRepository.findById(taskId)
                .orElseThrow(() -> new ServerRuntimeException("Task with id " + taskId + " can not be found"));

        TaskStatus status = new TaskStatus();
        status.setReason(resource.getReason());
        status.setState(TaskStatus.TaskState.valueOf(resource.getState()));
        status.setTimeOfStateChange(resource.getTimeOfStateChange());
        status.setTaskModel(taskModel);
        TaskStatus savedStatus = taskStatusRepository.save(status);

        return savedStatus.getId();
    }

    public Optional<TaskStatusResource> findTaskStatusById(long id) {
        return ToResourceUtil.toResource(taskStatusRepository.findById(id).get());
    }

    public Optional<TaskResource> findById(long id) {
        return ToResourceUtil.toResource(taskRepository.findById(id).get());
    }

    public Set<TaskDagResource> getDagForProcess(long processId) {
        Set<TaskDagResource> taskDagResources = new HashSet<>();
        Iterable<TaskDAG> taskDags = this.taskDAGRepository.findBysourceOutPort_taskModel_parentProcess_id(processId);
        Optional.ofNullable(taskDags).ifPresent(dags -> dags.forEach(taskDAG -> {
           taskDagResources.add(ToResourceUtil.toResource(taskDAG).get());
        }));
        return taskDagResources;
    }
}
