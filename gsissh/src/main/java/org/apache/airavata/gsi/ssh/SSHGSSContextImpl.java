/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package org.apache.airavata.gsi.ssh;

import org.globus.gsi.GSIConstants;
import org.globus.gsi.bc.BouncyCastleCertProcessingFactory;
import org.globus.gsi.bc.BouncyCastleUtil;
import org.globus.gsi.gssapi.GSSConstants;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
import org.globus.gsi.gssapi.GlobusGSSException;
import org.globus.gsi.gssapi.GlobusGSSName;
import org.globus.gsi.util.CertificateUtil;
import org.globus.gsi.util.ProxyCertificateUtil;
import org.gridforum.jgss.ExtendedGSSContext;
import org.ietf.jgss.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * User: AmilaJ (amilaj@apache.org)
 * Date: 8/8/13
 * Time: 7:03 PM
 */

public class SSHGSSContextImpl implements ExtendedGSSContext {

    //============= Following Options are set through setOptions method ==================//
    protected Integer gssMode = GSIConstants.MODE_GSI;

    protected GSIConstants.DelegationType delegationType =
            GSIConstants.DelegationType.LIMITED;

    protected Boolean checkContextExpiration = Boolean.FALSE;

    protected Boolean rejectLimitedProxy = Boolean.FALSE;

    protected Boolean requireClientAuth = Boolean.TRUE;

    protected Map proxyPolicyHandlers;

    protected Boolean acceptNoClientCerts = Boolean.FALSE;

    protected Boolean requireAuthzWithDelegation = Boolean.TRUE;

    //====================================================================================//

    protected boolean delegationFinished = false;

    protected boolean conn = false;

    // gss context state variables
    protected boolean credentialDelegation = false;
    protected boolean anonymity = false;
    protected boolean encryption = false;
    protected boolean established = false;

    /**
     * Credential of this context. Might be anonymous
     */
    protected GlobusGSSCredentialImpl ctxCred;

    protected BouncyCastleCertProcessingFactory certFactory;

    /* handshake states */
    private static final int
            HANDSHAKE = 0,
            CLIENT_START_DEL = 2,
            CLIENT_END_DEL = 3;

    protected int state = HANDSHAKE;

    /**
     * Limited peer credentials
     */
    protected Boolean peerLimited = null;

    public void setOption(Oid option, Object value) throws GSSException {

        if (option == null) {
            throw new GlobusGSSException(GSSException.FAILURE,
                    GlobusGSSException.BAD_ARGUMENT,
                    "nullOption");
        }
        if (value == null) {
            throw new GlobusGSSException(GSSException.FAILURE,
                    GlobusGSSException.BAD_ARGUMENT,
                    "nullOptionValue");
        }

        if (option.equals(GSSConstants.GSS_MODE)) {
            setGssMode(value);
        } else if (option.equals(GSSConstants.DELEGATION_TYPE)) {
            setDelegationType(value);
        } else if (option.equals(GSSConstants.CHECK_CONTEXT_EXPIRATION)) {
            setCheckContextExpired(value);
        } else if (option.equals(GSSConstants.REJECT_LIMITED_PROXY)) {
            setRejectLimitedProxy(value);
        } else if (option.equals(GSSConstants.REQUIRE_CLIENT_AUTH)) {
            setRequireClientAuth(value);
        } else if (option.equals(GSSConstants.TRUSTED_CERTIFICATES)) {
            // setTrustedCertificates(value);
            throw new GSSException(GSSException.UNAVAILABLE);
        } else if (option.equals(GSSConstants.PROXY_POLICY_HANDLERS)) {
            setProxyPolicyHandlers(value);
        } else if (option.equals(GSSConstants.ACCEPT_NO_CLIENT_CERTS)) {
            setAcceptNoClientCerts(value);
        } else if (option.equals(GSSConstants
                .AUTHZ_REQUIRED_WITH_DELEGATION)) {
            setRequireAuthzWithDelegation(value);
        } else {
            throw new GlobusGSSException(GSSException.FAILURE,
                    GlobusGSSException.UNKNOWN_OPTION,
                    "unknownOption",
                    new Object[]{option});
        }

    }

