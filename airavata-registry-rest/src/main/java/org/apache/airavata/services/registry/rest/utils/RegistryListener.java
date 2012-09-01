package org.apache.airavata.services.registry.rest.utils;

import org.apache.airavata.common.registry.api.exception.RegistryException;
import org.apache.airavata.registry.api.AiravataRegistry;
import org.apache.airavata.registry.api.Axis2Registry;
import org.apache.airavata.registry.api.DataRegistry;
import org.apache.airavata.registry.api.impl.AiravataJCRRegistry;

import javax.jcr.RepositoryException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

public class RegistryListener implements ServletContextListener {

    private static AiravataRegistry airavataRegistry;
    private static Axis2Registry axis2Registry;
    private static DataRegistry dataRegistry;

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try{
        ServletContext servletContext = servletContextEvent.getServletContext();
        URI url = new URI("http://localhost:8081/rmi");
        String username = "admin";
        String password = "admin";
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("org.apache.jackrabbit.repository.uri", url.toString());


        airavataRegistry = new AiravataJCRRegistry(url,
                "org.apache.jackrabbit.rmi.repository.RmiRepositoryFactory", username, password, map);

        axis2Registry =new AiravataJCRRegistry(url,
                "org.apache.jackrabbit.rmi.repository.RmiRepositoryFactory", username, password, map);

         dataRegistry = new AiravataJCRRegistry(url,
                "org.apache.jackrabbit.rmi.repository.RmiRepositoryFactory", username, password, map);

        servletContext.setAttribute("airavataRegistry", airavataRegistry);
        servletContext.setAttribute("axis2Registry", axis2Registry);
        servletContext.setAttribute("dataRegistry", dataRegistry);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (RegistryException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
package org.apache.airavata.services.registry.rest.utils;

import org.apache.airavata.registry.api.AiravataRegistry;
import org.apache.airavata.registry.api.Axis2Registry;
import org.apache.airavata.registry.api.DataRegistry;
import org.apache.airavata.registry.api.impl.AiravataJCRRegistry;

import javax.jcr.RepositoryException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

public class RegistryListener implements ServletContextListener {

    private static AiravataRegistry airavataRegistry;
    private static Axis2Registry axis2Registry;
    private static DataRegistry dataRegistry;

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try{
        ServletContext servletContext = servletContextEvent.getServletContext();
        URI url = new URI("http://localhost:8081/rmi");
        String username = "admin";
        String password = "admin";
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("org.apache.jackrabbit.repository.uri", url.toString());


        airavataRegistry = new AiravataJCRRegistry(url,
                "org.apache.jackrabbit.rmi.repository.RmiRepositoryFactory", username, password, map);

        axis2Registry =new AiravataJCRRegistry(url,
                "org.apache.jackrabbit.rmi.repository.RmiRepositoryFactory", username, password, map);

         dataRegistry = new AiravataJCRRegistry(url,
                "org.apache.jackrabbit.rmi.repository.RmiRepositoryFactory", username, password, map);

        servletContext.setAttribute("airavataRegistry", airavataRegistry);
        servletContext.setAttribute("axis2Registry", axis2Registry);
        servletContext.setAttribute("dataRegistry", dataRegistry);
        }catch (RepositoryException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
