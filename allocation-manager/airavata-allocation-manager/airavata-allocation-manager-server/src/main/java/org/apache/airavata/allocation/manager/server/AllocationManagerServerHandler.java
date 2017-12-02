/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
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

import org.apache.airavata.common.exception.ApplicationSettingsException;
import org.apache.airavata.allocation.manager.db.repositories.*;
import org.apache.airavata.allocation.manager.db.utils.JPAUtils;
import org.apache.airavata.allocation.manager.models.*;
import org.apache.airavata.allocation.manager.service.cpi.AllocationRegistryService;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AllocationManagerServerHandler implements AllocationRegistryService.Iface {
    
    private final static Logger logger = LoggerFactory.getLogger(AllocationManagerServerHandler.class);

    public static String OWNER_PERMISSION_NAME = "OWNER";

    public AllocationManagerServerHandler() throws AllocationManagerException, ApplicationSettingsException, TException, Exception {
        JPAUtils.initializeDB();
    }

	    
    //Implementing createAllocationRequest method 
    @Override
    public String createAllocationRequest(UserAllocationDetail reqDetails) throws AllocationManagerException, TException {
        try{
            if((new UserAllocationDetailPKRepository()).isExists(reqDetails.id.projectId))  
            throw new TException("There exist project with the id");
            UserAllocationDetail create = (new UserAllocationDetailRepository()).create(reqDetails);
            return reqDetails.id.projectId;
        }catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new AllocationManagerException().setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
        }
    }

    //Implementing isAllocationRequestExists method to check if the allocation request exists
    @Override
    public boolean isAllocationRequestExists(String projectId) throws AllocationManagerException, TException {
        try{
            UserAllocationDetailPK alloc = new UserAllocationDetailPK();
            alloc.setProjectId(projectId);
            return ((new UserAllocationDetailRepository()).isExists(alloc.projectId));
        }catch (Exception ex) {
            throw new AllocationManagerException().setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
        }
    }

    //Implementing deleteAllocationRequest method to delete an allocation request
    @Override
    public boolean deleteAllocationRequest(String projectId) throws AllocationManagerException, TException {
        try{
            UserAllocationDetailPK alloc = new UserAllocationDetailPK();
            alloc.setProjectId(projectId);
            (new UserAllocationDetailPKRepository()).delete(alloc.projectId);
            return true;
        }catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new AllocationManagerException().setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
        }
    }

    //Implementing getAllocationRequest method to get an allocation request
    @Override
    public UserAllocationDetail getAllocationRequest(String projectId) throws AllocationManagerException, TException {
        try{
            UserAllocationDetailPK alloc = new UserAllocationDetailPK();
            alloc.setProjectId(projectId);
            return (new UserAllocationDetailRepository()).get(alloc.projectId);
        }catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new AllocationManagerException().setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
        }
    }

    @Override
    public boolean updateAllocationRequest(UserAllocationDetail reqDetails) throws TException {
         try{
            UserAllocationDetail userAlloc = new UserAllocationDetail();
            
            //Update UserAllocationDetail field.
            userAlloc.id.setProjectId(reqDetails.id.projectId);
            userAlloc.setApplicationsToBeUsed(reqDetails.applicationsToBeUsed);
            userAlloc.setDiskUsageRangePerJob(reqDetails.diskUsageRangePerJob);           
            userAlloc.setDocuments(reqDetails.documents);
            userAlloc.setFieldOfScience(reqDetails.fieldOfScience);
            userAlloc.setKeywords(reqDetails.keywords);
            userAlloc.setMaxMemoryPerCpu(reqDetails.maxMemoryPerCpu);
            userAlloc.setNumberOfCpuPerJob(reqDetails.numberOfCpuPerJob);
            userAlloc.setProjectDescription(reqDetails.projectDescription);
            userAlloc.setServiceUnits(reqDetails.serviceUnits);
            userAlloc.setSpecificResourceSelection(reqDetails.specificResourceSelection);
            userAlloc.setTypeOfAllocation(reqDetails.typeOfAllocation);
            userAlloc.setTypicalSuPerJob(reqDetails.typicalSuPerJob);

            (new UserAllocationDetailRepository()).update(userAlloc);
            return true;
        }catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new AllocationManagerException().setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
        }
    }
    
    @Override
    public String getAllocationRequestStatus(String projectId) throws org.apache.thrift.TException{ 
        try{
            return (new RequestStatusRepository()).get(projectId).status;
        }catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new AllocationManagerException().setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
        }
    }

    @Override
    public String getAllocationRequestUserEmail(String projectId) throws TException {    
        try{
            String userName = getAllocationRequestUserName(projectId);
            return (new UserDetailRepository()).get(userName).email;
        }catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new AllocationManagerException().setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
        }
    }
     
    @Override
    public String getAllocationRequestUserName(String projectId) throws org.apache.thrift.TException{   
        try{
            return (new UserAllocationDetailPKRepository()).get(projectId).username;
        }catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new AllocationManagerException().setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
        }
    }
    
    @Override
    public String getAllocationManagerAdminEmail(String userType) throws TException {
      try{
            return (new UserDetailRepository()).getAdminDetails().getEmail();

        }catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new AllocationManagerException().setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
        }    
    }
}