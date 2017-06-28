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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class SSHTaskContext implements Serializable {
    private final static Logger logger = LoggerFactory.getLogger(SSHTaskContext.class);

    public static enum TASK_TYPE {FILE_COPY, EXECUTE_COMMAND;}

    private TASK_TYPE task_type;
    private SSHKeyAuthentication sshKeyAuthentication;
    private SSHUserInfo sshUserInfo;
    private ServerInfo serverInfo;

    private String sourceFilePath;
    private String destFilePath;

    private String command;

    /**
     * Constructor to create SSHTaskContext for File Copy type
     * @param task_type
     * @param sshKeyAuthentication
     * @param sshUserInfo
     * @param serverInfo
     * @param sourceFilePath
     * @param destFilePath
     */
    public SSHTaskContext(TASK_TYPE task_type, SSHKeyAuthentication sshKeyAuthentication, SSHUserInfo sshUserInfo, ServerInfo serverInfo, String sourceFilePath, String destFilePath) {
        this.task_type = task_type;
        this.sshKeyAuthentication = sshKeyAuthentication;
        this.sshUserInfo = sshUserInfo;
        this.serverInfo = serverInfo;
        this.sourceFilePath = sourceFilePath;
        this.destFilePath = destFilePath;
    }

    /**
     * Constructor to crete SSHTaskContext for Command Execute type
     * @param task_type
     * @param sshKeyAuthentication
     * @param sshUserInfo
     * @param serverInfo
     * @param command
     */
    public SSHTaskContext(TASK_TYPE task_type, SSHKeyAuthentication sshKeyAuthentication, SSHUserInfo sshUserInfo, ServerInfo serverInfo, String command) {
        this.task_type = task_type;
        this.sshKeyAuthentication = sshKeyAuthentication;
        this.sshUserInfo = sshUserInfo;
        this.serverInfo = serverInfo;
        this.command = command;
    }

    public TASK_TYPE getTask_type() {
        return task_type;
    }

    public void setTask_type(TASK_TYPE task_type) {
        this.task_type = task_type;
    }

    public SSHKeyAuthentication getSshKeyAuthentication() {
        return sshKeyAuthentication;
    }

    public void setSshKeyAuthentication(SSHKeyAuthentication sshKeyAuthentication) {
        this.sshKeyAuthentication = sshKeyAuthentication;
    }

    public SSHUserInfo getSshUserInfo() {
        return sshUserInfo;
    }

    public void setSshUserInfo(SSHUserInfo sshUserInfo) {
        this.sshUserInfo = sshUserInfo;
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    public String getSourceFilePath() {
        return sourceFilePath;
    }

    public void setSourceFilePath(String sourceFilePath) {
        this.sourceFilePath = sourceFilePath;
    }

    public String getDestFilePath() {
        return destFilePath;
    }

    public void setDestFilePath(String destFilePath) {
        this.destFilePath = destFilePath;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return "SSHTaskContext{" +
                "task_type=" + task_type +
                ", sshKeyAuthentication=" + sshKeyAuthentication +
                ", sshUserInfo=" + sshUserInfo +
                ", serverInfo=" + serverInfo +
                ", sourceFilePath='" + sourceFilePath + '\'' +
                ", destFilePath='" + destFilePath + '\'' +
                ", command='" + command + '\'' +
                '}';
    }
}