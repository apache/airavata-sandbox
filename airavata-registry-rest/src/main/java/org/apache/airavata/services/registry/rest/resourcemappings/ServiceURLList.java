package org.apache.airavata.services.registry.rest.resourcemappings;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ServiceURLList {
    private String[] uris = null;

    public String[] getUris() {
        return uris;
    }

    public void setUris(String[] uris) {
        this.uris = uris;
    }


}
