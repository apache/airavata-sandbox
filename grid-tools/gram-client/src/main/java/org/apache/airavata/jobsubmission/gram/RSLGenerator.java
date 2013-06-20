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

/**
 * User: AmilaJ (amilaj@apache.org)
 * Date: 6/18/13
 * Time: 10:24 AM
 */

import org.apache.log4j.Logger;
import org.globus.gram.GramAttributes;

/**
 * Responsible for generating the RSL from descriptors. Based on execution contexts.
 */
public class RSLGenerator {

    private static final String MULTIPLE = "multiple";

    private static final String MPI = "mpi";

    private static final String SINGLE = "single";

    private static final String CONDOR = "CONDOR";

    private static final Logger log = Logger.getLogger(RSLGenerator.class);

    protected GramAttributes configureRemoteJob(ExecutionContext appExecContext) throws Exception {
        GramAttributes jobAttr = new GramAttributes();
        jobAttr.setExecutable(appExecContext.getExecutable());
        if (appExecContext.getWorkingDir() != null) {
            jobAttr.setDirectory(appExecContext.getWorkingDir());
            jobAttr.setStdout(appExecContext.getStdOut());
            jobAttr.setStderr(appExecContext.getStderr());
        }

        if (appExecContext.getMaxWallTime() != null && appExecContext.getMaxWallTime() > 0) {
            log.info("Setting max wall clock time to " + appExecContext.getMaxWallTime());
            jobAttr.setMaxWallTime(appExecContext.getMaxWallTime());
            jobAttr.set("proxy_timeout", "1");
        }
        if (appExecContext.getPcount() != null && appExecContext.getPcount() >= 1) {
            log.info("Setting number of procs to " + appExecContext.getPcount());
            jobAttr.setNumProcs(appExecContext.getPcount());
        }

        if (appExecContext.getHostCount() != null && appExecContext.getHostCount() >= 1) {
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


        return jobAttr;
    }

}
