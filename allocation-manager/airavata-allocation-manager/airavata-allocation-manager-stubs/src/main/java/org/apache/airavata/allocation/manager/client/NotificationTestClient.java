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

public class NotificationTestClient {

    public static void main(String[] args) {

        try {
            TTransport transport;

            transport = new TSocket("localhost", 9040);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            AllocationRegistryService.Client client = new AllocationRegistryService.Client(protocol);
            System.out.println("Started client");
            
            System.out.println("Testing createAllocationRequest() ");

            UserAllocationDetail requestDetails = new UserAllocationDetail();
            requestDetails.setProjectId("4009");
            requestDetails.setUsername("nikitha");
            requestDetails.setRequestedDate(101L);
            requestDetails.setTitle("Test");
            requestDetails.setProjectDescription("Test");
            requestDetails.setTypeOfAllocation("Comm");
            requestDetails.setStatus("pending");

            System.out.println(client.createAllocationRequest(requestDetails));

//            System.out.println("Testing assignReviewers() ");

            System.out.println(client.assignReviewers("1234", "reviewer2", "admin"));
            System.out.println(client.assignReviewers("1234", "reviewer3", "admin"));

            System.out.println("Testing approveRequest() ");

            System.out.println(client.approveRequest("1234", "admin", 1l, 2l, 50l));
//            
//            System.out.println("######################");
//
            System.out.println("Testing rejectRequest() ");

            System.out.println(client.rejectRequest("1234", "admin"));
//            
//            System.out.println("######################");

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
