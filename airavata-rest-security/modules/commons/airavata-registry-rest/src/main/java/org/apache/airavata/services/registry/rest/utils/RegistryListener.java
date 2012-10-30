package org.apache.airavata.services.registry.rest.utils;

import org.apache.airavata.credential.store.CredentialStore;
import org.apache.airavata.credential.store.impl.CredentialStoreImpl;
import org.apache.airavata.credential.store.util.DBUtil;
import org.apache.airavata.registry.api.AiravataRegistry;
import org.apache.airavata.registry.api.Axis2Registry;
import org.apache.airavata.registry.api.DataRegistry;
import org.apache.airavata.registry.api.impl.AiravataJCRRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.net.URI;
import java.util.HashMap;

public class RegistryListener implements ServletContextListener {

    private static AiravataRegistry airavataRegistry;
    private static Axis2Registry axis2Registry;
    private static DataRegistry dataRegistry;

    public static final String CREDENTIAL_STORE = "credentialStore";


    protected static Logger log = LoggerFactory.getLogger(RegistryListener.class);


    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            ServletContext servletContext = servletContextEvent.getServletContext();
            URI url = new URI("http://localhost:8081/rmi");
            String username = "admin";
            String password = "admin";
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("org.apache.jackrabbit.repository.uri", url.toString());


            airavataRegistry = new AiravataJCRRegistry(url,
                    "org.apache.jackrabbit.rmi.repository.RmiRepositoryFactory", username, password, map);

            axis2Registry = new AiravataJCRRegistry(url,
                    "org.apache.jackrabbit.rmi.repository.RmiRepositoryFactory", username, password, map);

            dataRegistry = new AiravataJCRRegistry(url,
                    "org.apache.jackrabbit.rmi.repository.RmiRepositoryFactory", username, password, map);

            servletContext.setAttribute("airavataRegistry", airavataRegistry);
            servletContext.setAttribute("axis2Registry", axis2Registry);
            servletContext.setAttribute("dataRegistry", dataRegistry);

            initializeCredentialStoreAPI(servletContext);

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void initializeCredentialStoreAPI(ServletContext servletContext) throws Exception {

        CredentialStore credentialStore = new CredentialStoreImpl(DBUtil.getDBUtil(servletContext));
        servletContext.setAttribute(CREDENTIAL_STORE, credentialStore);

    }


    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
