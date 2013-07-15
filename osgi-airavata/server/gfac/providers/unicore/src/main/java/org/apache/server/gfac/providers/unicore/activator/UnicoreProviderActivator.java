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

package org.apache.server.gfac.providers.unicore.activator;

import org.apache.airavata.gfac.framework.provider.GFacProviderService;
import org.apache.server.gfac.providers.unicore.service.UnicoreProvider;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.util.Hashtable;

/**
 * User: AmilaJ (amilaj@apache.org)
 * Date: 7/15/13
 * Time: 10:27 AM
 */

public class UnicoreProviderActivator implements BundleActivator {

    public void start(BundleContext bundleContext) throws Exception {
        Hashtable<String, String> properties = new Hashtable<String, String>();
        properties.put("Provider", "Unicore");

        bundleContext.registerService(
                GFacProviderService.class.getName(), new UnicoreProvider(), properties);

    }

    public void stop(BundleContext bundleContext) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
