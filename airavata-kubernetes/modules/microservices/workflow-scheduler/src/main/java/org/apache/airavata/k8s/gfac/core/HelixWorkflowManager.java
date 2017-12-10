package org.apache.airavata.k8s.gfac.core;

import org.apache.airavata.k8s.api.resources.process.ProcessBootstrapDataResource;
import org.apache.airavata.k8s.api.resources.process.ProcessStatusResource;
import org.apache.airavata.k8s.api.resources.task.TaskResource;
import org.apache.helix.HelixManagerFactory;
import org.apache.helix.InstanceType;
import org.apache.helix.task.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class HelixWorkflowManager {

    private static final Logger logger = LogManager.getLogger(HelixWorkflowManager.class);

    private long processId;
    private List<TaskResource> tasks;
    private List<ProcessBootstrapDataResource> boostrapData;

    // out port id, next task id
    private Map<Long, Long> edgeMap;

    // Todo abstract out these parameters to reusable class
    private final RestTemplate restTemplate;
    private String apiServerUrl;

    private String zkConnectionString;
    private String helixClusterName;
    private String instanceName;

    public HelixWorkflowManager(long processId, List<TaskResource> tasks, List<ProcessBootstrapDataResource> boostrapData,
                                Map<Long, Long> edgeMap, RestTemplate restTemplate, String apiServerUrl,
                                String zkConnectionString, String helixClusterName, String instanceName) {
        this.processId = processId;
        this.tasks = tasks;
        this.edgeMap = edgeMap;
        this.restTemplate = restTemplate;
        this.apiServerUrl = apiServerUrl;
        this.zkConnectionString = zkConnectionString;
        this.helixClusterName = helixClusterName;
        this.instanceName = instanceName;
        this.boostrapData = boostrapData;
    }

    public void launchWorkflow() {
        org.apache.helix.HelixManager helixManager = HelixManagerFactory.getZKHelixManager(helixClusterName, instanceName,
                InstanceType.SPECTATOR, zkConnectionString);

        try {
            updateProcessStatus(ProcessStatusResource.State.CREATED);
            Workflow.Builder workflowBuilder = createWorkflow(this.boostrapData);
            WorkflowConfig.Builder config = new WorkflowConfig.Builder().setFailureThreshold(0);
            workflowBuilder.setWorkflowConfig(config.build());
            if (workflowBuilder == null) {
                throw new Exception("Failed to create a workflow for process id " + processId);
            }

            Workflow workflow = workflowBuilder.build();

            updateProcessStatus(ProcessStatusResource.State.VALIDATED);

            helixManager.connect();
            TaskDriver taskDriver = new TaskDriver(helixManager);

            Runtime.getRuntime().addShutdownHook(
                    new Thread() {
                        @Override
                        public void run() {
                            helixManager.disconnect();
                        }
                    }
            );

            taskDriver.start(workflow);

            updateProcessStatus(ProcessStatusResource.State.STARTED);

            logger.info("Started workflow");
            TaskState taskState = taskDriver.pollForWorkflowState(workflow.getName(), TaskState.COMPLETED, TaskState.FAILED, TaskState.STOPPED, TaskState.ABORTED);
            updateProcessStatus(taskState);
            System.out.println("Workflow state " + taskState.name());

        } catch (Exception ex) {
            logger.error("Error in connect() for Admin, reason: " + ex, ex);
        }
    }

    private Workflow.Builder createWorkflow(List<ProcessBootstrapDataResource> bootstrapData) {
        Optional<TaskResource> startingTask = tasks.stream().filter(TaskResource::isStartingTask).findFirst();
        if (startingTask.isPresent()) {
            Workflow.Builder workflow = new Workflow.Builder("Airavata_Process_" + processId).setExpiry(0);
            createWorkflowRecursively(startingTask.get(), workflow, null, bootstrapData);
            return workflow;
        } else {
            System.out.println("No starting task for this process " + processId);
            updateProcessStatus(ProcessStatusResource.State.CANCELED, "No starting task for this process");
            return null;
        }
    }

    private void createWorkflowRecursively(TaskResource taskResource, Workflow.Builder workflow, Long parentTaskId,
                                           List<ProcessBootstrapDataResource> boostrapData) {

        TaskConfig.Builder taskBuilder = new TaskConfig.Builder().setTaskId("Task_" + taskResource.getId())
                .setCommand(taskResource.getTaskType().getName());

        Optional.ofNullable(taskResource.getInputs()).ifPresent(inputs -> inputs.forEach(input -> {
            taskBuilder.addConfig(input.getName(), input.getValue());
        }));

        taskBuilder.addConfig("task_id", taskResource.getId() + "");
        taskBuilder.addConfig("process_id", taskResource.getParentProcessId() + "");

        Optional.ofNullable(boostrapData).ifPresent(data -> {
            data.forEach(d -> taskBuilder.addConfig(d.getKey(), d.getValue()));
        });

        Optional.ofNullable(taskResource.getOutPorts()).ifPresent(outPorts -> outPorts.forEach(outPort -> {
            Optional.ofNullable(edgeMap.get(outPort.getId())).ifPresent(nextTask -> {
                Optional<TaskResource> nextTaskResource = tasks.stream().filter(task -> task.getId() == nextTask).findFirst();
                nextTaskResource.ifPresent(t -> {
                    taskBuilder.addConfig("OUT_"+ outPort.getName(), "JOB_" + t.getId());
                });
            });
        }));


        List<TaskConfig> taskBuilds = new ArrayList<>();
        taskBuilds.add(taskBuilder.build());

        JobConfig.Builder job = new JobConfig.Builder()
                .addTaskConfigs(taskBuilds)
                .setFailureThreshold(0)
                .setMaxAttemptsPerTask(3)
                .setInstanceGroupTag(taskResource.getTaskType().getName());

        workflow.addJob(("JOB_" + taskResource.getId()), job);
        if (parentTaskId != null) {
            workflow.addParentChildDependency("JOB_" + parentTaskId, "JOB_" + taskResource.getId());
        }

        Optional.ofNullable(taskResource.getOutPorts()).ifPresent(outPorts -> outPorts.forEach(outPort -> {
            Optional.ofNullable(edgeMap.get(outPort.getId())).ifPresent(nextTask -> {
                Optional<TaskResource> nextTaskResource = tasks.stream().filter(task -> task.getId() == nextTask).findFirst();
                nextTaskResource.ifPresent(t -> {

                    createWorkflowRecursively(t, workflow, taskResource.getId(), null);
                });
            });
        }));
    }

    private void updateProcessStatus(TaskState taskState) {
        switch (taskState) {
            case ABORTED:
                updateProcessStatus(ProcessStatusResource.State.ABORTED);
                break;
            case COMPLETED:
                updateProcessStatus(ProcessStatusResource.State.COMPLETED);
                break;
            case STOPPED:
                updateProcessStatus(ProcessStatusResource.State.STOPPED);
                break;
            case NOT_STARTED:
                updateProcessStatus(ProcessStatusResource.State.NOT_STARTED);
                break;
            case FAILED:
                updateProcessStatus(ProcessStatusResource.State.FAILED);
                break;
            case IN_PROGRESS:
                updateProcessStatus(ProcessStatusResource.State.EXECUTING);
                break;
        }
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
