/**
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
 */
package org.apache.airavata.allocation.manager.server;

import org.apache.airavata.allocation.manager.db.repositories.RequestStatusRepository;
import org.apache.airavata.allocation.manager.db.repositories.UserAllocationDetailPKRepository;
import org.apache.airavata.allocation.manager.db.repositories.UserDetailRepository;
import org.apache.airavata.allocation.manager.models.*;
import org.apache.commons.lang3.exception.ExceptionUtils;


public class AllocationManagerServerHandler implements AllocationRegistryService.Iface {
	
	public String getAllocationRequestStatus(String projectId) throws org.apache.thrift.TException{	
		try{
            return (new RequestStatusRepository()).get(projectId).status;
        }catch (Throwable ex) {
            throw new Exception("Could not get project status");
        }
	}
	
	public String getAllocationRequestPIEmail(String projectId) throws org.apache.thrift.TException{	
		try{
			String userName = getAllocationRequestUserName(projectId);
            return (new UserDetailRepository()).get(userName).email;
        }catch (Throwable ex) {
            throw new Exception("Could not get email");
        }
	}
	
	public String getAllocationRequestUserName(String projectId) throws org.apache.thrift.TException{	
		try{
            return (new UserAllocationDetailPKRepository()).get(projectId).username;
        }catch (Throwable ex) {
            throw new Exception("Could not get project status");
        }
	}
	
	public String getAllocationRequestAdminEmail() throws org.apache.thrift.TException{	
		try{
            return (new UserDetailRepository()).getAdminDetails().getEmail();

        }catch (Throwable ex) {
            throw new Exception("Could not get project status");
        }
	}
}