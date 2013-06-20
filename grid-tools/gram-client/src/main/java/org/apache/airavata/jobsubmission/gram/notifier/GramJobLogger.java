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

package org.apache.airavata.jobsubmission.gram.notifier;

import org.apache.log4j.Logger;
import org.globus.gram.GramJob;
import org.ietf.jgss.GSSException;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * User: AmilaJ (amilaj@apache.org)
 * Date: 6/18/13
 * Time: 10:59 AM
 */

public class GramJobLogger implements GramJobNotifier {

    private static final Logger log = Logger.getLogger(GramJobLogger.class);

    private static ResourceBundle resources;

    private String format = "Job Id [ %s ] is in [ %s ] state.";

    static {
        try {
            resources = ResourceBundle.getBundle("org.apache.airavata.jobsubmission.gram.errors",
                    Locale.getDefault());
        } catch (MissingResourceException mre) {
            System.err.println("org.globus.gram.internal.gram.errors.properties not found");
        }
    }


    public void OnPending(GramJob job) {
        log.info(String.format(format, job.getIDAsString(), "PENDING"));
    }

    public void OnActive(GramJob job) {
        log.info(String.format(format, job.getIDAsString(), "ACTIVE"));
    }

    public void OnError(GramJob job) {
        log.info(String.format("Job Id [ %s ] is in %s state. Error code - %d and description - %s",
                job.getIDAsString(), "[ ERROR ]", job.getError(),"TODO fix bundle loading"));
    }

    public void OnCompletion(GramJob job) {
        log.info(String.format(format, job.getIDAsString(), "COMPLETE"));
    }

    public void OnCancel(GramJob job) {
        log.info(String.format(format, job.getIDAsString(), "USER-CANCELED"));
    }

    public void OnSuspend(GramJob job) {
        log.info(String.format(format, job.getIDAsString(), "SUSPEND"));
    }

    public void OnUnSubmit(GramJob job) {
        log.info(String.format(format, job.getIDAsString(), "UN-SUBMITTED"));
    }

    public void OnFilesStagedIn(GramJob job) {
        log.info(String.format(format, job.getIDAsString(), "FILES-STAGED-IN"));
    }

    public void OnFilesStagedOut(GramJob job) {
        log.info(String.format(format, job.getIDAsString(), "FILES-STAGED-OUT"));
    }

    public void OnListenerError(GramJob job, Exception e) {
        log.error("An error occurred while monitoring job id - " + job.getIDAsString(), e);
    }

    public void OnAuthorisationDenied(GramJob job) {
        try {
            log.error("Authorisation denied for job execution. User name - "
                    + job.getCredentials().getName().toString());
        } catch (GSSException e) {
            log.error("An error occurred while logging authorisation information - ", e);
        }
    }
}
