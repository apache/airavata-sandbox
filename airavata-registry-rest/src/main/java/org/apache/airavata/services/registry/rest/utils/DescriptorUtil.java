package org.apache.airavata.services.registry.rest.utils;

import org.apache.airavata.client.api.AiravataAPI;
import org.apache.airavata.client.api.AiravataAPIInvocationException;
import org.apache.airavata.client.api.ApplicationManager;
import org.apache.airavata.commons.gfac.type.ApplicationDeploymentDescription;
import org.apache.airavata.commons.gfac.type.HostDescription;
import org.apache.airavata.commons.gfac.type.ServiceDescription;
import org.apache.airavata.schemas.gfac.*;

import javax.ws.rs.FormParam;
import java.util.ArrayList;
import java.util.List;

public class DescriptorUtil {

    public static HostDescription createHostDescription(String hostName, String hostAddress,
                                                        String hostEndpoint, String gatekeeperEndpoint) {
        HostDescription host = new HostDescription();
        if("".equalsIgnoreCase(gatekeeperEndpoint) || "".equalsIgnoreCase(hostEndpoint)) {
            host.getType().changeType(GlobusHostType.type);
            host.getType().setHostName(hostName);
            host.getType().setHostAddress(hostAddress);
            ((GlobusHostType) host.getType()).
                    setGridFTPEndPointArray(new String[]{hostEndpoint});
            ((GlobusHostType) host.getType()).
                    setGlobusGateKeeperEndPointArray(new String[]{gatekeeperEndpoint});
        } else {
            host.getType().setHostName(hostName);
            host.getType().setHostAddress(hostAddress);
        }
        return host;
    }

    public static ApplicationDeploymentDescription registerApplication(String appName, String exeuctableLocation, String scratchWorkingDirectory, String hostName,
                                                                       String projAccNumber, String queueName, String cpuCount, String nodeCount, String maxMemory) throws Exception {
        // Create Application Description
        ApplicationDeploymentDescription appDesc = new ApplicationDeploymentDescription(GramApplicationDeploymentType.type);
        GramApplicationDeploymentType app = (GramApplicationDeploymentType) appDesc.getType();
        app.setCpuCount(Integer.parseInt(cpuCount));
        app.setNodeCount(Integer.parseInt(nodeCount));
        ApplicationDeploymentDescriptionType.ApplicationName name = appDesc.getType().addNewApplicationName();
        name.setStringValue(appName);
        app.setExecutableLocation(exeuctableLocation);
        app.setScratchWorkingDirectory(scratchWorkingDirectory);
        ProjectAccountType projectAccountType = ((GramApplicationDeploymentType) appDesc.getType()).addNewProjectAccount();
        projectAccountType.setProjectAccountNumber(projAccNumber);
        QueueType queueType = app.addNewQueue();
        queueType.setQueueName(queueName);
        app.setMaxMemory(Integer.parseInt(maxMemory));
        return appDesc;
    }

    public static ServiceDescription getServiceDescription(String serviceName, String inputName, String inputType,
                                                           String outputName, String outputType) {
        // Create Service Description
        ServiceDescription serv = new ServiceDescription();
        serv.getType().setName(serviceName);

        InputParameterType input = InputParameterType.Factory.newInstance();
        input.setParameterName(inputName);
        ParameterType parameterType = input.addNewParameterType();
        parameterType.setType(DataType.Enum.forString(inputType));
        parameterType.setName(inputName);
        List<InputParameterType> inputList = new ArrayList<InputParameterType>();
        inputList.add(input);
        InputParameterType[] inputParamList = inputList.toArray(new InputParameterType[inputList
                .size()]);

        OutputParameterType output = OutputParameterType.Factory.newInstance();
        output.setParameterName(outputName);
        ParameterType parameterType1 = output.addNewParameterType();
        parameterType1.setType(DataType.Enum.forString(outputType));
        parameterType1.setName(outputName);
        List<OutputParameterType> outputList = new ArrayList<OutputParameterType>();
        outputList.add(output);
        OutputParameterType[] outputParamList = outputList
                .toArray(new OutputParameterType[outputList.size()]);
        serv.getType().setInputParametersArray(inputParamList);
        serv.getType().setOutputParametersArray(outputParamList);
        return serv;
    }
}
