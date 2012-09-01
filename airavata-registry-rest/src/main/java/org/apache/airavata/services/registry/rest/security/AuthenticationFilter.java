package org.apache.airavata.services.registry.rest.security;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

import javax.ws.rs.core.MultivaluedMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Filter which intercept requests and do authentication.
 */
public class AuthenticationFilter implements ContainerRequestFilter {


    public ContainerRequest filter(ContainerRequest containerRequest) {

        System.out.println("Filter called !!");

        MultivaluedMap<String,String> mmap = containerRequest.getRequestHeaders();

        Iterator<Map.Entry<String, List<String>>> iterator = mmap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, List<String>> entry = iterator.next();
            System.out.println(entry.getKey());

            List<String> lstString = entry.getValue();

            for (String s : lstString) {
                System.out.println(s);
            }
        }

        return containerRequest;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
package org.apache.airavata.services.registry.rest.security;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

import javax.ws.rs.core.MultivaluedMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Filter which intercept requests and do authentication.
 */
public class AuthenticationFilter implements ContainerRequestFilter {


    public ContainerRequest filter(ContainerRequest containerRequest) {

        System.out.println("Filter called !!");

        MultivaluedMap<String,String> mmap = containerRequest.getRequestHeaders();

        Iterator<Map.Entry<String, List<String>>> iterator = mmap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, List<String>> entry = iterator.next();
            System.out.println(entry.getKey());

            List<String> lstString = entry.getValue();

            for (String s : lstString) {
                System.out.println(s);
            }
        }

        return containerRequest;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
