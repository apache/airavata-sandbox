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
package org.apache.airavata.datacat.server.userstore;

import org.apache.airavata.datacat.server.util.Constants;
import org.apache.airavata.datacat.server.util.ServerProperties;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Stub;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.wso2.carbon.um.ws.api.stub.RemoteUserStoreManagerServiceStub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class contains the basic functionality based on a backend wso2 IS
 */

public class WSO2IUserStoreClient implements IUserStoreClient {

    private static final String ADMIN_ROLE = "admin";
    private static final String INTERNAL_ROLE_PREFIX = "Internal";

    @Override
    public boolean authenticateUser(String username, String password) throws Exception{
        Stub serviceStub = getServiceStub();
        boolean isAuthentic = ((RemoteUserStoreManagerServiceStub) serviceStub).authenticate(username, password);
        return isAuthentic;
    }

    @Override
    public List<String> getUserGroups(String username) throws Exception {
        Stub serviceStub = getServiceStub();
        List<String> temp = Arrays.asList(((RemoteUserStoreManagerServiceStub) serviceStub).getRoleListOfUser(username));
        List<String> result = new ArrayList<String>();
        for(int i=0;i<temp.size();i++){
            if(temp.get(i).equals(ADMIN_ROLE) || temp.get(i).startsWith(INTERNAL_ROLE_PREFIX)){
                continue;
            }
            result.add(temp.get(i));
        }
        return result;
    }

    @Override
    public List<String> getAllGroups() throws Exception {
        Stub serviceStub = getServiceStub();
        List<String> temp = Arrays.asList(((RemoteUserStoreManagerServiceStub) serviceStub).getRoleNames());
        List<String> result = new ArrayList<String>();
        for(int i=0;i<temp.size();i++){
            if(temp.get(i).equals(ADMIN_ROLE) || temp.get(i).startsWith(INTERNAL_ROLE_PREFIX)){
                continue;
            }
            result.add(temp.get(i));
        }

        return result;
    }

    private Stub getServiceStub() throws AxisFault {
        String serviceName = "RemoteUserStoreManagerService";
        ServerProperties properties = ServerProperties.getInstance();
        String endPoint = properties.getProperty(Constants.IS_URL, "") + "/services/" + serviceName;
        Stub serviceStub = new RemoteUserStoreManagerServiceStub(endPoint);

        ServiceClient serviceClient;
        Options options;
        serviceClient = serviceStub._getServiceClient();
        options = serviceClient.getOptions();
        HttpTransportProperties.Authenticator authenticator = new HttpTransportProperties.Authenticator();

        authenticator.setUsername(properties.getProperty(Constants.IS_USERNAME,""));
        authenticator.setPassword(properties.getProperty(Constants.IS_PASSWORD, ""));
        authenticator.setPreemptiveAuthentication(true);
        options.setProperty(HTTPConstants.AUTHENTICATE, authenticator);
        serviceClient.setOptions(options);

        return serviceStub;
    }
}