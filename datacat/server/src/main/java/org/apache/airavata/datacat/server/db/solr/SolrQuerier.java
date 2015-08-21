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

import com.google.gson.Gson;
import org.apache.airavata.datacat.models.AclFields;
import org.apache.airavata.datacat.models.MetadataFields;
import org.apache.airavata.datacat.models.PrimaryQueryParameter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.*;

public class SolrQuerier {
    private final Logger logger = LogManager.getLogger(SolrQuerier.class);

    private SolrQueryBuilder solrQueryBuilder;

    private SolrClientFactory solrClientFactory;

    private ObjectMapper objectMapper;

    public SolrQuerier(){
        this.solrClientFactory = new SolrClientFactory();
        this.solrQueryBuilder = new SolrQueryBuilder();
        this.objectMapper = new ObjectMapper();
    }

    public String getMetadataDocHTMLById(String id) throws SolrServerException {
        SolrServer server = solrClientFactory.createSolrMetadataClient();
        SolrQuery solrQuery = new SolrQuery(MetadataFields.ID+":"+id);
        QueryResponse solrResponse = server.query(solrQuery);
        SolrDocumentList docs = solrResponse.getResults();
        String html = "<html>\n" +
                "\n" +
                "<head>\n" +
                "<style>\n" +
                "table, th, td {\n" +
                "    border: 1px solid black;\n" +
                "    border-collapse: collapse;\n" +
                "}\n" +
                "th, td {\n" +
                "    padding: 5px;\n" +
                "    text-align: left;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "\n" +
                "<body>"+
                "<table>";
        html += "<tr><th>Property</th><th>Value</th></tr>";
        if(docs.size()==1){
            SolrDocument doc = docs.get(0);
            Collection<String> fieldNames = doc.getFieldNames();
            Iterator<String> fieldNamesIterator = fieldNames.iterator();
            for(int i=0;i<fieldNames.size();i++){
                html += "<tr>";
                String fieldName = fieldNamesIterator.next();
                html += "<td>" + fieldName + "</td>" + "<td>" + doc.getFieldValue(fieldName) + "</td>";
                html += "</tr>";
            }
        }
        html += "</table>" +
                 "</html>";
        return  html;
    }

    public ArrayList<LinkedHashMap<String, Object>> getResultsFromParameters(List<PrimaryQueryParameter> primaryQueryParameters,
                                                          String username, String[] userRoles, int startRow, int numberOfRows
    )throws SolrServerException, IOException {
        SolrQuery solrQuery;
        logger.info("Retrieving results for the query parameters");
        solrQuery = solrQueryBuilder.generateQueryFromParameters(primaryQueryParameters, username, userRoles, startRow,
                numberOfRows
        );
        return getResults(solrQuery);
    }

    public ArrayList<LinkedHashMap<String, Object>> getResultsFromQueryString(String queryString, String username, String[] userRoles,
                                                           int startRow, int numberOfRows
    )throws SolrServerException, IOException {
        SolrQuery solrQuery;
        logger.info("Retrieving results for the query string:"+queryString);
        solrQuery = solrQueryBuilder.generateQueryFromQueryString(queryString, username, userRoles, startRow, numberOfRows);
        return getResults(solrQuery);
    }

    private ArrayList<LinkedHashMap<String, Object>> getResults(SolrQuery solrQuery) throws IOException, SolrServerException {
        SolrServer server = solrClientFactory.createSolrMetadataClient();
        QueryResponse solrResponse = server.query(solrQuery);
        SolrDocumentList docs = solrResponse.getResults();
        String json= (new Gson()).toJson(docs);
        ArrayList<LinkedHashMap<String, Object>> result =
                new ObjectMapper().readValue(json, ArrayList.class);
         return result;
    }

    public ArrayList<String> getAclList(String id) throws SolrServerException, IOException {
        SolrServer server = solrClientFactory.createSolrAclClient();
        SolrQuery solrQuery = new SolrQuery(AclFields.ID+":"+id);
        QueryResponse solrResponse = server.query(solrQuery);
        SolrDocumentList docs = solrResponse.getResults();
        String json= (new Gson()).toJson(docs);
        ArrayList<LinkedHashMap<String, Object>> result = objectMapper.readValue(json, ArrayList.class);
        return (ArrayList<String>)result.get(0).get(AclFields.ACL);
    }
}
