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

package org.apache.airavata.jobsubmission.context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.security.cert.X509Certificate;

import org.apache.log4j.Logger;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.TrustedCertificates;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
import org.globus.myproxy.MyProxy;
import org.ietf.jgss.GSSCredential;

public class MyProxyCredentials implements Serializable {

    private static final long serialVersionUID = -2471014486509046212L;
    protected String myproxyHostname;
    protected String myproxyUserName;
    protected String myproxyPassword;
    protected int myproxyPortNumber;
    protected GSSCredential gssCredential;
    protected String portalUserName;
    private String hostcertsKeyFile;
    private String trustedCertsFile;

    protected int myproxyLifeTime = 14400;
    final static int SECS_PER_MIN = 60;
    final static int SECS_PER_HOUR = 3600;

    private boolean initialized = false;
    private boolean user = true;
    protected X509Certificate[] trustedCertificates;

    private static final Logger log = Logger.getLogger(MyProxyCredentials.class);

    public MyProxyCredentials() {
        // default constructor
    }

    public MyProxyCredentials(String myproxyServer, int myproxyPort, String myproxyUsername, String myproxyPassphrase,
            int myproxyLifetime) {
        this.myproxyHostname = myproxyServer;
        this.myproxyPortNumber = myproxyPort;
        this.myproxyUserName = myproxyUsername;
        this.myproxyPassword = myproxyPassphrase;
        this.myproxyLifeTime = myproxyLifetime;

    }

    public GSSCredential getGssCredential() throws Exception {
        FileInputStream fis = null;
        try {
            if (hostcertsKeyFile != null && !user) {
                fis = new FileInputStream(hostcertsKeyFile);
                // X509Credential globusCred = new X509Credential(fis); //**
                GlobusCredential globusCred = new GlobusCredential(fis);
                this.gssCredential = new GlobusGSSCredentialImpl(globusCred, GSSCredential.INITIATE_AND_ACCEPT);

            } else {
                this.gssCredential = getDefaultProxy();
            }
            if (gssCredential != null) {
                return gssCredential;
            }
        } catch (Exception e) {
            log.error("Failed to load proxy credential from ProxyManager");
            e.printStackTrace();
            throw e;
        } finally {
            if (fis != null) {
                fis.close();
            }
        }

        // then we will try the MyProxy
        if (getMyproxyUserName() != null && getMyproxyPassword() != null) {
            gssCredential = renewProxy();
        }
        return gssCredential;
    }

    public GSSCredential getDefaultProxy() throws Exception {
        init();
        MyProxy myproxy = new MyProxy(this.myproxyHostname, this.myproxyPortNumber);
        log.info("USER=" + this.myproxyUserName + ",PASS=" + this.myproxyPassword + ",TIME=" + this.myproxyLifeTime);
        return myproxy.get(this.myproxyUserName, this.myproxyPassword, this.myproxyLifeTime);
    }

    private void init() {
        if (trustedCertsFile != null) {
            if (new File(trustedCertsFile).isDirectory()) {
                TrustedCertificates certificates = TrustedCertificates.load(trustedCertsFile);
                TrustedCertificates.setDefaultTrustedCertificates(certificates);
            } else {
                this.trustedCertificates = CertificateManager.getTrustedCertificate(trustedCertsFile);

                TrustedCertificates certificatesArray = new TrustedCertificates(this.trustedCertificates);
                TrustedCertificates.setDefaultTrustedCertificates(certificatesArray);
            }
        }
    }

