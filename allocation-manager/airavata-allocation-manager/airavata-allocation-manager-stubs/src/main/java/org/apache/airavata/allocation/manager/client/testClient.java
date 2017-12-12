/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.airavata.allocation.manager.client;

import java.util.List;
import org.apache.airavata.allocation.manager.models.ReviewerAllocationDetail;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.airavata.allocation.manager.models.UserAllocationDetail;
import org.apache.airavata.allocation.manager.models.UserDetail;
import org.apache.airavata.allocation.manager.service.cpi.AllocationRegistryService;

public class testClient {

    public static void main(String[] args) {

        try {
            TTransport transport;

            transport = new TSocket("localhost", 9030);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            AllocationRegistryService.Client client = new AllocationRegistryService.Client(protocol);
            System.out.println("Started client");

//          System.out.println("Testing createUser() ");

//          UserDetail reqDetails = new UserDetail();
//          reqDetails.setEmail("admin@gmail.com");
//          reqDetails.setFullName("admin");
//          reqDetails.setPassword("admin");
//          reqDetails.setUsername("admin");
//          reqDetails.setUserType("ADMIN");
//
//          System.out.println(client.createUser(reqDetails));
          
//          UserDetail reqDetails1 = new UserDetail();
//          reqDetails1.setEmail("reviewer1@gmail.com");
//          reqDetails1.setFullName("reviewer1");
//          reqDetails1.setPassword("reviewer1");
//          reqDetails1.setUsername("reviewer1");
//          reqDetails1.setUserType("REVIEWER");
//
//          System.out.println(client.createUser(reqDetails1));
//          
//          UserDetail reqDetails2 = new UserDetail();
//          reqDetails2.setEmail("reviewer2@gmail.com");
//          reqDetails2.setFullName("reviewer2");
//          reqDetails2.setPassword("reviewer2");
//          reqDetails2.setUsername("reviewer2");
//          reqDetails2.setUserType("REVIEWER");
//
//          System.out.println(client.createUser(reqDetails2));
//
//          UserDetail reqDetails3 = new UserDetail();
//          reqDetails3.setEmail("reviewer3@gmail.com");
//          reqDetails3.setFullName("reviewer3");
//          reqDetails3.setPassword("reviewer3");
//          reqDetails3.setUsername("reviewer3");
//          reqDetails3.setUserType("REVIEWER");
//
//          System.out.println(client.createUser(reqDetails3));
//          
//          UserDetail reqDetails4 = new UserDetail();
//          reqDetails4.setEmail("user@gmail.com");
//          reqDetails4.setFullName("user");
//          reqDetails4.setPassword("user");
//          reqDetails4.setUsername("user");
//          reqDetails4.setUserType("USER");
//
//          System.out.println(client.createUser(reqDetails4));
        
            
//            System.out.println("Testing createAllocationRequest() ");
//
//            UserAllocationDetail reqDetails = new UserAllocationDetail();
//            reqDetails.setProjectId("123456");
//            reqDetails.setUsername("harsha");
//            reqDetails.setRequestedDate(101L);
//            reqDetails.setTitle("Test");
//            reqDetails.setProjectDescription("Test");
//            reqDetails.setTypeOfAllocation("Comm");
//            reqDetails.setStatus("pending");
//
//            System.out.println(client.createAllocationRequest(reqDetails));
//           
//            UserAllocationDetail reqDetails1 = new UserAllocationDetail();
//            reqDetails1.setProjectId("1234");
//            reqDetails1.setUsername("madrina");
//            reqDetails1.setRequestedDate(101L);
//            reqDetails1.setTitle("Test");
//            reqDetails1.setProjectDescription("Test");
//            reqDetails1.setTypeOfAllocation("Comm");
//            reqDetails1.setStatus("pending");

//            System.out.println(client.createAllocationRequest(reqDetails1));

//             System.out.println("Testing deleteAllocationRequest() ");
//
//             System.out.println(client.deleteAllocationRequest("123456"));

//            System.out.println("Testing getAllocationRequest() ");
//
//            UserAllocationDetail userAllocationDetail = client.getAllocationRequest("123456");
//            System.out.println("Successful"+ userAllocationDetail.getProjectDescription());
//            
//            System.out.println("Testing updateAllocationRequest() ");
//
//            UserAllocationDetail reqDetails = new UserAllocationDetail();
//            reqDetails.setProjectId("123456");
//            reqDetails.setTitle("Title Temp");
//
//            System.out.println(client.updateAllocationRequest(reqDetails));
//            
//          System.out.println("Testing isAdmin() ");
//
//          System.out.println("Successful: " + client.isAdmin("admin"));
//          
//          System.out.println("Testing isReviewer() ");
//
//          System.out.println("Successful: " + client.isReviewer("reviewer1"));

//         System.out.println("Testing getAllRequestsForAdmin() ");
//          
//          List<UserAllocationDetail> userAllocationDetailList= client.getAllRequestsForAdmin("admin");
//          for(UserAllocationDetail object: userAllocationDetailList)
//          {
//               System.out.println(object.getProjectId());
//          }
          
//            System.out.println("Testing assignReviewers() ");
//          
//            System.out.println(client.assignReviewers("123456","reviewer1","admin"));
//           System.out.println(client.assignReviewers("123456","reviewer2","admin"));
 //           System.out.println(client.assignReviewers("1234","reviewer2","admin"));
//            System.out.println(client.assignReviewers("1234","reviewer3","admin"));
            
//          System.out.println("Testing getAllRequestsForReviewers() ");
//          
//          List<UserAllocationDetail> userAllocationDetailList= client.getAllRequestsForReviewers("reviewer2");
//          for(UserAllocationDetail object: userAllocationDetailList)
//          {
//               System.out.println(object.getProjectId());
//          }
//          
//          System.out.println("Testing getUserDetails() ");
//          
//          UserDetail userDetail= client.getUserDetails("admin");
//               System.out.println(userDetail.getEmail());

          System.out.println("Testing updateRequestByReviewer() ");
            ReviewerAllocationDetail reviewerAllocationDetail = new ReviewerAllocationDetail();
            reviewerAllocationDetail.setProjectId("123456");
            reviewerAllocationDetail.setUsername("reviewer2");
            reviewerAllocationDetail.setMaxMemoryPerCpu(15L);
           System.out.println(client.updateRequestByReviewer(reviewerAllocationDetail));

//            ReviewerAllocationDetail reviewerAllocationDetail1 = new ReviewerAllocationDetail();
//            reviewerAllocationDetail1.setProjectId("123456");
//            reviewerAllocationDetail1.setUsername("reviewer1");
//            reviewerAllocationDetail1.setMaxMemoryPerCpu(5L);
//           System.out.println(client.updateRequestByReviewer(reviewerAllocationDetail1));
             
//          System.out.println("Testing getAllReviewsForARequest() ");
//          
//          List<ReviewerAllocationDetail> reviewerAllocationDetailList = client.getAllReviewsForARequest("123456");
//          for(ReviewerAllocationDetail object: reviewerAllocationDetailList)
//          {
//               System.out.println(object.getMaxMemoryPerCpu());
//          }
          
//          System.out.println("Testing getAllUnassignedReviewersForRequest() ");
//          
//          List<UserDetail> userDetailList = client.getAllUnassignedReviewersForRequest("123456");
//          for(UserDetail object: userDetailList)
//          {
//               System.out.println(object.getUsername());
//          }

//          System.out.println("Testing approveRequest() ");
//          
//         System.out.println(client.approveRequest("123456","admin",1l,2l,50l));
         
//            System.out.println("Testing rejectRequest() ");
//          
//         System.out.println(client.rejectRequest("1234","admin"));

            transport.close();
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException x) {
            x.printStackTrace();
        } finally {
            //transport.close();
        }
    }

}
