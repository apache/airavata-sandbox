package org.apache.airavata.services.registry.rest.resourcemappings;

import org.apache.airavata.commons.gfac.type.ApplicationDeploymentDescription;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;

@XmlRootElement
public class ApplicationDescriptorList {
    private Map<String, ApplicationDeploymentDescription> map = new HashMap<String, ApplicationDeploymentDescription>();
    private Map<String[], ApplicationDeploymentDescription> applicationDeploymentDescriptionMap = new HashMap<String[], ApplicationDeploymentDescription>();

    public Map<String[], ApplicationDeploymentDescription> getApplicationDeploymentDescriptionMap() {
        return applicationDeploymentDescriptionMap;
    }

    public void setApplicationDeploymentDescriptionMap(Map<String[], ApplicationDeploymentDescription> applicationDeploymentDescriptionMap) {
        this.applicationDeploymentDescriptionMap = applicationDeploymentDescriptionMap;
    }

    public Map<String, ApplicationDeploymentDescription> getMap() {
        return map;
    }

    public void setMap(Map<String, ApplicationDeploymentDescription> map) {
        this.map = map;
    }
}

