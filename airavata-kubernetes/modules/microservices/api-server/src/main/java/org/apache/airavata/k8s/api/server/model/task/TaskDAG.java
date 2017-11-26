package org.apache.airavata.k8s.api.server.model.task;

import javax.persistence.*;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
@Entity
public class TaskDAG {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private TaskOutPort sourceOutPort;

    @ManyToOne
    private TaskModel targetTask;

    public long getId() {
        return id;
    }

    public TaskDAG setId(long id) {
        this.id = id;
        return this;
    }

    public TaskOutPort getSourceOutPort() {
        return sourceOutPort;
    }

    public TaskDAG setSourceOutPort(TaskOutPort sourceOutPort) {
        this.sourceOutPort = sourceOutPort;
        return this;
    }

    public TaskModel getTargetTask() {
        return targetTask;
    }

    public TaskDAG setTargetTask(TaskModel targetTask) {
        this.targetTask = targetTask;
        return this;
    }
}
