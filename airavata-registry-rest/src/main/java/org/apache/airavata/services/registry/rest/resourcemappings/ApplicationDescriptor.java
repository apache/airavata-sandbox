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

package org.apache.airavata.services.registry.rest.resourcemappings;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ApplicationDescriptor {
    private String appDocument;
    private String hostdescName;
    private ServiceDescriptor serviceDesc;
    private String executablePath;
    private String workingDir;
    private String jobType;
    private String projectNumber;
    private String queueName;
    private int maxWallTime;
    private int cpuCount;
    private int nodeCount;
    private int processorsPerNode;
    private int minMemory;
    private int maxMemory;

	public String getAppDocument() {
		return appDocument;
	}
	public void setAppDocument(String appDocument) {
		this.appDocument = appDocument;
	}
	public String getHostdescName() {
		return hostdescName;
	}
	public void setHostdescName(String hostdescName) {
		this.hostdescName = hostdescName;
	}
	public ServiceDescriptor getServiceDesc() {
		return serviceDesc;
	}
	public void setServiceDesc(ServiceDescriptor serviceDesc) {
		this.serviceDesc = serviceDesc;
	}
	public String getExecutablePath() {
		return executablePath;
	}
	public void setExecutablePath(String executablePath) {
		this.executablePath = executablePath;
	}
	public String getWorkingDir() {
		return workingDir;
	}
	public void setWorkingDir(String workingDir) {
		this.workingDir = workingDir;
	}
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	public String getProjectNumber() {
		return projectNumber;
	}
	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}
	public String getQueueName() {
		return queueName;
	}
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	public int getMaxWallTime() {
		return maxWallTime;
	}
	public void setMaxWallTime(int maxWallTime) {
		this.maxWallTime = maxWallTime;
	}
	public int getCpuCount() {
		return cpuCount;
	}
	public void setCpuCount(int cpuCount) {
		this.cpuCount = cpuCount;
	}
	public int getNodeCount() {
		return nodeCount;
	}
	public void setNodeCount(int nodeCount) {
		this.nodeCount = nodeCount;
	}
	public int getProcessorsPerNode() {
		return processorsPerNode;
	}
	public void setProcessorsPerNode(int processorsPerNode) {
		this.processorsPerNode = processorsPerNode;
	}
	public int getMinMemory() {
		return minMemory;
	}
	public void setMinMemory(int minMemory) {
		this.minMemory = minMemory;
	}
	public int getMaxMemory() {
		return maxMemory;
	}
	public void setMaxMemory(int maxMemory) {
		this.maxMemory = maxMemory;
	}

}
