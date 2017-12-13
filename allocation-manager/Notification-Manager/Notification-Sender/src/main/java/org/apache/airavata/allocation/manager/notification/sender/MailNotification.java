package org.apache.airavata.allocation.manager.notification.sender;


import java.util.List;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class MailNotification {


	public void sendMail(String requestId, String status, List<String> senderList, String projectId) {

		EmailNotificationMessage message = new EmailNotificationMessage();
		EmailNotificationConfiguration emailConfiguration = new EmailNotificationConfiguration();

		String username = emailConfiguration.getCredentials().getUserName();
		String password = emailConfiguration.getCredentials().getPassword();
		
		String subject = message.getEmailMessage(status ,projectId).getSubject();
		String body = message.getEmailMessage(status, projectId).getMessage();
		
		mail( username,  password,  subject,  body,  senderList);

	}
	
	public void mail(String username, String password, String subject, String body, List<String> senderList) {
		
		Email email = new SimpleEmail();
		email.setHostName("smtp.googlemail.com");
		email.setSmtpPort(465);

		email.setAuthenticator(new DefaultAuthenticator(username, password));
		email.setSSLOnConnect(true);
		try {
			email.setFrom(username);
			email.setSubject(subject);
			email.setMsg(body);

			for(String s : senderList) {
				email.addTo(s);
			}

			email.send();
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Mail sent");
	}
}
