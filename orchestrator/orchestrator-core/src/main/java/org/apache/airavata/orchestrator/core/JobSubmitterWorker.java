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

import org.apache.airavata.orchestrator.core.context.OrchestratorContext;
import org.apache.airavata.orchestrator.core.exception.OrchestratorException;
import org.apache.airavata.orchestrator.core.job.JobSubmitter;
import org.apache.airavata.orchestrator.core.utils.OrchestratorConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class JobSubmitterWorker implements Runnable{
    private final static Logger logger = LoggerFactory.getLogger(JobSubmitterWorker.class);

    OrchestratorContext orchestratorContext;

    JobSubmitter jobSubmitter;

    public JobSubmitterWorker(OrchestratorContext orchestratorContext) throws OrchestratorException{

        this.orchestratorContext = orchestratorContext;

        URL resource =
                JobSubmitterWorker.class.getClassLoader().getResource(OrchestratorConstants.ORCHESTRATOR_PROPERTIES);

        if(resource == null){
            throw new OrchestratorException("orchestrator.properties cannot found");
        }
        Properties orchestratorProps = new Properties();
        try {
            orchestratorProps.load(resource.openStream());
            String submitterClass = (String)orchestratorProps.get("job.submitter");
            Class<? extends JobSubmitter> aClass = Class.forName(submitterClass.trim()).asSubclass(JobSubmitter.class);
            jobSubmitter = aClass.newInstance();
        } catch (IOException e) {
            logger.error("Error reading orchestrator.properties");
            throw new OrchestratorException(e);
        } catch (ClassNotFoundException e) {
            logger.error("Error while loading Job Submitter");
        } catch (InstantiationException e) {
            logger.error("Error while loading Job Submitter");
            throw new OrchestratorException(e);
        } catch (IllegalAccessException e) {
            logger.error("Error while loading Job Submitter");
            throw new OrchestratorException(e);
        }

    }

    public void run() {
         /* implement logic to submit job batches time to time */
    }

    public OrchestratorContext getOrchestratorContext() {
        return orchestratorContext;
    }

    public void setOrchestratorContext(OrchestratorContext orchestratorContext) {
        this.orchestratorContext = orchestratorContext;
    }
}
