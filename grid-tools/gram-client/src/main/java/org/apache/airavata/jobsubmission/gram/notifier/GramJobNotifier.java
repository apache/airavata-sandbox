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

public interface GramJobNotifier {

    void OnPending(GramJob job);

    void OnActive(GramJob job);

    void OnError (GramJob job);

    void OnCompletion(GramJob job);

    void OnCancel (GramJob job);

    void OnSuspend (GramJob job);

    void OnUnSubmit (GramJob job);

    void OnFilesStagedIn (GramJob job);

    void OnFilesStagedOut (GramJob job);

    void OnListenerError(GramJob job, Exception e);

    void OnAuthorisationDenied(GramJob job);

}
