package org.apache.airavata.credential.store;

/**
 * This keeps the mapping between community user and portal user.
 */
public class Mapping {

    private String gatewayName;
    private String communityUser;
    private String portalUser;

    public Mapping() {
    }

    public Mapping(String gatewayName, String communityUser, String portalUser) {
        this.gatewayName = gatewayName;
        this.communityUser = communityUser;
        this.portalUser = portalUser;
    }

    public String getGatewayName() {
        return gatewayName;
    }

    public void setGatewayName(String gatewayName) {
        this.gatewayName = gatewayName;
    }

    public String getCommunityUser() {
        return communityUser;
    }

    public void setCommunityUser(String communityUser) {
        this.communityUser = communityUser;
    }

    public String getPortalUser() {
        return portalUser;
    }

    public void setPortalUser(String portalUser) {
        this.portalUser = portalUser;
    }
}
