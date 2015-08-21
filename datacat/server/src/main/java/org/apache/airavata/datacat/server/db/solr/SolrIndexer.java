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
package org.apache.airavata.datacat.server.db.solr;

import org.apache.airavata.datacat.models.AclDTO;
import org.apache.airavata.datacat.models.AclFields;
import org.apache.airavata.datacat.models.OutputMetadataDTO;
import org.apache.airavata.datacat.models.MetadataFields;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.UUID;

public class SolrIndexer {

    private final Logger logger = LogManager.getLogger(SolrIndexer.class);

    private SolrClientFactory solrClientFactory;

    public SolrIndexer() {
        solrClientFactory = new SolrClientFactory();
    }

    public void addMetadata(OutputMetadataDTO outputMetadataDTO) throws IOException, SolrServerException {
        addData(outputMetadataDTO);
    }

    public void updateMetadata(OutputMetadataDTO outputMetadataDTO) throws IOException, SolrServerException {
        addData(outputMetadataDTO);
    }

    public void updateACL(AclDTO aclDTO) throws IOException, SolrServerException {
        HttpSolrServer aclClient = solrClientFactory.createSolrAclClient();
        SolrInputDocument aclDoc = new SolrInputDocument();
        aclDoc.addField(AclFields.ID, aclDTO.getId());
        aclDoc.addField(AclFields.ACL, aclDTO.getAcl());

        aclClient.add(aclDoc, 1);
        logger.info("Updated ACL for metadata file. Id:"+aclDTO.getId());
    }

    private void addData(OutputMetadataDTO outputMetadataDTO) throws IOException, SolrServerException {
        HttpSolrServer metadataClient = solrClientFactory.createSolrMetadataClient();
        SolrInputDocument metadataDoc = new SolrInputDocument();

        HttpSolrServer aclClient = solrClientFactory.createSolrAclClient();
        SolrInputDocument aclDoc = new SolrInputDocument();

        if (outputMetadataDTO.getId() == "") {
            String primaryKey = UUID.randomUUID().toString();
            metadataDoc.addField(MetadataFields.ID, primaryKey);
            aclDoc.addField(AclFields.ID, primaryKey);
        } else {
            metadataDoc.addField(MetadataFields.ID, outputMetadataDTO.getId());
            aclDoc.addField(AclFields.ID, outputMetadataDTO.getId());
        }

        metadataDoc.addField(MetadataFields.EXPERIMENT_ID, outputMetadataDTO.getExperimentId());
        metadataDoc.addField(MetadataFields.EXPERIMENT_NAME, outputMetadataDTO.getExperimentName());
        metadataDoc.addField(MetadataFields.OUTPUT_PATH, outputMetadataDTO.getOutputPath());
        metadataDoc.addField(MetadataFields.OWNER_ID, outputMetadataDTO.getOwnerId());
        metadataDoc.addField(MetadataFields.GATEWAY_ID, outputMetadataDTO.getGatewayId());
        metadataDoc.addField(MetadataFields.APPLICATION_NAME, outputMetadataDTO.getApplicationName());
        metadataDoc.addField(MetadataFields.HOST, outputMetadataDTO.getHost());
        metadataDoc.addField(MetadataFields.CREATED_DATE, outputMetadataDTO.getCreatedDate());

        if(outputMetadataDTO.getCustomMetaData()!=null) {
            Object[] metadata_keys = outputMetadataDTO.getCustomMetaData().keySet().toArray();
            String key = "";
            for (int i = 0; i < metadata_keys.length; i++) {
                key = (String) metadata_keys[i];
                if (key.equals("id"))
                    continue;
                metadataDoc.addField(key,
                        outputMetadataDTO.getCustomMetaData().get(key)
                );
            }
        }
        metadataClient.add(metadataDoc,1);

        aclDoc.addField(AclFields.ACL, outputMetadataDTO.getOwnerId());
        aclClient.add(aclDoc,1);
        logger.info("Indexed Metadata file. Experiment Name:"+ outputMetadataDTO.getExperimentName());
    }
}