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

package org.apache.server.gfac.providers.unicore.service;

import org.apache.airavata.gfac.framework.provider.GFacProviderException;
import org.apache.airavata.gfac.framework.provider.GFacProviderService;
import org.apache.airavata.gfac.framework.provider.JobExecutionContext;

import java.util.Map;

/**
 * User: AmilaJ (amilaj@apache.org)
 * Date: 7/15/13
 * Time: 10:25 AM
 */

public class UnicoreProvider implements GFacProviderService {

    public void initProperties(Map<String, String> properties) throws GFacProviderException {
        System.out.println("In Unicore initProp");
    }

    public void initialize(JobExecutionContext jobExecutionContext) throws GFacProviderException {
        System.out.println("In Unicore init");
    }

    public void execute(JobExecutionContext jobExecutionContext) throws GFacProviderException {
        System.out.println("In Unicore execute");
    }

    public void dispose(JobExecutionContext jobExecutionContext) throws GFacProviderException {
        System.out.println("In Unicore dispose");
    }

    public void cancelJob(String experimentId, JobExecutionContext jobExecutionContext) throws GFacProviderException {
        System.out.println("In Unicore cancel");
    }

    public void cancelJob(String experimentId, String workflowId, JobExecutionContext jobExecutionContext) throws GFacProviderException {
        System.out.println("In Unicore cancel");
    }

    public void cancelJob(String experimentId, String workflowId, String nodeId, JobExecutionContext jobExecutionContext) throws GFacProviderException {
        System.out.println("In Unicore cancel");
    }
}
