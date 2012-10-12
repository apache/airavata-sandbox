package org.sample.airavata.api;

import org.apache.airavata.client.AiravataClient;
import org.apache.airavata.client.AiravataClientUtils;
import org.apache.airavata.client.api.AiravataAPI;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SampleUtil {

    public static AiravataAPI getAiravataAPI() {
        AiravataAPI airavataAPI = null;
        try {
            airavataAPI = AiravataClientUtils.getAPI(createConfigMap());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return airavataAPI;
    }


    private static Map<String, String> createConfigMap() {
        Properties prop = new Properties();
        InputStream resourceAsStream = SampleUtil.class.getClassLoader().getResourceAsStream("/deployment.properties");
        try {
            prop.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String hostName = prop.getProperty(WebAppConstants.HOST_NAME);
        String certLocation = prop.getProperty(WebAppConstants.CA_CERT_PATH);

        Map<String, String> config = new HashMap<String, String>();
        config.put(AiravataClient.MSGBOX, hostName + "/axis2/services/MsgBoxService");
        config.put(AiravataClient.BROKER, hostName + "/axis2/services/EventingService");
        config.put(AiravataClient.WORKFLOWSERVICEURL, hostName + "/axis2/services/WorkflowInterpretor?wsdl");
        config.put(AiravataClient.JCR, hostName + "/jackrabbit-webapp-2.4.0/rmi");
        config.put(AiravataClient.JCR_USERNAME, "admin");
        config.put(AiravataClient.JCR_PASSWORD, "admin");
        config.put(AiravataClient.GFAC, hostName + "/axis2/services/GFacService");
        config.put(AiravataClient.WITHLISTENER, "false");
        config.put(AiravataClient.TRUSTED_CERT_LOCATION, certLocation);

        return config;
    }

}
