package org.apache.airavata.k8s.api.resources.task;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class TaskDagResource {
    private long id;
    private TaskOutPortResource sourceOutPort;
    private TaskResource targetTask;

    public long getId() {
        return id;
    }

    public TaskDagResource setId(long id) {
        this.id = id;
        return this;
    }

    public TaskOutPortResource getSourceOutPort() {
        return sourceOutPort;
    }

    public TaskDagResource setSourceOutPort(TaskOutPortResource sourceOutPort) {
        this.sourceOutPort = sourceOutPort;
        return this;
    }

    public TaskResource getTargetTask() {
        return targetTask;
    }

    public TaskDagResource setTargetTask(TaskResource targetTask) {
        this.targetTask = targetTask;
        return this;
    }
}
