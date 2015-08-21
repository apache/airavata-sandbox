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

import org.apache.airavata.datacat.models.PrimaryQueryParameter;
import org.apache.solr.client.solrj.SolrQuery;

import java.util.List;

public class SolrQueryBuilder {

    public SolrQuery generateQueryFromParameters(List<PrimaryQueryParameter> primaryQueryParameters,
                                                 String username, String[] userGroups, int startRow, int numberOfRows
    ) {
        SolrQuery solrQuery = new SolrQuery();
        String aclSubQuery = getAclSubQuery(username, userGroups);
        solrQuery.setQuery(aclSubQuery);

        PrimaryQueryParameter qp;
        for (int i = 0; i < primaryQueryParameters.size(); i++) {
            qp = primaryQueryParameters.get(i);
            switch (qp.getPrimaryQueryType()) {
                case EQUALS:
                    solrQuery = solrQuery.addFilterQuery(qp.getField() + ":\"" + qp.getFirstParameter() + "\"");
                    break;
                case PHRASE:
                    solrQuery = solrQuery.addFilterQuery(qp.getField() + ":\"" + qp.getFirstParameter() + "\"");
                    break;
                case SUBSTRING:
                    solrQuery = solrQuery.addFilterQuery(qp.getField() + ":*" + qp.getFirstParameter() + "*");
                    break;
                case WILDCARD:
                    solrQuery = solrQuery.addFilterQuery(qp.getField() + ":" + qp.getFirstParameter());
                    break;
                case RANGE:
                    solrQuery = solrQuery.addFilterQuery(qp.getField() + ":["
                            + qp.getFirstParameter() + " TO " + qp.getSecondParameter() + "]");
                    break;
            }
        }

        solrQuery.setStart(startRow-1);
        solrQuery.setRows(numberOfRows);

        return solrQuery;
    }

    public SolrQuery generateQueryFromQueryString(String userQuery, String username, String[] userGroups,
                                                  int startRow, int numberOfRows
    ) {
        String aclSubQuery = getAclSubQuery(username, userGroups);

        SolrQuery solrQuery = new SolrQuery(aclSubQuery);
        solrQuery = solrQuery.addFilterQuery(userQuery);
        solrQuery = solrQuery.setStart(startRow-1);
        solrQuery = solrQuery.setRows(numberOfRows);

        return solrQuery;
    }

    private String getAclSubQuery(String username, String[] userGroups){
        //for generating access control filter query
        String query = "";
        if (username != null && username != "") {
            if (userGroups != null && userGroups.length > 0) {
                String temp = "(" + username + " OR ";
                for (int i = 0; i < userGroups.length - 1; i++) {
                    temp = temp + userGroups[i] + " OR ";
                }
                temp = temp + userGroups[userGroups.length - 1] + ")";

                query = "{!join from=id to=id fromIndex=acl}acl:" + temp;
            } else {
                query = "{!join from=id to=id fromIndex=acl}acl:" + username;
            }
        } else {
            query = "{!join from=id to=id fromIndex=acl}acl:public";
        }

        return query;
    }
}

