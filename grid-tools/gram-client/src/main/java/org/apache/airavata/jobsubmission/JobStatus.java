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

package org.apache.airavata.jobsubmission;

import org.apache.airavata.jobsubmission.context.ApplicationContext;
import org.globus.gram.GramJob;

public class JobStatus {

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            ApplicationContext context = new ApplicationContext();
            context.login();

            String rsl = "";
            GramJob job = new GramJob(context.getGssCredential(), rsl);
            job.setID("https://trestles-login2.sdsc.edu:50385/16289849894573229156/9412551328091080577/");
            JobListener listener = new JobListener(job, context.getGssCredential());
            job.addListener(listener);
            System.out.println("Status 1111111" + job.getStatusAsString());
            listener.waitFor();
            System.out.println("Now Status" + job.getStatusAsString());
            job.removeListener(listener);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
