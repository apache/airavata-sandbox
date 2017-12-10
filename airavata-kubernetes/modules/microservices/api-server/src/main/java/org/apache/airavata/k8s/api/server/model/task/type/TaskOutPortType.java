package org.apache.airavata.k8s.api.server.model.task.type;

import javax.persistence.*;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
@Entity
public class TaskOutPortType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @Column(name = "PORT_ORDER")
    private int order = 0;

    @ManyToOne
    private TaskModelType taskModelType;

    public long getId() {
        return id;
    }

    public TaskOutPortType setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TaskOutPortType setName(String name) {
        this.name = name;
        return this;
    }

    public int getOrder() {
        return order;
    }

    public TaskOutPortType setOrder(int order) {
        this.order = order;
        return this;
    }

    public TaskModelType getTaskModelType() {
        return taskModelType;
    }

    public TaskOutPortType setTaskModelType(TaskModelType taskModelType) {
        this.taskModelType = taskModelType;
        return this;
    }
}
