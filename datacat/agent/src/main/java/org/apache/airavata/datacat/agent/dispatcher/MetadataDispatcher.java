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
package org.apache.airavata.datacat.agent.dispatcher;

import com.google.gson.Gson;
import org.apache.airavata.datacat.agent.messageBroker.RabbitMQPublisher;
import org.apache.airavata.datacat.agent.util.AgentProperties;
import org.apache.airavata.datacat.agent.util.Constants;
import org.apache.airavata.datacat.models.OutputMetadataDTO;
import org.apache.airavata.datacat.models.Messaging.ExperimentOutputParsedEvent;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;

public class MetadataDispatcher {

    private final Logger logger = LogManager.getLogger(MetadataDispatcher.class);

    private MetadataDispatcherQueue metadataDispatcherQueue = MetadataDispatcherQueue.getInstance();

    private static MetadataDispatcher instance;

    private SSLConnectionSocketFactory sslsf;

    private RabbitMQPublisher rabbitMQPublisher;

    private MetadataDispatcher() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        KeyStore trustStore = KeyStore.getInstance("JKS");
        // set up security context
        if (new File("../security/" + AgentProperties.getInstance()
                .getProperty(Constants.KEYSTORE_FILE, "")).exists()) {
            keyStore.load(new FileInputStream(new File("../security/" + AgentProperties.getInstance()
                    .getProperty(Constants.KEYSTORE_FILE, ""))),AgentProperties.getInstance()
                    .getProperty(Constants.KEYSTORE_PWD, "").toCharArray());
        } else {;
            keyStore.load(ClassLoader.getSystemResourceAsStream("security/" +
                    AgentProperties.getInstance().getProperty(Constants.KEYSTORE_FILE, "")),
                    AgentProperties.getInstance()
                    .getProperty(Constants.KEYSTORE_PWD, "").toCharArray());
        }
        if (new File("../security/" + AgentProperties.getInstance()
                .getProperty(Constants.TRUSTSTORE_FILE, "")).exists()) {
            keyStore.load(new FileInputStream(new File("../security/" + AgentProperties.getInstance()
                    .getProperty(Constants.TRUSTSTORE_FILE, ""))),AgentProperties.getInstance()
                    .getProperty(Constants.TRUSTSTORE_PWD, "").toCharArray());
        } else {
            keyStore.load(ClassLoader.getSystemResourceAsStream("security/" +
                    AgentProperties.getInstance().getProperty(Constants.TRUSTSTORE_FILE, "")),
                    AgentProperties.getInstance()
                    .getProperty(Constants.TRUSTSTORE_PWD, "").toCharArray());
        }
        SSLContext sslContext = SSLContexts.custom()
            .loadKeyMaterial(keyStore,AgentProperties.getInstance()
                    .getProperty(Constants.KEYSTORE_PWD, "").toCharArray())
            .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
            .build();

        sslsf = new SSLConnectionSocketFactory(
                sslContext, new String[] { "TLSv1" }, null,
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        rabbitMQPublisher = new RabbitMQPublisher();
    }


    public static MetadataDispatcher getInstance() throws Exception {
        if(MetadataDispatcher.instance==null){
            MetadataDispatcher.instance = new MetadataDispatcher();
        }        
        return MetadataDispatcher.instance;
    }

    /**
     * Starts the message dispatcher
     */
    public void startDispatcher() {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                OutputMetadataDTO outputMetadataDTO = null;
                while (true) {
                    outputMetadataDTO = metadataDispatcherQueue.getMetadataFromQueue();
                    if (outputMetadataDTO != null) {
                        try {
                            dispatch(outputMetadataDTO);
                        } catch (Exception e) {
                            logger.error(e.toString());
                        }
                    }
                }
            }
        })).start();
    }

    /**
     * Dispatch single file metadata
     *
     * @param outputMetadataDTO
     * @throws Exception
     */
    private void dispatch(OutputMetadataDTO outputMetadataDTO) throws IOException {
        if (outputMetadataDTO != null) {
            logger.info("Dispatching newly generated file metadata");
            publishDataToServer(outputMetadataDTO);

            logger.info("Publishing the message to Airavata RabbitMQ Message Broker");
            ExperimentOutputParsedEvent experimentOutputParsedEvent = new ExperimentOutputParsedEvent();
            experimentOutputParsedEvent.setExperimentId(outputMetadataDTO.getExperimentId());
            experimentOutputParsedEvent.setDocumentID(outputMetadataDTO.getId());
            experimentOutputParsedEvent.setStatus("success");
            rabbitMQPublisher.publish(experimentOutputParsedEvent);
        } else {
            logger.info("Dispatching empty file metadata (doing nothing)");
        }
    }

    /**
     * Method to publish data to the Publisher REST Service
     * @param outputMetadataDTO
     */
    private void publishDataToServer(OutputMetadataDTO outputMetadataDTO){
        int count = 0;
        while(count<10){
            count++;
            String stringUri = "";
            try{
                stringUri = AgentProperties.getInstance().getProperty(Constants.PUBLISHER_ADD_ENDPOINT,"");
                HttpClient client = HttpClients.custom().setSSLSocketFactory(sslsf).build();
                HttpPost post = new HttpPost(stringUri);
                Gson gson = new Gson();
                StringEntity input = new StringEntity(gson.toJson(outputMetadataDTO));;
                post.setEntity(input);
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200) {
                    break;
                }
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }catch (Exception ex){
                logger.error("Trying to publish (URI:"+stringUri+") for the "+count+" time and failed", ex);
                ex.printStackTrace();
            }
        }
    }

}
