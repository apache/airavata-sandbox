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

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.airavata.datacat.models.MetadataFields;
import org.apache.airavata.datacat.models.PrimaryQueryParameter;
import org.apache.airavata.datacat.models.PrimaryQueryType;
import org.apache.airavata.datacat.server.db.solr.SolrQuerier;
import org.apache.airavata.datacat.server.util.Constants;
import org.apache.airavata.datacat.server.util.ServerProperties;
import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class SolrQuerierTest extends TestCase {

    private SolrQuerier solrQuerier;
    private List<PrimaryQueryParameter> primaryQueryParameters;
    private PrimaryQueryParameter parameter;
    private ArrayList<LinkedHashMap<String, Object>> result;
    private final String USERNAME = "test";
    private String[] groups;

    public void setUp() throws Exception {
        super.setUp();
        groups  = new String[]{"group_1", "group_2", "sudhakar"};
        solrQuerier = new SolrQuerier();

    }

    @Test
    public void testSubstringSearch() throws IOException, SolrServerException {
        List<PrimaryQueryParameter> primaryQueryParameters;
        PrimaryQueryParameter parameter;
        ArrayList<LinkedHashMap<String, Object>> result;
        String primaryKey = ServerProperties.getInstance().getProperty(Constants.METADATA_PRIMARY_INDEX,"InChi_s");
        String username = "test";
        String[] groups  = new String[]{"group_1", "group_2", "sudhakar"};

        primaryQueryParameters = new ArrayList<PrimaryQueryParameter>();
        parameter = new PrimaryQueryParameter();
        parameter.setField(primaryKey);
        parameter.setFirstParameter("C5H9O4");
        parameter.setPrimaryQueryType(PrimaryQueryType.SUBSTRING);
        primaryQueryParameters.add(parameter);
        result = solrQuerier.getResultsFromParameters(primaryQueryParameters, username, groups, 0, 5);
        Assert.assertTrue(result.size() >= 1);
    }

    @Test
    public void testRangeSearch() throws IOException, SolrServerException {
        //range search
        primaryQueryParameters = new ArrayList<PrimaryQueryParameter>();
        parameter = new PrimaryQueryParameter();
        parameter.setField(MetadataFields.CREATED_DATE);
        parameter.setFirstParameter("*");
        parameter.setSecondParameter("2014-10-25T19:41:04Z");
        parameter.setPrimaryQueryType(PrimaryQueryType.RANGE);
        primaryQueryParameters.add(parameter);
        result = solrQuerier.getResultsFromParameters(primaryQueryParameters, USERNAME, groups, 0, 1);
        Assert.assertTrue(result.size() >= 1);

    }

    @Test
    public void testGetResults() throws Exception {
        List<PrimaryQueryParameter> primaryQueryParameters;
        PrimaryQueryParameter parameter;
        ArrayList<LinkedHashMap<String, Object>> result;

        String username = "test";
        String[] groups  = new String[]{"group_1", "group_2", "sudhakar"};

        String primaryKey = ServerProperties.getInstance().getProperty(Constants.METADATA_PRIMARY_INDEX,"InChi_s");

        //Field value search
        primaryQueryParameters = new ArrayList<PrimaryQueryParameter>();
        parameter = new PrimaryQueryParameter();
        parameter.setField(primaryKey);
        parameter.setFirstParameter("InChI=1S/C5H9O4.C2H8N3/c1-4(6)8-9-5(2,3)7;1-5-2(3)4/h1-3H3;5H,3-4H2,1H3");
        parameter.setPrimaryQueryType(PrimaryQueryType.EQUALS);
        primaryQueryParameters.add(parameter);
        result = solrQuerier.getResultsFromParameters(primaryQueryParameters, username, groups, 0, 1);
        Assert.assertTrue(result.size() >= 1);

        //Sub string search
        primaryQueryParameters = new ArrayList<PrimaryQueryParameter>();
        parameter = new PrimaryQueryParameter();
        parameter.setField(primaryKey);
        parameter.setFirstParameter("C5H9O4");
        parameter.setPrimaryQueryType(PrimaryQueryType.SUBSTRING);
        primaryQueryParameters.add(parameter);
        result = solrQuerier.getResultsFromParameters(primaryQueryParameters, username, groups, 0, 5);
        Assert.assertTrue(result.size() >= 1);

        //wildcard search
        //primaryQueryParameters = new ArrayList<PrimaryQueryParameter>();
        //parameter = new PrimaryQueryParameter();
        //parameter.setField(MetadataFields.INCHI);
        //parameter.setFirstParameter("C5H*O4");
        //parameter.setPrimaryQueryType(PrimaryQueryType.WILDCARD);
        //primaryQueryParameters.add(parameter);
        //result = solrQuerier.getResultsFromParameters(primaryQueryParameters, username, groups);
        //Assert.assertTrue(result.size() >= 1);

        //phrase search
        primaryQueryParameters = new ArrayList<PrimaryQueryParameter>();
        parameter = new PrimaryQueryParameter();
        parameter.setField(primaryKey);
        parameter.setFirstParameter("InChI=1S/C5H9O4.C2H8N3/c1-4(6)8-9-5(2,3)7;1-5-2(3)4/h1-3H3;5H,3-4H2,1H3");
        parameter.setPrimaryQueryType(PrimaryQueryType.PHRASE);
        primaryQueryParameters.add(parameter);
        result = solrQuerier.getResultsFromParameters(primaryQueryParameters, username, groups, 0, 2);
        Assert.assertTrue(result.size() >= 1);

        //range search
        //Todo The range search is not working properly
        primaryQueryParameters = new ArrayList<PrimaryQueryParameter>();
        parameter = new PrimaryQueryParameter();
        parameter.setField(MetadataFields.CREATED_DATE);
        parameter.setFirstParameter("*");
        parameter.setSecondParameter("2015-09-08T00:43:54Z");
        parameter.setPrimaryQueryType(PrimaryQueryType.RANGE);
        primaryQueryParameters.add(parameter);
        result = solrQuerier.getResultsFromParameters(primaryQueryParameters, username, groups, 0, 1);
        //Assert.assertTrue(result.size() >= 1);

        //field value AND substring AND range
        primaryQueryParameters = new ArrayList<PrimaryQueryParameter>();
        parameter = new PrimaryQueryParameter();
        parameter.setField(primaryKey);
        parameter.setFirstParameter("InChI*");
        parameter.setPrimaryQueryType(PrimaryQueryType.WILDCARD);
        primaryQueryParameters.add(parameter);
        parameter = new PrimaryQueryParameter();
        parameter.setField(primaryKey);
        parameter.setFirstParameter("C");
        parameter.setPrimaryQueryType(PrimaryQueryType.SUBSTRING);
        primaryQueryParameters.add(parameter);
        parameter = new PrimaryQueryParameter();
        parameter.setField(MetadataFields.CREATED_DATE);
        parameter.setFirstParameter("*");
        parameter.setSecondParameter("2015-09-08T00:43:54Z");
        parameter.setPrimaryQueryType(PrimaryQueryType.RANGE);
        primaryQueryParameters.add(parameter);
        //parameter = new PrimaryQueryParameter();
        //parameter.setField(MetadataFields.INCHI);
        //parameter.setFirstParameter("InChI=1S/C5H9O4.C2H8N3/c1-4(6)8-9-5(2,3)7;1-5-2(3)4/h1-3H3;5H,3-4H2,1H3");
        //parameter.setPrimaryQueryType(PrimaryQueryType.PHRASE);
        //primaryQueryParameters.add(parameter);

        result = solrQuerier.getResultsFromParameters(primaryQueryParameters, username, groups, 0, 2);
        //Assert.assertTrue(result.size() >= 1);
    }
}