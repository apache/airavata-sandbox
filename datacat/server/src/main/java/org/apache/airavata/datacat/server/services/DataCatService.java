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
package org.apache.airavata.datacat.server.services;

import org.apache.airavata.datacat.models.AclDTO;
import org.apache.airavata.datacat.models.QueryObject;
import org.apache.airavata.datacat.server.db.IDataModel;
import org.apache.airavata.datacat.server.db.solr.SolrDataModel;
import org.apache.airavata.datacat.server.util.Constants;
import org.apache.airavata.datacat.server.util.ServerProperties;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;

@Path("/datacat")
public class DataCatService{

    private final Logger logger = LogManager.getLogger(DataCatService.class);

    private IDataModel dataModel;

    private ObjectMapper objectMapper;

    public DataCatService(){
        this.dataModel = new SolrDataModel();
        this.objectMapper = new ObjectMapper();
    }

    private String serialize (Object object) throws IOException {                                       // serialize given objects
        return objectMapper.writeValueAsString(object);
    }


    @GET                                                                                                // http request type
    @Path("/getAPIVersion")                                                                             // relative path annotation
    @Consumes("application/json")                                                                       // accepting data type
    public Response getAPIVersion(){
        return Response.status(200).entity(Constants.DATACAT_SERVER_VERSION).build();                   // response with corresponding header
    }

    @GET
    @Path("/getMetadataDocHTMLById")
    @Consumes("application/json")                                                                       // get full metadata list of a data product
    public Response getMetadataDocHTMLById(@QueryParam("id") String id){
        try {
            String jsonDoc = dataModel.getMetadataDocHTMLById(id);
            return Response.status(200).entity(jsonDoc).build();
        } catch (Exception e) {
            logger.error(e.toString());
            return Response.status(503).entity(e.toString()).build();
        }
    }

    @POST
    @Path("/getResults")
    @Consumes("application/json")
    public Response getResults(InputStream incomingData){                                               // search metadata
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
            String line = null;
            while ((line = in.readLine()) != null) {
                stringBuilder.append(line);
            }
            QueryObject queryObject = objectMapper.readValue(stringBuilder.toString(), QueryObject.class);
            String username = queryObject.getUsername();
            String[] userRoles = queryObject.getUserGroups();
            ArrayList<LinkedHashMap<String, Object>> fileMetadataDTO;
            if(!queryObject.isQueryStringSet()){
                fileMetadataDTO =  dataModel.getResultsFromParameters(                                  // do full text search
                        queryObject.getPrimaryQueryParameterList(), username, userRoles, queryObject.getStartRow(),
                        queryObject.getNumberOfRows()
                );
            }else {
                fileMetadataDTO =  dataModel.getResultsFromQueryString(                                 // do fielded search
                        queryObject.getQueryString(), username, userRoles,queryObject.getStartRow(),
                        queryObject.getNumberOfRows()
                );
            }
            String result = serialize(fileMetadataDTO);                                                 // serialize response object
            return Response.status(200).entity(result).build();
        } catch (Exception e) {
            logger.error(e.toString());

            return Response.status(503).entity(e.toString()).build();
        }
    }

    @GET
    @Path("/getAclList")
    @Produces("application/json")
    public Response getAclList(@QueryParam("id") String id){                                            // retrieve ACL list of a data product given the id
        try {
            ArrayList<String> result = dataModel.getAclList(id);
            return Response.status(200).entity(serialize(result)).build();
        } catch (Exception e) {
            logger.error(e.toString());
            return Response.status(503).entity(e.toString()).build();
        }
    }

    @POST
    @Path("/updateAclList")
    public Response updateAclList(InputStream incomingData){                                            // update ACL list of a data product
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
            String line = null;
            while ((line = in.readLine()) != null) {
                stringBuilder.append(line);
            }
            AclDTO aclDTO = objectMapper.readValue(stringBuilder.toString(), AclDTO.class);
            dataModel.updateAclList(aclDTO);
            return Response.status(200).build();
        } catch (Exception e) {
            logger.error(e.toString());
            return Response.status(503).entity(e.toString()).build();
        }
    }

    @GET
    @Path("/getMetadataFieldList")
    @Produces("application/json")
    public Response getMetadataFieldList(){                                                             // get the list of searchable/indexed fields in database
        try {
            String[] fields = ServerProperties.getInstance().getProperty(Constants.FILE_METADATA_FIELDS, "").split(",");
            String json = "";
            if(fields.length>=1){
                json = "[";
                for(int i=0;i<fields.length-1;i++){
                    json = json + "\"" + fields[i] + "\",";
                }
                json = json + "\"" + fields[fields.length-1] + "\"]";
            }
            return Response.status(200).entity(json).build();
        } catch (Exception e) {
            logger.error(e.toString());
            return Response.status(503).entity(e.toString()).build();
        }
    }
}
