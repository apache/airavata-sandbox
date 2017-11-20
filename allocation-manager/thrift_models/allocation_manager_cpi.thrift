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

}
