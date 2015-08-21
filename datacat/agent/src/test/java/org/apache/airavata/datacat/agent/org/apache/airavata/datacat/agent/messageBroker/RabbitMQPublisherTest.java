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

import org.apache.airavata.datacat.agent.messageBroker.AiravataUpdateListener;
import org.apache.airavata.datacat.agent.messageBroker.IMessageBroker;
import org.apache.airavata.datacat.agent.messageBroker.RabbitMQPublisher;
import org.apache.airavata.datacat.models.Messaging.ExperimentOutputParsedEvent;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class RabbitMQPublisherTest {
    private static final Logger logger = LogManager.getLogger(RabbitMQPublisherTest.class);

    private RabbitMQPublisher rabbitMQPublisher;
    private IMessageBroker messageBroker;

    @Before
    public void setup() {
        try {
            rabbitMQPublisher = new RabbitMQPublisher();
            logger.info("Started the RabbitMQPublisher");

            //start the RabbitMQConsumer
            messageBroker = new AiravataUpdateListener();
            messageBroker.startBroker();
            logger.info("Started Message Broker!!!");
            Thread.sleep(4000);

        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Test
    public void testRabbitMQPublisher() {
        rabbitMQPublisher.publish(getDummyOutputParsedEvent());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            logger.error(e);
        }finally {
            messageBroker.stopBroker();
        }
    }

    /**
     * a test method to return dummy data
     * @return ExperimentOutputParsedEvent
     */
    public ExperimentOutputParsedEvent getDummyOutputParsedEvent() {
        ExperimentOutputParsedEvent experimentOutputParsedEvent = new ExperimentOutputParsedEvent();
        experimentOutputParsedEvent.setExperimentId("3343243234");
        experimentOutputParsedEvent.setStatus("success");
        experimentOutputParsedEvent.setDocumentID("sadas3qe34");
        return experimentOutputParsedEvent;
    }
}
