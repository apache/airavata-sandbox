/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
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
