/**
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
 */
package org.apache.airavata.k8s.api.resources.process;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class ProcessStatusResource {

    private long id;
    private int state;
    private String stateStr;
    private long timeOfStateChange;
    private String reason;
    private long processId;

    public long getId() {
        return id;
    }

    public ProcessStatusResource setId(long id) {
        this.id = id;
        return this;
    }

    public int getState() {
        return state;
    }

    public ProcessStatusResource setState(int state) {
        this.state = state;
        return this;
    }

    public long getTimeOfStateChange() {
        return timeOfStateChange;
    }

    public ProcessStatusResource setTimeOfStateChange(long timeOfStateChange) {
        this.timeOfStateChange = timeOfStateChange;
        return this;
    }

    public String getReason() {
        return reason;
    }

    public ProcessStatusResource setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public long getProcessId() {
        return processId;
    }

    public ProcessStatusResource setProcessId(long processId) {
        this.processId = processId;
        return this;
    }

    public String getStateStr() {
        return stateStr;
    }

    public ProcessStatusResource setStateStr(String stateStr) {
        this.stateStr = stateStr;
        return this;
    }

    public static enum State {

        CREATED(0),
        VALIDATED(1),
        STARTED(2),
        PRE_PROCESSING(3),
        CONFIGURING_WORKSPACE(4),
        INPUT_DATA_STAGING(5),
        EXECUTING(6),
        MONITORING(7),
        OUTPUT_DATA_STAGING(8),
        POST_PROCESSING(9),
        COMPLETED(10),
        FAILED(11),
        CANCELLING(12),
        CANCELED(13),
        ABORTED(14),
        STOPPED(15),
        NOT_STARTED(16);

        private final int value;

        private State(int value) {
            this.value = value;
        }

        private static Map<Integer, State> map = new HashMap<>();

        static {
            for (State state : State.values()) {
                map.put(state.value, state);
            }
        }

        public static State valueOf(int taskState) {
            return map.get(taskState);
        }

        /**
         * Get the integer value of this enum value, as defined in the Thrift IDL.
         */
        public int getValue() {
            return value;
        }
    }
}
