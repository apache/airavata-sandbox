 namespace java org.apache.airavata.allocation.manager.models
 
/**
* <p>Required allocation request details</p>
* <li>id : (primary key) Ask the user to assign project ID, but this project should unique, we will need an API endpoint to check whether this ID is not used by other projects and the username</li>
* <li>applicationsToBeUsed : Select the application that the user intends to use, according to application chosen here, resources that can be allocable will be fetch from resource discovery module. User will not be restricted to these application upon allocation grant, provided the resources allocated support the application.</li>
* <li>diskUsageRangePerJob : An optional field to help reviewer and PI for allocation approval</li>
* <li>documents : Resume, CV, PI’s portfolio etc</li>
* <li>fieldOfScience :An optional field to help reviewer and PI for allocation approval</li>
* <li>keywords : Keyword will be helpful in search</li>
* <li>maxMemoryPerCpu :An optional field to help reviewer and PI for allocation approval</li>
* <li>numberOfCpuPerJob : An optional field to help reviewer and PI for allocation approval</li>
* <li>projectDescription :(Eg: Hypothesis, Model Systems, Methods, and Analysis)</li>
* <li>projectReviewedAndFundedBy : (Eg., NSF, NIH, DOD, DOE, None etc...). An optional field to help reviewer and PI for allocation approval</li>
* <li>requestedDate: The date the allocation was requested</li>
* <li>serviceUnits : 1 SU is approximately 1 workstation CPU hour, if the user fails to give a value, default value will be chosen.</li>
* <li>specificResourceSelection : This list will be fetched from resource discovery module, in case of community allocation, the request is subject to reviewers, PI discretion and availability</li>
* <li>title : Assign a title to allocation request</li>
* <li>typeOfAllocation : If the User has an exclusive allocation with third party organization and wants to use airavata middleware to manage jobs.</li>
* <li>typicalSuPerJob :  An optional field to help reviewer and PI for allocation approval</li>
**/
struct UserAllocationDetail{
1:optional string projectId,
2:optional string applicationsToBeUsed,
3:optional i64 diskUsageRangePerJob,
4:optional binary documents,
5:optional string fieldOfScience,
6:optional string keywords,
7:optional i64 maxMemoryPerCpu,
8:optional i64 numberOfCpuPerJob,
9:optional string projectDescription,
10:optional string projectReviewedAndFundedBy,
11:optional i64 requestedDate,
12:optional i64 serviceUnits,
13:optional string specificResourceSelection,
14:optional string title,
15:optional string typeOfAllocation,
16:optional i64 typicalSuPerJob,
17:optional i64 awardAllocation,
18:optional i64 startDate,
19:optional i64 endDate,
20:optional string status,
21:optional string username
}

struct ReviewerAllocationDetail{
1:optional string projectId,
2:optional string applicationsToBeUsed,
3:optional i64 diskUsageRangePerJob,
4:optional binary documents,
5:optional string fieldOfScience,
6:optional string keywords,
7:optional i64 maxMemoryPerCpu,
8:optional i64 numberOfCpuPerJob,
9:optional string projectDescription,
10:optional string projectReviewedAndFundedBy,
11:optional i64 requestedDate,
12:optional i64 serviceUnits,
13:optional string specificResourceSelection,
14:optional string title,
15:optional string typeOfAllocation,
16:optional i64 typicalSuPerJob,
17:optional i64 awardAllocation,
18:optional i64 startDate,
19:optional i64 endDate,
20:optional string status,
21:optional string username,
22:optional i64 id
}

/**
* <p>Allocation Request status details</p>
* <li>projectId: Unique id of the project</li>
* <li>awardAllocation: Allocation awarded</li>
* <li>endDate: End date of the request</li>
* <li>reviewers: reviewers of the request</li>
* <li>startDate: Start date of the allocation</li>
* <li>status: Status of the allocation request</li>
**/
struct ProjectReviewer{
	1:optional string projectId,
	2:optional string reviewer,
	3:optional i64 id
}

/**
* <p>A user should have an account with airavata to request an allocation</p>
* <li>username : Login Username</li>
* <li>email :Login email</li>
* <li>fullName: Full name of the user</li>
* <li>Password: Password of the user</li>
* <li>userType: Type of the user</li>
**/
struct UserDetail{
	1: optional string username,
 	2: optional string email,
 	3: optional string fullName,
 	4: optional string password,
 	5: optional string userType
}

/**
* <p>Exception model used in the allocation manager service</p>
**/
exception AllocationManagerException {
  1: required string message
}

