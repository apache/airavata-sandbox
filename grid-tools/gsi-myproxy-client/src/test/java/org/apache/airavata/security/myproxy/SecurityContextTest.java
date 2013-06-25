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

package org.apache.airavata.security.myproxy;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.globus.gsi.provider.GlobusProvider;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.Security;

/**
 * User: AmilaJ (amilaj@apache.org)
 * Date: 5/23/13
 * Time: 2:44 PM
 * NOTE : BEFORE RUNNING THESE TESTS UPDATE gsi-myproxy-client/src/test/resources/airavata-myproxy-client.properties
 * FILE WITH APPROPRIATE MY PROXY SERVER CONFIGURATIONS.
 */



public class SecurityContextTest extends TestCase {

    private String userName;
    private String password;

    private static final Logger log = Logger.getLogger(SecurityContext.class);

    static {
        Security.addProvider(new GlobusProvider());
    }

    public void setUp() {
        this.userName = System.getProperty("myproxy.user");
        this.password = System.getProperty("myproxy.password");

        if (userName == null || password == null || userName.trim().equals("") || password.trim().equals("")) {
            log.error("===== Please set myproxy.user and myproxy.password system properties. =======");
            Assert.fail("Please set myproxy.user and myproxy.password system properties.");
        }

        log.info("Using my proxy user name - " + userName);

    }


    public void testLogin() throws Exception {

        SecurityContext myProxy = new SecurityContext(userName, password);
        myProxy.login();

        Assert.assertNotNull(myProxy.getGssCredential());
    }


    public void testProxyCredentials() throws Exception {

        SecurityContext myProxy = new SecurityContext(userName, password);
        myProxy.login();

        Assert.assertNotNull(myProxy.getProxyCredentials(myProxy.getGssCredential()));
    }

    /**
     * Before executing you need to add your host as a trusted renewer.
     * Execute following command
     * > myproxy-logon -t <LIFETIME></LIFETIME> -s <MY PROXY SERVER> -l <USER NAME>
     * E.g :- > myproxy-logon -t 264 -s myproxy.teragrid.org -l us3
     *          Enter MyProxy pass phrase:
     *          A credential has been received for user us3 in /tmp/x509up_u501.
     * > myproxy-init -A --cert /tmp/x509up_u501 --key /tmp/x509up_u501 -l us3 -s myproxy.teragrid.org
     * @throws Exception
     */
    public void testRenewCredentials() throws Exception {

        SecurityContext myProxy = new SecurityContext(userName, password);
        myProxy.login();

        Assert.assertNotNull(myProxy.renewCredentials(myProxy.getGssCredential()));

    }
}
