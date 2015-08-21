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
package org.apache.airavata.datacat.agent.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;

public class AgentProperties {

    public static final String AGENT_PROPERTY_FILE = "../conf/agent.properties";
    public static final String DEFAULT_AGENT_PROPERTY_FILE = "conf/agent.properties";

    private static AgentProperties instance;

    private final Logger logger = LogManager.getLogger(AgentProperties.class);
    private java.util.Properties properties = null;

    private AgentProperties() {
        try {
            InputStream fileInput;
            if (new File(AGENT_PROPERTY_FILE).exists()) {
                fileInput = new FileInputStream(AGENT_PROPERTY_FILE);
                logger.info("Using configured agent property (agent.properties) file");
            } else {
                logger.info("Using default agent property (agent.properties) file");
                fileInput = ClassLoader.getSystemResource(DEFAULT_AGENT_PROPERTY_FILE).openStream();
            }
            java.util.Properties properties = new java.util.Properties();
            properties.load(fileInput);
            fileInput.close();
            this.properties = properties;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static AgentProperties getInstance() {
        if (instance == null) {
            instance = new AgentProperties();
        }
        return instance;
    }

    public String getProperty(String key, String defaultVal) {
        String val = this.properties.getProperty(key);

        if (val.isEmpty() || val == "") {
            return defaultVal;
        } else {
            return val;
        }
    }
}
