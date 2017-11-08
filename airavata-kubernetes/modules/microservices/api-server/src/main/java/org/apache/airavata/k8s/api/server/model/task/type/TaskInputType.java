package org.apache.airavata.k8s.api.server.model.task.type;

import javax.persistence.*;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
@Entity
public class TaskInputType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private String type;
    private String defaultValue;

    @ManyToOne
    private TaskModelType taskModelType;

    public long getId() {
        return id;
    }

    public TaskInputType setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TaskInputType setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public TaskInputType setType(String type) {
        this.type = type;
        return this;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public TaskInputType setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public TaskModelType getTaskModelType() {
        return taskModelType;
    }

    public TaskInputType setTaskModelType(TaskModelType taskModelType) {
        this.taskModelType = taskModelType;
        return this;
    }
}
