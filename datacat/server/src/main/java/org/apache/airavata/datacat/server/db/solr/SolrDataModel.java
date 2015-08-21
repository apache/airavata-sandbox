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
import org.apache.airavata.datacat.models.OutputMetadataDTO;
import org.apache.airavata.datacat.models.PrimaryQueryParameter;
import org.apache.airavata.datacat.server.db.IDataModel;
import org.apache.http.MethodNotSupportedException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SolrDataModel implements IDataModel {

    private SolrIndexer solrIndexer;

    private SolrQuerier solrQuerier;

    public SolrDataModel(){
        this.solrIndexer = new SolrIndexer();
        solrQuerier = new SolrQuerier();
    }

    @Override
    public void addMetadata(OutputMetadataDTO outputMetadataDTO) throws Exception{
        solrIndexer.addMetadata(outputMetadataDTO);
    }

    @Override
    public void updateMetadata(OutputMetadataDTO outputMetadataDTO)throws Exception {
        solrIndexer.updateMetadata(outputMetadataDTO);
    }

    @Override
    public void deleteFileMetadata(String dataArchiveNode, String filePath, String fileName) throws Exception {
        throw new MethodNotSupportedException("Method still not implemented!!");
    }

    @Override
    public ArrayList<String> getAclList(String id) throws Exception {
        return solrQuerier.getAclList(id);
    }

    @Override
    public void updateAclList(AclDTO aclDTO) throws Exception {
        solrIndexer.updateACL(aclDTO);
    }

    @Override
    public String getMetadataDocHTMLById(String id) throws Exception {
       return solrQuerier.getMetadataDocHTMLById(id);
    }

    @Override
    public ArrayList<LinkedHashMap<String, Object>> getResultsFromParameters(List<PrimaryQueryParameter> primaryQueryParameters, String username, String[] userRoles, int startRow, int numberOfRows) throws Exception {
        return solrQuerier.getResultsFromParameters(primaryQueryParameters, username, userRoles, startRow, numberOfRows);
    }

    @Override
    public ArrayList<LinkedHashMap<String, Object>> getResultsFromQueryString(String query, String username, String[] userRoles, int startRow, int numberOfRows) throws Exception {
        return solrQuerier.getResultsFromQueryString(query, username, userRoles, startRow, numberOfRows);
    }
}
