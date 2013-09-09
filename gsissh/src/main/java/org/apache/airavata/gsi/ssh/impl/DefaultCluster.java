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
package org.apache.airavata.gsi.ssh.impl;

import bsh.This;
import com.jcraft.jsch.*;
import org.apache.airavata.gsi.ssh.api.*;
import org.apache.airavata.gsi.ssh.api.job.Job;
import org.apache.airavata.gsi.ssh.config.ConfigReader;
import org.apache.airavata.gsi.ssh.jsch.ExtendedJSch;
import org.apache.airavata.gsi.ssh.listener.JobSubmissionListener;
import org.apache.airavata.gsi.ssh.util.SSHUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * This is the default implementation of a cluster.
 * this has most of the methods to be used by the end user of the
 * library.
 */
public class DefaultCluster implements Cluster {
    static {
        JSch.setConfig("gssapi-with-mic.x509", "org.apache.airavata.gsi.ssh.GSSContextX509");
        JSch.setConfig("userauth.gssapi-with-mic", "com.jcraft.jsch.UserAuthGSSAPIWithMICGSSCredentials");

    }

    private static final Logger log = LoggerFactory.getLogger(DefaultCluster.class);
    public static final String X509_CERT_DIR = "X509_CERT_DIR";
    public static final String POLLING_FREQUENCEY = "polling.frequency";
    public static final String SSH_SESSION_TIMEOUT = "ssh.session.timeout";

    private Machine[] Nodes;

    private ServerInfo serverInfo;

    private AuthenticationInfo authenticationInfo;

    private Session session;

    private static JSch jSch;

    private ConfigReader configReader;

    public DefaultCluster(ServerInfo serverInfo, AuthenticationInfo authenticationInfo) throws SSHApiException {

        this.serverInfo = serverInfo;

        this.authenticationInfo = authenticationInfo;

        System.setProperty(X509_CERT_DIR, (String) authenticationInfo.getProperties().get(X509_CERT_DIR));

        try {
            this.configReader = new ConfigReader();
        } catch (IOException e) {
            throw new SSHApiException("Unable to load system configurations.", e);
        }
        this.jSch = new ExtendedJSch();

        log.info("Connecting to server - " + serverInfo.getHost() + ":" + serverInfo.getPort() + " with user name - "
                + serverInfo.getUserName());

        try {
            session = jSch.getSession(serverInfo.getUserName(), serverInfo.getHost(), serverInfo.getPort());
            session.setTimeout(Integer.parseInt(configReader.getConfiguration(SSH_SESSION_TIMEOUT)));
        } catch (JSchException e) {
            throw new SSHApiException("An exception occurred while creating SSH session." +
                    "Connecting server - " + serverInfo.getHost() + ":" + serverInfo.getPort() +
                    " connecting user name - "
                    + serverInfo.getUserName(), e);
        }

        java.util.Properties config = this.configReader.getProperties();
        session.setConfig(config);

        // Not a good way, but we dont have any choice
        if (session instanceof ExtendedSession) {
            ((ExtendedSession) session).setAuthenticationInfo(authenticationInfo);
        }

        try {
            session.connect();
        } catch (JSchException e) {
            throw new SSHApiException("An exception occurred while connecting to server." +
                    "Connecting server - " + serverInfo.getHost() + ":" + serverInfo.getPort() +
                    " connecting user name - "
                    + serverInfo.getUserName(), e);
        }
        System.out.println(session.isConnected());
    }


