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
package org.apche.airavata.datacat.parsers.gridchem;

import junit.framework.Assert;
import org.apache.airavata.datacat.models.OutputMetadataDTO;
import org.apache.airavata.datacat.models.OutputMonitorMessage;
import org.apache.airavata.datacat.models.OutputMonitorMessageType;
import org.apache.airavata.datacat.parsers.gridchem.GridChemParser;
import org.apache.airavata.datacat.parsers.gridchem.util.Constants;
import org.apache.airavata.datacat.parsers.gridchem.util.GridChemProperties;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class GridChemParserTest {
    private Logger log = Logger.getLogger(GridChemParserTest.class);
    private String SAMPLE_DATA;

    private OutputMonitorMessage outputMonitorMessage;
    private GridChemParser parser;

    @Before
    public void setup() {
        //configuring the data root
        SAMPLE_DATA = GridChemProperties.getInstance().getProperty(Constants.SAMPLE_OUTPUT, "");

        parser = new GridChemParser();
        outputMonitorMessage = new OutputMonitorMessage();

        //creating a dummy file monitor message
        outputMonitorMessage.setExperimentName("2H2OOHNCmin.com.out");
        outputMonitorMessage.setOutputPath(SAMPLE_DATA);
        outputMonitorMessage.setFileMonitorMessageType(OutputMonitorMessageType.FILE_CREATED);
        log.info("File Monitor message initialized ...");
    }

    @Test
    public void testGCParserSingleOutput() {
        OutputMetadataDTO metadataDTO = parser.parse(outputMonitorMessage);
        Assert.assertNotNull(metadataDTO);
    }
}
