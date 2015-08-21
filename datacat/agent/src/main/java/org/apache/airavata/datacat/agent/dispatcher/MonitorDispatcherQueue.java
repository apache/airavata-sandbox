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

import org.apache.airavata.datacat.models.OutputMonitorMessage;

import java.util.LinkedList;

public class MonitorDispatcherQueue {
    public static MonitorDispatcherQueue instance;
    private LinkedList<OutputMonitorMessage> queue;

    private MonitorDispatcherQueue() {
        queue = new LinkedList<OutputMonitorMessage>();
    }

    /**
     * Returns a singleton dispatch queue object
     *
     * @return Dispatch queue object
     */
    public static MonitorDispatcherQueue getInstance() {
        if (instance == null) {
            instance = new MonitorDispatcherQueue();
        }
        return instance;
    }

    /**
     * Put a message into the dispatch queue
     *
     * @param outputMonitorMessage
     */
    public synchronized void addMsgToQueue(OutputMonitorMessage outputMonitorMessage) {
        queue.add(outputMonitorMessage);
        notifyAll();
    }

    /**
     * Returns a DirectoryUpdateMessage object from the dispatcher queue
     *
     * @return DirectoryUpdateMessage object
     */
    public synchronized OutputMonitorMessage getMsgFromQueue() {
        OutputMonitorMessage directoryUpdateMessage = queue.pollFirst();
        while (directoryUpdateMessage == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            directoryUpdateMessage = queue.pollFirst();
        }
        return directoryUpdateMessage;
    }

    /**
     * Returns dispatch queue size
     *
     * @return queue size
     */
    public long getQueueSize() {
        return queue.size();
    }

}
