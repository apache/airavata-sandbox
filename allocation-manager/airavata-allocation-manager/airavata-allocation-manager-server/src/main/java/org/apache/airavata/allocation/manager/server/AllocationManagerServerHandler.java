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

import org.apache.airavata.common.exception.ApplicationSettingsException;
import org.apache.airavata.allocation.manager.db.entities.*;
import org.apache.airavata.allocation.manager.db.repositories.*;
import org.apache.airavata.allocation.manager.db.utils.DBConstants;
import org.apache.airavata.allocation.manager.db.utils.JPAUtils;
import org.apache.airavata.allocation.manager.models.*;
import org.apache.airavata.allocation.manager.service.cpi.AllocationRegistryService;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

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
            (new UserAllocationDetailRepository()).create(reqDetails);
            return reqDetails.id.projectId;
        }catch (Throwable ex) {
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
        }catch (Throwable ex) {
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
        }catch (Throwable ex) {
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
        }catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
            throw new AllocationManagerException().setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
        }
    }
}