    public String submitAsyncJobWithPBS(String pbsFilePath, String workingDirectory) throws SSHApiException {

        this.scpTo(workingDirectory, pbsFilePath);

        // since this is a constant we do not ask users to fill this
        RawCommandInfo rawCommandInfo = new RawCommandInfo("/opt/torque/bin/qsub " +
                workingDirectory + File.separator + FilenameUtils.getName(pbsFilePath));

        StandardOutReader jobIDReaderCommandOutput = new StandardOutReader();
        CommandExecutor.executeCommand(rawCommandInfo, this.session, jobIDReaderCommandOutput);

        //Check whether pbs submission is successful or not, if it failed throw and exception in submitJob method
        // with the error thrown in qsub command
        if (jobIDReaderCommandOutput.getErrorifAvailable().equals("")) {
            return jobIDReaderCommandOutput.getStdOutput();
        } else {
            throw new SSHApiException(jobIDReaderCommandOutput.getStandardError().toString());
        }
    }

    public String submitAsyncJob(Job jobDescriptor) throws SSHApiException {
        TransformerFactory factory = TransformerFactory.newInstance();
        String xsltPath = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "PBSTemplate.xslt";
        Source xslt = new StreamSource(new File(xsltPath));
        Transformer transformer = null;
        StringWriter results = new StringWriter();
        File tempPBSFile = null;
        try {
            // generate the pbs script using xslt
            transformer = factory.newTransformer(xslt);
            Source text = new StreamSource(new ByteArrayInputStream(jobDescriptor.toXML().getBytes()));
            transformer.transform(text, new StreamResult(results));

            log.info("generated PBS:" + results.toString());

            // creating a temporary file using pbs script generated above
            int number = new SecureRandom().nextInt();
            number = (number < 0 ? -number : number);

            tempPBSFile = new File(Integer.toString(number) + ".pbs");
            FileUtils.writeStringToFile(tempPBSFile, results.toString());

            //reusing submitAsyncJobWithPBS method to submit a job

            String jobID = this.submitAsyncJobWithPBS(tempPBSFile.getAbsolutePath(),
                    jobDescriptor.getWorkingDirectory());
            log.info("Job has successfully submitted, JobID : " + jobID);
            return jobID.replace("\n", "");
        } catch (TransformerConfigurationException e) {
            throw new SSHApiException("Error parsing PBS transformation", e);
        } catch (TransformerException e) {
            throw new SSHApiException("Error generating PBS script", e);
        } catch (IOException e) {
            throw new SSHApiException("An exception occurred while connecting to server." +
                    "Connecting server - " + serverInfo.getHost() + ":" + serverInfo.getPort() +
                    " connecting user name - "
                    + serverInfo.getUserName(), e);
        } finally {
            tempPBSFile.delete();
        }
    }


    public Cluster loadCluster() throws SSHApiException {
        RawCommandInfo rawCommandInfo = new RawCommandInfo("/opt/torque/bin/qnodes");

        StandardOutReader stdOutReader = new StandardOutReader();
        CommandExecutor.executeCommand(rawCommandInfo, this.getSession(), stdOutReader);
        if (!stdOutReader.getErrorifAvailable().equals("")) {
            throw new SSHApiException(stdOutReader.getStandardError().toString());
        }
        String result = stdOutReader.getStdOutput();
        String[] Nodes = result.split("\n");
        String[] line;
        String header, value;
        Machine Node;
        Core[] Cores = null;
        ArrayList<Machine> Machines = new ArrayList<Machine>();
        int i = 0;
        while (i < Nodes.length) {
            Node = new Machine();
            Node.setName(Nodes[i]);
            i++;

            while (i < Nodes.length) {
                if (!Nodes[i].startsWith(" ")) {
                    i++;
                    break;
                }

                line = Nodes[i].split("=");
                header = line[0].trim();
                value = line[1].trim();

                if ("state".equals(header))
                    Node.setState(value);
                else if ("np".equals(header)) {
                    Node.setNp(value);
                    int np = Integer.parseInt(Node.getNp());
                    Cores = new Core[np];
                    for (int n = 0; n < np; n++) {
                        Cores[n] = new Core("" + n);
                    }
                } else if ("ntype".equals(header))
                    Node.setNtype(value);
                else if ("jobs".equals(header)) {
                    String[] jobs = value.split(", ");
                    Job jo = new Job();
                    //Job[] Jobs = new Job[jobs.length];
                    for (int j = 0; j < jobs.length; j++) {
                        String[] c = jobs[j].split("/");
                        String Jid = c[1];
                        jo = this.getJobById(Jid);
                        int core = Integer.parseInt(c[0]);
                        Cores[core].setJob(jo);

                    }


                }
                i++;
            }
            Node.setCores(Cores);
            Machines.add(Node);
        }
        this.setNodes(Machines.toArray(new Machine[Machines.size()]));
        return this;
    }

