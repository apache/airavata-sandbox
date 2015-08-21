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
package org.apache.airavata.datacat.parsers;

import org.apache.airavata.datacat.models.OutputMetadataDTO;
import org.apache.airavata.datacat.models.OutputMonitorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * This is a test parser to to work with PHP Reference Gateway.
 */
public class DefaultParser implements IParser {
    private final static Logger logger = LoggerFactory.getLogger(DefaultParser.class);

    @Override
    public OutputMetadataDTO parse(OutputMonitorMessage outputMonitorMessage) {
        //only a sample set of metadata is set.
        OutputMetadataDTO outputMetadataDTO = new OutputMetadataDTO();
        outputMetadataDTO.setExperimentId(outputMonitorMessage.getExperimentID());
        outputMetadataDTO.setExperimentName(outputMonitorMessage.getExperimentName());
        outputMetadataDTO.setOutputPath(outputMonitorMessage.getOutputPath());
        outputMetadataDTO.setOwnerId(outputMonitorMessage.getOwnerId());
        outputMetadataDTO.setGatewayId(outputMonitorMessage.getGatewayId());
        outputMetadataDTO.setApplicationName(outputMonitorMessage.getApplicationName());
        outputMetadataDTO.setHost(outputMonitorMessage.getHost());

        //For Solr requirement date is in the format of yyyy-MM-ddTHH:mm:ssZ
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        //rounding off the day
        String date = dateFormat.format(now).split(" ")[0] + "T" + dateFormat.format(now).split(" ")[1] + "Z/DAY";
        outputMetadataDTO.setCreatedDate(date);

        HashMap<String, String> customMetadata = new HashMap<>();
        outputMetadataDTO.setCustomMetaData(customMetadata);

        return outputMetadataDTO;
    }
}