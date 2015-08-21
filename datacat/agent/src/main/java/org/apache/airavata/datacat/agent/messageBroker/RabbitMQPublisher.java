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

import org.apache.airavata.datacat.agent.util.AgentProperties;
import org.apache.airavata.datacat.agent.util.Constants;
import org.apache.airavata.datacat.agent.util.ThriftUtils;
import org.apache.airavata.datacat.models.Messaging.ExperimentOutputParsedEvent;
import org.apache.airavata.datacat.models.Messaging.Message;
import org.apache.airavata.datacat.models.Messaging.MessageLevel;
import org.apache.airavata.datacat.models.Messaging.MessageType;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RabbitMQPublisher {

    private static Logger log = LoggerFactory.getLogger(RabbitMQPublisher.class);
    private final String BINDING_KEY;
    private final String RABBITMQ_HOST;
    private final String EXCHANGE_NAME;

    private RabbitMQProducer rabbitMQProducer;

    /**
     * Initializing the RabbitMQ related attributes
     * @throws Exception
     */
    public RabbitMQPublisher() throws Exception {
        RABBITMQ_HOST = AgentProperties.getInstance().getProperty(Constants.RABBITMQ_HOST, "");
        BINDING_KEY = AgentProperties.getInstance().getProperty(Constants.BINDING_KEY, "");
        EXCHANGE_NAME = AgentProperties.getInstance().getProperty(Constants.EXCHANGE_NAME, "");

        rabbitMQProducer = new RabbitMQProducer(RABBITMQ_HOST, EXCHANGE_NAME);
        rabbitMQProducer.open();
    }

    /**
     * publish the data products to Airavata
     * @param experimentOutputParsedEvent
     */
    public void publish(ExperimentOutputParsedEvent experimentOutputParsedEvent){
        try {
            log.info("Publishing status to datacat rabbitmq...");
            byte[] body = ThriftUtils.serializeThriftObject(experimentOutputParsedEvent);
            Message message = new Message();
            message.setEvent(body);
            message.setMessageId(experimentOutputParsedEvent.getDocumentID());
            message.setMessageType(MessageType.OUTPUT_PARSED);
            message.setUpdatedTime(System.currentTimeMillis());
            String routingKey = experimentOutputParsedEvent.getExperimentId();
            byte[] messageBody = ThriftUtils.serializeThriftObject(message);
            rabbitMQProducer.send(messageBody, routingKey);
        } catch (TException e) {
            String msg = "Error while deserializing the object";
            log.error(msg, e);
        } catch (Exception e) {
            String msg = "Error while sending to rabbitmq host";
            log.error(msg, e);
        }
    }
}
