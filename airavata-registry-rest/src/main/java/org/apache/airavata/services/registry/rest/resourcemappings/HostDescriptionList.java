package org.apache.airavata.services.registry.rest.resourcemappings;

import org.apache.airavata.commons.gfac.type.HostDescription;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class HostDescriptionList {
    private HostDescription [] hostDescriptions = null;

    public HostDescription[] getHostDescriptions() {
        return hostDescriptions;
    }

    public void setHostDescriptions(HostDescription[] hostDescriptions) {
        this.hostDescriptions = hostDescriptions;
    }




}
