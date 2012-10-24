package org.apache.airavata.services.registry.rest.resourcemappings;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WorkflowInstancesList {
    WorkflowInstanceMapping[] workflowInstanceMappings = null;

    public WorkflowInstanceMapping[] getWorkflowInstanceMappings() {
        return workflowInstanceMappings;
    }

    public void setWorkflowInstanceMappings(WorkflowInstanceMapping[] workflowInstanceMappings) {
        this.workflowInstanceMappings = workflowInstanceMappings;
    }
}