    //============== Set Option Methods =====================//

    protected void setGssMode(Object value)
            throws GSSException {
        if (!(value instanceof Integer)) {
            throw new GlobusGSSException(GSSException.FAILURE,
                    GlobusGSSException.BAD_OPTION_TYPE,
                    "badType",
                    new Object[]{"GSS mode", Integer.class});
        }
        Integer v = (Integer) value;
        if (v.equals(GSIConstants.MODE_GSI) ||
                v.equals(GSIConstants.MODE_SSL)) {
            this.gssMode = v;
        } else {
            throw new GlobusGSSException(GSSException.FAILURE,
                    GlobusGSSException.BAD_OPTION,
                    "badGssMode");
        }
    }

    protected void setDelegationType(Object value)
            throws GSSException {
        GSIConstants.DelegationType v;
        if (value instanceof GSIConstants.DelegationType)
            v = (GSIConstants.DelegationType) value;
        else if (value instanceof Integer) {
            v = GSIConstants.DelegationType.get(((Integer) value).intValue());
        } else {
            throw new GlobusGSSException(GSSException.FAILURE,
                    GlobusGSSException.BAD_OPTION_TYPE,
                    "badType",
                    new Object[]{"delegation type",
                            GSIConstants.DelegationType.class});
        }
        if (v == GSIConstants.DelegationType.FULL ||
                v == GSIConstants.DelegationType.LIMITED) {
            this.delegationType = v;
        } else {
            throw new GlobusGSSException(GSSException.FAILURE,
                    GlobusGSSException.BAD_OPTION,
                    "badDelegType");
        }
    }

    protected void setCheckContextExpired(Object value)
            throws GSSException {
        if (!(value instanceof Boolean)) {
            throw new GlobusGSSException(GSSException.FAILURE,
                    GlobusGSSException.BAD_OPTION_TYPE,
                    "badType",
                    new Object[]{"check context expired", Boolean.class});
        }
        this.checkContextExpiration = (Boolean) value;
    }

    protected void setRejectLimitedProxy(Object value)
            throws GSSException {
        if (!(value instanceof Boolean)) {
            throw new GlobusGSSException(GSSException.FAILURE,
                    GlobusGSSException.BAD_OPTION_TYPE,
                    "badType",
                    new Object[]{"reject limited proxy", Boolean.class});
        }
        this.rejectLimitedProxy = (Boolean) value;
    }

    protected void setRequireClientAuth(Object value)
            throws GSSException {
        if (!(value instanceof Boolean)) {
            throw new GlobusGSSException(GSSException.FAILURE,
                    GlobusGSSException.BAD_OPTION_TYPE,
                    "badType",
                    new Object[]{"require client auth", Boolean.class});
        }
        this.requireClientAuth = (Boolean) value;
    }

    protected void setProxyPolicyHandlers(Object value)
            throws GSSException {
        if (!(value instanceof Map)) {
            throw new GlobusGSSException(GSSException.FAILURE,
                    GlobusGSSException.BAD_OPTION_TYPE,
                    "badType",
                    new Object[]{"Proxy policy handlers",
                            Map.class});
        }
        this.proxyPolicyHandlers = (Map) value;
    }

    protected void setAcceptNoClientCerts(Object value)
            throws GSSException {
        if (!(value instanceof Boolean)) {
            throw new GlobusGSSException(GSSException.FAILURE,
                    GlobusGSSException.BAD_OPTION_TYPE,
                    "badType",
                    new Object[]{"accept no client certs", Boolean.class});
        }
        this.acceptNoClientCerts = (Boolean) value;
    }

