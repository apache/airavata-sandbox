package org.apache.airavata.k8s.api.resources.task.type;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class TaskTypeResource {

    private long id;
    private String name;
    private String topicName;
    private String icon;
    private List<TaskInputTypeResource> inputTypes = new ArrayList<>();
    private List<TaskOutputTypeResource> outputTypes = new ArrayList<>();
    private List<TaskOutPortTypeResource> outPorts = new ArrayList<>();

    public long getId() {
        return id;
    }

    public TaskTypeResource setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TaskTypeResource setName(String name) {
        this.name = name;
        return this;
    }

    public String getTopicName() {
        return topicName;
    }

    public TaskTypeResource setTopicName(String topicName) {
        this.topicName = topicName;
        return this;
    }

    public List<TaskInputTypeResource> getInputTypes() {
        return inputTypes;
    }

    public TaskTypeResource setInputTypes(List<TaskInputTypeResource> inputTypes) {
        this.inputTypes = inputTypes;
        return this;
    }

    public List<TaskOutputTypeResource> getOutputTypes() {
        return outputTypes;
    }

    public TaskTypeResource setOutputTypes(List<TaskOutputTypeResource> outputTypes) {
        this.outputTypes = outputTypes;
        return this;
    }

    public List<TaskOutPortTypeResource> getOutPorts() {
        return outPorts;
    }

    public TaskTypeResource setOutPorts(List<TaskOutPortTypeResource> outPorts) {
        this.outPorts = outPorts;
        return this;
    }

    public String getIcon() {
        return icon;
    }

    public TaskTypeResource setIcon(String icon) {
        this.icon = icon;
        return this;
    }
}
