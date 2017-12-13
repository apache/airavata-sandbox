package org.apache.airavata.allocation.manager.notification.receiver;

import org.apache.airavata.allocation.manager.notification.authenticator.NotificationDetails;
import org.apache.airavata.allocation.manager.notification.models.NotificationInformation;
import org.apache.airavata.allocation.manager.notification.sender.MailNotification;
import org.apache.thrift.transport.TServerSocket;
import com.rabbitmq.client.*;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NotificationReceiver {
	public static void StartsimpleServer() {
		try {

			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localHost");
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.queueDeclare("notify", false, false, false, null);
			// Comfort logging
			System.out.println("Waiting for notification");
			Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String request = new String(body, "UTF-8");

					String notificationDetails[] = request.split(",");
					String projectId = notificationDetails[0];
					String notificationType = notificationDetails[1];

					NotificationInformation information = (new NotificationDetails()).getRequestDetails(projectId,
							notificationType);
					(new MailNotification()).sendMail(projectId, notificationType, information.getSenderList(),projectId);

				}
			};
			channel.basicConsume("notify", true, consumer);

		} catch (Exception e) {
			// Dump any error to the console
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		StartsimpleServer();
	}
}