    protected void setRequireAuthzWithDelegation(Object value)
            throws GSSException {

        if (!(value instanceof Boolean)) {
            throw new GlobusGSSException(GSSException.FAILURE,
                    GlobusGSSException.BAD_OPTION_TYPE,
                    "badType",
                    new Object[]{"require authz with delehation",
                            Boolean.class});
        }
        this.requireAuthzWithDelegation = (Boolean) value;
    }

    //============== Set Option Methods =====================//


    public Object getOption(Oid option) throws GSSException {
        if (option == null) {
            throw new GlobusGSSException(GSSException.FAILURE,
                    GlobusGSSException.BAD_ARGUMENT,
                    "nullOption");
        }

        if (option.equals(GSSConstants.GSS_MODE)) {
            return this.gssMode;
        } else if (option.equals(GSSConstants.DELEGATION_TYPE)) {
            return this.delegationType;
        } else if (option.equals(GSSConstants.CHECK_CONTEXT_EXPIRATION)) {
            return this.checkContextExpiration;
        } else if (option.equals(GSSConstants.REJECT_LIMITED_PROXY)) {
            return this.rejectLimitedProxy;
        } else if (option.equals(GSSConstants.REQUIRE_CLIENT_AUTH)) {
            return this.requireClientAuth;
        } else if (option.equals(GSSConstants.TRUSTED_CERTIFICATES)) {
            // return this.tc;
            throw new GSSException(GSSException.UNAVAILABLE);
        } else if (option.equals(GSSConstants.PROXY_POLICY_HANDLERS)) {
            // return this.proxyPolicyHandlers;
            throw new GSSException(GSSException.UNAVAILABLE);
        } else if (option.equals(GSSConstants.ACCEPT_NO_CLIENT_CERTS)) {
            return this.acceptNoClientCerts;
        }

        return null;
    }

    public byte[] initDelegation(GSSCredential credential, Oid mechanism,
                                 int lifetime, byte[] buf, int off, int len)
            throws GSSException {
        //TODO: implement this
        return new byte[0];
    }

    public byte[] acceptDelegation(int i, byte[] bytes, int i1, int i2)
            throws GSSException {
        throw new GSSException(GSSException.UNAVAILABLE);
    }

    /**
     * TODO we need to implement delegation
     * @return At the moment null
     */
    public GSSCredential getDelegatedCredential() {
        return null;
    }

    public boolean isDelegationFinished() {
        // TODO properly implement delegation
        return this.delegationFinished;
    }

    /**
     * Retrieves arbitrary data about this context.
     * Currently supported oid: <UL>
     * <LI>
     * {@link GSSConstants#X509_CERT_CHAIN GSSConstants.X509_CERT_CHAIN}
     * returns certificate chain of the peer (<code>X509Certificate[]</code>).
     * </LI>
     * </UL>
     *
     * @param oid the oid of the information desired.
     * @return the information desired. Might be null.
     * @exception GSSException containing the following major error codes:
     *            <code>GSSException.FAILURE</code>
     */
    public Object inquireByOid(Oid oid) throws GSSException {
        if (oid == null) {
            throw new GlobusGSSException(GSSException.FAILURE,
                    GlobusGSSException.BAD_ARGUMENT,
                    "nullOption");
        }

        if (oid.equals(GSSConstants.X509_CERT_CHAIN)) {
            if (isEstablished()) {
                // converting certs is slower but keeping converted certs
                // takes lots of memory.
                try {
                    Certificate[] peerCerts;
                    //TODO:  used to get this from
                    //  SSLEngine.getSession().getPeerCertificates()
                    peerCerts = null;
                    if (peerCerts != null && peerCerts.length > 0) {
                        return (X509Certificate[]) peerCerts;
                    } else {
                        return null;
                    }
                } catch (Exception e) {
                    throw new GlobusGSSException(
                            GSSException.DEFECTIVE_CREDENTIAL,
                            e
                    );
                }
            }
        } else if (oid.equals(GSSConstants.RECEIVED_LIMITED_PROXY)) {
            return this.peerLimited;
        }

        return null;
    }

