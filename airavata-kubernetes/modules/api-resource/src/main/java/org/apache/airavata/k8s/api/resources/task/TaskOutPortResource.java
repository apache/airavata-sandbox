package org.apache.airavata.k8s.api.resources.task;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class TaskOutPortResource {
    private long id;
    private String name;
    private int referenceId = 0;
    private TaskResource taskResource;

    public long getId() {
        return id;
    }

    public TaskOutPortResource setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TaskOutPortResource setName(String name) {
        this.name = name;
        return this;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public TaskOutPortResource setReferenceId(int referenceId) {
        this.referenceId = referenceId;
        return this;
    }

    public TaskResource getTaskResource() {
        return taskResource;
    }

    public TaskOutPortResource setTaskResource(TaskResource taskResource) {
        this.taskResource = taskResource;
        return this;
    }
}
