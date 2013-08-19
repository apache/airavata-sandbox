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

package org.apache.airavata.gsi.ssh.impl;

import org.apache.airavata.gsi.ssh.api.*;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;

/**
 * User: AmilaJ (amilaj@apache.org)
 * Date: 8/15/13
 * Time: 10:54 AM
 */


public class DefaultSSHApiTest {

    private String myProxyUserName;
    private String myProxyPassword;
    private String certificateLocation;
    private String pbsFilePath;
    private String workingDirectory;

    @BeforeTest
    public void setUp() throws Exception {
        myProxyUserName = System.getProperty("myproxy.user");
        myProxyPassword = System.getProperty("myproxy.password");

        String pomDirectory = System.getProperty("basedir");

        File pomFileDirectory = new File(pomDirectory);

        System.out.println("POM directory ----------------- " + pomFileDirectory.getAbsolutePath());

        certificateLocation = pomFileDirectory.getAbsolutePath() + "/certificates";

        pbsFilePath = pomFileDirectory.getAbsolutePath() + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator +  "sleep.pbs";

        workingDirectory = File.separator + "home" + File.separator + "ogce" ;
        if (myProxyUserName == null || myProxyPassword == null) {
            System.out.println(">>>>>> Please run tests with my proxy user name and password. " +
                    "E.g :- mvn clean install -Dmyproxy.user=xxx -Dmyproxy.password=xxx <<<<<<<");
            throw new Exception("Need my proxy user name password to run tests.");
        }
    }

    public void tearDown() throws Exception {
    }

    @Test
    public void testExecuteCommand() throws Exception {
         // Create authentication
        AuthenticationInfo authenticationInfo
                = new MyProxyAuthenticationInfo(myProxyUserName, myProxyPassword, "myproxy.teragrid.org",
                7512, 17280000);

        // Create command
        CommandInfo commandInfo = new RawCommandInfo("/opt/torque/bin/qstat");

        // Server info
        ServerInfo serverInfo = new ServerInfo("ogce" ,"trestles.sdsc.edu");

        // Output
        CommandOutput commandOutput = new SystemCommandOutput();

        // Get the API
        SSHApi sshApi = SSHApiFactory.createSSHApi(this.certificateLocation);

        // Execute command
        sshApi.executeCommand(commandInfo, serverInfo, authenticationInfo, commandOutput);
    }

    @Test
    public void testSubmitSimpleCommand() throws Exception {
        // Create authentication
        AuthenticationInfo authenticationInfo
                = new MyProxyAuthenticationInfo(myProxyUserName, myProxyPassword, "myproxy.teragrid.org",
                7512, 17280000);

        // Create command
        CommandInfo commandInfo = new RawCommandInfo("/opt/torque/bin/qsub /home/ogce/test.pbs");

        // Server info
        ServerInfo serverInfo = new ServerInfo("ogce" ,"trestles.sdsc.edu");

        // Output
        CommandOutput commandOutput = new SystemCommandOutput();

        // Get the API
        SSHApi sshApi = SSHApiFactory.createSSHApi(this.certificateLocation);

        // Execute command
        sshApi.executeCommand(commandInfo, serverInfo, authenticationInfo, commandOutput);
    }

    @Test
    public void testSubmitJob() throws Exception {
        // Create authentication
        AuthenticationInfo authenticationInfo
                = new MyProxyAuthenticationInfo(myProxyUserName, myProxyPassword, "myproxy.teragrid.org",
                7512, 17280000);

        // Create command
        CommandInfo commandInfo = new RawCommandInfo("/opt/torque/bin/qsub /home/ogce/sleep.pbs");

        // Server info
        ServerInfo serverInfo = new ServerInfo("ogce" ,"trestles.sdsc.edu");

        // Output
        CommandOutput commandOutput = new SystemCommandOutput();

        // Get the API
        SSHApi sshApi = SSHApiFactory.createSSHApi(this.certificateLocation);

        // Execute command
        sshApi.submitJob(commandInfo, serverInfo, authenticationInfo, commandOutput, pbsFilePath, workingDirectory);
    }
}
