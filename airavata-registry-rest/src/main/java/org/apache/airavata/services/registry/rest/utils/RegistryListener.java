package org.apache.airavata.services.registry.rest.utils;

import org.apache.airavata.registry.api.AiravataRegistry2;
import org.apache.airavata.registry.api.AiravataRegistryFactory;
import org.apache.airavata.registry.api.AiravataUser;
import org.apache.airavata.registry.api.Gateway;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class RegistryListener implements ServletContextListener {
    public static final String AIRAVATA_SERVER_PROPERTIES = "airavata-server.properties";
    private static AiravataRegistry2 airavataRegistry;

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try{
            ServletContext servletContext = servletContextEvent.getServletContext();

            URL url = this.getClass().getClassLoader().getResource(AIRAVATA_SERVER_PROPERTIES);
            Properties properties = new Properties();
            try {
                properties.load(url.openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String gatewayID = properties.getProperty("gateway.id");

        airavataRegistry = AiravataRegistryFactory.getRegistry(new Gateway(gatewayID), new AiravataUser("admin"));
        servletContext.setAttribute("airavataRegistry", airavataRegistry);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