    public Job getJobById(String jobID) throws SSHApiException {
        RawCommandInfo rawCommandInfo = new RawCommandInfo("/opt/torque/bin/qstat -f " + jobID);

        StandardOutReader stdOutReader = new StandardOutReader();
        CommandExecutor.executeCommand(rawCommandInfo, this.getSession(), stdOutReader);
        if (!stdOutReader.getErrorifAvailable().equals("")) {
            throw new SSHApiException(stdOutReader.getStandardError().toString());
        }
        String result = stdOutReader.getStdOutput();
        String[] info = result.split("\n");
        Job jobDescriptor = new Job();
        String header = "";
        String value = "";
        String[] line;
        for (int i = 0; i < info.length; i++) {
            if (info[i].contains("=")) {
                line = info[i].split("=", 2);
            } else {
                line = info[i].split(":", 2);
            }
            if (line.length >= 2) {
                header = line[0].trim();
                log.debug("Header = " + header);
                value = line[1].trim();
                log.debug("value = " + value);

                if (header.equals("Variable_List")) {
                    while (info[i + 1].startsWith("\t")) {
                        value += info[i + 1];
                        i++;
                    }
                    value = value.replaceAll("\t", "");
                    jobDescriptor.setVariableList(value);
                } else if ("Job Id".equals(header)) {
                    jobDescriptor.setJobID(value);
                } else if ("Job_Name".equals(header)) {
                    jobDescriptor.setJobName(value);
                } else if ("Account_Name".equals(header)) {
                    jobDescriptor.setAcountString(value);
                } else if ("job_state".equals(header)) {
                    jobDescriptor.setStatus(value);
                } else if ("Job_Owner".equals(header)) {
                    jobDescriptor.setOwner(value);
                } else if ("resources_used.cput".equals(header)) {
                    jobDescriptor.setUsedCPUTime(value);
                } else if ("resources_used.mem".equals(header)) {
                    jobDescriptor.setUsedMemory(value);
                } else if ("resources_used.walltime".equals(header)) {
                    jobDescriptor.setEllapsedTime(value);
                } else if ("job_state".equals(header)) {
                    jobDescriptor.setStatus(value);
                } else if ("queue".equals(header))
                    jobDescriptor.setQueueName(value);
                else if ("ctime".equals(header)) {
                    jobDescriptor.setCTime(value);
                } else if ("qtime".equals(header)) {
                    jobDescriptor.setQTime(value);
                } else if ("mtime".equals(header)) {
                    jobDescriptor.setMTime(value);
                } else if ("start_time".equals(header)) {
                    jobDescriptor.setSTime(value);
                } else if ("comp_time".equals(header)) {
                    jobDescriptor.setCompTime(value);
                } else if ("exec_host".equals(header)) {
                    jobDescriptor.setExecuteNode(value);
                } else if ("Output_Path".equals(header)) {
                    if (info[i + 1].contains("=") || info[i + 1].contains(":"))
                        jobDescriptor.setStandardOutFile(value);
                    else {
                        jobDescriptor.setStandardOutFile(value + info[i + 1].trim());
                        i++;
                    }
                } else if ("Error_Path".equals(header)) {
                    if (info[i + 1].contains("=") || info[i + 1].contains(":"))
                        jobDescriptor.setStandardErrorFile(value);
                    else {
                        String st = info[i + 1].trim();
                        jobDescriptor.setStandardErrorFile(value + st);
                        i++;
                    }

                } else if ("submit_args".equals(header)) {
                    while (i + 1 < info.length) {
                        if (info[i + 1].startsWith("\t")) {
                            value += info[i + 1];
                            i++;
                        } else
                            break;
                    }
                    value = value.replaceAll("\t", "");
                    jobDescriptor.setSubmitArgs(value);
                }
            }
        }
        return jobDescriptor;
    }

