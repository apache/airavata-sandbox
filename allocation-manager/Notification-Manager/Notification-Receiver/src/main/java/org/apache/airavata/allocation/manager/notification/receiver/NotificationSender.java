package org.apache.airavata.allocation.manager.notification.receiver;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class NotificationSender {
	private final static String QUEUE_NAME = "notify";

	public static void main(String[] args) {

		try {

			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);

			// this is the project id and notification type that is sent 
			String project_ID = "1234,DENY_REQUEST";

			channel.basicPublish("", QUEUE_NAME, null, project_ID.getBytes());

			System.out.println(" [x] Sent the request");

			// Close the channel
			channel.close();
			// Close the connection
			connection.close();

		} catch (Exception e) {
			// Dump any exception to the console
			e.printStackTrace();
		}
	}

}
