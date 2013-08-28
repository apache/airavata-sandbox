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

import bsh.StringUtil;
import com.jcraft.jsch.*;
import org.apache.airavata.gsi.ssh.api.*;
import org.apache.airavata.gsi.ssh.api.job.JobDescriptor;
import org.apache.airavata.gsi.ssh.config.ConfigReader;
import org.apache.airavata.gsi.ssh.jsch.ExtendedJSch;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.xmlbeans.impl.store.Path;
import org.bouncycastle.crypto.prng.RandomGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.security.SecureRandom;

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

    public void submitAsyncJobWithPBS(ServerInfo serverInfo,
                                      AuthenticationInfo authenticationInfo,
                                      String pbsFilePath, JobDescriptor jobDescriptor) throws SSHApiException {
        try {

            SCPTo scpTo = new SCPTo(serverInfo, authenticationInfo, new ConfigReader());
            scpTo.scpTo(jobDescriptor.getWorkingDirectory(), pbsFilePath);

        } catch (JSchException e) {
            throw new SSHApiException("An exception occurred while connecting to server." +
                    "Connecting server - " + serverInfo.getHost() + ":" + serverInfo.getPort() +
                    " connecting user name - "
                    + serverInfo.getUserName(), e);
        } catch (IOException e) {
            throw new SSHApiException("An exception occurred while connecting to server." +
                    "Connecting server - " + serverInfo.getHost() + ":" + serverInfo.getPort() +
                    " connecting user name - "
                    + serverInfo.getUserName(), e);
        }

        // since this is a constant we do not ask users to fill this
        RawCommandInfo rawCommandInfo = new RawCommandInfo("/opt/torque/bin/qsub " +
                jobDescriptor.getWorkingDirectory() + File.separator + FilenameUtils.getName(pbsFilePath));

        this.executeCommand(rawCommandInfo, serverInfo, authenticationInfo, jobDescriptor.getCommandOutput());
    }

    public void submitAsyncJob(ServerInfo serverInfo, AuthenticationInfo authenticationInfo, JobDescriptor jobDescriptor) throws SSHApiException {
        TransformerFactory factory = TransformerFactory.newInstance();
        String xsltPath = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "PBSTemplate.xslt";
        Source xslt = new StreamSource(new File(xsltPath));
        Transformer transformer = null;
        StringWriter results = new StringWriter();

        try {
            // generate the pbs script using xslt
            transformer = factory.newTransformer(xslt);
            Source text = new StreamSource(new ByteArrayInputStream(jobDescriptor.toXML().getBytes()));
            transformer.transform(text, new StreamResult(results));

            System.out.println("***********************************");
            System.out.println(results.toString());
            System.out.println("***********************************");

            // creating a temporary file using pbs script generated above
            int number = new SecureRandom().nextInt();
            number = (number < 0 ? -number : number);

            File tempPBSFile = new File(Integer.toString(number) + ".pbs");
            FileUtils.writeStringToFile(tempPBSFile, results.toString());

            //reusing submitAsyncJobWithPBS method to submit a job

            this.submitAsyncJobWithPBS(serverInfo, authenticationInfo, tempPBSFile.getAbsolutePath(), jobDescriptor);
            tempPBSFile.delete();
        } catch (TransformerConfigurationException e) {
            throw new SSHApiException("Error parsing PBS transformation", e);
        } catch (TransformerException e) {
            throw new SSHApiException("Error generating PBS script", e);
        } catch (IOException e) {
            throw new SSHApiException("An exception occurred while connecting to server." +
                    "Connecting server - " + serverInfo.getHost() + ":" + serverInfo.getPort() +
                    " connecting user name - "
                    + serverInfo.getUserName(), e);
        }


    }
}
