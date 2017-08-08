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
package org.apache.airavata.resources.batch;

import org.apache.airavata.models.*;
import org.apache.airavata.models.resources.Authentication;
import org.apache.airavata.models.resources.ServerInfo;
import org.apache.airavata.models.resources.hpc.JobManagerConfiguration;
import org.apache.airavata.models.resources.hpc.PBSJobConfiguration;
import org.apache.airavata.models.runners.ssh.SSHKeyAuthentication;
import org.apache.airavata.models.runners.ssh.SSHServerInfo;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class BigRed2 extends HPCBatchResource {
    private final static Logger logger = LoggerFactory.getLogger(BigRed2.class);

    public BigRed2(ServerInfo serverInfo, JobManagerConfiguration jobManagerConfiguration, Authentication authentication) throws Exception {
        super(serverInfo, jobManagerConfiguration, authentication);
    }

    public BigRed2() throws Exception {
        //These should be read from a database. For simplicity I have hardcoded them
        SSHKeyAuthentication br2SshAuthentication = new SSHKeyAuthentication(
                Constants.loginUserName,
                IOUtils.toByteArray(BigRed2.class.getClassLoader().getResourceAsStream("ssh/id_rsa")),
                IOUtils.toByteArray(BigRed2.class.getClassLoader().getResourceAsStream("ssh/id_rsa.pub")),
                "dummy",
                BigRed2.class.getClassLoader().getResource("ssh/known_hosts").getPath(),
                false
        );
        SSHServerInfo br2 = new SSHServerInfo(Constants.loginUserName, "bigred2.uits.iu.edu", br2SshAuthentication,22);
        Map<JobManagerConfiguration.JobManagerCommand, String> jobManagerCommands = new HashMap<>();
        jobManagerCommands.put(JobManagerConfiguration.JobManagerCommand.SUBMISSION, "qsub");
        jobManagerCommands.put(JobManagerConfiguration.JobManagerCommand.JOB_MONITORING, "qstat");
        jobManagerCommands.put(JobManagerConfiguration.JobManagerCommand.DELETION, "qdel");

        JobManagerConfiguration pbsJobConfiguration = new PBSJobConfiguration(PBSJobConfiguration.class.getClassLoader().
                getResource("resources/batch/PBS_Groovy.template").getPath(), ".pbs",
                "/opt/torque/torque-5.0.1/bin", jobManagerCommands, new BatchJobOutputParser());

        this.serverInfo = (SSHServerInfo) br2;
        this.authentication = (SSHKeyAuthentication) br2SshAuthentication;
        this.jobManagerConfiguration = pbsJobConfiguration;
        this.outputParser = jobManagerConfiguration.getParser();
    }
}