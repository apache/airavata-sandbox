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

import org.apache.airavata.jobsubmission.gram.notifier.GramJobNotifier;
import org.apache.log4j.Logger;
import org.globus.gram.GramException;
import org.globus.gram.GramJob;
import org.globus.gram.GramJobListener;
import org.globus.gram.internal.GRAMProtocolErrorConstants;
import org.ietf.jgss.GSSException;

import java.util.List;

public class JobSubmissionListener implements GramJobListener {

    private int currentStatus = -43;

    private boolean isSubmitted = false;

    private static final Logger log = Logger.getLogger(JobSubmissionListener.class);

    private List<GramJobNotifier> gramJobNotifierList;

    public JobSubmissionListener(List<GramJobNotifier> notifiers) {
        this.gramJobNotifierList = notifiers;
    }


    public synchronized void statusChanged(GramJob job) {
        log.debug("Listener: statusChanged triggered");
        int jobStatus = job.getStatus();

        if (currentStatus != jobStatus) {
            currentStatus = jobStatus;

            if (currentStatus == GramJob.STATUS_FAILED) {
                int error = job.getError();

                log.debug("Job Error Code: " + error);

                try {
                    job.unbind();

                    if (error == GRAMProtocolErrorConstants.USER_CANCELLED) {
                        for(GramJobNotifier notifier : gramJobNotifierList) {
                            notifier.OnCancel(job);
                        }
                    } else if (error == GRAMProtocolErrorConstants.ERROR_AUTHORIZATION) {
                        for(GramJobNotifier notifier : gramJobNotifierList) {
                            notifier.OnAuthorisationDenied(job);
                        }
                    } else {
                        for(GramJobNotifier notifier : gramJobNotifierList) {
                            notifier.OnError(job);
                        }
                    }

                } catch (Exception e) {
                    for(GramJobNotifier notifier : gramJobNotifierList) {
                        notifier.OnListenerError(job, e);
                    }
                }
            } else if (currentStatus == GramJob.STATUS_DONE) {
                try {
                    job.unbind();

                    for(GramJobNotifier notifier : gramJobNotifierList) {
                        notifier.OnCompletion(job);
                    }

                } catch (Exception e) {
                    for(GramJobNotifier notifier : gramJobNotifierList) {
                        notifier.OnListenerError(job, e);
                    }
                }
            } else if (currentStatus == GramJob.STATUS_ACTIVE) {

                for(GramJobNotifier notifier : gramJobNotifierList) {
                    notifier.OnActive(job);
                }
            } else if (currentStatus == GramJob.STATUS_PENDING) {

                for(GramJobNotifier notifier : gramJobNotifierList) {
                    notifier.OnPending(job);
                }
            } else if (currentStatus == GramJob.STATUS_UNSUBMITTED) {

                for(GramJobNotifier notifier : gramJobNotifierList) {
                    notifier.OnUnSubmit(job);
                }
            } else if (currentStatus == GramJob.STATUS_STAGE_IN) {

                for(GramJobNotifier notifier : gramJobNotifierList) {
                    notifier.OnFilesStagedIn(job);
                }
            } else if (currentStatus == GramJob.STATUS_STAGE_OUT) {

                for(GramJobNotifier notifier : gramJobNotifierList) {
                    notifier.OnFilesStagedOut(job);
                }
            } else if (currentStatus == GramJob.STATUS_SUSPENDED) {

                for(GramJobNotifier notifier : gramJobNotifierList) {
                    notifier.OnSuspend(job);
                }
            }
        }
    }

    public static String formatJobStatus(String jobid, String jobstatus) {
        return System.currentTimeMillis() + " " + jobid + " " + jobstatus;
    }

    /*public int getError() {
        return error;
    }

    public int getStatus() {
        return status;
    }*/

    public void wakeup() {
        try {
            notify();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
