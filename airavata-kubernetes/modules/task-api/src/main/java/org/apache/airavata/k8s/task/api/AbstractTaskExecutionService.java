package org.apache.airavata.k8s.task.api;

import org.apache.airavata.k8s.api.resources.compute.ComputeResource;
import org.apache.airavata.k8s.api.resources.task.TaskInputResource;
import org.apache.airavata.k8s.api.resources.task.TaskResource;
import org.apache.airavata.k8s.api.resources.task.TaskStatusResource;
import org.apache.airavata.k8s.api.resources.task.type.TaskTypeResource;
import org.apache.airavata.k8s.compute.api.ComputeOperations;
import org.apache.airavata.k8s.compute.impl.MockComputeOperation;
import org.apache.airavata.k8s.compute.impl.SSHComputeOperations;
import org.apache.airavata.k8s.task.api.messaging.KafkaSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public abstract class AbstractTaskExecutionService {

    private final ExecutorService executorService;

    private final RestTemplate restTemplate;
    private final KafkaSender kafkaSender;

    @Value("${api.server.url}")
    private String apiServerUrl;

    @Value("${task.event.topic.name}")
    private String taskEventPublishTopic;

    public AbstractTaskExecutionService(RestTemplate restTemplate, KafkaSender kafkaSender, int concurrentTasks) {
        this.restTemplate = restTemplate;
        this.kafkaSender = kafkaSender;
        executorService = Executors.newFixedThreadPool(concurrentTasks);
    }

    @PostConstruct
    public void init() {
        getRestTemplate().postForObject("http://" + apiServerUrl + "/taskType", getType(), Long.class);
    }

    public abstract TaskTypeResource getType();

    public void executeTaskAsync(TaskContext taskContext) {

        System.out.println("Executing task " + taskContext.getTaskId());
        TaskResource taskResource = this.restTemplate.getForObject("http://" + apiServerUrl + "/task/" + taskContext.getTaskId(), TaskResource.class);

        publishTaskStatus(taskResource.getParentProcessId(), taskResource.getId(), TaskStatusResource.State.SCHEDULED);

        this.executorService.execute(() -> {
            try {
                initializeParameters(taskResource, taskContext);
                executeTask(taskResource, taskContext);
            } catch (Exception e) {
                e.printStackTrace();
                // Ignore silently as this is already handled
                // TODO add a new exception type
            }
        });
    }

    public ComputeOperations fetchComputeResourceOperation(ComputeResource computeResource) throws Exception {
        ComputeOperations operations;
        if ("SSH".equals(computeResource.getCommunicationType())) {
            operations = new SSHComputeOperations(computeResource.getHost(), computeResource.getUserName(), computeResource.getPassword());
        } else if ("Mock".equals(computeResource.getCommunicationType())) {
            operations = new MockComputeOperation(computeResource.getHost());
        } else {
            throw new Exception("No compatible communication method {" + computeResource.getCommunicationType() + "} not found for compute resource " + computeResource.getName());
        }
        return operations;
    }

    public String findInput(TaskResource taskResource, String name, boolean optional) throws Exception {

        Optional<TaskInputResource> inputResource = taskResource.getInputs()
                .stream()
                .filter(input -> name.equals(input.getValue()))
                .findFirst();

        if (inputResource.isPresent()) {
            return inputResource.get().getValue();

        } else {
            if (!optional) {
                publishTaskStatus(taskResource.getParentProcessId(), taskResource.getId(), TaskStatusResource.State.FAILED,
                        name + " is not available in inputs");
                throw new Exception(name + " is not available in inputs");
            } else {
                return null;
            }
        }
    }

    public abstract void initializeParameters(TaskResource taskResource, TaskContext taskContext) throws Exception;
    public abstract void executeTask(TaskResource taskResource, TaskContext taskContext);

    public void publishTaskStatus(long processId, long taskId, int status) {
        publishTaskStatus(processId, taskId, status, "");
    }

    public void publishTaskStatus(long processId, long taskId, int status, String reason) {
        this.kafkaSender.send(this.taskEventPublishTopic, processId + "-" + taskId,
                processId + "," + taskId + "," + status + "," + reason);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public String getApiServerUrl() {
        return apiServerUrl;
    }
}
