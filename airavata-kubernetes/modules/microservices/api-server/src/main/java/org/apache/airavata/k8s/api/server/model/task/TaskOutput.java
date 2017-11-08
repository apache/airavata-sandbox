package org.apache.airavata.k8s.api.server.model.task;

import javax.persistence.*;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
@Entity
public class TaskOutput {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "TASK_OUTPUT_NAME")
    private String name;

    @Column(name = "TASK_OUTPUT_VALUE")
    private String value;

    @Column(name = "TASK_OUTPUT_TYPE")
    private String type;

    @Column(name = "EXPORT_TO")
    private String exportTo;

    @ManyToOne
    private TaskModel taskModel;

    public long getId() {
        return id;
    }

    public TaskOutput setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TaskOutput setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public TaskOutput setValue(String value) {
        this.value = value;
        return this;
    }

    public String getType() {
        return type;
    }

    public TaskOutput setType(String type) {
        this.type = type;
        return this;
    }

    public String getExportTo() {
        return exportTo;
    }

    public TaskOutput setExportTo(String exportTo) {
        this.exportTo = exportTo;
        return this;
    }

    public TaskModel getTaskModel() {
        return taskModel;
    }

    public TaskOutput setTaskModel(TaskModel taskModel) {
        this.taskModel = taskModel;
        return this;
    }
}
