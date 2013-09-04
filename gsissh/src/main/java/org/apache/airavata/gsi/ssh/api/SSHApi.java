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

package org.apache.airavata.gsi.ssh.api;

/**
 * User: AmilaJ (amilaj@apache.org)
 * Date: 8/14/13
 * Time: 3:12 PM
 */

import org.apache.airavata.gsi.ssh.api.job.JobDescriptor;
import org.apache.airavata.gsi.ssh.listener.JobSubmissionListener;

/**
 * An API to executed commands/jobs using GSI-SSH or SSH.
 */
public interface SSHApi {

    /**
     * Executes a given command.
     * @param commandInfo Encapsulated information about command. E.g :- executable name
     *                    parameters etc ...
     * @param serverInfo The SSHing server information.
     * @param authenticationInfo Security data needs to be communicated with remote server.
     * @param commandOutput The output of the command.
     */
    void executeCommand(CommandInfo commandInfo, ServerInfo serverInfo,
                                 AuthenticationInfo authenticationInfo,
                                 CommandOutput commandOutput) throws SSHApiException;

    /**
     *  can be used to submit a job with a given pbs file
     * @param serverInfo
     * @param authenticationInfo
     * @param pbsFilePath
     * @param jobDescriptor
     * @return The jobID
     * @throws SSHApiException
     */
    String submitAsyncJobWithPBS(ServerInfo serverInfo,
                               AuthenticationInfo authenticationInfo,
                               String pbsFilePath, JobDescriptor jobDescriptor) throws SSHApiException;

    /**
     * can be used to submit a job without giving a pbs, but providing job attributes
     * @param serverInfo
     * @param authenticationInfo
     * @param jobDescriptor
     * @return The jobID
     * @throws SSHApiException
     */

    String submitAsyncJob(ServerInfo serverInfo,
                               AuthenticationInfo authenticationInfo,
                               JobDescriptor jobDescriptor) throws SSHApiException;

    /**
     *
     * @param serverInfo
     * @param authenticationInfo
     * @return
     * @throws SSHApiException
     */
    Cluster getCluster(ServerInfo serverInfo, AuthenticationInfo authenticationInfo) throws SSHApiException;

    /**
     * @param serverInfo
     * @param authenticationInfo
     * @param jobID
     * @return
     * @throws SSHApiException
     */
    JobDescriptor getJobById(ServerInfo serverInfo, AuthenticationInfo authenticationInfo, String jobID) throws SSHApiException;

    /**
     *
     * @param serverInfo
     * @param authenticationInfo
     * @param jobDescriptor
     * @param listener
     * @return
     * @throws SSHApiException
     */
    String submitAsyncJob(ServerInfo serverInfo,
                               AuthenticationInfo authenticationInfo,
                               JobDescriptor jobDescriptor,JobSubmissionListener listener) throws SSHApiException;
}
