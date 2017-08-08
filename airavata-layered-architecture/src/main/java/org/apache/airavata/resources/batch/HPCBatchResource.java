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
package org.apache.airavata.resources.batch;

import com.jcraft.jsch.JSchException;
import groovy.lang.*;
import groovy.text.GStringTemplateEngine;
import groovy.text.TemplateEngine;
import org.apache.airavata.models.resources.Authentication;
import org.apache.airavata.models.resources.JobSubmissionOutput;
import org.apache.airavata.models.resources.ServerInfo;
import org.apache.airavata.models.resources.hpc.*;
import org.apache.airavata.models.resources.hpc.Script;
import org.apache.airavata.models.runners.ssh.SSHKeyAuthentication;
import org.apache.airavata.models.runners.ssh.SSHServerInfo;
import org.apache.airavata.runners.ssh.SSHCommandOutputReader;
import org.apache.airavata.runners.ssh.SSHRunner;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class HPCBatchResource {
    private final static Logger logger = LoggerFactory.getLogger(HPCBatchResource.class);

    private static final int MAX_RETRY_COUNT = 3;

    protected ServerInfo serverInfo;
    protected Authentication authentication;
    protected JobManagerConfiguration jobManagerConfiguration;
    protected OutputParser outputParser;

    public HPCBatchResource(ServerInfo serverInfo, JobManagerConfiguration jobManagerConfiguration, Authentication
            authentication) throws Exception {
        if(!(serverInfo instanceof SSHServerInfo) || !(authentication instanceof SSHKeyAuthentication)){
            throw new Exception("Currently only SSH based communication is enabled for HPCRemote clusters");
        }
        if(!(jobManagerConfiguration instanceof PBSJobConfiguration)){
            throw new Exception("Currently only PBS job configuration is enabled");
        }

        this.serverInfo = (SSHServerInfo) serverInfo;
        this.authentication = (SSHKeyAuthentication) authentication;
        this.jobManagerConfiguration = jobManagerConfiguration;
        this.outputParser = jobManagerConfiguration.getParser();
    }

    public HPCBatchResource() {}

    public JobSubmissionOutput submitBatchJob(String routingKey, GroovyMap groovyMap) throws Exception {
        File tempJobFile = File.createTempFile("temp_job", jobManagerConfiguration.getScriptExtension());
        FileUtils.writeStringToFile(tempJobFile, generateScript(groovyMap));
        String workingDirectory = groovyMap.get(Script.WORKING_DIR).toString();

        String jobScriptFilePath = tempJobFile.getPath();
        JobSubmissionOutput jsoutput = new JobSubmissionOutput();
        copyTo(routingKey, jobScriptFilePath, workingDirectory+"/demo_job"
                + jobManagerConfiguration.getScriptExtension()); // scp script file to working directory
        RawCommandInfo submitCommand = jobManagerConfiguration.getSubmitCommand(workingDirectory, workingDirectory+"/demo_job"
                + jobManagerConfiguration.getScriptExtension());
        submitCommand.setRawCommand("cd " + workingDirectory + " && " + submitCommand.getRawCommand());
        SSHCommandOutputReader reader = executeCommand(routingKey, submitCommand);

        jsoutput.setJobId(outputParser.parseJobSubmission(reader.getStdOutputString()));
        if (jsoutput.getJobId() == null) {
            if (outputParser.isJobSubmissionFailed(reader.getStdOutputString())) {
                jsoutput.setJobSubmissionFailed(true);
                jsoutput.setFailureReason("stdout : " + reader.getStdOutputString() +
                        "\n stderr : " + reader.getStdErrorString());
            }
        }
        jsoutput.setExitCode(reader.getExitCode());
        if (jsoutput.getExitCode() != 0) {
            jsoutput.setJobSubmissionFailed(true);
            jsoutput.setFailureReason("stdout : " + reader.getStdOutputString() +
                    "\n stderr : " + reader.getStdErrorString());
        }
        jsoutput.setStdOut(reader.getStdOutputString());
        jsoutput.setStdErr(reader.getStdErrorString());
        return jsoutput;
    }

    public void copyTo(String routingKey, String localFile, String remoteFile) throws Exception {
        int retry = 3;
        while (retry > 0) {
            try {
                SSHRunner sshRunner = new SSHRunner();
                logger.info("Transferring localhost:" + localFile  + " to " + serverInfo.getHost() + ":" + remoteFile);
                sshRunner.scpTo("", localFile, remoteFile,  (SSHServerInfo) serverInfo, (SSHKeyAuthentication) authentication);
                retry = 0;
            } catch (Exception e) {
                retry--;
                if (retry == 0) {
                    throw new Exception("Failed to scp localhost:" + localFile + " to " + serverInfo.getHost() +
                            ":" + remoteFile, e);
                } else {
                    logger.info("Retry transfer localhost:" + localFile + " to " + serverInfo.getHost() + ":" +
                            remoteFile);
                }
            }
        }
    }

    public void copyFrom(String routingKey, String remoteFile, String localFile) throws Exception {
        int retry = 0;
        while(retry < MAX_RETRY_COUNT) {
            try {
                logger.info("Transferring " + serverInfo.getHost() + ":" + remoteFile + " To localhost:" + localFile);
                SSHRunner sshRunner = new SSHRunner();
                sshRunner.scpFrom(routingKey, remoteFile, localFile, (SSHServerInfo) serverInfo, (SSHKeyAuthentication)authentication);
                retry=0;
            } catch (Exception e) {
                retry++;
                if (retry == 0) {
                    throw new Exception("Failed to scp " + serverInfo.getHost() + ":" + remoteFile + " to " +
                            "localhost:" + localFile, e);
                } else {
                    logger.info("Retry transfer " + serverInfo.getHost() + ":" + remoteFile + "  to localhost:" + localFile);
                }
            }
        }
    }

    public void makeDirectory(String routingKey, String directoryPath) throws Exception {
        int retryCount = 0;
        try {
            while (retryCount < MAX_RETRY_COUNT) {
                retryCount++;
                logger.info("Creating directory: " + serverInfo.getHost() + ":" + directoryPath);
                try {
                    SSHRunner sshRunner = new SSHRunner();
                    sshRunner.makeDirectory(routingKey, directoryPath, (SSHServerInfo) serverInfo, (SSHKeyAuthentication)authentication);
                    break;  // Exit while loop
                } catch (JSchException e) {
                    if (retryCount == MAX_RETRY_COUNT) {
                        logger.error("Retry count " + MAX_RETRY_COUNT + " exceeded for creating directory: "
                                + serverInfo.getHost() + ":" + directoryPath, e);

                        throw e;
                    }
                    logger.error("Issue with jsch, Retry creating directory: " + serverInfo.getHost() + ":" + directoryPath);
                }
            }
        } catch (JSchException | IOException e) {
            throw new Exception("Failed to create directory " + serverInfo.getHost() + ":" + directoryPath, e);
        }
    }

    private SSHCommandOutputReader executeCommand(String routingKey, RawCommandInfo commandInfo) throws Exception {
        String command = commandInfo.getCommand();
        int retryCount = 0;
        try {
            while (retryCount < MAX_RETRY_COUNT) {
                retryCount++;

                SSHRunner sshRunner = new SSHRunner();
                SSHCommandOutputReader commandOutput = sshRunner.executeCommand(routingKey, command, (SSHServerInfo)serverInfo, (SSHKeyAuthentication)authentication);
                logger.info("Executing command {}", commandInfo.getCommand());
                return commandOutput;
            }
            throw new Exception("Unable to execute command after "+retryCount+" retry attempts - " + command);
        } catch (JSchException e) {
            throw new Exception("Unable to execute command - " + command, e);
        }
    }


    private String generateScript(GroovyMap groovyMap) throws Exception {
        URL templateUrl = new URL("file://"+jobManagerConfiguration.getJobDescriptionTemplateName());
        if (templateUrl == null) {
            String error = "Template file '" + jobManagerConfiguration.getJobDescriptionTemplateName() + "' not found";
            throw new Exception(error);
        }
        File template = new File(templateUrl.getPath());
        TemplateEngine engine = new GStringTemplateEngine();
        Writable make;
        try {
            make = engine.createTemplate(template).make(groovyMap);
        } catch (Exception e) {
            throw new Exception("Error while generating script using groovy map");
        }
        return make.toString();
    }
}