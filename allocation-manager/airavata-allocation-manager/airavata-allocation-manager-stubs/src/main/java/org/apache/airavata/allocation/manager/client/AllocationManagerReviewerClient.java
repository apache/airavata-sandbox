package org.apache.airavata.allocation.manager.client;

import org.apache.airavata.allocation.manager.service.cpi.AllocationRegistryService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class AllocationManagerReviewerClient {
	public  void reviewerFunctions(String requestType, String projectId, String status) {

		try {
			TTransport transport;

			transport = new TSocket("localhost", 9091);
			transport.open();

			TProtocol protocol = new TBinaryProtocol(transport);
			
			AllocationRegistryService.Client client = new AllocationRegistryService.Client(protocol);
			if (requestType.equals("UPDATE_REQUEST")) {
				client.updateAllocationRequestStatus(projectId, status);
			} else if(requestType.equals("GET_REQUEST")){
				client.getAllocationRequest(projectId);
			}

			transport.close();
		} catch (TTransportException e) {
			e.printStackTrace();
		} catch (TException x) {
			x.printStackTrace();
		}
	}
}
