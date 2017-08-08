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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class HPCApplication {
    private final static Logger logger = LoggerFactory.getLogger(HPCApplication.class);

    private String applicationName;

    private Map<String, String> applicationInputs;

    public HPCApplication(String applicationName, Map<String, String> applicationInputs) {
        this.applicationName = applicationName;
        this.applicationInputs = applicationInputs;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Map<String, String> getApplicationInputs() {
        return applicationInputs;
    }

    public void setApplicationInputs(Map<String, String> applicationInputs) {
        this.applicationInputs = applicationInputs;
    }

    public GroovyMap getJobMap(String computeResource){return null;}
}