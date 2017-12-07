namespace java org.apache.airavata.allocation.manager.service.cpi

include "./allocation_manager_models.thrift"

service AllocationRegistryService{

    /**
      <p>API method to create new allocation requests</p>
    */
    string createAllocationRequest(1: required allocation_manager_models.UserAllocationDetail allocDetail)
    
    /**
     <p>API method to check if the allocation request exists</p>
    */
    bool isAllocationRequestExists(1: required string projectId)
    
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
    <p>API method to update the status of a request</p>
    */
    void updateAllocationRequestStatus(1: required string projectId, 2: required string status)

/**
    <p>API method to get all requests for admin</p>
    */
    list<allocation_manager_models.UserAllocationDetail> getAllRequestsForAdmin(1: required string userName)

/**
    <p>API method to assign reviewers</p>
    */
    bool assignReviewers(1:required allocation_manager_models.UserAllocationDetailPK projectId , 2: required string userName)

/**
    <p>API method to update request submitted by reviewer</p>
    */
    bool updateRequestByReviewer(1:required allocation_manager_models.UserAllocationDetailPK projectId, 2:required allocation_manager_models.UserAllocationDetail userAllocationDetail)

    
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
        list<allocation_manager_models.UserAllocationDetail> getReviewsForRequest(1:required allocation_manager_models.UserAllocationDetailPK projectId)
}
