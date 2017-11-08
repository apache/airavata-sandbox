package org.apache.airavata.k8s.api.resources.task;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class TaskInputResource {

    private long id;
    private String name;
    private String value;
    private String type;
    private String importFrom;

    public long getId() {
        return id;
    }

    public TaskInputResource setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TaskInputResource setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public TaskInputResource setValue(String value) {
        this.value = value;
        return this;
    }

    public String getType() {
        return type;
    }

    public TaskInputResource setType(String type) {
        this.type = type;
        return this;
    }

    public String getImportFrom() {
        return importFrom;
    }

    public TaskInputResource setImportFrom(String importFrom) {
        this.importFrom = importFrom;
        return this;
    }
}
