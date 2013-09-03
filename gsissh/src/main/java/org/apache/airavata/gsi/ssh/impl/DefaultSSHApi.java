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

import com.jcraft.jsch.*;
import org.apache.airavata.gsi.ssh.api.*;
import org.apache.airavata.gsi.ssh.api.job.JobDescriptor;
import org.apache.airavata.gsi.ssh.config.ConfigReader;
import org.apache.airavata.gsi.ssh.jsch.ExtendedJSch;
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
import java.util.List;

/**
 * User: AmilaJ (amilaj@apache.org)
 * Date: 8/14/13
 * Time: 5:18 PM
 */

/**
 * Default SSH API implementation.
 */
public class DefaultSSHApi implements SSHApi {

    private static final Logger log = LoggerFactory.getLogger(DefaultSSHApi.class);

    private ConfigReader configReader;

    /**
     * Initializes default SSH API. During initialization basic configurations
     * are read.
     *
     * @throws SSHApiException If an error occurred while reading basic configurations.
     */
    public DefaultSSHApi() throws SSHApiException {
        try {
            this.configReader = new ConfigReader();
        } catch (IOException e) {
            throw new SSHApiException("Unable to load system configurations.", e);
        }
    }

    /**
     * @param commandInfo        Encapsulated information about command. E.g :- executable name
     *                           parameters etc ...
     * @param serverInfo         The SSHing server information.
     * @param authenticationInfo Security data needs to be communicated with remote server.
     * @param commandOutput      The output of the command.
     * @throws SSHApiException
     */
    public void executeCommand(CommandInfo commandInfo, ServerInfo serverInfo,
                               AuthenticationInfo authenticationInfo,
                               CommandOutput commandOutput) throws SSHApiException {

        JSch jsch = new ExtendedJSch();

        log.info("Connecting to server - " + serverInfo.getHost() + ":" + serverInfo.getPort() + " with user name - "
                + serverInfo.getUserName());

        Session session = null;

        try {
            session = jsch.getSession(serverInfo.getUserName(), serverInfo.getHost(), serverInfo.getPort());
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

        String command = commandInfo.getCommand();

        Channel channel = null;
        try {
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
        } catch (JSchException e) {
            session.disconnect();

            throw new SSHApiException("Unable to execute command - " + command +
                    " on server - " + serverInfo.getHost() + ":" + serverInfo.getPort() +
                    " connecting user name - "
                    + serverInfo.getUserName(), e);
        }


        channel.setInputStream(null);
        ((ChannelExec) channel).setErrStream(commandOutput.getStandardError());

        try {
            channel.connect();
        } catch (JSchException e) {

            channel.disconnect();
            session.disconnect();

            throw new SSHApiException("Unable to retrieve command output. Command - " + command +
                    " on server - " + serverInfo.getHost() + ":" + serverInfo.getPort() +
                    " connecting user name - "
                    + serverInfo.getUserName(), e);
        }

        commandOutput.onOutput(channel);

        channel.disconnect();
        session.disconnect();


    }

    /**
     * @param serverInfo
     * @param authenticationInfo
     * @param pbsFilePath
     * @param jobDescriptor
     * @throws SSHApiException
     */
    public String submitAsyncJobWithPBS(ServerInfo serverInfo,
                                        AuthenticationInfo authenticationInfo,
                                        String pbsFilePath, JobDescriptor jobDescriptor) throws SSHApiException {
        try {

            SCPTo scpTo = new SCPTo(serverInfo, authenticationInfo, new ConfigReader());
            scpTo.scpTo(jobDescriptor.getWorkingDirectory(), pbsFilePath);

        } catch (JSchException e) {
            throw new SSHApiException("An exception occurred while connecting to server." +
                    "Connecting server - " + serverInfo.getHost() + ":" + serverInfo.getPort() +
                    " connecting user name - "
                    + serverInfo.getUserName(), e);
        } catch (IOException e) {
            throw new SSHApiException("An exception occurred while connecting to server." +
                    "Connecting server - " + serverInfo.getHost() + ":" + serverInfo.getPort() +
                    " connecting user name - "
                    + serverInfo.getUserName(), e);
        }
        // since this is a constant we do not ask users to fill this
        RawCommandInfo rawCommandInfo = new RawCommandInfo("/opt/torque/bin/qsub " +
                jobDescriptor.getWorkingDirectory() + File.separator + FilenameUtils.getName(pbsFilePath));

        StandardOutReader jobIDReaderCommandOutput = new StandardOutReader();
        this.executeCommand(rawCommandInfo, serverInfo, authenticationInfo, jobIDReaderCommandOutput);

        //Check whether pbs submission is successful or not, if it failed throw and exception in submitJob method
        // with the error thrown in qsub command
        if (jobIDReaderCommandOutput.getErrorifAvailable().equals("")) {
            return jobIDReaderCommandOutput.getStdOutput();
        } else {
            throw new SSHApiException(jobIDReaderCommandOutput.getStandardError().toString());
        }
    }

    /**
     * @param serverInfo
     * @param authenticationInfo
     * @param jobDescriptor
     * @throws SSHApiException
     */
    public String submitAsyncJob(ServerInfo serverInfo, AuthenticationInfo authenticationInfo, JobDescriptor jobDescriptor) throws SSHApiException {
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

            System.out.println("***********************************");
            System.out.println(results.toString());
            System.out.println("***********************************");

            // creating a temporary file using pbs script generated above
            int number = new SecureRandom().nextInt();
            number = (number < 0 ? -number : number);

            tempPBSFile = new File(Integer.toString(number) + ".pbs");
            FileUtils.writeStringToFile(tempPBSFile, results.toString());

            //reusing submitAsyncJobWithPBS method to submit a job

            String jobID = this.submitAsyncJobWithPBS(serverInfo, authenticationInfo, tempPBSFile.getAbsolutePath(), jobDescriptor);
            return jobID.replace("\n","");
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

    public Cluster getCluster(ServerInfo serverInfo, AuthenticationInfo authenticationInfo) throws SSHApiException {
        RawCommandInfo rawCommandInfo = new RawCommandInfo("/opt/torque/bin/qnodes");

        StandardOutReader stdOutReader = new StandardOutReader();
        this.executeCommand(rawCommandInfo, serverInfo, authenticationInfo, stdOutReader);
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
                    JobDescriptor jo = new JobDescriptor();
                    //Job[] Jobs = new Job[jobs.length];
                    for (int j = 0; j < jobs.length; j++) {
                        String[] c = jobs[j].split("/");
                        String Jid = c[1];
                        jo = this.getJobById(serverInfo, authenticationInfo, Jid);
                        int core = Integer.parseInt(c[0]);
                        Cores[core].setJob(jo);

                    }


                }
                i++;
            }
            Node.setCores(Cores);
            Machines.add(Node);
        }
        Cluster c = new Cluster();
        c.setNodes(Machines.toArray(new Machine[Machines.size()]));
        return c;
    }

    public JobDescriptor getJobById(ServerInfo serverInfo, AuthenticationInfo authenticationInfo, String jobID) throws SSHApiException {
        RawCommandInfo rawCommandInfo = new RawCommandInfo("/opt/torque/bin/qstat -f " + jobID);

        StandardOutReader stdOutReader = new StandardOutReader();
        this.executeCommand(rawCommandInfo, serverInfo, authenticationInfo, stdOutReader);
        String result = stdOutReader.getStdOutput();
        String[] info = result.split("\n");
        JobDescriptor jobDescriptor = new JobDescriptor();
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
                //                  System.out.println("Header = " + header);
                value = line[1].trim();
//                    System.out.println("value = " + value);

                if (header.equals("Variable_List")) {
                    while (info[i + 1].startsWith("\t")) {
                        value += info[i + 1];
                        i++;
                    }
                    value = value.replaceAll("\t", "");
//                        jobDescriptor.VariablesList=value;
//                        jobDescriptor.analyzeVariableList(value);
                } else if ("Job Id".equals(header))
                    jobDescriptor.setJobID(value);
                else if ("Job_Name".equals(header))
                    jobDescriptor.setJobName(value);

                else if ("Job_Owner".equals(header)) {
//                       jobDescriptor.setOwner(value);
                } else if ("resources_used.cput".equals(header)) {
//                       jobDescriptor.setUsedcput(value);
                } else if ("resources_used.mem".equals(header)) {
//                       jobDescriptor.setUsedMem(value);
                } else if ("resources_used.walltime".equals(header)) {
//                       jobDescriptor.setEllapsedTime(value);
                } else if ("job_state".equals(header))
                    jobDescriptor.setStatus(value);

                else if ("queue".equals(header))
                    jobDescriptor.setQueueName(value);
                else if ("ctime".equals(header)) {
//                       jobDescriptor.setCtime(value);
                } else if ("qtime".equals(header)) {
//                       jobDescriptor.setQtime(value);
                } else if ("mtime".equals(header)) {
//                       jobDescriptor.setMtime(value);
                } else if ("start_time".equals(header)) {
//                        jobDescriptor.setStime(value);
                } else if ("comp_time".equals(header)) {
//                            jobDescriptor.setComp_time(value);
                } else if ("exec_host".equals(header)) {
//                       jobDescriptor.setExecuteNode(value);
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
//                         jobDescriptor.setSubmitArgs (value);
                }


            }
        }
        return jobDescriptor;

    }
}
