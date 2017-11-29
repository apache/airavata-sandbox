package org.apache.airavata.allocation.manager.notification.models;

import java.util.List;

public class NotificationInformation {

	private String status;
	List<String> SenderList;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<String> getSenderList() {
		return SenderList;
	}
	public void setSenderList(List<String> senderList) {
		SenderList = senderList;
	}
 }
