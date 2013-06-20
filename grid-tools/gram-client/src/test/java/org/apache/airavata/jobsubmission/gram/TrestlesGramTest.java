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

import org.apache.airavata.jobsubmission.gram.notifier.GramJobLogger;

/**
 * User: AmilaJ (amilaj@apache.org)
 * Date: 6/19/13
 * Time: 3:54 PM
 */

public class TrestlesGramTest extends GramJobSubmissionManagerTest {

    // ====================== Trestles ==============================//

    public void testExecuteJobTrestlesInteractive() throws Exception {

        ExecutionContext executionContext = getDefaultExecutionContext();

        executionContext.setHost("trestles");

        executionContext.setInteractive(true);
        executionContext.addGramJobNotifier(new GramJobLogger());

        executeJob(executionContext);
    }

    public void testMonitoringRunningJobsTrestles() throws Exception {

        ExecutionContext executionContext = getDefaultExecutionContext();

        executionContext.setHost("trestles");

        executionContext.setInteractive(true);
        executionContext.addGramJobNotifier(new GramJobLogger());

        monitoringRunningJobs(executionContext);
    }

    public void testCancelJobsTrestles() throws Exception {

        ExecutionContext executionContext = getDefaultExecutionContext();

        executionContext.setHost("trestles");

        executionContext.setInteractive(true);
        executionContext.addGramJobNotifier(new GramJobLogger());

        cancelJob(executionContext);
    }
}
