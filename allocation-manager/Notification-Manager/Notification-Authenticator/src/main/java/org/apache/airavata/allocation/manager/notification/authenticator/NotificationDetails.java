package org.apache.airavata.allocation.manager.notification.authenticator;

import org.apache.airavata.allocation.manager.server.AllocationManagerServerHandler;
import org.apache.thrift.TException;

public class NotificationDetails {

	public String[] getRequestDetails(String projectID) {
		String result[] = new String[2] ;
		AllocationManagerServerHandler obj  = new AllocationManagerServerHandler();
		 try {
			result[0] = obj.getAllocationRequestStatus(projectID);
			if(result[0].equals("IN_PROCESS")) {
				result[1] = obj.getAllocationRequestUserName(projectID);
			}
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
}
