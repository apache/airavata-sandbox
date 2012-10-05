package org.sample.airavata.api;

import org.apache.airavata.client.AiravataClientUtils;
import org.apache.airavata.client.api.AiravataAPI;
import org.apache.airavata.common.registry.api.exception.RegistryException;

import javax.jcr.RepositoryException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public class SampleUtil {

    public static AiravataAPI getAiravataAPI(String username, String password, String registryRMIURI) {
        URI registryURI = null;
        try {
            registryURI = new URI(registryRMIURI);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        AiravataAPI airavataAPI = null;
        try {
            airavataAPI = AiravataClientUtils.getAPI(registryURI, username, password);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RepositoryException e) {
            e.printStackTrace();
        } catch (RegistryException e) {
            e.printStackTrace();
        }
        return airavataAPI;
    }

}
