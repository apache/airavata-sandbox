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

import com.jcraft.jsch.UserInfo;
import org.apache.airavata.gsi.ssh.api.*;
import org.apache.airavata.gsi.ssh.api.authentication.AuthenticationInfo;
import org.apache.airavata.gsi.ssh.config.ConfigReader;
import org.apache.airavata.gsi.ssh.impl.authentication.DefaultPasswordAuthenticationInfo;
import org.apache.airavata.gsi.ssh.impl.authentication.DefaultPublicKeyFileAuthentication;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * User: AmilaJ (amilaj@apache.org)
 * Date: 9/20/13
 * Time: 12:05 PM
 */

public class VanilaSSHTest {

    private String userName;
    private String password;
    private String passPhrase;
    private String hostName;

    @BeforeTest
    public void setUp() throws Exception {


        this.hostName = "bigred2.uits.iu.edu";
        this.userName = System.getProperty("my.ssh.user");
        this.password = System.getProperty("my.ssh.user.password");
        this.passPhrase = System.getProperty("my.ssh.user.pass.phrase");

        if (this.userName == null || this.passPhrase == null || this.password == null) {
            System.out.println("########### In order to run tests you need to set password and " +
                    "passphrase ###############");
            System.out.println("Use -Dmy.ssh.user=xxx -Dmy.ssh.user.password=yyy -Dmy.ssh.user.pass.phrase=zzz");
        }

    }


    @Test
    public void testSimpleCommand1() throws Exception{

        System.out.println("Starting vanila SSH test ....");

        AuthenticationInfo authenticationInfo = new DefaultPasswordAuthenticationInfo(this.password);

        // Create command
        CommandInfo commandInfo = new RawCommandInfo("/opt/torque/torque-4.2.3.1/bin/qstat");

        // Server info
        ServerInfo serverInfo = new ServerInfo(this.userName, this.hostName);

        // Output
        CommandOutput commandOutput = new SystemCommandOutput();

        // Execute command
        CommandExecutor.executeCommand(commandInfo, serverInfo, authenticationInfo, commandOutput, new ConfigReader());


    }

    @Test
    public void testSimpleCommand2() throws Exception{

        System.out.println("Starting vanila SSH test ....");

        String privateKeyFile = "/Users/thejaka/.ssh/id_rsa";
        String publicKeyFile = "/Users/thejaka/.ssh/id_rsa.pub";

        AuthenticationInfo authenticationInfo = new DefaultPublicKeyFileAuthentication(publicKeyFile, privateKeyFile,
                this.passPhrase);

        // Create command
        CommandInfo commandInfo = new RawCommandInfo("/opt/torque/torque-4.2.3.1/bin/qstat");

        // Server info
        ServerInfo serverInfo = new ServerInfo(this.userName, this.hostName);

        // Output
        CommandOutput commandOutput = new SystemCommandOutput();

        // Execute command
        CommandExecutor.executeCommand(commandInfo, serverInfo, authenticationInfo, commandOutput, new ConfigReader());


    }

}
