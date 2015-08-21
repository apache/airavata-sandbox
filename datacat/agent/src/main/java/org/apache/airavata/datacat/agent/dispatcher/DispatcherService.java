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


import org.apache.airavata.datacat.models.OutputMetadataDTO;
import org.apache.airavata.datacat.models.OutputMonitorMessage;

public class DispatcherService {
    private MetadataDispatcher metadataDispatcher;
    private MetadataDispatcherQueue metadataDispatcherQueue;
    private MonitorDispatcher monitorDispatcher;
    private MonitorDispatcherQueue monitorDispatcherQueue;

    private static DispatcherService instance;

    private DispatcherService() throws Exception {
        this.metadataDispatcher = MetadataDispatcher.getInstance();
        this.metadataDispatcherQueue = MetadataDispatcherQueue.getInstance();
        this.monitorDispatcher = MonitorDispatcher.getInstance();
        this.monitorDispatcherQueue = MonitorDispatcherQueue.getInstance();
    }

    public static DispatcherService getInstance() throws Exception {
        if(DispatcherService.instance==null){
            DispatcherService.instance = new DispatcherService();
        }

        return DispatcherService.instance;
    }

    public void startDispatcher() {
        metadataDispatcher.startDispatcher();
        monitorDispatcher.startDispatcher();
    }

    public void stopService() {
    }

    public void addFileMetadata(OutputMetadataDTO outputMetadataDTO){
        this.metadataDispatcherQueue.addMetadataToQueue(outputMetadataDTO);
    }

    public void addFileMonitorMessage(OutputMonitorMessage outputMonitorMessage){
        this.monitorDispatcherQueue.addMsgToQueue(outputMonitorMessage);
    }

}
