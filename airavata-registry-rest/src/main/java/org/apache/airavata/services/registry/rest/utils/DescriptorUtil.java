package org.apache.airavata.services.registry.rest.utils;

import org.apache.airavata.commons.gfac.type.HostDescription;
import org.apache.airavata.schemas.gfac.GlobusHostType;

public class DescriptorUtil {

    public static HostDescription createHostDescription(String hostName, String hostAddress,
                                                        String hostEndpoint, String gatekeeperEndpoint) {
        HostDescription host = new HostDescription();
        if("".equalsIgnoreCase(gatekeeperEndpoint) || "".equalsIgnoreCase(hostEndpoint)) {
            host.getType().changeType(GlobusHostType.type);
            host.getType().setHostName(hostName);
            host.getType().setHostAddress(hostAddress);
            ((GlobusHostType) host.getType()).
                    setGridFTPEndPointArray(new String[]{hostEndpoint});
            ((GlobusHostType) host.getType()).
                    setGlobusGateKeeperEndPointArray(new String[]{gatekeeperEndpoint});
        } else {
            host.getType().setHostName(hostName);
            host.getType().setHostAddress(hostAddress);
        }
        return host;
    }
}
