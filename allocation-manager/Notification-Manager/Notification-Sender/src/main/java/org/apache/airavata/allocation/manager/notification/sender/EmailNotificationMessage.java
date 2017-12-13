package org.apache.airavata.allocation.manager.notification.sender;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.airavata.allocation.manager.notification.models.NotificationMessage;

public class EmailNotificationMessage {
	public NotificationMessage getEmailMessage(String status, String projectID) {
		NotificationMessage result = new NotificationMessage();
		Properties prop = new Properties();
		InputStream input = null;

		try {
			InputStream input2 = new FileInputStream("./src/main/resources/messages.properties");
			prop.load(input2);

			switch (status) {
			case "APPROVE_REQUEST":
				result.setMessage(prop.getProperty("SUBJECT_APPROVED")+" "+projectID);
				result.setSubject(prop.getProperty("MESSAGE_APPROVED")+" "+projectID);
				break;
			case "DENY_REQUEST":
				result.setMessage(prop.getProperty("SUBJECT_REJECTED")+" "+projectID);
				result.setSubject(prop.getProperty("MESSAGE_REJECTED")+" "+projectID);
				break;
			case "IN_PROGRESS":
				result.setMessage(prop.getProperty("SUBJECT_IN_PROCESS")+" "+projectID);
				result.setSubject(prop.getProperty("MESSAGE_IN_PROCESS")+" "+projectID);
				break;
			case "NEW_REQUEST":
				result.setMessage(prop.getProperty("SUBJECT_NEW_REQUEST")+" "+projectID);
				result.setSubject(prop.getProperty("MESSAGE_NEW_REQUEST")+" "+projectID);
				break;
			case "ASSIGN_REQUEST":
				result.setMessage(prop.getProperty("SUBJECT_ASSIGN_REQUEST")+" "+projectID);
				result.setSubject(prop.getProperty("MESSAGE_ASSIGN_REQUEST")+" "+projectID);
				break;
			case "DELETE_REQUEST":
				result.setMessage(prop.getProperty("SUBJECT_DELETE_REQUEST")+" "+projectID);
				result.setSubject(prop.getProperty("MESSAGE_DELETE_REQUEST")+" "+projectID);
				break;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}
}
