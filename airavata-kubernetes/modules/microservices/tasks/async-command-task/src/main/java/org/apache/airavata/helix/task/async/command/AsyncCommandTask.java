package org.apache.airavata.helix.task.async.command;

import org.apache.airavata.agents.core.AsyncOperation;
import org.apache.airavata.helix.api.AbstractTask;
import org.apache.airavata.helix.api.PropertyResolver;
import org.apache.airavata.k8s.api.resources.compute.ComputeResource;
import org.apache.airavata.k8s.api.resources.task.TaskStatusResource;
import org.apache.airavata.k8s.api.resources.task.type.TaskInputTypeResource;
import org.apache.airavata.k8s.api.resources.task.type.TaskOutPortTypeResource;
import org.apache.airavata.k8s.api.resources.task.type.TaskTypeResource;
import org.apache.helix.task.TaskCallbackContext;
import org.apache.helix.task.TaskResult;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class AsyncCommandTask extends AbstractTask {

    public static final String NAME = "ASYNC_COMMAND_TASK";

    private String command;
    private String arguments;
    private String stdOutPath;
    private String stdErrPath;
    private String computeResourceId;
    private ComputeResource computeResource;
    private Long callBackWorkflowId;

    public AsyncCommandTask(TaskCallbackContext callbackContext, PropertyResolver propertyResolver) {
        super(callbackContext, propertyResolver);
    }

    @Override
    public void init() {
        this.command = getCallbackContext().getTaskConfig().getConfigMap().get(PARAMS.COMMAND);
        this.arguments = getCallbackContext().getTaskConfig().getConfigMap().get(PARAMS.ARGUMENTS);
        this.stdOutPath = getCallbackContext().getTaskConfig().getConfigMap().get(PARAMS.STD_OUT_PATH);
        this.stdErrPath = getCallbackContext().getTaskConfig().getConfigMap().get(PARAMS.STD_ERR_PATH);
        this.computeResourceId = getCallbackContext().getTaskConfig().getConfigMap().get(PARAMS.COMPUTE_RESOURCE);
        this.callBackWorkflowId = Long.parseLong(getCallbackContext().getTaskConfig().getConfigMap().get(PARAMS.CALLBACK_WORKFLOW));
        this.computeResource = this.getRestTemplate().getForObject("http://" + this.getApiServerUrl()
                + "/compute/" + Long.parseLong(this.computeResourceId), ComputeResource.class);

    }

    @Override
    public TaskResult onRun() {
        try {
            AsyncOperation operation = (AsyncOperation) Class.forName("org.apache.airavata.agents.thrift.operation.ThriftAgentOperation")
                    .getConstructor(ComputeResource.class).newInstance(this.computeResource);
            operation.executeCommandAsync(this.command, this.callBackWorkflowId);
            publishTaskStatus(TaskStatusResource.State.COMPLETED, "Task completed");
            return new TaskResult(TaskResult.Status.COMPLETED, "Task completed");
        } catch (InstantiationException | IllegalAccessException |
                InvocationTargetException | ClassNotFoundException | NoSuchMethodException | ClassCastException e) {
            e.printStackTrace();
            publishTaskStatus(TaskStatusResource.State.FAILED, "Failed to load async operation");
            return new TaskResult(TaskResult.Status.FATAL_FAILED, "Task failed");
        }
    }

    @Override
    public void onCancel() {

    }

    public static TaskTypeResource getTaskType() {
        TaskTypeResource taskTypeResource = new TaskTypeResource();
        taskTypeResource.setName(NAME);
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
                                .setName(PARAMS.CALLBACK_WORKFLOW)
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
        public static final String CALLBACK_WORKFLOW = "callback_workflow";
    }
}
