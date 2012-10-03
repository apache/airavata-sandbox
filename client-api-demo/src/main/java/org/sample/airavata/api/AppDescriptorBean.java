package org.sample.airavata.api;

import org.apache.xmlbeans.SchemaType;

public class AppDescriptorBean {
    private final String username;
    private final String password;
    private final String registryRMIURI;
    private final SchemaType hostType;
    private final String hostName;
    private final String hostAddress;
    private final String hostEPR;
    private final String gateKeeperEPR;
    private final String applicationName;
    private final String executable;
    private final String scratchWorkingDir;
    private final String projectAccNumber;
    private final String queueName;
    private final int cpuCount;
    private final int nodeCount;
    private final int maxMemory;
    private final String serviceName;
    private final String inputName;
    private final String outputName;
    private final String inputType;
    private final String outputType;

    public AppDescriptorBean(String username, String password, String registryRMIURI, SchemaType hostType,
                             String hostName, String hostAddress, String hostEPR, String gateKeeperEPR,
                             String applicationName, String executable, String scratchWorkingDir,
                             String projectAccNumber, String queueName, int cpuCount, int nodeCount,
                             int maxMemory, String serviceName, String inputName, String outputName,
                             String inputType, String outputType) {
        this.username = username;
        this.password = password;
        this.registryRMIURI = registryRMIURI;
        this.hostType = hostType;
        this.hostName = hostName;
        this.hostAddress = hostAddress;
        this.hostEPR = hostEPR;
        this.gateKeeperEPR = gateKeeperEPR;
        this.applicationName = applicationName;
        this.executable = executable;
        this.scratchWorkingDir = scratchWorkingDir;
        this.projectAccNumber = projectAccNumber;
        this.queueName = queueName;
        this.cpuCount = cpuCount;
        this.nodeCount = nodeCount;
        this.maxMemory = maxMemory;
        this.serviceName = serviceName;
        this.inputName = inputName;
        this.outputName = outputName;
        this.inputType = inputType;
        this.outputType = outputType;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRegistryRMIURI() {
        return registryRMIURI;
    }

    public SchemaType getHostType() {
        return hostType;
    }

    public String getHostName() {
        return hostName;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public String getHostEPR() {
        return hostEPR;
    }

    public String getGateKeeperEPR() {
        return gateKeeperEPR;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getExecutable() {
        return executable;
    }

    public String getScratchWorkingDir() {
        return scratchWorkingDir;
    }

    public String getProjectAccNumber() {
        return projectAccNumber;
    }

    public String getQueueName() {
        return queueName;
    }

    public int getCpuCount() {
        return cpuCount;
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public int getMaxMemory() {
        return maxMemory;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getInputName() {
        return inputName;
    }

    public String getOutputName() {
        return outputName;
    }

    public String getInputType() {
        return inputType;
    }

    public String getOutputType() {
        return outputType;
    }
}
