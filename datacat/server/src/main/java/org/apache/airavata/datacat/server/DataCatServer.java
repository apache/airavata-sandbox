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
package org.apache.airavata.datacat.server;

import org.apache.airavata.datacat.server.services.DataCatService;
import org.apache.airavata.datacat.server.services.PublisherService;
import org.apache.airavata.datacat.server.util.Constants;
import org.apache.airavata.datacat.server.util.ServerProperties;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public class DataCatServer {
    private final static Logger logger = LoggerFactory.getLogger(DataCatService.class);

    public static void main(String[] args) throws IOException {
        try {
            // Grizzly ssl configuration
            SSLContextConfigurator sslContext = new SSLContextConfigurator();

            // set up security context
            if (new File("../security/" + ServerProperties.getInstance()
                    .getProperty(Constants.KEYSTORE_FILE, "")).exists()) {
                sslContext.setKeyStoreFile("../security/" + ServerProperties.getInstance()
                        .getProperty(Constants.KEYSTORE_FILE, ""));
                logger.info("Using the configured key store");
            } else {
                sslContext.setKeyStoreFile(ClassLoader.getSystemResource("security/" +
                        ServerProperties.getInstance()
                                .getProperty(Constants.KEYSTORE_FILE, "")).getPath());
                logger.info("Using the default key store");
            }
            sslContext.setKeyStorePass(ServerProperties.getInstance()
                    .getProperty(Constants.KEYSTORE_PWD,""));

            if (new File("../security/" + ServerProperties.getInstance()
                    .getProperty(Constants.TRUSTSTORE_FILE, "")).exists()) {
                sslContext.setTrustStoreFile("../security/" + ServerProperties.getInstance()
                        .getProperty(Constants.TRUSTSTORE_FILE, ""));
                logger.info("Using the configured trust store");
            } else {
                sslContext.setTrustStoreFile(ClassLoader.getSystemResource("security/" +
                        ServerProperties.getInstance()
                                .getProperty(Constants.TRUSTSTORE_FILE, "")).getPath());
                logger.info("Using the default trust store");
            }
            sslContext.setTrustStoreFile(ServerProperties.getInstance()
                    .getProperty(Constants.TRUSTSTORE_PWD, ""));

            URI datacatUri = UriBuilder.fromUri(
                    ServerProperties.getInstance().getProperty(Constants.DATACAT_URI, "")).build();

            ResourceConfig dataCatConfig = new ResourceConfig(DataCatService.class);
            final HttpServer dataCatServer = GrizzlyHttpServerFactory.createHttpServer(
                    datacatUri, dataCatConfig, true,
                    new SSLEngineConfigurator(sslContext).setClientMode(false).setNeedClientAuth(false));

            URI publisherUri = UriBuilder.fromUri(
                    ServerProperties.getInstance().getProperty(Constants.PUBLISHER_URI, "")).build();

            ResourceConfig publisherConfig = new ResourceConfig(PublisherService.class);
            final HttpServer publisherServer = GrizzlyHttpServerFactory.createHttpServer(
                    publisherUri, publisherConfig,
                true,new SSLEngineConfigurator(sslContext).setClientMode(false).setNeedClientAuth(false));

            //Running a proxy api for the user api is not essential
            //URI userstoreUri = UriBuilder.fromUri(
            //        ServerProperties.getInstance().getProperty(Constants.USERSTORE_URI, "")).build();

            //ResourceConfig userstoreConfig = new ResourceConfig(UserStoreService.class);
            //final HttpServer userstoreServer = GrizzlyHttpServerFactory.createHttpServer(userstoreUri, userstoreConfig);
            //userstoreServer.start();

            try {
                new Thread() {
                    public void run() {
                        try {
                            logger.info("Starting DataCat Service. URI:"
                                    + ServerProperties.getInstance().getProperty(Constants.DATACAT_URI,"")
                                    ,"datacat-service");
                            dataCatServer.start();
                            //registerWithZk(ServerProperties.getInstance().getProperty(Constants.DATACAT_URI,"")
                            //        ,"datacat-service");
                            Thread.currentThread().join();
                        } catch (Exception e) {
                            e.printStackTrace();
                            logger.error("There was an error when starting DataCat Service." +
                                    "Unable to start DataCat Service", e);
                            System.exit(-1);
                        }
                    }
                }.start();
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    public void run() {
                        dataCatServer.stop();
                        logger.info("DataCat Service Stopped!");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Unable to start DataCat Service", e);
                throw e;
            }
            try {
                new Thread() {
                    public void run() {
                        try {
                            logger.info("Starting Publisher Service. URI:"
                                    +ServerProperties.getInstance().getProperty(Constants.PUBLISHER_URI,"")
                                    ,"publisher-service");
                            publisherServer.start();
                            //registerWithZk(ServerProperties.getInstance().getProperty(Constants.PUBLISHER_URI,"")
                            //       ,"publisher-service");
                            Thread.currentThread().join();
                        } catch (Exception e) {
                            e.printStackTrace();
                            logger.error("There was an error when starting Publisher Service." +
                                    "Unable to start Publisher Service", e);
                            System.exit(-1);
                        }
                    }
                }.start();
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    public void run() {
                        publisherServer.stop();
                        logger.info("Publisher Service Stopped!");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Unable to start Publisher Service", e);
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString(), e);
        }
    }

    /**
     * Method to register the service with Zookeeper
     * @throws Exception
     */
    private static void registerWithZk(String uri, String name) throws Exception {
        CuratorFramework curator = CuratorFrameworkFactory.newClient(
                ServerProperties.getInstance().getProperty(
                        Constants.ZK_HOST, ""),
                new ExponentialBackoffRetry(1000, 3));
        curator.start();
        JsonInstanceSerializer<String> serializer =
                new JsonInstanceSerializer<String>(String.class );
        ServiceInstance<String> instance =
                ServiceInstance.<String>builder()
                        .name(name)
                        .payload(new String(Constants.DATACAT_SERVER_VERSION))
                        .sslPort(Integer.parseInt(uri.split(":")[2].replaceAll("/","")))
                        .uriSpec(new UriSpec(uri))
                        .build();
        ServiceDiscovery<String> discovery =
                ServiceDiscoveryBuilder.builder(String.class)
                        .client(curator)
                        .basePath("services")
                        .serializer(serializer)
                        .thisInstance(instance)
                        .build();
        discovery.start();
    }
}
