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

import static java.lang.System.in;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.apache.airavata.common.exception.ApplicationSettingsException;
import org.apache.airavata.allocation.manager.client.NotificationManager;
import org.apache.airavata.allocation.manager.db.entities.ProjectReviewerEntityPK;
import org.apache.airavata.allocation.manager.db.entities.UserAllocationDetailEntityPK;
import org.apache.airavata.allocation.manager.db.repositories.*;
import org.apache.airavata.allocation.manager.db.utils.DBConstants;
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
        try {
            UserAllocationDetailEntityPK objAllocationDetailEntityPK = new UserAllocationDetailEntityPK();
            objAllocationDetailEntityPK.setProjectId(reqDetails.id.projectId);
            objAllocationDetailEntityPK.setUsername(reqDetails.id.username);

            if ((new UserAllocationDetailRepository()).isExists(objAllocationDetailEntityPK)) {
                throw new TException("There exist project with the id");
            }
            reqDetails.setStatus(DBConstants.RequestStatus.PENDING);
            reqDetails.setIsPrimaryOwner(true);
            UserAllocationDetail create = (new UserAllocationDetailRepository()).create(reqDetails);
            return reqDetails.id.projectId;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new AllocationManagerException().setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
        }
    }

    //Implementing isAllocationRequestExists method to check if the allocation request exists
    @Override
    public boolean isAllocationRequestExists(String projectId, String userName) throws AllocationManagerException, TException {
        try {
            UserAllocationDetailEntityPK objAllocationDetailEntityPK = new UserAllocationDetailEntityPK();
            objAllocationDetailEntityPK.setProjectId(projectId);
            objAllocationDetailEntityPK.setUsername(userName);

            return ((new UserAllocationDetailRepository()).isExists(objAllocationDetailEntityPK));
        } catch (Exception ex) {
            throw new AllocationManagerException().setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
        }
    }

    //Implementing deleteAllocationRequest method to delete an allocation request
    @Override
    public boolean deleteAllocationRequest(String projectId, String userName) throws AllocationManagerException, TException {
        try {
            UserAllocationDetailEntityPK objAllocationDetailEntityPK = new UserAllocationDetailEntityPK();
            objAllocationDetailEntityPK.setProjectId(projectId);
            objAllocationDetailEntityPK.setUsername(userName);

            (new UserAllocationDetailRepository()).delete(objAllocationDetailEntityPK);
            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new AllocationManagerException().setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
        }
    }

    //Implementing getAllocationRequest method to get an allocation request
    @Override
    public UserAllocationDetail getAllocationRequest(String projectId, String userName) throws AllocationManagerException, TException {
        try {
            UserAllocationDetailEntityPK objAllocationDetailEntityPK = new UserAllocationDetailEntityPK();
            objAllocationDetailEntityPK.setProjectId(projectId);
            objAllocationDetailEntityPK.setUsername(userName);

            return (new UserAllocationDetailRepository().get(objAllocationDetailEntityPK));
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new AllocationManagerException().setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
        }
    }

    @Override
    public boolean updateAllocationRequest(UserAllocationDetail reqDetails) throws TException {
        try {
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
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new AllocationManagerException().setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
        }
    }

    @Override
    public String getAllocationRequestStatus(String projectId, String userName) throws org.apache.thrift.TException {
        try {
            UserAllocationDetailEntityPK objAllocDetails = new UserAllocationDetailEntityPK();
            objAllocDetails.setProjectId(projectId);
            objAllocDetails.setUsername(userName);
            return (new UserAllocationDetailRepository()).get(objAllocDetails).status;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new AllocationManagerException().setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
        }
    }

    @Override
    public String getAllocationRequestUserEmail(String projectId) throws TException {
        try {
            String userName = getAllocationRequestUserName(projectId);
            return (new UserDetailRepository()).get(userName).email;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new AllocationManagerException().setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
        }
    }

    @Override
    public String getAllocationRequestUserName(String projectId) throws org.apache.thrift.TException {
        try {
            return (new UserAllocationDetailPKRepository()).get(projectId).username;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new AllocationManagerException().setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
        }
    }

    @Override
    public String getAllocationManagerAdminEmail(String userType) throws TException {
        try {
            return (new UserDetailRepository()).getAdminDetails().getEmail();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new AllocationManagerException().setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
        }
    }

    @Override
    public void updateAllocationRequestStatus(String projectId, String status) throws TException {
        // TODO Auto-generated method stub
        UserAllocationDetailRepository userAllocationDetail = new UserAllocationDetailRepository();
        try {
            UserAllocationDetailEntityPK userAllocationDetailPK = new UserAllocationDetailEntityPK();
            userAllocationDetailPK.setProjectId(projectId);
            userAllocationDetailPK.setUsername(new UserAllocationDetailRepository().getPrimaryOwner(projectId));
            userAllocationDetail.get(userAllocationDetailPK).setStatus(status);

            //once status updated notify user
            (new NotificationManager()).notificationSender(projectId);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new AllocationManagerException().setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
        }

    }

    @Override
    public boolean isAdmin(String userName) throws AllocationManagerException, TException {
        try {
            UserDetail objUser = getUserDetails(userName);
            if (objUser == null) {
                throw new IllegalArgumentException();
            }
            return objUser.userType.equals("admin");
        } catch (Exception ex) {
            throw new AllocationManagerException().setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
        }
    }

    @Override
    public boolean isReviewer(String userName) throws AllocationManagerException, TException {
        try {
            UserDetail objUser = getUserDetails(userName);
            if (objUser == null) {
                throw new IllegalArgumentException();
            }
            return objUser.userType.equals("reviewer");
        } catch (Exception ex) {
            throw new AllocationManagerException().setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
        }
    }

    @Override
    public List<UserAllocationDetail> getAllRequestsForReviewers(String userName) throws AllocationManagerException, TException {
        List<UserAllocationDetail> userAllocationDetailList = new ArrayList<UserAllocationDetail>();
        try {
            if (!isReviewer(userName)) {
                throw new AllocationManagerException().setMessage("Invalid reviewer id!");
            }
            List<ProjectReviewer> projReviewerList = (new ProjectReviewerRepository()).getProjectForReviewer(userName);
            for (ProjectReviewer objProj : projReviewerList) {
                UserAllocationDetailEntityPK userAllocationDetailPK = new UserAllocationDetailEntityPK();
                userAllocationDetailPK.setProjectId(objProj.id.getProjectId());
                userAllocationDetailPK.setUsername(new UserAllocationDetailRepository().getPrimaryOwner(objProj.id.getProjectId()));
                userAllocationDetailList.add(new UserAllocationDetailRepository().get(userAllocationDetailPK));
            }
        } catch (Exception ex) {
            throw new AllocationManagerException().setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
        }
        return userAllocationDetailList;
    }

    @Override
    public UserDetail getUserDetails(String userName) throws AllocationManagerException, TException {
        try {
            return (new UserDetailRepository()).get(userName);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new AllocationManagerException().setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
        }
    }

    @Override
    public List<UserAllocationDetail> getAllRequestsForAdmin(String userName) throws TException {
        List<UserAllocationDetail> userAllocationDetailList = new ArrayList<UserAllocationDetail>();
        try {
            if (!isAdmin(userName)) {
                throw new AllocationManagerException().setMessage("Invalid admin id!");
            }
            userAllocationDetailList = new UserAllocationDetailRepository().getAllUserRequests();
        } catch (Exception ex) {
            throw new AllocationManagerException().setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
        }
        return userAllocationDetailList;
    }

    // To-do : update status to under review, add approve request method
    @Override
    public boolean assignReviewers(String projectId, String reviewerId, String adminId) throws TException {
        try {
            if (!isAdmin(adminId)) {
                throw new AllocationManagerException().setMessage("Invalid admin id!");
            }
            if (!isReviewer(reviewerId)) {
                throw new AllocationManagerException().setMessage("Invalid reviewer id!");
            }
            ProjectReviewerEntityPK projectReviewerEntityPK = new ProjectReviewerEntityPK();
            projectReviewerEntityPK.setProjectId(projectId);
            projectReviewerEntityPK.setReviewer(reviewerId);
            ProjectReviewer projectReviewer = new ProjectReviewer();
            projectReviewer.setId(new ProjectReviewerRepository().get(projectReviewerEntityPK).getId());

            ProjectReviewer projectReviewerObj = new ProjectReviewerRepository().create(projectReviewer);
            if (projectReviewerObj.getId() != null) {
                return true;
            }
        } catch (Exception ex) {
            throw new AllocationManagerException().setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
        }
        return false;
    }

    @Override
    public List<UserAllocationDetail> getAllReviewsForARequest(String projectId) throws TException {
        List<UserAllocationDetail> userAllocationDetailList = new ArrayList<UserAllocationDetail>();
        try {
            userAllocationDetailList = new UserAllocationDetailRepository().getAllReviewsForARequest(projectId);
        } catch (Exception ex) {
            throw new AllocationManagerException().setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
        }
        return userAllocationDetailList;
    }

    @Override
    public List<UserDetail> getAllReviewers() throws TException {
        try {
            return new UserDetailRepository().getAllReviewers();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(AllocationManagerServerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean updateRequestByReviewer(String reviewerId, UserAllocationDetail userAllocationDetail) throws TException {
        try {
            UserAllocationDetailEntityPK objAllocationDetailEntityPK = new UserAllocationDetailEntityPK();
            objAllocationDetailEntityPK.setProjectId(userAllocationDetail.getId().getProjectId());
            objAllocationDetailEntityPK.setUsername(reviewerId);
            UserAllocationDetail userAllocationDetailObj = new UserAllocationDetail();
            userAllocationDetail.setId(new UserAllocationDetailRepository().get(objAllocationDetailEntityPK).getId());
            userAllocationDetail.setIsPrimaryOwner(false);
            if ((new UserAllocationDetailRepository()).isExists(objAllocationDetailEntityPK)) {
                userAllocationDetailObj = (new UserAllocationDetailRepository()).update(userAllocationDetail);
            } else {
                userAllocationDetailObj = (new UserAllocationDetailRepository()).create(userAllocationDetail);
            }
            if (userAllocationDetailObj.getId().getProjectId() != null) {
                return true;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new AllocationManagerException().setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
        }
        return false;
    }

    @Override
    public List<UserDetail> getAllUnassignedReviewersForRequest(String projectId) throws TException {
        List<UserDetail> userDetailList = getAllReviewers();
        List<UserDetail> reviewerList = new ArrayList<UserDetail>();
        for (UserDetail userDetailObj : userDetailList) {
            ProjectReviewerEntityPK projectReviewerEntityPK = new ProjectReviewerEntityPK();
            projectReviewerEntityPK.setProjectId(projectId);
            projectReviewerEntityPK.setReviewer(userDetailObj.getUsername());
            try {
                if (!new ProjectReviewerRepository().isExists(projectReviewerEntityPK)) {
                    reviewerList.add(userDetailObj);
                }
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(AllocationManagerServerHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return reviewerList;
    }
}
