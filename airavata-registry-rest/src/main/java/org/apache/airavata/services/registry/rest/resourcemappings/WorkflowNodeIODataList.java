package org.apache.airavata.services.registry.rest.resourcemappings;


import org.apache.airavata.registry.api.workflow.WorkflowNodeIOData;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WorkflowNodeIODataList {
    WorkflowNodeIOData[] workflowNodeIODatas = null;

    public WorkflowNodeIOData[] getWorkflowNodeIODatas() {
        return workflowNodeIODatas;
    }

    public void setWorkflowNodeIODatas(WorkflowNodeIOData[] workflowNodeIODatas) {
        this.workflowNodeIODatas = workflowNodeIODatas;
    }
}
