package org.apache.airavata.client.registry.samples;

import org.apache.airavata.api.Airavata;
import org.apache.airavata.api.client.AiravataClientFactory;
import org.apache.airavata.client.registry.tools.GenAppUtils;
import org.apache.airavata.client.registry.tools.RegisterGenAppUtils;

import org.apache.airavata.model.appcatalog.appdeployment.ApplicationParallelismType;
import org.apache.airavata.model.appcatalog.appinterface.DataType;
import org.apache.airavata.model.appcatalog.appinterface.InputDataObjectType;
import org.apache.airavata.model.appcatalog.appinterface.OutputDataObjectType;
import org.apache.airavata.model.appcatalog.computeresource.ComputeResourceDescription;
import org.apache.airavata.model.appcatalog.computeresource.LOCALSubmission;
import org.apache.airavata.model.appcatalog.computeresource.ResourceJobManager;
import org.apache.airavata.model.appcatalog.computeresource.ResourceJobManagerType;
import org.apache.airavata.model.appcatalog.gatewayprofile.ComputeResourcePreference;
import org.apache.airavata.model.appcatalog.gatewayprofile.GatewayResourceProfile;
import org.apache.airavata.model.error.AiravataClientConnectException;
import org.apache.thrift.TException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class RegisterGenApp {

    private static final String THRIFT_SERVER_HOST = "127.0.0.1";
    private static final int THRIFT_SERVER_PORT = 8930;
    private static final String DEFAULT_GATEWAY = "Sample";
    private Airavata.Client airavataClient;
    private String localhostId ;
    private List<String> modules;
    private HashMap<String, String> moduleIds;
    private String executablePath;

    public static void main(String[] args) throws AiravataClientConnectException, TException {
        RegisterGenApp registerGenApp = new RegisterGenApp();
        registerGenApp.init();
        registerGenApp.register();
    }

    public void init() {
        executablePath = GenAppUtils.GetExectuablePath();
        modules = GenAppUtils.GetModuleList();
//        System.out.println(executablePath);
    }

    public void register() throws AiravataClientConnectException, TException {
        airavataClient = AiravataClientFactory.createAiravataClient(THRIFT_SERVER_HOST, THRIFT_SERVER_PORT);
        registerLocalhost();
//        registerGatewayProfile();
        registerApplicationModules();
        registerApplicationDeployments();
        registerApplicationInterfaces();
    }
    
//    private void registerGatewayProfile() throws TException {
//        ComputeResourcePreference localhostResourcePreference = RegisterGenAppUtils.
//                createComputeResourcePreference(localhostId, "Sample", false, null, null, null, executablePath + "/..");
//        GatewayResourceProfile gatewayResourceProfile = new GatewayResourceProfile();
//        gatewayResourceProfile.setGatewayID(DEFAULT_GATEWAY);
//        gatewayResourceProfile.setGatewayName(DEFAULT_GATEWAY);
//        gatewayResourceProfile.addToComputeResourcePreferences(localhostResourcePreference);
//        airavataClient.registerGatewayResourceProfile(gatewayResourceProfile);
//    }
//


    private void registerLocalhost() {
        try {
            System.out.println("\n #### Registering Localhost Computational Resource #### \n");

            ComputeResourceDescription computeResourceDescription = RegisterGenAppUtils.
                    createComputeResourceDescription("localhost", "LocalHost", null, null);
            localhostId = airavataClient.registerComputeResource(computeResourceDescription);
            ResourceJobManager resourceJobManager = RegisterGenAppUtils.
                    createResourceJobManager(ResourceJobManagerType.FORK, null, null, null);
            LOCALSubmission submission = new LOCALSubmission();
            submission.setResourceJobManager(resourceJobManager);
            String localSubmission = airavataClient.addLocalSubmissionDetails(localhostId, 1, submission);
//            if (!localSubmission) throw new AiravataClientException();
            System.out.println(localSubmission);
            System.out.println("LocalHost Resource Id is " + localhostId);

        } catch (TException e) {
            e.printStackTrace();
        }
    }

    private void registerApplicationInterfaces() {
        Iterator<String> it = modules.iterator();
        while(it.hasNext()){
            try {
                System.out.println("#### Registering Echo Interface ####");
                String id = it.next();
                List<String> appModules = new ArrayList<String>();
                appModules.add(moduleIds.get(id));

                InputDataObjectType input1 = RegisterGenAppUtils.createAppInput("Input_JSON", "{}",
                        DataType.STRING, null, false, "JSON String", null);

                List<InputDataObjectType> applicationInputs = new ArrayList<InputDataObjectType>();
                applicationInputs.add(input1);

                OutputDataObjectType output1 = RegisterGenAppUtils.createAppOutput("JSON_Output",
                        "{}", DataType.STRING);

                List<OutputDataObjectType> applicationOutputs = new ArrayList<OutputDataObjectType>();
                applicationOutputs.add(output1);

                String InterfaceId = airavataClient.registerApplicationInterface(
                        RegisterGenAppUtils.createApplicationInterfaceDescription(id , id+" application description",
                                appModules, applicationInputs, applicationOutputs));
                System.out.println(id+" Application Interface Id " + InterfaceId);

            } catch (TException e) {
                e.printStackTrace();
            }
        }

    }

    private void registerApplicationDeployments() throws TException {
        System.out.println("#### Registering Application Deployments on Localhost ####");
        Iterator<String> it = modules.iterator();
        while(it.hasNext()){
            String id = it.next();
            String deployId = airavataClient.registerApplicationDeployment(
                    RegisterGenAppUtils.createApplicationDeployment(moduleIds.get(id), localhostId,
                            executablePath + "/"+id, ApplicationParallelismType.SERIAL, id+" application description"));
            System.out.println("Successfully registered "+id+" application on localhost, application Id = "+deployId);
        }
    }

    private void registerApplicationModules() throws TException {
        moduleIds = new HashMap<String, String>();
        Iterator<String> it = modules.iterator();
        while(it.hasNext()){
            String id = it.next();
            moduleIds.put(id, airavataClient.registerApplicationModule(
                    RegisterGenAppUtils.createApplicationModule(
                            id, "1.0", id+" application description")));
        }
    }
}
