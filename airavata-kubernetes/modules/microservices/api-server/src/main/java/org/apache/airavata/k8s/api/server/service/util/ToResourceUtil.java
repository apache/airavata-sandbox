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
package org.apache.airavata.k8s.api.server.service.util;

import org.apache.airavata.k8s.api.resources.experiment.ExperimentStatusResource;
import org.apache.airavata.k8s.api.resources.process.ProcessBootstrapDataResource;
import org.apache.airavata.k8s.api.resources.process.ProcessStatusResource;
import org.apache.airavata.k8s.api.resources.task.*;
import org.apache.airavata.k8s.api.resources.task.type.TaskInputTypeResource;
import org.apache.airavata.k8s.api.resources.task.type.TaskOutPortTypeResource;
import org.apache.airavata.k8s.api.resources.task.type.TaskOutputTypeResource;
import org.apache.airavata.k8s.api.resources.task.type.TaskTypeResource;
import org.apache.airavata.k8s.api.resources.workflow.WorkflowResource;
import org.apache.airavata.k8s.api.server.model.application.ApplicationDeployment;
import org.apache.airavata.k8s.api.server.model.application.ApplicationInterface;
import org.apache.airavata.k8s.api.server.model.application.ApplicationModule;
import org.apache.airavata.k8s.api.server.model.compute.ComputeResourceModel;
import org.apache.airavata.k8s.api.server.model.experiment.Experiment;
import org.apache.airavata.k8s.api.server.model.experiment.ExperimentInputData;
import org.apache.airavata.k8s.api.server.model.experiment.ExperimentOutputData;
import org.apache.airavata.k8s.api.server.model.experiment.ExperimentStatus;
import org.apache.airavata.k8s.api.server.model.process.ProcessBootstrapData;
import org.apache.airavata.k8s.api.server.model.process.ProcessModel;
import org.apache.airavata.k8s.api.server.model.process.ProcessStatus;
import org.apache.airavata.k8s.api.server.model.task.*;
import org.apache.airavata.k8s.api.resources.application.*;
import org.apache.airavata.k8s.api.resources.compute.ComputeResource;
import org.apache.airavata.k8s.api.resources.experiment.ExperimentInputResource;
import org.apache.airavata.k8s.api.resources.experiment.ExperimentOutputResource;
import org.apache.airavata.k8s.api.resources.experiment.ExperimentResource;
import org.apache.airavata.k8s.api.resources.process.ProcessResource;
import org.apache.airavata.k8s.api.server.model.task.type.TaskInputType;
import org.apache.airavata.k8s.api.server.model.task.type.TaskModelType;
import org.apache.airavata.k8s.api.server.model.task.type.TaskOutPortType;
import org.apache.airavata.k8s.api.server.model.task.type.TaskOutputType;
import org.apache.airavata.k8s.api.server.model.workflow.Workflow;

