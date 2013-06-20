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

package org.apache.airavata.jobsubmission.gram.persistence;

import org.apache.airavata.jobsubmission.gram.GFacException;
import org.apache.airavata.jobsubmission.gram.notifier.GramJobNotifier;
import org.globus.gram.GramJob;

/**
 * User: AmilaJ (amilaj@apache.org)
 * Date: 6/18/13
 * Time: 3:49 PM
 */

public class PersistenceGramJobNotifier implements GramJobNotifier {

    private JobPersistenceManager jobPersistenceManager;

    public PersistenceGramJobNotifier(JobPersistenceManager persistenceManager) {
        this.jobPersistenceManager = persistenceManager;
    }

    public void OnPending(GramJob job) {
        try {
            this.jobPersistenceManager.updateJobStatus(new JobData(job.getIDAsString(), job.getStatus()));
        } catch (GFacException e) {
            e.printStackTrace();
        }
    }

    public void OnActive(GramJob job) {}

    public void OnError(GramJob job) {
        try {
            this.jobPersistenceManager.updateJobStatus(new JobData(job.getIDAsString(), job.getStatus()));
        } catch (GFacException e) {
            e.printStackTrace();
        }
    }

    public void OnCompletion(GramJob job) {
        try {
            this.jobPersistenceManager.updateJobStatus(new JobData(job.getIDAsString(), job.getStatus()));
        } catch (GFacException e) {
            e.printStackTrace();
        }
    }

    public void OnCancel(GramJob job) {
        try {
            this.jobPersistenceManager.updateJobStatus(new JobData(job.getIDAsString(), job.getStatus()));
        } catch (GFacException e) {
            e.printStackTrace();
        }
    }

    public void OnSuspend(GramJob job) {}

    public void OnUnSubmit(GramJob job) {}

    public void OnFilesStagedIn(GramJob job) {}

    public void OnFilesStagedOut(GramJob job) {}

    public void OnListenerError(GramJob job, Exception e) {}

    public void OnAuthorisationDenied(GramJob job) {
        try {
            this.jobPersistenceManager.updateJobStatus(new JobData(job.getIDAsString(), job.getStatus()));
        } catch (GFacException e) {
            e.printStackTrace();
        }
    }
}
