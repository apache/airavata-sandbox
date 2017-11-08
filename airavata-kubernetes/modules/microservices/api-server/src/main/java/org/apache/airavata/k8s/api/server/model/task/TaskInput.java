package org.apache.airavata.k8s.api.server.model.task;

import javax.persistence.*;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
@Entity
public class TaskInput {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "TASK_INPUT_NAME")
    private String name;

    @Column(name = "TASK_INPUT_VALUE")
    private String value;

    @Column(name = "TASK_INPUT_TYPE")
    private String type;

    @Column(name = "IMPORT_FORM")
    private String importFrom;

    @ManyToOne
    private TaskModel taskModel;

    public long getId() {
        return id;
    }

    public TaskInput setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TaskInput setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public TaskInput setValue(String value) {
        this.value = value;
        return this;
    }

    public String getType() {
        return type;
    }

    public TaskInput setType(String type) {
        this.type = type;
        return this;
    }

    public TaskModel getTaskModel() {
        return taskModel;
    }

    public TaskInput setTaskModel(TaskModel taskModel) {
        this.taskModel = taskModel;
        return this;
    }

    public String getImportFrom() {
        return importFrom;
    }

    public TaskInput setImportFrom(String importFrom) {
        this.importFrom = importFrom;
        return this;
    }
}