    public void setBannedCiphers(String[] strings) {
        // TODO implement this later
    }

    private void init() throws GlobusGSSException {
        this.certFactory = BouncyCastleCertProcessingFactory.getDefault();
        this.state = HANDSHAKE;


        try {
          /*  this.cipherFactory = new GlobusTlsCipherFactory();
            this.tlsClient =
                    new GlobusTlsClient(this.ctxCred.getX509Credential(),
                            this.cipherFactory);*/
        } catch (Exception e) {
            throw new GlobusGSSException(GSSException.FAILURE, e);
        }

        // TODO: set enabled cipher suites in client?
        // TODO: enable null encryption ciphers on user request?

        /*
       TlsProtocolVersion[] tlsVersion =
            new TlsProtocolVersion[] {TlsProtocolVersion.TLSv10,
                                      TlsProtocolVersion.SSLv3};
                                      */
        //new TlsProtocolVersion[] {TlsProtocolVersion.TLSv10};
        //new TlsProtocolVersion[] {TlsProtocolVersion.SSLv3};

        //this.tlsHU = new TlsHandlerUtil(this.tlsClient, tlsVersion);
        //this.tlsHU = new TlsHandlerUtil(this.tlsClient);
    }

    /**
     * This function drives the initiating side of the context establishment
     * process. It is expected to be called in tandem with the
     * {@link #acceptSecContext(byte[], int, int) acceptSecContext} function.
     * <BR>
     * The behavior of context establishment process can be modified by
     * {@link GSSConstants#GSS_MODE GSSConstants.GSS_MODE},
     * {@link GSSConstants#DELEGATION_TYPE GSSConstants.DELEGATION_TYPE}, and
     * {@link GSSConstants#REJECT_LIMITED_PROXY GSSConstants.REJECT_LIMITED_PROXY}
     * context options. If the {@link GSSConstants#GSS_MODE GSSConstants.GSS_MODE}
     * option is set to
     * {@link org.globus.gsi.GSIConstants#MODE_SSL GSIConstants.MODE_SSL}
     * the context establishment process will be compatible with regular SSL
     * (no credential delegation support). If the option is set to
     * {@link org.globus.gsi.GSIConstants#MODE_GSI GSIConstants.GSS_MODE_GSI}
     * credential delegation during context establishment process will performed.
     * The delegation type to be performed can be set using the
     * {@link GSSConstants#DELEGATION_TYPE GSSConstants.DELEGATION_TYPE}
     * context option. If the {@link GSSConstants#REJECT_LIMITED_PROXY
     * GSSConstants.REJECT_LIMITED_PROXY} option is enabled,
     * a peer presenting limited proxy credential will be automatically
     * rejected and the context establishment process will be aborted.
     *
     * @return a byte[] containing the token to be sent to the peer.
     *         null indicates that no token is generated (needs more data).
     */
    public byte[] initSecContext(byte[] inBuff, int off, int len)
            throws GSSException { return new byte[0]; }
    /*public byte[] initSecContext(byte[] inBuff, int off, int len)
            throws GSSException {

        if (!this.conn) {
            //System.out.println("enter initializing in initSecContext");
            if (this.credentialDelegation) {
                if (this.gssMode.equals(GSIConstants.MODE_SSL)) {
                    throw new GlobusGSSException(GSSException.FAILURE,
                            GlobusGSSException.BAD_ARGUMENT,
                            "initCtx00");
                }
                if (this.anonymity) {
                    throw new GlobusGSSException(GSSException.FAILURE,
                            GlobusGSSException.BAD_ARGUMENT,
                            "initCtx01");
                }
            }

            if (this.anonymity || this.ctxCred.getName().isAnonymous()) {
                this.anonymity = true;
            } else {
                this.anonymity = false;

                if (ctxCred.getUsage() != GSSCredential.INITIATE_ONLY &&
                        ctxCred.getUsage() != GSSCredential.INITIATE_AND_ACCEPT) {
                    throw new GlobusGSSException(
                            GSSException.DEFECTIVE_CREDENTIAL,
                            GlobusGSSException.UNKNOWN,
                            "badCredUsage");
                }
            }

            init();

            this.conn = true;
        }

        // Unless explicitly disabled, check if delegation is
        // requested and expected target is null
        if (!Boolean.FALSE.equals(this.requireAuthzWithDelegation)) {

            if (this.expectedTargetName == null &&
                    this.credentialDelegation) {
                throw new GlobusGSSException(GSSException.FAILURE,
                        GlobusGSSException.BAD_ARGUMENT,
                        "initCtx02");
            }
        }

        byte[] returnToken = null;

        switch (state) {
            case HANDSHAKE:
                try {
                    returnToken = this.tlsHU.nextHandshakeToken(inBuff);

                    if (this.tlsHU.isHandshakeFinished()) {
                        //System.out.println("initSecContext handshake finished");
                        handshakeFinished(); // just enable encryption

                        Certificate[] chain = this.tlsClient.getPeerCerts();
                        if (!(chain instanceof X509Certificate[])) {
                            throw new Exception(
                                    "Certificate chain not of type X509Certificate");
                        }

                        for (X509Certificate cert : (X509Certificate[]) chain) {
                            setGoodUntil(cert.getNotAfter());
                        }

                        String identity = BouncyCastleUtil.getIdentity(
                                bcConvert(
                                        BouncyCastleUtil.getIdentityCertificate(
                                                (X509Certificate[]) chain)));
                        this.targetName =
                                new GlobusGSSName(CertificateUtil.toGlobusID(
                                        identity, false));

                        this.peerLimited = ProxyCertificateUtil.isLimitedProxy(
                                BouncyCastleUtil.getCertificateType(
                                        (X509Certificate) chain[0]));

                        // initiator
                        if (this.anonymity) {
                            this.sourceName = new GlobusGSSName();
                        } else {
                            for (X509Certificate cert :
                                    ctxCred.getCertificateChain()) {
                                setGoodUntil(cert.getNotAfter());
                            }
                            this.sourceName = this.ctxCred.getName();
                        }

                        // mutual authentication test
                        if (this.expectedTargetName != null &&
                                !this.expectedTargetName.equals(this.targetName)) {
                            throw new GlobusGSSException(
                                    GSSException.UNAUTHORIZED,
                                    GlobusGSSException.BAD_NAME,
                                    "authFailed00",
                                    new Object[]{this.expectedTargetName,
                                            this.targetName});
                        }

                        if (this.gssMode.equals(GSIConstants.MODE_GSI)) {
                            this.state = CLIENT_START_DEL;
                            // if there is a token to return then break
                            // otherwise we fall through to delegation
                            if (returnToken != null && returnToken.length > 0) {
                                break;
                            }
                        } else {
                            setDone();
                            break;
                        }

                    } else { // handshake not complete yet
                        break;
                    }
                } catch (IOException e) {
                    throw new GlobusGSSException(GSSException.FAILURE, e);
                } catch (Exception e) {
                    throw new GlobusGSSException(GSSException.FAILURE, e);
                }

            case CLIENT_START_DEL:

                // sanity check - might be invalid state
                if (this.state != CLIENT_START_DEL ||
                        (returnToken != null && returnToken.length > 0) ) {
                    throw new GSSException(GSSException.FAILURE);
                }

                try {
                    String deleg;

                    if (getCredDelegState()) {
                        deleg = Character.toString(
                                GSIConstants.DELEGATION_CHAR);
                        this.state = CLIENT_END_DEL;
                    } else {
                        deleg = Character.toString('0');
                        setDone();
                    }

                    // TODO: Force ASCII encoding?
                    byte[] a = deleg.getBytes();
                    // SSL wrap the delegation token
                    returnToken = this.tlsHU.wrap(a);
                } catch (Exception e) {
                    throw new GlobusGSSException(GSSException.FAILURE, e);
                }

                break;

            case CLIENT_END_DEL:

                if (inBuff == null || inBuff.length == 0) {
                    throw new GSSException(GSSException.DEFECTIVE_TOKEN);
                }

                try {
                    // SSL unwrap the token on the inBuff (it's a CSR)
                    byte[] certReq = this.tlsHU.unwrap(inBuff);

                    if (certReq.length == 0) break;

                    X509Certificate[] chain =
                            this.ctxCred.getCertificateChain();

                    X509Certificate cert = this.certFactory.createCertificate(
                            new ByteArrayInputStream(certReq),
                            chain[0],
                            this.ctxCred.getPrivateKey(),
                            -1,
                            BouncyCastleCertProcessingFactory.decideProxyType(
                                    chain[0], this.delegationType));

                    byte[] enc = cert.getEncoded();
                    // SSL wrap the encoded cert and return that buffer
                    returnToken = this.tlsHU.wrap(enc);
                    setDone();
                } catch (GeneralSecurityException e) {
                    throw new GlobusGSSException(GSSException.FAILURE, e);
                } catch (IOException e) {
                    throw new GlobusGSSException(GSSException.FAILURE, e);
                }

                break;

            default:
                throw new GSSException(GSSException.FAILURE);
        }

        //TODO: Why is there a check for CLIENT_START_DEL?
        if (returnToken != null && returnToken.length > 0 ||
                this.state == CLIENT_START_DEL) {
            return returnToken;
        } else
            return null;
    }*/


