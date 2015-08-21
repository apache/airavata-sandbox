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
package org.apache.airavata.datacat.agent.dispatcher;

import org.apache.airavata.datacat.agent.util.AgentProperties;
import org.apache.airavata.datacat.agent.util.Constants;
import org.apache.airavata.datacat.models.OutputMetadataDTO;
import org.apache.airavata.datacat.models.OutputMonitorMessage;
import org.apache.airavata.datacat.parsers.IParser;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MonitorDispatcher {

    private final Logger logger = LogManager.getLogger(MonitorDispatcher.class);


    private MonitorDispatcherQueue monitorDispatcherQueue = MonitorDispatcherQueue.getInstance();


    private MetadataDispatcherQueue metadataDispatcherQueue = MetadataDispatcherQueue.getInstance();


    private ExecutorService exec;


    private static MonitorDispatcher instance;


    private MonitorDispatcher() {
        init();
    }

    public static MonitorDispatcher getInstance(){
        if(MonitorDispatcher.instance==null){
            MonitorDispatcher.instance = new MonitorDispatcher();
        }

        return MonitorDispatcher.instance;
    }

    private void init() {
        try {
            exec = Executors.newFixedThreadPool(Integer.parseInt(AgentProperties.getInstance().getProperty(
                    Constants.MAX_PARSER_THREADS, "1000")));
        } catch (Exception e) {
            logger.error(e.toString());
            exec = Executors.newFixedThreadPool(100);
        }
    }

    /**
     * Starts the message dispatcher
     */
    public void startDispatcher() {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                OutputMonitorMessage directoryUpdateMessage = null;
                while (true) {
                    directoryUpdateMessage = monitorDispatcherQueue.getMsgFromQueue();
                    if (directoryUpdateMessage != null) {
                        try {
                            dispatch(directoryUpdateMessage);
                        } catch (Exception e) {
                            logger.error(e.toString());
                        }
                    }
                }
            }
        })).start();
    }

    /**
     * Dispatch single message
     *
     * @param outputMonitorMessage
     * @throws Exception
     */
    private void dispatch(final OutputMonitorMessage outputMonitorMessage) throws Exception {
        logger.info("Dispatching new message for experiment name: " + outputMonitorMessage.getExperimentName());
        exec.execute(new Runnable() {
            @Override
            public void run() {
                IParser parser = null;
                try {
                    Class c = this.getClass().getClassLoader()
                            .loadClass(AgentProperties.getInstance().getProperty(
                            Constants.PARSER_CLASS,""
                    ));
                    parser = (IParser)c.newInstance();
                    OutputMetadataDTO outputMetadataDTO = parser.parse(outputMonitorMessage);
                    metadataDispatcherQueue.addMetadataToQueue(outputMetadataDTO);
                    logger.info("Successfully parsed the metadata for experiment name:"+ outputMetadataDTO.getExperimentName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    logger.error("Cannot load Parser Class." +
                            " Please check PARSER_CLASS property in agent.properties.");
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    logger.error("Cannot load Parser Class." +
                            " Please check PARSER_CLASS property in agent.properties.");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    logger.error("Cannot load Parser Class." +
                            " Please check PARSER_CLASS property in agent.properties.");
                }
            }
        });
    }

}
