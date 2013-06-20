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

import org.globus.gram.GramJob;

/**
 * User: AmilaJ (amilaj@apache.org)
 * Date: 6/18/13
 * Time: 10:45 AM
 */

/**
 * This interface abstracts out state changes of a job submitted to GFac.
 * For each state change of a job an appropriate method will get called.
 * Further each method will get details about the executing job as a
 * GramJob object.
 */
public interface GramJobNotifier {

    /**
     * This method will get called when job is in pending state.
     * @param job Currently executing job.
     */
    void OnPending(GramJob job);

    /**
     * This method will get called when job is in active state.
     * @param job Currently executing job.
     */
    void OnActive(GramJob job);

    /**
     * This method will get called when job is in Error state.
     * @param job Currently executing job.
     */
    void OnError (GramJob job);

    /**
     * This method will get called when job is completed.
     * @param job Currently executing job.
     */
    void OnCompletion(GramJob job);

    /**
     * This method will get called when some process cancels the currently executing job.
     * @param job Currently executing job.
     */
    void OnCancel (GramJob job);

    /**
     * This method will get called when job is in suspended state.
     * @param job Currently executing job.
     */
    void OnSuspend (GramJob job);

    /**
     * This method will get called when job is in un-submitted state.
     * @param job Currently executing job.
     */
    void OnUnSubmit (GramJob job);

    /**
     * When job stage in input files, this method will get called.
     * @param job Currently executing job.
     */
    void OnFilesStagedIn (GramJob job);

    /**
     * When job stage out input files, this method will get called.
     * @param job Currently executing job.
     */
    void OnFilesStagedOut (GramJob job);

    /**
     * If an unexpected error occurs in the listener code this method will get
     * called.
     * @param job Currently executing job.
     * @param e The unexpected exception.
     */
    void OnListenerError(GramJob job, Exception e);

    /**
     * If authorisation failed.
     * @param job Currently executing job.
     */
    void OnAuthorisationDenied(GramJob job);

}
