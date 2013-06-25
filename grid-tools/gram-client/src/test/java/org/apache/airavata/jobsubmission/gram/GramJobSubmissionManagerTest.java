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

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.airavata.common.utils.DBUtil;
import org.apache.airavata.common.utils.DerbyUtil;
import org.apache.airavata.jobsubmission.gram.notifier.GramJobLogger;
import org.apache.airavata.jobsubmission.gram.persistence.DBJobPersistenceManager;
import org.apache.airavata.jobsubmission.gram.persistence.JobData;
import org.apache.airavata.jobsubmission.gram.persistence.JobPersistenceManager;
import org.apache.airavata.security.myproxy.SecurityContext;
import org.apache.log4j.Logger;
import org.globus.gsi.provider.GlobusProvider;

import java.security.Security;
import java.util.List;

/**
 * User: AmilaJ (amilaj@apache.org)
 * Date: 6/12/13
 * Time: 10:24 AM
 */

public class GramJobSubmissionManagerTest extends TestCase {

    static {
        Security.addProvider(new GlobusProvider());
    }

    private static final Logger logger = Logger.getLogger(GramJobSubmissionManagerTest.class);

    protected static String hostAddress = "localhost";
    protected static int port = 20000;
    protected static String userName = "admin";
    protected static String password = "admin";
    protected static String driver = "org.apache.derby.jdbc.ClientDriver";

    private String myProxyUserName = System.getProperty("myproxy.user");
    private String myProxyPassword = System.getProperty("myproxy.password");

    public void setUp() throws Exception{

        if (myProxyUserName == null || myProxyPassword == null || myProxyUserName.trim().equals("") ||
                myProxyPassword.trim().equals("")) {
            logger.error("myproxy.user and myproxy.password system properties are not set. Example :- " +
                    "> mvn clean install -Dmyproxy.user=u1 -Dmyproxy.password=xxx");

            Assert.fail("Please set myproxy.user and myproxy.password system properties.");

        }


        DerbyUtil.startDerbyInServerMode(getHostAddress(), getPort(), getUserName(), getPassword());

        String createTable = "CREATE TABLE gram_job\n" +
                "(\n" +
                "        job_id VARCHAR(256) NOT NULL,\n" +
                "        status int NOT NULL,\n" +
                "        PRIMARY KEY (job_id)\n" +
                ")";


        String dropTable = "drop table gram_job";

        try {
            executeSQL(dropTable);
        } catch (Exception e) {}

        executeSQL(createTable);

        ListenerQueue listenerQueue = ListenerQueue.getInstance();
        listenerQueue.startListenerQueue();
    }

    public void tearDown() {

        ListenerQueue listenerQueue = ListenerQueue.getInstance();
        listenerQueue.stopListenerQueue();
    }

    public static void executeSQL(String sql) throws Exception {
        DBUtil dbUtil = new DBUtil(getJDBCUrl(), getUserName(), getPassword(), getDriver());
        dbUtil.executeSQL(sql);
    }

    public static String getJDBCUrl() {
        return "jdbc:derby://" + getHostAddress() + ":" + getPort() + "/persistent_data;create=true;user=" + getUserName() + ";password=" + getPassword();
    }

    public static String getHostAddress() {
        return hostAddress;
    }

    public static int getPort() {
        return port;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getPassword() {
        return password;
    }

    public static String getDriver() {
        return driver;
    }

    public DBUtil getDbUtil () throws Exception {
        return new DBUtil(getJDBCUrl(), getUserName(), getPassword(), getDriver());

    }

    // Dummy test case just avoid failures
    public void testDummy() {}

    public void executeJob(ExecutionContext executionContext) throws Exception {

        SecurityContext context = new SecurityContext(myProxyUserName, myProxyPassword);
        context.login();

        JobPersistenceManager jobPersistenceManager
                = new DBJobPersistenceManager(getDbUtil());
        GramJobSubmissionManager gramJobSubmissionManager
                = new GramJobSubmissionManager(jobPersistenceManager);

        String jobId = gramJobSubmissionManager.executeJob(context.getRawCredential(),
                executionContext.getGRAMEndPoint(),
                executionContext);

        Assert.assertNotNull(jobId);

        Thread.sleep(2 * 60 * 1000);

        logger.info("Checking whether job is in successful state in the persistence store");

        List<JobData> list = jobPersistenceManager.getSuccessfullyCompletedJobs();
        Assert.assertEquals(1, list.size());

        Assert.assertEquals(jobId, list.get(0).getJobId());
    }

    public void monitoringRunningJobs(ExecutionContext executionContext) throws Exception {

        SecurityContext context = new SecurityContext(myProxyUserName, myProxyPassword);
        context.login();

        JobPersistenceManager jobPersistenceManager
                = new DBJobPersistenceManager(getDbUtil());
        GramJobSubmissionManager gramJobSubmissionManager
                = new GramJobSubmissionManager(jobPersistenceManager);

        executionContext.addGramJobNotifier(new GramJobLogger());

        String jobId = gramJobSubmissionManager.executeJob(context.getRawCredential(),
                executionContext.getGRAMEndPoint(),
                executionContext);

        Thread.sleep(3000);

        ListenerQueue listenerQueue = ListenerQueue.getInstance();
        listenerQueue.stopListenerQueue();

        logger.info("=================== Process Finished - Monitoring Stopped ==========================");

        Assert.assertNotNull(jobId);

        listenerQueue = ListenerQueue.getInstance();
        listenerQueue.startListenerQueue();

        logger.info("=================== Monitoring Stored Jobs ==========================");

        gramJobSubmissionManager.startMonitoringRunningJobs(context.getRawCredential(), executionContext);

        Thread.sleep(1 * 60 * 1000);
    }

    public void cancelJob(ExecutionContext executionContext) throws Exception {

        SecurityContext context = new SecurityContext(myProxyUserName, myProxyPassword);
        context.login();

        JobPersistenceManager jobPersistenceManager
                = new DBJobPersistenceManager(getDbUtil());
        GramJobSubmissionManager gramJobSubmissionManager
                = new GramJobSubmissionManager(jobPersistenceManager);

        executionContext.addGramJobNotifier(new GramJobLogger());

        String jobId = gramJobSubmissionManager.executeJob(context.getRawCredential(),
                executionContext.getGRAMEndPoint(),
                executionContext);

        Thread.sleep(30 * 1000);

        Assert.assertNotNull(jobId);

        gramJobSubmissionManager.cancelJob(jobId, context.getRawCredential());


        logger.info("========== End of test case ==============");

        Thread.sleep(1 * 30 * 1000);
    }


    public static ExecutionContext getDefaultExecutionContext() throws Exception {

        return new ExecutionContext();
    }
}
