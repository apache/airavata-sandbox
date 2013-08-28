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
package org.apache.airavata.gsi.ssh.api.job;

import org.apache.airavata.gsi.ssh.api.CommandInfo;
import org.apache.airavata.gsi.ssh.api.CommandOutput;
import org.apache.airavata.gsi.ssh.x2012.x12.InputList;
import org.apache.airavata.gsi.ssh.x2012.x12.JobDescriptorDocument;
import org.apache.airavata.gsi.ssh.x2012.x12.PbsParams;
import org.apache.airavata.gsi.ssh.x2012.x12.impl.PbsParamsImpl;
import org.apache.xmlbeans.XmlException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class define a job with required parameters, based on this configuration API is generating a Pbs script and
 * submit the job to the computing resource
 */
public class JobDescriptor {

    private JobDescriptorDocument jobDescriptionDocument;

    private CommandInfo commandInfo;

    private CommandOutput commandOutput;

    public JobDescriptor() {
        jobDescriptionDocument = JobDescriptorDocument.Factory.newInstance();
        jobDescriptionDocument.addNewJobDescriptor();
    }

    public JobDescriptor(JobDescriptorDocument jobDescriptorDocument) {
        this.jobDescriptionDocument = jobDescriptorDocument;
    }


    public JobDescriptor(CommandOutput commandOutput) {
        jobDescriptionDocument = JobDescriptorDocument.Factory.newInstance();
        jobDescriptionDocument.addNewJobDescriptor();
        this.commandOutput = commandOutput;
    }


    public String toXML() {
        return jobDescriptionDocument.xmlText();
    }

    public JobDescriptorDocument getJobDescriptorDocument() {
        return this.jobDescriptionDocument;
    }

    public static JobDescriptor fromXML(String xml)
            throws XmlException {
        JobDescriptorDocument parse = JobDescriptorDocument.Factory
                .parse(xml);
        JobDescriptor jobDescriptor = new JobDescriptor(parse);
        return jobDescriptor;
    }

    public void setCommandInfo(CommandInfo commandInfo) {
        this.commandInfo = commandInfo;
    }

    public void setCommandOutput(CommandOutput commandOutput) {
        this.commandOutput = commandOutput;
    }

    public CommandInfo getCommandInfo() {
        return commandInfo;
    }

    public CommandOutput getCommandOutput() {
        return commandOutput;
    }

    //todo write bunch of setter getters to set and get jobdescription parameters
    public void setWorkingDirectory(String workingDirectory) {
        this.getJobDescriptorDocument().getJobDescriptor().setWorkingDirectory(workingDirectory);
    }

    public String getWorkingDirectory() {
        return this.getJobDescriptorDocument().getJobDescriptor().getWorkingDirectory();
    }

    public void setShellName(String shellName) {
        this.getJobDescriptorDocument().getJobDescriptor().setShellName(shellName);
    }

    public void setJobName(String name) {
        this.getJobDescriptorDocument().getJobDescriptor().setJobName(name);
    }

    public void setExecutablePath(String name) {
        this.getJobDescriptorDocument().getJobDescriptor().setExecutablePath(name);
    }

    public void setAllEnvExport(boolean name) {
        this.getJobDescriptorDocument().getJobDescriptor().setAllEnvExport(name);
    }

    public void setMailOptions(String name) {
        this.getJobDescriptorDocument().getJobDescriptor().setMailOptions(name);
    }

    public void setStandardOutFile(String name) {
        this.getJobDescriptorDocument().getJobDescriptor().setStandardOutFile(name);
    }

    public void setStandardErrorFile(String name) {
        this.getJobDescriptorDocument().getJobDescriptor().setStandardErrorFile(name);
    }

    public void setNodes(int name) {
        this.getJobDescriptorDocument().getJobDescriptor().setNodes(name);
    }

    public void setProcessesPerNode(int name) {
        this.getJobDescriptorDocument().getJobDescriptor().setProcessesPerNode(name);
    }

    public void setMaxWallTime(String name) {
        this.getJobDescriptorDocument().getJobDescriptor().setMaxWallTime(name);
    }

    public void setAcountString(String name) {
        this.getJobDescriptorDocument().getJobDescriptor().setAcountString(name);
    }

    public void setInputValues(List<String> inputValue){
        InputList inputList = this.getJobDescriptorDocument().getJobDescriptor().addNewInputs();
        inputList.setInputArray(inputValue.toArray(new String[inputValue.size()]));
    }
}

