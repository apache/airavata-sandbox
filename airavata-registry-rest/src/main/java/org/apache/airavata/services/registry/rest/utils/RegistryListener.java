package org.apache.airavata.services.registry.rest.utils;

import org.apache.airavata.registry.api.AiravataRegistry2;
import org.apache.airavata.registry.api.AiravataRegistryFactory;
import org.apache.airavata.registry.api.AiravataUser;
import org.apache.airavata.registry.api.Gateway;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class RegistryListener implements ServletContextListener {

    private static AiravataRegistry2 airavataRegistry;
//    private static Axis2Registry axis2Registry;
//    private static DataRegistry dataRegistry;

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try{
        ServletContext servletContext = servletContextEvent.getServletContext();
//        URI url = new URI("http://localhost:8081/rmi");
//        String username = "admin";
//        String password = "admin";
//        HashMap<String, String> map = new HashMap<String, String>();
//        map.put("org.apache.jackrabbit.repository.uri", url.toString());


        airavataRegistry = AiravataRegistryFactory.getRegistry(new Gateway("gateway1"), new AiravataUser("admin"));
        servletContext.setAttribute("airavataRegistry", airavataRegistry);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
