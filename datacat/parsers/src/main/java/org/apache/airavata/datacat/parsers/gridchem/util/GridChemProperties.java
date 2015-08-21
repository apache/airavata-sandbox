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
package org.apache.airavata.datacat.parsers.gridchem.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;

public class GridChemProperties {
    public static final String GRIDCHEM_PARSERS_PROPERTIES = "../conf/gridchem.properties";
    public static final String DEFAULT_GRIDCHEM_PARSERS_PROPERTIES = "gridchem.properties";

    private static GridChemProperties instance;

    private final Logger logger = LogManager.getLogger(GridChemProperties.class);
    private java.util.Properties properties = null;

    private GridChemProperties() {
        try {
            InputStream fileInput;
            if (new File(GRIDCHEM_PARSERS_PROPERTIES).exists()) {
                fileInput = new FileInputStream(GRIDCHEM_PARSERS_PROPERTIES);
            } else {
                fileInput = ClassLoader.getSystemResource(DEFAULT_GRIDCHEM_PARSERS_PROPERTIES).openStream();
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

    public static GridChemProperties getInstance() {
        if (instance == null) {
            instance = new GridChemProperties();
        }
        return instance;
    }

    public String[] getParserSequence() {
        String parsers = getProperty(Constants.PARSER_SEQUENCE, "");
        String[] parserArray = parsers.split(",");
        return parserArray;
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
