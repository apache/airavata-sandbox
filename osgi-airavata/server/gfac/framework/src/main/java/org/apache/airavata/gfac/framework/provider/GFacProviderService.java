package org.apache.airavata.gfac.framework.provider;/*
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


import java.util.Map;

/**
 * User: AmilaJ (amilaj@apache.org)
 * Date: 7/14/13
 * Time: 12:05 PM
 */

/**
 * GFacProviderService Service injected into framework.
 */
public interface GFacProviderService {

    void initProperties(Map<String,String> properties) throws GFacProviderException;
    /**
     * Initialize environment required for invoking the execute method of the provider. If environment setup is
     * done during the in handler execution, validation of environment will go here.
     * @param jobExecutionContext containing job execution related information.
     * @throws GFacProviderException in case of a error initializing the environment.
     */
    void initialize(JobExecutionContext jobExecutionContext) throws GFacProviderException;

    /**
     * Invoke the providers intended functionality using information and data in job execution context.
     * @param jobExecutionContext containing job execution related information.
     * @throws GFacProviderException in case of a error executing the job.
     */
    void execute(JobExecutionContext jobExecutionContext) throws GFacProviderException;

    /**
     * Cleans up the acquired resources during initialization and execution of the job.
     * @param jobExecutionContext containing job execution related information.
     * @throws GFacProviderException in case of a error cleaning resources.
     */
    void dispose(JobExecutionContext jobExecutionContext) throws GFacProviderException;

    /**
     * Cancels all jobs relevant to an experiment.
     * @param experimentId The experiment id
     * @param jobExecutionContext The job execution context, contains runtime information.
     * @throws GFacProviderException If an error occurred while cancelling the job.
     */
    void cancelJob(String experimentId, JobExecutionContext jobExecutionContext) throws GFacProviderException;

    /**
     * Cancels all jobs relevant to a workflow in an experiment.
     * @param experimentId The experiment id
     * @param workflowId The workflow id.
     * @param jobExecutionContext The job execution context, contains runtime information.
     * @throws GFacProviderException If an error occurred while cancelling the job.
     */
    void cancelJob(String experimentId, String workflowId,
                   JobExecutionContext jobExecutionContext) throws GFacProviderException;

    /**
     * Cancels the job for a given a workflow id and node id in an experiment.
     * @param experimentId The experiment id.
     * @param workflowId The workflow id.
     * @param nodeId The node id.
     * @param jobExecutionContext The job execution context relevant to cancel job operation.
     * @throws GFacProviderException If an error occurred while cancelling the job.
     */
    void cancelJob(String experimentId, String workflowId, String nodeId,
                   JobExecutionContext jobExecutionContext) throws GFacProviderException;
}
