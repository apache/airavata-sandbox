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
import org.apache.airavata.models.resources.hpc.GroovyMap;
import org.apache.airavata.models.resources.hpc.JobManagerConfiguration;
import org.apache.airavata.models.resources.hpc.PBSJobConfiguration;
import org.apache.airavata.models.resources.hpc.Script;
import org.apache.airavata.models.runners.ssh.SSHKeyAuthentication;
import org.apache.airavata.models.runners.ssh.SSHServerInfo;
import org.apache.airavata.runners.ssh.SSHRunnerTest;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class HPCBatchResourceTest {
    private final static Logger logger = LoggerFactory.getLogger(HPCBatchResourceTest.class);

    @Test
    public void testGenericResource() throws Exception {
        SSHKeyAuthentication br2SshAuthentication = new SSHKeyAuthentication(
                Constants.loginUserName,
                IOUtils.toByteArray(SSHRunnerTest.class.getClassLoader().getResourceAsStream("ssh/id_rsa")),
                IOUtils.toByteArray(SSHRunnerTest.class.getClassLoader().getResourceAsStream("ssh/id_rsa.pub")),
                "dummy",
                SSHRunnerTest.class.getClassLoader().getResource("ssh/known_hosts").getPath(),
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
        HPCBatchResource hpcBatchResource = new HPCBatchResource(br2, pbsJobConfiguration, br2SshAuthentication);


        String routingKey = UUID.randomUUID().toString();

        //Defining all the configurations
        hpcBatchResource.submitBatchJob(routingKey, getJobSpecification());
    }

    @Test
    public void testBR2() throws Exception {
        String routingKey = UUID.randomUUID().toString();

        //Defining only which resource to use and the implementation handles the details
        BigRed2 bigRed2 = new BigRed2();
        bigRed2.submitBatchJob(routingKey, getJobSpecification());
    }

    private GroovyMap getJobSpecification(){
        GroovyMap jobSpecification = new GroovyMap();

        jobSpecification.add(Script.NODES, 1);
        jobSpecification.add(Script.PROCESS_PER_NODE, 16);
        jobSpecification.add(Script.MAX_WALL_TIME, "00:30:00");

        jobSpecification.add(Script.QUEUE_NAME, "debug_gpu");

        jobSpecification.add(Script.MAIL_ADDRESS, "supun.nakandala@gmail.com");

        List<String> moduleLoads = new ArrayList<>();
        moduleLoads.add("module load ccm");
        moduleLoads.add("module load singularity");
        jobSpecification.add(Script.MODULE_COMMANDS, moduleLoads);

        jobSpecification.add(Script.WORKING_DIR, "/N/dc2/scratch/snakanda/work-dirs");
        List<String> inputs = new ArrayList<>();
        inputs.add("~/airavata/code_tf.py");
        jobSpecification.add(Script.INPUTS, inputs);
        jobSpecification.add(Script.EXECUTABLE_PATH, "singularity exec /N/soft/cle5/singularity/images/tensorflow1.1-ubuntu-py2.7.11-test.img python");
        jobSpecification.add(Script.JOB_SUBMITTER_COMMAND,"ccmrun");

        jobSpecification.add(Script.STANDARD_OUT_FILE, "STDOUT.txt");
        jobSpecification.add(Script.STANDARD_ERROR_FILE, "STDERR.txt");

        return jobSpecification;
    }
}