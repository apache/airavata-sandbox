package org.apache.airavata.k8s.gfac.core;

import org.apache.airavata.k8s.api.resources.process.ProcessStatusResource;
import org.apache.airavata.k8s.api.resources.task.TaskResource;
import org.apache.airavata.k8s.gfac.messaging.KafkaSender;
import org.apache.helix.HelixManagerFactory;
import org.apache.helix.InstanceType;
import org.apache.helix.task.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.zookeeper.Op;
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

    // out port id, next task id
    private Map<Long, Long> edgeMap;

    private KafkaSender kafkaSender;

    // Todo abstract out these parameters to reusable class
    private final RestTemplate restTemplate;
    private String apiServerUrl;

    public HelixWorkflowManager(long processId, List<TaskResource> tasks, Map<Long, Long> edgeMap,
                                KafkaSender kafkaSender,
                                RestTemplate restTemplate, String apiServerUrl) {
        this.processId = processId;
        this.tasks = tasks;
        this.edgeMap = edgeMap;
        this.kafkaSender = kafkaSender;
        this.restTemplate = restTemplate;
        this.apiServerUrl = apiServerUrl;
    }

    public void launchWorkflow() {
        org.apache.helix.HelixManager helixManager = HelixManagerFactory.getZKHelixManager("AiravataDemoCluster", "Admin",
                InstanceType.SPECTATOR, "localhost:2199");

        try {

            Workflow.Builder workflowBuilder = createWorkflow();
            WorkflowConfig.Builder config = new WorkflowConfig.Builder().setFailureThreshold(0);
            workflowBuilder.setWorkflowConfig(config.build());
            if (workflowBuilder == null) {
                throw new Exception("Failed to create a workflow for process id " + processId);
            }

            Workflow workflow = workflowBuilder.build();

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
            logger.info("Started workflow");
            TaskState taskState = taskDriver.pollForWorkflowState(workflow.getName(), TaskState.COMPLETED, TaskState.FAILED, TaskState.STOPPED, TaskState.ABORTED);
            System.out.println("Workflow state " + taskState.name());

        } catch (Exception ex) {
            logger.error("Error in connect() for Admin, reason: " + ex, ex);
        }
    }

    private Workflow.Builder createWorkflow() {
        Optional<TaskResource> startingTask = tasks.stream().filter(TaskResource::isStartingTask).findFirst();
        if (startingTask.isPresent()) {
            Workflow.Builder workflow = new Workflow.Builder("Airavata_Process_" + processId).setExpiry(0);
            createWorkflowRecursively(startingTask.get(), workflow, null);
            return workflow;
        } else {
            System.out.println("No starting task for this process " + processId);
            updateProcessStatus(ProcessStatusResource.State.CANCELED, "No starting task for this process");
            return null;
        }
    }

    private void createWorkflowRecursively(TaskResource taskResource, Workflow.Builder workflow, Long parentTaskId) {

        TaskConfig.Builder taskBuilder = new TaskConfig.Builder().setTaskId("Task_" + taskResource.getId())
                .setCommand(taskResource.getTaskType().getName());

        Optional.ofNullable(taskResource.getInputs()).ifPresent(inputs -> inputs.forEach(input -> {
            taskBuilder.addConfig(input.getName(), input.getValue());
        }));

        taskBuilder.addConfig("task_id", taskResource.getId() + "");
        taskBuilder.addConfig("process_id", taskResource.getParentProcessId() + "");

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

                    createWorkflowRecursively(t, workflow, taskResource.getId());
                });
            });
        }));
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
