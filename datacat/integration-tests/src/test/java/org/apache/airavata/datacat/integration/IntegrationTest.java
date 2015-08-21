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
package org.apache.airavata.datacat.integration;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.airavata.datacat.agent.dispatcher.DispatcherService;
import org.apache.airavata.datacat.agent.monitor.IMonitor;
import org.apache.airavata.datacat.agent.monitor.impl.LocalFileSystemMonitor;
import org.apache.airavata.datacat.agent.util.AgentProperties;
import org.apache.airavata.datacat.models.PrimaryQueryParameter;
import org.apache.airavata.datacat.models.PrimaryQueryType;
import org.apache.airavata.datacat.server.db.solr.SolrClientFactory;
import org.apache.airavata.datacat.server.db.solr.SolrQuerier;
import org.apache.airavata.datacat.server.services.DataCatService;
import org.apache.airavata.datacat.server.services.PublisherService;
import org.apache.airavata.datacat.server.util.Constants;
import org.apache.airavata.datacat.server.util.ServerProperties;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class IntegrationTest extends TestCase {

    public void testSimpleIntegration() throws IOException, ClassNotFoundException, InterruptedException, SolrServerException {
        //deleting file snapshot file
        File file = new File("snapshot.ser");
        file.delete();
        System.out.println("snapshot.ser file deleted.");

        //Deleting Solr index files
        SolrServer metadataClient = new SolrClientFactory().createSolrMetadataClient();
        metadataClient.deleteByQuery("*:*");
        metadataClient.commit();
        SolrServer aclClient = new SolrClientFactory().createSolrAclClient();
        aclClient.deleteByQuery("*:*");
        aclClient.commit();
        System.out.println("Deleted existing data in Solr");

        //starting DataCat server
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URI datacatUri = UriBuilder.fromUri(
                            ServerProperties.getInstance().getProperty(Constants.DATACAT_URI, "")).build();
                    ResourceConfig datacatConfig = new ResourceConfig(DataCatService.class);
                    HttpServer datacatServer = GrizzlyHttpServerFactory.createHttpServer(datacatUri, datacatConfig);

                    datacatServer.start();

                    URI publisherUri = UriBuilder.fromUri(
                            ServerProperties.getInstance().getProperty(Constants.PUBLISHER_URI, "")).build();
                    ResourceConfig publisherConfig = new ResourceConfig(PublisherService.class);
                    HttpServer publisherServer = GrizzlyHttpServerFactory.createHttpServer(publisherUri, publisherConfig);

                    publisherServer.start();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Assert.assertTrue(false);
                }
            }
        }).start();


        //starting DataCat agent
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //waiting for some time to get the data indexed
                    Thread.sleep(1000 * 15); // waits for 15 seconds
                    AgentProperties properties = AgentProperties.getInstance();
                    IMonitor iMonitor = new LocalFileSystemMonitor();
                    DispatcherService dispatcherService = DispatcherService.getInstance();
                    dispatcherService.startDispatcher();
                    iMonitor.startMonitor(Paths.get(properties.getProperty(
                                    org.apache.airavata.datacat.agent.util.Constants.DATA_ROOT, ""))
                    );

                } catch (Exception ex) {
                    ex.printStackTrace();
                    Assert.assertTrue(false);
                }
            }
        }).start();


        //Testing the Query API
        //waiting for some time to get the data indexed
        Thread.sleep(1000 * 300); // waits for 30 seconds

        //FIXME Ideally this querying should be done via the REST API
        //Querying using the DataCat API
        List<PrimaryQueryParameter> primaryQueryParameters;
        PrimaryQueryParameter parameter;
        ArrayList<LinkedHashMap<String, Object>> result;
        SolrQuerier solrQuerier = new SolrQuerier();

        String username = "test";
        String[] groups = new String[]{"group_1", "group_2", "sudhakar"};

        String primaryKey = ServerProperties.getInstance().getProperty(Constants.METADATA_PRIMARY_INDEX, "InChi");

        //Field value search
        primaryQueryParameters = new ArrayList<PrimaryQueryParameter>();
        parameter = new PrimaryQueryParameter();
        parameter.setField(primaryKey);
        parameter.setFirstParameter("InChI=1S/C14H14N4O3/c1-7-5-9-10(6-8(7)2)18(3-4-19)12-11(15-9)13(20)17-14(21)16-12/h5-6,19H,3-4H2,1-2H3,(H,17,20,21)");
        parameter.setPrimaryQueryType(PrimaryQueryType.EQUALS);
        primaryQueryParameters.add(parameter);
        result = solrQuerier.getResultsFromParameters(primaryQueryParameters, username, groups, 1, 2);
        Assert.assertTrue(result.size() >= 1);

        //Sub string search
        primaryQueryParameters = new ArrayList<PrimaryQueryParameter>();
        parameter = new PrimaryQueryParameter();
        parameter.setField(primaryKey);
        parameter.setFirstParameter("C14H14N4");
        parameter.setPrimaryQueryType(PrimaryQueryType.SUBSTRING);
        primaryQueryParameters.add(parameter);
        result = solrQuerier.getResultsFromParameters(primaryQueryParameters, username, groups, 1, 5);
        Assert.assertTrue(result.size() >= 1);
    }
}
