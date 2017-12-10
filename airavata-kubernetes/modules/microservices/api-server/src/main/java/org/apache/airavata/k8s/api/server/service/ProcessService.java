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
package org.apache.airavata.k8s.api.server.service;

import org.apache.airavata.k8s.api.resources.process.ProcessStatusResource;
import org.apache.airavata.k8s.api.server.ServerRuntimeException;
import org.apache.airavata.k8s.api.server.model.process.ProcessBootstrapData;
import org.apache.airavata.k8s.api.server.model.process.ProcessModel;
import org.apache.airavata.k8s.api.server.model.process.ProcessStatus;
import org.apache.airavata.k8s.api.server.model.task.TaskModel;
import org.apache.airavata.k8s.api.server.repository.process.ProcessBootstrapDataRepository;
import org.apache.airavata.k8s.api.server.repository.process.ProcessRepository;
import org.apache.airavata.k8s.api.resources.process.ProcessResource;
import org.apache.airavata.k8s.api.server.repository.process.ProcessStatusRepository;
import org.apache.airavata.k8s.api.server.repository.workflow.WorkflowRepository;
import org.apache.airavata.k8s.api.server.service.task.TaskService;
import org.apache.airavata.k8s.api.server.service.util.ToResourceUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
@Service
public class ProcessService {

    private ProcessRepository processRepository;
    private ProcessStatusRepository processStatusRepository;
    private ProcessBootstrapDataRepository bootstrapDataRepository;

    private ExperimentService experimentService;
    private TaskService taskService;

    private WorkflowRepository workflowRepository;

    public ProcessService(ProcessRepository processRepository,
                          ProcessStatusRepository processStatusRepository,
                          ExperimentService experimentService,
                          TaskService taskService,
                          WorkflowRepository workflowRepository,
                          ProcessBootstrapDataRepository bootstrapDataRepository) {

        this.processRepository = processRepository;
        this.processStatusRepository = processStatusRepository;
        this.experimentService = experimentService;
        this.taskService = taskService;
        this.workflowRepository = workflowRepository;
        this.bootstrapDataRepository = bootstrapDataRepository;
    }

    public long create(ProcessResource resource) {

        ProcessModel processModel = new ProcessModel();
        processModel.setId(resource.getId());
        processModel.setCreationTime(resource.getCreationTime());
        processModel.setLastUpdateTime(resource.getLastUpdateTime());
        processModel.setName(resource.getName());
        processModel.setProcessType(ProcessModel.ProcessType.valueOf(resource.getProcessType()));

        if (resource.getExperimentId() != 0) {
            processModel.setExperiment(experimentService.findEntityById(resource.getExperimentId())
                    .orElseThrow(() -> new ServerRuntimeException("Can not find experiment with id " +
                            resource.getExperimentId())));
        }

        if (resource.getWorkflowId() != 0) {
            processModel.setWorkflow(workflowRepository.findById(resource.getWorkflowId())
                    .orElseThrow(() -> new ServerRuntimeException("Can not find workflow with id " +
                            resource.getWorkflowId())));
        }

        processModel.setExperimentDataDir(resource.getExperimentDataDir());

        ProcessModel saved = processRepository.save(processModel);

        Optional.ofNullable(resource.getTasks()).ifPresent(taskResources -> taskResources.forEach(taskRes -> {
            TaskModel taskModel = new TaskModel();
            taskRes.setParentProcessId(saved.getId());
            taskModel.setId(taskService.create(taskRes));
        }));

        Optional.ofNullable(resource.getProcessBootstrapData()).ifPresent(bootstrapDatas -> bootstrapDatas.forEach(data -> {
            ProcessBootstrapData bootstrapData = new ProcessBootstrapData();
            bootstrapData.setProcessModel(saved);
            bootstrapData.setKey(data.getKey());
            bootstrapData.setValue(data.getValue());
            this.bootstrapDataRepository.save(bootstrapData);
        }));

        return saved.getId();
    }

    public long addProcessStatus(long processId, ProcessStatusResource resource) {
        ProcessModel processModel = processRepository.findById(processId)
                .orElseThrow(() -> new ServerRuntimeException("Process with id " + processId + " can not be found"));

        ProcessStatus status = new ProcessStatus();
        status.setReason(resource.getReason());
        status.setState(ProcessStatus.ProcessState.valueOf(resource.getState()));
        status.setTimeOfStateChange(resource.getTimeOfStateChange());
        status.setProcessModel(processModel);
        ProcessStatus savedStatus = processStatusRepository.save(status);
        return savedStatus.getId();
    }

    public Optional<ProcessResource> findById(long id) {
        return ToResourceUtil.toResource(processRepository.findById(id).get());
    }
}
