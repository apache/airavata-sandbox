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

package org.apache.airavata.jobsubmission.gram;

import org.apache.airavata.jobsubmission.gram.persistence.JobData;
import org.apache.airavata.jobsubmission.gram.persistence.JobPersistenceManager;
import org.apache.airavata.jobsubmission.gram.persistence.PersistenceGramJobNotifier;
import org.apache.log4j.Logger;
import org.globus.gram.GramAttributes;
import org.globus.gram.GramException;
import org.globus.gram.GramJob;
import org.globus.gram.WaitingForCommitException;
import org.globus.gram.internal.GRAMConstants;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GramJobSubmissionManager {

    private static final Logger log = Logger.getLogger(GramJobSubmissionManager.class);

    private static final Map<String, GramJob> currentlyExecutingJobCache = new ConcurrentHashMap<String, GramJob>();

    private RSLGenerator rslGenerator;

    private JobPersistenceManager jobPersistenceManager;

    public GramJobSubmissionManager(JobPersistenceManager jobPersistenceManager) {
        this.jobPersistenceManager = jobPersistenceManager;
        this.rslGenerator = new RSLGenerator();
    }

    public String executeJob(GSSCredential gssCred, String contactString,
                             ExecutionContext appExecContext) throws Exception {

        try {
            //TODO remove when porting
            log.setLevel(org.apache.log4j.Level.ALL);

            appExecContext.addGramJobNotifier(new PersistenceGramJobNotifier(this.jobPersistenceManager));

            GramAttributes jobAttr = rslGenerator.configureRemoteJob(appExecContext);
            String rsl = jobAttr.toRSL();
            GramJob job = new GramJob(rsl + "(twoPhase=yes)");

            log.info("RSL = " + rsl);
            JobSubmissionListener listener = new JobSubmissionListener(appExecContext.getGramJobNotifierList());
            job.setCredentials(gssCred);
            job.addListener(listener);
            log.info("Request to contact:" + contactString);

            try {

                job.request(true, contactString, appExecContext.isInteractive());

            } catch(WaitingForCommitException e) {

                log.info("JobID = " + job.getIDAsString());

                jobPersistenceManager.updateJobStatus(new JobData(job.getIDAsString(),
                        GRAMConstants.STATUS_UNSUBMITTED));

                ListenerQueue listenerQueue = ListenerQueue.getInstance();
                listenerQueue.addJob(job);

                currentlyExecutingJobCache.put(job.getIDAsString(), job);

                log.debug("Two phase commit: sending COMMIT_REQUEST signal");
                job.signal(GramJob.SIGNAL_COMMIT_REQUEST);
            }

            return job.getIDAsString();

        } catch (GramException ge) {
            ge.printStackTrace();
            log.error(ge, ge.getCause());
        } catch (GSSException gss) {
            gss.printStackTrace();
            log.error(gss, gss.getCause());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e, e.getCause());
        }

        return null;
    }



    public void cancelJob(String jobId, GSSCredential gssCred) throws GramException, GSSException,
            MalformedURLException {

        if (currentlyExecutingJobCache.containsKey(jobId)) {
            GramJob gramJob = currentlyExecutingJobCache.get(jobId);
            if (gramJob != null) {
                gramJob.cancel();
                gramJob.signal(GramJob.SIGNAL_COMMIT_END);
            }
        } else {

            GramJob gramJob = new GramJob(null);
            gramJob.setID(jobId);
            gramJob.setCredentials(gssCred);

            gramJob.cancel();
            gramJob.signal(GramJob.SIGNAL_COMMIT_END);
        }

    }

    public void startMonitoringRunningJobs(GSSCredential gssCred, ExecutionContext appExecContext) throws GFacException, MalformedURLException {

        ListenerQueue listenerQueue = ListenerQueue.getInstance();

        List<JobData> jobDataList = this.jobPersistenceManager.getRunningJobs();

        appExecContext.addGramJobNotifier(new PersistenceGramJobNotifier(this.jobPersistenceManager));
        JobSubmissionListener listener = new JobSubmissionListener(appExecContext.getGramJobNotifierList());

        for (JobData jobData : jobDataList) {

            GramJob gramJob = new GramJob(null);
            gramJob.setID(jobData.getJobId());
            gramJob.setCredentials(gssCred);
            gramJob.addListener(listener);


            log.info("Adding job " + jobData.getJobId() + " in state " + jobData.getState()
                    + " to monitoring queue");

            listenerQueue.addJob(gramJob);
        }

    }
}
