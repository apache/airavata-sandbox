package org.apache.airavata.k8s.api.server.model.task.type;

import javax.persistence.*;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
@Entity
public class TaskOutputType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private String type;

    @ManyToOne
    private TaskModelType taskModelType;

    public long getId() {
        return id;
    }

    public TaskOutputType setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TaskOutputType setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public TaskOutputType setType(String type) {
        this.type = type;
        return this;
    }

    public TaskModelType getTaskModelType() {
        return taskModelType;
    }

    public TaskOutputType setTaskModelType(TaskModelType taskModelType) {
        this.taskModelType = taskModelType;
        return this;
    }
}
