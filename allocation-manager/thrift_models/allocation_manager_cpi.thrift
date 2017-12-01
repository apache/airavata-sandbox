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
}
