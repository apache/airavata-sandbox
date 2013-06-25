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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.airavata.jobsubmission.gram.notifier.GramJobNotifier;
import org.apache.airavata.jobsubmission.utils.ServiceConstants;

public class ExecutionContext {

    private String testingHost;
    
    private String lonestarGRAM;
    private String stampedeGRAM;
    private String trestlesGRAM;
    
    private String workingDir;
    private String tmpDir;
    private String stdOut;
    private String stderr;
    private String host;
    private String executable;
    private ArrayList<String[]> env;
    private String inputDataDir;
    private String outputDataDir;
    private boolean parameterNamesNeeded = false;
    private String stdIn;
    private String stdoutStr;
    private String stderrStr;
    private String queue;
    private Integer maxWallTime;
    private Integer pcount;
    private String projectName;
    private Integer minMemory;
    private Integer hostCount;
    private String jobType;
    private String arguments;

    private boolean interactive;

    private String userName;
    private String password;

    private List<GramJobNotifier> gramJobNotifierList = new ArrayList<GramJobNotifier>();

    public static final String PROPERTY_FILE = "airavata-myproxy-client.properties";

    public ExecutionContext() throws IOException {
        loadConfigration();
    }

    private void loadConfigration() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream propertyStream = classLoader.getResourceAsStream(PROPERTY_FILE);

