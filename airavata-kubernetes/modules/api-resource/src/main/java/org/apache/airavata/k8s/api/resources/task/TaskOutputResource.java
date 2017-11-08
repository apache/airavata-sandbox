package org.apache.airavata.k8s.api.resources.task;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class TaskOutputResource {

    private long id;
    private String name;
    private String value;
    private String type;
    private String exportTo;

    public long getId() {
        return id;
    }

    public TaskOutputResource setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TaskOutputResource setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public TaskOutputResource setValue(String value) {
        this.value = value;
        return this;
    }

    public String getType() {
        return type;
    }

    public TaskOutputResource setType(String type) {
        this.type = type;
        return this;
    }

    public String getExportTo() {
        return exportTo;
    }

    public TaskOutputResource setExportTo(String exportTo) {
        this.exportTo = exportTo;
        return this;
    }
}
