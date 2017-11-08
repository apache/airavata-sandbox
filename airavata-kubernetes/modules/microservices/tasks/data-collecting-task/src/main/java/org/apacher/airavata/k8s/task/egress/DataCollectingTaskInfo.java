package org.apacher.airavata.k8s.task.egress;

import org.apache.airavata.k8s.api.resources.task.type.TaskInputTypeResource;
import org.apache.airavata.k8s.api.resources.task.type.TaskOutPortTypeResource;
import org.apache.airavata.k8s.api.resources.task.type.TaskTypeResource;

import java.util.Arrays;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class DataCollectingTaskInfo {
    public static final String REMOTE_SOURCE_PATH = "remote_source_path";
    public static final String COMPUTE_RESOURCE = "compute_resource";
    public static final String IDENTIFIER = "identifier";

    public static TaskTypeResource getTaskType() {
        TaskTypeResource taskTypeResource = new TaskTypeResource();
        taskTypeResource.setName("Data Collect");
        taskTypeResource.setTopicName("airavata-data-collect");
        taskTypeResource.setIcon("assets/icons/copy.png");
        taskTypeResource.getInputTypes().addAll(
                Arrays.asList(
                        new TaskInputTypeResource()
                                .setName(REMOTE_SOURCE_PATH)
                                .setType("String")
                                .setDefaultValue(""),
                        new TaskInputTypeResource()
                                .setName(IDENTIFIER)
                                .setType("String"),
                        new TaskInputTypeResource()
                                .setName(COMPUTE_RESOURCE)
                                .setType("Long")));

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
