/*
*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*
*/
package org.apache.airavata.datacat.agent.org.apache.airavata.datacat.agent.messageBroker;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.airavata.datacat.agent.messageBroker.AiravataUpdateListener;
import org.apache.airavata.datacat.agent.messageBroker.IMessageBroker;
import org.apache.airavata.datacat.agent.util.AgentProperties;
import org.apache.airavata.datacat.agent.util.Constants;
import org.apache.airavata.datacat.agent.util.ThriftUtils;
import org.apache.airavata.datacat.models.Messaging.ExperimentOutputCreatedEvent;
import org.apache.airavata.datacat.models.Messaging.Message;
import org.apache.airavata.datacat.models.Messaging.MessageType;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.junit.Before;
import org.junit.Test;


public class RabbitMQConsumerTest {

    private static final Logger logger = LogManager.getLogger(RabbitMQConsumerTest.class);

    private final String RABBITMQ_HOST = AgentProperties.getInstance().getProperty(Constants.RABBITMQ_HOST, "");
    private final String EXCHANGE_NAME = AgentProperties.getInstance().getProperty(Constants.EXCHANGE_NAME, "");
    private final String BINDING_KEY = AgentProperties.getInstance().getProperty(Constants.BINDING_KEY, "");

    private IMessageBroker messageBroker;

    /**
     * RabbitMQ server should be started to run this test
     */
    @Before
    public void init() {
        //start the RabbitMQConsumer
        messageBroker = new AiravataUpdateListener();
        try {
            messageBroker.startBroker();
            logger.info("Started Message Broker!!!");
            Thread.sleep(4000);
        } catch (Exception e) {
            logger.error("Exception occured while starting the message broker!! " + e);
        }
    }

    @Test
    public void testRabbitMQConsumer() {
        try {
            //publish test data
            publish(MessageType.EXPERIMENT_OUTPUT);

            //wait for the consumer to recieve the message
            Thread.sleep(4000);
        } catch (Exception e) {
            logger.error(e);
        }
        messageBroker.stopBroker();
    }

    /**
     * Test method to publish dummy data to RabbitMQ
     * @param messageType
     * @throws java.io.IOException
     * @throws TException
     */
    public void publish(MessageType messageType)
            throws java.io.IOException, TException {

        //establishing the connection
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RABBITMQ_HOST);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        String DATA_ROOT = AgentProperties.getInstance().getProperty(Constants.DATA_ROOT, "");

        //creating the output created Event
        ExperimentOutputCreatedEvent event = new ExperimentOutputCreatedEvent();
        event.setExperimentId("test");
        event.setOutputPath(DATA_ROOT + "/2H2OOHNCmin.com.out");

        //serializing the event
        byte[] body = ThriftUtils.serializeThriftObject(event);
        Message message = new Message();
        message.setEvent(body);
        message.setMessageId("sad");
        message.setMessageType(messageType);
        message.setUpdatedTime(993344232);
        String routingKey = "*";

        //serializing the message object
        byte[] messageArray = ThriftUtils.serializeThriftObject(message);
        channel.basicPublish(EXCHANGE_NAME, BINDING_KEY, null, messageArray);

        logger.debug(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }

}
