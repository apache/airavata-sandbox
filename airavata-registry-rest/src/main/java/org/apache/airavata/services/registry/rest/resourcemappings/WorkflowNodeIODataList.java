package org.apache.airavata.services.registry.rest.resourcemappings;



import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WorkflowNodeIODataList {
    WorkflowNodeIODataMapping[] workflowNodeIODataMappings = null;

    public WorkflowNodeIODataMapping[] getWorkflowNodeIODataMappings() {
        return workflowNodeIODataMappings;
    }

    public void setWorkflowNodeIODataMappings(WorkflowNodeIODataMapping[] workflowNodeIODataMappings) {
        this.workflowNodeIODataMappings = workflowNodeIODataMappings;
    }
}
