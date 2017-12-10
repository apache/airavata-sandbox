package org.apache.airavata.helix.task.dataout;

import org.apache.airavata.helix.api.AbstractTask;
import org.apache.airavata.helix.api.PropertyResolver;
import org.apache.airavata.k8s.api.resources.compute.ComputeResource;
import org.apache.airavata.k8s.api.resources.task.TaskStatusResource;
import org.apache.airavata.k8s.api.resources.task.type.TaskInputTypeResource;
import org.apache.airavata.k8s.api.resources.task.type.TaskOutPortTypeResource;
import org.apache.airavata.k8s.api.resources.task.type.TaskTypeResource;
import org.apache.helix.task.TaskCallbackContext;
import org.apache.helix.task.TaskResult;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Arrays;
import java.util.UUID;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class DataOutputTask extends AbstractTask {

    public static final String NAME = "DATA_OUTPUT";

    private String sourcePath;
    private String identifier;
    private String computeResourceId;
    private ComputeResource computeResource;

    public DataOutputTask(TaskCallbackContext callbackContext, PropertyResolver propertyResolver) {
        super(callbackContext, propertyResolver);
    }

    @Override
    public void init() {
        this.sourcePath = getCallbackContext().getTaskConfig().getConfigMap().get(PARAMS.SOURCE_PATH);
        this.identifier = getCallbackContext().getTaskConfig().getConfigMap().get(PARAMS.IDENTIFIER);
        this.computeResourceId = getCallbackContext().getTaskConfig().getConfigMap().get(PARAMS.COMPUTE_RESOURCE);
        this.computeResource = this.getRestTemplate().getForObject("http://" + this.getApiServerUrl()
                + "/compute/" + Long.parseLong(this.computeResourceId), ComputeResource.class);
    }

    @Override
    public TaskResult onRun() {
        try {
            publishTaskStatus(TaskStatusResource.State.EXECUTING, "");

            String temporaryFile = "/tmp/" + UUID.randomUUID().toString();
            System.out.println("Downloading " + sourcePath + " to " + temporaryFile + " from compute resource "
                    + computeResource.getName());

            fetchComputeResourceOperation(computeResource).transferDataOut(sourcePath, temporaryFile, "SCP");

            LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            map.add("file", new FileSystemResource(temporaryFile));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);

            System.out.println("Uploading data file with task id " + getTaskId() + " and identifier "
                    + identifier + " to data store");

            getRestTemplate().exchange("http://" + getApiServerUrl() + "/data/" + getTaskId() + "/"
                    + identifier + "/upload", HttpMethod.POST, requestEntity, Long.class);

            publishTaskStatus(TaskStatusResource.State.COMPLETED, "Task completed");
            sendToOutPort("Out");
            return new TaskResult(TaskResult.Status.COMPLETED, "Task completed");

        } catch (Exception e) {
            e.printStackTrace();
            publishTaskStatus(TaskStatusResource.State.FAILED, e.getMessage());
            sendToOutPort("Error");
            return new TaskResult(TaskResult.Status.FATAL_FAILED, "Task completed");

        }
    }

    @Override
    public void onCancel() {

    }

    public static final class PARAMS {
        public static final String SOURCE_PATH = "source_path";
        public static final String COMPUTE_RESOURCE = "compute_resource";
        public static final String IDENTIFIER = "identifier";
    }

    public static TaskTypeResource getTaskType() {
        TaskTypeResource taskTypeResource = new TaskTypeResource();
        taskTypeResource.setName(NAME);
        taskTypeResource.setTopicName("airavata-data-collect");
        taskTypeResource.setIcon("assets/icons/dataout.png");
        taskTypeResource.getInputTypes().addAll(
                Arrays.asList(
                        new TaskInputTypeResource()
                                .setName(PARAMS.SOURCE_PATH)
                                .setType("String")
                                .setDefaultValue(""),
                        new TaskInputTypeResource()
                                .setName(PARAMS.IDENTIFIER)
                                .setType("String")
                                .setDefaultValue(""),
                        new TaskInputTypeResource()
                                .setName(PARAMS.COMPUTE_RESOURCE)
                                .setType("Long")
                                .setDefaultValue("")));

        taskTypeResource.getOutPorts().addAll(
                Arrays.asList(
                        new TaskOutPortTypeResource()
                                .setName("Out")
                                .setOrder(0),
                        new TaskOutPortTypeResource()
                                .setName("Error")
                                .setOrder(1))
        );

        return taskTypeResource;

    }
}