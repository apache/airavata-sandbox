package org.apache.airavata.helix.task.datain;

import org.apache.airavata.helix.api.AbstractTask;
import org.apache.airavata.helix.api.PropertyResolver;
import org.apache.airavata.k8s.api.resources.compute.ComputeResource;
import org.apache.airavata.k8s.api.resources.task.TaskStatusResource;
import org.apache.airavata.k8s.api.resources.task.type.TaskInputTypeResource;
import org.apache.airavata.k8s.api.resources.task.type.TaskOutPortTypeResource;
import org.apache.airavata.k8s.api.resources.task.type.TaskTypeResource;
import org.apache.commons.io.FileUtils;
import org.apache.helix.task.TaskCallbackContext;
import org.apache.helix.task.TaskResult;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.UUID;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class DataInputTask extends AbstractTask {

    public static final String NAME = "DATA_INPUT";

    private String remoteSourcePath;
    private String targetPath;
    private String computeResourceId;
    private ComputeResource computeResource;

    public DataInputTask(TaskCallbackContext callbackContext, PropertyResolver propertyResolver) {
        super(callbackContext, propertyResolver);
    }

    @Override
    public void init() {
        this.remoteSourcePath = getCallbackContext().getTaskConfig().getConfigMap().get(PARAMS.REMOTE_SOURCE_PATH);
        this.targetPath = getCallbackContext().getTaskConfig().getConfigMap().get(PARAMS.TARGET_PATH);
        this.computeResourceId = getCallbackContext().getTaskConfig().getConfigMap().get(PARAMS.COMPUTE_RESOURCE);
        this.computeResource = this.getRestTemplate().getForObject("http://" + this.getApiServerUrl()
                + "/compute/" + Long.parseLong(this.computeResourceId), ComputeResource.class);
    }

    @Override
    public TaskResult onRun() {
        try {
            String tempFilePath = "/tmp/" + UUID.randomUUID().toString();
            System.out.println("Creating tmp file " + tempFilePath);
            publishTaskStatus(TaskStatusResource.State.EXECUTING, "");

            if (remoteSourcePath.startsWith("http")) {
                System.out.println("Downloading text file " + remoteSourcePath);
                FileUtils.copyURLToFile(new URL(remoteSourcePath), new File(tempFilePath));

            } else {
                publishTaskStatus(TaskStatusResource.State.FAILED, "Unsupported source type");
                sendToOutPort("Error");
                return new TaskResult(TaskResult.Status.FATAL_FAILED, "Task failed");

            }

            System.out.println("Transferring file to remote resource");
            fetchComputeResourceOperation(computeResource).transferDataIn(tempFilePath, this.targetPath, "SCP");

            publishTaskStatus(TaskStatusResource.State.COMPLETED, "Task completed");
            sendToOutPort("Out");
            return new TaskResult(TaskResult.Status.COMPLETED, "Task completed");

        } catch (Exception e) {

            e.printStackTrace();
            publishTaskStatus(TaskStatusResource.State.FAILED, e.getMessage());
            sendToOutPort("Error");
            return new TaskResult(TaskResult.Status.FATAL_FAILED, "Task failed");
        }
    }

    @Override
    public void onCancel() {

    }

    public static TaskTypeResource getTaskType() {
        TaskTypeResource taskTypeResource = new TaskTypeResource();
        taskTypeResource.setName(NAME);
        taskTypeResource.setTopicName("airavata-data-collect");
        taskTypeResource.setIcon("assets/icons/datain.png");
        taskTypeResource.getInputTypes().addAll(
                Arrays.asList(
                        new TaskInputTypeResource()
                                .setName(PARAMS.REMOTE_SOURCE_PATH)
                                .setType("String")
                                .setDefaultValue(""),
                        new TaskInputTypeResource()
                                .setName(PARAMS.TARGET_PATH)
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

    public static final class PARAMS {
        public static final String REMOTE_SOURCE_PATH = "remote_source_path";
        public static final String TARGET_PATH = "target_path";
        public static final String COMPUTE_RESOURCE = "compute_resource";
    }
}
