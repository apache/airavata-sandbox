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
package org.apache.airavata.runners.ssh;

import org.apache.airavata.models.Constants;
import org.apache.airavata.models.runners.ssh.SSHKeyAuthentication;
import org.apache.airavata.models.runners.ssh.SSHServerInfo;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class SSHRunnerTest {
    private final static Logger logger = LoggerFactory.getLogger(SSHRunnerTest.class);

    @Test
    public void test() {
        try{
            SSHKeyAuthentication br2SshAuthentication = new SSHKeyAuthentication(
                    Constants.loginUserName,
                    IOUtils.toByteArray(SSHRunnerTest.class.getClassLoader().getResourceAsStream("ssh/id_rsa")),
                    IOUtils.toByteArray(SSHRunnerTest.class.getClassLoader().getResourceAsStream("ssh/id_rsa.pub")),
                    "dummy",
                    SSHRunnerTest.class.getClassLoader().getResource("ssh/known_hosts").getPath(),
                    false
            );
            SSHServerInfo br2 = new SSHServerInfo(Constants.loginUserName, "bigred2.uits.iu.edu", br2SshAuthentication,22);

            String routingKey = UUID.randomUUID().toString();
            SSHRunner sshExecutor = new SSHRunner();

            sshExecutor.executeCommand(routingKey, "mkdir -p airavata", br2, br2SshAuthentication);

            sshExecutor.scpTo(routingKey, SSHRunnerTest.class.getClassLoader().getResource("job_tf.pbs").getPath(),
                    "~/airavata/", br2, br2SshAuthentication);

            sshExecutor.scpTo(routingKey, SSHRunnerTest.class.getClassLoader().getResource("code_tf.py").getPath(),
                    "~/airavata/", br2, br2SshAuthentication);

            sshExecutor.executeCommand(routingKey, new String[]{"cd ~/airavata", "qsub ~/airavata/job_tf.pbs"}, br2, br2SshAuthentication);

        }catch (Exception ex){
            ex.printStackTrace();
            Assert.fail();
        }
    }

}