    public void scpTo(String rFile, String lFile) throws SSHApiException {
        try {
            SSHUtils.scpTo(rFile, lFile, session);
        } catch (IOException e) {
            new SSHApiException("Faile during scping local file:" + lFile + " to remote file "
                    + serverInfo.getHost() + ":rFile", e);
        } catch (JSchException e) {
            new SSHApiException("Faile during scping local file:" + lFile + " to remote file "
                    + serverInfo.getHost() + ":rFile", e);
        }
    }


    static int checkAck(InputStream in) throws IOException {
        int b = in.read();
        if (b == 0) return b;
        if (b == -1) return b;

        if (b == 1 || b == 2) {
            StringBuffer sb = new StringBuffer();
            int c;
            do {
                c = in.read();
                sb.append((char) c);
            }
            while (c != '\n');
            if (b == 1) { // error
                System.out.print(sb.toString());
            }
            if (b == 2) { // fatal error
                System.out.print(sb.toString());
            }
        }
        return b;
    }

    public String submitAsyncJob(Job jobDescriptor, JobSubmissionListener listener) throws SSHApiException {
        final Cluster cluster = this;
        final String jobID = this.submitAsyncJob(jobDescriptor);
        final JobSubmissionListener jobSubmissionListener = listener;
        try {
            // Wait 5 seconds to start the first poll, this is hard coded, user doesn't have
            // to configure this.
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            log.error("Error during job status monitoring");
            throw new SSHApiException("Error during job status monitoring", e);
        }
        // Get the job status first
        try {

            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        Job jobById = cluster.getJobById(jobID);
                        while (true) {
                            while (!jobById.getStatus().equals(JobStatus.C.toString())) {
                                if (!jobById.getStatus().equals(jobSubmissionListener.getJobStatus().toString())) {
                                    jobSubmissionListener.setJobStatus(JobStatus.fromString(jobById.getStatus()));
                                    jobSubmissionListener.statusChanged(jobById);
                                }
                                Thread.sleep(Long.parseLong(configReader.getConfiguration(POLLING_FREQUENCEY)));

                                jobById = cluster.getJobById(jobID);
                            }
                            //Set the job status to Complete
                            jobSubmissionListener.setJobStatus(JobStatus.C);
                            jobSubmissionListener.statusChanged(jobById);
                            Thread.sleep(Long.parseLong(configReader.getConfiguration(POLLING_FREQUENCEY)));
                        }
                    } catch (InterruptedException e) {
                        log.error("Error listening to the submitted job", e);
                    } catch (SSHApiException e) {
                        log.error("Error listening to the submitted job", e);
                    }
                }
            };
            //  This thread runs until the program termination, so that use can provide
            // any action in onChange method of the listener, without worrying for waiting in the caller thread.
            t.setDaemon(true);
            t.start();
        } catch (Exception e) {
            log.error("Error during job status monitoring");
            throw new SSHApiException("Error during job status monitoring", e);
        }
        return jobID;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setServerInfo(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    public void setAuthenticationInfo(AuthenticationInfo authenticationInfo) {
        this.authenticationInfo = authenticationInfo;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * @return cluster Nodes as array of machines
     */
    public Machine[] getNodes() {
        return Nodes;
    }

    public void setNodes(Machine[] Nodes) {
        this.Nodes = Nodes;
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public AuthenticationInfo getAuthenticationInfo() {
        return authenticationInfo;
    }

    /**
     * This gaurantee to return a valid session
     *
     * @return
     */
    public Session getSession() {
        return this.session;
    }
}
