package org.apache.airavata.k8s.task.job;

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
public class CommandTaskInfo {

    public static final String COMMAND = "command";
    public static final String ARGUMENTS = "arguments";
    public static final String STD_OUT_PATH = "std_out_path";
    public static final String STD_ERR_PATH = "std_err_path";
    public static final String COMPUTE_RESOURCE = "compute_resource";

    public static TaskTypeResource getTaskType() {
        TaskTypeResource taskTypeResource = new TaskTypeResource();
        taskTypeResource.setName("Command Execute");
        taskTypeResource.setTopicName("airavata-command");
        taskTypeResource.setIcon("assets/icons/ssh.png");
        taskTypeResource.getInputTypes().addAll(
                Arrays.asList(
                        new TaskInputTypeResource()
                                .setName(COMMAND)
                                .setType("String")
                                .setDefaultValue(""),
                        new TaskInputTypeResource()
                                .setName(ARGUMENTS)
                                .setType("String"),
                        new TaskInputTypeResource()
                                .setName(COMPUTE_RESOURCE)
                                .setType("Long"),
                        new TaskInputTypeResource()
                                .setName(STD_OUT_PATH)
                                .setType("String"),
                        new TaskInputTypeResource()
                                .setName(STD_ERR_PATH)
                                .setType("String")));

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
