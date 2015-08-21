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

import org.apache.airavata.datacat.server.userstore.IUserStoreClient;
import org.apache.airavata.datacat.server.userstore.WSO2IUserStoreClient;
import org.apache.airavata.datacat.server.util.Constants;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/userstore")
public class UserStoreService {

    private final Logger logger = LogManager.getLogger(UserStoreService.class);

    private IUserStoreClient iUserStoreClient;

    private ObjectMapper objectMapper;

    public UserStoreService(){
        this.iUserStoreClient = new WSO2IUserStoreClient();
    }

    @GET
    @Path("/getAPIVersion")
    @Consumes("application/json")
    public Response getAPIVersion(){
        return Response.status(200).entity(Constants.DATACAT_SERVER_VERSION).build();
    }

    @GET
    @Path("/authenticate")
    @Produces("application/json")
    public Response authenticate(@QueryParam("username") String username, @QueryParam("password") String password){
        try {
            boolean status = iUserStoreClient.authenticateUser(username, password);
            return Response.status(200).entity(status+"").build();
        } catch (Exception e) {
            logger.error(e.toString());
            return Response.status(503).entity(e.toString()).build();
        }
    }


    @GET
    @Path("/getGroupsOfUser")
    @Produces("application/json")
    public Response getGroupsOfUser(@QueryParam("username") String username){
        try {
            List<String> groups = iUserStoreClient.getUserGroups(username);
            String json = "";
            if(groups.size()>=1){
                json = "[";
                for(int i=0;i<groups.size()-1;i++){
                    json = json + "\"" + groups.get(i) + "\",";
                }
                json = json + "\"" + groups.get(groups.size()-1) + "\"]";
            }
            return Response.status(200).entity(json).build();
        } catch (Exception e) {
            logger.error(e.toString());
            return Response.status(503).entity(e.toString()).build();
        }
    }

    @GET
    @Path("/getGroupsList")
    @Produces("application/json")
    public Response getGroupsList(){
        try {
            List<String> groups = iUserStoreClient.getAllGroups();
            String json = "";
            if(groups.size()>=1){
                json = "[";
                for(int i=0;i<groups.size()-1;i++){
                    json = json + "\"" + groups.get(i) + "\",";
                }
                json = json + "\"" + groups.get(groups.size()-1) + "\"]";
            }
            return Response.status(200).entity(json).build();
        } catch (Exception e) {
            logger.error(e.toString());
            return Response.status(503).entity(e.toString()).build();
        }
    }
}
