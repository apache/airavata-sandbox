package org.apache.airavata.services.registry.rest.resourcemappings;

import org.apache.airavata.commons.gfac.type.ServiceDescription;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ServiceDescriptionList {
    private ServiceDescription[] serviceDescriptions = null;

    public ServiceDescription[] getServiceDescriptions() {
        return serviceDescriptions;
    }

    public void setServiceDescriptions(ServiceDescription[] serviceDescriptions) {
        this.serviceDescriptions = serviceDescriptions;
    }
}
