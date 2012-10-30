package org.apache.airavata.credential.store;

import java.util.Date;

/**
 * Represents the certificate credentials.
 */
public class CertificateCredential implements Credential {

    public CertificateCredential() {

    }

    /**
     * The community user associated with this credentials.
     */
    private CommunityUser communityUser;

    private String certificate;

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    private String privateKey;

    private long lifeTime;

    private String portalUserName;

    private String notBefore;

    public String getNotBefore() {
        return notBefore;
    }

    public void setNotBefore(String notBefore) {
        this.notBefore = notBefore;
    }

    public String getNotAfter() {
        return notAfter;
    }

    public void setNotAfter(String notAfter) {
        this.notAfter = notAfter;
    }

    private String notAfter;

    public Date getCertificateRequestedTime() {
        return certificateRequestedTime;
    }

    public void setCertificateRequestedTime(Date certificateRequestedTime) {
        this.certificateRequestedTime = certificateRequestedTime;
    }

    private Date certificateRequestedTime;

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public long getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(long lifeTime) {
        this.lifeTime = lifeTime;
    }

    public String getPortalUserName() {
        return portalUserName;
    }

    public void setPortalUserName(String portalUserName) {
        this.portalUserName = portalUserName;
    }

    public CommunityUser getCommunityUser() {
        return communityUser;
    }

    public void setCommunityUser(CommunityUser communityUser) {
        this.communityUser = communityUser;
    }

}
