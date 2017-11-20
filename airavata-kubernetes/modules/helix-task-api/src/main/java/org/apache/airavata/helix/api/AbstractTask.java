package org.apache.airavata.helix.api;

import org.apache.airavata.k8s.api.resources.compute.ComputeResource;
import org.apache.airavata.k8s.api.resources.task.type.TaskTypeResource;
import org.apache.airavata.k8s.compute.api.ComputeOperations;
import org.apache.airavata.k8s.compute.impl.MockComputeOperation;
import org.apache.airavata.k8s.compute.impl.SSHComputeOperations;
import org.apache.helix.task.Task;
import org.apache.helix.task.TaskCallbackContext;
import org.apache.helix.task.TaskResult;
import org.apache.helix.task.UserContentStore;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public abstract class AbstractTask extends UserContentStore implements Task {

    public static final String NEXT_JOB = "next-job";
    public static final String WORKFLOW_STARTED = "workflow-started";
    public static final String TASK_ID = "task_id";
    public static final String PROCESS_ID = "process_id";

    //Configurable values
    private String apiServerUrl = "localhost:8080";
    private String kafkaBootstrapUrl = "localhost:9092";
    private String eventTopic = "airavata-task-event";

    private TaskCallbackContext callbackContext;
    private RestTemplate restTemplate;
    private Producer<String, String> eventProducer;
    private long processId;
    private long taskId;

    public AbstractTask(TaskCallbackContext callbackContext) {
        this.callbackContext = callbackContext;
        this.taskId = Long.parseLong(this.callbackContext.getTaskConfig().getConfigMap().get(TASK_ID));
        this.processId = Long.parseLong(this.callbackContext.getTaskConfig().getConfigMap().get(PROCESS_ID));
        this.restTemplate = new RestTemplate();
        initializeKafkaEventProducer();
        init();
    }

    public TaskCallbackContext getCallbackContext() {
        return callbackContext;
    }

    @Override
    public final TaskResult run() {
        boolean isThisNextJob = getUserContent(WORKFLOW_STARTED, Scope.WORKFLOW) == null ||
                this.callbackContext.getJobConfig().getJobId()
                        .equals(this.callbackContext.getJobConfig().getWorkflow() + "_" + getUserContent(NEXT_JOB, Scope.WORKFLOW));
        if (isThisNextJob) {
            return onRun();
        } else {
            return new TaskResult(TaskResult.Status.COMPLETED, "Not a target job");
        }
    }

    @Override
    public final void cancel() {
        onCancel();
    }

    public void init() {

    }

    public abstract TaskResult onRun();

    public abstract void onCancel();

    public void sendToOutPort(String port) {
        putUserContent(WORKFLOW_STARTED, "TRUE", Scope.WORKFLOW);
        String outJob = getCallbackContext().getTaskConfig().getConfigMap().get("OUT_" + port);
        if (outJob != null) {
            putUserContent(NEXT_JOB, outJob, Scope.WORKFLOW);
        }
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public String getApiServerUrl() {
        return apiServerUrl;
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

    public void initializeKafkaEventProducer() {
        Properties props = new Properties();

        props.put("bootstrap.servers", this.kafkaBootstrapUrl);

        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        eventProducer = new KafkaProducer<String, String>(props);
    }

    public void publishTaskStatus(long status, String reason) {
        eventProducer.send(new ProducerRecord<String, String>(
                this.eventTopic, String.join(",", this.processId + "", this.taskId + "", status + "", reason)));
    }

    public long getProcessId() {
        return processId;
    }

    public long getTaskId() {
        return taskId;
    }
}
