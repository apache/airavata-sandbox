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
package org.apache.airavata.datacat.agent.messageBroker;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import org.apache.airavata.datacat.agent.dispatcher.MonitorDispatcherQueue;
import org.apache.airavata.datacat.agent.util.AgentProperties;
import org.apache.airavata.datacat.agent.util.Constants;
import org.apache.airavata.datacat.agent.util.ThriftUtils;
import org.apache.airavata.datacat.models.OutputMonitorMessage;
import org.apache.airavata.datacat.models.OutputMonitorMessageType;
import org.apache.airavata.datacat.models.Messaging.ExperimentOutputCreatedEvent;
import org.apache.airavata.datacat.models.Messaging.Message;
import org.apache.airavata.datacat.models.Messaging.MessageType;
import org.apache.log4j.LogManager;
import org.apache.thrift.TBase;

public class AiravataUpdateListener implements IMessageBroker {
    private final org.apache.log4j.Logger logger = LogManager.getLogger(AiravataUpdateListener.class);

    private final String BINDING_KEY;
    private final String RABBITMQ_HOST;
    private final String EXCHANGE_NAME;

    private boolean runFileUpdateListener = false;
    private MonitorDispatcherQueue monitorDispatcherQueue;

    /**
     * Initializes the attributes
     */
    public AiravataUpdateListener() {
        RABBITMQ_HOST = AgentProperties.getInstance().getProperty(Constants.RABBITMQ_HOST, "");
        BINDING_KEY = AgentProperties.getInstance().getProperty(Constants.BINDING_KEY, "");
        EXCHANGE_NAME = AgentProperties.getInstance().getProperty(Constants.EXCHANGE_NAME, "");
        monitorDispatcherQueue = MonitorDispatcherQueue.getInstance();
        runFileUpdateListener = true;
    }

    public void startBroker() {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ConnectionFactory factory = new ConnectionFactory();
                    factory.setHost(RABBITMQ_HOST);

                    Connection connection = factory.newConnection();
                    Channel channel = connection.createChannel();

                    channel.exchangeDeclare(EXCHANGE_NAME, "topic");
                    String queueName = channel.queueDeclare().getQueue();

                    channel.basicQos(1);
                    channel.queueBind(queueName, EXCHANGE_NAME, BINDING_KEY);

                    logger.debug("Waiting for messages. To exit press CTRL+C");

                    QueueingConsumer consumer = new QueueingConsumer(channel);
                    channel.basicConsume(queueName, true, consumer);

                    while (runFileUpdateListener) {
                        QueueingConsumer.Delivery delivery = consumer.nextDelivery();

                        Message message = new Message();
                        ThriftUtils.createThriftFromBytes(delivery.getBody(), message);
                        TBase event = null;

                        if (message.getMessageType().equals(MessageType.EXPERIMENT_OUTPUT)) {

                            ExperimentOutputCreatedEvent experimentOutputCreatedEvent = new ExperimentOutputCreatedEvent();
                            ThriftUtils.createThriftFromBytes(message.getEvent(), experimentOutputCreatedEvent);

                            logger.debug(" Message Received with message id '" + message.getMessageId()
                                    + "' and with message type '" + message.getMessageType() + "'  with experiment name " +
                                    experimentOutputCreatedEvent.getExperimentName());

                            event = experimentOutputCreatedEvent;

                            logger.debug(" [x] Received FileInfo Message'");
                            process(experimentOutputCreatedEvent, message.getUpdatedTime());
                            logger.debug(" [x] Done Processing FileInfo Message");
                        } else {
                            logger.debug("Recieved message of type ..." +message.getMessageType());
                        }
                    }
                } catch (Exception e) {
                    logger.error(e);
                }
            }


        })).start();


    }

    @Override
    public void stopBroker() {
        runFileUpdateListener = false;
        logger.info("Shutting down FileUpdateListener...");
    }

    /**
     * This method processe the Experiment Output Created Event that comes from Airavata
     * @param experimentOutputCreatedEvent
     * @param modifiedTime
     * @throws InterruptedException
     */
    private void process(ExperimentOutputCreatedEvent experimentOutputCreatedEvent, long modifiedTime) throws InterruptedException {
        if (experimentOutputCreatedEvent != null) {
            //creating the output monitor message
            OutputMonitorMessage outputMonitorMessage = new OutputMonitorMessage();
            outputMonitorMessage.setExperimentID(experimentOutputCreatedEvent.getExperimentId());
            outputMonitorMessage.setExperimentName(experimentOutputCreatedEvent.getExperimentName());
            outputMonitorMessage.setOutputPath(experimentOutputCreatedEvent.getOutputPath());
            outputMonitorMessage.setOwnerId(experimentOutputCreatedEvent.getOwnerId());
            outputMonitorMessage.setGatewayId(experimentOutputCreatedEvent.getGatewayName());
            outputMonitorMessage.setApplicationName(experimentOutputCreatedEvent.getApplicationName());
            outputMonitorMessage.setHost(experimentOutputCreatedEvent.getHost());
            outputMonitorMessage.setFileMonitorMessageType(OutputMonitorMessageType.FILE_CREATED);

            //Dispatching the message to the queue
            monitorDispatcherQueue.addMsgToQueue(outputMonitorMessage);
            logger.info("FileMonitor Message Added to the queue output path: " + outputMonitorMessage.getOutputPath());
        }
    }
}
