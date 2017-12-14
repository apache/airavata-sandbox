package org.apache.airavata.allocation.manager.notification.authenticator;

import java.util.ArrayList;
import java.util.List;

import org.apache.airavata.allocation.manager.notification.models.NotificationInformation;
import org.apache.airavata.allocation.manager.server.AllocationManagerServerHandler;
import org.apache.thrift.TException;

public class NotificationDetails {

	public NotificationInformation getRequestDetails(String projectID, String notificationType) {

		NotificationInformation result = new NotificationInformation();

		try {
			AllocationManagerServerHandler obj = new AllocationManagerServerHandler();
			List<String> senderList = new ArrayList<String>();

			if (notificationType.equals("NEW_REQUEST") || notificationType.equals("DELETE_REQUEST")) {
				senderList.add(obj.getAllocationRequestUserEmail(projectID));
				senderList.add(obj.getAllocationManagerAdminEmail("ADMIN"));

			} else if (notificationType.equals("ASSIGN_REQUEST")) {
				senderList = obj.getEmailIdsOfReviewersForRequest(projectID);
			} else if (notificationType.equals("APPROVE_REQUEST")) {
				senderList.add(obj.getAllocationRequestUserEmail(projectID));
			} else if (notificationType.equals("DENY_REQUEST")) {
				senderList.add(obj.getAllocationRequestUserEmail(projectID));
			}

			result.setSenderList(senderList);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
}
