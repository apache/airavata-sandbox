/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.airavata.allocation.manager.client;

import java.util.List;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.airavata.allocation.manager.models.UserAllocationDetail;
import org.apache.airavata.allocation.manager.models.UserAllocationDetailPK;
import org.apache.airavata.allocation.manager.models.UserDetail;
import org.apache.airavata.allocation.manager.service.cpi.AllocationRegistryService;


public class testClient {

    public static void main(String[] args) {

        try {
            TTransport transport;

            transport = new TSocket("localhost", 9000);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            AllocationRegistryService.Client client = new AllocationRegistryService.Client(protocol);
            System.out.println("Started client");
            //System.out.println(client.isAdmin("admin"));
            
            UserAllocationDetailPK obj = new UserAllocationDetailPK();
            obj.setProjectId("123");
            obj.setUsername("MAD");
            UserAllocationDetail reqDetails = new UserAllocationDetail();
            reqDetails.setId(obj);
            reqDetails.setRequestedDate(101L);
            reqDetails.setTitle("Test");
            reqDetails.setProjectDescription("Test");
            reqDetails.setTypeOfAllocation("Comm");
            reqDetails.setStatus("pending");
            
            
            System.out.println(client.createUser("harsha"));
            
            //System.out.println(client.createAllocationRequest(reqDetails));
            
            //List<UserDetail> lst = client.getAllReviewers();
          /*  boolean temp= true;
            temp = client.isAdmin("admin");
            System.out.println(temp);*/
            transport.close();
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException x) {
            x.printStackTrace();
        }
        finally
        {
            //transport.close();
        }
    }

}
