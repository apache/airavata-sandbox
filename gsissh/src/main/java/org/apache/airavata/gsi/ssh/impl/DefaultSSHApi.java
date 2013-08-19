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

package org.apache.airavata.gsi.ssh.impl;

import com.jcraft.jsch.*;
import org.apache.airavata.gsi.ssh.api.*;
import org.apache.airavata.gsi.ssh.config.ConfigReader;
import org.apache.airavata.gsi.ssh.jsch.ExtendedJSch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * User: AmilaJ (amilaj@apache.org)
 * Date: 8/14/13
 * Time: 5:18 PM
 */

/**
 * Default SSH API implementation.
 */
public class DefaultSSHApi implements SSHApi {

    private static final Logger log = LoggerFactory.getLogger(DefaultSSHApi.class);


    private ConfigReader configReader;

    /**
     * Initializes default SSH API. During initialization basic configurations
     * are read.
     *
     * @throws SSHApiException If an error occurred while reading basic configurations.
     */
    public DefaultSSHApi() throws SSHApiException {
        try {
            this.configReader = new ConfigReader();
        } catch (IOException e) {
            throw new SSHApiException("Unable to load system configurations.", e);
        }
    }

    public void executeCommand(CommandInfo commandInfo, ServerInfo serverInfo,
                               AuthenticationInfo authenticationInfo,
                               CommandOutput commandOutput) throws SSHApiException {

        JSch jsch = new ExtendedJSch();

        log.info("Connecting to server - " + serverInfo.getHost() + ":" + serverInfo.getPort() + " with user name - "
                + serverInfo.getUserName());

        Session session = null;

        try {
            session = jsch.getSession(serverInfo.getUserName(), serverInfo.getHost(), serverInfo.getPort());
        } catch (JSchException e) {
            throw new SSHApiException("An exception occurred while creating SSH session." +
                    "Connecting server - " + serverInfo.getHost() + ":" + serverInfo.getPort() +
                    " connecting user name - "
                    + serverInfo.getUserName(), e);
        }

        java.util.Properties config = this.configReader.getProperties();
        session.setConfig(config);

        // Not a good way, but we dont have any choice
        if (session instanceof ExtendedSession) {
            ((ExtendedSession) session).setAuthenticationInfo(authenticationInfo);
        }

        try {
            session.connect();
        } catch (JSchException e) {
            throw new SSHApiException("An exception occurred while connecting to server." +
                    "Connecting server - " + serverInfo.getHost() + ":" + serverInfo.getPort() +
                    " connecting user name - "
                    + serverInfo.getUserName(), e);
        }

        String command = commandInfo.getCommand();

        Channel channel = null;
        try {
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
        } catch (JSchException e) {
            session.disconnect();

            throw new SSHApiException("Unable to execute command - " + command +
                    " on server - " + serverInfo.getHost() + ":" + serverInfo.getPort() +
                    " connecting user name - "
                    + serverInfo.getUserName(), e);
        }


        channel.setInputStream(null);
        ((ChannelExec) channel).setErrStream(commandOutput.getStandardError());

        try {
            channel.connect();
        } catch (JSchException e) {

            channel.disconnect();
            session.disconnect();

            throw new SSHApiException("Unable to retrieve command output. Command - " + command +
                    " on server - " + serverInfo.getHost() + ":" + serverInfo.getPort() +
                    " connecting user name - "
                    + serverInfo.getUserName(), e);
        }

        commandOutput.onOutput(channel);

        channel.disconnect();
        session.disconnect();


    }

    public void submitJob(CommandInfo commandInfo, ServerInfo serverInfo,
                          AuthenticationInfo authenticationInfo,
                          CommandOutput commandOutput, String pbsFilePath, String workingDirectory) throws  SSHApiException {
        try {
            SCPTo scpTo = new SCPTo(serverInfo, authenticationInfo, new ConfigReader());
            scpTo.scpTo(workingDirectory, pbsFilePath);
        } catch (JSchException e) {
            throw new SSHApiException("An exception occurred while connecting to server." +
                    "Connecting server - " + serverInfo.getHost() + ":" + serverInfo.getPort() +
                    " connecting user name - "
                    + serverInfo.getUserName(), e);
        }  catch (IOException e){
           throw new SSHApiException("An exception occurred while connecting to server." +
                    "Connecting server - " + serverInfo.getHost() + ":" + serverInfo.getPort() +
                    " connecting user name - "
                    + serverInfo.getUserName(), e);
        }

        this.executeCommand(commandInfo,serverInfo,authenticationInfo,commandOutput);
    }

}
