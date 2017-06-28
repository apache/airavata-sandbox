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

import edu.iu.helix.airavata.HelixUtil;
import edu.iu.helix.airavata.ZkUtils;
import org.apache.helix.task.Task;
import org.apache.helix.task.TaskCallbackContext;
import org.apache.helix.task.TaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class SSHTask implements Task {
    private final static Logger logger = LoggerFactory.getLogger(SSHTask.class);

    public static final String TASK_COMMAND = "SSH_TASK";
    private TaskCallbackContext callbackContext;
    private String taskId;

    public SSHTask(TaskCallbackContext callbackContext){
        this.callbackContext = callbackContext;
        this.taskId = callbackContext.getTaskConfig().getId();
    }

    @Override
    public TaskResult run() {
// Todo Should be stored before the DAG execution start
//        byte[] input = SerializationUtils.serialize(yourList);
//        curator.create()
//                .creatingParentContainersIfNeeded()
//                .forPath(path, input);

// Todo Deserialize the TaskContext from data store.
//        byte[] output = curator.getData().forPath(path);
//        List<String> newList = (List<String>)SerializationUtils.deserialize(output);

        System.out.println("Running SSH Task for ID: " + taskId);
        try {
            SSHTaskContext taskContext = (SSHTaskContext) ZkUtils.getZkData(ZkUtils.getCuratorClient(), HelixUtil.SSH_WORKFLOW, taskId);
            String routingKey = UUID.randomUUID().toString();
            SSHRunner sshExecutor = new SSHRunner();

            System.out.println("Task: " + taskId + ", is of Type: " + taskContext.getTask_type());
            switch (taskContext.getTask_type()) {

                case EXECUTE_COMMAND:
                    SSHCommandOutputReader sshOut = sshExecutor.executeCommand(routingKey, taskContext.getCommand(),
                            (SSHServerInfo) taskContext.getServerInfo(), taskContext.getSshKeyAuthentication());
                    System.out.println("SSH Command Output: " + sshOut.getStdOutputString());
                    break;

                case FILE_COPY:
                    String scpOut = sshExecutor.scpTo(routingKey, taskContext.getSourceFilePath(), taskContext.getDestFilePath(),
                            (SSHServerInfo) taskContext.getServerInfo(), taskContext.getSshKeyAuthentication());
                    System.out.println("SCP Command Output: " + scpOut);
                    break;

                default:
                    throw new Exception("Unknown SSH Task Type: " + taskContext.getTask_type());
            }
        } catch (Exception ex) {
            System.err.println("Something went wrong for task: " + taskId + ", reason: " + ex);
            return new TaskResult(TaskResult.Status.FAILED, "SSH command completed!");
        }
        return new TaskResult(TaskResult.Status.COMPLETED, "SSH command completed!");
    }

    @Override
    public void cancel() {

    }
}