import java.util.Optional;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class ToResourceUtil {

    public static Optional<ExperimentStatusResource> toResource(ExperimentStatus experimentStatus) {
        if (experimentStatus != null) {
            ExperimentStatusResource resource = new ExperimentStatusResource();
            resource.setId(experimentStatus.getId());
            resource.setState(experimentStatus.getState().getValue());
            resource.setReason(experimentStatus.getReason());
            resource.setStateStr(experimentStatus.getState().name());
            resource.setTimeOfStateChange(experimentStatus.getTimeOfStateChange());
            return Optional.of(resource);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<ExperimentResource> toResource(Experiment experiment) {

        if (experiment != null) {
            ExperimentResource resource = new ExperimentResource();
            resource.setId(experiment.getId());
            resource.setExperimentName(experiment.getExperimentName());
            resource.setDescription(experiment.getDescription());
            resource.setCreationTime(experiment.getCreationTime());
            Optional.ofNullable(experiment.getErrors())
                    .ifPresent(errs -> errs.forEach(err -> resource.getErrorsIds().add(err.getId())));
            Optional.ofNullable(experiment.getExperimentStatus())
                    .ifPresent(sts -> sts.forEach(status -> resource.getExperimentStatus().add(toResource(status).get())));
            Optional.ofNullable(experiment.getExperimentInputs())
                    .ifPresent(ips -> ips.forEach(ip -> resource.getExperimentInputs().add(toResource(ip).get())));
            Optional.ofNullable(experiment.getExperimentOutputs())
                    .ifPresent(ops -> ops.forEach(op -> resource.getExperimentOutputs().add(toResource(op).get())));
            Optional.ofNullable(experiment.getApplicationDeployment())
                    .ifPresent(appDep -> {
                        resource.setApplicationDeploymentId(appDep.getId());
                        resource.setApplicationDeploymentName(appDep.getName());
                    });
            Optional.ofNullable(experiment.getApplicationInterface())
                    .ifPresent(iface -> {
                        resource.setApplicationInterfaceId(iface.getId());
                        resource.setApplicationInterfaceName(iface.getName());
                    });
            Optional.ofNullable(experiment.getProcesses())
                    .ifPresent(processModels -> processModels
                            .forEach(processModel -> resource.getProcessIds().add(processModel.getId())));
            return Optional.of(resource);

        } else {
            return Optional.empty();
        }
    }

    public static Optional<ExperimentInputResource> toResource(ExperimentInputData input) {
        if (input != null) {
            ExperimentInputResource resource = new ExperimentInputResource();
            resource.setId(input.getId());
            resource.setName(input.getName());
            resource.setValue(input.getValue());
            resource.setType(input.getType().getValue());
            resource.setArguments(input.getArguments());
            return Optional.of(resource);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<ExperimentOutputResource> toResource(ExperimentOutputData output) {
        if (output != null) {
            ExperimentOutputResource resource = new ExperimentOutputResource();
            resource.setId(output.getId());
            resource.setName(output.getName());
            resource.setValue(output.getValue());
            resource.setType(output.getType().getValue());
            return Optional.of(resource);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<ComputeResource> toResource(ComputeResourceModel computeResourceModel) {

        if (computeResourceModel != null) {
            ComputeResource resource = new ComputeResource();
            resource.setId(computeResourceModel.getId());
            resource.setName(computeResourceModel.getName());
            resource.setUserName(computeResourceModel.getUserName());
            resource.setPassword(computeResourceModel.getPassword());
            resource.setCommunicationType(computeResourceModel.getCommunicationType());
            resource.setHost(computeResourceModel.getHost());
            return Optional.of(resource);

        } else {
            return Optional.empty();
        }
    }

    public static Optional<ApplicationModuleResource> toResource(ApplicationModule applicationModule) {

        if (applicationModule != null) {
            ApplicationModuleResource resource = new ApplicationModuleResource();
            resource.setId(applicationModule.getId());
            resource.setName(applicationModule.getName());
            resource.setDescription(applicationModule.getDescription());
            resource.setVersion(applicationModule.getVersion());
            return Optional.of(resource);

        } else {
            return Optional.empty();
        }
    }

    public static Optional<ApplicationDeploymentResource> toResource(ApplicationDeployment applicationDeployment) {
        if (applicationDeployment != null) {
            ApplicationDeploymentResource resource = new ApplicationDeploymentResource();
            resource.setId(applicationDeployment.getId());
            resource.setExecutablePath(applicationDeployment.getExecutablePath());
            resource.setPreJobCommand(applicationDeployment.getPreJobCommand());
            resource.setPostJobCommand(applicationDeployment.getPostJobCommand());
            resource.setName(applicationDeployment.getName());
            Optional.ofNullable(applicationDeployment.getApplicationModule())
                    .ifPresent(module -> resource.setApplicationModuleId(module.getId()));
            Optional.ofNullable(applicationDeployment.getComputeResource())
                    .ifPresent(cr -> resource.setComputeResourceId(cr.getId()));
            return Optional.of(resource);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<ApplicationIfaceResource> toResource(ApplicationInterface applicationInterface) {
        if (applicationInterface != null) {
            ApplicationIfaceResource resource = new ApplicationIfaceResource();
            resource.setId(applicationInterface.getId());
            resource.setName(applicationInterface.getName());
            resource.setApplicationModuleId(applicationInterface.getId());
            resource.setDescription(applicationInterface.getDescription());

            Optional.ofNullable(applicationInterface.getInputs()).ifPresent(ips -> ips.forEach(ip -> {
                ApplicationInputResource ipResource = new ApplicationInputResource();
                ipResource.setId(ip.getId());
                ipResource.setName(ip.getName());
                ipResource.setArguments(ip.getArguments());
                ipResource.setType(ip.getType().getValue());
                ipResource.setValue(ip.getValue());
                resource.getInputs().add(ipResource);
            }));

            Optional.ofNullable(applicationInterface.getOutputs()).ifPresent(ops -> ops.forEach(op -> {
                ApplicationOutputResource opResource = new ApplicationOutputResource();
                opResource.setId(op.getId());
                opResource.setName(op.getName());
                opResource.setType(op.getType().getValue());
                opResource.setValue(op.getValue());
                resource.getOutputs().add(opResource);
            }));

            return Optional.of(resource);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<TaskResource> toResource(TaskModel taskModel) {
        if (taskModel != null) {
            TaskResource resource = new TaskResource();
            resource.setName(taskModel.getName());
            resource.setId(taskModel.getId());
            resource.setLastUpdateTime(taskModel.getLastUpdateTime());
            resource.setCreationTime(taskModel.getCreationTime());
            resource.setParentProcessId(taskModel.getParentProcess().getId());
            resource.setTaskType(toResource(taskModel.getTaskType()).get());
            resource.setTaskTypeStr(taskModel.getTaskType().getName());
            resource.setTaskDetail(taskModel.getTaskDetail());
            resource.setStartingTask(taskModel.isStartingTask());
            resource.setStoppingTask(taskModel.isStoppingTask());
            resource.setReferenceId(taskModel.getReferenceId());
            Optional.ofNullable(taskModel.getTaskInputs())
                    .ifPresent(inputs ->
                            inputs.forEach(input -> resource.getInputs()
                                    .add(toResource(input).get())));

            Optional.ofNullable(taskModel.getTaskOutputs())
                    .ifPresent(outputs ->
                            outputs.forEach(output -> resource.getOutputs()
                                    .add(toResource(output).get())));


            Optional.ofNullable(taskModel.getTaskStatuses())
                    .ifPresent(taskStatuses ->
                            taskStatuses.forEach(taskStatus -> resource.getTaskStatus()
                                    .add(toResource(taskStatus).get())));

            Optional.ofNullable(taskModel.getTaskOutPorts())
                    .ifPresent(outPorts -> outPorts.forEach(outPort -> resource.getOutPorts()
                            .add(toResource(outPort).get())));

            resource.setOrder(taskModel.getOrderIndex());
            return Optional.of(resource);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<TaskStatusResource> toResource(TaskStatus taskStatus) {
        if (taskStatus != null) {
            TaskStatusResource resource = new TaskStatusResource();
            resource.setId(taskStatus.getId());
            resource.setState(taskStatus.getState().getValue());
            resource.setTimeOfStateChange(taskStatus.getTimeOfStateChange());
            resource.setTaskId(taskStatus.getTaskModel().getId());
            resource.setStateStr(taskStatus.getState().name());
            resource.setReason(taskStatus.getReason());
            return Optional.of(resource);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<TaskInputResource> toResource(TaskInput taskInput) {
        if (taskInput != null) {
            TaskInputResource resource = new TaskInputResource();
            resource.setId(taskInput.getId());
            resource.setName(taskInput.getName());
            resource.setValue(taskInput.getValue());
            resource.setType(taskInput.getType());
            resource.setImportFrom(taskInput.getImportFrom());
            return Optional.of(resource);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<TaskOutputResource> toResource(TaskOutput taskOutput) {
        if (taskOutput != null) {
            TaskOutputResource resource = new TaskOutputResource();
            resource.setId(taskOutput.getId());
            resource.setName(taskOutput.getName());
            resource.setValue(taskOutput.getValue());
            resource.setType(taskOutput.getType());
            resource.setExportTo(taskOutput.getExportTo());
            return Optional.of(resource);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<ProcessResource> toResource(ProcessModel processModel) {
        if (processModel != null) {
            ProcessResource processResource = new ProcessResource();
            processResource.setId(processModel.getId());
            processResource.setLastUpdateTime(processModel.getLastUpdateTime());

            Optional.ofNullable(processModel.getExperiment()).ifPresent(experiment -> {
                processResource.setExperimentId(experiment.getId());
            });
            Optional.ofNullable(processModel.getWorkflow()).ifPresent(workflow -> {
                processResource.setWorkflowId(workflow.getId());
            });

            processResource.setTaskDag(processModel.getTaskDag());
            processResource.setCreationTime(processModel.getCreationTime());
            Optional.ofNullable(processModel.getProcessStatuses())
                    .ifPresent(stss -> stss.forEach(sts -> processResource.getProcessStatuses().add(toResource(sts).get())));
            Optional.ofNullable(processModel.getTasks())
                    .ifPresent(tasks -> tasks.forEach(task -> processResource.getTasks().add(toResource(task).get())));
            Optional.ofNullable(processModel.getProcessErrors())
                    .ifPresent(errs -> errs.forEach(err -> processResource.getProcessErrorIds().add(err.getId())));
            Optional.ofNullable(processModel.getProcessBootstrapData())
                    .ifPresent(datas -> datas.forEach(data -> processResource.getProcessBootstrapData().add(toResource(data).get())));

            return Optional.of(processResource);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<ProcessBootstrapDataResource> toResource(ProcessBootstrapData bootstrapData) {
        if (bootstrapData != null) {
            ProcessBootstrapDataResource resource = new ProcessBootstrapDataResource();
            resource.setId(bootstrapData.getId());
            resource.setKey(bootstrapData.getKey());
            resource.setValue(bootstrapData.getValue());
            return Optional.of(resource);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<ProcessStatusResource> toResource(ProcessStatus processStatus) {
        if (processStatus != null) {
            ProcessStatusResource resource = new ProcessStatusResource();
            resource.setId(processStatus.getId());
            resource.setState(processStatus.getState().getValue());
            resource.setTimeOfStateChange(processStatus.getTimeOfStateChange());
            resource.setStateStr(processStatus.getState().name());
            resource.setReason(processStatus.getReason());
            resource.setProcessId(processStatus.getProcessModel().getId());
            return Optional.of(resource);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<TaskTypeResource> toResource(TaskModelType taskModelType) {

        if (taskModelType != null) {
            TaskTypeResource resource = new TaskTypeResource();
            resource.setName(taskModelType.getName());
            resource.setId(taskModelType.getId());
            resource.setTopicName(taskModelType.getTopicName());
            resource.setIcon(taskModelType.getIcon());

            Optional.ofNullable(taskModelType.getInputTypes())
                    .ifPresent(taskInputTypes -> taskInputTypes.forEach(taskInputType -> {
                        resource.getInputTypes().add(toResource(taskInputType).get());
                    }));

            Optional.ofNullable(taskModelType.getOutputTypes())
                    .ifPresent(taskOutputTypes -> taskOutputTypes.forEach(taskOutputType -> {
                        resource.getOutputTypes().add(toResource(taskOutputType).get());
                    }));

            Optional.ofNullable(taskModelType.getOutPorts())
                    .ifPresent(taskOutPorts -> taskOutPorts.forEach(taskOutPort -> {
                        resource.getOutPorts().add(toResource(taskOutPort).get());
                    }));
            return Optional.of(resource);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<TaskInputTypeResource> toResource(TaskInputType taskInputType) {
        if (taskInputType != null) {
            TaskInputTypeResource resource = new TaskInputTypeResource();
            resource.setId(taskInputType.getId());
            resource.setName(taskInputType.getName());
            resource.setType(taskInputType.getType());
            resource.setDefaultValue(taskInputType.getDefaultValue());
            return Optional.of(resource);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<TaskOutputTypeResource> toResource(TaskOutputType taskOutputType) {
        if (taskOutputType != null) {
            TaskOutputTypeResource resource = new TaskOutputTypeResource();
            resource.setId(taskOutputType.getId());
            resource.setName(taskOutputType.getName());
            resource.setType(taskOutputType.getType());
            return Optional.of(resource);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<TaskOutPortTypeResource> toResource(TaskOutPortType taskOutPort) {
        if (taskOutPort != null) {
            TaskOutPortTypeResource resource = new TaskOutPortTypeResource();
            resource.setId(taskOutPort.getId());
            resource.setName(taskOutPort.getName());
            resource.setOrder(taskOutPort.getOrder());
            return Optional.of(resource);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<WorkflowResource> toResource(Workflow workflow) {
        if (workflow != null) {
            WorkflowResource resource = new WorkflowResource();
            resource.setId(workflow.getId());
            resource.setName(workflow.getName());
            resource.setWorkflowGraphXML(new String(workflow.getWorkFlowGraph()));
            Optional.ofNullable(workflow.getProcesses()).ifPresent(processModels -> {
                processModels.forEach(processModel -> {
                    resource.getProcessIds().add(processModel.getId());
                });
            });
            return Optional.of(resource);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<TaskDagResource> toResource(TaskDAG taskDAG) {
        if (taskDAG != null) {
            TaskDagResource resource = new TaskDagResource();
            resource.setId(taskDAG.getId());
            resource.setSourceOutPort(toResource(taskDAG.getSourceOutPort()).get());
            resource.setTargetTask(toResource(taskDAG.getTargetTask()).get());
            return Optional.of(resource);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<TaskOutPortResource> toResource(TaskOutPort outPort) {
        if (outPort != null) {
            TaskOutPortResource resource = new TaskOutPortResource();
            resource.setId(outPort.getId());
            resource.setReferenceId(outPort.getReferenceId());
            resource.setName(outPort.getName());
            //resource.setTaskResource(toResource(outPort.getTaskModel()).get());
            return Optional.of(resource);
        } else {
            return Optional.empty();
        }
    }
}
