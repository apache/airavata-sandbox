package org.sample.airavata.api;

import org.apache.airavata.schemas.gfac.GlobusHostType;
import org.apache.xmlbeans.SchemaType;

public class HostDescriptorBean {
    private final SchemaType hostType;
    private final String hostName;
    private final String hostAddress;
    private final String hostEndpoint;
    private final String gateKeeperEndpoint;
    private final boolean isGramEndpoint = false;

    public HostDescriptorBean(String hostType, String hostName, String hostAddress,
                              String hostEndpoint, String gateKeeperEndpoint) {
        if ("globus".equalsIgnoreCase(hostType)) {
            this.hostType = GlobusHostType.type;
        } else {
            this.hostType = null;
        }
        this.hostName = hostName;
        this.hostAddress = hostAddress;
        this.hostEndpoint = hostEndpoint;
        this.gateKeeperEndpoint = gateKeeperEndpoint;
    }

    public String getGateKeeperEndpoint() {
        return gateKeeperEndpoint;
    }

    public String getHostEndpoint() {
        return hostEndpoint;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public String getHostName() {
        return hostName;
    }

    public SchemaType getHostType() {
        return hostType;
    }

}