        Properties properties = new Properties();
        if (propertyStream != null) {
            properties.load(propertyStream);
            
            String testinghost = properties.getProperty(ServiceConstants.TESTINGHOST);
            
            String lonestargram = properties.getProperty(ServiceConstants.LONESTARGRAMEPR);
            String stampedeGram = properties.getProperty(ServiceConstants.STAMPEDE_GRAM_EPR);
            String trestlesgram = properties.getProperty(ServiceConstants.TRESTLESGRAMEPR);

            String exec = properties.getProperty(ServiceConstants.EXECUTABLE);
            String args = properties.getProperty(ServiceConstants.ARGUMENTS);
            String queueName = properties.getProperty(ServiceConstants.QUEUE);
            String pn = properties.getProperty(ServiceConstants.PROJECT_NUMBER);
            String jt = properties.getProperty(ServiceConstants.JOB_TYPE);
            String mwt = properties.getProperty(ServiceConstants.MAX_WALL_TIME);
            String pc = properties.getProperty(ServiceConstants.PCOUNT);
            String hc = properties.getProperty(ServiceConstants.HOSTCOUNT);

            if (testinghost != null) {
                this.testingHost = testinghost;
            }
            
            if (lonestargram != null) {
                this.lonestarGRAM = lonestargram;
            }
            if (stampedeGram != null) {
                this.stampedeGRAM = stampedeGram;
            }
            if (trestlesgram != null) {
                this.trestlesGRAM = trestlesgram;
            }
            
            if (exec != null) {
                this.executable = exec;
            }
            if (args != null) {
                this.arguments = args;
            }
            if (queueName != null) {
                this.queue = queueName;
            }
            if (pn != null) {
                this.projectName = pn;
            }
            if (jt != null) {
                this.jobType = jt;
            }
            if (mwt != null) {
                try {
                    this.maxWallTime = Integer.parseInt(mwt);
                } catch (NumberFormatException e) {
                    this.maxWallTime = 1;
                }
            }
            if (pc != null) {
                try {
                    this.pcount = Integer.parseInt(pc);
                } catch (NumberFormatException e) {
                    this.pcount = 1;
                }
            }
            if (hc != null) {
                try {
                    this.hostCount = Integer.parseInt(hc);
                } catch (NumberFormatException e) {
                    this.hostCount = 1;
                }
            }

        }
    }

    public String getTestingHost() {
        return testingHost;
    }

    public void setTestingHost(String testingHost) {
        this.testingHost = testingHost;
    }

    public String getLonestarGRAM() {
        return lonestarGRAM;
    }

    public void setLonestarGRAM(String lonestarGRAM) {
        this.lonestarGRAM = lonestarGRAM;
    }

    public String getStampedeGRAM() {
        return stampedeGRAM;
    }

    public void setStampedeGRAM(String stampedeGRAM) {
        this.stampedeGRAM = stampedeGRAM;
    }

    public String getTrestlesGRAM() {
        return trestlesGRAM;
    }

    public String getGRAMEndPoint() {

        if (this.getHost().equals("trestles")) {
            return this.getTrestlesGRAM();
        } else if (this.getHost().equals("stampede")) {
            return this.getStampedeGRAM();
        } else if (this.getHost().equals("lonestar")) {
            return this.getLonestarGRAM();
        } else {
            throw new RuntimeException("Invalid host " + this.getHost() );
        }

    }

    public void setTrestlesGRAM(String trestlesGRAM) {
        this.trestlesGRAM = trestlesGRAM;
    }

    /**
     * @return the workingDir
     */
    public String getWorkingDir() {
        return workingDir;
    }

    /**
     * @param workingDir
     *            the workingDir to set
     */
    public void setWorkingDir(String workingDir) {
        this.workingDir = workingDir;
    }

    /**
     * @return the tmpDir
     */
    public String getTmpDir() {
        return tmpDir;
    }

    /**
     * @param tmpDir
     *            the tmpDir to set
     */
    public void setTmpDir(String tmpDir) {
        this.tmpDir = tmpDir;
    }

    /**
     * @return the stdOut
     */
    public String getStdOut() {
        return stdOut;
    }

    /**
     * @param stdOut
     *            the stdOut to set
     */
    public void setStdOut(String stdOut) {
        this.stdOut = stdOut;
    }

    /**
     * @return the stderr
     */
    public String getStderr() {
        return stderr;
    }

    /**
     * @param stderr
     *            the stderr to set
     */
    public void setStderr(String stderr) {
        this.stderr = stderr;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host
     *            the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the executable
     */
    public String getExecutable() {
        return executable;
    }

    /**
     * @param executable
     *            the executable to set
     */
    public void setExecutable(String executable) {
        this.executable = executable;
    }

    /**
     * @return the env
     */
    public ArrayList<String[]> getEnv() {
        return env;
    }

    /**
     * @param env
     *            the env to set
     */
    public void setEnv(ArrayList<String[]> env) {
        this.env = env;
    }

    /**
     * @return the inputDataDir
     */
    public String getInputDataDir() {
        return inputDataDir;
    }

    /**
     * @param inputDataDir
     *            the inputDataDir to set
     */
    public void setInputDataDir(String inputDataDir) {
        this.inputDataDir = inputDataDir;
    }

    /**
     * @return the outputDataDir
     */
    public String getOutputDataDir() {
        return outputDataDir;
    }

    /**
     * @param outputDataDir
     *            the outputDataDir to set
     */
    public void setOutputDataDir(String outputDataDir) {
        this.outputDataDir = outputDataDir;
    }

    /**
     * @return the parameterNamesNeeded
     */
    public boolean isParameterNamesNeeded() {
        return parameterNamesNeeded;
    }

    /**
     * @param parameterNamesNeeded
     *            the parameterNamesNeeded to set
     */
    public void setParameterNamesNeeded(boolean parameterNamesNeeded) {
        this.parameterNamesNeeded = parameterNamesNeeded;
    }

    /**
     * @return the stdIn
     */
    public String getStdIn() {
        return stdIn;
    }

    /**
     * @param stdIn
     *            the stdIn to set
     */
    public void setStdIn(String stdIn) {
        this.stdIn = stdIn;
    }

    /**
     * @return the stdoutStr
     */
    public String getStdoutStr() {
        return stdoutStr;
    }

    /**
     * @param stdoutStr
     *            the stdoutStr to set
     */
    public void setStdoutStr(String stdoutStr) {
        this.stdoutStr = stdoutStr;
    }

    /**
     * @return the stderrStr
     */
    public String getStderrStr() {
        return stderrStr;
    }

    /**
     * @param stderrStr
     *            the stderrStr to set
     */
    public void setStderrStr(String stderrStr) {
        this.stderrStr = stderrStr;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getQueue() {
        return queue;
    }

    public void setMaxWallTime(Integer maxWallTime) {
        this.maxWallTime = maxWallTime;
    }

    public Integer getMaxWallTime() {
        return maxWallTime;
    }

    /**
     * @return the pcount
     */
    public Integer getPcount() {
        return pcount;
    }

    /**
     * @param pcount
     *            the pcount to set
     */
    public void setPcount(Integer pcount) {
        this.pcount = pcount;
    }

    /**
     * @return the projectName
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * @param projectName
     *            the projectName to set
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * @return the minMemory
     */
    public Integer getMinMemory() {
        return minMemory;
    }

    /**
     * @param minMemory
     *            the minMemory to set
     */
    public void setMinMemory(Integer minMemory) {
        this.minMemory = minMemory;
    }

    /**
     * @return the hostCount
     */
    public Integer getHostCount() {
        return hostCount;
    }

    /**
     * @param hostCount
     *            the hostCount to set
     */
    public void setHostCount(Integer hostCount) {
        this.hostCount = hostCount;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getJobType() {
        return jobType;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public String getArguments() {
        return arguments;
    }

    public boolean isInteractive() {
        return interactive;
    }

    public void setInteractive(boolean interactive) {
        this.interactive = interactive;
    }

    public void addGramJobNotifier(GramJobNotifier gramJobNotifier) {
        this.gramJobNotifierList.add(gramJobNotifier);
    }

    public List<GramJobNotifier> getGramJobNotifierList() {
        return Collections.unmodifiableList(gramJobNotifierList);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
