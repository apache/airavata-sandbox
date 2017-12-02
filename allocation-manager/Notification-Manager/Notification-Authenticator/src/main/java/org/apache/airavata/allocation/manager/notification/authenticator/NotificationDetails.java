package org.apache.airavata.allocation.manager.notification.authenticator;

import java.util.ArrayList;
import java.util.List;

import org.apache.airavata.allocation.manager.notification.models.NotificationInformation;
import org.apache.airavata.allocation.manager.server.AllocationManagerServerHandler;
import org.apache.thrift.TException;

public class NotificationDetails {

	public NotificationInformation getRequestDetails(String projectID) {
		NotificationInformation result = new NotificationInformation() ;
		
		 try {
                     AllocationManagerServerHandler obj  = new AllocationManagerServerHandler();
			 String status =  obj.getAllocationRequestStatus(projectID);
			 
			 List<String>senderList = new ArrayList<String>() ;
				
				senderList.add(obj.getAllocationRequestUserName(projectID));
				senderList.add(obj.getAllocationManagerAdminEmail("ADMIN"));
				
			result.setStatus(status);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
}
