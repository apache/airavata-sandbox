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
package edu.iu.helix.airavata.tasks.ssh;

import org.apache.helix.task.Task;
import org.apache.helix.task.TaskCallbackContext;
import org.apache.helix.task.TaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SSHTask implements Task {
    private final static Logger logger = LoggerFactory.getLogger(SSHTask.class);

    private TaskCallbackContext callbackContext;

    public SSHTask(TaskCallbackContext callbackContext){
        this.callbackContext = callbackContext;
    }

    @Override
    public TaskResult run() {
        return null;
    }

    @Override
    public void cancel() {

    }
}