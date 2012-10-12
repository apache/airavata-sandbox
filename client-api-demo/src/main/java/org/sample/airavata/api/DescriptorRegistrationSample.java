package org.sample.airavata.api;

import org.apache.airavata.client.api.AiravataAPI;
import org.apache.airavata.client.api.AiravataAPIInvocationException;
import org.apache.airavata.client.api.ApplicationManager;
import org.apache.airavata.commons.gfac.type.ActualParameter;
import org.apache.airavata.commons.gfac.type.ApplicationDeploymentDescription;
import org.apache.airavata.commons.gfac.type.HostDescription;
import org.apache.airavata.commons.gfac.type.ServiceDescription;
import org.apache.airavata.core.gfac.context.invocation.impl.DefaultExecutionContext;
import org.apache.airavata.core.gfac.context.invocation.impl.DefaultInvocationContext;
import org.apache.airavata.core.gfac.context.message.impl.ParameterContextImpl;
import org.apache.airavata.core.gfac.context.security.impl.GSISecurityContext;
import org.apache.airavata.core.gfac.services.impl.PropertiesBasedServiceImpl;
import org.apache.airavata.schemas.gfac.*;
import org.apache.xmlbeans.SchemaType;

import java.util.ArrayList;
import java.util.List;

public class DescriptorRegistrationSample {
    public static final String MYPROXY = "myproxy";

    public static void main(String[] args) {

    }

    // TODO
    public static void registerApplication(AppDescriptorBean appDescriptorBean) throws Exception {
        AiravataAPI airavataAPI = SampleUtil.getAiravataAPI();

        // Create Host Description
        HostDescription host = new HostDescription();
        host.getType().changeType(appDescriptorBean.getHostType());
        host.getType().setHostName(appDescriptorBean.getHostName());
        host.getType().setHostAddress(appDescriptorBean.getHostAddress());
        ((GlobusHostType) host.getType()).
                setGridFTPEndPointArray(new String[]{appDescriptorBean.getHostEPR()});
        ((GlobusHostType) host.getType()).
                setGlobusGateKeeperEndPointArray(new String[]{appDescriptorBean.getGateKeeperEPR()});

        // Create Application Description
        ApplicationDeploymentDescription appDesc = new ApplicationDeploymentDescription(GramApplicationDeploymentType.type);
        GramApplicationDeploymentType app = (GramApplicationDeploymentType) appDesc.getType();
        app.setCpuCount(appDescriptorBean.getCpuCount());
        app.setNodeCount(appDescriptorBean.getNodeCount());
        ApplicationDeploymentDescriptionType.ApplicationName name = appDesc.getType().addNewApplicationName();
        name.setStringValue(appDescriptorBean.getApplicationName());
        app.setExecutableLocation(appDescriptorBean.getExecutable());
        app.setScratchWorkingDirectory(appDescriptorBean.getScratchWorkingDir());
        ProjectAccountType projectAccountType = ((GramApplicationDeploymentType) appDesc.getType()).addNewProjectAccount();
        projectAccountType.setProjectAccountNumber(appDescriptorBean.getProjectAccNumber());
        QueueType queueType = app.addNewQueue();
        queueType.setQueueName(appDescriptorBean.getQueueName());
        app.setMaxMemory(appDescriptorBean.getMaxMemory());

        // Create Service Description
        ServiceDescription serv = new ServiceDescription();
        serv.getType().setName(appDescriptorBean.getServiceName());

        InputParameterType input = InputParameterType.Factory.newInstance();
        input.setParameterName(appDescriptorBean.getInputName());
        ParameterType parameterType = input.addNewParameterType();
        parameterType.setType(DataType.Enum.forString(appDescriptorBean.getInputType()));
        parameterType.setName(appDescriptorBean.getInputType());
        List<InputParameterType> inputList = new ArrayList<InputParameterType>();
        inputList.add(input);
        InputParameterType[] inputParamList = inputList.toArray(new InputParameterType[inputList
                .size()]);

        OutputParameterType output = OutputParameterType.Factory.newInstance();
        output.setParameterName(appDescriptorBean.getOutputName());
        ParameterType parameterType1 = output.addNewParameterType();
        parameterType1.setType(DataType.Enum.forString(appDescriptorBean.getOutputType()));
        parameterType1.setName(appDescriptorBean.getOutputType());
        List<OutputParameterType> outputList = new ArrayList<OutputParameterType>();
        outputList.add(output);
        OutputParameterType[] outputParamList = outputList
                .toArray(new OutputParameterType[outputList.size()]);
        serv.getType().setInputParametersArray(inputParamList);
        serv.getType().setOutputParametersArray(outputParamList);

        // Save to Registry
        if (airavataAPI!=null) {
            ApplicationManager applicationManager = airavataAPI.getApplicationManager();
            System.out.println("Saving to Registry");
            try {
                applicationManager.saveHostDescription(host);
                applicationManager.saveServiceDescription(serv);
                applicationManager.saveDeploymentDescription(serv.getType().getName(), host.getType().getHostName(), appDesc);
            } catch (AiravataAPIInvocationException e) {
                e.printStackTrace();
                throw new Exception(e);
            }
        }
        System.out.println("DONE");


    }

    public static void execute(String username,
                               String password,
                               String registryRMIURI,
                               String trustedCertLoc,
                               String serviceName,
                               String inputName,
                               String inputValue,
                               String outputName,
                               String myproxyServer,
                               String myproxyUsername,
                               String myproxyPassword) {
        try {

            DefaultInvocationContext ct = new DefaultInvocationContext();
            AiravataAPI airavataAPI = SampleUtil.getAiravataAPI();
            DefaultExecutionContext ec = airavataAPI.getExecutionManager().createDefaultExecutionContext();
            ct.setExecutionContext(ec);

            GSISecurityContext gsiSecurityContext = new GSISecurityContext();
            gsiSecurityContext.setMyproxyServer(myproxyServer);
            gsiSecurityContext.setMyproxyUserName(myproxyUsername);
            gsiSecurityContext.setMyproxyPasswd(myproxyPassword);
            gsiSecurityContext.setMyproxyLifetime(14400);
            gsiSecurityContext.setTrustedCertLoc(trustedCertLoc);

            ct.addSecurityContext(MYPROXY, gsiSecurityContext);
            ct.setServiceName(serviceName);

            // Input
            ParameterContextImpl input = new ParameterContextImpl();
            ActualParameter echo_input = new ActualParameter();
            ((StringParameterType) echo_input.getType()).setValue(inputValue);
            input.add(inputName, echo_input);

            // Output
            ParameterContextImpl output = new ParameterContextImpl();
            ActualParameter echo_output = new ActualParameter();
            output.add(outputName, echo_output);

            // parameter
            ct.setInput(input);
            ct.setOutput(output);

            PropertiesBasedServiceImpl service = new PropertiesBasedServiceImpl();
            service.init();
            service.execute(ct);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
