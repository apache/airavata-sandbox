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
package org.apache.airavata.applications.hpc;

import org.apache.airavata.models.resources.hpc.GroovyMap;
import org.apache.airavata.models.resources.hpc.Script;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TensorFlow extends HPCApplication {
    private final static Logger logger = LoggerFactory.getLogger(TensorFlow.class);

    public TensorFlow(String applicationName, Map<String, String> applicationInputs) {
        super(applicationName, applicationInputs);
    }

    public GroovyMap getJobSpecification(String computeResource){
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
        List<java.lang.String> inputs = new ArrayList<>();
        inputs.add("~/airavata/code_tf.py");
        jobSpecification.add(Script.INPUTS, inputs);
        jobSpecification.add(Script.EXECUTABLE_PATH, "singularity exec /N/soft/cle5/singularity/images/tensorflow1.1-ubuntu-py2.7.11-test.img python");
        jobSpecification.add(Script.JOB_SUBMITTER_COMMAND,"ccmrun");

        jobSpecification.add(Script.STANDARD_OUT_FILE, "STDOUT.txt");
        jobSpecification.add(Script.STANDARD_ERROR_FILE, "STDERR.txt");

        return jobSpecification;
    }
}