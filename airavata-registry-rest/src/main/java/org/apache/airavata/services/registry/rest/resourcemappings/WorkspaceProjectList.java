package org.apache.airavata.services.registry.rest.resourcemappings;

import org.apache.airavata.registry.api.WorkspaceProject;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WorkspaceProjectList {
    WorkspaceProject[] workspaceProjects = null;

    public WorkspaceProject[] getWorkspaceProjects() {
        return workspaceProjects;
    }

    public void setWorkspaceProjects(WorkspaceProject[] workspaceProjects) {
        this.workspaceProjects = workspaceProjects;
    }
}
