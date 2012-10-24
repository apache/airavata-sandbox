package org.apache.airavata.services.registry.rest.resourcemappings;


import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WorkspaceProjectList {
    private WorkspaceProjectMapping[] workspaceProjectMappings = null;

    public WorkspaceProjectList() {
    }

    public WorkspaceProjectMapping[] getWorkspaceProjectMappings() {
        return workspaceProjectMappings;
    }

    public void setWorkspaceProjectMappings(WorkspaceProjectMapping[] workspaceProjectMappings) {
        this.workspaceProjectMappings = workspaceProjectMappings;
    }
}