    public GSSCredential renewProxy() throws Exception {
        init();

        FileOutputStream fout = null;
        try {
            String proxyloc = null;
            MyProxy myproxy = new MyProxy(myproxyHostname, myproxyPortNumber);
            int lifeHours = myproxyLifeTime * SECS_PER_HOUR;
            GSSCredential proxy = myproxy.get(myproxyUserName, myproxyPassword, lifeHours);

            GlobusCredential globusCred = null; // **
            // X509Credential globusCred = null; //**
            if (proxy instanceof GlobusGSSCredentialImpl) {
                globusCred = ((GlobusGSSCredentialImpl) proxy).getGlobusCredential();// **
                // globusCred = ((GlobusGSSCredentialImpl) proxy).getX509Credential();//**
                log.info("got proxy from myproxy for " + myproxyUserName + " with " + myproxyLifeTime + " lifetime.");
                String uid = myproxyUserName;
                if (proxyloc == null) {
                    log.info("uid: " + uid);
                    proxyloc = "/tmp/x509up_u" + uid;
                }
                File proxyfile = new File(proxyloc);
                log.info("proxy location: " + proxyfile.getAbsolutePath());
                if (proxyfile.exists() == false) {
                    String dirpath = proxyloc.substring(0, proxyloc.lastIndexOf('/'));
                    File dir = new File(dirpath);
                    if (dir.exists() == false) {
                        dir.mkdirs();
                        log.info("new directory " + dirpath + " is created.");
                    }
                    proxyfile.createNewFile();
                    log.info("new proxy file " + proxyloc + " is created.");
                }
                fout = new FileOutputStream(proxyfile);
                globusCred.save(fout);
                String osName = System.getProperty("os.name");
                if (!osName.contains("Windows")) {
                    Runtime.getRuntime().exec("/bin/chmod 600 " + proxyloc);
                }
                log.info("Proxy file renewed to " + proxyloc + " for the user " + myproxyUserName + " with "
                        + myproxyLifeTime + " lifetime.");

            }
            return proxy;
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            if (fout != null) {
                fout.close();
            }
        }
    }

    /**
     * @return the myproxyHostname
     */
    public String getMyproxyHostname() {
        return myproxyHostname;
    }

    /**
     * @param myproxyHostname
     *            the myproxyHostname to set
     */
    public void setMyproxyHostname(String myproxyHostname) {
        this.myproxyHostname = myproxyHostname;
    }

    /**
     * @return the myproxyUserName
     */
    public String getMyproxyUserName() {
        return myproxyUserName;
    }

    /**
     * @param myproxyUserName
     *            the myproxyUserName to set
     */
    public void setMyproxyUserName(String myproxyUserName) {
        this.myproxyUserName = myproxyUserName;
    }

    /**
     * @return the myproxyPassword
     */
    public String getMyproxyPassword() {
        return myproxyPassword;
    }

    /**
     * @param myproxyPassword
     *            the myproxyPassword to set
     */
    public void setMyproxyPassword(String myproxyPassword) {
        this.myproxyPassword = myproxyPassword;
    }

    /**
     * @return the myproxyLifeTime
     */
    public int getMyproxyLifeTime() {
        return myproxyLifeTime;
    }

    /**
     * @param myproxyLifeTime
     *            the myproxyLifeTime to set
     */
    public void setMyproxyLifeTime(int myproxyLifeTime) {
        this.myproxyLifeTime = myproxyLifeTime;
    }

    /**
     * @return the myproxyPortNumber
     */
    public int getMyproxyPortNumber() {
        return myproxyPortNumber;
    }

    /**
     * @param myproxyPortNumber
     *            the myproxyPortNumber to set
     */
    public void setMyproxyPortNumber(int myproxyPortNumber) {
        this.myproxyPortNumber = myproxyPortNumber;
    }

    /**
     * @return the portalUserName
     */
    public String getPortalUserName() {
        return portalUserName;
    }

    /**
     * @param portalUserName
     *            the portalUserName to set
     */
    public void setPortalUserName(String portalUserName) {
        this.portalUserName = portalUserName;
    }

    /**
     * Returns the initialized.
     * 
     * @return The initialized
     */
    public boolean isInitialized() {
        return this.initialized;
    }

    /**
     * Sets initialized.
     * 
     * @param initialized
     *            The initialized to set.
     */
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    /**
     * @param hostcertsKeyFile
     *            the hostcertsKeyFile to set
     */
    public void setHostcertsKeyFile(String hostcertsKeyFile) {
        this.hostcertsKeyFile = hostcertsKeyFile;
    }

    /**
     * @return the hostcertsKeyFile
     */
    public String getHostcertsKeyFile() {
        return hostcertsKeyFile;
    }

    /**
     * @param trustedCertsFile
     *            the trustedCertsFile to set
     */
    public void setTrustedCertsFile(String trustedCertsFile) {
        this.trustedCertsFile = trustedCertsFile;
    }

    /**
     * @return the trustedCertsFile
     */
    public String getTrustedCertsFile() {
        return trustedCertsFile;
    }

}
