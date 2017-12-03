package org.apache.airavata.helix.task.command;

import org.apache.airavata.helix.api.AbstractTask;
import org.apache.airavata.helix.api.PropertyResolver;
import org.apache.airavata.k8s.api.resources.compute.ComputeResource;
import org.apache.airavata.k8s.api.resources.task.TaskStatusResource;
import org.apache.airavata.k8s.api.resources.task.type.TaskInputTypeResource;
import org.apache.airavata.k8s.api.resources.task.type.TaskOutPortTypeResource;
import org.apache.airavata.k8s.api.resources.task.type.TaskTypeResource;
import org.apache.airavata.k8s.compute.api.ExecutionResult;
import org.apache.helix.task.TaskCallbackContext;
import org.apache.helix.task.TaskResult;

import java.util.Arrays;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class CommandTask extends AbstractTask {

    public static final String NAME = "COMMAND";

    private String command;
    private String arguments;
    private String stdOutPath;
    private String stdErrPath;
    private String computeResourceId;
    private ComputeResource computeResource;

    public CommandTask(TaskCallbackContext callbackContext, PropertyResolver propertyResolver) {
        super(callbackContext, propertyResolver);
    }

    @Override
    public void init() {
        this.command = getCallbackContext().getTaskConfig().getConfigMap().get(PARAMS.COMMAND);
        this.arguments = getCallbackContext().getTaskConfig().getConfigMap().get(PARAMS.ARGUMENTS);
        this.stdOutPath = getCallbackContext().getTaskConfig().getConfigMap().get(PARAMS.STD_OUT_PATH);
        this.stdErrPath = getCallbackContext().getTaskConfig().getConfigMap().get(PARAMS.STD_ERR_PATH);
        this.computeResourceId = getCallbackContext().getTaskConfig().getConfigMap().get(PARAMS.COMPUTE_RESOURCE);
        this.computeResource = this.getRestTemplate().getForObject("http://" + this.getApiServerUrl()
                + "/compute/" + Long.parseLong(this.computeResourceId), ComputeResource.class);
    }

    public TaskResult onRun() {
        System.out.println("Executing command " + command);
        try {

            String stdOutSuffix = " > " + stdOutPath + " 2> " + stdErrPath;

            publishTaskStatus(TaskStatusResource.State.EXECUTING, "");

            String finalCommand = command + (arguments != null ? arguments : "") + stdOutSuffix;

            System.out.println("Executing command " + finalCommand);
            Thread.sleep(2000);
            ExecutionResult executionResult = fetchComputeResourceOperation(computeResource).executeCommand(finalCommand);

            if (executionResult.getExitStatus() == 0) {
                publishTaskStatus(TaskStatusResource.State.COMPLETED, "Task completed");
                sendToOutPort("Out");
                return new TaskResult(TaskResult.Status.COMPLETED, "Task completed");

            } else if (executionResult.getExitStatus() == -1) {
                publishTaskStatus(TaskStatusResource.State.FAILED, "Process didn't exit successfully");
                sendToOutPort("Error");
                return new TaskResult(TaskResult.Status.FATAL_FAILED, "Task failed");

            } else {
                publishTaskStatus(TaskStatusResource.State.FAILED, "Process exited with error status " + executionResult.getExitStatus());
                sendToOutPort("Error");
                return new TaskResult(TaskResult.Status.FATAL_FAILED, "Task failed");
            }

        } catch (Exception e) {

            e.printStackTrace();
            publishTaskStatus(TaskStatusResource.State.FAILED, e.getMessage());
            return new TaskResult(TaskResult.Status.FATAL_FAILED, "Task failed");
        }
    }

    public void onCancel() {

    }

    public static TaskTypeResource getTaskType() {
        TaskTypeResource taskTypeResource = new TaskTypeResource();
        taskTypeResource.setName(NAME);
        taskTypeResource.setTopicName("airavata-command");
        taskTypeResource.setIcon("assets/icons/ssh.png");
        taskTypeResource.getInputTypes().addAll(
                Arrays.asList(
                        new TaskInputTypeResource()
                                .setName(PARAMS.COMMAND)
                                .setType("String")
                                .setDefaultValue(""),
                        new TaskInputTypeResource()
                                .setName(PARAMS.ARGUMENTS)
                                .setType("String")
                                .setDefaultValue(""),
                        new TaskInputTypeResource()
                                .setName(PARAMS.COMPUTE_RESOURCE)
                                .setType("Long")
                                .setDefaultValue(""),
                        new TaskInputTypeResource()
                                .setName(PARAMS.STD_OUT_PATH)
                                .setType("String")
                                .setDefaultValue(""),
                        new TaskInputTypeResource()
                                .setName(PARAMS.STD_ERR_PATH)
                                .setType("String")
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
        public static final String COMMAND = "command";
        public static final String ARGUMENTS = "arguments";
        public static final String STD_OUT_PATH = "std_out_path";
        public static final String STD_ERR_PATH = "std_err_path";
        public static final String COMPUTE_RESOURCE = "compute_resource";
    }
}
