package org.apache.airavata.services.registry.rest.resourcemappings;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ServiceDescriptionList {
    private ServiceDescriptor[] serviceDescriptions = null;

    public ServiceDescriptor[] getServiceDescriptions() {
        return serviceDescriptions;
    }

    public void setServiceDescriptions(ServiceDescriptor[] serviceDescriptions) {
        this.serviceDescriptions = serviceDescriptions;
    }
}
