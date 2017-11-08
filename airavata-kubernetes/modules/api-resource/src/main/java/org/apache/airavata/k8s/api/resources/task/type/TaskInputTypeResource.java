package org.apache.airavata.k8s.api.resources.task.type;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class TaskInputTypeResource {

    private long id;
    private String name;
    private String type;
    private String defaultValue;

    public long getId() {
        return id;
    }

    public TaskInputTypeResource setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TaskInputTypeResource setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public TaskInputTypeResource setType(String type) {
        this.type = type;
        return this;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public TaskInputTypeResource setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }
}
