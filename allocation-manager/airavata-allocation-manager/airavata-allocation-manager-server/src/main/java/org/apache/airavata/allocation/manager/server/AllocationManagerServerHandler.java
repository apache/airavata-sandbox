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

	public AllocationManagerServerHandler()
			throws AllocationManagerException, ApplicationSettingsException, TException, Exception {
		JPAUtils.initializeDB();
	}

	@Override
	public boolean createUser(UserDetail userDetail) throws TException {
		// TODO Auto-generated method stub
		try {
			UserDetail create = (new UserDetailRepository()).create(userDetail);
			return true;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new AllocationManagerException()
					.setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
		}
	}

	// Implementing createAllocationRequest method
	@Override
	public String createAllocationRequest(UserAllocationDetail reqDetails)
			throws AllocationManagerException, TException {
		try {
			if ((new UserAllocationDetailRepository()).isExists(reqDetails.getProjectId())) {
				throw new TException("There exist project with the id");
			}
			reqDetails.setStatus(DBConstants.RequestStatus.PENDING);
			UserAllocationDetail create = (new UserAllocationDetailRepository()).create(reqDetails);
			String projectId =reqDetails.getProjectId();
			(new NotificationManager()).notificationSender(projectId, "NEW_REQUEST");
			return projectId;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new AllocationManagerException()
					.setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
		}
	}

	@Override
	public boolean deleteAllocationRequest(String projectId) throws TException {
		try {
			(new UserAllocationDetailRepository()).delete(projectId);
			(new NotificationManager()).notificationSender(projectId, "NEW_REQUEST");
			return true;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new AllocationManagerException()
					.setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
		}
	}

	@Override
	public boolean updateAllocationRequest(UserAllocationDetail reqDetails) throws TException {
		try {
			if ((new UserAllocationDetailRepository()).update(reqDetails).getProjectId() != null) {
				return true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new AllocationManagerException()
					.setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
		}
		return false;
	}

	@Override
	public UserAllocationDetail getAllocationRequest(String projectId) throws TException {
		try {
			return (new UserAllocationDetailRepository().get(projectId));
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new AllocationManagerException()
					.setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
		}
	}

	@Override
	public boolean isAdmin(String userName) throws AllocationManagerException, TException {
		try {
			UserDetail objUser = getUserDetails(userName);
			if (objUser == null) {
				throw new IllegalArgumentException();
			}
			return objUser.userType.equals(DBConstants.UserType.ADMIN);
		} catch (Exception ex) {
			throw new AllocationManagerException()
					.setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
		}
	}

	@Override
	public boolean isReviewer(String userName) throws AllocationManagerException, TException {
		try {
			UserDetail objUser = getUserDetails(userName);
			if (objUser == null) {
				throw new IllegalArgumentException();
			}
			return objUser.userType.equals(DBConstants.UserType.REVIEWER);
		} catch (Exception ex) {
			throw new AllocationManagerException()
					.setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
		}
	}

	@Override
	public List<UserAllocationDetail> getAllRequestsForReviewers(String userName)
			throws AllocationManagerException, TException {
		List<UserAllocationDetail> userAllocationDetailList = new ArrayList<UserAllocationDetail>();
		try {
			if (!isReviewer(userName)) {
				throw new AllocationManagerException().setMessage("Invalid reviewer id!");
			}
			List<ProjectReviewer> projReviewerList = (new ProjectReviewerRepository()).getProjectForReviewer(userName);
			List<String> projectIds = new ArrayList<String>();
			for (ProjectReviewer objProj : projReviewerList) {
				projectIds.add(objProj.getProjectId());
			}
			return new UserAllocationDetailRepository().get(projectIds);
		} catch (Exception ex) {
			throw new AllocationManagerException()
					.setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
		}
	}

	@Override
	public UserDetail getUserDetails(String userName) throws AllocationManagerException, TException {
		try {
			return (new UserDetailRepository()).get(userName);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new AllocationManagerException()
					.setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
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
			throw new AllocationManagerException()
					.setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
		}
		return userAllocationDetailList;
	}

	@Override
	public boolean assignReviewers(String projectId, String reviewerId, String adminId) throws TException {
		try {
			if (!isAdmin(adminId)) {
				throw new AllocationManagerException().setMessage("Invalid admin id!");
			}
			if (!isReviewer(reviewerId)) {
				throw new AllocationManagerException().setMessage("Invalid reviewer id!");
			}

			ProjectReviewer projectReviewer = new ProjectReviewer();
			projectReviewer.setProjectId(projectId);
			projectReviewer.setReviewer(reviewerId);

			ProjectReviewer projectReviewerObj = new ProjectReviewerRepository().create(projectReviewer);
			if (projectReviewerObj.getProjectId() != null) {
				// Update the status to under review.
				// Construct the primary key
				UserAllocationDetail userAllocationDetail = new UserAllocationDetailRepository().get(projectId)
						.setStatus(DBConstants.RequestStatus.UNDER_REVIEW);
				// Updates the request
				new UserAllocationDetailRepository().update(userAllocationDetail);
			}
			(new NotificationManager()).notificationSender(projectId, "ASSIGN_REQUEST");
		} catch (Exception ex) {
			throw new AllocationManagerException()
					.setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
		}
		return true;
	}

	@Override
	public List<ReviewerAllocationDetail> getAllReviewsForARequest(String projectId) throws TException {
		List<ReviewerAllocationDetail> reviewerAllocationDetailList = new ArrayList<ReviewerAllocationDetail>();
		try {
			reviewerAllocationDetailList = new ReviewerAllocationDetailRepository().getAllReviewsForARequest(projectId);
		} catch (Exception ex) {
			throw new AllocationManagerException()
					.setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
		}
		return reviewerAllocationDetailList;
	}

	@Override
	public List<UserDetail> getAllReviewers() throws TException {
		try {
			return new UserDetailRepository().getAllReviewers();
		} catch (Exception ex) {
			java.util.logging.Logger.getLogger(AllocationManagerServerHandler.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		return null;
	}

	@Override
	public boolean updateRequestByReviewer(ReviewerAllocationDetail reviewerAllocationDetail) throws TException {
		try {
			ReviewerAllocationDetail reviewerAllocationDetailObj = new ReviewerAllocationDetail();
			reviewerAllocationDetailObj = new ReviewerAllocationDetailRepository()
					.isProjectExists(reviewerAllocationDetail.getProjectId(), reviewerAllocationDetail.getUsername());
			if (reviewerAllocationDetailObj != null) {
				reviewerAllocationDetail.setId(reviewerAllocationDetailObj.getId());
				reviewerAllocationDetailObj = (new ReviewerAllocationDetailRepository())
						.update(reviewerAllocationDetail);
			} else {
				reviewerAllocationDetailObj = (new ReviewerAllocationDetailRepository())
						.create(reviewerAllocationDetail);
			}
			if (reviewerAllocationDetailObj.getProjectId() != null) {
				return true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new AllocationManagerException()
					.setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
		}
		return false;
	}

	/* Method to get a list of all reviewers not yet assigned to a request */
	@Override
	public List<UserDetail> getAllUnassignedReviewersForRequest(String projectId) throws TException {
		List<UserDetail> userDetailList = getAllReviewers();
		// TO_DO part
		List<UserDetail> reviewerList = new ArrayList<UserDetail>();
		for (UserDetail userDetailObj : userDetailList) {
			try {
				if (!new ProjectReviewerRepository().isProjectExists(projectId, userDetailObj.getUsername())) {
					reviewerList.add(userDetailObj);
				}
			} catch (Exception ex) {
				java.util.logging.Logger.getLogger(AllocationManagerServerHandler.class.getName()).log(Level.SEVERE,
						null, ex);
			}
		}
		return reviewerList;
	}

	/*
	 * Method to update the request's start date, end date, status and award
	 * allocation on approval
	 */
	@Override
	public boolean approveRequest(String projectId, String adminId, long startDate, long endDate, long awardAllocation)
			throws TException {
		try {
			if (!isAdmin(adminId)) {
				throw new AllocationManagerException().setMessage("Invalid admin id!");
			}
			// Create UserAllocationDetail object to call update method
			UserAllocationDetail userAllocDetail = new UserAllocationDetail();
			userAllocDetail = new UserAllocationDetailRepository().get(projectId);
			userAllocDetail.setStatus(DBConstants.RequestStatus.APPROVED);
			userAllocDetail.setStartDate(startDate);
			userAllocDetail.setEndDate(endDate);
			userAllocDetail.setAwardAllocation(awardAllocation);
			
			(new NotificationManager()).notificationSender(projectId, "APPROVE_REQUEST");
			
			// updates the request
			return updateAllocationRequest(userAllocDetail);
		} catch (Exception ex) {
			throw new AllocationManagerException()
					.setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
		}
	}

	/* Method to update the request status to rejected on reject of a request */
	@Override
	public boolean rejectRequest(String projectId, String adminId) throws TException {
		try {
			if (!isAdmin(adminId)) {
				throw new AllocationManagerException().setMessage("Invalid admin id!");
			}

			// Create UserAllocationDetail object to call update method
			UserAllocationDetail userAllocDetail = new UserAllocationDetail();
			userAllocDetail = new UserAllocationDetailRepository().get(projectId);
			userAllocDetail.setStatus(DBConstants.RequestStatus.REJECTED);

			// Updates the request
			(new NotificationManager()).notificationSender(projectId, "DENY_REQUEST");
			return updateAllocationRequest(userAllocDetail);
		} catch (Exception ex) {
			throw new AllocationManagerException()
					.setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
		}
	}

	@Override
	public String getAllocationRequestStatus(String projectId) throws TException {
		try {
			return (new UserAllocationDetailRepository().get(projectId).getStatus());
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new AllocationManagerException()
					.setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
		}
	}

	@Override
	public String getAllocationRequestUserEmail(String projectId) throws TException {
		try {
			String username = getAllocationRequestUserName(projectId);
			return ((new UserDetailRepository()).get(username)).getEmail();
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new AllocationManagerException()
					.setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
		}
	}

	@Override
	public String getAllocationRequestUserName(String projectId) throws org.apache.thrift.TException {
		try {
			return ((new UserAllocationDetailRepository()).get(projectId)).getUsername();
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new AllocationManagerException()
					.setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
		}
	}

	@Override
	public String getAllocationManagerAdminEmail(String userType) throws TException {
		try {
			return (new UserDetailRepository()).getAdminDetails().getEmail();
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new AllocationManagerException()
					.setMessage(ex.getMessage() + " Stack trace:" + ExceptionUtils.getStackTrace(ex));
		}
	}

	public List<String> getEmailIdsOfReviewersForRequest(String projectId) {
		List<String> reviewerEmailList = new ArrayList<>();
		try {
			for (ReviewerAllocationDetail s : getAllReviewsForARequest(projectId)) {
				reviewerEmailList.add(getUserDetails(s.getUsername()).getEmail());
			}
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reviewerEmailList;
	}
}
