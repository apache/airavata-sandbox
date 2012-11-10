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
import org.globus.gram.GramJob;
import org.globus.gram.GramJobListener;

public class JobSubmittionListener implements GramJobListener {

    private static final String DELIMITER = "#";
    private boolean finished;
    private int error;
    private int status;
    private StringBuffer buffer;

    private GramJob job;

    private static final Logger log = Logger.getLogger(JobSubmittionListener.class);

    public JobSubmittionListener(GramJob job, StringBuffer buffer) {
        this.buffer = buffer;
        this.job = job;
    }

    // waits for DONE or FAILED status
    public void waitFor() throws InterruptedException {
        while (!finished) {

            // job status is changed but method isn't invoked
            if (status != 0) {
                if (job.getStatus() != status) {
                    log.info("invoke method manually");
                    statusChanged(job);
                } else {
                    log.info("job " + job.getIDAsString() + " have same status: " + GramJob.getStatusAsString(status));
                }
            } else {
                log.info("Status is zero");
            }

            synchronized (this) {
                wait(60 * 1000l);
            }
        }
    }

    public synchronized void statusChanged(GramJob job) {
        log.debug("Listener: statusChanged triggered");
        int jobStatus = job.getStatus();
        String jobId = job.getIDAsString();
        String statusString = job.getStatusAsString();
        log.info("Job Status: " + statusString + "(" + jobStatus + ")");
        buffer.append(formatJobStatus(jobId, statusString));
        log.debug(formatJobStatus(jobId, statusString));

        status = jobStatus;
        if (jobStatus == GramJob.STATUS_DONE) {
            finished = true;
        } else if (jobStatus == GramJob.STATUS_FAILED) {
            finished = true;
            error = job.getError();
            log.info("Job Error Code: " + error);
            buffer.append(DELIMITER + error);
        }
        buffer.append("\n");

        // notify wait thread to wake up if done
        if (finished) {
            notify();
        }
    }

    public static String formatJobStatus(String jobid, String jobstatus) {
        return System.currentTimeMillis() + DELIMITER + jobid + DELIMITER + jobstatus;
    }

    public int getError() {
        return error;
    }

    public int getStatus() {
        return status;
    }

    public void wakeup() {
        try {
            notify();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
