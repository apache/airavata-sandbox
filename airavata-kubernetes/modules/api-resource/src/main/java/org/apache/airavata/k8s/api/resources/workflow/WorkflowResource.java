package org.apache.airavata.k8s.api.resources.workflow;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class WorkflowResource {

    private long id;
    private String name;
    private String workflowGraphXML;
    private List<Long> processIds = new ArrayList<>();

    public long getId() {
        return id;
    }

    public WorkflowResource setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public WorkflowResource setName(String name) {
        this.name = name;
        return this;
    }

    public String getWorkflowGraphXML() {
        return workflowGraphXML;
    }

    public WorkflowResource setWorkflowGraphXML(String workflowGraphXML) {
        this.workflowGraphXML = workflowGraphXML;
        return this;
    }

    public List<Long> getProcessIds() {
        return processIds;
    }

    public WorkflowResource setProcessIds(List<Long> processIds) {
        this.processIds = processIds;
        return this;
    }
}
