package org.apache.airavata.k8s.api.server.model.task.type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
@Entity
public class TaskModelType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private String topicName;
    private String icon;

    @OneToMany(mappedBy = "taskModelType", cascade = CascadeType.ALL)
    private List<TaskInputType> inputTypes = new ArrayList<>();

    @OneToMany(mappedBy = "taskModelType", cascade = CascadeType.ALL)
    private List<TaskOutputType> outputTypes = new ArrayList<>();

    @OneToMany(mappedBy = "taskModelType", cascade = CascadeType.ALL)
    private List<TaskOutPortType> outPorts = new ArrayList<>();

    public long getId() {
        return id;
    }

    public TaskModelType setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TaskModelType setName(String name) {
        this.name = name;
        return this;
    }

    public String getTopicName() {
        return topicName;
    }

    public TaskModelType setTopicName(String topicName) {
        this.topicName = topicName;
        return this;
    }

    public List<TaskInputType> getInputTypes() {
        return inputTypes;
    }

    public TaskModelType setInputTypes(List<TaskInputType> inputTypes) {
        this.inputTypes = inputTypes;
        return this;
    }

    public List<TaskOutputType> getOutputTypes() {
        return outputTypes;
    }

    public TaskModelType setOutputTypes(List<TaskOutputType> outputTypes) {
        this.outputTypes = outputTypes;
        return this;
    }

    public List<TaskOutPortType> getOutPorts() {
        return outPorts;
    }

    public TaskModelType setOutPorts(List<TaskOutPortType> outPorts) {
        this.outPorts = outPorts;
        return this;
    }

    public String getIcon() {
        return icon;
    }

    public TaskModelType setIcon(String icon) {
        this.icon = icon;
        return this;
    }
}
