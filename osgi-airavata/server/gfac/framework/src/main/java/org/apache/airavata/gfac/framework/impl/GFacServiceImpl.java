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

package org.apache.airavata.gfac.framework.impl;

import org.apache.airavata.gfac.framework.provider.GFacProviderService;
import org.apache.airavata.gfac.framework.service.GFacService;
import org.apache.airavata.gfac.framework.service.JobInfo;

import java.util.concurrent.ConcurrentHashMap;

/**
 * User: AmilaJ (amilaj@apache.org)
 * Date: 7/14/13
 * Time: 1:26 PM
 */

public class GFacServiceImpl implements GFacService {

    private ConcurrentHashMap<String, GFacProviderService> gfacProviders
            = new ConcurrentHashMap<String, GFacProviderService>();

    public void submitJob(JobInfo jobInfo) {
        System.out.println("Submitting job ........");

        // TODO invoke in handlers

        // TODO invoke provider

        // TODO invoke out handlers

    }

    public void addGFacProvider(String name, GFacProviderService gFacProviderService) {
        gfacProviders.put(name, gFacProviderService);
    }

    public void removeGFacProvider(String name) {
        gfacProviders.remove(name);
    }
}
