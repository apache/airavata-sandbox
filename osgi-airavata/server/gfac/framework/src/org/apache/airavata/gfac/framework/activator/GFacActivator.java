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

package org.apache.airavata.gfac.framework.activator;

import org.apache.airavata.gfac.framework.impl.GFacServiceImpl;
import org.apache.airavata.gfac.framework.provider.GFacProviderService;
import org.apache.airavata.gfac.framework.service.GFacService;
import org.osgi.framework.*;

/**
 * User: AmilaJ (amilaj@apache.org)
 * Date: 7/14/13
 * Time: 12:15 PM
 */

public class GFacActivator implements BundleActivator, ServiceListener
{
    // Bundle's context.
    private BundleContext bundleContext = null;
    // The service reference being used.
    private ServiceReference serviceReference = null;

    private GFacServiceImpl gFacServiceImpl = new GFacServiceImpl();

    /**
     * Implements BundleActivator.start(). Prints
     * a message and adds itself to the bundle context as a service
     * listener.
     * @param context the framework context for the bundle.
     **/
    public void start(BundleContext context) throws InvalidSyntaxException {
        System.out.println("Starting to listen for service events.");

        this.bundleContext = context;

        context.registerService(
                GFacService.class.getName(), gFacServiceImpl, null);


        synchronized (this)
        {
            // Listen for events pertaining to dictionary services.
            context.addServiceListener(this, "(&(objectClass=" + GFacProviderService.class.getName() + ")" +
                    "(Provider=*))");

            // Query for any service references matching any language.
            ServiceReference[] refs = this.bundleContext.getServiceReferences(
                    GFacProviderService.class.getName(), "(Provider=*)");

            // If we found any dictionary services, then just get
            // a reference to the first one so we can use it.
            if (refs != null)
            {
                for(ServiceReference serviceRef : refs) {
                    gFacServiceImpl.addGFacProvider((String)serviceRef.getProperty("Provider"),
                            (GFacProviderService)this.bundleContext.getService(serviceRef));

                }
            }
        }
    }

    /**
     * Implements BundleActivator.stop(). Prints
     * a message and removes itself from the bundle context as a
     * service listener.
     * @param context the framework context for the bundle.
     **/
    public void stop(BundleContext context) {
        context.removeServiceListener(this);
        System.out.println("Stopped listening for service events.");

        // Note: It is not required that we remove the listener here,
        // since the framework will do it automatically anyway.
    }

    /**
     * Implements ServiceListener.serviceChanged(). Checks
     * to see if the service we are using is leaving or tries to get
     * a service if we need one.
     * @param event the fired service event.
     **/
    public synchronized void serviceChanged(ServiceEvent event)
    {
        // If a dictionary service was registered, see if we
        // need one. If so, get a reference to it.
        if (event.getType() == ServiceEvent.REGISTERED)
        {
            String providerKey = (String) event.getServiceReference().getProperty("Provider");
            GFacProviderService providerService
                    = (GFacProviderService)this.bundleContext.getService(event.getServiceReference());

            gFacServiceImpl.addGFacProvider(providerKey, providerService);

        }
        // If a dictionary service was unregistered, see if it
        // was the one we were using. If so, unget the service
        // and try to query to get another one.
        else if (event.getType() == ServiceEvent.UNREGISTERING)
        {
            String providerKey = (String) event.getServiceReference().getProperty("Provider");

            this.gFacServiceImpl.removeGFacProvider(providerKey);
            this.bundleContext.ungetService(event.getServiceReference());

        }
    }
}
