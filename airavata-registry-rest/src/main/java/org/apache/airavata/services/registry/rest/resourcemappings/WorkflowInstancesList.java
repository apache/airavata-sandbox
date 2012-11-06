package org.apache.airavata.services.registry.rest.resourcemappings;

import org.apache.airavata.registry.api.workflow.WorkflowInstance;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WorkflowInstancesList {
    WorkflowInstance[] workflowInstances = null;

    public WorkflowInstance[] getWorkflowInstances() {
        return workflowInstances;
    }

    public void setWorkflowInstances(WorkflowInstance[] workflowInstances) {
        this.workflowInstances = workflowInstances;
    }
}