    public int initSecContext(InputStream inStream, OutputStream outStream) throws GSSException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public byte[] acceptSecContext(byte[] inToken, int offset, int len) throws GSSException {
        return new byte[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void acceptSecContext(InputStream inStream, OutputStream outStream) throws GSSException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isEstablished() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dispose() throws GSSException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getWrapSizeLimit(int qop, boolean confReq, int maxTokenSize) throws GSSException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public byte[] wrap(byte[] inBuf, int offset, int len, MessageProp msgProp) throws GSSException {
        return new byte[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void wrap(InputStream inStream, OutputStream outStream, MessageProp msgProp) throws GSSException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public byte[] unwrap(byte[] inBuf, int offset, int len, MessageProp msgProp) throws GSSException {
        return new byte[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void unwrap(InputStream inStream, OutputStream outStream, MessageProp msgProp) throws GSSException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public byte[] getMIC(byte[] inMsg, int offset, int len, MessageProp msgProp) throws GSSException {
        return new byte[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void getMIC(InputStream inStream, OutputStream outStream, MessageProp msgProp) throws GSSException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void verifyMIC(byte[] inToken, int tokOffset, int tokLen, byte[] inMsg, int msgOffset, int msgLen, MessageProp msgProp) throws GSSException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void verifyMIC(InputStream tokStream, InputStream msgStream, MessageProp msgProp) throws GSSException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public byte[] export() throws GSSException {
        return new byte[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void requestMutualAuth(boolean state) throws GSSException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void requestReplayDet(boolean state) throws GSSException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void requestSequenceDet(boolean state) throws GSSException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void requestCredDeleg(boolean state) throws GSSException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void requestAnonymity(boolean state) throws GSSException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void requestConf(boolean state) throws GSSException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void requestInteg(boolean state) throws GSSException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void requestLifetime(int lifetime) throws GSSException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setChannelBinding(ChannelBinding cb) throws GSSException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean getCredDelegState() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean getMutualAuthState() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean getReplayDetState() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean getSequenceDetState() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean getAnonymityState() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isTransferable() throws GSSException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isProtReady() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean getConfState() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean getIntegState() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getLifetime() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public GSSName getSrcName() throws GSSException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public GSSName getTargName() throws GSSException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Oid getMech() throws GSSException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public GSSCredential getDelegCred() throws GSSException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isInitiator() throws GSSException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
