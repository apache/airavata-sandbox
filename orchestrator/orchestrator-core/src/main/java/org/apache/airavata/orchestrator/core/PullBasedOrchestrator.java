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
package org.apache.airavata.orchestrator.core;

import org.apache.airavata.common.exception.AiravataConfigurationException;
import org.apache.airavata.orchestrator.core.context.OrchestratorContext;
import org.apache.airavata.orchestrator.core.exception.OrchestratorException;
import org.apache.airavata.orchestrator.core.gfac.GFACInstance;
import org.apache.airavata.orchestrator.core.utils.OrchestratorConstants;
import org.apache.airavata.orchestrator.core.utils.OrchestratorUtils;
import org.apache.airavata.registry.api.*;
import org.apache.airavata.registry.api.exception.RegistryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PullBasedOrchestrator implements Orchestrator {
    private final static Logger logger = LoggerFactory.getLogger(PullBasedOrchestrator.class);

    OrchestratorContext orchestratorContext;

    OrchestratorRegistry airavataRegistry;

    Executor executor;

    public boolean initialize() throws OrchestratorException {
        try {
            /* Initializing the OrchestratorConfiguration object */
            OrchestratorConfiguration orchestratorConfiguration = OrchestratorUtils.loadOrchestratorConfiguration();

            /* initializing the Orchestratorcontext object */
            airavataRegistry = AiravataRegistryFactory.getRegistry(new Gateway("default"), new AiravataUser("admin"));
            Map<String, Integer> gfacNodeList = airavataRegistry.getGFACNodeList();
            if (gfacNodeList.size() == 0) {
                String error = "No GFAC instances available in the system, Can't initialize Orchestrator";
                logger.error(error);
                throw new OrchestratorException(error);
            }
            Set<String> uriList = gfacNodeList.keySet();
            Iterator<String> iterator = uriList.iterator();
            List<GFACInstance> gfacInstanceList = new ArrayList<GFACInstance>();
            while (iterator.hasNext()) {
                String uri = iterator.next();
                Integer integer = gfacNodeList.get(uri);
                gfacInstanceList.add(new GFACInstance(uri, integer));
            }

            orchestratorContext = new OrchestratorContext(gfacInstanceList);
            orchestratorContext.setOrchestratorConfiguration(orchestratorConfiguration);

            /* Starting submitter thread pool */

            executor = Executors.newFixedThreadPool(orchestratorConfiguration.getThreadPoolSize());



        } catch (RegistryException e) {
            logger.error("Failed to initializing Orchestrator");
            OrchestratorException orchestratorException = new OrchestratorException(e);
            throw orchestratorException;
        } catch (AiravataConfigurationException e) {
            logger.error("Failed to initializing Orchestrator");
            OrchestratorException orchestratorException = new OrchestratorException(e);
            throw orchestratorException;
        } catch (IOException e) {
            logger.error("Failed to initializing Orchestrator - Error parsing orchestrator.properties");
            OrchestratorException orchestratorException = new OrchestratorException(e);
            throw orchestratorException;
        }
        return true;
    }


    public boolean shutdown() throws OrchestratorException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String createExperiment(ExperimentRequest request) throws OrchestratorException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean acceptExperiment(JobRequest request) throws OrchestratorException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void startJobSubmitter() throws OrchestratorException {
        for (int i = 0; i < orchestratorContext.getOrchestratorConfiguration().getThreadPoolSize(); i++) {
                JobSubmitterWorker jobSubmitterWorker = new JobSubmitterWorker(orchestratorContext);
                executor.execute(jobSubmitterWorker);
        }
    }
}
