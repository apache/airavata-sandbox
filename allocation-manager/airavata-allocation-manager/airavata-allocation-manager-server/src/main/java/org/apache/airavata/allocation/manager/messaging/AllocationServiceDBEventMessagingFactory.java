/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.airavata.allocation.manager.messaging;


import org.apache.airavata.common.exception.AiravataException;
import org.apache.airavata.common.utils.DBEventManagerConstants;
import org.apache.airavata.common.utils.DBEventService;
import org.apache.airavata.messaging.core.MessageContext;
import org.apache.airavata.messaging.core.MessagingFactory;
import org.apache.airavata.messaging.core.Publisher;
import org.apache.airavata.messaging.core.Subscriber;
import org.apache.airavata.model.dbevent.DBEventMessage;
import org.apache.airavata.model.dbevent.DBEventMessageContext;
import org.apache.airavata.model.dbevent.DBEventSubscriber;
import org.apache.airavata.model.dbevent.DBEventType;
import org.apache.airavata.model.messaging.event.MessageType;
import org.apache.airavata.allocation.manager.models.AllocationManagerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *
 * @author madrinathapa
 */
public class AllocationServiceDBEventMessagingFactory {

    private final static Logger log = LoggerFactory.getLogger(AllocationServiceDBEventMessagingFactory.class);

    private static Publisher dbEventPublisher;

    private static Subscriber allocationServiceDBEventSubscriber;

    /**
     * Get publisher for DB Event queue
     * Change access specifier as required
     * @return
     * @throws AiravataException
     */
    private static Publisher getDBEventPublisher() throws AiravataException {
        if(null == dbEventPublisher){
            synchronized (AllocationServiceDBEventMessagingFactory.class){
                if(null == dbEventPublisher){
                    log.info("Creating DB Event publisher.....");
                    dbEventPublisher = MessagingFactory.getDBEventPublisher();
                    log.info("DB Event publisher created");
                }
            }
        }
        return dbEventPublisher;
    }

    public static Subscriber getDBEventSubscriber() throws AiravataException, AllocationManagerException {
        if(null == allocationServiceDBEventSubscriber){
            synchronized (AllocationServiceDBEventMessagingFactory.class){
                if(null == allocationServiceDBEventSubscriber){
                    log.info("Creating DB Event publisher.....");
                    allocationServiceDBEventSubscriber = MessagingFactory.getDBEventSubscriber(new AllocationServiceDBEventHandler(), "allocation");
                    log.info("DB Event publisher created");
                }
            }
        }
        return allocationServiceDBEventSubscriber;
    }

    /**
     * Register Allocation Manager service with stated publishers
     * @param publishers
     * @return
     * @throws AiravataException
     */
    public static boolean registerAllocationServiceWithPublishers(List<String> publishers) throws AiravataException {

        for(String publisher : publishers){

            log.info("Sending service discovery message. Publisher : " + publisher + ", Subscriber : " + "allocation");

            DBEventSubscriber dbEventSubscriber = new DBEventSubscriber("allocation");
            DBEventMessageContext dbEventMessageContext = new DBEventMessageContext();
            dbEventMessageContext.setSubscriber(dbEventSubscriber);

            DBEventMessage dbEventMessage = new DBEventMessage(DBEventType.SUBSCRIBER, dbEventMessageContext, publisher);

            MessageContext messageContext = new MessageContext(dbEventMessage, MessageType.DB_EVENT, "", "");

            getDBEventPublisher().publish(messageContext, DBEventManagerConstants.getRoutingKey(DBEventService.DB_EVENT.toString()));

        }

        return true;
    }

}

