package org.apache.airavata.task.async.command.monitor;

import org.apache.airavata.agents.core.AsyncCommandStatus;
import org.apache.airavata.helix.api.AbstractTask;
import org.apache.airavata.helix.api.PropertyResolver;
import org.apache.airavata.k8s.api.resources.task.TaskStatusResource;
import org.apache.airavata.k8s.api.resources.task.type.TaskInputTypeResource;
import org.apache.airavata.k8s.api.resources.task.type.TaskOutPortTypeResource;
import org.apache.airavata.k8s.api.resources.task.type.TaskTypeResource;
import org.apache.helix.task.TaskCallbackContext;
import org.apache.helix.task.TaskResult;

import java.util.Arrays;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class AsyncCommandMonitor extends AbstractTask {

    public static final String NAME = "ASYNC_COMMAND_MONITOR";

    private AsyncCommandStatus status;
    private String message;

    public AsyncCommandMonitor(TaskCallbackContext callbackContext, PropertyResolver propertyResolver) {
        super(callbackContext, propertyResolver);
    }

    @Override
    public void init() {
        super.init();
        this.status = AsyncCommandStatus.valueOf(getCallbackContext().getTaskConfig().getConfigMap().get("event"));
        this.message = getCallbackContext().getTaskConfig().getConfigMap().get("message");
    }

    @Override
    public TaskResult onRun() {
        System.out.println("Received status " + this.status.name() + " with message " + this.message);
        if (this.status != null) {
            sendToOutPort(this.status.name());
            publishTaskStatus(TaskStatusResource.State.COMPLETED, "Sending to out port " + this.status.name());
            return new TaskResult(TaskResult.Status.COMPLETED, "Sending to out port " + this.status.name());

        } else {
            publishTaskStatus(TaskStatusResource.State.FAILED, "Unsupported status");
            return new TaskResult(TaskResult.Status.FATAL_FAILED, "Unsupported status");
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
                                .setName(PARAMS.STATUS_KEY)
                                .setType("String")
                                .setDefaultValue("status"),
                        new TaskInputTypeResource()
                                .setName(PARAMS.MESSAGE_KEY)
                                .setType("String")
                                .setDefaultValue("message")
                ));

        taskTypeResource.getOutPorts().addAll(
                Arrays.asList(
                        new TaskOutPortTypeResource()
                                .setName(AsyncCommandStatus.PENDING.name())
                                .setOrder(0),
                        new TaskOutPortTypeResource()
                                .setName(AsyncCommandStatus.RUNNING.name())
                                .setOrder(1),
                        new TaskOutPortTypeResource()
                                .setName(AsyncCommandStatus.SUCCESS.name())
                                .setOrder(2),
                        new TaskOutPortTypeResource()
                                .setName(AsyncCommandStatus.CANCEL.name())
                                .setOrder(3),
                        new TaskOutPortTypeResource()
                                .setName(AsyncCommandStatus.ERROR.name())
                                .setOrder(4),
                        new TaskOutPortTypeResource()
                                .setName(AsyncCommandStatus.PAUSE.name())
                                .setOrder(5),
                        new TaskOutPortTypeResource()
                                .setName(AsyncCommandStatus.WARNING.name())
                                .setOrder(6),
                        new TaskOutPortTypeResource()
                                .setName(AsyncCommandStatus.MANUAL_RECOVERY.name())
                                .setOrder(7)
                )

        );

        return taskTypeResource;
    }

    public static final class PARAMS {
        public static final String STATUS_KEY = "status-key";
        public static final String MESSAGE_KEY = "message-key";
    }
}
