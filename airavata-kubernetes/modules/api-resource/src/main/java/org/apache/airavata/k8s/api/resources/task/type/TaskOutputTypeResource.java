package org.apache.airavata.k8s.api.resources.task.type;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class TaskOutputTypeResource {

    private long id;
    private String name;
    private String type;

    public long getId() {
        return id;
    }

    public TaskOutputTypeResource setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TaskOutputTypeResource setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public TaskOutputTypeResource setType(String type) {
        this.type = type;
        return this;
    }
}
