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

import com.jcraft.jsch.Session;
import org.apache.airavata.gsi.ssh.api.job.Job;
import org.apache.airavata.gsi.ssh.listener.JobSubmissionListener;

/**
 * This interface represents a Cluster machine
 * End users of the API can implement this and come up with their own
 * implementations, but mostly this interface is for internal usage.
 */
public interface Cluster {

    /**
     * This will submit a job to the cluster with a given pbs file and some parameters
     *
     * @param pbsFilePath
     * @param workingDirectory
     * @return
     * @throws SSHApiException
     */
    public String submitAsyncJobWithPBS(String pbsFilePath, String workingDirectory) throws SSHApiException;

    /**
     * This will submit the given job and not performing any monitoring
     *
     * @param jobDescriptor
     * @return
     * @throws SSHApiException
     */
    public String submitAsyncJob(Job jobDescriptor) throws SSHApiException;

    /**
     * This will get all the information about the cluster and store them as parameters
     * So that api user can extract required information about the cluster
     *
     * @return
     * @throws SSHApiException
     */
    public Cluster loadCluster() throws SSHApiException;

    /**
     * This will copy the lFile to rFile location in configured cluster
     *
     * @param rFile
     * @param lFile
     * @return
     * @throws SSHApiException
     */
    public void scpTo(String rFile, String lFile) throws SSHApiException;

    /**
     * submit a job and register the listener so that status changes will be triggers
     * and appropricate action implemented in the JobSubmissionListener will get invoked
     *
     * @param jobDescriptor
     * @param listener
     * @return
     * @throws SSHApiException
     */
    public String submitAsyncJob(Job jobDescriptor, JobSubmissionListener listener) throws SSHApiException;

    /**
     * @param jobID
     * @return
     * @throws SSHApiException
     */
    public Job getJobById(String jobID) throws SSHApiException;

    /**
     * This will delete the given job from the queue
     * @param jobID
     * @return
     * @throws SSHApiException
     */
    public Job cancelJob(String jobID) throws SSHApiException;
}
