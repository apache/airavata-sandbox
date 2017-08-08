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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HPCApplicationTest {
    private final static Logger logger = LoggerFactory.getLogger(HPCApplicationTest.class);

    @Test
    public void testTensorFlow() throws Exception {
        Map<String, String> inputs = new HashMap<>();
        inputs.put("code_tf.py", HPCApplicationTest.class.getClassLoader().getResource("code_tf.py").getPath());
        TensorFlow tensorflow = new TensorFlow("tensorflow", inputs);

        String routingKey = UUID.randomUUID().toString();
        HPCApplicationExecutor hpcApplicationExecutor = new HPCApplicationExecutor();
        String computerResource = "bigred2.uits.iu.edu";
        hpcApplicationExecutor.executeApplication(routingKey, tensorflow, computerResource,
                tensorflow.getJobSpecification(computerResource));
    }
}