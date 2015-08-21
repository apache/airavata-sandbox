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
package org.apache.airavata.datacat.agent;

import org.apache.airavata.datacat.agent.dispatcher.DispatcherService;
import org.apache.airavata.datacat.agent.messageBroker.AiravataUpdateListener;
import org.apache.airavata.datacat.agent.messageBroker.IMessageBroker;
import org.apache.airavata.datacat.agent.monitor.IMonitor;
import org.apache.airavata.datacat.agent.util.AgentProperties;
import org.apache.airavata.datacat.agent.util.Constants;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.nio.file.Paths;

public class DataCatAgent {
    private static final Logger logger = LogManager.getLogger(DataCatAgent.class);
    private IMessageBroker messageBroker;
    private DispatcherService dispatcherService;
    private IMonitor iMonitor;

    private String monitorType = AgentProperties.getInstance().getProperty(Constants.MONITOR_TYPE,"");

    public DataCatAgent() throws Exception {
        dispatcherService = DispatcherService.getInstance();
        if(monitorType.equals("FILE_SYSTEM")){
            iMonitor = new org.apache.airavata.datacat.agent.monitor.impl.LocalFileSystemMonitor();
        }else{
            messageBroker = new AiravataUpdateListener();
        }
    }

    public static void main(String[] args) throws Exception {

        final DataCatAgent dataCatAgent = new DataCatAgent();
        dataCatAgent.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                logger.info("ShutDown called...");
                dataCatAgent.stop();
            }
        });
    }

    public void start() throws Exception {
        logger.info("\nStarting DataCat Agent...!\n");
        dispatcherService.startDispatcher();
        if(monitorType.equals("FILE_SYSTEM")){
            String path = AgentProperties.getInstance().getProperty(Constants.DATA_ROOT,"");
            iMonitor.startMonitor(Paths.get(path));
        }else{
            messageBroker.startBroker();
        }
    }

    public void stop() {
        dispatcherService.stopService();
        if(monitorType.equals("FILE_SYSTEM")){
            iMonitor.stopMonitor();
        }else{
            messageBroker.stopBroker();
        }
        logger.info("\nGood bye from DataCat agent...!\n");
    }
}
