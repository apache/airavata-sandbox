package org.apache.airavata.services.registry.rest.resourcemappings;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ConfigurationList {
    private Object[] configValList = null;

    public Object[] getConfigValList() {
        return configValList;
    }

    public void setConfigValList(Object[] configValList) {
        this.configValList = configValList;
    }
}
