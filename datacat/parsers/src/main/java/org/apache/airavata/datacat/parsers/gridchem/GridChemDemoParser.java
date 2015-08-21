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
package org.apache.airavata.datacat.parsers.gridchem;

import org.apache.airavata.datacat.models.OutputMetadataDTO;
import org.apache.airavata.datacat.models.OutputMonitorMessage;
import org.apache.airavata.datacat.parsers.DefaultParser;
import org.apache.airavata.datacat.parsers.IParser;
import org.apache.airavata.datacat.parsers.gridchem.inchi.InChI;
import org.apache.airavata.datacat.parsers.gridchem.inchi.InChIGenerator;
import org.apache.airavata.datacat.parsers.gridchem.util.Constants;
import org.apache.airavata.datacat.parsers.gridchem.util.GridChemProperties;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class GridChemDemoParser extends DefaultParser implements IParser{
    private final static Logger logger = LoggerFactory.getLogger(GridChemDemoParser.class);

    @Override
    public OutputMetadataDTO parse(OutputMonitorMessage outputMonitorMessage) {
        OutputMetadataDTO outputMetadataDTO = super.parse(outputMonitorMessage);
        HashMap<String, String> customMetadata = new HashMap<>();

        String[] outPutFiles = (new File(outputMonitorMessage.getOutputPath())).list(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".out");
            }
        });
        if (outPutFiles != null && outPutFiles.length > 0) {
            String outFilePath = outputMonitorMessage.getOutputPath() + File.separator + outPutFiles[0];
            String uniqueID = UUID.randomUUID().toString();
            File source = new File(outFilePath);
            File dest = new File(GridChemProperties.getInstance().getProperty(
                    Constants.TMP_DIR, "") + "/" + uniqueID + ".out");
            try {
                FileUtils.copyFile(source, dest);
                try {
                    InChIGenerator inChIGenerator = new InChIGenerator(uniqueID);
                    InChI inChI = inChIGenerator.getInChI();
                    customMetadata.put("inChi", inChI.getInchi());
                    customMetadata.put("inChiKey", inChI.getInchiKey());
                } catch (Exception ex) {
                    logger.error(
                            "Parser Failure in " + outputMetadataDTO.getOutputPath() + "/" + outputMetadataDTO.getExperimentName()
                                    + " in InChi Generators"
                    );
                }
            } catch (IOException ex) {
                logger.error("Cannot copy temp file to the tmp directory");
            }
        }
        outputMetadataDTO.setCustomMetaData(customMetadata);

        return outputMetadataDTO;
    }
}