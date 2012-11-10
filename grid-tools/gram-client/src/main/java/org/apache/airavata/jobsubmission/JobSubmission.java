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
import org.apache.airavata.jobsubmission.gram.ExectionContext;
import org.apache.airavata.jobsubmission.gram.GramJobSubmission;
import org.apache.log4j.Logger;
import org.ietf.jgss.GSSCredential;

public class JobSubmission {

    private static final Logger log = Logger.getLogger(JobSubmission.class);
    private static ExectionContext contextExectionContext;
    private static ApplicationContext context;
    private static String newline = System.getProperty("line.separator");

    public static synchronized ApplicationContext getContext() throws Exception {
        if (context == null) {
            context = new ApplicationContext();
            try {
                context.login();
            } catch (Exception e) {
                context = null;
                e.printStackTrace();
                throw e;
            }
        }
        return context;
    }

    public static ExectionContext getExecutionContext() {

        if (contextExectionContext == null) {
            try {
                contextExectionContext = new ExectionContext();
            } catch (Exception e) {
                log.error("Error loading configuration file", e);
                System.err.println("Make sure configuration file is in classpath and has proper values");

                /*
                 * Set required value to default since property file cannot be loaded
                 */
                // contextExectionContext.setHost("login5.ranger.tacc.utexas.edu:2120/jobmanager-sge");
                // contextExectionContext.setHost("gatekeeper.lonestar.tacc.teragrid.org:2120/jobmanager-lsf");
                // contextExectionContext.setHost("queenbee.loni-lsu.teragrid.org:2120/jobmanager-pbs");
                contextExectionContext.setHost("gatekeeper.ranger.tacc.teragrid.org:2120/jobmanager-sge");
                contextExectionContext.setExecutable("/bin/sleep");
                contextExectionContext.setArguments("30");
                contextExectionContext.setQueue("checkpt");
                contextExectionContext.setProjectName("TG-STA060010N");
                contextExectionContext.setJobType("single");
                contextExectionContext.setMaxWallTime(1);
                contextExectionContext.setPcount(1);
                contextExectionContext.setHostCount(1);
                e.printStackTrace();
            }
        }
        return contextExectionContext;
    }

    public static void main(String[] args) {
        try {
            ApplicationContext context = new ApplicationContext();
            context.login();

            StringBuffer jobstatus = new StringBuffer();
            GramJobSubmission gramJobSubmittion = new GramJobSubmission();

            System.out.println();

            gramJobSubmittion.executeJob(context.getGssCredential(), getExecutionContext(), jobstatus);
            System.out.println(jobstatus.toString());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        String result = null;
        try {
            StringBuffer x = new StringBuffer();
            GSSCredential gssCredential = getContext().getGssCredential();
            x.append(getExecutionContext().getHost() + "#" + getCName(gssCredential.getName().toString()) + newline);
            log.info("NAME:" + getCName(gssCredential.getName().toString()));

            GramJobSubmission gramJobSubmittion = new GramJobSubmission();
            gramJobSubmittion.executeJob(gssCredential, getExecutionContext(), x);
            result = x.toString();
        } catch (Exception e) {
            log.error(e.getCause());
            result = getExecutionContext().getHost() + "#" + "user" + newline + System.currentTimeMillis() + "#" + "id"
                    + "#" + "FAILED" + "#" + e.getMessage();

        }
        return result;
    }

    private String getCName(String full) {
        int index = full.indexOf("/CN=");
        return full.substring(index + 4);
    }
}
