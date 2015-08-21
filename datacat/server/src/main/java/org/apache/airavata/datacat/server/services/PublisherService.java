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

//These are Jersey jars

import org.apache.airavata.datacat.models.OutputMetadataDTO;
import org.apache.airavata.datacat.server.db.IDataModel;
import org.apache.airavata.datacat.server.db.solr.SolrDataModel;
import org.apache.airavata.datacat.server.util.Constants;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


@Path("/publisher")
public class PublisherService{

    private final Logger logger = LogManager.getLogger(PublisherService.class);

    private IDataModel dataModel =  new SolrDataModel();

    ObjectMapper mapper = new ObjectMapper();


    @GET
    @Path("/getAPIVersion")
    public Response getAPIVersion(){
        return Response.status(200).entity(Constants.DATACAT_SERVER_VERSION).build();
    }

    @DELETE
    @Path("/deleteFileMetadata")
    public Response deleteFileMetadata(@QueryParam("dataArchiveNode") String dataArchiveNode,
                                       @QueryParam("fileName") String fileName,
                                       @QueryParam("filePath") String filePath
    ) {
        try {
            dataModel.deleteFileMetadata(dataArchiveNode, filePath, fileName);

            // return HTTP response 200 in case of success
            return Response.status(200).build();
        } catch (Exception e) {
            logger.error(e.toString());
            return Response.status(503).entity(e.toString()).build();
        }
    }

    @POST
    @Path("/addFileMetadata")
    public Response addFileMetadata(InputStream incomingData) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
            String line = null;
            while ((line = in.readLine()) != null) {
                stringBuilder.append(line);
            }
            OutputMetadataDTO outputMetadataDTO = mapper.readValue(stringBuilder.toString(),OutputMetadataDTO.class);
            dataModel.addMetadata(outputMetadataDTO);
            // return HTTP response 200 in case of success
            return Response.status(200).entity(stringBuilder.toString()).build();
        } catch (Exception e) {
            logger.error(e.toString());
            return Response.status(503).entity(e.toString()).build();
        }
    }

    @PUT
    @Path("/updateFileMetadata")
    public Response updateFileMetadata(InputStream incomingData) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
            String line = null;
            while ((line = in.readLine()) != null) {
                stringBuilder.append(line);
            }
            OutputMetadataDTO outputMetadataDTO = mapper.readValue(stringBuilder.toString(),OutputMetadataDTO.class);
            dataModel.updateMetadata(outputMetadataDTO);
            // return HTTP response 200 in case of success
            return Response.status(200).entity(stringBuilder.toString()).build();
        } catch (Exception e) {
            logger.error(e.toString());
            return Response.status(503).entity(e.toString()).build();
        }
    }
}
