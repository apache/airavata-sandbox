
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
import org.apache.airavata.datacat.parsers.IParser;
import org.apache.airavata.datacat.parsers.gridchem.inchi.InChI;
import org.apache.airavata.datacat.parsers.gridchem.inchi.InChIGenerator;
import org.apache.airavata.datacat.parsers.gridchem.util.Constants;
import org.apache.airavata.datacat.parsers.gridchem.util.GridChemProperties;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GridChemParser implements IParser {

    private final Logger logger = LogManager.getLogger(GridChemParser.class);
    private String[] gridChemQueueParserses;

    public GridChemParser() {
    }

    public OutputMetadataDTO parse(OutputMonitorMessage outputMonitorMessage) {
        gridChemQueueParserses = GridChemProperties.getInstance().getParserSequence();
        logger.info("Loaded the GridChem Parser Sequence ...");

        logger.info("Started passing new file monitor message...");

        OutputMetadataDTO outputMetadataDTO = new OutputMetadataDTO();

        //For Solr requirement date is in the format of yyyy-MM-ddTHH:mm:ssZ
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String date = dateFormat.format(now).split(" ")[0] + "T" + dateFormat.format(now).split(" ")[1] + "Z";

        //setting the basic file meta-data
        outputMetadataDTO.setCreatedDate(date);
        outputMetadataDTO.setHost(computingResourceName());
        outputMetadataDTO.setExperimentName(outputMonitorMessage.getExperimentName());
        outputMetadataDTO.setExperimentId(outputMonitorMessage.getExperimentID());
        outputMetadataDTO.setOutputPath(outputMonitorMessage.getOutputPath());
        outputMetadataDTO.setApplicationName(applicationName());
        outputMetadataDTO.setOwnerId(getOwnerId());

        String[] outPutFiles = (new File(outputMonitorMessage.getOutputPath())).list(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".out");
            }
        });

        Map<String, String> results = new HashMap<String, String>();

        if (outPutFiles != null && outPutFiles.length > 0) {

            // have a copy of the file in a temp location
            //FIXME: is this really necessary?
            String outFilePath = outputMonitorMessage.getOutputPath() + File.separator + outPutFiles[0];
            String uniqueID = UUID.randomUUID().toString();
            File source = new File(outFilePath);
            File dest = new File(GridChemProperties.getInstance().getProperty(
                    Constants.TMP_DIR, "") + "/" + uniqueID + ".out");
            try{
                FileUtils.copyFile(source, dest);
                try {

                    InChIGenerator inChIGenerator = new InChIGenerator(uniqueID);
                    InChI inChI = inChIGenerator.getInChI();
                    results.put("InChi", inChI.getInchi());
                    results.put("InChiKey", inChI.getInchiKey());

                    //running the parser queue serially
                    Map<String, String> parserResult;
                    for (int j = 0; j < gridChemQueueParserses.length; j++) {
                        try {
                            long time = System.currentTimeMillis();
                            //copying the file to the memory
                            //FIXME make this file shared rather than re-reading it.
                            FileReader fileReader = new FileReader(dest);
                            parserResult = loadAndParse(gridChemQueueParserses[j], fileReader);
                            results.putAll(parserResult);
                        } catch (Exception ex) {
                            logger.error(
                                    "Parser Failure in " + outputMetadataDTO.getOutputPath() + "/" + outputMetadataDTO.getOutputPath()
                                            + " in " + gridChemQueueParserses[j]
                            );
                        }
                    }
                } catch (Exception ex) {
                    logger.error(
                            "Parser Failure in " + outputMetadataDTO.getOutputPath() + "/" + outputMetadataDTO.getExperimentName()
                                    + " in InChi Generators"
                    );
                }
            }catch (IOException ex){
                logger.error("Cannot copy temp file to the tmp directory");
            }
            //adding the custom parsed data to the fileMetaDataObject
            outputMetadataDTO.setCustomMetaData(results);
            return outputMetadataDTO;
        }
        logger.info("No output file corresponding to the file monitor message location");
        return null;
    }

    private HashMap<String, String> loadAndParse(String classname, final FileReader reader) throws Exception {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        Class parser = null;
        try {
            //todo: handle unparsed documents and log and ignore
            parser = classLoader.loadClass(classname);
            Constructor parserConstructor = parser.getConstructor(FileReader.class);
            GridChemQueueParser queueParser = (GridChemQueueParser) parserConstructor.newInstance(reader);
            HashMap<String, String> parsedData = queueParser.getParsedData();
            return parsedData;
        } catch (ClassNotFoundException e) {
            logger.error("Could not load the class ..." + e.getMessage());
        } catch (NoSuchMethodException e) {
            logger.error("Could not load the constructor ... " + e.getMessage());
        } catch (InvocationTargetException e) {
            logger.error("Could not load the Class ... " + e.getMessage());
        } catch (InstantiationException e) {
            logger.error("Could not load the Class ... " + e.getMessage());
        } catch (IllegalAccessException e) {
            logger.error("Could not load the Class ... " + e.getMessage());
        }
        return null;
    }

    public String computingResourceName() {
        return GridChemProperties.getInstance().getProperty(Constants.ARCHIVING_NODE, "");
    }

    public String applicationName() {
        return GridChemProperties.getInstance().getProperty(Constants.APPLICATION_NAME, "");
    }

    public String getOwnerId() {
        return GridChemProperties.getInstance().getProperty(Constants.USERNAME, "");
    }
}