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
package org.apache.airavata.datacat.server.db;

import junit.framework.TestCase;
import org.apache.airavata.datacat.models.OutputMetadataDTO;
import org.apache.airavata.datacat.server.db.solr.SolrIndexer;
import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SolrIndexerTest extends TestCase {

    private SolrIndexer indexer;

    private OutputMetadataDTO outputMetadataDTO;

    public void setUp() throws Exception {
        super.setUp();
        indexer = new SolrIndexer();

        //populating a dummy fileMetaDataDTO
        outputMetadataDTO = new OutputMetadataDTO();
        Map<String, String> customMetaData = new HashMap<String, String>();
        customMetaData.put("id", "InChI=1S/H2O.HO/h1H2;1LMLKF4HXB");
        customMetaData.put("InChi_s", "InChI=1S/H2O.HO/h1H2;LMLKF4HXB");

        outputMetadataDTO.setOutputPath("/Users/swithana/data_root/gaussian_sample_23");
        outputMetadataDTO.setApplicationName("gaussian 9");
        outputMetadataDTO.setHost("localhost");
        outputMetadataDTO.setCreatedDate("2014-10-25T19:41:04Z");
        outputMetadataDTO.setOwnerId("sudhakar");
        outputMetadataDTO.setCustomMetaData(customMetaData);

    }

    @Test
    public void testAddData() throws IOException, SolrServerException {
        indexer.addMetadata(outputMetadataDTO);
    }
}
