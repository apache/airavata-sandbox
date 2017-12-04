/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.airavata.allocation.manager.messaging;

import org.apache.airavata.common.exception.AiravataException;
import org.apache.airavata.common.exception.ApplicationSettingsException;
import org.apache.airavata.common.utils.ServerSettings;
import org.apache.airavata.common.utils.ThriftUtils;
import org.apache.airavata.messaging.core.MessageContext;
import org.apache.airavata.messaging.core.MessageHandler;
import org.apache.airavata.model.dbevent.DBEventMessage;
import org.apache.airavata.model.dbevent.DBEventMessageContext;
import org.apache.airavata.model.error.DuplicateEntryException;
import org.apache.airavata.model.user.UserProfile;
import org.apache.airavata.allocation.manager.client.AllocationServiceClientFactory;
import org.apache.airavata.allocation.manager.models.*;
import org.apache.airavata.allocation.manager.server.AllocationManagerServer;
import org.apache.airavata.allocation.manager.service.cpi.AllocationRegistryService;
import org.apache.airavata.allocation.manager.utils.ThriftDataModelConversion;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author madrinathapa
 */
public class AllocationServiceDBEventHandler implements MessageHandler {

    private final static Logger log = LoggerFactory.getLogger(AllocationServiceDBEventHandler.class);

    private final AllocationRegistryService.Client allocationManagerClient;

    AllocationServiceDBEventHandler() throws ApplicationSettingsException, AllocationManagerException {
        log.info("Starting Allocation registry client.....");
        allocationManagerClient = AllocationServiceClientFactory.createAllocationRegistryClient(ServerSettings.getSetting(AllocationManagerServer.ALLOCATION_REG_SERVER_HOST), Integer.parseInt(ServerSettings.getSetting(AllocationManagerServer.ALLOCATION_REG_SERVER_PORT)));
    }

    @Override
    public void onMessage(MessageContext messageContext) {

        log.info("New DB Event message to Allocation service.");

        try{

            byte[] bytes = ThriftUtils.serializeThriftObject(messageContext.getEvent());

            DBEventMessage dbEventMessage = new DBEventMessage();
            ThriftUtils.createThriftFromBytes(bytes, dbEventMessage);

            log.info("DB Event message to Allocation service from " + dbEventMessage.getPublisherService());

            DBEventMessageContext dBEventMessageContext = dbEventMessage.getMessageContext();
            try{
                switch (dBEventMessageContext.getPublisher().getPublisherContext().getEntityType()){

                    case USER_PROFILE :

                        log.info("User profile specific DB Event communicated by " + dbEventMessage.getPublisherService());

                        UserProfile  userProfile = new UserProfile();
                        ThriftUtils.createThriftFromBytes(dBEventMessageContext.getPublisher().getPublisherContext().getEntityDataModel(), userProfile);
            
                        userProfile.setUserId(userProfile.getAiravataInternalUserId());
                        UserAllocationDetail user = ThriftDataModelConversion.getUser(userProfile);

                        switch (dBEventMessageContext.getPublisher().getPublisherContext().getCrudType()){

                            case CREATE:
                                log.info("Creating user. User name: " + user.id.getUsername());

                                allocationManagerClient.createAllocationRequest(user);
                                log.debug("User created. User name : " + user.id.getUsername());

                                break;

                            case READ:
                                log.info("Updating user. User name : " + user.id.getUsername());

                                allocationManagerClient.getAllocationRequest(user.id.projectId);
                                log.debug("User updated. User Id : " + user.id.getUsername());

                                break;

                            case UPDATE:
                                log.info("Updating user. User name : " + user.id.getUsername());
                                //To be done
                                log.debug("User updated. User Id : " + user.id.getUsername());

                                break;

                            case DELETE:
                                log.info("Deleting user. User name : " + user.id.getUsername());

                                allocationManagerClient.deleteAllocationRequest(user.id.projectId);
                                log.debug("User deleted. User name : " + user.id.getUsername());

                                break;
                        }
                        break;

                    default: log.error("Handler not defined for " + dBEventMessageContext.getPublisher().getPublisherContext().getEntityType());
                }
            } catch (DuplicateEntryException ex) {
                // log this exception and proceed (do nothing)
                // this exception is thrown mostly when messages are re-consumed in case of some exception, hence ignore
                log.warn("DuplicateEntryException while consuming db-event message, ex: " + ex.getMessage(), ex);
            }

            log.info("Sending ack. Message Delivery Tag : " + messageContext.getDeliveryTag());
            AllocationServiceDBEventMessagingFactory.getDBEventSubscriber().sendAck(messageContext.getDeliveryTag());

        } catch (TException e) {
            log.error("Error processing message.", e);
        } catch (ApplicationSettingsException e) {
            log.error("Error fetching application settings.", e);
        } catch (AiravataException e) {
            log.error("Error sending ack. Message Delivery Tag : " + messageContext.getDeliveryTag(), e);
        }
    }
}