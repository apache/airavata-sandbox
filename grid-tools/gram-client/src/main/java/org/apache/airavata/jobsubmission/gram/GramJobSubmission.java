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

import org.apache.log4j.Logger;
import org.globus.gram.GramAttributes;
import org.globus.gram.GramException;
import org.globus.gram.GramJob;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;

public class GramJobSubmission {

    private static final String MULTIPLE = "multiple";

    private static final String MPI = "mpi";

    private static final String SINGLE = "single";

    private static final String CONDOR = "CONDOR";

    private static final Logger log = Logger.getLogger(GramJobSubmission.class);

    public void executeJob(GSSCredential gssCred, ExectionContext appExecContext, StringBuffer buffer) throws Exception {

        try {
            log.setLevel(org.apache.log4j.Level.INFO);
            String contact = appExecContext.getHost();
            GramAttributes jobAttr = configureRemoteJob(appExecContext);
            String rsl = jobAttr.toRSL();
            GramJob job = new GramJob(rsl);

            log.info("RSL = " + rsl);
            JobSubmissionListener listener = new JobSubmissionListener(job, buffer);
            job.setCredentials(gssCred);
            job.addListener(listener);
            log.info("Request to contact:" + contact);
            job.request(contact);

            log.info("JobID = " + job.getIDAsString());

            listener.waitFor();
            job.removeListener(listener);
        } catch (GramException ge) {
            ge.printStackTrace();
            log.error(ge, ge.getCause());
            buffer.append(System.currentTimeMillis() + "#id#FAILED#" + ge.getMessage());
        } catch (GSSException gss) {
            gss.printStackTrace();
            log.error(gss, gss.getCause());
            buffer.append(System.currentTimeMillis() + "#id#FAILED#" + gss.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e, e.getCause());
            buffer.append(System.currentTimeMillis() + "#id#FAILED#" + e.getMessage());
        }
    }

    protected GramAttributes configureRemoteJob(ExectionContext appExecContext) throws Exception {
        GramAttributes jobAttr = new GramAttributes();
        jobAttr.setExecutable(appExecContext.getExecutable());
        if (appExecContext.getWorkingDir() != null) {
            jobAttr.setDirectory(appExecContext.getWorkingDir());
            jobAttr.setStdout(appExecContext.getStdOut());
            jobAttr.setStderr(appExecContext.getStderr());
        }
        // The env here contains the env of the host and the application. i.e
        // ArrayList<String[]> nv = appExecContext.getEnv();
        //
        // for (int i = 0; i < nv.size(); ++i) {
        // String[] nvPair = (String[]) nv.get(i);
        // jobAttr.addEnvVariable(nvPair[0], nvPair[1]);
        // }

        // jobAttr.addEnvVariable(GFacConstants.INPUT_DATA_DIR,
        // appExecContext.getInputDataDir());
        // jobAttr.addEnvVariable(GFacConstants.OUTPUT_DATA_DIR,
        // appExecContext.getOutputDataDir());

        if (appExecContext.getMaxWallTime() != null && appExecContext.getMaxWallTime() > 0) {
            log.info("Setting max wall clock time to " + appExecContext.getMaxWallTime());
            jobAttr.setMaxWallTime(appExecContext.getMaxWallTime());
            jobAttr.set("proxy_timeout", "1");
        }
        if (appExecContext.getPcount() != null && appExecContext.getPcount() > 1) {
            log.info("Setting number of procs to " + appExecContext.getPcount());
            jobAttr.setNumProcs(appExecContext.getPcount());
        }

        if (appExecContext.getHostCount() != null && appExecContext.getHostCount() > 1) {
            jobAttr.set("hostCount", String.valueOf(appExecContext.getHostCount()));
        }

        if (appExecContext.getProjectName() != null) {
            log.info("Setting project to " + appExecContext.getProjectName());
            jobAttr.setProject(appExecContext.getProjectName());
        }

        if (appExecContext.getQueue() != null) {
            jobAttr.setQueue(appExecContext.getQueue());
        }
        if (appExecContext.getArguments() != null) {
            jobAttr.set("arguments", appExecContext.getArguments());
        }
        String jobType = SINGLE;

        if (appExecContext.getJobType() != null) {
            jobType = appExecContext.getJobType();
        }
        if (jobType.equals(SINGLE)) {
            log.info("Setting job type to single");
            jobAttr.setJobType(GramAttributes.JOBTYPE_SINGLE);
        } else if (jobType.equals(MPI)) {
            log.info("Setting job type to mpi");
            jobAttr.setJobType(GramAttributes.JOBTYPE_MPI);
        } else if (jobType.equals(MULTIPLE)) {
            log.info("Setting job type to multiple");
            jobAttr.setJobType(GramAttributes.JOBTYPE_MULTIPLE);
        } else if (jobType.equals(CONDOR)) {
            log.info("Setting job type to condor");
            jobAttr.setJobType(GramAttributes.JOBTYPE_CONDOR);
        }

        // Support to add the Additional RSL parameters
        // RSLParmType[] rslParams = app.getRslparmArray();
        // if (rslParams.length > 0) {
        // for (RSLParmType rslType : rslParams) {
        // log.info("Adding rsl param of [" + rslType.getName() + ","
        // + rslType.getStringValue() + "]");
        // if(rslType.getName()!= ""){
        // jobAttr.set(rslType.getName(), rslType.getStringValue());
        // }
        // }
        // }

        // support urgency/SPRUCE case
        // only add spruce rsl parameter if this host has a spruce jobmanager
        // configured
        // jobAttr.set("urgency", appExecContext.getLeadHeader().getUrgency());

        return jobAttr;
    }
}
