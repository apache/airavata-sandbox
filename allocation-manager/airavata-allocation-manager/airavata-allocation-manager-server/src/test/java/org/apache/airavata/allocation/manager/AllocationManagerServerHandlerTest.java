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
package org.apache.airavata.allocation.manager;

import junit.framework.Assert;
import org.apache.airavata.common.exception.ApplicationSettingsException;
import org.apache.airavata.allocation.manager.models.*;
import org.apache.airavata.allocation.manager.server.AllocationManagerServerHandler;
import org.apache.airavata.allocation.manager.util.Initialize;
import org.apache.thrift.TException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class AllocationManagerServerHandlerTest {
    private final static Logger logger = LoggerFactory.getLogger(AllocationManagerServerHandlerTest.class);

    @BeforeClass
    public static void setup() throws AllocationManagerException, SQLException {
//        Initialize initialize = new Initialize("sharing-registry-derby.sql");
//        initialize.initializeDB();
    }

    @Test
    public void test() throws Exception {
        AllocationManagerServerHandler allocationManagerServerHandler = new AllocationManagerServerHandler();

      //Creating requests
        UserAllocationDetail userAllocationDetails = new UserAllocationDetail();
        UserAllocationDetailPK userAllocationDetailPK = new UserAllocationDetailPK(); 
        userAllocationDetailPK.setProjectId("123");
        userAllocationDetailPK.setUsername("harsha");
        userAllocationDetails.setId(userAllocationDetailPK);
        userAllocationDetails.setApplicationsToBeUsed("AB, BC");
        userAllocationDetails.setDiskUsageRangePerJob(2L);
        userAllocationDetails.setTypicalSuPerJob(12l);
        userAllocationDetails.setTitle("Chemistry Project");
        userAllocationDetails.setSpecificResourceSelection("resource");
        userAllocationDetails.setServiceUnits(2L);
        userAllocationDetails.setProjectDescription("This project does the calculation ...");
        userAllocationDetails.setProjectReviewedAndFundedBy("NSA");
        userAllocationDetails.setMaxMemoryPerCpu(100L);
        userAllocationDetails.setDiskUsageRangePerJob(20L);
        userAllocationDetails.setNumberOfCpuPerJob(5L);
        userAllocationDetails.setKeywords("chemistry,biology");
        userAllocationDetails.setFieldOfScience("chemistry");
        userAllocationDetails.setTypeOfAllocation("community");


//        Assert.assertNotNull(allocationManagerServerHandler.createAllocationRequest(userAllocationDetails));
//        Assert.assertEquals(allocationManagerServerHandler.getAllocationRequest("123",""),userAllocationDetails);

        UserAllocationDetail userAllocationDetails1 = new UserAllocationDetail();
        UserAllocationDetailPK userAllocationDetailPK1 = new UserAllocationDetailPK(); 
        userAllocationDetailPK1.setProjectId("123");
        userAllocationDetailPK1.setUsername("harsha");
        userAllocationDetails1.setId(userAllocationDetailPK1);

//        Assert.assertTrue(allocationManagerServerHandler.isAllocationRequestExists(userAllocationDetailPK1.getProjectId(),""));
//        Assert.assertEquals(allocationManagerServerHandler.createAllocationRequest(userAllocationDetails1),"There exist project with the id");
//
//        Assert.assertTrue(allocationManagerServerHandler.deleteAllocationRequest("123",""));
//   
    }
}