namespace java org.apache.airavata.allocation.manager.service.cpi

include "./allocation_manager_models.thrift"

service AllocationRegistryService{

    /**
      <p>API method to create new allocation requests</p>
    */
    string createAllocationRequest(1: required allocation_manager_models.UserAllocationDetail allocDetail)
    
    /**
     <p>API method to delete allocation request</p>
    */
    bool deleteAllocationRequest(1: required string projectId)
    
    /**
     <p>API method to get an allocation Request</p>
    */
    allocation_manager_models.UserAllocationDetail getAllocationRequest(1: required string projectId)
    
    /**
     <p>API method to update an allocation Request</p>
    */
    bool updateAllocationRequest(1: required allocation_manager_models.UserAllocationDetail allocDetail)

   /**
    <p>API method to get an allocation Request status</p>
    */
    string getAllocationRequestStatus(1: required string projectId)

    /**
    <p>API method to get an allocation Request PI email</p>
    */
    string getAllocationRequestUserEmail(1: required string userName)

    /**
    <p>API method to get an allocation Request Admin email</p>
    */
    string getAllocationManagerAdminEmail(1: required string userType)

    /**
    <p>API method to get an allocation Request PI</p>
    */
    string getAllocationRequestUserName(1: required string projectId)

/**
    <p>API method to get all requests for admin</p>
    */
    list<allocation_manager_models.UserAllocationDetail> getAllRequestsForAdmin(1: required string userName)

/**
    <p>API method to assign reviewers</p>
    */
    bool assignReviewers(1:required string projectId , 2: required string reviewerId, 3: required string adminId)

/**
    <p>API method to update request submitted by reviewer</p>
    */
    bool updateRequestByReviewer(1:required allocation_manager_models.ReviewerAllocationDetail reviewerAllocationDetail)

    
    /**
        <p>API method to check if the logged in user is an Admin</p>
        */
        bool isAdmin(1: required string userName)

    /**
        <p>API method to check if the logged in user is a Reviewer</p>
        */
        bool isReviewer(1: required string userName)

    /**
        <p>API method to get all requests assigned to the reviewers</p>
        */
        list<allocation_manager_models.UserAllocationDetail> getAllRequestsForReviewers(1: required string userName)
    
    /**
        <p>API method to get a user details</p>
        */
        allocation_manager_models.UserDetail getUserDetails(1: required string userName)

    /**
        <p>API method to get all the reviews for a request</p>
        */
        list<allocation_manager_models.ReviewerAllocationDetail> getAllReviewsForARequest(1:required string projectId)

 	/**
        <p>API method to get all reviewers</p>
        */
        list<allocation_manager_models.UserDetail> getAllReviewers()
	/**
        <p>API method to get all unassigned reviewers for a request</p>
    */
        list<allocation_manager_models.UserDetail> getAllUnassignedReviewersForRequest(1:required string projectId)
        /**
        <p>API method to approve a request</p>
        */
        bool approveRequest(1:required string projectId, 2:required string adminId, 3:required i64 startDate, 4:required i64 endDate, 5:required i64 awardAllocation)
 /**
        <p>API method to reject a request</p>
        */
        bool rejectRequest(1:required string projectId, 2:required string adminId)

        /**
        <p>API method to create a new user</p>
        */
        bool createUser(1:required allocation_manager_models.UserDetail userDetail)
}
